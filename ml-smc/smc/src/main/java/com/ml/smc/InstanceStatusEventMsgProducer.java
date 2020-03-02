package com.ml.smc;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 实例状态变更消息生产者
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年10月21日 下午2:31:11
 */
@Slf4j
@Component
public class InstanceStatusEventMsgProducer {
    @Resource
    private AmqpTemplate dingDingRabbitTemplate;
    @Value("${mnc.default.dingdingUrl}")
    private String defaultDingdingUrl;

    public void send(InstanceStatusEventMsg msg) {
        log.info("sendInstanceStatusEventMsg begin,appCode:{},mqBody:{}", msg.getAppCode(), JSON.toJSONString(msg));
        try {
            if(StringUtils.isBlank(msg.getDingdingUrl())) {
                msg.setDingdingUrl(defaultDingdingUrl);
            }
            dingDingRabbitTemplate.convertAndSend(JSON.toJSONString(msg));
        } catch (Exception ex) {
            log.error("sendInstanceStatusEventMsgFailre,appCode:{},mqBody:{}", msg.getAppCode(), JSON.toJSONString(msg), ex);
        }
        log.info("sendInstanceStatusEventMsg end,appCode:{},mqBody:{}", msg.getAppCode(), JSON.toJSONString(msg));
    }
}
