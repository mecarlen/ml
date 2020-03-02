package com.ml.mnc.admin.job;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisException;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ml.base.query.PageParams;
import com.ml.mnc.admin.rpc.SlaMsgProducer;
import com.ml.mnc.admin.rpc.SlaMsgProducer.SlaMessage;
import com.ml.mnc.http.HttpMsg;
import com.ml.mnc.http.domain.HttpMsgVO;
import com.ml.util.cache.CacheUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * SLA消息推送任务
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年11月7日 下午8:06:34
 */
@Slf4j
@JobHandler("slaMsgSendJob")
@Component
public class SlaMsgSendJob extends IJobHandler {
	@Resource
	private SlaMsgProducer slaMsgProducer;
	@Resource
	private CacheUtils cacheUtils;
	@Resource
	private RedissonClient redissonClient;

	@Override
	public ReturnT<String> execute(String param) throws Exception {
		int pageSize = 100;
		log.info("sendSLA begin,params:{}", param);
		if (StringUtils.isNotBlank(param)) {
			PageParams<?> params = JSON.parseObject(param, PageParams.class);
			pageSize = params.getPageSize();
		}
		// 加锁
		RLock lock = null;
		boolean trylock = false;
		boolean trimRes = false;
		List<HttpMsgVO> httpMsglist = Lists.newArrayList();
		try {
			lock = redissonClient.getLock(String.format(SlaMessage.SLA_MSG_QUEUE_LOCK));
			trylock = lock.tryLock(5, 30, TimeUnit.MILLISECONDS);
			if (trylock) {
				//从右边取指定页长的消息
				httpMsglist = cacheUtils.lRange(HttpMsg.HTTP_SLAMSG_CACHE_KEY, 0 - pageSize, -1, HttpMsgVO.class);
				//从左边截取除取出实际长度之外的消息
				if(!httpMsglist.isEmpty()) {
					trimRes = cacheUtils.lTrim(HttpMsg.HTTP_SLAMSG_CACHE_KEY, 0, 0 - httpMsglist.size() - 1);
					log.info("slaLtrim:{}",trimRes);
				}
			}
		} catch (RedisException ex) {
			// redis访问异常
			log.error("slaTryLockRedisException", ex);
		} catch (Exception ex) {
			// redis访问异常
			log.error("slaTryLockFailure", ex);
		} finally {
			// 释放锁
			if (null != lock) {
				try {
					lock.unlock();
				} catch (Exception ex) {
					log.warn("unlockFailure",ex);
				}
			}
		}
		if (trimRes && !httpMsglist.isEmpty()) {
			httpMsglist.forEach(httpMsg->slaMsgProducer.sendSla(httpMsg));
		}
		log.info("sendSLA over,params:{}", param);
		return SUCCESS;
	}

}
