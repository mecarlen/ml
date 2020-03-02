package com.ml.mnc.dingding.domain;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ml.base.domain.BaseVO;
import com.ml.mnc.dingding.DingdingMsgSendRecord;

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
public class DingdingMsgSendRecordVO extends BaseVO<Long, DingdingMsgSendRecordEntity> implements DingdingMsgSendRecord {
	private String businessUid;
	private Long beginTimeMills;
	private Long endTimeMills;
	private Integer sendStatus;
	private Integer sendType;
	private String senderErp;
	private String senderName;
	private String remark;

	@Override
	public DingdingMsgSendRecordVO id(Long id) {
		this.setId(id);
		return this;
	}

	@Override
	public DingdingMsgSendRecordVO state(State state) {
		if (null != state) {
			this.setYn(state.code());
		}
		return this;
	}

	@Override
	public DingdingMsgSendRecordVO createTime(Date createTime) {
		if (null != createTime) {
			this.setCreateTime(new Date(createTime.getTime()));
		}
		return this;
	}

	@Override
	public DingdingMsgSendRecordVO updateTime(Date updateTime) {
		if (null != updateTime) {
			this.setUpdateTime(new Date(updateTime.getTime()));
		}
		return this;
	}

	@Override
	public DingdingMsgSendRecordEntity toEntity() {
		return DingdingMsgSendRecordEntity.getInstance(this);
	}

	final public static DingdingMsgSendRecordVO getInstance(DingdingMsgSendRecord msgSendRecord) {
		if (null == msgSendRecord) {
			return null;
		}
		DingdingMsgSendRecordVO target = new DingdingMsgSendRecordVO();
		BeanUtils.copyProperties(msgSendRecord, target);
		return target;
	}
	
	public static DingdingMsgSendRecordVO buildRecord(DingdingMsgVO dingdingMsg, long beginTimeMills,long endTimeMills, String remark) {
		DingdingMsgSendRecordVO record = new DingdingMsgSendRecordVO();
		record.setBeginTimeMills(beginTimeMills);
		record.setEndTimeMills(endTimeMills);
		record.setBusinessUid(dingdingMsg.getBusinessUid());
		record.setRemark(remark);
		return record;
	}
}
