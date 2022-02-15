<<정리중>>
## OAuth2 연동

- CommondOAuth2Provider ENUM

  - 해당 이름의enum값이 새롭게 추가되어 구글, 깃허브, 페이스북의 기본 설정값은 모두 여기서 제공한다.
  - 다른 소셜 로그인인 네이버, 카카오를 추가한다면 직접 추가해주어야한다. 

- 승인된 리디렉션 URI

  - 서비스에서 파라미터로 인증정보를 주었을 때 인증이 성공하면 구글에서 리다이렉트할 URL
  - 스프링부트2버전 시큐리티에서는 기본적으로 `{도메인}/login/oauth2/code/{소셜서비스코드}` 로 리다이렉트 URL을 지원하고있어 사용자가 별도로 리다이렉트 URL을 지원하는 Controller를 만들 필요가 없다. 

- spring boot는 properties의 이름을 `application-xx`로 지정하면 xx라는 profile이 생성되어 이를 통해 관리할 수 있다. -> 이건 나중에 테스트해보기 

  - 기본 application.yml파일에 이렇게 추가하고 gitignore해서 숨김처리 

    ```yml
    spring:
      config:
        import: classpath:application-security.yml
        activate:
          on-profile: default
    ```

- 스프링 시큐리티에서는 권한 코드에 항상 `ROLE_`이 앞에 있어야만 한다. 

  - ```java
    @Getter
    @RequiredArgsConstructor
    public enum Role {
        
        ADMIN("ROLE_ADMIN","관리자"),
        USER("ROLE_USER","일반 사용자");
        
        private final String key;
        private final String title;
    }
    ```

- spring-boot-starter-oauth2-client

  - 소셜 로그인 등 클라이언트 입장에서 소셜 기능 구현시 필요한 의존성
  - spring-security-oauth2-client와 spring-security-oauth2-jose 기본 관리 

### 5.3 구글 로그인 연동하기 까지 결과

