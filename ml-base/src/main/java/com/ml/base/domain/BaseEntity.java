package com.ml.base.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * Entity基类
 * 
 * 描述
 * 	1、集群公共属性
 * 
 * </pre>
 * 
 * @author mecarlen.wang 2018年4月12日 下午3:02:44
 */
@Setter
@Getter
public abstract class BaseEntity<ID extends Serializable, V extends BaseVO<ID, ?>> implements Entity<ID> {
	protected ID id;
	protected Integer yn;
	protected Date createTime;
	protected Date updateTime;

	/**
	 * <pre>
	 * 给Entity唯一标示赋值方法,在BaseServiceImpl用到,此方法其子类必须实现
	 * 
	 * </pre>
	 * @param id ID
	 * @return BaseEntity<ID,V>唯一标示类型
	 */
	public abstract BaseEntity<ID, V> id(ID id);

	/**
	 * <pre>
	 * 给Entity设置状态,此方法其子类必须实现
	 * 
	 * </pre>
	 * 
	 * @param state EnumState
	 * @return BaseEntity<ID, V>
	 */
	public abstract BaseEntity<ID, V> state(final State state);

	/**
	 * <pre>
	 * 给Entity创建时间赋值方法，在CommonServiceImpl用到,此方法其子类必须实现
	 * 
	 * </pre>
	 * 
	 * @param createTime Date 创建时间
	 * @return BaseEntity<ID, V>
	 */
	public abstract BaseEntity<ID, V> createTime(Date createTime);

	/**
	 * <pre>
	 * 给Entity更新时间赋值方法，在BaseServiceImpl用到,此方法其子类必须实现
	 * 
	 * </pre>
	 * 
	 * @param updateTime Date 更新时间
	 * @return BaseEntity<ID, V>
	 */
	public abstract BaseEntity<ID, V> updateTime(Date updateTime);

	/**
	 * <pre>
	 * Entity转换成VO方法,在CommonServiceImpl用到,此方法其子类必须实现
	 * 
	 * </pre>
	 * 
	 * @return V 具体VO类型
	 */
	public abstract V toVO();
}
