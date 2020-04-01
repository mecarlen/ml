package com.ml.msf.service.impl;

import com.ml.msf.annotation.MsfService;
import com.ml.msf.entity.UserDTO;
import com.ml.msf.service.UserService;

/**
 * <pre>
 * <b>用户</b>
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2020年3月30日 下午4:24:17
 */
@MsfService
public class UserServiceImpl implements UserService {

	@Override
	public UserDTO findByLoginName(String loginName) {
		UserDTO user = new UserDTO();
		user.setLoginName(loginName);
		return user;
	}

}
