package com.ml.mnc.dingding.job;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.ml.base.query.PageModel;
import com.ml.base.query.PageParams;
import com.ml.base.query.PageThreadLocal;
import com.ml.mnc.dingding.DingdingMsgSender;
import com.ml.mnc.dingding.domain.DingdingMsgParams;
import com.ml.mnc.dingding.domain.DingdingMsgVO;
import com.ml.mnc.dingding.service.DingdingMsgService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.core.util.ShardingUtil;
import com.xxl.job.core.util.ShardingUtil.ShardingVO;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 钉钉消息推送任务
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月9日 上午10:18:59
 */
@Slf4j
@JobHandler("dingdingMsgSendJob")
@Component
public class DingdingMsgSendJob extends IJobHandler {
	@Resource
	private DingdingMsgService dingdingMsgService;
	@Resource
	private DingdingMsgSender dingdingMsgSender;
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		final long currTimeMills = System.currentTimeMillis();
		DingdingMsgParams params = null;
		PageParams<?> pageParams = null;
		if (StringUtils.isNotBlank(param)) {
			try {
				pageParams = JSON.parseObject(param, PageParams.class);
				params = JSON.parseObject(param, DingdingMsgParams.class);
			} catch (Exception ex) {
				log.warn("uscParamParseException,param:{}", param, ex);
			}
		} else {
			log.warn("uscParamIsEmpty,param:{}", param);
			pageParams = new PageParams<>();
			// 默认取50条件
			pageParams.setPageSize(50);
			pageParams.setPageNo(1);
		}
		if (null == params) {
			params = DingdingMsgParams.builder().build();
			params.setMaxRetryCount(10);
		}
		params.setCurrentSendTimeMills(currTimeMills);
		// 分片参数
		ShardingVO shardingParams = ShardingUtil.getShardingVo();
		if (null != shardingParams && shardingParams.getTotal() > 1) {
			params.setShardIdx(shardingParams.getIndex());
			params.setShards(shardingParams.getTotal());
			log.info("shardingParams:{}", JSON.toJSONString(shardingParams));
		}
		XxlJobLogger.log("dingdingMsgSend begin");
		log.info("dingdingMsgSend begin,params:{}", param);
		// 设置分页
		PageThreadLocal.setParams(pageParams);
		//查询任务列表
		PageModel<DingdingMsgVO> msglist = dingdingMsgService.findToSendMsg(params);
		//逐条推送
		msglist.getData().forEach(msg->dingdingMsgSender.send(msg,currTimeMills));
		log.info("dingdingMsgSend over,params:{}",param);
		XxlJobLogger.log("dingdingMsgSend over");
		return SUCCESS;
	}

}
