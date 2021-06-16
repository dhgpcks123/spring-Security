#스프링 시큐리티란?

---

웹사이트는 각종 서비스를 위한 리소스와 유저 개인정보를 갖고 있다.  
리소스를 보호하기 위한 두 가지 보안 정책!


## 인증  
  Authentication :  
  사이트에 접근하는 사람이 누구인지 시슽메이 알아야해.   
  로그인! username/ password를 입력하고 로그인하는 경우와  
  sns사이트를 통해 인증을 대리하는 경우가 있다.
  
 SNS fhrmdls(소셜 로그인) : 인증 위임  
  UsernamePassword 인증
  - Session관리
  - 토큰 관리(sessionless)
    


## 권한 
Authorization  
사용자가 누구인지 알았다면 사이트 관리자 혹은 시스템은 사용자가 어떤 일 할 수 있냐!  권한을 설정해준다   
권한에 따라 리소스 접근 허용해주는 거 쉽게 해주는 프레임 워크!   

바로 Spring Security Framework!

- Secured : deprecated
- PrePostAuthorize  
  --> 둘 다 애노테이션으로 체크
- AOP  
  --> 너무 복잡하면 AOP써서 더 세세하게 나눠줌


