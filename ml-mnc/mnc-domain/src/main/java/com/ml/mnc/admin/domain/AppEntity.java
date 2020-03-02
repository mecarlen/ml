package com.ml.mnc.admin.domain;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ml.base.domain.BaseEntity;
import com.ml.mnc.admin.App;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 应用信息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月4日 下午5:22:00
 */
@Setter
@Getter
public class AppEntity extends BaseEntity<Long, AppVO> implements App {
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
	public AppEntity id(Long id) {
		this.setId(id);
		return this;
	}

	@Override
	public AppEntity state(State state) {
		if (null != state) {
			this.setYn(state.code());
		}
		return this;
	}

	@Override
	public AppEntity createTime(Date createTime) {
		if (null != createTime) {
			this.setCreateTime(createTime);
		}
		return this;
	}

	@Override
	public AppEntity updateTime(Date updateTime) {
		if (null != updateTime) {
			this.setUpdateTime(new Date(updateTime.getTime()));
		}
		return this;
	}

	@Override
	public AppVO toVO() {
		return AppVO.getInstance(this);
	}

	final public static AppEntity getInstance(App app) {
		if (null == app) {
			return null;
		}
		AppEntity target = new AppEntity();
		BeanUtils.copyProperties(app, target);
		return target;
	}
}
