package com.ml.mnc.dingding.service.impl;

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
import com.ml.mnc.Message.SendStatus;
import com.ml.mnc.MsgSendRecord.SendType;
import com.ml.mnc.admin.domain.AppVO;
import com.ml.mnc.admin.domain.MessageQueueVO;
import com.ml.mnc.admin.service.AppService;
import com.ml.mnc.dingding.domain.DingdingMQ;
import com.ml.mnc.dingding.domain.DingdingMsgEntity;
import com.ml.mnc.dingding.domain.DingdingMsgParams;
import com.ml.mnc.dingding.domain.DingdingMsgSendRecordVO;
import com.ml.mnc.dingding.domain.DingdingMsgVO;
import com.ml.mnc.dingding.query.DingdingMsgQuery;
import com.ml.mnc.dingding.repository.DingdingMsgRepository;
import com.ml.mnc.dingding.service.DingdingCallbackMsgService;
import com.ml.mnc.dingding.service.DingdingMsgSendRecordService;
import com.ml.mnc.dingding.service.DingdingMsgService;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 钉钉消息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月6日 上午11:30:22
 */
@Slf4j
@Service("dingdingMsgService")
public class DingdingMsgServiceImpl
		extends BaseServiceImpl<Long, DingdingMsgEntity, DingdingMsgVO, DingdingMsgRepository, DingdingMsgQuery>
		implements DingdingMsgService {
	@Resource
	private AppService appService;
	@Resource
	private DingdingMsgSendRecordService dingdingMsgSendRecordService;
	@Resource
	private DingdingCallbackMsgService dingdingCallbackMsgService;

	@Resource
	public void setDingdingMsgRepository(DingdingMsgRepository dingdingMsgRepository) {
		this.setRepository(dingdingMsgRepository);
	}

	@Resource
	public void setDingtalkMsgQuery(DingdingMsgQuery dingdingMsgQuery) {
		this.setQuery(dingdingMsgQuery);
	}

	@Override
	public DingdingCallbackMsgService callbackMsgService() {
		return dingdingCallbackMsgService;
	}

	@Override
	public DingdingMsgSendRecordService sendRecordService() {
		return dingdingMsgSendRecordService;
	}

	@Override
	public DingdingMsgVO saveMq(DingdingMQ dingdingMq) {
		log.info("saveMq,appCode:{},queueName:{},routeKey:{},vccId:{},businessType:{},businessId:{},timeStamp:{}",
				dingdingMq.getAppCode(), dingdingMq.getQueueName(), dingdingMq.getRouteKey(), dingdingMq.getVccId(),
				dingdingMq.getBusinessType(), dingdingMq.getBusinessId(),
				null != dingdingMq.getMessageTimeStamp() ? dingdingMq.getMessageTimeStamp().getTime() : -1L);
		long acceptTime = System.currentTimeMillis();
		DingdingMsgVO vo = dingdingMq.toMsg(appService.getApp(dingdingMq.getAppCode()));
		// 接收时间
		vo.setAcceptTimeMills(acceptTime);
		// 设置首次执行时间,默认取接收时间,有延时的取计算后的延时时间
		vo.setNextSendTimeMills(vo.toEntity().isFirstNeedDelay() ? vo.toEntity().getToSendTimeMills(0) : acceptTime);
		vo.setId(this.create(vo));
		return vo;
	}

	@Override
	public PageModel<DingdingMsgVO> findToSendMsg(DingdingMsgParams params) {
		Page<DingdingMsgVO> pg = PageHelper.startPage(PageThreadLocal.getParams().getPageNo(),
				PageThreadLocal.getParams().getPageSize(), true);
		List<DingdingMsgVO> volist = query.selectToSendMsgByPage(params);
		PageModel<DingdingMsgVO> page = toPage(pg.toPageInfo());
		page.setData(volist);
		return page;
	}

	@Override
	public void sendSuccess(DingdingMsgVO dingdingMsg, long beginTimeMills, String returnJson) {
		long currTimeMills = System.currentTimeMillis();
		// 添加推送记录
		DingdingMsgSendRecordVO record = DingdingMsgSendRecordVO.buildRecord(dingdingMsg, beginTimeMills, currTimeMills,
				returnJson);
		record.setSendStatus(SendStatus.SUCCESS.code());
		// TODO 登录逻辑
		record.setSendType(SendType.AUTO.code());
		record.setSenderErp("MNC");
		record.setSenderName("MNC");
		dingdingMsgSendRecordService.create(record);
		// 更新消息状态
		if (dingdingMsg.isInitial()) {
			dingdingMsg.setFirstSendTimeMills(beginTimeMills);
		}
		if (!dingdingMsg.isSuccess()) {
			dingdingMsg.setFirstSendSuccessTimeMills(currTimeMills);
			dingdingMsg.setSendStatus(SendStatus.SUCCESS.code());
			dingdingMsg.setNextSendTimeMills(0L);
		}

		dingdingMsg.addRetryCount();
		modify(dingdingMsg);
	}

	@Override
	public void sendFailure(DingdingMsgVO dingdingMsg, long beginTimeMills, String errMsg) {
		long currTimeMills = System.currentTimeMillis();
		// 添加推送记录
		DingdingMsgSendRecordVO record = DingdingMsgSendRecordVO.buildRecord(dingdingMsg, beginTimeMills, currTimeMills,
				errMsg);
		record.setSendStatus(SendStatus.FAILURE.code());
		// TODO 登录逻辑
		record.setSendType(SendType.AUTO.code());
		record.setSenderErp("MNC");
		record.setSenderName("MNC");
		dingdingMsgSendRecordService.create(record);

		// 获取应用和队列信息
		MessageQueueVO queue = appService.messageQueueService().getMessageQueue(dingdingMsg.getAppCode(),
				dingdingMsg.getQueueName(), dingdingMsg.getRouteKey());
		AppVO app = dingdingMsg.getApp();
		if (null == app) {
			app = appService.getApp(dingdingMsg.getAppCode());
			dingdingMsg.setApp(app);
		}
		dingdingMsg.addRetryCount();
		// 更新消息状态
		if (dingdingMsg.isInitial()) {
			dingdingMsg.setFirstSendTimeMills(beginTimeMills);
			dingdingMsg.setSendStatus(SendStatus.SENDING.code());
			// 设置下次执行时间
			dingdingMsg.setNextSendTimeMills(
					StringUtils.isNotBlank(dingdingMsg.getRetryInterval()) ? dingdingMsg.toEntity().getToSendTimeMills()
							: dingdingMsg.toEntity().getToSendTimeMills(dingdingMsg.getRetryCount() - 1,
									Message.DEFAULT_MSG_DELAY_INTERVAL));
		} else if (dingdingMsg.isSending()) {
			// 默认
			dingdingMsg.setNextSendTimeMills(0L);
			dingdingMsg.setSendStatus(SendStatus.FAILURE.code());
			if (null != queue && dingdingMsg.getRetryCount() < queue.getMaxRetryCount()) {
				// 优先按队列配置计算重试次数
				dingdingMsg.setSendStatus(SendStatus.SENDING.code());
				// 设置下次执行时间
				dingdingMsg.setNextSendTimeMills(dingdingMsg.toEntity().getToSendTimeMills(queue.getRetryIntervals()));
			} else if (StringUtils.isNotBlank(dingdingMsg.getRetryInterval())
					&& (dingdingMsg.getRetryCount() < dingdingMsg.getRetryInterval().split(",").length
							&& dingdingMsg.getRetryCount() < Message.MAX_MSG_RETRY_COUNT)) {
				// 客户端传重试间隔,重试次不能超过30
				dingdingMsg.setSendStatus(SendStatus.SENDING.code());
				// 设置下次执行时间
				dingdingMsg.setNextSendTimeMills(dingdingMsg.toEntity().getToSendTimeMills());
			} else if (StringUtils.isBlank(dingdingMsg.getRetryInterval())
					&& (dingdingMsg.getRetryCount() - 1) <= Message.DEFAULT_MSG_RETRY_COUNT) {
				// 默认重试间隔，重试次数3次
				dingdingMsg.setSendStatus(SendStatus.SENDING.code());
				// 设置下次执行时间
				dingdingMsg.setNextSendTimeMills(dingdingMsg.toEntity()
						.getToSendTimeMills(dingdingMsg.getRetryCount() - 1, Message.DEFAULT_MSG_DELAY_INTERVAL));
			}
		}
		modify(dingdingMsg);
		// TODO 回调
	}
}
