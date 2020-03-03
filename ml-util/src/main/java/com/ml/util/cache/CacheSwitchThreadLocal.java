package com.ml.util.cache;

/**
 * <pre>
 * 缓存local开关
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月24日 下午3:51:06
 */

public class CacheSwitchThreadLocal {
    private static ThreadLocal<String> CACHE_SWITCH = new ThreadLocal<String>();
    
    /**
     * <pre>
     * 关闭缓存
     * 
     * </pre>
     * */
    public static  void close() {
        CACHE_SWITCH.set(CacheUtils.CLOSE_CACHE_SWITCH);
    }
    
    /**
     * <pre>
     * 开启缓存
     * 
     * </pre>
     * */
    public static void open() {
        CACHE_SWITCH.set(CacheUtils.OPEN_CACHE_SWITCH);
    }
    
    /**
     * <pre>
     * 取缓存开关状态
     * 
     * </pre>
     * */
    public static String getSwitch(){
        return CACHE_SWITCH.get();
    }
    
    public static void clean() {
        CACHE_SWITCH.remove();
    }
}
