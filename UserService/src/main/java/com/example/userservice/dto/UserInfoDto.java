package com.example.userservice.dto;

import com.example.userservice.repository.entity.UserInfoEntity;
import lombok.Builder;

@Builder
public record UserInfoDto(

		String userid,
		String username,
		String password,
		String email,
		String addr1,
		String addr2,
		String regId,
		String regDt,
		String chgId,
		String chgDt,
		String roles
) {

	public static UserInfoEntity toEntity(UserInfoDto userInfoDto) {
		return UserInfoEntity.builder()
				.userid(userInfoDto.userid())
				.username(userInfoDto.username())
				.password(userInfoDto.password())
				.email(userInfoDto.email())
				.addr1(userInfoDto.addr1())
				.addr2(userInfoDto.addr2())
				.regId(userInfoDto.regId())
				.regDt(userInfoDto.regDt())
				.chgId(userInfoDto.chgId())
				.chgDt(userInfoDto.chgDt())
				.roles(userInfoDto.roles())
				.build();
	}

	public static UserInfoDto toDto(UserInfoEntity userInfoEntity) {
		return UserInfoDto.builder()
				.userid(userInfoEntity.getUserid())
				.username(userInfoEntity.getUsername())
				.password(userInfoEntity.getPassword())
				.email(userInfoEntity.getEmail())
				.addr1(userInfoEntity.getAddr1())
				.addr2(userInfoEntity.getAddr2())
				.regId(userInfoEntity.getRegId())
				.regDt(userInfoEntity.getRegDt())
				.chgId(userInfoEntity.getChgId())
				.chgDt(userInfoEntity.getChgDt())
				.roles(userInfoEntity.getRoles())
				.build();
	}
}
