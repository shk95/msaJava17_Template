package com.example.userservice.auth;

import com.example.userservice.dto.UserInfoDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public record AuthInfo(UserInfoDto userInfoDto) implements UserDetails {

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.stream(this.userInfoDto.roles().split(","))
				.distinct()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return this.userInfoDto.password();
	}

	@Override
	public String getUsername() {
		return this.userInfoDto.userid();
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.userInfoDto != null;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.userInfoDto != null;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.userInfoDto != null;
	}

	@Override
	public boolean isEnabled() {
		return this.userInfoDto != null;
	}
}
