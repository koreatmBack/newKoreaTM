package com.example.smsSpringTest.repository;

import com.example.smsSpringTest.entity.CafeconUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * author : 신기훈
 * date : 2025-01-17
 * comment : JPA 카페콘 회원 조회 Repository (JWT 용)
 */
public interface CafeconUserRepository extends JpaRepository<CafeconUserEntity, Long> {
    Optional<CafeconUserEntity> findByUserId(String userId);
}
