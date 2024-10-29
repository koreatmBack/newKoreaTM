package com.example.smsSpringTest.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * author : 신기훈
 * date : 2024-10-29
 * comment : 잡사이트 회원 entity , jpa용
 */

@Data
@NoArgsConstructor
@Entity
@Table(name = "jobsite_user")
public class JobsiteUserEntity {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_pwd")
    private String userPwd;

    @Column(name = "user_name")
    private String userName;

    private String phone;

    private String address;

    private String sido;

    private String sigungu;

    private String gender;

    private String birth;

    private String photo;

    private String marketing;
}
