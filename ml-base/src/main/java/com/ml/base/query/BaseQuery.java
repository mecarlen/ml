package com.ml.base.query;

import java.io.Serializable;
import java.util.List;

import com.ml.base.domain.BaseVO;

/**
 * <pre>
 * 通用查询
 * 
 * </pre>
 * 
 * @author mecarlen 2018年9月6日 下午4:03:00
 */
public interface BaseQuery<ID extends Serializable,V extends BaseVO<ID,?>> {
	/**
	 * <pre>
	 * 根据主键取
	 * 
	 * </pre>
	 * 
	 * @param id
	 *            Id 实体实例id
	 * @return V VO实例
	 */
	V selectById(ID id);

	/**
	 * <pre>
	 * 取所有
	 * 
	 * </pre>
	 * 
	 * @return List<V> VO实例列表
	 */
	List<V> selectAll();
	
	/**
	 * <pre>
	 * 按条件查询
	 * 
	 * </pre>
	 * @param conditions V
	 * @return List<V>
	 * */
	List<V> select(V conditions);
}
