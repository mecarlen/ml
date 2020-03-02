package com.ml.base.rpc;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import com.ml.base.domain.Entity;

/**
 * <pre>
 * DTO基类
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年7月18日 下午3:07:17
 */
@Setter
@Getter
public abstract class BaseDTO<ID extends Serializable> implements Entity<ID> {
    protected ID id;
    protected Integer yn;
    protected Date createTime;
    protected Date updateTime;
}
