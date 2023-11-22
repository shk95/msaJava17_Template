package com.example.userservice.service.impl;

import com.example.userservice.repository.UserInfoRepository;
import com.example.userservice.auth.AuthInfo;
import com.example.userservice.dto.UserInfoDto;
import com.example.userservice.service.IUserInfoSsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserInfoSsService implements IUserInfoSsService {

	private final UserInfoRepository userInfoSsRepository;

	@Transactional(readOnly = true)
	@Override
	public boolean isUserExists(UserInfoDto userInfoDto) {
		return userInfoSsRepository.findByUserid(userInfoDto.userid()).isPresent();
	}

	@Transactional
	@Override
	public boolean insertUserInfo(UserInfoDto userInfoDto) {
		if (this.isUserExists(userInfoDto)) {
			return false;
		}
		userInfoSsRepository.save(UserInfoDto.toEntity(userInfoDto));
		return true;
	}

	@Transactional(readOnly = true)
	@Override
	public UserInfoDto getUserInfo(UserInfoDto userInfoDto) {
		return userInfoSsRepository.findByUserid(userInfoDto.userid())
				.map(UserInfoDto::toDto)
				.orElse(null);
	}

	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userInfoSsRepository.findByUserid(username)
				.map(userInfoEntity -> new AuthInfo(UserInfoDto.toDto(userInfoEntity)))
				.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
	}
}
