package com.ml.util.roundRobin;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 带权重轮询参数
 * 
 * </pre>
 * @author mecarlen.Wang 2019年11月22日 下午6:22:16
 */
@Setter
@Getter
@Builder
public class RoundRobinParam {
	Integer currentIndex;
    Integer currentWeight;
}
