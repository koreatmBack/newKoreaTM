package com.example.smsSpringTest.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * author : 신기훈
 * date : 2025-01-17
 * comment : 카페콘 회원 entity
 */

@Data
@NoArgsConstructor
@Entity
@Table(name = "cafecon_user")
public class CafeconUserEntity {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_pwd")
    private String userPwd;

    @JsonProperty("managerName")
    @Column(name = "manager_name")
    private String managerName;
}
