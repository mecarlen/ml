package com.ml.base.domain;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * VO基类
 * 
 * </pre>
 * 
 * @author mecarlen.wang 2018年4月12日 下午3:06:18
 */

@Getter
@Setter
public abstract class BaseVO<ID extends Serializable, E extends BaseEntity<ID, ?>> implements Entity<ID> {
	@Id
	protected ID id;
    protected Integer yn;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss:SSS", store = true)
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
    protected Date createTime;
    private Long createTimeMills;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss:SSS", store = true)
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
    protected Date updateTime;
    private Long updateTimeMills;

    /**
     * <pre>
     * 给VO唯一标示赋值方法,在BaseServiceImpl用到,此方法其子类必须实现
     * 
     * </pre>
     * 
     * @param id
     *            ID VO唯一标示类型
     * @return BaseVO<ID, E>
     */
    public abstract BaseVO<ID, E> id(ID id);

    /**
     * <pre>
     * 给VO设置状态
     * 
     * </pre>
     * 
     * @param state
     *            EnumState
     * @return BaseVO<ID, E>
     */
    public abstract BaseVO<ID, E> state(final State state);

    /**
     * <pre>
     * 给VO创建时间赋值方法，在CommonServiceImpl用到,此方法其子类必须实现
     * 
     * </pre>
     * 
     * @param createTime
     *            Date 创建时间
     * @return BaseVO<ID, E>
     */
    public abstract BaseVO<ID, E> createTime(Date createTime);

    /**
     * <pre>
     * 给VO更新时间赋值方法，在BaseServiceImpl用到,此方法其子类必须实现
     * 
     * </pre>
     * 
     * @param updateTime
     *            Date 更新时间
     * @return BaseVO<ID, E>
     */
    public abstract BaseVO<ID, E> updateTime(Date updateTime);

    /**
     * <pre>
     * VO转换成Entity方法,在BaseServiceImpl用到,此方法其子类必须实现
     * 
     * </pre>
     * 
     * @return E 具体Entity类型
     */
    public abstract E toEntity();
}
