package com.example.smsSpringTest.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * author : 신기훈
 * date : 2024-09-20
 * comment : ADMIN entity
 */

@Data
@NoArgsConstructor
@Entity
@Table(name = "formmail_admin")
public class FormMailAdminEntity {

    @Id
    @Column(name = "user_id")
    private String userId;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Column(name = "user_pwd")
    private String userPwd;

    @NotBlank(message = "본명은 필수입니다.")
    @JsonProperty("rName")
    @Column(name = "r_name")
    private String rName;

    @Column(name = "user_name")
    private String userName;

    @NotBlank(message = "직책은 필수입니다.")
    @Column(name = "position")
    private String position;

    @Column(name = "role")
    private String role;

    @NotBlank(message = "팀 등록은 필수입니다.")
    @Column(name = "team")
    private String team;

    @NotBlank(message = "업무용 번호는 필수입니다.")
    @Column(name = "m_phone")
    @JsonProperty("mPhone")
    private String mPhone;

    @NotBlank(message = "개인 연락처는 필수입니다.")
    @Column(name = "r_phone")
    @JsonProperty("rPhone")
    private String rPhone;

    @Column(name = "form_no")
    @JsonProperty("formNo")
    private String formNo;

    @Column(name = "rank")
    @JsonProperty("rank")
    private String rank;

    @Column(name = "use_status")
    @JsonProperty("useStatus")
    private boolean useStatus;

}
