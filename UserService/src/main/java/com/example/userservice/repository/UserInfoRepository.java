package com.example.userservice.repository;

import com.example.userservice.repository.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Long> {

	Optional<UserInfoEntity> findByUserid(String userid);

}
