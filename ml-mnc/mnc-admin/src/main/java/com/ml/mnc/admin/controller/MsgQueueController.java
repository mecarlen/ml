package com.ml.mnc.admin.controller;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ml.base.BussinessException;
import com.ml.base.controller.WebResponse;
import com.ml.mnc.admin.domain.MessageQueueVO;
import com.ml.mnc.admin.rpc.CTIEventMsgProducer;
import com.ml.mnc.admin.rpc.CTIEventMsgProducer.CTIEventMsg;
import com.ml.mnc.admin.rpc.MsgQueueRpcService;
import com.ml.mnc.admin.service.MessageQueueService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <pre>
 * <b>队列操作</b>
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2020年3月2日 下午4:55:53
 */
@Api(description = "消息队列操作API")
@RestController
@RequestMapping(value = "/queue", produces = { MediaType.APPLICATION_JSON_VALUE })
public class MsgQueueController {
	@Resource
	private MessageQueueService messageQueueService;
	@Resource
	private MsgQueueRpcService msgQueueRpcService;
	@Resource
	private CTIEventMsgProducer ctiEventMsgProducer;

	/**
	 * 保存单条数据.
	 *
	 * @param condition AppVO
	 *
	 * @return WebResponse<MessageQueueVO>
	 */
	@PostMapping(value = "/store")
	public WebResponse<MessageQueueVO> store(@RequestBody MessageQueueVO condition) {
		WebResponse<MessageQueueVO> response = new WebResponse<>();

		Long id = messageQueueService.create(condition);
		condition.setId(id);

		return response.result(condition);
	}

	/**
	 * 更新某条记录.
	 *
	 * @param id   long
	 * @param data AppVO
	 *
	 * @return WebResponse<MessageQueueVO>
	 */
	@PostMapping(value = "/{id}/update")
	public WebResponse<MessageQueueVO> update(@PathVariable("id") long id, @RequestBody MessageQueueVO data) {
		WebResponse<MessageQueueVO> response = new WebResponse<>();

		MessageQueueVO messageQueueVO = messageQueueService.find(id);
		if (messageQueueVO == null || id != data.getId()) {
			response.code(BussinessException.BizErrorCode.INVALID_PARAM);

			return response;
		}

		messageQueueService.modify(data);

		return response.result(data);
	}

	/**
	 * 删除.
	 *
	 * @param id long
	 *
	 * @return WebResponse<MessageQueueVO>
	 */
	@PostMapping(value = "/{id}/destroy")
	public WebResponse<MessageQueueVO> destroy(@PathVariable("id") long id) {
		WebResponse<MessageQueueVO> response = new WebResponse<>();

		MessageQueueVO messageQueueVO = messageQueueService.find(id);
		if (messageQueueVO == null) {
			response.code(BussinessException.BizErrorCode.INVALID_PARAM);

			return response;
		}

		messageQueueService.remove(messageQueueVO);

		return response.result(messageQueueVO);
	}

	/**
	 * <pre>
	 * 扩展消息队列
	 * </pre>
	 */
	@ApiOperation("扩展消息队列")
	@PostMapping(value = "/extend/{exchangeName}/{queueName}/{routeKey}")
	public WebResponse<Boolean> extendQueue(
			@ApiParam(value = "exchange名称", required = true) @PathVariable(name = "exchangeName", required = true) String exchangeName,
			@ApiParam(value = "queue名称", required = true) @PathVariable(name = "queueName", required = true) String queueName,
			@ApiParam(value = "routeKey", required = true) @PathVariable(name = "routeKey", required = true) String routeKey) {
		return msgQueueRpcService.extendQueue(exchangeName, queueName, routeKey);
	}

	/**
	 * <pre>
	 * 扩展消费者数
	 * 
	 * </pre>
	 */
	@ApiOperation("扩展消费者数")
	@PostMapping(value = "/extend/{appCode}/{queueName}/{concurrentConsumers}/{prefetchCount}")
	public WebResponse<Integer> extendConsumer(
			@ApiParam(value = "应用编码", required = true) @PathVariable(name = "appCode", required = true) String appCode,
			@ApiParam(value = "queue名称", required = true) @PathVariable(name = "queueName", required = true) String queueName,
			@ApiParam(value = "并发消费者数", required = true) @PathVariable(name = "concurrentConsumers", required = true) int concurrentConsumers,
			@ApiParam(value = "预取消息条数", required = true) @PathVariable(name = "prefetchCount", required = true) int prefetchCount) {
		return msgQueueRpcService.extendConsumer(appCode, queueName, concurrentConsumers, prefetchCount);
	}

	@ApiOperation(value = "手动推送MNC消息")
	@PostMapping("/manual/send/mnc")
	public WebResponse<Boolean> send(@RequestBody CTIEventMsg msg) {
		WebResponse<Boolean> response = new WebResponse<>();
		response.result(ctiEventMsgProducer.send(msg));
		return response;
	}
}
