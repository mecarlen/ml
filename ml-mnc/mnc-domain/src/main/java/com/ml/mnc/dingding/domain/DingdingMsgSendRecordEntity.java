package com.ml.mnc.dingding.domain;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ml.base.domain.BaseEntity;
import com.ml.mnc.dingding.DingdingMsgSendRecord;

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
public class DingdingMsgSendRecordEntity extends BaseEntity<Long, DingdingMsgSendRecordVO> implements DingdingMsgSendRecord {
	private String businessUid;
	private Long beginTimeMills;
	private Long endTimeMills;
	private Integer sendStatus;
	private Integer sendType;
	private String senderErp;
	private String senderName;
	private String remark;

	@Override
	public DingdingMsgSendRecordEntity id(Long id) {
		this.setId(id);
		return this;
	}

	@Override
	public DingdingMsgSendRecordEntity state(State state) {
		if (null != state) {
			this.setYn(state.code());
		}
		return this;
	}

	@Override
	public DingdingMsgSendRecordEntity createTime(Date createTime) {
		if (null != createTime) {
			this.setCreateTime(new Date(createTime.getTime()));
		}
		return this;
	}

	@Override
	public DingdingMsgSendRecordEntity updateTime(Date updateTime) {
		if (null != updateTime) {
			this.setUpdateTime(new Date(updateTime.getTime()));
		}
		return this;
	}

	@Override
	public DingdingMsgSendRecordVO toVO() {
		return DingdingMsgSendRecordVO.getInstance(this);
	}

	final public static DingdingMsgSendRecordEntity getInstance(DingdingMsgSendRecord msgSendRecord) {
		if (null == msgSendRecord) {
			return null;
		}
		DingdingMsgSendRecordEntity target = new DingdingMsgSendRecordEntity();
		BeanUtils.copyProperties(msgSendRecord, target);
		return target;
	}

}
