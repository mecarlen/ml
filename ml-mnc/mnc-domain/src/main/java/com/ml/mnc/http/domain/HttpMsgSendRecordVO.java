package com.ml.mnc.http.domain;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ml.base.domain.BaseVO;
import com.ml.mnc.http.HttpMsgSendRecord;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 消息推送记录
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月4日 下午4:32:21
 */
@Setter
@Getter
public class HttpMsgSendRecordVO extends BaseVO<Long, HttpMsgSendRecordEntity> implements HttpMsgSendRecord {
	private String businessUid;
	private Long beginTimeMills;
	private Long endTimeMills;
	private Integer sendStatus;
	private Integer sendType;
	private String senderErp;
	private String senderName;
	private String remark;

	@Override
	public HttpMsgSendRecordVO id(Long id) {
		this.setId(id);
		return this;
	}

	@Override
	public HttpMsgSendRecordVO state(State state) {
		if (null != state) {
			this.setYn(state.code());
		}
		return this;
	}

	@Override
	public HttpMsgSendRecordVO createTime(Date createTime) {
		if (null != createTime) {
			this.setCreateTime(new Date(createTime.getTime()));
		}
		return this;
	}

	@Override
	public HttpMsgSendRecordVO updateTime(Date updateTime) {
		if (null != updateTime) {
			this.setUpdateTime(new Date(updateTime.getTime()));
		}
		return this;
	}

	@Override
	public HttpMsgSendRecordEntity toEntity() {
		return HttpMsgSendRecordEntity.getInstance(this);
	}

	final public static HttpMsgSendRecordVO getInstance(HttpMsgSendRecord msgSendRecord) {
		if (null == msgSendRecord) {
			return null;
		}
		HttpMsgSendRecordVO target = new HttpMsgSendRecordVO();
		BeanUtils.copyProperties(msgSendRecord, target);
		return target;
	}
	
	public static HttpMsgSendRecordVO buildRecord(HttpMsgVO httpMsg, long beginTimeMills,long endTimeMills, String remark) {
		HttpMsgSendRecordVO record = new HttpMsgSendRecordVO();
		record.setBeginTimeMills(beginTimeMills);
		record.setEndTimeMills(endTimeMills);
		record.setBusinessUid(httpMsg.getBusinessUid());
		record.setRemark(remark);
		return record;
	}
}
