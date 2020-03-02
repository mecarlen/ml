package com.ml.mnc.admin.domain;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ml.base.domain.BaseVO;
import com.ml.mnc.admin.AuthRenewal;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 认证续签
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月23日 上午9:42:59
 */
@Setter
@Getter
public class AuthRenewalVO extends BaseVO<Long, AuthRenewalEntity> implements AuthRenewal {
	private SendUrlEntity sendUrl;
	private String appCode;
	private String queueName;
	private String routeKey;
	private Long vccId;
	private String businessType;
	private String sendUrlAddress;
	private String renewalUrlAddress;
	private String authDataField;
	private String remark;
	
	@Override
	public AuthRenewalVO id(Long id) {
		this.setId(id);
		return this;
	}
	@Override
	public AuthRenewalVO state(State state) {
		if(null!=state) {
			this.setYn(state.code());
		}
		return this;
	}
	@Override
	public AuthRenewalVO createTime(Date createTime) {
		if(null!=createTime) {
			this.setCreateTime(new Date(createTime.getTime()));
		}
		return this;
	}
	@Override
	public AuthRenewalVO updateTime(Date updateTime) {
		if(null!=updateTime) {
			this.setUpdateTime(new Date(updateTime.getTime()));
		}
		return this;
	}
	@Override
	public AuthRenewalEntity toEntity() {
		return AuthRenewalEntity.getInstance(this);
	}
	
	final public static AuthRenewalVO getInstance(AuthRenewal authRenewal) {
		if(null == authRenewal) {
			return null;
		}
		AuthRenewalVO target = new AuthRenewalVO();
		BeanUtils.copyProperties(authRenewal, target, new String[] {"sendUrl"});
		if(null!=authRenewal.getSendUrl()) {
			target.setSendUrl(SendUrlEntity.getInstance(authRenewal.getSendUrl()));
		}
		return target;
	}
}
