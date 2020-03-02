package com.ml.mnc.admin.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ml.base.controller.WebResponse;
import com.ml.base.query.PageModel;
import com.ml.mnc.admin.domain.MessageQueueVO;
import com.ml.mnc.admin.service.MessageQueueService;

/**
 * com.ml.mnc.admin.controller
 *
 * @author yansongda <me@yansongda.cn>
 * @version 2019/11/4 上午10:29
 */
@RestController
@RequestMapping(value = "/msgQueues", produces = {MediaType.APPLICATION_JSON_VALUE})
public class MsgQueueDisplayController {

    @Resource
    private MessageQueueService messageQueueService;

    /**
     * 查询列表.
     *
     * @param currentPage long
     * @param pageSize long
     * @param condition MessageQueueVO
     *
     * @return WebResponse<PageModel>
     */
    @GetMapping(value = "/page/{currentPage}/{pageSize}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public WebResponse<PageModel<MessageQueueVO>> index(
            @PathVariable("currentPage") long currentPage,
            @PathVariable("pageSize") long pageSize,
            @RequestBody MessageQueueVO condition) {
        WebResponse<PageModel<MessageQueueVO>> response = new WebResponse<>();

        return response.result(messageQueueService.findByPage(condition));
    }

    /**
     * 查询所有数据.
     *
     * @param condition MessageQueueVO
     *
     * @return WebResponse<List<MessageQueueVO>>
     */
    @GetMapping(value = "/", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public WebResponse<List<MessageQueueVO>> index(@RequestBody MessageQueueVO condition) {
        WebResponse<List<MessageQueueVO>> response = new WebResponse<>();

        return response.result(messageQueueService.find(condition));
    }

    /**
     * 查询某条详细信息.
     *
     * @return WebResponse<MessageQueueVO>
     */
    @GetMapping(value = "/{id}")
    public WebResponse<MessageQueueVO> show(@PathVariable("id") long id) {
        WebResponse<MessageQueueVO> response = new WebResponse<>();

        return response.result(messageQueueService.find(id));
    }
}