- [커밋 링크](https://github.com/kang-jisu/Team-auction/commit/5084588ede7dd56fbdf3de951f587febe482f47e)

#### 새로 추가한 파일

##### User.java

```java
package com.project.auction.lol.domain.user;

import com.project.auction.lol.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(Long id, String name, String email, String picture, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public User update(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}

```

##### UserRepository.java

```java
package com.project.auction.lol.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}

```

##### auth/SecurityConfig.java

```java
package com.project.auction.lol.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity // SpringSecurity설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable().headers().frameOptions().disable() // h2-console화면 사용하기 위해서
                .and()
                .authorizeRequests()
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
                .antMatchers("/api/v1/**").authenticated()
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .oauth2Login()
                .userInfoEndpoint() // 로그인 성공 이후 사용자 정보를 가져올 때의 설정 담당
                .userService(customOAuth2UserService);
    }
}

```

##### auth/CustomOAuth2UserService

```java
package com.project.auction.lol.config.auth;

import com.project.auction.lol.config.auth.dto.OAuthAttributes;
import com.project.auction.lol.config.auth.dto.SessionUser;
import com.project.auction.lol.domain.user.User;
import com.project.auction.lol.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        /**
         * registrationId : 현재 로그인 진행중인 서비스를 구분하는 코드 (네이버/구글)
         * userNameAttributeName : OAuth2 로그인 진행 시 키가 되는 필드값 (PK 의미) - 구글 기본코드는 sub, 네이버는 지원하지 않음
         * OAuthAttributes : OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담은 클래스
         * SessionUser : 세션에 사용자 정보를 저장하기 위한 Dto클래스
         */

        //	/**
        //	 * Constructs a {@code DefaultOAuth2User} using the provided parameters.
        //	 * @param authorities the authorities granted to the user
        //	 * @param attributes the attributes about the user
        //	 * @param nameAttributeKey the key used to access the user's &quot;name&quot; from
        //	 * {@link #getAttributes()}
        //	 */
        //	public DefaultOAuth2User(Collection<? extends GrantedAuthority > authorities, Map<String, Object> attributes,
        //                String nameAttributeKey) {
        //            Assert.notEmpty(authorities, "authorities cannot be empty");
        //            Assert.notEmpty(attributes, "attributes cannot be empty");
        //            Assert.hasText(nameAttributeKey, "nameAttributeKey cannot be empty");
        //            if (!attributes.containsKey(nameAttributeKey)) {
        //                throw new IllegalArgumentException("Missing attribute '" + nameAttributeKey + "' in attributes");
        //            }
        //            this.authorities = Collections.unmodifiableSet(new LinkedHashSet<>(this.sortAuthorities(authorities)));
        //            this.attributes = Collections.unmodifiableMap(new LinkedHashMap<>(attributes));
        //            this.nameAttributeKey = nameAttributeKey;
        //        }
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);
        httpSession.setAttribute("user", new SessionUser(user));
        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(), attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture())) // 사용자 정보가 변경되었을 경우 이름이나 프로필사진 변경 용도
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}

```

##### auth/dto/OAuthAttribute

```java
package com.project.auction.lol.config.auth.dto;

import com.project.auction.lol.domain.user.Role;
import com.project.auction.lol.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/**
 * OAuth2User의 정보들을 담을 클래스
 */
@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.USER)
                .build();
    }
}

```

##### auth/dto/SessionUser

```java
package com.project.auction.lol.config.auth.dto;

import com.project.auction.lol.domain.user.User;
import lombok.Getter;

/*
인증된 사용자 정보 -> User클래스를 사용하며 직렬화를 구현하는것보다 따로 이렇게 Dto를 추가하는것이 낫다.
 */
@Getter
public class SessionUser {
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}

```



로그인 성공시

![스크린샷 2022-02-15 오후 11.21.54](/Users/jskang/Desktop/스크린샷 2022-02-15 오후 11.21.54.png)

```java
Hibernate:     select        user0_.id as id1_2_,        user0_.created_date as created_2_2_,        user0_.modified_date as modified3_2_,        user0_.email as email4_2_,        user0_.name as name5_2_,        user0_.picture as picture6_2_,        user0_.role as role7_2_     from        user user0_     where        user0_.email=?2022-02-15 22:56:42.594 TRACE 79769 --- [nio-8080-exec-3] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [VARCHAR] - [rkdwltn0425@gmail.com]Hibernate:     insert     into        user        (id, created_date, modified_date, email, name, picture, role)     values        (null, ?, ?, ?, ?, ?, ?)2022-02-15 22:56:42.652 TRACE 79769 --- [nio-8080-exec-3] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [TIMESTAMP] - [2022-02-15T22:56:42.626]2022-02-15 22:56:42.653 TRACE 79769 --- [nio-8080-exec-3] o.h.type.descriptor.sql.BasicBinder      : binding parameter [2] as [TIMESTAMP] - [2022-02-15T22:56:42.626]2022-02-15 22:56:42.653 TRACE 79769 --- [nio-8080-exec-3] o.h.type.descriptor.sql.BasicBinder      : binding parameter [3] as [VARCHAR] - [rkdwltn0425@gmail.com]2022-02-15 22:56:42.653 TRACE 79769 --- [nio-8080-exec-3] o.h.type.descriptor.sql.BasicBinder      : binding parameter [4] as [VARCHAR] - [Js Kang]2022-02-15 22:56:42.653 TRACE 79769 --- [nio-8080-exec-3] o.h.type.descriptor.sql.BasicBinder      : binding parameter [5] as [VARCHAR] - [https://lh3.googleusercontent.com/a/AATXAJycO-FgXBylj1D8UL-pz6AcbbykYRjLp152cGaw=s96-c]2022-02-15 22:56:42.653 TRACE 79769 --- [nio-8080-exec-3] o.h.type.descriptor.sql.BasicBinder      : binding parameter [6] as [VARCHAR] - [USER]
```





```java
@Overridepublic OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {   Assert.notNull(userRequest, "userRequest cannot be null");   if (!StringUtils         .hasText(userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri())) {      OAuth2Error oauth2Error = new OAuth2Error(MISSING_USER_INFO_URI_ERROR_CODE,            "Missing required UserInfo Uri in UserInfoEndpoint for Client Registration: "                  + userRequest.getClientRegistration().getRegistrationId(),            null);      throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());   }   String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()         .getUserNameAttributeName();   if (!StringUtils.hasText(userNameAttributeName)) {      OAuth2Error oauth2Error = new OAuth2Error(MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE,            "Missing required \"user name\" attribute name in UserInfoEndpoint for Client Registration: "                  + userRequest.getClientRegistration().getRegistrationId(),            null);      throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());   }   RequestEntity<?> request = this.requestEntityConverter.convert(userRequest);   ResponseEntity<Map<String, Object>> response = getResponse(userRequest, request);   Map<String, Object> userAttributes = response.getBody();   Set<GrantedAuthority> authorities = new LinkedHashSet<>();   authorities.add(new OAuth2UserAuthority(userAttributes));   OAuth2AccessToken token = userRequest.getAccessToken();   for (String authority : token.getScopes()) {      authorities.add(new SimpleGrantedAuthority("SCOPE_" + authority));   }   return new DefaultOAuth2User(authorities, userAttributes, userNameAttributeName);}
```

![스크린샷 2022-02-15 오후 11.30.34](/Users/jskang/Library/Application Support/typora-user-images/스크린샷 2022-02-15 오후 11.30.34.png)



구글서비스에게 AccessToken을 받고 그 토큰을 이용해서 로그인한 유저에 대한 정보를 받아오는데 성공했다. 

일단 네이버 로그인 마저 구현하고 

이제는 그 로그인한 정보를 가지고 JWT 토큰을 생성해서 , (로그인유저, 식별 토큰?) 클라이언트에게 JWT토큰과 Refresh 토큰을 전달해주고, 서버에는 Refresh토큰을 저장한다.

클라이언트의 요청이 왔을 때마다 JWT토큰 유효성 검사를 하고, 리프레시 토큰갱신 요청이 오면 해주고, 로그아웃하면 리프레시토큰을 지워주는 방식을 구현할 예정이다. 



### 네이버 로그인 

다음은 네이버 로그인 API에서 사용하는 주요 요청 URL과 메서드, 응답 형식입니다.

|                  요청 URL                  |  메서드  | 응답 형식 |                    설명                    |
| :----------------------------------------: | :------: | :-------: | :----------------------------------------: |
| `https://nid.naver.com/oauth2.0/authorize` | GET/POST |     -     |      네이버 로그인 인증을 요청합니다.      |
|   `https://nid.naver.com/oauth2.0/token`   | GET/POST |   JSON    | 접근 토큰의 발급, 갱신, 삭제를 요청합니다. |
|   `https://openapi.naver.com/v1/nid/me`    |   GET    |   JSON    |     네이버 회원의 프로필을 조회합니다.     |

각각의 프로필 정보의 규격은 다음과 같습니다.

- 이용자 식별자 : 64자 이내로 구성된 BASE64 형식의 문자열
  - (2021년 5월 1일 이후 생성된 애플리케이션부터 적용. 기존 INT64 규격의 숫자)
- 이름 : 10자 이내로 구성된 문자열
- 닉네임 : 20자 이내로 구성된 문자열
- 프로필 이미지 : 255자 이내로 구성된 URL 형태의 문자열
- 이메일 주소 : 이메일 규격의 문자열
- 생일 : 월-일 (MM-DD) 형태의 문자열
- 연령대 : 연령 구간에 따라 0-9 / 10-19 / 20-29 / 30-39 / 40-49 / 50-59 / 60- 으로 표현된 문자열
- 성별 : M/F (남성/여성) 으로 표된 문자
- 출생연도 : 연(YYYY) 형태의 문자열
- 휴대전화번호 : 대쉬(-)를 포함한 휴대전화번호 문자열

프로필 정보 예시

- 이용자 식별자 : abcdefgABCDEFG1234567
- 이름 : 네이버
- 닉네임 : 네이버닉네임
- 프로필 이미지 : https://phinf.pstatic.net/.../image.jpg
- 이메일 주소 : naveridlogin@naver.com
- 생일 : 08-15
- 연령대 : 20-29
- 성별 : F
- 출생연도 : 1900
- 휴대전화번호 : 010-0000-0000



### 3.3.2 로그인한 회원의 네이버 로그인 사용 여부 

네이버 로그인을 이용하여 로그인 연동을 수행한 사용자는 각각의 사용자를 구별하기 위한 사용자 유니크ID를 가지고 있습니다.
이용자 식별자는 프로필 정보 조회 API를 통하여 조회할수 있는 정보입니다.

이용자 식별자 (Unique Identifier)

- 64자 이내로 구성된 BASE64 형식의 문자열
  - (2021년 5월 1일 이후 생성된 애플리케이션부터 적용. 기존 애플리케이션은 INT64 규격의 숫자로 구성)
- 네이버 아이디 별로 고유하게 부여된 값
- 애플리케이션간에는 이용자 식별자가 공유되지 않습니다.

서비스에서는 이용자 식별자를 네이버 로그인 사용자 식별값으로 이용하여 사용자 정보를 조회하거나 가입 등의 처리를 할 수 있습니다.



### 3.4.2 네이버 로그인 연동 URL 생성하기 

네이버 로그인 연동을 진행하기 위해서는 네이버 로그인 버튼을 클릭하였을 때 이동할 '네이버 로그인' URL을 먼저 생성하여야 합니다.
이 과정에서 사용자는 네이버에 로그인인증을 수행하고 네이버 로그인 연동 동의과정을 수행할 수 있습니다.
사용자가 로그인 연동에 동의하였을 경우 동의 정보를 포함하여 Callback URL로 전송됩니다.

***요청 URL 정보***

|   메서드   |                 요청 URL                 |   출력 포맷    |          설명           |
| :--------: | :--------------------------------------: | :------------: | :---------------------: |
| GET / POST | https://nid.naver.com/oauth2.0/authorize | URL 리다이렉트 | 네이버 로그인 인증 요청 |

***요청 변수 정보***

|  요청 변수명  |  타입  | 필수 여부 | 기본값 |                             설명                             |
| :-----------: | :----: | :-------: | :----: | :----------------------------------------------------------: |
| response_type | string |     Y     |  code  |    인증 과정에 대한 내부 구분값으로 'code'로 전송해야 함     |
|   client_id   | string |     Y     |   -    |          애플리케이션 등록 시 발급받은 Client ID 값          |
| redirect_uri  | string |     Y     |   -    | 애플리케이션을 등록 시 입력한 Callback URL 값으로 URL 인코딩을 적용한 값 |
|     state     | string |     Y     |   -    | 사이트 간 요청 위조(cross-site request forgery) 공격을 방지하기 위해 애플리케이션에서 생성한 상태 토큰값으로 URL 인코딩을 적용한 값을 사용 |



### .4.4 접근 토큰 발급 요청 

Callback으로 전달받은 정보를 이용하여 접근 토큰을 발급받을 수 있습니다. 접근 토큰은 사용자가 인증을 완료했다는 것을 보장할 수 있는 인증 정보입니다.
이 접근 토큰을 이용하여 프로필 API를 호출하거나 오픈API를 호출하는것이 가능합니다.

Callback으로 전달받은 'code' 값을 이용하여 '접근토큰발급API'를 호출하게 되면 API 응답으로 접근토큰에 대한 정보를 받을 수 있습니다.
'code' 값을 이용한 API호출은 최초 1번만 수행할 수 있으며 접근 토큰 발급이 완료되면 사용된 'code'는 더 이상 재사용할수 없습니다.

***요청 URL 정보***

|   메서드   |               요청 URL               | 출력 포맷 |        설명        |
| :--------: | :----------------------------------: | :-------: | :----------------: |
| GET / POST | https://nid.naver.com/oauth2.0/token |   json    | 접근토큰 발급 요청 |

***요청 변수 정보***

|   요청 변수명    |  타입  |  필수 여부   | 기본값  |                             설명                             |
| :--------------: | :----: | :----------: | :-----: | :----------------------------------------------------------: |
|    grant_type    | string |      Y       |    -    | 인증 과정에 대한 구분값 1) 발급:'authorization_code' 2) 갱신:'refresh_token' 3) 삭제: 'delete' |
|    client_id     | string |      Y       |    -    |          애플리케이션 등록 시 발급받은 Client ID 값          |
|  client_secret   | string |      Y       |    -    |        애플리케이션 등록 시 발급받은 Client secret 값        |
|       code       | string | 발급 때 필수 |    -    | 로그인 인증 요청 API 호출에 성공하고 리턴받은 인증코드값 (authorization code) |
|      state       | string | 발급 때 필수 |    -    | 사이트 간 요청 위조(cross-site request forgery) 공격을 방지하기 위해 애플리케이션에서 생성한 상태 토큰값으로 URL 인코딩을 적용한 값을 사용 |
|  refresh_token   | string | 갱신 때 필수 |    -    | 네이버 사용자 인증에 성공하고 발급받은 갱신 토큰(refresh token) |
|   access_token   | string | 삭제 때 필수 |    -    |   기 발급받은 접근 토큰으로 URL 인코딩을 적용한 값을 사용    |
| service_provider | string | 삭제 때 필수 | 'NAVER' |          인증 제공자 이름으로 'NAVER'로 세팅해 전송          |



