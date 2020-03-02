package com.ml.mnc.http.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ml.base.query.PageModel;
import com.ml.base.query.PageThreadLocal;
import com.ml.base.service.impl.BaseServiceImpl;
import com.ml.mnc.http.domain.HttpCallbackMsgEntity;
import com.ml.mnc.http.domain.HttpCallbackMsgParams;
import com.ml.mnc.http.domain.HttpCallbackMsgVO;
import com.ml.mnc.http.query.HttpCallbackMsgQuery;
import com.ml.mnc.http.repository.HttpCallbackMsgRepository;
import com.ml.mnc.http.service.HttpCallbackMsgService;

/**
 * <pre>
 * http回调消息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月23日 下午3:43:51
 */
@Service("httpCallbackMsgService")
public class HttpCallbackMsgServiceImpl extends
		BaseServiceImpl<Long, HttpCallbackMsgEntity, HttpCallbackMsgVO, HttpCallbackMsgRepository, HttpCallbackMsgQuery>
		implements HttpCallbackMsgService {
	
	@Resource
	public void setHttpCallbackMsgRepository(HttpCallbackMsgRepository httpCallbackMsgRepository) {
		this.setRepository(httpCallbackMsgRepository);
	}

	@Resource
	public void setHttpCallbackMsgQuery(HttpCallbackMsgQuery httpCallbackMsgQuery) {
		this.setQuery(httpCallbackMsgQuery);
	}
	
	@Override
	public void save(List<HttpCallbackMsgVO> callbackMsglist) {
		if(callbackMsglist.isEmpty()) {
			return ;
		}
		this.repository.batchInsert(callbackMsglist.stream().map(HttpCallbackMsgEntity::getInstance).collect(Collectors.toList()));
	}

	@Override
	public PageModel<HttpCallbackMsgVO> findToSendMsg(HttpCallbackMsgParams params) {
		Page<HttpCallbackMsgVO> pg = PageHelper.startPage(PageThreadLocal.getParams().getPageNo(),
				PageThreadLocal.getParams().getPageSize(), true);
		List<HttpCallbackMsgVO> volist = query.selectToSendMsgByPage(params);
		PageModel<HttpCallbackMsgVO> page = toPage(pg.toPageInfo());
		page.setData(volist);
		return page;
	}
}
