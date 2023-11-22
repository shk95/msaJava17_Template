package com.example.apigateway.config;

import com.example.apigateway.filter.JwtAuthenticationFilter;
import com.example.apigateway.handler.AccessDeniedHandler;
import com.example.apigateway.handler.LoginServerAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	private final AccessDeniedHandler accessDeniedHandler;
	private final LoginServerAuthenticationEntryPoint loginServerAuthenticationEntryPoint;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public SecurityWebFilterChain filterChain(ServerHttpSecurity httpSecurity) {
		httpSecurity
				.csrf(ServerHttpSecurity.CsrfSpec::disable)
				.cors(ServerHttpSecurity.CorsSpec::disable)
				.formLogin(ServerHttpSecurity.FormLoginSpec::disable)
				.exceptionHandling(exceptionHandlingSpec
						-> exceptionHandlingSpec.accessDeniedHandler(accessDeniedHandler))
				.exceptionHandling(exceptionHandlingSpec
						-> exceptionHandlingSpec.authenticationEntryPoint(loginServerAuthenticationEntryPoint))
				.securityContextRepository(NoOpServerSecurityContextRepository.getInstance());
		httpSecurity
				.authorizeExchange(authz ->
						authz
								.pathMatchers("/notice/**").hasAnyAuthority("ROLE_USER")
								.pathMatchers("/user/**").hasAnyAuthority("ROLE_USER")
								.anyExchange().permitAll());
		httpSecurity.addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.HTTP_BASIC);
		return httpSecurity.build();
	}
}
