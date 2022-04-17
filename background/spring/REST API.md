> https://meetup.toast.com/posts/92
>
> TOAST MeetUp! REST API 제대로 알고 사용하기 정리



## REST API(RESTful API)

REST는 Representational State Transfer라는 용어의 약자이다. REST 아키텍처의 제약 조건을 준수하는 API를 뜻한다. 



### REST 구성

- 자원 (RESOURCE) : URI
- 행위 (VERB) : HTTP METHOD
- 표현 (Representations)



### REST의 특징

1. Uniform (균일한 인터페이스)

Uniform Interface는 URI로 지정한 리소스에 대한 조작을 통일되고 한정적인 인터페이스로 수행하는 아키텍쳐 스타일을 말한다.  

동일한 리소스에 대한 모든 API요청은 동일하게 보여야한다. 



2. Stateless (무상태성)

REST는 무상태성 성격을 갖는다. 다시말해 작업을 위한 상태정보를 따로 저장하지 않고 관리하지 않는다. 세션 정보나 쿠키 정보를 별도로 저장하고 관리하지 않기 때문에 API서버는 들어오는 요청만을 단순히 처리하면 된다. 때문에 서비스의 자유도가 높아지고 서버에서 불필요한 정보를 관리하지 않음으로써 구현이 단순해진다.  

요청에서 이의 처리에 필요한 모든 정보를 포함해야 한다. 



3. Cacheable (캐시가능)

REST의 가장 큰 특징 중 하나는 HTTP라는 기존 웹 표준을 그대로 사용하기 때문에, 웹에서 사용하는 기존 인프라를 그대로 활용이 가능하다. 따라서 HTTP가 가진 캐싱 기능이 적용 가능하다. HTTP 프로토콜 표준에서 사용하는 Last-Modified태그나 E-Tag를 이용하면 캐싱 구현이 가능하다.



4. Self-descriptiveness (자체 표현 구조)

REST의 또 다른 큰 특징 중 하나는 REST API 메시지만 보고도 이를 쉽게 이해할 수 있는 자체 표현구조로 되어있다는 것이다.



5. Client-Server 구조가 Decoupling, 완전히 독립적이어야함 

REST 서버는 API 제공, 클라이언트는 사용자 인증이나 컨텍스트(세션, 로그인 정보) 등을 직접 관리하는 구조로 각각의 역할이 확실히 구분되기 때문에 클라이언트와 서버에서 개발해야 할 내용이 명확해지고 서로간 의존성이 줄어들게 된다. 

서로 알아야하는 유일한 정보는 요청된 리소스의 URI이고 다른 방법으로 상호작용할 수 없다. 



6. 계층형 구조

REST 서버는 다중 계층으로 구성될 수 있으며 보안, 로드밸런싱, 암호화 계층을 추가해 구조상의 유연성을 둘 수 있고 PROXY, 게이트웨이 같은 네트워크 기반의 중간매체를 사용할 수 있게 한다.



### REST API 디자인 가이드

```
1. URI는 정보의 자원을 표현해야 한다.
2. 자원에 대한 행위는 HTTP Method(GET, POST, PUT, DELETE)로 표현한다.
```

POST - POST를 통해 해당 URI를 요청하면 리소스를 생성한다.

GET - GET을 통해 해당 리소스를 조회한다. 

PUT - PUT을 통해 해당 리소스를 수정한다

DELETE - DELETE를 통해 리소스를 삭제한다. 



- 마지막 문자로 (/)를 포함하지 않는다.
- (-)를 가독성을 높이는데 사용한다
- (_)은 사용하지 않는다
- URI경로에는 소문자를 사용한다
- 파일 확장자는 포함시키지 않는다.



**리소스간의 연관 관계**

- GET /users/{userid}/devices : has 관계
- GET /users/{userid}/likes/devices : 관계명이 애매하거나 구체적 표현이 필요할 때

**자원을 표현하는 Collection과 Document(Element)**

- Document : 단순히 문서로 이해해도 되고, 한 객체를 의미 , 단수로 사용

- Collection은 문서들의 집합, 객체들의 집합, **복수로 사용**



### HTTP 응답 상태 코드

- 200 : 요청 정상 수행
- 201 : 리소스 생성 요청시 성공적으로 생성됨 (POST)

- 400 : 클라이언트의 요청이 부적절
- 401 : 인증되지 않은 상태에서 보호된 리소스 요청, 로그인하지 않은 유저가 로그인했을 때 요청가능한 리소스 요청
- 403 : 유저 인증 상태와 관계없이 응답하고 싶지 않은 리소스를 요청했을 때
  - 403보다는 400,404를 권고함. 403 자체가 리소스가 존재한다는 뜻이므로
- 405 : 클라이언트가 요청한 리소스에서 사용 불가능한 Method 요청했을 경우
- 301 : 클라이언트가 요청한 리소스에 대한 URI가 변경되었을 때 , Location header에 변경된 URI 명시 필요

- 500: 서버에 문제가 있음 