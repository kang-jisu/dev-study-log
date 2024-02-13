## AOP 실무 주의사항



#### 프록시와 내부 호출 - 문제

스프링 - 프록시 방식의 AOP 사용

프록시를 통해서 대상 객체를 호출해야하는데, 객체의 내부 메서드 호출이 발생할 때 프록시를 거치지 않고 대상 객체를 직접 호출하는 문제가 발생한다.



```bash
CallLogAspect     : aop = void hello.aop.internalcall.CallServiceV0.external()
CallServiceV0     : call external
CallServiceV0     : call internal
```



external -> internal 호출할 때 Aspect aop 거치지 않음

![스크린샷 2024-01-18 오후 11.24.14](/Users/jskang/Library/Application Support/typora-user-images/스크린샷 2024-01-18 오후 11.24.14.png)



자기 자신 인스턴스의 내부 메서드를 호출하는 this.internal() 은 this는 대상객체이기 때문에 프록시를 거치지 않는다. 그래서 어드바이스도 적용할 수 없다.



스프링의 프록시 방식의 AOP는 내부호출에 프록시를 적용할 수가 없다. 

AspectJ를 사용하면 이런 경우가 나타나지 않는데, 스프링 AOP를 사용하면 이렇게 된다.



#### 대안 1 - 자기 자신 주입

자기 자신을 의존관계 주입 해버리기

- this가 아닌 자기자신.내부메서드 호출 



#### 대안 2.  지연 조회

- applicationContext.getBean으로 지연 호출
- 근데 애플리케이션 컨텍스트는 기능이 너무 많음 
- ObjectProvider.getObject



#### 대안3 - 구조 변경

- 이게 가장 많이 쓰고, 올바른 방법일 것 같음



AOP는 주로 인터페이스에 메서드가 나올 정도 규모에서 적용하는 것이 적당하다. AOP는 public 메서드에만 적용한다. private 메서드같은 작은 단위에는 적용하지 않는다. 

AOP가 잘 적용되지 않는다면 내부 호출을 의심해보자.



----

## 프록시 기술과 한계



### 1. 타입 캐스팅

스프링이 프록시를 만들 때 제공하는 ProxyFactory에 proxyTargetClass 옵션에 따라 CGLIB, JDK 동적프록시 선택할 수 있다.

- false : JDK
- true : CGLIB



#### JDK 동적 프록시의 한계가 있음

```
java.lang.ClassCastException: class jdk.proxy2.$Proxy9 cannot be cast to class hello.aop.member.MemberServiceImpl (jdk.proxy2.$Proxy9 is in module jdk.proxy2 of loader 'app'; hello.aop.member.MemberServiceImpl is in unnamed module of loader 'app')
```

MemberServiceImpl 타입 기반으로 JDK 동적 프록시를 생성함.

MemberService 인터페이스 기반으로 생성한거니깐 MemberServiceImpl이 뭔지 모름



CGLIB는 구체클래스를 기반으로 프록시를 생성하므로 타입 캐스팅이 가능함.



### 2. 의존관계 주입

JDK 동적프록시를 사용하면 구체 클래스 타입을 의존관계주입받을때 문제가 생긴다.

memberServiceImpl 의존성주입이 안됨.





JDK 프록시는 MemberService 인터페이스를 기반으로 만들어지기때문에 MemberServiceImpl타입이 뭔지 모름. 그래서 해당 타입에 주입할 수 없다.

CGLIB는 구체클래스를 기반으로 만들기 때문에 MemberServiceImpl도 이걸 기반으로 만들었으니 해당 타입으로 캐스팅할 수 있어서 의존관계주입도 할 수 있다.



JDK동적프록시는 대상 객체인 Impl타입에 의존관계를 주입할 수 없다.

CGLIB 프록시는 대상 객체인 Impl타입에 의존관계 주입을 할 수 있다.



올바르게 DI 하려면 인터페이스로 의존관계주입을 해줘야한다.

### 3. CGLIB의 한계

- 대상 클래스에 기본 생성자 필수
  - 자바 언어에서 상속을 받으면 자식클래스 생성자 호출할 때 자식 클래스의 생성자에서 부모클래스의 생성자도 호출해야한다.
  - CGLIB를 사용할 때 생성ㅇ자에서 대상클래스의 기본 생성자를 호출한다. 그래서 만들어줘야한다. 
- 생성자가 2번 호출됨
  - CGLIB는 구체클래스를 상속받음. 자바언어에서 상속을 받으면 자식클래스의 생성자를 호출할 때 부모클래스의 생성자도 호출해야한다.
  - 실제 target 객체 생성할때, 프록시객체 생성할때 -> 부모 클래스의 생성자가 2번 호출됨
- final 키워드 클래스, 메서드 사용 불가 (상속이 안되므로)
  - 일반 웹 어플리케이션에서는 final키워드를 잘 사용하지는 않긴함.



### 4. 스프링의 해결책

- 스프링 3.2 , CGLIB 스프링 내부에 패키징 (spring-core)

- 스프링 4.0부터 CGLIB 기본생성자 문제 해결

  - objenesis 를 이용해 기본 생성자 없이 객체 생성 가능

- 생성자2번 호출 문제

  - 이것도 objenesis를 이용해 생성자가 1번만 호출된다

- 스프링부트 2.0 -> CGLIB를 기본으로 사용함

  - spring.aop.proxy--target-class=false로 JDK설정 가능, true가 기본값(CGLIB)

  

