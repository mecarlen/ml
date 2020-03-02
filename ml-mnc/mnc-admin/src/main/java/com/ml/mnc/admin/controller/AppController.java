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
import com.ml.mnc.admin.domain.AppVO;
import com.ml.mnc.admin.service.AppService;

/**
 * <pre>
 * <b>应用操作</b>
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2020年3月2日 下午4:55:53
 */
@RestController
@RequestMapping(value = "/app", produces = { MediaType.APPLICATION_JSON_VALUE })
public class AppController {

	@Resource
	private AppService appService;

	/**
	 * 保存单条数据.
	 *
	 * @param condition AppVO
	 *
	 * @return WebResponse<AppVO>
	 */
	@PostMapping(value = "/store", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public WebResponse<AppVO> store(@RequestBody AppVO condition) {
		WebResponse<AppVO> response = new WebResponse<>();

		Long id = appService.create(condition);
		condition.setId(id);

		return response.result(condition);
	}

	/**
	 * 更新某条记录.
	 *
	 * @param id   long
	 * @param data AppVO
	 *
	 * @return WebResponse<AppVO>
	 */
	@PostMapping(value = "/{id}/update", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public WebResponse<AppVO> update(@PathVariable("id") long id, @RequestBody AppVO data) {
		WebResponse<AppVO> response = new WebResponse<>();

		AppVO appVO = appService.find(id);
		if (appVO == null || id != data.getId()) {
			response.code(BussinessException.BizErrorCode.INVALID_PARAM);

			return response;
		}

		appService.modify(data);

		return response.result(data);
	}

	/**
	 * 删除.
	 *
	 * @param id long
	 *
	 * @return WebResponse<AppVO>
	 */
	@PostMapping(value = "/{id}/destroy")
	public WebResponse<AppVO> destroy(@PathVariable("id") long id) {
		WebResponse<AppVO> response = new WebResponse<>();

		AppVO appVO = appService.find(id);
		if (appVO == null) {
			response.code(BussinessException.BizErrorCode.INVALID_PARAM);

			return response;
		}

		appService.remove(appVO);

		return response.result(appVO);
	}
}
