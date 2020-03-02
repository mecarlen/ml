package com.ml.mnc.dingding;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ml.mnc.dingding.domain.DingdingMsgVO;
import com.ml.mnc.dingding.service.DingdingMsgService;
import com.ml.util.http.OkHttpClientBuilder;

import lombok.Getter;
import lombok.Setter;
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
 * 钉钉消息推送者
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月9日 下午4:36:02
 */
@Slf4j
@Component
public class DingdingMsgSender {
	private OkHttpClient client = null;
	@Resource
	private DingdingMsgService dingdingMsgService;
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

	public DingdingMsgSender() {
		client = OkHttpClientBuilder.getInstance().threadPoolConfigProperties().coreThreadSize(coreThreadSize)
				.maxThreadSize(maxThreadSize).threadKeepAliveTime(threadKeepAliveTime).coreThreadSize(coreThreadSize)
				.builder().dispatcherConfigProperties().maxRequests(maxRequests).maxRequestsPerHost(maxRequestsPerHost)
				.builder().connectTimeout(connectTimeout).readTimeout(readTimeout).build();
	}

	public void send(final DingdingMsgVO msg, long beginTimeMills) {
		String json = JSON.toJSONString(buildMsgBody(msg));
		RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);

		Request req = new Request.Builder().url(buildUrl(msg.getDingdingUrl())).post(requestBody).build();
		Call call = client.newCall(req);
		call.enqueue(new Callback() {
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				StringBuilder errMsg = new StringBuilder();
				String returnJson = response.body().string();
				log.info("dingdingSendReuslt,appCode:{},dingdingUrl:{},returnJson:{}", msg.getAppCode(),
						req.url().toString(), returnJson);
				if (response.isSuccessful()) {
					DingdingSendResponse resp = JSON.parseObject(returnJson, DingdingSendResponse.class);
					if (resp.isSuccess()) {
						dingdingMsgService.sendSuccess(msg, beginTimeMills, returnJson);
						return;
					} else {
						errMsg.append(resp.getErrcode()).append("-").append(resp.getErrmsg());
					}
				} else {
					errMsg.append(response.code()).append("-").append(response.message());
				}
				log.warn("sendDingtalkFailure,message:{},url:{}", errMsg.toString(), req.url().toString());
				dingdingMsgService.sendFailure(msg, beginTimeMills, errMsg.toString());
			}

			@Override
			public void onFailure(Call call, IOException ex) {
				log.error("sendDingtalkIOException,url:{}", req.url().toString(), ex);
				dingdingMsgService.sendFailure(msg, beginTimeMills, "IOException");
			}
		});
	}

	/**
	 * <pre>
	 * 构建钉钉URL
	 * 
	 * </pre>
	 * 
	 * @param dingdingUrl String
	 * @return String
	 */
	String buildUrl(String dingdingUrl) {
		String[] urlFields = dingdingUrl.split("&secret=");
		if (urlFields.length == 1) {
			return dingdingUrl;
		}
		// 加签URL
		StringBuilder signUrl = new StringBuilder(urlFields[0]);
		String secret = urlFields[1];
		Long timestamp = System.currentTimeMillis();
		// url加时间戳
		signUrl.append("&timestamp=").append(String.valueOf(timestamp));
		String stringToSign = String.valueOf(timestamp) + "\n" + secret;
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
			byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
			String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
			// url加签名
			signUrl.append("&sign=").append(sign);
		} catch (InvalidKeyException | NoSuchAlgorithmException | UnsupportedEncodingException
				| IllegalStateException ex) {
			log.warn("buildSignUrlFailure", ex);
		}
		return signUrl.toString();
	}

	/**
	 * <pre>
	 * 组装钉钉消息体
	 * 
	 * </pre>
	 * 
	 * @param msg DingdingMsgVO
	 * @return DingdingMsg
	 */
	DingdingMsg buildMsgBody(DingdingMsgVO msg) {
		Markdown md = new Markdown();
		md.setTitle(msg.getMsgTitle());
		md.setText(msg.getMsgTitle() + "\n> " + msg.getMsgContent());
		DingDingAt at = new DingDingAt();
		DingdingMsg msgBody = new DingdingMsg();
		msgBody.setMarkdown(md);
		msgBody.setAt(at);
		return msgBody;
	}

	/**
	 * <pre>
	 * 钉钉消息
	 * 
	 * </pre>
	 */
	@Setter
	@Getter
	public static class DingdingMsg {
		private String msgtype;
		private Markdown markdown;
		private DingDingAt at;

		public DingdingMsg() {
			msgtype = "markdown";
		}
	}

	/**
	 * <pre>
	 * markdown消息体格式
	 * 
	 * </pre>
	 */
	@Setter
	@Getter
	public static class Markdown {
		private String title;
		private String text;

	}

	/**
	 * <pre>
	 * 钉钉客户端
	 * 
	 * </pre>
	 */
	@Setter
	@Getter
	public static class DingDingAt {
		private List<String> atMobiles;
		private Boolean atAll;

		public DingDingAt() {
			atAll = Boolean.TRUE;
			atMobiles = Lists.newArrayList(new String[] { "13693397270" });
		}
	}
}
