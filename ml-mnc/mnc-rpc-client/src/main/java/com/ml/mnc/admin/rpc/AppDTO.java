package com.ml.mnc.admin.rpc;

import com.ml.base.rpc.BaseDTO;
import com.ml.mnc.admin.App;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

/**
 * <pre>
 * 应用
 * 
 * </pre>
 * @author mecarlen.Wang 2019年9月9日 上午10:25:35
 */
@Setter
@Getter
@Builder
public class AppDTO extends BaseDTO<Long> implements App {
	private String plCode;
	private String plName;
	private String appCode;
	private String appName;
	private String ownerErp;
	private String ownerName;
	private String dingding;
	private String memberMobiles;
	private String memberEmails;
	private Integer alarmStatus;
	private Boolean needCallback;
	private String remark;
	
	@Tolerate
	public AppDTO() {}
}
