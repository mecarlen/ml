package com.ml.msf.service;

import com.ml.msf.entity.UserDTO;

/**
 * <pre>
 * <b>用户</b>
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2020年3月30日 下午4:06:48
 */
public interface UserService {
	
	/**
	 * <pre>
	 * <b>根据登录名取用户</b>
	 * </pre>
	 * @param loginName String
	 * @return UserDTO
	 * */
	UserDTO findByLoginName(String loginName);
}
