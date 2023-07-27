# 스프링 핵심 원리 고급편



```
<스프링 핵심 원리 기본편>

- 객체 지향 설계와 스프링
 ㄴ SOLID
- 스프링 컨테이너와 스프링 빈
 ㄴ 스프링 컨테이너 - IoC, DI
 ㄴ 의존관계 주입
 ㄴ 빈 생성주기와 스코프
```



### 고급편에서 다룰 내용 

- 스프링 핵심 디자인 패턴
  - 템플릿 메서드 패턴
  - 전략 패턴
  - 템플릿 콜백 패턴
  - 프록시 패턴
  - 데코레이터 패턴
- 동시성 문제와 쓰레드 로컬
  - 웹 애플리케이션
  - 멀티쓰레드
  - 동시성 문제
- 스프링 AOP
  - 개념, 용어정리
  - 프록시 - JDK 동적 프록시, CGLIB
  - 동작 원리
  - 실전 예제
  - 실무 주의 사항
- 기타
  - 스프링 컨테이너의 확장 포인트 - 빈 후처리기
  - 스프링 애플리케이션을 개발하는 다양한 실무 팁 





----

### 간단한 예제 프로젝트 만들기

- 간단하게 V0 버전으로 Controller, Service, Repository 생성해서 `@RequriedArgumentConstructor`로 의존성 주입을 하고 컴포넌트 스캔의 대상이 되는 어노테이션(`@Controller`, `@Service`, `@Repository`) 를 이용해서 빈으로 만들었다. 



### 로그 추적기 - 요구사항 분석

**로그 추적기를 만드는 것** 

- 애플리케이션이 커지면서 모니터링과 운영이 중요해짐
- 자주 병목이 발생하는데, 어떤부분에서 병목이 발생하고 예외가 발생하는지를 로그를 통해 확인하는 것이 점점 중요해지고 있다.



**요구사항**

- 모든 PUBLIC 메서드의 호출과 응답 정보를 로그로 출력
- 애플리케이션의 흐름을 변경하면 안됨
  - 로그를 남긴다고 비즈니스로직의 동작에 영향을 주면 안됨
- 메서드 호출에 걸린 시간
- 정상 흐름과 예외 흐름 구분
  - 예외 발생시 예외 정보가 남아야함
- 메서드 호출의 깊이 표현
- HTTP 요청 구분
  - HTTP요청단위로 특정 ID를 남겨서 어떤 HTTP요청에서 시작된 것인지 명확하게 구분이 가능해야함
  - 트랜잭션 ID (하나의 HTTP요청이 시작해서 끝날 때 까지를 하나의 트랜잭션이라고 한다.)



### 로그 추적기 V1 - 프로토타입 개발

- `TraceId`
  - 트랜잭션 ID와 깊이를 표현하는 level을 묶은 개념
-  `TraceStatus`
  - 로그를 시작할 때의 상태 정보 - 로그를 종료할 때 사용함



**public method**

- `TraceStatus begin(String message)`
  - 로그 시작
  - 로그 메시지를 파라미터로 받아서 시작 로그 출력
  - 응답 결과로 현재 로그 상태인 TraceStatus 반환
- `void end(TraceStatus status)`
  - 로그 정상 종료
  - 파라미터로 시작 로그의 상태를 전달받아 실행 시간을 계산하고, 종료시에도 시작할 때와 동일한 로그 메시지 출력
  - 정상 흐름에서 호출
- `void exception(TraceStatus status, Exception e)`
  - 로그를 예외 상황으로 종료
  - `TraceStatus`, `Exception`  정보를 함께 전달 받아서 실행시간, 예외 정보를 포함한 결과 로그를 출력
  - 예외가 발생했을 때 호출



**private method**

- `complete(TraceStatus status, Exception e)`
  - 로그를 남김



**테스트 코드 작성**

> 이 테스트는 검증하는 코드 없이 콘솔로 확인하기 때문에 제대로된 테스트 코드는 아니다.

