package com.ml.mnc.admin.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ml.base.controller.WebResponse;
import com.ml.mnc.admin.rpc.DingdingMsgQueueRpcService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <pre>
 * 钉钉消息队列操作
 * 
 * </pre>
 * @author mecarlen.Wang 2019年11月5日 上午10:50:38
 */
@Api(tags = { "钉钉消息队列操作API" })
@RestController
@RequestMapping(value = "/queue", produces = { "application/json" })
public class DingdingMsgQueueController {
	@Resource
	private DingdingMsgQueueRpcService dingdingMsgQueueRpcService;
	
	/**
	 * <pre>
	 * 扩展消费者数
	 * </pre>
	 */
	@ApiOperation("扩展消费者数")
	@PostMapping(value = "/extend/{queueName}/{concurrentConsumers}/{prefetchCount}")
	public WebResponse<Boolean> extendConsumer(@PathVariable(name = "queueName", required = true) String queueName,
			@PathVariable(name = "concurrentConsumers", required = true) int concurrentConsumers,
			@PathVariable(name = "prefetchCount", required = true) int prefetchCount) {
		return dingdingMsgQueueRpcService.extendConsumer(queueName, concurrentConsumers, prefetchCount);
	}
}
