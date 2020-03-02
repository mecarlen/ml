package com.ml.base.service;

import java.io.Serializable;
import java.util.List;

import com.ml.base.SystemException;
import com.ml.base.domain.BaseVO;
import com.ml.base.query.PageModel;

/**
 * <pre>
 * 通用service
 * 
 * 描述:
 * 1、基础核心业务逻辑服务
 * 2、为具体领域服务提供底层通用逻辑
 * </pre>
 * 
 * @author mecarlen 2017年11月21日 下午5:13:42
 */
public interface BaseService<ID extends Serializable, V extends BaseVO<ID, ?>> {
	/**
	 * <pre>
	 * 创建
	 * 
	 * </pre>
	 * 
	 * @param v V具体ValueObject类型,其必须继承ValueObject<?,?>
	 * @return ID ValueObject唯一标示
	 * @throws SystemException
	 */
	ID create(V v) throws SystemException;

	/**
	 * <pre>
	 * 更新
	 * 
	 * </pre>
	 * 
	 * @param v V 具体ValueObject类型
	 * @return boolean
	 * @throws SystemException
	 */
	boolean modify(V v) throws SystemException;

	/**
	 * <pre>
	 * 删除
	 * 
	 * </pre>
	 * 
	 * @param v V 具体ValueObject类型
	 * @return boolean
	 * @throws SystemException
	 */
	boolean remove(V v) throws SystemException;

	/**
	 * <pre>
	 * 取所有
	 * 
	 * </pre>
	 * 
	 * @return List<V>
	 */
	List<V> find();

	/**
	 * <pre>
	 * 根据主键取
	 * 
	 * </pre>
	 * 
	 * @param id ID ValueObject唯一标示
	 * @return V 具体ValueObject
	 */
	V find(ID id);

	/**
	 * <pre>
	 * 查询列表
	 * 
	 * </pre>
	 * 
	 * @param conditions V
	 * @return List<V>
	 */
	List<V> find(V conditions);

	/**
	 * <pre>
	 * 分页查询
	 * 
	 * </pre>
	 * 
	 * @param conditions V
	 * @return Page<V>
	 */
	PageModel<V> findByPage(V conditions);

}
