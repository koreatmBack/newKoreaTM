package com.example.smsSpringTest.repository;

import com.example.smsSpringTest.entity.JobsiteUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * author : 신기훈
 * date : 2024-10-29
 * comment : JPA 회원 조회 Repository (JWT 용)
 */
public interface UserRepository extends JpaRepository<JobsiteUserEntity, Long> {
    Optional<JobsiteUserEntity> findByUserId(String userId);
}
