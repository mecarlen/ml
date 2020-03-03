package com.ml.util.roundRobin;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

/**
 * <pre>
 * 带权重轮询算法
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年8月19日 上午10:17:45
 */

public class WeightRoundRobin {
    final public static Random RANDOM_FUNCTION = new Random();
    /** 带权重轮询算法缓存key outbound:permission:servers:roundRobin:algorithm*/
    final public static String ROUND_ROBIN_ALGORITHM_CACHE_KEY = "outbound:permission:servers:roundRobin:algorithm";
    /** 带权重轮询算法round-轮询实现，*/
    final public static String ROUND_ROBIN_ALGORITHM = "round";
    /** 带权重轮询算法random-随机实现 */
    final public static String ROUND_ROBIN_RANDOM_ALGORITHM = "random";
    /**
     * <pre>
     * 带权重轮询取一个值
     * 
     * </pre>
     * 
     * @param params
     *            RoundRobinParam
     * @param datas
     *            List<T>
     * @return T
     */
    public static <T extends Weight> T roundRobin(RoundRobinParam params, List<T> datas) {
        if (null == datas || datas.isEmpty()) {
            return null;
        }
        // 枚重轮询
        if (datas.size() == 1) {
            return datas.get(0);
        }
        return round(params, datas);
    }

    /**
     * <pre>
     * 按权重比例随机
     * 
     * </pre>
     * @param datas List<T>
     * @return T
     */
    public static <T extends Weight> T random(List<T> datas) {
        if (null == datas || datas.isEmpty()) {
            return null;
        }
        List<T> totalDatas = Lists.newArrayList();
        for(T data:datas) {
        	if(data.getWeight() > 0) {
        		for (int i = 1; i <= data.getWeight(); i++) {
                    totalDatas.add(data);
                }
        	}
        }
        return totalDatas.get(RANDOM_FUNCTION.nextInt(totalDatas.size()));
    }

    /**
     * <pre>
     * 枚重轮询
     * 
     * </pre>
     * 
     * @param params
     *            RoundRobinParam
     * @param trunkNums
     *            List<TrunkNum>
     * @return TrunkNum
     */
    private static <T extends Weight> T round(RoundRobinParam params, List<T> trunkNums) {
        int currentIndex = params.getCurrentIndex();
        int currentWeight = params.getCurrentWeight();
        // 最大权重
        int maxWeight = maxWeight(trunkNums);
        // 权重的最大公约数
        int gcdWeight = trunksGcd(trunkNums);
        T res = null;
        while (true) {
            currentIndex = (currentIndex + 1) % trunkNums.size();
            if (currentIndex == 0) {
                currentWeight = currentWeight - gcdWeight;
                if (currentWeight <= 0) {
                    currentWeight = maxWeight;
                    if (currentWeight == 0) {
                        break;
                    }
                }
            }

            if (trunkNums.get(currentIndex).getWeight() >= currentWeight) {
                res = trunkNums.get(currentIndex);
                break;
            }
        }
        // 参数回传
        params.setCurrentIndex(currentIndex);
        params.setCurrentWeight(currentWeight);
        return res;
    }

    /**
     * <pre>
     * 返回所有号码池权重的最大公约数
     * 
     * </pre>
     * 
     * @param trunkNums
     *            List<WeightTrunk>
     * @return int
     */
    private static <T extends Weight> int trunksGcd(List<T> trunkNums) {
        int comDivisor = 0;
        for (int i = 0; i < trunkNums.size() - 1; i++) {
            if (comDivisor == 0) {
                comDivisor = gcd(trunkNums.get(i).getWeight(), trunkNums.get(i + 1).getWeight());
            } else {
                comDivisor = gcd(comDivisor, trunkNums.get(i + 1).getWeight());
            }
        }
        return comDivisor;
    }

    /**
     * <pre>
     * 获得服务器中的最大权重
     * 
     * </pre>
     * 
     * @param trunkNums
     * @return int
     */
    private static <T extends Weight> int maxWeight(List<T> trunkNums) {
        int max = trunkNums.get(0).getWeight();
        int tmp;
        for (int i = 1; i < trunkNums.size(); i++) {
            tmp = trunkNums.get(i).getWeight();
            if (max < tmp) {
                max = tmp;
            }
        }
        return max;
    }

    /**
     * <pre>
     * 求两个数的最大公约数 4和6最大公约数是2
     * 
     * </pre>
     * 
     * @param num1
     *            int
     * @param num2
     *            int
     * @return int
     */
    private static int gcd(int num1, int num2) {
        BigInteger i1 = new BigInteger(String.valueOf(num1));
        BigInteger i2 = new BigInteger(String.valueOf(num2));
        return i1.gcd(i2).intValue();
    }
}
