package com.example.smsSpringTest.model.response;

import com.example.smsSpringTest.entity.PhoneNum;
import com.example.smsSpringTest.entity.UserProfile;
import com.example.smsSpringTest.model.User;
import com.example.smsSpringTest.model.findUser;
import com.example.smsSpringTest.model.FormMailAdmin;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

/**
 * author : 신기훈
 * date : 2024-09-20
 * comment : 회원 조회 결과값 리턴
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse extends ApiResponse{

    private UserProfile userProfile;
    private FormMailAdmin formMailAdmin;
    private User user;
    private List<UserProfile> userList;
    private String position;
    private String rName;
    private String userName;
    private List<PhoneNum> phoneNumList;
    private List<findUser> findUserList;
    private int totalPages;
}
