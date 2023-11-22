package com.example.userservice.service;

import com.example.userservice.dto.UserInfoDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserInfoSsService extends UserDetailsService {

	boolean isUserExists(UserInfoDto userInfoDto);

	boolean insertUserInfo(UserInfoDto userInfoDto);

	UserInfoDto getUserInfo(UserInfoDto userInfoDto);
}
