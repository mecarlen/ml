package com.ml.mnc.admin.query;

import org.apache.ibatis.annotations.Param;

import com.ml.base.query.BaseQuery;
import com.ml.mnc.admin.domain.SendUrlVO;

/**
 * <pre>
 * 推送URL
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月23日 上午10:52:49
 */
public interface SendUrlQuery extends BaseQuery<Long, SendUrlVO> {
	/**
	 * <pre>
	 * 根据URL地址查询
	 * 
	 * </pre>
	 * 
	 * @param urlAddress String
	 * @return SendUrlVO
	 */
	SendUrlVO selectByUrlAddress(@Param("urlAddress")String urlAddress);
}
