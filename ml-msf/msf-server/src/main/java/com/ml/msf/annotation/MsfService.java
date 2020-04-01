package com.ml.msf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * <pre>
 * <b>RPC注解</b>
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2020年3月25日 下午3:43:29
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface MsfService {

}
