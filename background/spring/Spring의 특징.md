키워드 : IOC, DI, POJO, AOP, PSA 에 대해서 공부한 내용을 정리 
아직 어려워서 계속 채워볼예정 

> 출처가 링크로 명시되어있지 않은 내용의 출처는 김영한님의 인프런 Spring 기초 강의이며 개인적인 생각은 *이탤릭체로 표시*

---



## 프레임워크와 라이브러리

- 프레임워크
  - 소프트웨어의 특정 문제를 해결하기 위해 상호 협력하는 클래스와 인터페이스 집합 
  - 뼈대나 기반 구조, IoC개념이 적용된 기술
  - 전체적인 흐름을 프레임워크가 가지고 있으며 그 안에서 개발자가 필요한 코드를  짜 넣는다. 
- 라이브러리
  - 단순히 활용 가능한 도구들의 집합
  - 사용자가 전체적인 흐름을 만들어 라이브러리를 가져다 씀 



### zip vs jar vs was

```
zip : 압축파일
jar : 클래스 압축 파일, jre환경에서 바로 실행 가능
war : 사전 정의된 구조를 사용하며 (WEB-INF/web.xml) 실행을 위해 톰캣같은 웹서버나 웹 컨테이너가 필요함
```



### Java SE vs EE , EJB

**Java SE (Standard Edition)**

- 순수 자바
- JVM, JAVA API제공 

**Java EE(Enterprise edition) Spec**

- servlet, jsp, EJB(enterprise Java Beans)
  - Web, Service용 언어
- 의존성있는 프로그램이다.
  - servlet , jsp : web container에 의존성이 있음, web container에서 실행시켜야함 (ex:Tomcat)
  - EJB : EJB server에 의존성이 있음 

**EJB(Enterprise Java Beans)**

- 기업환경의 시스템을 구현하기 위한 서버측 컴포넌트
- 종류
  - 세션 빈 (DB연동이 필요 없음)
  - 엔티티 빈
    - 데이터베이스 관리
  - 메시지 구동 빈

- 출처
  - https://ko.wikipedia.org/wiki/%EC%97%94%ED%84%B0%ED%94%84%EB%9D%BC%EC%9D%B4%EC%A6%88_%EC%9E%90%EB%B0%94%EB%B9%88%EC%A6%88

### **Servlet**

- CGI의 일종
- Java를 사용하여 동적으로 웹페이지를 생성하는 서버측 프로그램 
- Java코드 안에 HTML이 들어감 
  - `doGet` 메서드 안에 out.print(`<HTML></HTML>`) 
- 구성
  - WEB/INF/classes/package/servlet.class
    - WEB/lib/외부 jar파일
    - WEB/web.xml 
- 장점
  - Java SE와 호환된다 
  - Thread지원
- 단점
  - 느린속도
  - 웹 디자이너와 개발자간의 업무 구분이 어려움
  - java클래스 내부에 html, css, js코드가 다 들어감 
  - 극복하고자 JSP가 나옴
    - JSP
      - HTML코드 안에 Java가 들어감 
      - `<%= request.getParamter %>`등 
      - mvc1구조 : (servlet(view+controller) + model)
      - mvc2구조 :servlet(controller ) + view(jsp page) + model(pojo)

### 서블릿 컨테이너(톰캣)

- 서블릿을 관리해주는 프로그램
- 요청을 받아 응답할 수 있게 웹서버와 소켓을 생성해주고 서블릿을 연동 등 서블릿의 생명주기를 관리해줌



