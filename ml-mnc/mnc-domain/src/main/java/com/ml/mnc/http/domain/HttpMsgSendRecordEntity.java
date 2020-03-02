package com.ml.mnc.http.domain;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ml.base.domain.BaseEntity;
import com.ml.mnc.http.HttpMsgSendRecord;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 消息推送记录
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月4日 下午4:28:54
 */
@Setter
@Getter
public class HttpMsgSendRecordEntity extends BaseEntity<Long, HttpMsgSendRecordVO> implements HttpMsgSendRecord {
	private String businessUid;
	private Long beginTimeMills;
	private Long endTimeMills;
	private Integer sendStatus;
	private Integer sendType;
	private String senderErp;
	private String senderName;
	private String remark;

	@Override
	public HttpMsgSendRecordEntity id(Long id) {
		this.setId(id);
		return this;
	}

	@Override
	public HttpMsgSendRecordEntity state(State state) {
		if (null != state) {
			this.setYn(state.code());
		}
		return this;
	}

	@Override
	public HttpMsgSendRecordEntity createTime(Date createTime) {
		if (null != createTime) {
			this.setCreateTime(new Date(createTime.getTime()));
		}
		return this;
	}

	@Override
	public HttpMsgSendRecordEntity updateTime(Date updateTime) {
		if (null != updateTime) {
			this.setUpdateTime(new Date(updateTime.getTime()));
		}
		return this;
	}

	@Override
	public HttpMsgSendRecordVO toVO() {
		return HttpMsgSendRecordVO.getInstance(this);
	}

	final public static HttpMsgSendRecordEntity getInstance(HttpMsgSendRecord msgSendRecord) {
		if (null == msgSendRecord) {
			return null;
		}
		HttpMsgSendRecordEntity target = new HttpMsgSendRecordEntity();
		BeanUtils.copyProperties(msgSendRecord, target);
		return target;
	}

}
