웹사이트에서 아이디와 패스워드를 통해 로그인하는 과정 : 인증(Authentication)

특정 기준을 충족해야 웹사이트의 서비스를 이용하도록 하는것 : 인가(Authorization)

보호 대상에 접근하는 유저 : 접근 주체(Principal)

인증된 주체가 어플리케이션의 동작을 수행할 수 있도록 허락되었는지를 결정할 때 사용 : 권한

# 기본 개념

## 1. Spring Security

스프링 어플리케이션에서 인증과 권한을 포함하는 보안을 담당하는 스프링의 하위 프레임워크이다.

인증과 인가를 위한 세션관리를 자체적으로 구현하지 않아도, 스프링 시큐리티를 적용하면 해결할 수 있다.

스프링 시큐리티는 본래 **세션 기반 인증**을 사용하기 때문에 JWT와 분리해서 잘 이해해야한다.

<br/>

### Spring Security Filter

스프링 시큐리티는 필터(Filter)를 기반으로 동작한다. **필터는 요청이 DispatcherServlet으로 오기 전 과정**에 존재하며, 클라이언트와 Servlet 자원 사이에서 여러 사전 처리를 수행한다.

<br/>

Spring Security에서 제공하는 필터는 10개 이상으로, 이를 묶어 Security Filter Chain이라고 한다.

![img](https://blog.kakaocdn.net/dn/bJ51wr/btqYE69wD6W/zKZPrVK8BFaeISABGEYiD1/img.png)  
출처 : https://atin.tistory.com/590

<br/>

- **SecurityContextPersistenceFilter** : SecurityContextRepository에서 SecurityContext를 가져오거나 저장

- **LogoutFilter** : 설정된 로그아웃 URL로 오는 요청을 처리하며, 유저를 로그아웃 처리한다.

- (UsernamePassword)**AuthenticationFilter** :

  form 태그 기반의 인증을 담당하며, 설정된 로그인 URL로 오는 요청을 처리하며 유저를 인증처리한다.

    - AuthenticationManager를 통해 인증 실행
    - 인증 성공시, 획득한 Authentication 객체를 SecurityContext에 저장 후, AuthenticationSuccessHandler를 실행
    - 인증 실패시, AuthenticationFailureHandler을 실행

### Spring Security 인증 관련 아키텍쳐

아이디와 비밀번호를 통해서 로그인 관련 인증을 처리하는 필터는 AutenticationFilter이다.

![img](https://blog.kakaocdn.net/dn/ceanmM/btqYIkl9GF0/3AKGUzcpXgrHg1hOFOvNz0/img.png)

- 클라이언트가 로그인을 위한 요청을 보낸다.
- AuthenticationFilter는 이 요청을 가로채서, 가로챈 정보를 통해 UsernamePasswordAuthenticationToken이라는 인증용 객체를 생성한다.
- AutenticationManager의 구현체인 ProvideManager에게 UPAToken객체를 전달한다.
- 그다음 authenticationProvider(s), UserDetailService를 통해 사용자 정보를 읽어온다.
    - UserDeatilService는 인터페이스이며, 이를 구현한 Bean을 생성하면 스프링 시큐리티가 해당 Bean을 사용한다. 사용자 정보를 읽어오기 위해 개발자가 어떤 DB를 사용할지 정할 수 있다.
    - UserDetailService가 로그인한 ID에 해당하는 정보를 DB에서 읽어들여 UserDetails를 구현한 객체를 반환한다. 반환된 UserDetails 객체에 저장된 사용자 정보는 세션에 저장된다.
- 스프링 시큐리티는 **세션 저장소인 SecurityContextHolder**에서 UserDetails정보를 저장하며, 이후 Session Id와 함께 응답을 보낸다.
- 이후 요청이 들어올 경우 쿠키에 포함된 Session ID 정보를 통해 로그인 정보가 저장되어 있는지 서버에서 확인하며, 정보가 저장되어 있고 유효하다고 판단되면 인증처리를 해주게 된다. **-> 스프링 시큐리티는 세션-쿠키 기반의 인증 방식을 사용함을 의미한다.**

출처

- 부스트코스 웹 백엔드 강의

----

## 정리

실제 구현이랑 연관지어 생각해보면 세션을 사용할 경우 

**SecurityContextHolder의 `SecurityContext` (security session)에 저장된 `Authentication`객체에 UserDetails 객체를 저장한다.** 우리는 여기 있는 UserDetails를 불러와서 현재 세션에 접속중인 로그인 정보를 확인하는 것이다.   

<br/>

**스프링 시큐리티** 

- 자기만의 시큐리티 세션을 들고 있다. (서버 세션안에 시큐리티가 관리하는 세션이 따로 있음)
- 시큐리티 섹션에는 Authentication 객체만 들어갈수있음 -> 로그인된것
  - Authentication 안에 들어갈 수 있는 타입 2개 
  - UserDetails -> 일반 로그인
  - OAuth2User -> oauth 로그인 

그럼 알아야 할 것은 

- 로그인 요청이 들어오면 어떤 일이 일어나는가
- UserDetails 객체를 어떻게 반환하는가

<br/>

1. 로그인 요청이 들어오면 어떤 일이 일어나는가

UserDetailService 인터페이스를 구현한 클래스에서 `UserDetails`객체를  반환하는 `loadUser` (OAuth) 또는 `loadUserByUsername`(Security) 함수를 오버라이딩 해서  유저 정보를 얻어오는 작업을 거친다.

- Bean 등록
  - `WebSecurityConfigurerAdapter`를 상속받고 `@EnableWebSecurity` 어노테이션을 지정하여 시큐리티 필터체인에 등록한 `SecurityConfig` 파일을 만들어 `.userService(xxxUserService)`로 등록해주면된다. 
- User 정보 얻어오기
  - 기본 Security - DB에 저장해뒀다가 불러온다. 보통 User엔티티는 username과 password 형식을 맞춘다. 
  - OAuth - Authentication Server에게서 token을 얻고 token으로 userinfo를 조회해온다. 
- UserDetails 객체 반환하기 - `service.loadUser~~()`
  - Security - UserDetails
    -  DB에서 얻어온 user 엔티티 정보를 넣어 반환
  - OAuth - OAuth2User 
    - resource server에서 얻은 user info로 값을 채워 반환 
  - 이때 Security와 OAuth를 둘 다 같이 쓴다면, `UserDetails`와 `OAuth2User`를 모두 구현한 `CustomUserDetails`와 같은 클래스를 만들어 getAttributes, getAuthority, getXXX(), 등을 구현하여 사용하면 된다.
- 이 값을 컨트롤러에서 파라미터로 `Authentication` 객체나 `@AuthenticationPrincipal UserDetails userDetails` 와 같은 방식으로 얻어올 수 있다. 
  - Authentication.getPrincipal() == userDetails



---

실제 구현 및 Security 로그인 / oauth 로그인 / jwt 로그인 연습한 코드는 

github [spring-secuity-oauth2-jwt-practice](https://github.com/kang-jisu/spring-security-oauth2-jwt-practice) 참고 