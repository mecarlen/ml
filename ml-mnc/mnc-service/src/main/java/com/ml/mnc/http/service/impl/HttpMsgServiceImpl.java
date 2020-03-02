package com.ml.mnc.http.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ml.base.query.PageModel;
import com.ml.base.query.PageThreadLocal;
import com.ml.base.service.impl.BaseServiceImpl;
import com.ml.mnc.Message;
import com.ml.mnc.Message.IntervalType;
import com.ml.mnc.Message.SendStatus;
import com.ml.mnc.MsgSendRecord.SendType;
import com.ml.mnc.admin.domain.AppVO;
import com.ml.mnc.admin.domain.MessageQueueVO;
import com.ml.mnc.admin.service.AppService;
import com.ml.mnc.http.HttpCallbackMsg;
import com.ml.mnc.http.HttpMsg;
import com.ml.mnc.http.domain.HttpCallbackMsgVO;
import com.ml.mnc.http.domain.HttpMQ;
import com.ml.mnc.http.domain.HttpMsgEntity;
import com.ml.mnc.http.domain.HttpMsgParams;
import com.ml.mnc.http.domain.HttpMsgSendRecordVO;
import com.ml.mnc.http.domain.HttpMsgVO;
import com.ml.mnc.http.query.HttpMsgQuery;
import com.ml.mnc.http.repository.HttpMsgRepository;
import com.ml.mnc.http.service.HttpCallbackMsgService;
import com.ml.mnc.http.service.HttpMsgSendRecordService;
import com.ml.mnc.http.service.HttpMsgService;
import com.ml.util.cache.CacheUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * http消息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月23日 下午3:42:12
 */
