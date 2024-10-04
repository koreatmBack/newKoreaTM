//package com.example.smsSpringTest.service;
//
//import com.example.smsSpringTest.entity.UserProfile;
//import com.example.smsSpringTest.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
//
//        return userRepository.findByUserId(userId)
//                .map(this::createUserDetails)
//                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
//    }
//
//    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
//    private UserDetails createUserDetails(UserProfile member) {
//
//        String role;
//        if(member.isAdmin()){
//            // 관리자
//            role = "ADMIN";
//        } else {
//            role = "USER";
//        }
//
//        return User.builder()
//                .username(member.getUserId())
//                .password(member.getUserPwd())
//                .roles(role)
//                .build();
//    }
//
//}