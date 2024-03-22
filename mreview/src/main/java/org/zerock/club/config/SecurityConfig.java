//package org.zerock.club.config;
//
//import lombok.extern.log4j.Log4j2;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//@Log4j2
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
//        http.csrf((csrfConfig)->csrfConfig.disable())
//                .formLogin((formLogin) -> formLogin.defaultSuccessUrl("/", true));
//
//
//        return http.build();
//    }
//}
