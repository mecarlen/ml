package com.ml.mnc.http;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.ml.mnc.admin.SendUrl;
import com.ml.mnc.http.domain.HttpMsgVO;
import com.ml.mnc.http.service.HttpMsgService;
import com.ml.util.cache.CacheUtils;
import com.ml.util.http.OkHttpClientBuilder;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * <pre>
 * HTTP推送
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月19日 下午2:26:42
 */
@Slf4j
public class HttpMsgSender {
	private OkHttpClient client = null;
	@Resource
	private HttpMsgService httpMsgService;
	@Value("${okhttp.client.connectTimeout}")
	private Long connectTimeout;
	@Value("${okhttp.client.readTimeout}")
	private Long readTimeout;
	@Value("${okhttp.client.threadPool.coreThreadSize}")
	private Integer coreThreadSize;
	@Value("${okhttp.client.threadPool.maxThreadSize}")
	private Integer maxThreadSize;
	@Value("${okhttp.client.threadPool.threadKeepAliveTime}")
	private Long threadKeepAliveTime;
	@Value("${okhttp.client.dispatcher.maxRequests}")
	private Integer maxRequests;
	@Value("${okhttp.client.dispatcher.maxRequestsPerHost}")
	private Integer maxRequestsPerHost;
	@Value("${rabbitmq.notification.url-threshold}")
    private Integer threshold;
	
	@Resource
	private CacheUtils cacheUtils;

	public void init() {
		client = OkHttpClientBuilder.getInstance().threadPoolConfigProperties().coreThreadSize(coreThreadSize)
				.maxThreadSize(maxThreadSize).threadKeepAliveTime(threadKeepAliveTime).coreThreadSize(coreThreadSize)
				.builder().dispatcherConfigProperties().maxRequests(maxRequests).maxRequestsPerHost(maxRequestsPerHost)
				.builder().connectTimeout(connectTimeout).readTimeout(readTimeout).build();
	}

	public void send(final HttpMsgVO httpMsg) {
		RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"),
				httpMsg.getMsgContent());
		final Request.Builder builder = new Request.Builder();
		// 加头认证信息 TODO 补充认证超时逻辑
		parseHeadAuthDatas(httpMsg).entrySet()
				.forEach(authData -> builder.addHeader(authData.getKey(), String.valueOf(authData.getValue())));
		Request req = builder.url(httpMsg.getSendUrl()).post(requestBody).build();
		Call call = client.newCall(req);
		final long beginTimeMills = System.currentTimeMillis();
		call.enqueue(new Callback() {

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				StringBuilder errMsg = new StringBuilder();
				if (response.isSuccessful()) {
					String respJson = response.body().string();
					if (httpMsg.toEntity().isSendSuccess(respJson)) {
						httpMsgService.sendSuccess(respJson, httpMsg, beginTimeMills);
					} else {
						errMsg.append(respJson);
					}
					// 成功推送URL失败次数清零
					writeUrlSendResultRecord(Boolean.TRUE, httpMsg);
				} else {
					errMsg.append(response.code()).append("-").append(response.message());
					// 记录目标URL失败情况
					writeUrlSendResultRecord(Boolean.FALSE, httpMsg);
				}
				if(StringUtils.isNotBlank(errMsg.toString())) {
					log.warn("sendHttpMsgFailure,message:{},url:{}", errMsg.toString(), httpMsg.getSendUrl());
					httpMsgService.sendFailure(httpMsg, beginTimeMills, errMsg.toString());
				}
			}

			@Override
			public void onFailure(Call call, IOException ex) {
				log.error(String.format("sendHttpMsgIOException,businessId:%s,retryCount:%d,url:%s", httpMsg.getBusinessId(),
						httpMsg.getRetryCount(),httpMsg.getSendUrl()), ex);
				httpMsgService.sendFailure(httpMsg, beginTimeMills, ex.getClass().getName() + ":" + ex.getMessage());
				// 记录目标URL失败情况
				writeUrlSendResultRecord(Boolean.FALSE, httpMsg);
			}
		});
	}

	/**
	 * <pre>
	 * 认证过期获取新认证信息逻辑
	 * 
	 * </pre>
	 */

	/**
	 * <pre>
	 * 取header认证参数
	 * 
	 * </pre>
	 * 
	 * @param httpMsg HttpMsgVO
	 * @return Map<String,Object>
	 */
	public Map<String, Object> parseHeadAuthDatas(HttpMsgVO httpMsg) {
		if (StringUtils.isBlank(httpMsg.getAuthJson())) {
			return Maps.newHashMap();
		}
		try {
			return JSONObject.parseObject(httpMsg.getAuthJson());
		} catch (Exception ex) {
			log.error("parseHeadAuthDatasException,authJson:{},sendUrl:{}", httpMsg.getAuthJson(), httpMsg.getSendUrl(),
					ex);
		}
		return Maps.newHashMap();
	}

	/**
	 * <pre>
	 * 记录URL请求成功与失败计数
	 * 
	 * </pre>
	 */
	private void writeUrlSendResultRecord(boolean success, HttpMsgVO httpMsg) {
		if (success) {
			cacheUtils.hSet(SendUrl.URL_SEND_FAILURE_RECORD_CACHEKEY, httpMsg.getSendUrl().split("\\?")[0], 0);
		} else {
			cacheUtils.hIncrBy(SendUrl.URL_SEND_FAILURE_RECORD_CACHEKEY, httpMsg.getSendUrl().split("\\?")[0], 1);
		}
	}

	/**
	 * <pre>
	 * 判断是否允许即时推送
	 * 
	 * 描述
	 * 连续threshold次因URL异常而推送失败的，此URL停止即时推送
	 * 
	 * </pre>
	 * 
	 * @param httpMsg HttpMsgVO
	 * @return boolean
	 */
	public boolean isCloseImmediatelySend(HttpMsgVO httpMsg) {
		if (StringUtils.isBlank(httpMsg.getSendUrl())) {
			log.warn("urlIsEmptyStopSend,businessId:{},vccId:{},url:{}", httpMsg.getBusinessId(), httpMsg.getVccId(),
					httpMsg.getSendUrl());
			return true;
		}
		String failCountStr = cacheUtils
				.get(String.format(SendUrl.URL_SEND_FAILURE_RECORD_CACHEKEY, httpMsg.getSendUrl().split("\\?")[0]));
		if (StringUtils.isNotBlank(failCountStr) && Integer.valueOf(failCountStr) > threshold) {
			return true;
		}
		return false;
	}
}
