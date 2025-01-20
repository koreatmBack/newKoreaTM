package com.example.smsSpringTest.repository;

import com.example.smsSpringTest.entity.FormMailAdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * author : 신기훈
 * date : 2024-10-24
 * comment : JPA 관리자 조회 Repository (JWT 용)
 */

public interface AdminRepository extends JpaRepository<FormMailAdminEntity, Long> {

    Optional<FormMailAdminEntity> findByUserId(String userId);
}
