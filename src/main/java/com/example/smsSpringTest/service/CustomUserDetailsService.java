package com.example.smsSpringTest.service;

import com.example.smsSpringTest.entity.UserProfile;
import com.example.smsSpringTest.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * author : 신기훈
 * date : 2024-10-22
 * comment : Security UserDetailsService 상속 받은 회원 조회 Service
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

//    private final MemberRepository memberRepository;
      private final AdminRepository adminRepository;
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        return adminRepository.findByUserId(userId)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
    }

    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
//    private UserDetails createUserDetails(Member member) {
//
//
//        return User.builder()
//                .username(member.getUserId())
//                .password(member.getUserPwd())
//                .roles(member.getRole())
//                .build();
//    }

    // fm_admin 으로 변경하기 위해
    private UserDetails createUserDetails(UserProfile admin) {


        return User.builder()
                .username(admin.getUserId())
                .password(admin.getUserPwd())
                .roles("ADMIN")
                .build();
    }

}