package com.example.smsSpringTest.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * author : 신기훈
 * date : 2024-10-21
 * comment : 로그인을 위한 전용 Jpa Entity
 */
@Getter
@NoArgsConstructor
@Entity
@Table(name = "formmail_user_profile")
public class Member extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = true, name = "user_id")
    private String userId;

    @Column(nullable = true, name = "user_pwd")
    private String userPwd;

    @Column(nullable = true, name = "user_name")
    private String userName;

    @Column(name = "role")
    private String role;

}
