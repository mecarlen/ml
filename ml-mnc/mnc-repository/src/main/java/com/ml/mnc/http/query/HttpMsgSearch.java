package com.ml.mnc.http.query;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.ml.mnc.http.domain.HttpMsgVO;

/**
 * <pre>
 * <b>HTTP消息检索</b>
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2020年3月3日 下午3:47:57
 */
public interface HttpMsgSearch extends ElasticsearchRepository<HttpMsgVO, Long> {

}
