package com.ml.mnc.http.job;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ml.base.query.PageParams;
import com.ml.mnc.http.HttpMsg;
import com.ml.mnc.http.HttpMsgSender;
import com.ml.mnc.http.domain.HttpMsgSendRecordVO;
import com.ml.mnc.http.domain.HttpMsgVO;
import com.ml.mnc.http.service.HttpMsgService;
import com.ml.util.cache.CacheUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 签收未推送的http消息推送任务
 * 
 * 描述
 * 1、签收的消息先进缓存
 * 2、未处理的消息有即时推送和延时推送两种
 * 3、签收未推送造成因：发版重启签收未及时推送，实例异常宕机签收未及时推送
 * 4、由于清除缓存是正向流程最后一步，有可能推送得未及时处理后续动作，所以这里可能会有重复推送的情况
 * </pre>
 * @author mecarlen.Wang 2019年12月18日 下午4:30:21
 */
@Slf4j
@JobHandler("ackNoSendHttpMsgSendJob")
@Component
public class AckNoSendHttpMsgSendJob extends IJobHandler {
	@Resource
	private HttpMsgService httpMsgService;
	@Resource
	private HttpMsgSender httpMsgSender;
	@Resource
	private CacheUtils cacheUtils;
	@Value("${okhttp.client.readTimeout}")
	private Long maxSendUseTimeMills;
	@Override
	public ReturnT<String> execute(String jobParams) throws Exception {
		final long currTimeMills = System.currentTimeMillis();
		int pageSize = 200;
		if (StringUtils.isNotBlank(jobParams)) {
			PageParams<?> params = JSON.parseObject(jobParams, PageParams.class);
			pageSize = params.getPageSize();
		}
		XxlJobLogger.log("ackNoSendHttpMsgSend begin");
		log.info("ackNoSendHttpMsgSend begin,params:{}", jobParams);
		Map<String,HttpMsgVO> msgMap = cacheUtils.hGetAll(HttpMsg.HTTP_MSG_CACHE_KEY,HttpMsgVO.class);
		int sendCount = 0;
		if(!msgMap.isEmpty()) {
			List<String> uidlist = Lists.newArrayList();
			Set<Entry<String,HttpMsgVO>> msgSet = msgMap.entrySet();
			for(Entry<String,HttpMsgVO> entry:msgSet) {
				//检查是否推送中
				String sendTimeMillsStr = cacheUtils.hGet(HttpMsg.HTTP_MSG_SENDING_CACHE_KEY, entry.getKey());
				long sendTimeMills = StringUtils.isNotBlank(sendTimeMillsStr)?Long.valueOf(sendTimeMillsStr):currTimeMills + maxSendUseTimeMills;
				if((currTimeMills-sendTimeMills) < maxSendUseTimeMills * 3) {
					//推送用时还不到阀值则直接跳过
					log.info("httpMsg sendUseTime<maxSendUseTimeMills:{},ackNoSendHttpMsg send skip uid:{}",maxSendUseTimeMills,entry.getKey());
					continue;
				}
				uidlist.add(entry.getKey());
				sendCount++;
				//检查是否推送过
				List<HttpMsgSendRecordVO> srlist = httpMsgService.sendRecordService().findByBusinessUid(entry.getKey());
				if(!srlist.isEmpty()) {
					//当已经有推送记录则不推
					log.info("httpMsg has been sended or sendUseTime<maxSendUseTimeMills:{},ackNoSendHttpMsg send skip uid:{}",maxSendUseTimeMills,entry.getKey());
					continue;
				}
				HttpMsgVO httpMsg = entry.getValue();
				if(!httpMsg.toEntity().isNeedDelay(currTimeMills)) {
					if(!httpMsgSender.isCloseImmediatelySend(httpMsg)) {
						//目标URL正常时直接推送
						httpMsgSender.send(httpMsg);
					} else {
						//URL当前不可用直接入库
						httpMsgService.sendFailure(httpMsg, httpMsg.getAcceptTimeMills(), "IOException");
					}
				} else {
					//延时推送的直接入库
					httpMsgService.create(httpMsg);
				}
				if(sendCount>pageSize) {
					log.info("ackNoSendHttpMsgCount:{} greatter than pageSize:{}",msgMap.size(),pageSize);
					break;
				}
			}
			//批量删除
			cacheUtils.hDel(HttpMsg.HTTP_MSG_CACHE_KEY, uidlist.toArray(new String[uidlist.size()]));
			log.info("ackNoSendHttpMsgJob consumer num:{}",uidlist.size());
		}
		log.info("ackNoSendHttpMsgSend over,params:{}", jobParams);
		XxlJobLogger.log("ackNoSendHttpMsgSend over");
		return SUCCESS;
	}

	
	
}
