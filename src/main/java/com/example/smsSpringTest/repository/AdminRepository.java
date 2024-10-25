package com.example.smsSpringTest.repository;

import com.example.smsSpringTest.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * author : 신기훈
 * date : 2024-10-24
 * comment : JPA 회원 조회 Repository (JWT 용)
 */

public interface AdminRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByUserId(String userId);
}
