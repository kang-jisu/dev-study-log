## 프록시



- v1 : 인터페이스와 구현 클래스 - 스프링 빈으로 수동 등록
- v2 - 인터페이스 없는 구체 클레스 - 스프링 빈으로 수동 등록
- v3 - 컴포넌트 스캔으로 스프링 빈 자동 등록

v1~v3 다양한 경우에 프록시를 어떻게 적용하는지 알아보자.





### 예제 프로젝트 만들기 v1

> 인터페이스와 구현 클래스 - 스프링 빈으로 수동 등록

빈 등록

- Config파일 만들어서 `@Configuration` 어노테이션 붙이고 `@Bean` 으로 등록

- basecPackage가 `hello.proxy.app` 으로 되어있어서, config랑 trace 패키지는 스캔 대상이 아님. 그래서 Application파일에  AppV1Config를 스프링 빈으로 등록해야해서 이렇게 해주었음

  - ```java
    @Import(AppV1Config.class)
    @SpringBootApplication(scanBasePackages = "hello.proxy.app") //주의
    public class ProxyApplication {
    
    	public static void main(String[] args) {
    		SpringApplication.run(ProxyApplication.class, args);
    	}
    
    }
    
    ```



### 예제 프로젝트 만들기 v2

> 인터페이스 없는 구체클래스를 스프링 빈으로 수동 등록



### 예제 프로젝트 만들기 v3

> 컴포넌트 스캔으로 스프링 빈 자동 등록

`@Repository`, `@Service`, `@RestController` 사용



### 요구사항 추가

- 기존 요구사항
  - 기존 코드를 많이 수정해야한다. 코드 수정을 최소화 하기 위한 패턴을 사용했지만 결과적으로 코드를 모두 고쳐야했다. (로그를 남길 때 원본 코드를 변경해야한다는 사실 그 자체가 개발자에게는 가장 큰 문제)
- 요구사항 추가
  - **원본 코드를 전혀 수정하지 않고 로그 추적기를 적용해라.**
  - 특정 메서드는 로그를 출력하지 않는 기능
    - 보안상 일부는 로그를 출력하지 않음
  - 다음과 같은 다양한 케이스에 적용할 수 있어야 한다.

-> 프록시의 개념을 이해해야 한다.



### 프록시, 프록시 패턴, 데코레이터 패턴 - 소개

**클라이언트와 서버**

- 클라이언트 : 필요한 것을 요청 : 요청하는 객체
- 서버 : 서버는 클라이언트의 요청을 처리 : 요청을 처리하는 객체



**직접 호출과 간접 호출**

- 직접 호출
  - 클라이언트 -> 서버
- 간접 호출
  - 클라이언트 -> 프록시 -> 서버
  - 대리자 : 프록시

**특징**

- **접근 제어 , 캐싱**
  - 기대한 것 보다 빨리 처리가 가능하다.
- **부가 기능 추가**
- 프록시 체인
  - 대리자를 통해 요청했기 때문에 그 이후에 뭔가 일어난 과정은 모른다.
- 대체 가능 (DI)
  - 클라이언트는 서버에게 요청한것인지, 프록시에게 요청한 것인지 몰라야한다.
  - 프록시와 서버는 같은 인터페이스를 사용해야 하고 클라이언트가 사용하는 서버 객체를 프록시 객체로 변경해도 클라이언트 코드를 변경하지 않고 동작할 수 있어야 한다.



**런타임 객체 의존관계**

- 런타임(애플리케이션 실행 시점)에 클라이언트 객체에 DI를 사용해서 `client->server` 관계에서 `client -> Proxy` 로 객체 의존관계를 변경해도 클라이언트 코드를 전혀 변경하지 않아도 된다.
- DI를 사용해 클라이언트 코드 변경 없이 유연하게 프록시를 주입할 수 있다.



**프록시의 주요 기능**

- 접근 제어
  - 권한에 따른 접근 차단
  - 캐싱
  - 지연 로딩
    - 일단은 프록시로만 사용하다가 실제 요청이 있을 때 실체 객체를 사용