```bash
23:28:13.308 [main] INFO hello.advanced.trace.hellotrace.HelloTraceV1 -- [59417793] hello
23:28:13.309 [main] INFO hello.advanced.trace.hellotrace.HelloTraceV1 -- [59417793] hello time=1ms
```



```bash
23:28:13.294 [main] INFO hello.advanced.trace.hellotrace.HelloTraceV1 -- [6de8b18a] hello
23:28:13.299 [main] INFO hello.advanced.trace.hellotrace.HelloTraceV1 -- [6de8b18a] hello time=7ms ex=java.lang.IllegalStateException
```





### 로그 추적기 V1 - 적용

- 단순 begin, end 뿐 아니라 `try`, `catch` 구문 등을 추가해주어야함.
  - TraceStatus를 try catch 바깥에 선언해주어야 하고 
  - Exception을 다시 던져주어야 한다.



**Controller, Service, Repository 모두 적용**

```bash
2023-07-27T23:39:24.542+09:00  INFO 16523 --- [nio-8080-exec-1] h.a.trace.hellotrace.HelloTraceV1        : [6e724e2b] OrderController.request()
2023-07-27T23:39:24.544+09:00  INFO 16523 --- [nio-8080-exec-1] h.a.trace.hellotrace.HelloTraceV1        : [b3c45ac9] OrderService.orderItem()
2023-07-27T23:39:24.544+09:00  INFO 16523 --- [nio-8080-exec-1] h.a.trace.hellotrace.HelloTraceV1        : [30b15af7] OrderRepository.save()
2023-07-27T23:39:25.549+09:00  INFO 16523 --- [nio-8080-exec-1] h.a.trace.hellotrace.HelloTraceV1        : [30b15af7] OrderRepository.save() time=1005ms
2023-07-27T23:39:25.549+09:00  INFO 16523 --- [nio-8080-exec-1] h.a.trace.hellotrace.HelloTraceV1        : [b3c45ac9] OrderService.orderItem() time=1005ms
2023-07-27T23:39:25.549+09:00  INFO 16523 --- [nio-8080-exec-1] h.a.trace.hellotrace.HelloTraceV1        : [6e724e2b] OrderController.request() time=1007ms
```

```bash
2023-07-27T23:39:55.227+09:00  INFO 16523 --- [nio-8080-exec-2] h.a.trace.hellotrace.HelloTraceV1        : [da9b3627] OrderController.request()
2023-07-27T23:39:55.227+09:00  INFO 16523 --- [nio-8080-exec-2] h.a.trace.hellotrace.HelloTraceV1        : [1390f9bc] OrderService.orderItem()
2023-07-27T23:39:55.227+09:00  INFO 16523 --- [nio-8080-exec-2] h.a.trace.hellotrace.HelloTraceV1        : [02f76253] OrderRepository.save()
2023-07-27T23:39:55.227+09:00  INFO 16523 --- [nio-8080-exec-2] h.a.trace.hellotrace.HelloTraceV1        : [02f76253] OrderRepository.save() time=0ms ex=java.lang.IllegalStateException: 예외 발생!
2023-07-27T23:39:55.227+09:00  INFO 16523 --- [nio-8080-exec-2] h.a.trace.hellotrace.HelloTraceV1        : [1390f9bc] OrderService.orderItem() time=0ms ex=java.lang.IllegalStateException: 예외 발생!
2023-07-27T23:39:55.227+09:00  INFO 16523 --- [nio-8080-exec-2] h.a.trace.hellotrace.HelloTraceV1        : [da9b3627] OrderController.request() time=0ms ex=java.lang.IllegalStateException: 예외 발생!
```



**미 구현 요구사항**

- 메서드 호출 깊이 표현
- 같은 HTTP 요청이면 같은 트랜잭션 ID를 남김
- **로그에 대한 문맥(Context)정보가 필요함**

-> 파라미터로 동기화 해 볼 예정



----

### 로그 추적기 V2 - 파라미터로 동기화 개발

