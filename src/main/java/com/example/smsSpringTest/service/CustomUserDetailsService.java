package com.example.smsSpringTest.service;

import com.example.smsSpringTest.entity.CafeconUserEntity;
import com.example.smsSpringTest.entity.FormMailAdminEntity;
import com.example.smsSpringTest.entity.JobsiteUserEntity;
import com.example.smsSpringTest.repository.AdminRepository;
import com.example.smsSpringTest.repository.CafeconUserRepository;
import com.example.smsSpringTest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    private final UserRepository userRepository;
    private final CafeconUserRepository cafeconUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        String[] parts = username.split(":", 2);
        if (parts.length != 2) {
            throw new UsernameNotFoundException("잘못된 사용자 이름 형식입니다.");
        }

        String role = parts[0];
        String userId = parts[1];

        switch (role) {
            case "ADMIN":
                return findAdminDetails(userId)
                        .orElseThrow(() -> new UsernameNotFoundException("관리자를 찾을 수 없습니다."));
            case "USER":
                return findUserDetails(userId)
                        .orElseThrow(() -> new UsernameNotFoundException("일반 유저를 찾을 수 없습니다."));
            case "CAFECON":
                return findCafeconUserDetails(userId)
                        .orElseThrow(() -> new UsernameNotFoundException("Cafecon 유저를 찾을 수 없습니다."));
            default:
                throw new UsernameNotFoundException("잘못된 역할입니다.");
        }
    }

    private Optional<UserDetails> findAdminDetails(String userId) {
        return adminRepository.findByUserId(userId).map(this::createAdminDetails);
    }

    private Optional<UserDetails> findUserDetails(String userId) {
        return userRepository.findByUserId(userId).map(this::createUserDetails);
    }

    private Optional<UserDetails> findCafeconUserDetails(String userId) {
        return cafeconUserRepository.findByUserId(userId).map(this::createCafeconUserDetails);
    }

    // fm_admin 으로 변경하기 위해
    private UserDetails createAdminDetails(FormMailAdminEntity admin) {
        String role = admin.getTeam().equals("관리자") ? "ADMIN" : "MANAGER";

        return User.builder()
                .username(admin.getUserId())
                .password(admin.getUserPwd())
                .roles(role)
                .build();
    }

    // jobsite user 용
    private UserDetails createUserDetails(JobsiteUserEntity user) { // 일반 유저 엔티티
        return User.builder()
                .username(user.getUserId())
                .password(user.getUserPwd())
                .roles("USER")
                .build();
    }

    // Cafecon user 용
    private UserDetails createCafeconUserDetails(CafeconUserEntity user) { // 일반 유저 엔티티
        return User.builder()
                .username(user.getUserId())
                .password(user.getUserPwd())
                .roles("CAFECON")
                .build();
    }

}