### 3.4.5 접근 토큰을 이용하여 프로필 API 호출하기 

접근 토큰을 이용하면 프로필 정보 조회 API를 호출하거나 오픈 API를 호출하는것이 가능합니다.
사용자 로그인 정보를 획득하기 위해서는 프로필 정보 조회 API를 먼저 호출하여야 합니다.

***요청 URL 정보***

|   메서드   |   인증   |              요청 URL               | 출력 포맷 |       설명       |
| :--------: | :------: | :---------------------------------: | :-------: | :--------------: |
| GET / POST | OAuth2.0 | https://openapi.naver.com/v1/nid/me |   JSON    | 프로필 정보 조회 |

***요청 변수 정보***

요청 변수는 별도로 없으며, 요청 URL로 호출할 때 아래와 같이 요청 헤더에 접근 토큰 값을 전달하면 됩니다.

***요청 헤더***

|  요청 헤더명  |                             설명                             |
| :-----------: | :----------------------------------------------------------: |
| Authorization | 접근 토큰(access token)을 전달하는 헤더 다음과 같은 형식으로 헤더 값에 접근 토큰(access token)을 포함합니다. 토큰 타입은 "Bearer"로 값이 고정되어 있습니다. Authorization: {토큰 타입] {접근 토큰] |

***요청문 예시***

