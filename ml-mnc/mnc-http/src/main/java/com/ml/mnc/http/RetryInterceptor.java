package com.ml.mnc.http;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ml.mnc.http.domain.HttpMsgVO;
import com.ml.mnc.http.service.HttpMsgService;
import com.ml.util.cache.CacheUtils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <pre>
 * 重试监听器
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年10月29日 上午10:51:14
 */
@Slf4j
@Component
public class RetryInterceptor implements Interceptor {
	@Value("${okhttp.max-retry}")
	private Integer maxRetry;
	@Resource
	private CacheUtils cacheUtils;
	@Resource
	private HttpMsgService httpMsgService;
	
	@Override
	public Response intercept(Chain chain) throws IOException {
		Request originalReq = chain.request();

		String businessUid = originalReq.header("uid");
		HttpMsgVO httpMsg = cacheUtils.hGet(HttpMsg.HTTP_MSG_CACHE_KEY, businessUid,HttpMsgVO.class);
		Request request = originalReq.newBuilder().headers(originalReq.headers().newBuilder().removeAll("uid").build())
				.build();
		originalReq = null;
		Response response = null;
		int retryCount = 0;
		do {
			if (retryCount > 0) {
				try {
					TimeUnit.SECONDS.sleep(1L);
				} catch (InterruptedException ex) {
					log.warn("sleep1sException", ex);
				}
			}
			long beginTimeMills = System.currentTimeMillis();
			if (retryCount > 0) {
				log.warn("autoRetry businessId:{} url:{},retryCount:{}",httpMsg.getBusinessId(), request.url().uri(), retryCount);
			}
			try {
				response = chain.proceed(request);
			} catch (IOException ex) {
				if(retryCount < maxRetry) {
					//记录失败
					httpMsgService.autoRetryFailure(httpMsg, beginTimeMills, ex.getClass().getName() + ":" + ex.getMessage());
				}
				response = null;
				log.warn(String.format("autoRetryTimeout,businessId:%s url:%s,retryCount:%d",httpMsg.getBusinessId(), request.url().toString(), retryCount), ex);
				if (retryCount == maxRetry) {
					//重试次数用完，同步缓存
					boolean retrySet = cacheUtils.hSet(HttpMsg.HTTP_MSG_CACHE_KEY, businessUid,httpMsg);
					log.info("autoRetryTimeout autoRetry:{} update cache httpMsg success:{}",retryCount,retrySet);
					throw new IOException(ex.getClass().getName() + ":" + ex.getMessage());
				}
			}
			retryCount++;

		} while ((response == null) && retryCount <= maxRetry);
		//重试成功,retryCount=1时只时首次正常推送，为2时表示重试1次，所以大于1时同步缓存
		if(retryCount > 1) {
			boolean retrySet = cacheUtils.hSet(HttpMsg.HTTP_MSG_CACHE_KEY, businessUid,httpMsg);
			log.info("businessId:{} autoRetry:{} update cache httpMsg success:{}",httpMsg.getBusinessId(),retryCount,retrySet);
		}
		return response;
	}

	public void setMaxRetry(Integer maxRetry) {
		this.maxRetry = maxRetry;
	}
}
