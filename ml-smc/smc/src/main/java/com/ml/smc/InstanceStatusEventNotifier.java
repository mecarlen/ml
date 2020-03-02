package com.ml.smc;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ml.util.date.DateConvertor;
import com.ml.util.date.EnumDateFormat;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * <pre>
 * 服务状态变更通知
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年7月31日 下午5:54:16
 */
@Slf4j
@Component
public class InstanceStatusEventNotifier extends AbstractStatusChangeNotifier {
	@Value("${spring.profiles.active}")
	private String systemEnv;
	@Resource
	private InstanceStatusEventMsgProducer instanceStatusEventMsgProducer;

	public InstanceStatusEventNotifier(InstanceRepository repositpry) {
		super(repositpry);
	}

	@Override
	protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {

		return Mono.fromRunnable(() -> {
			InstanceStatusEventMsg msg = new InstanceStatusEventMsg();
			if (event instanceof InstanceStatusChangedEvent) {
				log.info("Instance {} ({}) is {}", instance.getRegistration().getName(), event.getInstance(),
						((InstanceStatusChangedEvent) event).getStatusInfo().getStatus());
				String status = ((InstanceStatusChangedEvent) event).getStatusInfo().getStatus();
				msg.setAppCode(instance.getRegistration().getName());
				msg.setBusinessId(msg.getAppCode() + "-" + status + "-" + System.currentTimeMillis());
				msg.setDingdingUrl(null != getGroupDingdingUrl(instance) ? getGroupDingdingUrl(instance) : null);
				msg.setBusinessType(status);
				// 标题
				StringBuilder title = new StringBuilder("");
				title.append(systemEnv).append(":").append(msg.getAppCode()).append(":").append(status);
				msg.setMsgTitle(title.toString());
				// 正文
				StringBuilder content = new StringBuilder("");
				content.append("instanceIp:" + instance.getRegistration().getServiceUrl()).append("\n");
				content.append(
						"changeTime:" + DateConvertor.long2Str(System.currentTimeMillis(), EnumDateFormat.TIME_YMDHMS));
				msg.setMsgContent(content.toString());
				instanceStatusEventMsgProducer.send(msg);
			} else {
				log.info("Instance {} ({}) {}", instance.getRegistration().getName(), event.getInstance(),
						event.getType());
			}
		});
	}

	/**
	 * <pre>
	 * 获取研发组钉钉URL
	 * 
	 * </pre>
	 * 
	 * @param instance Instance
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	String getGroupDingdingUrl(Instance instance) {
		if (null == instance.getInfo() || null == instance.getInfo().getValues().get("group")) {
			log.warn("{} instance no config app info", instance.getRegistration().getName());
			return null;
		}
		Map<String,Object> groupInfo = (Map<String,Object>)instance.getInfo().getValues().get("group");
		
    	if(null == groupInfo.get("dingding")) {
    		log.warn("{} instance no config app.group.dingding info",instance.getRegistration().getName());
    		return null;
    	}
		return String.valueOf(groupInfo.get("dingding"));
	}
}