```shell
curl  -XGET "https://openapi.naver.com/v1/nid/me" \
      -H "Authorization: Bearer AAAAPIuf0L+qfDkMABQ3IJ8heq2mlw71DojBj3oc2Z6OxMQESVSrtR0dbvsiQbPbP1/cxva23n7mQShtfK4pchdk/rc="
```

***출력 결과***

|          필드          |  타입  | 필수 여부 |                             설명                             |
| :--------------------: | :----: | :-------: | :----------------------------------------------------------: |
|       resultcode       | String |     Y     |                      API 호출 결과 코드                      |
|        message         | String |     Y     |                       호출 결과 메시지                       |
|      response/id       | String |     Y     | 동일인 식별 정보 동일인 식별 정보는 네이버 아이디마다 고유하게 발급되는 값입니다. |
|   response/nickname    | String |     Y     |                         사용자 별명                          |
|     response/name      | String |     Y     |                         사용자 이름                          |
|     response/email     | String |     Y     |                       사용자 메일 주소                       |
|    response/gender     | String |     Y     |            성별 - F: 여성 - M: 남성 - U: 확인불가            |
|      response/age      | String |     Y     |                        사용자 연령대                         |
|   response/birthday    | String |     Y     |                   사용자 생일(MM-DD 형식)                    |
| response/profile_image | String |     Y     |                    사용자 프로필 사진 URL                    |
|   response/birthyear   | String |     Y     |                           출생연도                           |
|    response/mobile     | String |     Y     |                         휴대전화번호                         |

