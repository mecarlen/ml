package com.ml.mnc.http.repository;
/**
 * <pre>
 * http回调消息
 * 
 * </pre>
 * @author mecarlen.Wang 2019年9月23日 下午3:28:08
 */

import java.util.List;

import com.ml.base.repository.BaseRepository;
import com.ml.mnc.http.domain.HttpCallbackMsgEntity;

public interface HttpCallbackMsgRepository extends BaseRepository<Long, HttpCallbackMsgEntity> {
	/**
	 * <pre>
	 * 批理插入回调消息
	 * 
	 * </pre>
	 * @param callbackMsglist List<HttpCallbackMsgEntity>
	 * */
	void batchInsert(List<HttpCallbackMsgEntity> callbackMsglist);
}
