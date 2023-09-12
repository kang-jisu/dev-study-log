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

- Client변경 없이 Decorator를 여러가지로 꾸밀 수 있다.



### 프록시 패턴과 데코레이터 패턴 정리

- Decorator기능에는 중복이 있고, 꾸며주는 역할을 하기 때문에 스스로 존재할 수 없다.
- 내부에 호출 대상인 component를 가지고 있어야 하고, component를 항상 호출해야한다. 이런 중복을 제거하기 위해 component를 속성으로 가지는 Decorator 추상 클래스를 만드는 방법도 고민할 수 있다.
- 이렇게 하면 추가로 클래스 다이어그램에서 어떤 것이 실제 컴포넌트인지(RealComponent), 데코레이터인지 (MessageDecorator, TimeDecorator) 명확하게 구분할 수 있다.



#### **프록시 패턴 vs 데코레이터 패턴**

**의도 (intent)**

- 디자인 패턴에서는 해당 패턴의 겉 모양이 아니라 만든 의도가 더 중요하다.

프록시 패턴의 의도

- 다른 개체에 대한 **접근을 제어**하기 위해 대리자를 제공

데코레이터 패턴의 의도

- **객체에 추가 책임(기능)을 동적으로 추가**하고 기능 확장을 위한 유연한 대안 제공



**정리**

프록시를 사용하고 해당 프록시가 접근 제어가 목적이라면, 프록시 패턴이고 새로운 기능을 추가하는 것이 목적이라면 데코레이터 패턴이 된다.



### 인터페이스 기반 프록시 - 적용

프록시를 사용하면 기존 코드를 전혀 수정하지 않고 로그 추적 기능을 도입할 수 있다.

![스크린샷 2023-09-05 오후 11.31.01](./스크린샷%202023-09-05%20오후%2011.31.01.png)



#### ![스크린샷 2023-09-05 오후 11.31.42](./스크린샷%202023-09-05%20오후%2011.31.42.png)



![스크린샷 2023-09-05 오후 11.44.13](./스크린샷%202023-09-05%20오후%2011.44.13.png)



---

### 구체클래스 기반 프록시 - 예제 1

인터페이스가 아니라 구체클래스에 대해서도 프록시 작성이 가능한지

```java
@Slf4j
public class ConcreteLogic {
    public String operation() {
        log.info("Concrete Logic");
    }
}
```

![스크린샷 2023-09-12 오후 11.32.44](./스크린샷%202023-09-12%20오후%2011.32.44.png)



### 구체클래스 기반 프록시 - 예제 2

자바의 다형성은 **인터페이스를 구현하든, 클래스를 상속하든, 상위 타입만 맞으면** 다형성이 적용된다. 따라서 인터페이스가 없어도 프록시를 만들 수 있다.



![스크린샷 2023-09-12 오후 11.35.14](./스크린샷%202023-09-12%20오후%2011.35.14.png)

```java
    @Test
    void noProxy() {
        ConcreteLogic concreteLogic = new ConcreteLogic();
        ConcreteClient concreteClient = new ConcreteClient(concreteLogic);
        concreteClient.execute();
    }

    @Test
    void addProxy() {
        ConcreteLogic concreteLogic = new ConcreteLogic();
        ConcreteLogic timeProxy = new TimeProxy(concreteLogic);
        ConcreteClient concreteClient = new ConcreteClient(timeProxy);
        concreteClient.execute();
    }
```

```bash
23:46:55.349 [main] INFO hello.proxy.pureproxy.concreteproxy.code.TimeProxy - TimeDecorator 실행
23:46:55.352 [main] INFO hello.proxy.pureproxy.concreteproxy.code.ConcreteLogic - Concrete Logic 실행
23:46:55.352 [main] INFO hello.proxy.pureproxy.concreteproxy.code.TimeProxy - TimeDecorator 종료 resultTime = 0ms
```



핵심은 concreateClient생성자에 concreteLogic이 아니라 TimeProxy를 주입하는건데, 이는 다형성에 의해서 가능하다. (상속의 다형성)



**=> 인터페이스가 없더라도 프록시가 가능하다는 것을 확실하게 알고 넘어가야한다!**

### 구체 클래스 기반 프록시 - 적용

자바에서 상속할 때 super()를 무조건 호출해야하는데 부모클래스에 기본생성자가 없기때문에 , 그리고 프록시라 repository 사용하지 않을거라서 super(null) 해줘야함

```java
    public OrderServiceConcreteProxy(OrderV2Service target, LogTrace logTrace) {
        super(null); // 첫번째 줄에는 들어가야함 . 근데 프록시라서 안들어가도됨 -> 이게 프록시 기반의 단점
        // 이게 없으면 자동으로 super()을 호출하는데 OrderV2Service는 기본생성자가 없음. 오류가 날 것
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public void orderItem(String itemId) {
        TraceSt
```



### 인터페이스 기반 프록시와 클래스 기반 프록시

**프록시**

프록시를 사용한 덕분에 원본 코드를 전혀 변경하지 않고 V1, V2 애플리케이션에 LogTrace를 적용할 수 있었다.



**인터페이스 기반 프록시 vs 클래스 기반 프록시**

- 인터페이스가 없어도 클래스 기반으로 프록시를 생성할 수 있다.
- 클래스 기반 프록시는 해당 클래스에만 적용할 수 있다. 인터페이스 기반 프록시는 인터페이스만 같으면 모든 곳에 적용할 수 있다.
- 클래스 기반 프록시는 상속을 사용하기 때문에 몇가지 제약이 있다.
  - 부모 클래스의 생성자를 호출해야 한다.
  - 클래스에 final 키워드가 붙으면 상속이 불가능하다.
  - 메서드에 final 키워드가 붙으면 해당 메서드를 오버라이딩 할 수 없다.

이렇게 보면 인터페이스 기반 프록시가 더 좋아보이는데, 인터페이스 기반 프록시는 상속이라는 제약에서 자유롭다. 

인터페이스 기반 프록시의 단점은 인터페이스가 필요하다는 그 자체이다. 



**결론**

실무에서는 인터페이스,구체 클래스 있는 경우 모두 있다. 두 상황을 모두 대응할 수 있어야 한다.



**너무 많은 프록시 클래스**

대상 클래스가 100개일 때 프록시 클래스도 100개를 만들어야하는가??

프록시를 하나만 만들어서 모든 곳에 적용할 방법은 없는지. 동적 프록시 기술이 이 문제를 해결해줄 것이다.



### 정리
