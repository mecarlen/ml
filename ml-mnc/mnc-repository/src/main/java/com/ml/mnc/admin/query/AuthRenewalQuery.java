package com.ml.mnc.admin.query;

import org.apache.ibatis.annotations.Param;

import com.ml.base.query.BaseQuery;
import com.ml.mnc.admin.domain.AuthRenewalVO;

/**
 * <pre>
 * 认证续签
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月23日 上午10:53:41
 */
public interface AuthRenewalQuery extends BaseQuery<Long, AuthRenewalVO> {
	/**
	 * <pre>
	 * 根据sendUrl查询
	 * 
	 * </pre>
	 * 
	 * @param sendUrlId long
	 * @return AuthRenewalVO
	 */
	AuthRenewalVO selectBySendUrlId(@Param("sendUrlId")long sendUrlId);
}
