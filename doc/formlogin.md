#폼 로그인

---

##DefaultLoginPageGeneratingFilter
- Get/login 처리를 한다.
- 기본 로그인 폼
- 별도 설정 없을 때 뜨는 로그인 폼

DefaultLoginPageGeneratingFilter 에 해당 내용 설정이 되어있음

##UsernamePasswordAuthenticationFilter
폼 로그인 처리
- Post/login 처리를 한다.
- processingUrl을 변경하면 주소를 바꿀 수 있다.
- form 인증 처리해주는 필터로 스프링 시큐리티에 가장 일반적

```java
@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers().disable()
                .csrf().disable()
                .formLogin(login->
                    //여기서 처리!
                )
                .logout().disable()
                .requestCache().disable()
                ;
    }
```


- filterProcessingUrl : 로그인 처리를 해줄 URL (POST)
- username parameter : POST에 username에 대한 값 넘겨줄 인자의 이름 ->메일로 바꿔줄 수 있음
- password parameter : 비밀번호
- 로그인 성공시 처리 방법
    - defaultSuccessUrl : alwaysUse 옵션 설정이 중요
    - successHandler
```java
@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers().disable()
                .csrf().disable()
                .formLogin(login->
                    login.defaultSuccessUrl("/", false);
                //alwaysUse를 true로 하면 루트페이지로 감. (?)
                )
                .logout().disable()
                .requestCache().disable()
                ;

    }
```
- 로그인 실패시 처리 방법
    - failureUrl
    - failureHandler
    
- authenticationDetailSource : Authentication 객체의 details에 들어갈 정보를 직접 만들어 줌

```java
    @Override
    public Authentication attemptAuthentication(HttpServletReqeust request, HttpServletResponse response) throws AuthenticationException{
        if(this.postOnly && !request.getMethod().equals("POST")){
            throw new AuthenticationServiceException("Authentication method not supported")
        }
        String username = obtainUsername(request);
        username = (username != null)? username: "";
        username = username.trim();
        String password = obtainPassword(request);
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        setDetails(reuqest, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
        }
```