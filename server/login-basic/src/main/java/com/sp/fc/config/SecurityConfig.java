package com.sp.fc.config;


import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.boot.autoconfigure.security.reactive.StaticResourceRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
//debug모드로 사용하겠다 ->
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    // 필터체인 설정 여기 configure에서 하겠다.
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /*
        http
                .authorizeRequests(request->{
                    request
                            .antMatchers("/**").permitAll(); //ant개미.. 필터체인 Matchers//일치자//모두승인
                            //.anyRequest().authenticated() 다른요청은 authenticated 권한필요
                    // http 자체에 antMatcher가 있다. =>  // 특정 api요청만 체크하겠다!
                    // 모든 access에 대한 접근은 permitAll이 되어있다.
                });
         */

        http
                .authorizeRequests(request->{
                    request
                            .antMatchers("/").permitAll()
                            .anyRequest().authenticated()
                    ;
                });
    }

    //css같은 거 다 막힌 상태여서 풀어줘야함. WebSecurity파라미터로 넘겨주는 configure에서 설정하면 됨됨

    @Override
    public void configure(WebSecurity web) throws Exception {
//        web.ignoring()
//                .requestMatchers(
//                        PathRequest.toStaticResources().atCommonLocations()
//                ) 이거 안 돼서 그냥 regex넣었음... 왜 않되?
        ;
    }
}
