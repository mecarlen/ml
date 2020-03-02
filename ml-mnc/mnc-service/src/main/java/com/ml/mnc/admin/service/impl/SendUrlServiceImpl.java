package com.ml.mnc.admin.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ml.base.service.impl.BaseServiceImpl;
import com.ml.mnc.admin.SendUrl;
import com.ml.mnc.admin.domain.SendUrlEntity;
import com.ml.mnc.admin.domain.SendUrlVO;
import com.ml.mnc.admin.query.SendUrlQuery;
import com.ml.mnc.admin.repository.SendUrlRepository;
import com.ml.mnc.admin.service.AuthRenewalService;
import com.ml.mnc.admin.service.SendUrlService;
import com.ml.util.cache.CacheUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 推送URL
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月25日 下午4:13:50
 */
@Slf4j
@Service("sendUrlService")
public class SendUrlServiceImpl extends BaseServiceImpl<Long, SendUrlEntity, SendUrlVO, SendUrlRepository, SendUrlQuery>
		implements SendUrlService {
	@Resource
	private CacheUtils cacheUtils;
	@Resource
	private AuthRenewalService authRenewalService;

	@Override
	public AuthRenewalService authRenewalService() {
		return authRenewalService;
	}

	@Resource
	public void setSendUrlRepository(SendUrlRepository sendUrlRepository) {
		this.setRepository(sendUrlRepository);
	}

	@Resource
	public void setSendUrlQuery(SendUrlQuery sendUrlQuery) {
		this.setQuery(sendUrlQuery);
	}

	@Override
	public SendUrlVO getSendUrl(String appCode, String queueName, String routeKey, String urlAddress) {
		String key = String.format(SendUrl.SENDURL_CACHEKEY_FORMAT, appCode, queueName, routeKey);
		SendUrlVO sendUrl = cacheUtils.hGet(key, urlAddress, SendUrlVO.class);
		if (null != sendUrl) {
			return sendUrl;
		}
		sendUrl = query.selectByUrlAddress(urlAddress);
		if (null != sendUrl) {
			boolean res = cacheUtils.hSet(key, urlAddress, sendUrl);
			log.info("getSendUrlFromDB and cache sendUrl,key:{},field:{},value:{},return:{}", key, urlAddress,
					JSON.toJSONString(sendUrl), res);
		}
		return sendUrl;
	}

}