- 출처
  - [[Java\] Java EE , Servlet 개념과 정리글](https://ekfqkqhd.tistory.com/entry/Java-Java-EE-Servlet-개념과-정리글)

## Spring framework

스프링은 Spring framework라고 하는것이 정확한 표현이다. 



- 자바 엔터프라이즈 애플리케이션 개발에 사용되는 애플리케이션 프레임워크 
- 오픈소스로된 경량 프레임워크
  - Java EE, EJB와 비교해서 가벼움
  - POJO 객체 사용
- 특징
  - 크기나 부하 측면에서 경량 프레임워크임
  - 제어의 역행(IoC)기술을 통해 애플리케이션의 느슨한 결합 도모
    - 제어권이 사용자가 아니라 프레임워크에 있고 필요에 따라 스프링에서 사용자의 코드를 호출함 
  - 관점지향(AOP) 프로그래밍을 위한 지원
  - 애플리케이션 객체의 생명주기와 설정을 포함하고 관리하는 컨테이너
  - DI
    - 각각의 계층이나 서비스들 간에 의존성이 존재할 경우 프레임워크가 서로 연결시켜줌 
  - POJO 방식
    - J2EE프레임워크에 비해 특정 인터페이스를 구현하거나 상속받을 필요 없는 객체를 사용



엔터프라이즈 시스템은 기술적인 제약조건과 요구사항이 늘어간다. 많은 사용자의 요청을 동시에 처리해야하고 서버의 자원을 효율적으로 사용해야 하고, 기업의 핵심 정보를 처리하기 때문에 보호, 안정성, 확장성 측면에서 뛰어나야한다. 그리고 데이터베이스 연동, 타 시스템과의 연동 등 복잡함이 추가될 수 밖에 없다.

엔터프라이즈 어플리케이션이 구현해야할 핵심 비즈니스 로직의 복잡함이 증가하는데 Spring은 이때 비즈니스 로직과 기술 제약을 분리시켜줌. 그러기 위해서 IoC/DI, AOP, PSA가 사용됨



## 스프링 핵심 삼각형 IoC/DI , AOP, PSA, POJO

![img](https://media.vlpt.us/images/j_user0719/post/7178c2ca-71e5-44d9-8db6-7bdbf8b3a77f/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202021-11-07%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%204.41.07.png)

스프리링의 핵심 삼각형, 기업용 애플리케이션 개발시 복잡함을 어떻게 해결하는지에 대한 Spring의 핵심

**POJO ( Plain Old Java Object )**

```
- Java EE등의 중량 프레임워크를 사용하면서 해당 프레임워크에 종속된 자바 객체를 만들게 된것에 반발해서 사용하게 된 용어 
- 자바 모델이나 기능, 프레임워크 등을 따르지 않는 자바 오브젝트를 지칭하는 말로 쓰이며 스프링 프레임워크는 POJO 방식의 프레임워크이다.
- 특정 클래스나 인터페이스를 구현하고 확장해야할 필요없고, 특정 애너테이션을 포함해야될 필요가 없음 

- 출처 https://ko.wikipedia.org/wiki/Plain_Old_Java_Object
```

Plain : 특정 환경, 프레임워크에 종속적이지 않다는 의미

Old Java Object : 는 객체지향 원리에 충실한 클래스형태의 자바 객체

- 특정 인터페이스를 구현하거나, 클래스를 상속하지 않는 일반 자바 객체
- Java Beans
  - 기본 생성자를 가짐
  - private 멤버 변수를 가짐 
    - getter, setter를 사용해 메서드에 접근
    - 필요한 이벤트 처리 메서드 포함 
  - 직렬화 가능해야함 
  - 패키징 되어야함 



## PSA (Portable Service Abstraction)

```
환경의 변화와 세부기술의 관계 없이 일관된 방식으로 기술에 접근할 수 있게 해주는 설계 원칙

복잡한 어떤 기술을 내부에 숨기고 개발자에게 편의성을 제공 

Portable(어느 환경이든) Service(복잡한 기술을) Abstraction(추상화를 사용해) 사용할 수 있다.
```

- DB에 접근하는 방법은 여러가지가 존재 
  - Jdbc
  - ORM ( ex JPA) 
- 그런데 어떠한 경우라도 @Transactional 어노테이션을 선언하는 것 만으로도 별도의 코드나 추가 없이 트랜잭션 서비스를 사용할 수 있음. 

@Transactional을 사용하면 별도의 트랜잭션 관련 코드 없이 트랜잭션을 이용할 수 있다. 트랜잭션에 대한 공통 코드를 어노테이션이 AOP를 통해 대신 처리해주어 우리가 구현해야할 비즈니스 로직에 집중할 수 있게 해줌. 

@Transactional 어노테이션은 JDBC특화된 DatasouceTransactinalManager, JPA특화된 EntityManager가 사용하는 JPATransactionalManager를 각각 구현하는게 아니라 PlatformTransactionalManager를 만들고 각각이 이걸 구현하도록 만들어서 DI로 주입받아 사용함. 

- 출처
  - https://ooz.co.kr/170
  - [velog Spring핵심구조 POJOPSA](https://velog.io/@j_user0719/Spring-%ED%95%B5%EC%8B%AC-%EA%B5%AC%EC%A1%B0-POJOPSA)
  - [@Transactional의 동작방식(AOP,PSA)](https://cantcoding.tistory.com/88)



## IoC (Inversion of Control) 제어의 역전

메소드나 객체의 호출 작업을 개발자가 결정하는 것이 아니라 외부에서 결정되는 것을 의미한다.   

대부분의 프레임워크에서 사용하는 방식으로, 최종 호출은 개발자에 의해서가 아니라 프레임워크 내부에서 결정된 대로 이루어지게 됨.  

> - 스프링이 모든 의존성 객체를 스프링이 실행될 때 마다 만들어주고 필요한 곳에 주입시켜줌
>
> - Bean들은 싱글턴 패턴의 특징을 가짐
> - 프로그램의 흐름을 프레임워크가 주도하는 것. 객체의 생명주기 관리를 컨테이너가 맡아서 하므로 제어권이 컨테이너로 넘어간 것이고, 제어의 흐름이 바뀌었다고 하여 IoC라고 한다. 
>   - [출처](https://mo-world.tistory.com/entry/IOC%EC%99%80-DI-%EC%97%90-%EB%8C%80%ED%95%98%EC%97%AC-%EC%8A%A4%ED%94%84%EB%A7%81-%EA%B0%9C%EB%85%90-%EC%9D%B4%ED%95%B4%ED%95%98%EA%B8%B0-%EC%89%BD%EA%B2%8C-%EC%84%A4%EB%AA%85)



> IOC에 대해 이해될때까지 이곳저곳에서 설명한 내용 읽어보며 정리할예정  밑에쓴블로그에서 걍 따라침 
>
> 프로그램은 다음 과정을 반복한다. 
>
> - 객체 결정 및 생성
> - 의존성 객체 생성
> - 객체 내의 메소드 호출 
>
> 이는 모든 작업을 사용자가 제어하는 구조이다. 
>
> IoC 에서는 이 흐름의 구조가 바뀐다. IoC에서는 객체는 자기가 사용할 객체를 선택하거나 생성하지 않는다. 자신이 어디서 만들어지고 어떻게 사용되는지도 모르며, 자신의 모든 권한을 다른 대상에 위임함으로써 제어권한을 위임받은 특별한 객체에 의해 결정되고 만들어진다.   
>
> 즉, 제어의 흐름을 사용자가 컨트롤하지 않고 위임한 특별한 객체에게 모든 것을 맡기는 것이다.   
>
> IoC란 기존 사용자가 모든 작업을 제어하던 것을 특별한 객체에 위임하여 객체의 생성부터 생명주기 등 모든 객체에 대한 제어권을 넘긴것.   
>
> **IOC의 구성요소 DI, DL**
>
> DL(Dependency Lookup) - 의존성 검색
>
> 컨테이너에서는 객체들을 관리하기 위해 별도의 저장소에 빈을 저장하는데 저장소에 저장되어있는 객체를 개발자들이 컨테이너에서 제공하는 API를 이용하여 사용하고자 하는 빈을 검새하는 방법.
>
> DI (Dependency Injection) - 의존성 주입
>
> 의존성 주입이란 객체가 서로 의존하는 관계가 되게 의존성을 주입하는 것. IoC에서 DI는 각 클래스 사이에 필요로하는 의존관계를 빈 설정 정보를 바탕으로 컨테이너가 자동으로 연결해줌 
>
> 
>
> ### AOP
>
> Aspect Oriented Programming 관점 지향 프로그래밍
>
> OOP : 객체지향원칙에 따라 관심사가 같은 것은 데이터를 한곳에 모아 분리하여 결합도를 낮추고 응집도를 높임. 캡슐화,,.. 이러한 과정에서 중복된 코드들이 많아지고 가독성, 유지보수성, 확장성을 떨어트리는데 이것을 보완하기 위해 나옴
>핵심 기능과 공통 기능을 분리시켜 핵심 로직에 영향을 끼치지 않게 공통 기능을 끼워넣는 개발 형태. 중복되는 코드 제거. 
> 
>
> 
>- 출처
>   - https://khj93.tistory.com/entry/Spring-Spring-Framework%EB%9E%80-%EA%B8%B0%EB%B3%B8-%EA%B0%9C%EB%85%90-%ED%95%B5%EC%8B%AC-%EC%A0%95%EB%A6%AC





### DI 의존관계 주입

애플리케이션 실행 시점에 외부에서 실제 구현 객체를 생성하고 클라이언트에 전달해서 클라이언트와 서버의 실제 의존관계가 연결되는 것 

- 의존관계 주입을 사용하면
  -  클라이언트 코드를 변경하지 않고 클라이언트가 호출하는 대상의 타입 인스턴스 변경 가능
  -  **정적인 클래스의 의존관계를 변경하지 않고 동적인 객체 인스턴스를 쉽게 변경할 수 있다**

의존적인 객체를 직접 생성하거나 제어하는 것이 아니라, 특정 객체에 필요한 객체를 외부에서 결정해서 연결시키는 것이다. 

### IoC 컨테이너, DI 컨테이너

- AppConfig처럼 객체를 생성하고 의존관계를 관리해주는 것을 말함

 

---

### Spring MVC의 장점과 단점 그리고 SpringBoot

- 장점
  - 의존성 주입을 통해 컴포넌트 간의 결합도를 낮출 수 있어 단위테스트가 용이함
  - 제어의 역전을 통해 빈(객체)의 라이프싸이클에 관여하지 않고 개발에 집중할 수 있음
- 단점
  - XML을 기반으로 하는 프로젝트 설정은 너무 많은 시간을 필요로 함
  - 톰캣과 같은 WAS를 별도로 설치해주어야 함
- 해결책(Spring Boot)
  - 자동설정(AutoConfiguration)을 도입하여 Dispatcher Servlet 등과 같은 설정 시간을 줄여줌
  - 프로젝트의 의존성을 독립적으로 선택하지 않고 spring-boot-starter로 모아두어 외부 도구들을 사용하기 편리함
  - 내장 톰캣을 제공하여 별도의 WAS를 필요로 하지 않음 

 

 

### Spring @Bean, @Configuration, @Component 어노테이션

- @Bean: 개발자가 직접 제어가 불가능한 외부 라이브러리 또는 설정을 위한 클래스를 Bean으로 등록할 때 사용	
  - 가져다 쓰는 라이브러리일경우 ex RestTemplate을 싱글톤으로 생성하고 싶기 때문에 @Bean으로 등록 
- @Configuration: 1개 이상의 @Bean 메소드를 갖는 클래스의 경우에 반드시 명시해 주어야 함
- @Component: 개발자가 직접 개발한 클래스를 Bean으로 등록하고자 하는 경우에 사용
  - 스프링은 특정 어노테이션이 있는 클래스를 찾아서 빈으로 등록해주는 컴포넌트 스캔 기능을 제공. @Component 어노테이션이 있는 클래스를 찾아 자동으로 빈으로 등록해준다. 
  - SpringBoot를 이용할때는 @SpringBootConfiguration하위에 기본적으로 포함되어있어 별도 설정이 필요 없고, 아닌경우 Main또는 App 클래스에서 @ComponentScan으로 컴포넌트를 찾는 탐색 번위를 지정해주어야한다. 

스프링 컨테이너는 @Configuration이 붙어있는 클래스를 자동으로 빈으로 등록해두고, 해당 클래스를 파싱해서 @Bean이 있는 메소드를 찾아서 빈을 생성해준다.  (@Configuration안에서 Bean을 사용해야 싱글톤을 보장받을 수 있다. )

- @Configuration을 상속받은 클래스를 또 @Configuration으로 선언하여 override하게되면 해당 빈이 여러개 생성될 수 있다. 그래서 @Configuration이 있는 클래스를 객체로 생성할 때 CGLib 라이브러리를 사용해 프록시 패턴을 적용한다. 







### 의존성 주입 방법

- **생성자 주입**
  - 생성자의 호출시점에 1회 호출되는것이 보장됨 
  - 생성자가 1개인경우 @Autowired생략가능 
- 수정자 주입 (Setter)
  - 주입받는 객체가 변경될 가능성이 있는 경우에 사용하는데, 극히 드물다
- 필드 주입
  - 외부에서 변경이 불가능하다. 반드시 DI프레임워크가 존재해야하므로 사용을 지양해야한다. 테스트코드에서는 사용해도 괜찮다.

**생성자 주입을 사용해야 하는 이유**

- 의존 관계 주입의 변경이 필요한 사항은 없다. 수정자 주입이나 일반 메소드 주입은 수정의 가능성을 열어두게된다. 
- 실제 코드가 필드주입으로 작성되면 순수 자바 코드로 단위테스트를 하는것이 불가능하다. 
- final 키워드 작성 및 Lombok과의 결합
  - 생성자 주입을 사용하면 객체에 final키워드를 주입할 수 있고, 컴파일 시점에 누락된 의존성을 확인할 수 있다. 또한 Lombok의 @RequiredArgsConstructor와 함께 사용할 수 있다. 
- 순환참조를 애플리케이션 구동 시점에 파악하여 방지할 수 있다. 



## 애플리케이션 컨텍스트

Spring에서는 빈의 생성과 관계설정 같은 제어를 담당하는 IoC컨테이너인 빈 팩토리가 존재한다. 하지만 실제로는 빈의 생성과 관계설정이외에도 추가적인 기능이 필요한데, 이러한 이유로 Spring에서는 빈 팩토리를 상속받아 확장한 Application Context를 주로 사용한다. 

애플리케이션 컨텍스트는 별도의 설정 정보를 참고하고 IoC를 적용하여 빈의 생성, 관계 설정 등의 제어 작업을 총괄한다. 



ApplicationContext는 @Configuration이 붙은 클래스를 설정정보로 등록해주고 @Bean이 붙은 메소드의 이름으로 빈을 생성한다. 

클라이언트가 해당 빈을 요청한다. 

ApplicationContext는 자신의 빈 목록에서 요청한 이름이 있는지 찾는다. 

ApplicationContext는 설정 클래스로부터 빈 생성을 요청하고, 생성된 빈을 돌려준다. 

클라이언트는 @COnfiguration이 붙은 구체적인 팩토리클래스를 알 필요가 없으며, 어플리케이션컨텍스트가 종합 IoC 서비스를 제공해준다. 





## 트랜잭션

### 트랜잭션 전파

트랜잭션 전파란 트랜잭션의 경계에서 이미 진행중인 트랙잭션이 있거나 없을 때 어덯게 동작할것인가를 결정하는 방식을 의미한다. (A진행중, B 시작될때 )

- **REQUIRED**
  - **디폴트**
  - **이미 시작된 트랜잭션이 있으면 참여하고 없으면 새로시작** 
- SUPPORTS
  - 이미 시작된 트랜잭션이 있으면 참여하고 그렇지않으면 트랜잭션 없이 진행
- MANDATORY
  - 이미 시작된 트랜잭션이 있으면 참여하고 없으면 예외를 발생시킴
  - 혼자서 진행하면 안되는겨우
- REQUIRES_NEW
  - 항상 새로운 트랜잭션 시작
- NOT_SUPPORTEd
  - 이미 진행중인 트랜잭션이 있으면 보류시키고 트랜잭션을 사용하지 않도록함
- NEVER
  - 진행중인 트랜잭션이 있으면 예외발생, 트랜잭션 사용하지 않도록 강제 
- NESTED
  - 이미 진행중인 트랜잭션이 있으면 중첩트랜잭션을 시작
  - 먼저 시작된 부모 트랜잭션의 커밋과 롤백에는 영향을 받지만, 자신의 커밋과 롤백은 부모에게 영향을 주지 않음

```
- PROPAGATION_REQUIRED
  - A의 트랜잭션에 참여 
  - B의 작업이 마무리되고나서 A의 작업이 처리될 때 예외가 발생하면 A,B모두 롤백 
- PROPAGATION_REQUIRES_NEW
  - B의 트랜잭션은 A와 무관하고 B의 트랜잭션 경계를 빠져나오는 순간 B의 트랜잭션은 독자적으로 커밋 또는 롤뱃되며, A에 영향을 주지 않는다. 
  - A가 예외가 발생해도 B에 영향을 주지 않는다.
- PROPAGATRION_NOT_SUPPORTED
  - B작업에트랜잭션없이 동작(단순 데이터조회일경우)
```



### 격리수준(isolation)

모든 디비 트랜잭션은 격리수준을 가지고 있어야한다. 여러개의 트랜잭션이 동시에 진행될 수 있는데, 모든 트랜잭션을 독립적으로 만들고 순차진행한다면 안전하겠지만 성능이 떨어질 수 밖에 없다. 따라서 적절하게 격리수준을 조정해서 가능한 많은 트랜잭션을 동시에 진행시키며 문제가 발생하지 않도록 제어해야한다. 

- DEFAULT
  - 사용하는 데이터 액세스기술, DB 드라이버 디폴트 설정을 따름 
  - READ_COMMITED가 일반적으로 기본 
- READ_UNCOMMITED
  - 가장 낮은 격리수준으로 하나의 트랜잭션이 커밋되기 전에 그 변화가 다른 트랜잭션에 그대로 노출됨 
  - 일관성이 떨어지더라도 성능을 극대화할경우 사용
- READ_COMMITED
  - SPRING의 default설정
  - 트랜잭션이 커밋하지 않은 정보는 읽을 수 없음
  - 이미 읽은 트랜잭션을 다른 트랜잭션이 수정할 수 있어서 처음 트랜잭션이 다시 읽을 때 다른 내용이 발견되 수 있음 
- REPEATABLE_READ
  - 하나의 트랜잭션이 읽은 로우를 다른 트랜잭션이 수정할 수 없도록 막아주지만 새로운 로우를 추가하는것은 막지 않음 
  - select의 경우 트랜잭션이 끝나기 전에 추가된 로우가 발견될 수 있음 
- SERIALIZABLE
  - 가장 강력한 트랜잭션 격리 수준으로 이름 그대로 트랜잭션을 순차적으로 진행시켜줌
  - 여러트랜잭션이 동시에 같은 테이블 정보 액세스할 수 없음
  - 극단적.

### 읽기전용

- 읽기전용으로 설정하여 dirty checking off
- 쓰기작업 의도적으로 방지 

### 롤백, 커밋 예외

- 런타임 예외시 롤백, 예외가 발생하지 않았거나 체크 예외시 커밋하는 기본값설정
- 롤백 발생시킬 예외나 클래스를 rollbackFor, rollbackForClassName으로 지정 가능, 



### AOP

애플리케이션에 공통적으로 나타나는 부가적인 기능들을 독립적으로 모듈화하는 프로그래밍 모델이다. 

- 어드바이스(Advice) : 타겟 오브젝트에 적용하는 부가기능을 담은 오브젝트
- 포인트컷(PointCut) : 메소드 선정 알고리즘을 담은 오브젝트





핸들러 어댑터는 ArgumentResolver를 호출해서 미리 들어갈 매개변수를 파악하고 생성한다. HandlerMethodArgumentResolver




@Controller, @RestController, @ControllerAdvice가 붙은 클래스들에 @ExceptionHandler를 추가하여 에러처리 . ExceptionHandlerExceptionResolver가 처리함 

ErrorResponse를 만들고..

ControllerAdvice는 여러 컨트롤러에 대해 전역적으로 ExcpetionHandler를 적용할수 있도록 해줌. 



출처: https://mangkyu.tistory.com/95 [MangKyu's Diary]

