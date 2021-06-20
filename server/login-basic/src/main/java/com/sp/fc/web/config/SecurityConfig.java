package com.sp.fc.web.config;



import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;

@Configuration
//debug모드로 사용하겠다 ->
@EnableGlobalMethodSecurity(prePostEnabled = true)
//Global Method에 Security적용할껀가. 지금 HomeController에 권한별로 설정해뒀는데 이 옵션을 켜야지 그게 적용됨
@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthDetails customAuthDetails;

    public SecurityConfig(CustomAuthDetails customAuthDetails) {
        this.customAuthDetails = customAuthDetails;
    }

    // 잘 안될때는 UsernamePassword필터 가서 직접 break point 찍어보자.
    //    UsernamePasswordAuthenticationFilter filter;
    // 어 값이 안들어오네...
    // 다음 확인 CSRF Filter들어오나?
    //    CsrfFilter
    // 여긴 들어오네! 근데 Token에 null...?
    // html ㅔ크

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(
                        User.withDefaultPasswordEncoder()
                                .username("user1")
                                .password("1")
                                .roles("USER"))
                .withUser(
                        User.withDefaultPasswordEncoder()
                                .username("user2")
                                .password("2")
                                .roles("ADMIN")
                );
    }

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
                })
                .formLogin(//로그인 페이지로 보내줌. request요청을 받음 DefaultLoginPageGeneratingFilter와 DefaultLogoutPageGeneratingFilter
                        login->login.loginPage("/login")
                                .permitAll()// 로그인 page 들어오는 거 모두 permitAll해줘야 함. 아니면 무한루프 돌게 됨.. 요청요청요청요청
                        .defaultSuccessUrl("/", false)
                        //성공하면 "/"페이지로 보냄. alwaysUse는 false.
                        //alswaysUse는 왜 false냐?
                        // 유저 페이지 접근 -> 로그인 -> 성공 -> alswaysUse사용 -> 메인 "/"
                        // false 유저 페이지 접근 -> 로그인 -> 성공 -> alwaysUse사용x -> 유저 페이지
                        // 그냥 로그인 -> 메인 "/"
                        .failureUrl("/login-error") //로그아웃 실패 시 보낼 곳. default!
                        .authenticationDetailsSource(customAuthDetails)
                )
                .logout(logout->logout.logoutSuccessUrl("/login-error"))
                //로그아웃 시 자동으로 로그인 페이지 가게하고 있는데...
                //logout했을 때 페이지도 바꿀 수 있음
                .exceptionHandling(exception->exception.accessDeniedPage("/access-denied"));
                //권한 없는 곳 페이지 접근할 때 어떻게 해주냐. exceptionHandling설정해주면 됨!
    }

    //일반적으로 관리자는 일반 유저 접근하는 곳 다 접근 가능 그걸 여기 설정해줘야함.
    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        //ROLE_ADMIN은 ROLE_USER가 할 수 있는 거 다! 할 수 있다.
        return roleHierarchy;
    }

    //css같은 거 다 막힌 상태여서 풀어줘야함. WebSecurity파라미터로 넘겨주는 configure에서 설정하면 됨됨
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations()
                        //import문제;;
                        //import org.springframework.boot.autoconfigure.security.servlet.PathRequest; 이걸 import 해와야 함
                        //import org.springframework.boot.autoconfigure.security.reactive.PathRequest; 근데 이걸 import하고 있었음!
                        //js/css/image 파일 등 보안 필터를 적용할 필요 없는 리소스 설정
                )
        ;
    }
}
