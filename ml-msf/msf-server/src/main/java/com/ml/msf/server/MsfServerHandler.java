package com.ml.msf.server;

import java.lang.reflect.Method;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.ml.msf.entity.MsfRequest;
import com.ml.msf.entity.MsfResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * <b></b>
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2020年3月25日 下午5:18:11
 */
@Slf4j
public class MsfServerHandler extends ChannelInboundHandlerAdapter {
	private final Map<String, Object> serviceMap;

	public MsfServerHandler(Map<String, Object> serviceMap) {
		this.serviceMap = serviceMap;
	}

	public void channelActive(ChannelHandlerContext ctx) {
		log.info("客户端连接成功!" + ctx.channel().remoteAddress());
	}

	public void channelInactive(ChannelHandlerContext ctx) {
		log.info("客户端断开连接!{}", ctx.channel().remoteAddress());
		ctx.channel().close();
	}

	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		MsfRequest request = JSON.parseObject(msg.toString(), MsfRequest.class);

		if ("heartBeat".equals(request.getMethodName())) {
			log.info("客户端心跳信息..." + ctx.channel().remoteAddress());
		} else {
			log.info("RPC客户端请求接口:" + request.getClassName() + "   方法名:" + request.getMethodName());
			MsfResponse response = new MsfResponse();
			response.setRequestId(request.getId());
			try {
				Object result = this.handler(request);
				response.setData(result);
			} catch (Throwable e) {
				e.printStackTrace();
				response.setCode(1);
				response.setError_msg(e.toString());
				log.error("RPC Server handle request error", e);
			}
			ctx.writeAndFlush(response);
		}
	}

	/**
	 * <pre>
	 * <b>通过反射，执行本地方法</b>
	 * 
	 * </pre>
	 * @param request
	 * @return
	 * @throws Throwable
	 */
	private Object handler(MsfRequest request) throws Throwable {
		String className = request.getClassName();
		Object serviceBean = serviceMap.get(className);

		if (serviceBean != null) {
			Class<?> serviceClass = serviceBean.getClass();
			String methodName = request.getMethodName();
			Class<?>[] parameterTypes = request.getParameterTypes();
			Object[] parameters = request.getParameters();

			Method method = serviceClass.getMethod(methodName, parameterTypes);
			method.setAccessible(true);
			return method.invoke(serviceBean, getParameters(parameterTypes, parameters));
		} else {
			throw new Exception("未找到服务接口,请检查配置!:" + className + "#" + request.getMethodName());
		}
	}

	/**
	 * <pre>
	 * <b>获取参数列表</b>
	 * 
	 * </pre>
	 * 
	 * @param parameterTypes
	 * @param parameters
	 * @return
	 */
	private Object[] getParameters(Class<?>[] parameterTypes, Object[] parameters) {
		if (parameters == null || parameters.length == 0) {
			return parameters;
		} else {
			Object[] new_parameters = new Object[parameters.length];
			for (int i = 0; i < parameters.length; i++) {
				new_parameters[i] = JSON.parseObject(parameters[i].toString(), parameterTypes[i]);
			}
			return new_parameters;
		}
	}

	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.ALL_IDLE) {
				log.info("客户端已超过60秒未读写数据,关闭连接.{}", ctx.channel().remoteAddress());
				ctx.channel().close();
			}
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		log.info(cause.getMessage());
		ctx.close();
	}
}
