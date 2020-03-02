package com.ml.mnc.admin.domain;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ml.base.domain.BaseVO;
import com.ml.mnc.admin.App;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 应用信息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月4日 下午5:22:09
 */
@Setter
@Getter
public class AppVO extends BaseVO<Long, AppEntity> implements App {
	private String plCode;
	private String plName;
	private String appCode;
	private String appName;
	private String ownerErp;
	private String ownerName;
	private String dingding;
	private String memberMobiles;
	private String memberEmails;
	private Integer alarmStatus;
	private Boolean needCallback;
	private String remark;

	@Override
	public AppVO id(Long id) {
		this.setId(id);
		return this;
	}

	@Override
	public AppVO state(State state) {
		if (null != state) {
			this.setYn(state.code());
		}
		return this;
	}

	@Override
	public AppVO createTime(Date createTime) {
		if (null != createTime) {
			this.setCreateTime(createTime);
		}
		return this;
	}

	@Override
	public AppVO updateTime(Date updateTime) {
		if (null != updateTime) {
			this.setUpdateTime(new Date(updateTime.getTime()));
		}
		return this;
	}

	@Override
	public AppEntity toEntity() {
		return AppEntity.getInstance(this);
	}

	final public static AppVO getInstance(App app) {
		if (null == app) {
			return null;
		}
		AppVO target = new AppVO();
		BeanUtils.copyProperties(app, target);
		return target;
	}
}
