package com.example.userservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Value("${jwt.token.access.name}")
	private String accessTokenName;
	@Value("${jwt.token.refresh.name}")
	private String refreshTokenName;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.cors(AbstractHttpConfigurer::disable)
				.formLogin(login ->
						login
								.loginPage("/ss/login")
								.loginProcessingUrl("/login/loginProc")
								.usernameParameter("user_id")
								.passwordParameter("password")
								.successForwardUrl("/login/loginSuccess")
								.failureForwardUrl("/login/loginFail"))
				.logout(logout ->
						logout
								.logoutUrl("/user/logout")
								.logoutSuccessUrl("/ss/login")
								.deleteCookies(accessTokenName, refreshTokenName))
				.sessionManagement(session ->
						session
								.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		return http.build();
	}
}
