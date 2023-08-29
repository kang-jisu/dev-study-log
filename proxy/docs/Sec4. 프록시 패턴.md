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

