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
import com.ml.mnc.admin.domain.AppVO;
import com.ml.mnc.admin.service.AppService;

/**
 * <pre>
 * <b>应用展示</b>
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2020年3月2日 下午4:55:53
 */
@RestController
@RequestMapping(value = "/app/display", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AppDisplayController {

    @Resource
    private AppService appService;

    /**
     * 查询列表.
     *
     * @param currentPage long
     * @param pageSize long
     * @param condition AppVO
     *
     * @return WebResponse<PageModel>
     */
    @GetMapping(value = "/page/{currentPage}/{pageSize}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public WebResponse<PageModel<AppVO>> index(
            @PathVariable("currentPage") long currentPage,
            @PathVariable("pageSize") long pageSize,
            @RequestBody AppVO condition) {
        WebResponse<PageModel<AppVO>> response = new WebResponse<>();

        return response.result(appService.findByPage(condition));
    }

    /**
     * 查询所有数据.
     *
     * @param condition AppVO
     *
     * @return WebResponse<List<AppVO>>
     */
    @GetMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public WebResponse<List<AppVO>> index(@RequestBody AppVO condition) {
        WebResponse<List<AppVO>> response = new WebResponse<>();

        List<AppVO> data = appService.find(condition);
        return response.result(data);
    }

    /**
     * 查询某条详细信息.
     *
     * @return WebResponse<AppVO>
     */
    @GetMapping(value = "/{id}")
    public WebResponse<AppVO> show(@PathVariable("id") long id) {
        WebResponse<AppVO> response = new WebResponse<>();

        return response.result(appService.find(id));
    }
}
