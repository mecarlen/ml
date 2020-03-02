package com.ml.util.cache.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.ml.util.cache.CacheUtils;

/**
 * <pre>
 * 缓存工具 - Spring Data Redis实现
 * 
 * </pre>
 * 
 * @author mecarlen.wang 2018年5月10日 下午7:02:41
 */
public class CacheUtilsSpringDataRedisImpl implements CacheUtils {
    private final static Logger SPRING_DATA_REDIS_LOG = LoggerFactory.getLogger(CacheUtilsSpringDataRedisImpl.class);
    final public static Gson GSON = new GsonBuilder().create();
    private StringRedisTemplate stringRedisClient;

    @Override
    public boolean set(String key, String value) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
            return false;
        }
        try {
            stringRedisClient.opsForValue().set(key, value);
            return true;
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis-->set failure key:" + key + ",value:" + value, ex);
        }
        return false;
    }

    @Override
    public boolean set(String key, Object value) {
        if (StringUtils.isBlank(key) || null == value) {
            return false;
        }
        try {
            return set(key, JSON.toJSONString(value));
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis-->set Object failure key:" + key + ",value:" + value, ex);
        }
        return false;
    }

    @Override
    public boolean setEx(String key, String value, long seconds) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(value) || 0 > seconds) {
            return false;
        }
        try {
            stringRedisClient.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
            return true;
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG
                .error("springDataRedis-->setEx failure key:" + key + ",value:" + value + ",seconds:" + seconds, ex);
        }
        return false;
    }

    @Override
    public boolean setEx(String key, Object value, long seconds) {
        if (StringUtils.isBlank(key) || null == value || 0 > seconds) {
            return false;
        }
        try {
            return setEx(key, JSON.toJSONString(value), seconds);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error(
                "springDataRedis-->setEx Object failure key:" + key + ",value:" + value + ",seconds:" + seconds, ex);
        }
        return false;
    }

    @Override
    public boolean setNx(String key, String value) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
            return false;
        }
        try {
            return stringRedisClient.opsForValue().setIfAbsent(key, value);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis-->setNX failure key:" + key + ",value:" + value, ex);
        }
        return false;
    }

    @Override
    public boolean setNx(String key, Object value) {
        if (StringUtils.isBlank(key) || null == value) {
            return false;
        }
        try {
            return setNx(key, JSON.toJSONString(value));
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis-->setNX Object failure key:" + key + ",value:" + value, ex);
        }
        return false;
    }

    @Override
    public boolean hSet(String key, String field, String value) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(field) || StringUtils.isBlank(value)) {
            return false;
        }
        try {
            stringRedisClient.opsForHash().put(key, field, value);
            return true;
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG
                .error("springDataRedis-->hSet failure key:" + key + ",field:" + field + ",value:" + value, ex);
        }

        return false;
    }

    @Override
    public boolean hSet(String key, String field, Object value) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(field) || null == value) {
            return false;
        }
        try {
            return hSet(key, field, JSON.toJSONString(value));
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG
                .error("springDataRedis-->hSet Object failure key:" + key + ",field:" + field + ",value:" + value, ex);
        }
        return false;
    }

    @Override
    public boolean hSetNx(String key, String field, String value) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(field) || StringUtils.isBlank(value)) {
            return false;
        }
        try {
            return stringRedisClient.opsForHash().putIfAbsent(key, field, value);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG
                .error("springDataRedis-->hSetNX failure key:" + key + ",field:" + field + ",value:" + value, ex);
        }
        return false;
    }

    @Override
    public boolean hSetNx(String key, String field, Object value) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(field) || null == value) {
            return false;
        }
        try {
            return hSetNx(key, field, JSON.toJSONString(value));
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error(
                "springDataRedis-->hSetNX Object failure key:" + key + ",field:" + field + ",value:" + value, ex);
        }
        return false;
    }

    @Override
    public boolean mSet(Map<String, String> values) {
        if (null == values || values.isEmpty()) {
            return false;
        }
        try {
            stringRedisClient.opsForValue().multiSet(values);
            return true;
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis-->mSet failure the size of values:" + values.size(), ex);
        }
        return false;
    }

    @Override
    public boolean hmSet(String key, Map<String, String> values) {
        if (StringUtils.isBlank(key) || null == values || values.isEmpty()) {
            return false;
        }
        try {
            stringRedisClient.opsForHash().putAll(key, values);
            return true;
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG
                .error("springDataRedis-->hMSet failure key:" + key + ",the size of values:" + values.size(), ex);
        }
        return false;
    }

    @Override
    public long append(String key, String value) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
            return -1L;
        }
        try {
            return stringRedisClient.opsForValue().append(key, value);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis-->append failure key:" + key + ",value:" + value, ex);
        }
        return -1L;
    }

    @Override
    public Long sAdd(String key, String... values) {
        if (StringUtils.isBlank(key) || null == values || values.length == 0) {
            return null;
        }
        try {
            return stringRedisClient.opsForSet().add(key, values);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis-->sAdd failure key:" + key + ",values:" + values, ex);
        }
        return -1L;
    }

    @Override
    public boolean zAdd(String key, String value, double score) {
        if (StringUtils.isAnyBlank(key, value)) {
            return false;
        }
        try {
            return stringRedisClient.opsForZSet().add(key, value, score);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis-->zAdd failure key:{},value{}", key, value, ex);
        }
        return false;
    }

    @Override
    public boolean zAdd(String key, Object value, double score) {
        if (StringUtils.isBlank(key) || null == value) {
            return false;
        }
        return zAdd(key, GSON.toJson(value), score);
    }

    @Override
    public Long lPush(String key, String... values) {
        if (StringUtils.isBlank(key) || null == values || values.length == 0) {
            return -1L;
        }
        try {
            return stringRedisClient.opsForList().leftPushAll(key, values);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis-->lPush failure key:" + key + ",values:" + values, ex);
        }
        return -1L;
    }

    @Override
    public Long lPush(String key, Object... values) {
        if (StringUtils.isBlank(key) || null == values || values.length == 0) {
            return null;
        }
        String[] jsonValues = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            jsonValues[i] = JSON.toJSONString(values[i]);
        }
        return lPush(key, jsonValues);
    }
    
    

    @Override
	public Long rPush(String key, String... values) {
    	if (StringUtils.isBlank(key) || null == values || values.length == 0) {
            return -1L;
        }
    	try {
            return stringRedisClient.opsForList().rightPushAll(key, values);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis-->lPush failure key:" + key + ",values:" + values, ex);
        }
        return -1L;
	}

	@Override
	public Long rPush(String key, Object... values) {
		if (StringUtils.isBlank(key) || null == values || values.length == 0) {
			return null;
		}
		String[] jsonValues = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			jsonValues[i] = JSON.toJSONString(values[i]);
		}
		return rPush(key, jsonValues);
	}

	@Override
    public String get(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        try {
            return stringRedisClient.opsForValue().get(key);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis-->get failure key:" + key, ex);
        }
        return null;
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        if (StringUtils.isBlank(key) || null == clazz) {
            return null;
        }
        String stringValue = get(key);
        if (StringUtils.isBlank(stringValue)) {
            SPRING_DATA_REDIS_LOG.warn("springDataRedis--> get failure key:" + key + ",return null");
            return null;
        }
        try {
            return JSON.parseObject(stringValue, clazz);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis--><T> T get failure key:" + key, ex);
        }
        return null;
    }

    @Override
    public String hGet(String key, String field) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(field)) {
            return null;
        }
        try {
            return (String)stringRedisClient.opsForHash().get(key, field);
        } catch (Exception ex) {
            String errorMsg = "springDataRedis--> hGet failure key:" + key + ",field:" + field;
            SPRING_DATA_REDIS_LOG.error(errorMsg, ex);
        }
        return null;
    }

    @Override
	public long hLen(String key) {
		if(StringUtils.isBlank(key)) {
			return 0;
		}
		return stringRedisClient.opsForHash().size(key);
	}

	@Override
    public <T> T hGet(String key, String field, Class<T> clazz) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(field) || null == clazz) {
            return null;
        }
        String stringValue = hGet(key, field);
        if (StringUtils.isBlank(stringValue)) {
            SPRING_DATA_REDIS_LOG.warn("springDataRedis--><T> T hGet failure key:" + key + ",field:" + field + ",class:"
                + clazz + ",return null");
            return null;
        }
        try {
            return JSON.parseObject(stringValue, clazz);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG
                .error("springDataRedis--><T> T hGet failure key:" + key + ",field:" + field + ",class:" + clazz, ex);
        }
        return null;
    }

    @Override
    public Map<String, String> hGetAll(String key) {
        if (StringUtils.isBlank(key)) {
            return new HashMap<String, String>(0);
        }
        try {
            Map<Object, Object> values = stringRedisClient.opsForHash().entries(key);
            Map<String, String> datas = new HashMap<String, String>(values.size());
            for (Entry<Object, Object> entry : values.entrySet()) {
                datas.put((String)entry.getKey(), (String)entry.getValue());
            }
            return datas;
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis-->hGetAll failure key:" + key, ex);
        }
        return new HashMap<String, String>(0);
    }

    @Override
    public <T> Map<String, T> hGetAll(String key, Class<T> clazz) {
        if (StringUtils.isBlank(key) || null == clazz) {
            return new HashMap<String, T>(0);
        }
        Map<String, String> stringValues = hGetAll(key);
        try {
            Map<String, T> values = new HashMap<String, T>(stringValues.size());
            for (Map.Entry<String, String> entry : stringValues.entrySet()) {
                values.put(entry.getKey(), JSON.parseObject(entry.getValue(), clazz));
            }
            return values;
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG
                .error("springDataRedis--><T> Map<String, T> hGetAll failure key:" + key + ",class:" + clazz, ex);
        }
        return new HashMap<String, T>(0);
    }

    @Override
    public List<String> mGet(String... keys) {
        if (null == keys || 0 == keys.length) {
            return new ArrayList<String>();
        }
        try {
            return stringRedisClient.opsForValue().multiGet(Arrays.asList(keys));
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis--> mGet failure size of keys:" + keys.length, ex);
        }
        return new ArrayList<String>();
    }

    @Override
    public Set<String> sMembers(String key) {
        if (StringUtils.isBlank(key)) {
            return new HashSet<String>();
        }
        try {
            return stringRedisClient.opsForSet().members(key);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis-->sMembers failure key:" + key, ex);
        }
        return new HashSet<String>();
    }

    @Override
    public Long sCard(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        try {
            return stringRedisClient.opsForSet().size(key);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis-->sCard failure key:" + key, ex);
        }
        return null;
    }

    @Override
    public Long lLen(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        try {
            return stringRedisClient.opsForList().size(key);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis-->lLen failure key:" + key, ex);
        }
        return null;
    }

    @Override
    public boolean exists(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        try {
            return stringRedisClient.hasKey(key);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis--> exists failure key:" + key, ex);
        }
        return false;
    }

    @Override
    public boolean sIsMember(String key, String value) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
            return false;
        }
        try {
            return stringRedisClient.opsForSet().isMember(key, value);
        } catch (Exception ex) {
            String errorMsg = "springDataRedis--> sIsMember failure key:" + key + ",value:" + value;
            SPRING_DATA_REDIS_LOG.error(errorMsg, ex);
        }
        return false;
    }

    @Override
    public Long incr(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        try {
            return stringRedisClient.opsForValue().increment(key, 1L);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis--> incr failure key:" + key, ex);
        }
        return null;
    }

    @Override
    public Long incrBy(String key, long step) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        try {
            return stringRedisClient.opsForValue().increment(key, step);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis--> incrBy failure key:" + key + ",step:" + step, ex);
        }
        return null;
    }

    @Override
    public Long hIncrBy(String key, String field, long step) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(field)) {
            return null;
        }
        try {
            return stringRedisClient.opsForHash().increment(key, field, step);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG
                .error("springDataRedis--> hIncrBy failure key:" + key + ",field:" + field + ",step:" + step, ex);
        }
        return null;
    }

    @Override
    public Long decr(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        try {
            return stringRedisClient.opsForValue().increment(key, -1L);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis--> decr failure key:" + key, ex);
        }
        return null;
    }

    @Override
    public Long decrBy(String key, long step) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        try {
            return stringRedisClient.opsForValue().increment(key, -1 * step);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis--> decrBy failure key:" + key + ",step:" + step, ex);
        }
        return null;
    }

    @Override
    public Long sRem(String key, String... values) {
        if (StringUtils.isBlank(key) || null == values || 0 == values.length) {
            return null;
        }
        try {
            return stringRedisClient.opsForSet().remove(key, (Object[])values);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG
                .error("springDataRedis--> srem failure key:" + key + ",size of values:" + values.length, ex);
        }
        return null;
    }

    @Override
    public String lPop(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        try {
            return stringRedisClient.opsForList().leftPop(key);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis--> lPop failure key:" + key, ex);
        }
        return null;
    }

    @Override
    public <T> T lPop(String key, Class<T> clazz) {
        if (StringUtils.isBlank(key) || null == clazz) {
            return null;
        }
        String stringValue = lPop(key);
        if (StringUtils.isBlank(stringValue)) {
            SPRING_DATA_REDIS_LOG.warn("springDataRedis--> lPop failure key:{},class:{} ,return empty", key, clazz);
            return null;
        }
        try {
            return JSON.parseObject(stringValue, clazz);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis--> lPop failure key:{},class:{}", key, clazz, ex);
        }
        return null;
    }

    @Override
    public List<String> lRange(String key, long start, long end) {
        if (StringUtils.isBlank(key)) {
            return new ArrayList<>(0);
        }
        try {
            return stringRedisClient.opsForList().range(key, start, end);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis--> lRange failure key:{},start:{},end:{}", key, start, end,
                ex);
        }
        return new ArrayList<>(0);
    }

    @Override
    public <T> List<T> lRange(String key, long start, long end, Class<T> clazz) {
        if (StringUtils.isBlank(key) || null == clazz) {
            return new ArrayList<>(0);
        }
        List<String> strlist = lRange(key, start, end);
        if (strlist.isEmpty()) {
            return new ArrayList<>(0);
        }
        List<T> objlist = new ArrayList<>(strlist.size());
        try {
            strlist.forEach(str -> objlist.add(JSON.parseObject(str, clazz)));
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis--> lRange failure key:{},start:{},end:{},class:{}", key, start,
                end, clazz, ex);
            return new ArrayList<>(0);
        }
        return objlist;
    }

    @Override
    public String rPop(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        try {
        	
            return stringRedisClient.opsForList().rightPop(key);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis--> rPop failure key:" + key, ex);
        }
        return null;
    }

    @Override
    public <T> T rPop(String key, Class<T> clazz) {
        if (StringUtils.isBlank(key) || null == clazz) {
            return null;
        }
        String stringValue = rPop(key);
        if (StringUtils.isBlank(stringValue)) {
            SPRING_DATA_REDIS_LOG
                .warn("springDataRedis--> rPop failure key:{},class:{},return null" ,key , clazz);
            return null;
        }
        try {
            return JSON.parseObject(stringValue, clazz);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis--> rPop failure key:" + key + ",class:" + clazz, ex);
        }
        return null;
    }

    @Override
    public long zCard(String key) {
        if (StringUtils.isBlank(key)) {
            return -1;
        }
        try {
            return stringRedisClient.opsForZSet().zCard(key);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis--> zCard failure key:{}", key, ex);
        }
        return -1;
    }

    @Override
    public Set<String> zRangeByScore(String key, double minScore, double maxScore) {
        if (StringUtils.isBlank(key) || minScore > maxScore) {
            return Sets.newHashSet();
        }
        try {
            return stringRedisClient.opsForZSet().rangeByScore(key, minScore, maxScore);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis--> zRangeByScore failure key:{},minScore:{},maxScore:{}", key,
                minScore, maxScore, ex);
        }
        return Sets.newHashSet();
    }

    @Override
    public <T> Set<T> zRangeByScore(String key, double minScore, double maxScore, Class<T> clazz) {
        if (StringUtils.isBlank(key) || minScore > maxScore) {
            return Sets.newHashSet();
        }
        Set<String> members = zRangeByScore(key, minScore, maxScore);
        Set<T> objs = Sets.newHashSet();
        try {
            members.forEach(member -> objs.add(GSON.fromJson(member, clazz)));
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error(
                "springDataRedis--> zRangeByScore failure key:{},minScore:{},maxScore:{},clazz:{}", key, minScore,
                maxScore, clazz, ex);
        }
        return objs;
    }

    @Override
    public <T> Set<T> zRevRangeByScore(String key, double minScore, double maxScore, Class<T> clazz) {
        if (StringUtils.isBlank(key) || minScore > maxScore) {
            return Sets.newHashSet();
        }
        Set<String> members = zRevRangeByScore(key, minScore, maxScore);

        Set<T> objs = Sets.newHashSet();
        try {
            members.forEach(member -> objs.add(GSON.fromJson(member, clazz)));
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error(
                "springDataRedis--> zRevRangeByScore failure key:{},minScore:{},maxScore:{},clazz:{}", key, minScore,
                maxScore, clazz, ex);
        }
        return objs;
    }

    @Override
    public Set<String> zRevRangeByScore(String key, double minScore, double maxScore) {
        if (StringUtils.isBlank(key) || minScore > maxScore) {
            return Sets.newHashSet();
        }
        try {
            return stringRedisClient.opsForZSet().reverseRangeByScore(key, minScore, maxScore);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis--> zRevRangeByScore failure key:{},minScore:{},maxScore:{}",
                key, minScore, maxScore, ex);
        }
        return Sets.newHashSet();
    }

    @Override
    public Long zRemRangeByScore(String key, double minScore, double maxScore) {
        if (StringUtils.isBlank(key) || minScore > maxScore) {
            return 0L;
        }

        try {
            return stringRedisClient.opsForZSet().removeRangeByScore(key, minScore, maxScore);
        } catch (Exception e) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis--> zRemRangeByScore failure key:{},minScore:{},maxScore:{}",
                key, minScore, maxScore, e);
        }

        return 0L;
    }

    @Override
    public Long hDel(String key, String... fields) {
        if (StringUtils.isBlank(key) || null == fields || 0 == fields.length) {
            return null;
        }
        try {
            return stringRedisClient.opsForHash().delete(key, (Object[])fields);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis--> hDel failure key:" + key + ",fields:" + fields, ex);
        }
        return null;
    }

    @Override
    public boolean expire(String key, long seconds) {
        if (StringUtils.isBlank(key) || 0 > seconds) {
            return false;
        }
        try {
            return stringRedisClient.expire(key, seconds, TimeUnit.SECONDS);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis--> expire failure key:" + key + ",seconds:" + seconds, ex);
        }
        return false;
    }

    @Override
    public boolean expireAt(String key, Date time) {
        if (StringUtils.isBlank(key) || null == time) {
            return false;
        }
        try {
            return stringRedisClient.expireAt(key, time);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG
                .error("springDataRedis--> expireAt failure key:" + key + ",time:" + time.getTime() / 1000, ex);
        }
        return false;
    }

    @Override
    public boolean del(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        try {
            stringRedisClient.delete(key);
            return true;
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis--> del failure key:" + key, ex);
        }
        return false;
    }

    @Override
    public boolean lTrim(String key, long start, long end) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        try {
            stringRedisClient.opsForList().trim(key, start, end);
            return true;
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG
                .error("springDataRedis-->lTrim failure key:" + key + ",start:" + start + ",end:" + end, ex);
        }
        return false;
    }

	@Override
    public String lIndex(String key, long index) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        try {
            return stringRedisClient.opsForList().index(key, index);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis-->lIndex failure key:" + key + ",index:" + index, ex);
        }
        return null;
    }

    @Override
    public boolean hExists(String key, String field) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        try {
            return stringRedisClient.opsForHash().hasKey(key, field);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis-->hExists failure key:" + key + ",field:" + field, ex);
        }
        return false;
    }

    @Override
    public Long pTtl(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        try {
            return stringRedisClient.getExpire(key, TimeUnit.MILLISECONDS);
        } catch (Exception ex) {
            SPRING_DATA_REDIS_LOG.error("springDataRedis-->hExists failure key:" + key, ex);
        }
        return null;
    }

    @Override
    public Set<String> keys(String pattern) {
        if (StringUtils.isBlank(pattern)) {
            return null;
        }

        return stringRedisClient.keys(pattern);
    }

    public void setStringRedisClient(StringRedisTemplate stringRedisClient) {
        this.stringRedisClient = stringRedisClient;
    }
}
