package com.ml.mnc.admin.service.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.ml.base.service.impl.BaseServiceImpl;
import com.ml.mnc.admin.App;
import com.ml.mnc.admin.domain.AppEntity;
import com.ml.mnc.admin.domain.AppVO;
import com.ml.mnc.admin.query.AppQuery;
import com.ml.mnc.admin.repository.AppRepository;
import com.ml.mnc.admin.service.AppService;
import com.ml.mnc.admin.service.MessageQueueService;
import com.ml.mnc.admin.service.SendUrlService;
import com.ml.util.cache.CacheUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 应用信息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月6日 上午11:13:46
 */
@Slf4j
@Service("appService")
public class AppServiceImpl extends BaseServiceImpl<Long, AppEntity, AppVO, AppRepository, AppQuery>
		implements AppService {
	@Resource
	private CacheUtils cacheUtils;
	@Resource
	private MessageQueueService messageQueueService;
	@Resource
	private SendUrlService sendUrlService;

	/**
	 * <pre>
	 * 本地缓存
	 * 
	 * 描述
	 * 	1、最多存储5W中继号
	 *  2、内存数据保留5分钟过期
	 *  3、内存过期数据重新从redis取
	 * </pre>
	 */
	private LoadingCache<String, AppVO> localCache = CacheBuilder.newBuilder()
			.concurrencyLevel(Runtime.getRuntime().availableProcessors()).maximumSize(50000)
			.expireAfterWrite(5, TimeUnit.MINUTES).build(new CacheLoader<String, AppVO>() {
				@Override
				public AppVO load(String appCode) throws Exception {
					return getApp(appCode);
				}
			});

	@Resource
	public void setAppRepository(AppRepository appRepository) {
		this.setRepository(appRepository);
	}

	@Resource
	public void setAppQuery(AppQuery appQuery) {
		this.setQuery(appQuery);
	}

	@Override
	public MessageQueueService messageQueueService() {
		return messageQueueService;
	}

	@Override
	public SendUrlService sendUrlService() {
		return sendUrlService;
	}

	@Override
	public AppVO getApp(String appCode) {
		if (StringUtils.isBlank(appCode)) {
			log.warn("param:appCode is empty,return null");
			return null;
		}
		AppVO app = cacheUtils.hGet(App.APP_CACHE_KEY, appCode.toLowerCase(), AppVO.class);
		if (null != app) {
			return app;
		}
		app = query.selectByAppCode(appCode.toLowerCase());
		if (null != app) {
			boolean res = cacheUtils.hSet(App.APP_CACHE_KEY, app.getAppCode().toLowerCase(), app);
			log.info("getAppFromDB and cache app success={}", res);
		}
		return app;
	}

	@Override
	public AppVO getAppFromLocal(String appCode) {
		AppVO app = null;
		try {
			app = this.localCache.get(appCode);
		} catch (ExecutionException ex) {
			log.warn(String.format("GetAppFromLocalException,appCode:%s", appCode), ex);
		}
		return app;
	}

}
