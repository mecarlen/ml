package com.ml.mnc.admin.domain;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ml.base.domain.BaseEntity;
import com.ml.mnc.admin.AuthRenewal;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 认证续签
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月23日 上午9:42:27
 */
@Setter
@Getter
public class AuthRenewalEntity extends BaseEntity<Long, AuthRenewalVO> implements AuthRenewal {
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
	public AuthRenewalEntity id(Long id) {
		this.setId(id);
		return this;
	}

	@Override
	public AuthRenewalEntity state(State state) {
		if(null!=state) {
			this.setYn(state.code());
		}
		return this;
	}

	@Override
	public AuthRenewalEntity createTime(Date createTime) {
		if(null!=createTime) {
			this.setCreateTime(new Date(createTime.getTime()));
		}
		return this;
	}

	@Override
	public AuthRenewalEntity updateTime(Date updateTime) {
		if(null!=updateTime) {
			this.setUpdateTime(new Date(updateTime.getTime()));
		}
		return this;
	}

	@Override
	public AuthRenewalVO toVO() {
		return AuthRenewalVO.getInstance(this);
	}

	final public static AuthRenewalEntity getInstance(AuthRenewal authRenewal) {
		if(null == authRenewal) {
			return null;
		}
		AuthRenewalEntity target = new AuthRenewalEntity();
		BeanUtils.copyProperties(authRenewal, target, new String[] {"sendUrl"});
		if(null!=authRenewal.getSendUrl()) {
			target.setSendUrl(SendUrlEntity.getInstance(authRenewal.getSendUrl()));
		}
		return target;
	}
}
