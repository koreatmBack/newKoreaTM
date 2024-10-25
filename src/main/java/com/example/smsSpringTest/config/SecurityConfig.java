package com.example.smsSpringTest.config;

import com.example.smsSpringTest.filter.CustomFilter;
import com.example.smsSpringTest.filter.JwtFilter;
import com.example.smsSpringTest.security.JwtAccessDeniedHandler;
import com.example.smsSpringTest.security.JwtAuthenticationEntryPoint;
import com.example.smsSpringTest.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * author : 신기훈
 * date : 2024-09-20
 * comment : security 설정
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
//@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDenieHandler;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.addAllowedOrigin("*");
//        configuration.addAllowedMethod("GET");
//        configuration.addAllowedMethod("POST");
//        configuration.addAllowedMethod("PUT");
//        configuration.addAllowedMethod("DELETE");
//        configuration.addAllowedHeader("*");
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
//                .cors().configurationSource(corsConfigurationSource())
//                .and()
                .csrf().disable() // token을 사용하는 방식이므로 csrf 보안 사용 안함
                .httpBasic().disable()
                .formLogin().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDenieHandler)
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용 안함
                .and().cors(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new CustomFilter(), JwtFilter.class);
//                .authorizeRequests()
//                .antMatchers("/v1/formMail_admin/join","/api/v1/formMail_admin/join","/v1/formMail_admin/login","/api/v1/formMail_admin/login"
//                ,"/v1/formMail_common/login").permitAll()
//                .anyRequest().authenticated();
        return http.build();
    }


//    // 가장 기본적인 필터체인, 추후 수정 필요.
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
//        return httpSecurity
//                .httpBasic().disable()
//                .csrf().disable()
//                .cors().and()
//                .authorizeRequests()
//                .anyRequest().permitAll()
////                .requestMatchers("/api/users/login", "/api/users/join").permitAll()
//                .and()
//                .build();
//    }
}
