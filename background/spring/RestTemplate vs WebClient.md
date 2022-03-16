# RestTemplate vs WebClient

스프링 어플리케이션에서 HTTP 요청을 할 때 사용하는 HttpClient 모듈이다.   

Spring 5.0이전까지는 HTTP 접근을 위해 RestTemplate을 사용하였지만, 스프링 5.0에서 WebClient가 나왔고 WebClient를 사용하기를 권고하고있다.   



## RestTemplate

- 스프링 3.0부터 지원하는 HTTP 요청 템플릿
- 멀티쓰레드 방식
- Blocking 
  - Thread Pool을 만들어 놓고 요청이 Queue에 쌓이며 1요청당 1스레드 할당 
  - 요청을 처리할때 Block된다
  - `implementation 'org.springframework.boot:spring-boot-starter-web'` 을 사용하면 자동으로 추가된다. 



## WebClient

- 스프링 5.0에서 추가된 인터페이스이다.
- 싱글 스레드 방식
- Non-Blocking
  - 각 요청은 Event Loop내 Job으로 등록된다.
  - Event Loop는 Job을 제공자에게 요청한 후 결과를 기다리지 않고 다른 Job을 처리하다 callback응답이 오면 결과를 요청자에게 제공한다.   
- `implementation 'org.springframework.boot:spring-boot-starter-webflux'` webflux의존성을 추가해주어야한다.  



- 출처
  - https://happycloud-lee.tistory.com/220



## RestTemplate Singleton으로 Bean 생성하기

RestTemplate은 스레드간 공유해도 안전한 객체이므로 @Bean으로 등록해 싱글톤으로 사용해도 된다. 

또한 여러 정의해야하는 설정이 많기 때문에 RestTemplateBuilder를 사용해서 객체를 만든다. 