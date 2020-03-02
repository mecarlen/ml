package com.ml.mnc.dingding.domain;

import org.springframework.beans.BeanUtils;

import com.ml.base.rpc.AbstractMQ;
import com.ml.mnc.admin.domain.AppVO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

/**
 * <pre>
 * 钉钉消息MQ
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月9日 上午10:08:34
 */
@Setter
@Getter
@Builder
public class DingdingMQ extends AbstractMQ {
	private String appCode;
	private Long vccId;
	private String businessId;
	private String businessType;
	private Long businessAcceptTimeMills;
	private String msgTitle;
	private String msgContent;
	private String dingdingUrl;
	private Long specifySendTimeMills;
	private String retryInterval;

	@Tolerate
	public DingdingMQ() {
	}

	public DingdingMsgVO toMsg(AppVO app) {
		DingdingMsgVO target = new DingdingMsgVO();
		BeanUtils.copyProperties(this, target, new String[] { "exchange" });
		if (null != app) {
			target.setApp(app);
			target.setPlCode(app.getPlCode());
		}
		return target;
	}
}