### 5.1.3 접근 토큰 만료와 갱신 주기. 프로필 정보의 갱신 

접근 토큰은 만료일자에 따라 또는 접근 토큰 갱신, 삭제 등의 동작에 따라 유효하지 않게 될 수 있습니다.
유효하지 않은 접근 토큰으로는 프로필 정보를 조회하거나 로그인 OpenAPI를 호출할 수 없습니다.
따라서 접근 토큰이 유효하지 않은 경우에는 갱신토큰을 이용하여 유효한 접근 토큰으로 재발급 받거나 네이버 로그인 인증을 다시한번 수행하는것으로 유효한 접근 토큰을 발급받을 수 있습니다.

접근 토큰의 유효성을 판단하기 위해서는 다음과 같은 방법을 이용할 수 있습니다.

- 프로필 정보 조회 API 호출 시 응답이 정상적으로 전달될 경우 접근 토큰은 유효하다고 할 수 있습니다.
- 접근 토큰 유효성 체크 API 호출을 통해 현재 접근 토큰이 유효한지 판단할 수 있습니다.

접근 토큰 유효성 체크 API는 다음과 같이 이용 가능합니다.

***요청 URL 정보***

|   메서드   |   인증   |              요청 URL               | 출력 포맷 |         설명          |
| :--------: | :------: | :---------------------------------: | :-------: | :-------------------: |
| GET / POST | OAuth2.0 | https://openapi.naver.com/v1/nid/me |   JSON    | 접근 토큰 유효성 체크 |

