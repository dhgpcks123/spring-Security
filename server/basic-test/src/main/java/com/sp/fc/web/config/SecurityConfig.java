package com.sp.fc.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
@Order(1)
    만약 두 개 이상의 필터체인을 구성하고 싶다면?
    그냥 SecurityFilterChain을 하나 더 만들면 된다.
    다만 필터 순서가 중요하기 때문에 @Order순서를 정해줘야한다.
*/
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(User.builder()
                        .username("user2")
                        .password(passwordEncoder().encode("2"))
                        .roles("USER"))
                .withUser(User.builder()
                        .username("user3")
                        .password(passwordEncoder().encode("3"))
                        .roles("ADMIN")
                );
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 어떤 요청에 대해서 필터체인이 동작할 수 있게 설정하는 것!
        http.antMatcher("/api/**");
        // http 자체에 antMMatcher가 있다.
        // 특정 api요청만 체크하겠다!

        //필터 셋팅은 여기서!
        http.authorizeRequests((requests)->
                requests.antMatchers("/").permitAll()
                        .anyRequest().authenticated()
        );
        http.formLogin();
        http.httpBasic();
        /*
        http
                .headers().disable()
                .csrf().disable()
                .logout().disable()
                .requestCache().disable()
                ;
                이 필터들 이제 작동 안 함-
         */
    }
}
