package com.ml.mnc.admin.rpc;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.ml.base.controller.WebResponse;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;

/**
 * <pre>
 * SRC客户端
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年11月5日 下午4:15:51
 */
@Slf4j
@Component
public class MncAdmHttpClient {
	@Value("${eureka.client.service-url.defaultZone}")
	private String srcUrl;

	private static OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3000L, TimeUnit.MILLISECONDS)
			.readTimeout(3000L, TimeUnit.MILLISECONDS).build();

	/**
	 * <pre>
	 * 通过appCode获取应用实例
	 * 
	 * </pre>
	 * 
	 * @param appCode String
	 * @return List<>
	 */
	public List<InstanceOf> getAppInstances(String appCode) {
		if (StringUtils.isBlank(appCode)) {
			return Lists.newArrayList();
		}
		final String urlFormat = "%s/apps/%s";
		Request.Builder reqBuilder = new Request.Builder().url(String.format(urlFormat, srcUrl, appCode))
				.addHeader("Accept", "application/json");
		Request req = reqBuilder.build();
		try {
			Response resp = client.newCall(req).execute();
			if (resp.isSuccessful()) {
				String applicationJson = resp.body().string();
				return JSONArray.parseObject(applicationJson, SrcApplication.class).getApplication().getInstance();
			} else {
				log.warn("SRCRequestReturnFailure,url:{},code:{},message:{}", req.url().toString(), resp.code(),
						resp.message());
			}
		} catch (IOException ex) {
			log.warn("SRCRequestIOException,URL:{}", req.url().toString(), ex);
		} catch (JSONException ex) {
			log.warn("SRCRequestJSONException,URL:{}", req.url().toString(), ex);
		} catch (Exception ex) {
			log.warn("SRCRequestException,URL:{}", req.url().toString(), ex);
		}
		return Lists.newArrayList();
	}

	/**
	 * <pre>
	 * 扩展消费者
	 * 
	 * </pre>
	 * 
	 * @param extConsumerUrl String
	 * @return WebResponse<Boolean>
	 */
	public Boolean extendConsumer(String extConsumerUrl) {
		Request.Builder reqBuilder = new Request.Builder().post(Util.EMPTY_REQUEST).url(extConsumerUrl)
				.addHeader("Accept", "application/json");
		Request req = reqBuilder.build();
		try {
			Response resp = client.newCall(req).execute();
			if (resp.isSuccessful()) {
				String respJson = resp.body().string();
				return JSON.parseObject(respJson, new TypeReference<WebResponse<Boolean>>() {
				}).getResult();
			} else {
				log.warn("SRCRequestReturnFailure,url:{},code:{},message:{}", extConsumerUrl, resp.code(),
						resp.message());
			}
		} catch (IOException ex) {
			log.warn("RequestIOException,url:{}", extConsumerUrl, ex);
		} catch (JSONException ex) {
			log.warn("RequestJSONException,url:{}", extConsumerUrl, ex);
		} catch (Exception ex) {
			log.warn("RequestException,url:{}", extConsumerUrl, ex);
		}

		return false;
	}

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	@Setter
	@Getter
	public static class SrcApplication {
		private ApplicationInfo application;
	}

	/**
	 * <pre>
	 * 应用对象
	 * 
	 * </pre>
	 * 
	 */
	@Setter
	@Getter
	public static class ApplicationInfo {
		private String name;
		private List<InstanceOf> instance;
	}

	/**
	 * <pre>
	 * 实例对象
	 * 
	 * </pre>
	 */
	@Setter
	@Getter
	public static class InstanceOf {
		/** 实例状态-UP-线上 */
		final public static String UP_STATUS_INSTANCE = "UP";

		private String app;
		private String ipAddr;
		private String status;
		private String hostName;
		private String healthCheckUrl;
		private String statusPageUrl;

		public String getRootUrl() {
			return healthCheckUrl.split("actuator")[0];
		}
	}

}