***요청 변수 정보***

요청 변수는 별도로 없으며, 요청 URL로 호출할 때 아래와 같이 요청 헤더에 접근 토큰 값을 전달하면 됩니다.

***요청 헤더***

|  요청 헤더명  |                             설명                             |
| :-----------: | :----------------------------------------------------------: |
| Authorization | 접근 토큰(access token)을 전달하는 헤더다음과 같은 형식으로 헤더 값에 접근 토큰(access token)을 포함합니다. 토큰 타입은 "Bearer"로 값이 고정돼 있습니다. Authorization: {토큰 타입] {접근 토큰] |

***요청문 예시***

```shell
curl  -XGET "https://openapi.naver.com/v1/nid/verify" \
    -H "Authorization: Bearer AAAAPIuf0L+qfDkMABQ3IJ8heq2mlw71DojBj3oc2Z6OxMQESVSrtR0dbvsiQbPbP1/cxva23n7mQShtfK4pchdk/rc="
```

***출력 결과***

|    필드    |  타입  | 필수 여부 |               설명               |
| :--------: | :----: | :-------: | :------------------------------: |
| resultcode | String |     Y     |        API 호출 결과 코드        |
|  message   | String |     Y     | 접근토큰 유효성 체크 결과 메시지 |

## 5.3 네이버 로그인 연동 해제 

### 5.3.1 네이버 로그인 연동 해제가 필요한 경우 

사용자가 서비스를 더이상 이용하지 않거나 (서비스 탈퇴) 네이버 로그인의 연동을 더이상 이용하지 않을 경우 (연동 해제)
네이버 로그인 연동 해제 API를 통해 연결 관계를 끊을 수 있습니다.

연동 해제 API를 통해 성공적으로 연동이 해제되면 다음과 같이 변경사항이 적용됩니다.

- 앞서 발급받은 접근토큰과 갱신토큰은 API호출 즉시 만료처리됩니다. (더이상 접근토큰 및 갱신토큰을 이용할 수 없습니다.)
- 네이버의 "내정보>보안설정>외부사이트연결관리" 의 네이버 로그인 연동 목록에서 항목이 제거됩니다.
- 연동 해제 이후 사용자가 다시 연동을 수행할 수 있으며 연동 과정에서 사용자 동의를 새로이 받게 됩니다.

연동 해제는 아래와 같이 이용 가능합니다.

***요청 URL 정보***

|   메서드   |               요청 URL               | 출력 포맷 |              설명               |
| :--------: | :----------------------------------: | :-------: | :-----------------------------: |
| GET / POST | https://nid.naver.com/oauth2.0/token |   JSON    | 접근토큰을 이용한 연결해제 요청 |

