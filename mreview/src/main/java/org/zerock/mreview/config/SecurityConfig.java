package org.zerock.mreview.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true ,prePostEnabled = true)
public class SecurityConfig {

    //비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

//        http.authorizeHttpRequests().requestMatchers("/sample/all").permitAll()
//                .requestMatchers("sample/member").hasRole("USER") // USER외에 MANAGER, ADMIN 등이 있음. 상수로써 대문자로 작성해야 함.
                                                                    // 다만, 위 코드를 작성하는 것보다 해당 컨트롤러 페이지 위에 @을 쓰는 게 더 간편함.
//                .requestMatchers("/sample/admin").hasRole("ADMIN");


        // security에서는 csrf토큰 쓰는 게 기본값.
        // csrf토큰을 비활성화(disable)한다는 의미. 서버로 데이터를 보낼 때, csrf토큰을 같이 보내서 일치여부를 확인

//        http.csrf(AbstractHttpConfigurer::disable);

        http.formLogin((formLogin) -> formLogin.defaultSuccessUrl("/movie/list", true));

        return http.build();
    }
}
