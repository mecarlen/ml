package com.ml.mnc.admin.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ml.base.service.impl.BaseServiceImpl;
import com.ml.mnc.admin.domain.AuthRenewalEntity;
import com.ml.mnc.admin.domain.AuthRenewalVO;
import com.ml.mnc.admin.query.AuthRenewalQuery;
import com.ml.mnc.admin.repository.AuthRenewalRepository;
import com.ml.mnc.admin.service.AuthRenewalService;

/**
 * <pre>
 * 认证续签
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月25日 下午4:14:09
 */
@Service("authRenewalService")
public class AuthRenewalServiceImpl
		extends BaseServiceImpl<Long, AuthRenewalEntity, AuthRenewalVO, AuthRenewalRepository, AuthRenewalQuery>
		implements AuthRenewalService {
	@Resource
	public void setAuthRenewalRepository(AuthRenewalRepository authRenewalRepository) {
		this.setRepository(authRenewalRepository);
	}
	@Resource
	public void setAuthRenewalQuery(AuthRenewalQuery authRenewalQuery) {
		this.setQuery(authRenewalQuery);
	}
}