- 부가 기능 추가
  - 원래 서버가 제공하는 기능에 더해서 부가 기능을 수행한다.
  - 예 ) 요청 값이나, 응답 값을 중간에 변형한다.
  - 예 ) 실행 시간을 측정해서 추가 로그를 남긴다.



**GOF 디자인 패턴**

GOF 디자인 패턴 에서는 의도에 따라서 주요 기능 두 가지를 이렇게 나눈다.

- 프록시 패턴 : 접근 제어가 목적 
- 데코레이터 패턴 : 새로운 기능 추가가 목적 



### 프록시 패턴 - 예제 코드 1



**프록시 패턴 적용 전 - 클래스 의존 관계**

Client -> Subject (interface) `{operation()}`

​                 ㄴ RealSubject(class) `{operation()}`

**프록시 패턴 적용 전 - 런타임 객체 의존 관계**

Client -> operation() -> realSubject



RealSubject :  *//* *외부 호출로 호출할 때 마다* 1초가 걸리는 시스템에 큰 부하를 주는 데이터 조회 서비스

```java
    @Test
    void noProxyTest() {
        RealSubject realSubject = new RealSubject();
        ProxyPatternClient proxyPatternClient = new ProxyPatternClient(realSubject);
        proxyPatternClient.execute();
        proxyPatternClient.execute();
        proxyPatternClient.execute();
    }
```

```bash
23:50:36.324 [main] INFO hello.proxy.pureproxy.proxy.code.RealSubject - 실제 객체 호출
23:50:37.331 [main] INFO hello.proxy.pureproxy.proxy.code.RealSubject - 실제 객체 호출
23:50:38.335 [main] INFO hello.proxy.pureproxy.proxy.code.RealSubject - 실제 객체 호출
```

1초씩 걸림 

- 데이터가 한번 조회하면 변하지 않는 데이터라면, 캐시 하는게 더 좋을 것 



### 프록시 패턴 - 예제 코드 2

```java
@Slf4j
public class CacheProxy implements Subject {
    private Subject target;
    private String cacheValue;

    public CacheProxy(Subject target) {
        this.target = target;
    }

    @Override
    public String operation() {
        log.info("프록시 호출");
        if (cacheValue == null) {
            cacheValue = target.operation();
        }
        return cacheValue;
    }
}
```

```java
    @Test
    void cacheProxyTest() {
        RealSubject realSubject = new RealSubject();
        CacheProxy cacheProxy = new CacheProxy(realSubject);
        ProxyPatternClient proxyPatternClient = new ProxyPatternClient(cacheProxy);
        proxyPatternClient.execute();
        proxyPatternClient.execute();
        proxyPatternClient.execute();
    }
```

```bash
23:55:51.682 [main] INFO hello.proxy.pureproxy.proxy.code.CacheProxy - 프록시 호출
23:55:51.684 [main] INFO hello.proxy.pureproxy.proxy.code.RealSubject - 실제 객체 호출
23:55:52.687 [main] INFO hello.proxy.pureproxy.proxy.code.CacheProxy - 프록시 호출
23:55:52.687 [main] INFO hello.proxy.pureproxy.proxy.code.CacheProxy - 프록시 호출
```

1초 정도로 해결됨



클라이언트 코드 변경 없이 자유롭게 프록시를 넣고 뺄 수 있었다.



### 데코레이터 패턴 - 예제 코드 1,2

- 프록시 적용해서 부가기능 추가
  - 응답 값을 꾸며주는 데코레이터 프록시를 만든다.



```java
@Slf4j
public class MessageDecorator implements Component {
    private final Component component;

    public MessageDecorator(Component component) {
        this.component = component;
    }

    @Override
    public String operation() {
        log.info("MessageDecorator 실행");
        String result = component.operation();
        String decoResult = "****" + result + "****";
        log.info("MessageDecorator 꾸미기 적용 전 = {}, 적용 후 = {}", result, decoResult);
        return decoResult;
    }
}
```

클라이언트 코드를 전혀 수정하지 않았음!



### 데코레이터 패턴 - 예제 코드 3

프록시는 체인이 될 수 있음. 

