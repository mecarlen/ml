package com.ml.util.roundRobin;
/**
 * <pre>
 * 权重接口
 * 
 * </pre>
 * @author mecarlen.Wang 2019年11月22日 下午6:20:55
 */
public interface Weight {
	/**
     * <pre>
     * 取权重,权重必须大于0
     * 
     * </pre>
     * 
     * @return int
     * */
    int getWeight();
}
