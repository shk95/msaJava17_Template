package com.example.userservice.repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
@Cacheable
@Entity
@Table(name = "USER_INFO")
public class UserInfoEntity {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "USER_ID")
	private String userid;
	@NotNull
	@Column(name = "USER_PASSWORD")
	private String password;
	@NotNull
	@Column(name = "USER_NAME")
	private String username;
	@NotNull
	@Column(name = "USER_EMAIL", nullable = false)
	private String email;
	@NotNull
	@Column(name = "USER_ADDR1", nullable = false)
	private String addr1;
	@Column(name = "USER_ADDR2", nullable = false)
	private String addr2;
	@Column(name = "USER_REG_ID", updatable = false)
	private String regId;
	@Column(name = "USER_REG_DT", updatable = false)
	private String regDt;
	@Column(name = "USER_CHG_ID")
	private String chgId;
	@Column(name = "USER_CHG_DT")
	private String chgDt;
	@Column(name = "USER_ROLES")
	private String roles;
}
