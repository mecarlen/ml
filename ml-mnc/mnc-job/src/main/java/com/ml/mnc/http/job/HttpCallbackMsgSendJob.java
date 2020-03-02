package com.ml.mnc.http.job;

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
import com.ml.mnc.http.HttpCallbackMsg;
import com.ml.mnc.http.HttpCallbackMsgSender;
import com.ml.mnc.http.domain.HttpCallbackMsgVO;
import com.ml.mnc.http.service.HttpMsgService;
import com.ml.util.cache.CacheUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 回调消息推送
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月25日 下午6:16:00
 */
@Slf4j
@JobHandler("httpCallbackMsgSendJob")
@Component
public class HttpCallbackMsgSendJob extends IJobHandler {
	@Resource
	private HttpMsgService httpMsgService;
	@Resource
	private HttpCallbackMsgSender httpCallbackMsgSender;
	@Resource
	private RedissonClient redissonClient;
	@Resource
	private CacheUtils cacheUtils;

	@Override
	public ReturnT<String> execute(String jobParams) throws Exception {
		int pageSize = 100;
		if (StringUtils.isNotBlank(jobParams)) {
			PageParams<?> params = JSON.parseObject(jobParams, PageParams.class);
			pageSize = params.getPageSize();
		}

		XxlJobLogger.log("httpCallbackMsgSend begin");
		log.info("httpCallbackMsgSend begin,params:{}", jobParams);
		// 加锁
		RLock lock = null;
		boolean trylock = false;
		boolean trimRes = false;
		List<HttpCallbackMsgVO> msglist = Lists.newArrayList();
		try {
			lock = redissonClient.getLock(String.format(HttpCallbackMsg.CALLBACK_MSG_QUEUE_LOCK));
			trylock = lock.tryLock(5, 30, TimeUnit.MILLISECONDS);
			if (trylock) {
				msglist = cacheUtils.lRange(HttpCallbackMsg.CALLBACK_MSG_CACHEKEY, 0 - pageSize, -1, HttpCallbackMsgVO.class);
				if(!msglist.isEmpty()) {
					trimRes = cacheUtils.lTrim(HttpCallbackMsg.CALLBACK_MSG_CACHEKEY, 0, 0 - msglist.size() - 1);
					log.info("callbackMsgltrim:{}",trimRes);
				}
			}
		} catch (RedisException ex) {
			// redis访问异常
			log.error("callbackMsgSendJobTryLockRedisException", ex);
		} catch (Exception ex) {
			// redis访问异常
			log.error("callbackMsgSendJobTryLockFailure", ex);
		} finally {
			// 释放锁
			if (null != lock && trylock) {
				try {
					lock.unlock();
				} catch (Exception ex) {
					log.warn("unlockFailure",ex);
				}
			}
		}
		if(trimRes && !msglist.isEmpty()) {
			log.info("callbackMsgSize:{}",msglist.size());
			msglist.forEach(msg -> httpCallbackMsgSender.send(msg));
			httpMsgService.callbackMsgService().save(msglist);
		}
		log.info("httpCallbackMsgSend over,params:{}", jobParams);
		XxlJobLogger.log("httpCallbackMsgSend over");
		return SUCCESS;
	}

}