@Slf4j
@Service("httpMsgService")
public class HttpMsgServiceImpl extends BaseServiceImpl<Long, HttpMsgEntity, HttpMsgVO, HttpMsgRepository, HttpMsgQuery>
		implements HttpMsgService {
	@Resource
	private AppService appService;
	@Resource
	private HttpMsgSendRecordService httpMsgSendRecordService;
	@Resource
	private HttpCallbackMsgService httpCallbackMsgService;
	@Resource
	private CacheUtils cacheUtils;

	@Resource
	public void setHttpMsgRepository(HttpMsgRepository httpMsgRepository) {
		this.setRepository(httpMsgRepository);
	}

	@Resource
	public void setHttpMsgQuery(HttpMsgQuery httpMsgQuery) {
		this.setQuery(httpMsgQuery);
	}

	@Override
	public HttpCallbackMsgService callbackMsgService() {
		return httpCallbackMsgService;
	}

	@Override
	public HttpMsgSendRecordService sendRecordService() {
		return httpMsgSendRecordService;
	}

	@Override
	public HttpMsgVO saveMq(HttpMQ httpMq) {
		log.info("saveMq,appCode:{},queueName:{},routeKey:{},vccId:{},businessType:{},businessId:{},timeStamp:{}",
				httpMq.getAppCode(), httpMq.getQueueName(), httpMq.getRouteKey(), httpMq.getVccId(),
				httpMq.getBusinessType(), httpMq.getBusinessId(),
				null != httpMq.getMessageTimeStamp() ? httpMq.getMessageTimeStamp().getTime() : -1L);
		long acceptTimeMills = System.currentTimeMillis();
		HttpMsgVO vo = httpMq.toMsg(appService.getApp(httpMq.getAppCode()));
		vo.setAcceptTimeMills(acceptTimeMills);
		// 设置首次执行时间,默认取接收时间,有延时的取计算后的延时时间
		vo.setNextSendTimeMills(
				vo.toEntity().isFirstNeedDelay() ? vo.toEntity().getToSendTimeMills(0) : acceptTimeMills);
		if (!vo.toEntity().isFirstNeedDelay()) {
			// 立即推送的消息先放缓存
			cacheUtils.hSet(HttpMsg.HTTP_MSG_CACHE_KEY, vo.getBusinessUid(), vo);
		}
		return vo;
	}

	@Override
	public HttpMsgVO modifyMsg(HttpMsgVO httpMsg) {
		if (null == httpMsg.getId()) {
			super.create(httpMsg);
		} else {
			super.modify(httpMsg);
		}
		// 清除缓存
		cacheUtils.hDel(HttpMsg.HTTP_MSG_CACHE_KEY, httpMsg.getBusinessUid());
		if (httpMsg.isFirstSend()) {
			// 首次推送,缓存处理完的消息用于记录SLA,mnc-job消费清除
			cacheUtils.lPush(HttpMsg.HTTP_SLAMSG_CACHE_KEY, httpMsg);
		}
		// 清除推送中标记
		cacheUtils.hDel(HttpMsg.HTTP_MSG_SENDING_CACHE_KEY, httpMsg.getBusinessUid());
		return httpMsg;
	}

	@Override
	public PageModel<HttpMsgVO> findDelaySendMsg(HttpMsgParams params) {
		Page<HttpMsgVO> pg = PageHelper.startPage(PageThreadLocal.getParams().getPageNo(),
				PageThreadLocal.getParams().getPageSize(), true);
		List<HttpMsgVO> volist = query.selectDelayMsgByPage(params);
		PageModel<HttpMsgVO> page = toPage(pg.toPageInfo());
		page.setData(volist);
		return page;
	}

	@Override
	public Integer findDelaySendMsgCount(HttpMsgParams params) {
		return query.selectDelaySendMsgCount(params);
	}

	@Override
	public PageModel<HttpMsgVO> findRetrySendMsg(HttpMsgParams params) {
		Page<HttpMsgVO> pg = PageHelper.startPage(PageThreadLocal.getParams().getPageNo(),
				PageThreadLocal.getParams().getPageSize(), true);
		List<HttpMsgVO> volist = query.selectRetryMsgByPage(params);
		PageModel<HttpMsgVO> page = toPage(pg.toPageInfo());
		page.setData(volist);
		return page;
	}

	@Override
	public Integer findRetrySendMsgCount(HttpMsgParams params) {
		return query.selectRetrySendMsgCount(params);
	}

	@Override
	public PageModel<HttpMsgVO> findSendFailureMsg(HttpMsgParams params) {
		Page<HttpMsgVO> pg = PageHelper.startPage(PageThreadLocal.getParams().getPageNo(),
				PageThreadLocal.getParams().getPageSize(), true);
		List<HttpMsgVO> volist = query.selectSendFailureMsgByPage(params);
		PageModel<HttpMsgVO> page = toPage(pg.toPageInfo());
		page.setData(volist);
		return page;
	}

	@Override
	public Integer findSendFailureMsgCount(HttpMsgParams params) {
		return query.selectSendFailureMsgCount(params);
	}

	@Override
	public void sendSuccess(String returnJson, HttpMsgVO httpMsg, long beginTimeMills) {
		long currTimeMills = System.currentTimeMillis();
		// 添加推送记录
		HttpMsgSendRecordVO record = HttpMsgSendRecordVO.buildRecord(httpMsg, beginTimeMills, currTimeMills,
				returnJson);
		record.setSendStatus(SendStatus.SUCCESS.code());
		// TODO 登录逻辑
		record.setSendType(SendType.AUTO.code());
		record.setSenderErp("MNC");
		record.setSenderName("MNC");
		httpMsgSendRecordService.create(record);
		httpMsg.getSendRecords().add(record);
		// 更新消息状态
		if (httpMsg.isInitial()) {
			httpMsg.setFirstSendTimeMills(beginTimeMills);
		}
		if (!httpMsg.isSuccess()) {
			httpMsg.setFirstSendSuccessTimeMills(currTimeMills);
			httpMsg.setNextSendTimeMills(0L);
			httpMsg.setSendStatus(SendStatus.SUCCESS.code());
		}

		modifyMsg(httpMsg.addRetryCount());
		// 回调逻辑
		MessageQueueVO queue = appService.messageQueueService().getMessageQueue(httpMsg.getAppCode(),
				httpMsg.getQueueName(), httpMsg.getRouteKey());
		AppVO app = httpMsg.getApp();
		if (null == app) {
			app = appService.getApp(httpMsg.getAppCode());
			httpMsg.setApp(app);
		}
		if ((null != queue && queue.isNeedCallback()) || (null != app && app.getNeedCallback())) {
			// 添加回调
			HttpCallbackMsgVO callback = httpMsg.buildCallbackMsg();
			callback.returnJson(returnJson).msgContent(httpMsg.buildCallbackMq(record));
//			httpCallbackMsgService.create(callback);
			log.info("sendSuccess cacheCallbackMsg businessId:{},retryCount:{},sendTimeMills:{}", httpMsg.getBusinessId(),httpMsg.getRetryCount(),
					record.getBeginTimeMills());
			cacheUtils.lPush(HttpCallbackMsg.CALLBACK_MSG_CACHEKEY, callback);
		}

	}

	@Override
	public void sendFailure(HttpMsgVO httpMsg, long beginTimeMills, String errMsg) {
		long currTimeMills = System.currentTimeMillis();
		// 添加推送记录
		HttpMsgSendRecordVO record = HttpMsgSendRecordVO.buildRecord(httpMsg, beginTimeMills, currTimeMills, errMsg);
		record.setSendStatus(SendStatus.FAILURE.code());
		record.setSendType(SendType.AUTO.code());
		record.setSenderErp("MNC");
		record.setSenderName("MNC");
		httpMsgSendRecordService.create(record);
		httpMsg.getSendRecords().add(record);
		// 获取应用和队列信息
		MessageQueueVO queue = appService.messageQueueService().getMessageQueue(httpMsg.getAppCode(),
				httpMsg.getQueueName(), httpMsg.getRouteKey());
		AppVO app = httpMsg.getApp();
		if (null == app) {
			app = appService.getApp(httpMsg.getAppCode());
			httpMsg.setApp(app);
		}
		// 更新消息状态
		calculateStatusAndNextSendTimeMills(queue, httpMsg, beginTimeMills);
		modifyMsg(httpMsg);
		// 回调逻辑
		if ((null != queue && queue.isNeedCallback()) || (null != app && app.getNeedCallback())) {
			// 添加回调
			HttpCallbackMsgVO callback = httpMsg.buildCallbackMsg();
			callback.returnJson(errMsg).msgContent(httpMsg.buildCallbackMq(record));
//			httpCallbackMsgService.create(callback);
			log.info("sendFailure cacheCallbackMsg businessId:{},retryCount:{},sendTimeMills:{}", httpMsg.getBusinessId(),httpMsg.getRetryCount(),
					record.getBeginTimeMills());
			cacheUtils.lPush(HttpCallbackMsg.CALLBACK_MSG_CACHEKEY, callback);
		}
	}

	@Override
	public void autoRetryFailure(HttpMsgVO httpMsg, long beginTimeMills, String errMsg) {
		long currTimeMills = System.currentTimeMillis();
		// 添加推送记录
		HttpMsgSendRecordVO record = HttpMsgSendRecordVO.buildRecord(httpMsg, beginTimeMills, currTimeMills, errMsg);
		record.setSendStatus(SendStatus.FAILURE.code());
		record.setSendType(SendType.AUTO.code());
		record.setSenderErp("MNC");
		record.setSenderName("MNC");
		httpMsgSendRecordService.create(record);
		httpMsg.getSendRecords().add(record);
		// 获取应用和队列信息
		MessageQueueVO queue = appService.messageQueueService().getMessageQueue(httpMsg.getAppCode(),
				httpMsg.getQueueName(), httpMsg.getRouteKey());
		AppVO app = httpMsg.getApp();
		if (null == app) {
			app = appService.getApp(httpMsg.getAppCode());
			httpMsg.setApp(app);
		}
		httpMsg.addAutoRetryCount();
		if (httpMsg.isInitial()) {
			httpMsg.setFirstSendTimeMills(beginTimeMills);
			// 重试失败状态为推送中
			httpMsg.setSendStatus(SendStatus.SENDING.code());
		}
		if(httpMsg.isFirstSend()) {
			// 首次推送,缓存处理完的消息用于记录SLA,mnc-job消费清除
			cacheUtils.lPush(HttpMsg.HTTP_SLAMSG_CACHE_KEY, httpMsg);
		}
		// 自动重试间隔1秒
		httpMsg.setNextSendTimeMills(currTimeMills + IntervalType.SECOND.mills());

		// 回调逻辑
		if ((null != queue && queue.isNeedCallback()) || (null != app && app.getNeedCallback())) {
			// 添加回调
			HttpCallbackMsgVO callback = httpMsg.buildCallbackMsg();
			callback.returnJson(errMsg).msgContent(httpMsg.buildCallbackMq(record));
			log.info("autoRetryFailure cacheCallbackMsg businessId:{},retryCount:{},sendTimeMills:{}", httpMsg.getBusinessId(),httpMsg.getRetryCount(),
					record.getBeginTimeMills());
			cacheUtils.lPush(HttpCallbackMsg.CALLBACK_MSG_CACHEKEY, callback);
		}
	}

	/**
	 * <pre>
	 * 失败时更新消息状态并计算下次执行时间
	 * 
	 * </pre>
	 * 
	 * @param queue          MessageQueueVO
	 * @param httpMsg        HttpMsgVO
	 * @param beginTimeMills long
	 */
	void calculateStatusAndNextSendTimeMills(MessageQueueVO queue, HttpMsgVO httpMsg, long beginTimeMills) {
		if (httpMsg.isFailure() || httpMsg.isSuccess()) {
			// 终结状态跳过，一般只有手动调用才会出现
			return;
		}
		httpMsg.addRetryCount();
		// 设置首次推送时间
		if (httpMsg.isInitial()) {
			httpMsg.setFirstSendTimeMills(beginTimeMills);
			httpMsg.setSendStatus(SendStatus.SENDING.code());
		}

		// 设置下次执行时间
		if (StringUtils.isNotBlank(httpMsg.getRetryInterval())
				&& ((httpMsg.getRetryCount() - httpMsg.getAutoRetryCount()) < httpMsg.getRetryInterval()
						.split(",").length && httpMsg.getRetryCount() < Message.MAX_MSG_RETRY_COUNT)) {
			// 客户端传重试间隔,重试次不能超过30
			httpMsg.setNextSendTimeMills(httpMsg.toEntity().getToSendTimeMills());
		} else if (StringUtils.isBlank(httpMsg.getRetryInterval()) && null != queue
				&& StringUtils.isNotBlank(queue.getRetryIntervals())
				&& (httpMsg.getRetryCount() - httpMsg.getAutoRetryCount()) < queue.getMaxRetryCount()) {
			// 客户端未传重试间隔，队列配置了默认间隔则用队列默认间隔
			httpMsg.setNextSendTimeMills(httpMsg.toEntity().getToSendTimeMills(queue.getRetryIntervals()));
		} else if (StringUtils.isBlank(httpMsg.getRetryInterval())
				&& ((httpMsg.getRetryCount() - httpMsg.getAutoRetryCount()) - 1) <= Message.DEFAULT_MSG_RETRY_COUNT) {
			// 默认重试间隔，重试次数3次
			httpMsg.setNextSendTimeMills(httpMsg.toEntity().getToSendTimeMills(httpMsg.getRetryCount() - 1,
					Message.DEFAULT_MSG_DELAY_INTERVAL));
		} else {
			// 重试次数用完
			httpMsg.setSendStatus(SendStatus.FAILURE.code());
			httpMsg.setNextSendTimeMills(0L);
		}
	}

}
