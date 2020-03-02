package com.ml.mnc.admin.rpc;

import javax.annotation.Resource;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.ml.mnc.dingding.domain.DingdingMQ;

/**
 * <pre>
 * 钉钉MQ生产
 * 
 * </pre>
 * @author mecarlen.Wang 2020年3月2日 下午4:13:05
 */
@Component
public class DingdingMsgProducer {

    @Value("${rabbitmq.notification.exchange}")
    private String exchange;

    @Value("${rabbitmq.notification.routingKey}")
    private String routingKey;

    @Value("${rabbitmq.notification.url}")
    private String url;

    @Resource
    private AmqpTemplate rabbitTemplate;

    public void send(String title, String content) {
        DingdingMQ dingding = new DingdingMQ();

        dingding.setAppCode("MNC-ADMIN");
        dingding.setBusinessId(String.valueOf(System.currentTimeMillis()));
        dingding.setMsgTitle(title);
        dingding.setMsgContent(content);
        dingding.setDingdingUrl(url);

        rabbitTemplate.send(
                exchange,
                routingKey,
                new Message(JSON.toJSONBytes(dingding), new MessageProperties())
        );
    }
}
