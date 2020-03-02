package com.ml.base.rpc;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import com.alibaba.fastjson.parser.deserializer.ExtraTypeProvider;
import com.rabbitmq.client.Channel;

import com.ml.base.BussinessException;
import com.ml.base.SystemException;

/**
 * <pre>
 * rabbitmq抽象监听器
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年6月27日 下午3:53:07
 */
public abstract class AbstractMQListener<T extends AbstractMQ> implements ChannelAwareMessageListener {
	private static Logger RABBITMQ_LISTENER_LOG = LoggerFactory.getLogger(AbstractMQListener.class);
	/** clazz */
	private Class<T> objectClass;
	protected ExtraProcessor extraProcessor = new JSONExtraProcessor();

	@SuppressWarnings("unchecked")
	public AbstractMQListener() {
		Class<?> thisClass = getClass();
		// 通过泛型获得继承类的类原型
		objectClass = (Class<T>) ((ParameterizedType) thisClass.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public void before(Message message, Channel channel) {
		// TODO
	}

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		String messageJson = new String(message.getBody(), "utf-8");
		RABBITMQ_LISTENER_LOG.debug("----->" + messageJson);
		// before
		before(message, channel);
		boolean consumerRes = false;
		try {
			// consumer
			T obj = JSON.parseObject(messageJson, objectClass, extraProcessor, Feature.AllowComment);
			if (null == obj) {
				// 未知消息，直接签收
				RABBITMQ_LISTENER_LOG.warn("exceptionMessageBody: exchange="
						+ message.getMessageProperties().getReceivedExchange() + ",routeKey="
						+ message.getMessageProperties().getReceivedRoutingKey() + ",message=" + messageJson);
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				return;
			}
			obj.setExchange(message.getMessageProperties().getReceivedExchange());
			obj.setQueueName(message.getMessageProperties().getConsumerQueue());
			obj.setRouteKey(message.getMessageProperties().getReceivedRoutingKey());
			obj.setMessageTimeStamp(message.getMessageProperties().getTimestamp());
			consumerRes = onMessage(obj);
		} catch (JSONException ex) {
			RABBITMQ_LISTENER_LOG.error("JSONException,message:{}", messageJson, ex);
		} catch (SystemException ex) {
			RABBITMQ_LISTENER_LOG.error("systemException", ex);
		} catch (BussinessException ex) {
			RABBITMQ_LISTENER_LOG.error("bussinessException", ex);
		} catch (Exception ex) {
			RABBITMQ_LISTENER_LOG.error("unknowException", ex);
		}
		// 执行后续操作
		after(message, channel, consumerRes);
	}
	/** 
	 * <pre>
	 * 消息处理
	 * 
	 * </pre>
	 * @param message T
	 * @return boolean 
	 *  */
	abstract public boolean onMessage(T message) throws Exception;

	/**
	 * <pre>
	 * 消费后处理
	 * 
	 * </pre>
	 */
	public void after(Message message, Channel channel, boolean consumerSuccess) throws Exception {
		if (consumerSuccess) {
			RABBITMQ_LISTENER_LOG
					.info("normalMessageBody: exchange=" + message.getMessageProperties().getReceivedExchange()
							+ ",routeKey=" + message.getMessageProperties().getReceivedRoutingKey() + ",message="
							+ new String(message.getBody(), "utf-8"));
			// 正常消费则签收
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} else {
			// 异常消费则扔回队列
			RABBITMQ_LISTENER_LOG
					.warn("exceptionMessageBody: exchange=" + message.getMessageProperties().getReceivedExchange()
							+ ",routeKey=" + message.getMessageProperties().getReceivedRoutingKey() + ",message="
							+ new String(message.getBody(), "utf-8"));
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}
	}

	/**
	 * <pre>
	 * 处理json字符串包含转义字符类型
	 * 
	 * </pre>
	 */
	public class JSONExtraProcessor implements ExtraProcessor, ExtraTypeProvider {
		private ArrayList<String> needAttrStrs = new ArrayList<String>(Arrays.asList("msgContent"));

		@Override
		public Type getExtraType(Object object, String key) {
			String objNm = object.getClass().getSimpleName().toLowerCase();
			String objAttrFormat="%s.%s";
			if (needAttrStrs.contains(key) || needAttrStrs.contains(String.format(objAttrFormat, objNm, key))) {
				return String.class;
			}
			return null;
		}

		@Override
		public void processExtra(Object object, String key, Object value) {

		}

	}

}
