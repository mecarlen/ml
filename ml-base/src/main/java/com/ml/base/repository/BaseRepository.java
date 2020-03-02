package com.ml.base.repository;

import java.io.Serializable;

import com.ml.base.domain.BaseEntity;

/**
 * <pre>
 * 通用仓储接口
 * 
 * 描述
 * 	1、定义仓储的通用方法
 * 
 * @param Id
 *            实体唯一标识类型，要求是实现Serializable接口的类型,推荐用String
 * @param E
 *            具体Entity类
 * </pre>
 * 
 * @author mecarlen 2017年11月21日 下午4:53:22
 */
public interface BaseRepository<ID extends Serializable, E extends BaseEntity<ID, ?>> {
	/**
	 * <pre>
	 * 创建
	 * 
	 * </pre>
	 * 
	 * @param e
	 *            E 实体实例
	 */
	void insert(E e);

	/**
	 * <pre>
	 * 更新
	 * 
	 * </pre>
	 * 
	 * @param e
	 *            E 实体实例
	 * @return boolean
	 */
	boolean update(E e);

	/**
	 * <pre>
	 * 删除
	 * 
	 * </pre>
	 * 
	 * @param e
	 *            E 实体实例
	 * @return boolean
	 */
	boolean delete(E e);
	
}