***요청 변수 정보***

|  요청 변수명  |  타입  | 필수 여부 | 기본값 |                      설명                      |
| :-----------: | :----: | :-------: | :----: | :--------------------------------------------: |
|   client_id   | string |     Y     |   -    |   애플리케이션 등록 시 발급받은 Client ID 값   |
| client_secret | string |     Y     |   -    | 애플리케이션 등록 시 발급받은 Client Secret 값 |
| access_token  | string |     Y     |   -    |               유효한 접근토큰 값               |
|  grant_type   | string |     Y     |   -    |          요청 타입. delete 으로 설정           |

***요청문 샘플***

```
https://nid.naver.com/oauth2.0/token?grant_type=delete&client_id=CLIENT_ID&client_secret=CLIENT_SECRET&access_token=ACCESS_TOKEN
```

***출력 결과***

|     필드     |  타입  | 필수 여부 |        설명         |
| :----------: | :----: | :-------: | :-----------------: |
| access_token | String |     Y     | 삭제처리된 접근토큰 |
|    result    | String |     Y     | 처리결과 (success)  |

**중요**

연동 해제 API에 사용되는 접근토큰은 반드시 유효한 접근토큰을 이용하여야 합니다.(만료된 토큰이나 존재하지 않는 토큰으로 연동해제 불가)
따라서 연동 해제를 수행하기 전에 접근토큰의 유효성을 점검하고 5.1의 접근토큰 갱신 과정에 따라 접근토큰을 갱신하는것을 권장합니다.



참고 : https://developers.naver.com/docs/login/devguide/devguide.md#%EB%84%A4%EC%9D%B4%EB%B2%84%20%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EA%B0%9C%EB%B0%9C%EA%B0%80%EC%9D%B4%EB%93%9C



```bash
Hibernate: 
    select
        user0_.id as id1_2_,
        user0_.created_date as created_2_2_,
        user0_.modified_date as modified3_2_,
        user0_.email as email4_2_,
        user0_.name as name5_2_,
        user0_.picture as picture6_2_,
        user0_.role as role7_2_ 
    from
        user user0_ 
    where
        user0_.email=?
2022-02-16 00:08:51.058 TRACE 80771 --- [nio-8080-exec-2] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [VARCHAR] - [w97ww@naver.com]
Hibernate: 
    insert 
    into
        user
        (id, created_date, modified_date, email, name, picture, role) 
    values
        (null, ?, ?, ?, ?, ?, ?)
2022-02-16 00:08:51.114 TRACE 80771 --- [nio-8080-exec-2] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [TIMESTAMP] - [2022-02-16T00:08:51.092]
2022-02-16 00:08:51.117 TRACE 80771 --- [nio-8080-exec-2] o.h.type.descriptor.sql.BasicBinder      : binding parameter [2] as [TIMESTAMP] - [2022-02-16T00:08:51.092]
2022-02-16 00:08:51.117 TRACE 80771 --- [nio-8080-exec-2] o.h.type.descriptor.sql.BasicBinder      : binding parameter [3] as [VARCHAR] - [w97ww@naver.com]
2022-02-16 00:08:51.117 TRACE 80771 --- [nio-8080-exec-2] o.h.type.descriptor.sql.BasicBinder      : binding parameter [4] as [VARCHAR] - [강지수]
2022-02-16 00:08:51.117 TRACE 80771 --- [nio-8080-exec-2] o.h.type.descriptor.sql.BasicBinder      : binding parameter [5] as [VARCHAR] - [https://phinf.pstatic.net/contact/20200107_76/1578377707498w4EPo_PNG/profileImage.png]
2022-02-16 00:08:51.117 TRACE 80771 --- [nio-8080-exec-2] o.h.type.descriptor.sql.BasicBinder      : binding parameter [6] as [VARCHAR] - [USER]

```





Security 적용으로 테스트코드 실패하는 문제 해결

- Gradle - Tasks - verification - test 에러메세지 

```bash
Caused by: org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'com.project.auction.lol.config.auth.CustomOAuth2UserService' available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {}
```

