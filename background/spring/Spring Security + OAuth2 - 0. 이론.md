# OAuth란

OAuth(Open Authorization)는 토큰 기반의 인증 및 권한을 위한 표준 프로토콜이다. OAuth와 같은 인증 프로토콜을 통해 유저 정보를 페이스북, 구글 등의 서비스에서 제공받을 수 있고 이 정보를 기반으로 어플리케이션 사용자에게 로그인이나 다른 여러 기능을 손쉽게 제공할 수 있다.

OAuth2는 보안 수준이 어느정도 검증된 플랫폼의 API를 이용하여 사용자 인증과 리소스에 대한 권한 획득(인가)를 할 수 있도록 해주는 역할을 한다. 신뢰할 수 있는 플랫폼이 인증과 리소스에 대한 권한을 외부 플랫폼에 부여함으로써 보안적인 측면에서 안전해지고, 사용자는 불안전한 서비스에 자신의 id, pw를 입력하며 회원가입을 한번 더 하지 않아도 되기 때문에 사용자 경험측면에서도 장점이 있다. 

## OAuth2 구성 요소

- Resource Owner : 사용자  
  - 웹서비스 이용자
- Client : 리소스 서버에서 제공해주는 자원을 사용하는 외부 플랫폼 
  -  내가만든 프로젝트, 서버
- Authorization Server : 외부 플랫폼이 리소스 서버의 사용자 자원을 사용하기 위한 인증 서버
  - (처음에 Access Token 발급 )
- Resource Server : 사용자의 자원을 제공해주는 서버 
  - Access Token으로 user info 요청을 날리는 대상 서버 

## OAuth2 인증 종류(Grant Type)

- **Authorization Code Grant : 권한 코드 승인 방식**, "인가받은 code값을 토대로 토큰을 요청하는 방식"
  - Resouce Owner로 부터 리소스 사용에 대한 허락을 의미하는 AuthorizationCode를 이용하여 AccessToken을 요청하는 방식. Token이 바로 클라이언트로 전달되지 않기 때문에 다른 방식보다 보안에 좋은 특징이 있다.
  - 처음에 구글로그인이 ~서비스에 사용되는데 동의하시겠습니까?로 로그인하게되면 Authorization Code가 발급되어 이걸 가지고 내 프로젝트 서버에 redirect가 간다.(AccessToken과 함께) 그럼 서버는 구글한테 다시 AccessToken을 가지고 구글한테 유저의 정보를 달라고 요청을 보내서 응답받게된다.
    - 이후에 프로젝트에서 유저의 로그인을 유지하기 위해 JWT와 등등..으로 발급하는 AccessToken과 위에 말한 AccessToken을 구분해야한다. 
- Implicit Grant : 암시적 승인 방식
  - 권한 코드 없이 바로 토큰 발급. read only 서비스에만 사용하는것이 좋다. 
- Password Credentials Crant : 비밀번호 자격 증명 방식
  - 아이디 비밀번호를 저장해두고 사용하는 방식
- Client Credentials Grant : 클라이언트 자격 증명 방식
  - Client와 Resource Owner가 같을 때 사용하는 방식 

## Client<->Authorization Server간에 주고받는 정보

- client id, client secret
  - 해당 oauth를 사용하기 위한 key, (id,pw)같은 느낌
  - 개발자 사이트에서 발급받아야하고, 보안유지를 잘 해야한다
- redirect_uri
  - client가 authorization server에게 토큰 발행을 요청할 때 다시 제어권을 돌려받을 수 있도록 redirect uri을 전달해준다. 해당 uri에 파라미터로 값이 담겨 Client에게 전해진다. 
- response_type
  - authoization code방식일땐 code
  - implicit 방식일땐 token 
  - 으로 응답이 온다. 
- scope
  - Client가 접근가능하도록 승인된 내 리소스 범위
  - e.g. name, email, birth ..
- state(optional)
  - csrf공격을 막기 위한 정보값 



## Authorization Code Grant Type 

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fbo2Pk2%2FbtqudUcxHtg%2FfJCQKYtKnTsa97iwfq1zK1%2Fimg.png)

출처 : https://blinders.tistory.com/65?category=825013

<br/>

3-2 부분은 처음 로그인하면서 사전동의를 처음 한다면 하고, 다음부터는 바로 3-1과정으로 넘어간다.

코드방식은 그때의 쿼리 스트링값이 토큰 발행 딱 한순간에 쓰이고 폐기된다. (implicit은 다회성이라서 노출되면 위험하다. 해당 방식은 스크립트언어로 동작하는 클라이언트들이 보통 사용한다. )



- 출처
  - 글쓰는 개발자, 나는 어쩌다 인증을 하게되었나 1-3편
  - https://blinders.tistory.com/64
    https://blinders.tistory.com/65?category=825013
  - https://deeplify.dev/back-end/spring/oauth2-social-login#%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EA%B5%AC%EC%A1%B0

