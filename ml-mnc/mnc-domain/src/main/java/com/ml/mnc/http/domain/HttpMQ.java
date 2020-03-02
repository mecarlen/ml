package com.ml.mnc.http.domain;

import org.springframework.beans.BeanUtils;

import com.ml.base.rpc.AbstractMQ;
import com.ml.mnc.admin.domain.AppVO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

/**
 * <pre>
 * http消息体
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月16日 下午2:36:36
 */
@Setter
@Getter
@Builder
public class HttpMQ extends AbstractMQ {
	private String appCode;
	private Long vccId;
	private String businessId;
	private String businessType;
	private String msgContent;
	private Long businessAcceptTimeMills;
	private Long specifySendTimeMills;
	private String retryInterval;
	private String sendUrl;
	private String successFieldCode;
	private String authJson;
	private String authExpireTimeMills;
	private Boolean autoRetry;

	@Tolerate
	public HttpMQ() {
		
	}
	
	/**
	 * <pre>
	 * 消息体解析
	 * 
	 * </pre>
	 * @param app App
	 * @return HttpMsgVO
	 * */
	public HttpMsgVO toMsg(AppVO app) {
		HttpMsgVO target = new HttpMsgVO();
		BeanUtils.copyProperties(this, target, new String[] {"exchange","messageTimeStamp"});
		if (null != app) {
			target.setApp(app);
			target.setPlCode(app.getPlCode());
		}
		return target;
	}
}
