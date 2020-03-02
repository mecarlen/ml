package com.ml.mnc.admin.query;

import org.apache.ibatis.annotations.Param;

import com.ml.base.query.BaseQuery;
import com.ml.mnc.admin.domain.AppVO;

/**
 * <pre>
 * 应用信息
 * 
 * </pre>
 * @author mecarlen.Wang 2019年9月5日 下午7:36:04
 */
public interface AppQuery extends BaseQuery<Long, AppVO>{
	/**
	 * <pre>
	 * 根据应用编码查询
	 * 
	 * </pre>
	 * @param appCode String 
	 * @return AppVO
	 * */
	AppVO selectByAppCode(@Param("appCode") String appCode);
}
