## 포인트컷

### 포인트컷 지시자

- execution : 메소드 실행 조인포인트 매칭
- within : 특정 타입 내의 조인포인트 매칭
- args : 인자가 주어진 타입의 인스턴스인 조인 포인트
- this : 스프링 빈 객체를 대상으로 하는 조인포인트
- target : Target객체를 대상으로 하는 조인포인트
- @target
- @within
- @annotation
- @args
- bean



### execution 문법

```
execution(modifiers-pattern? ret-type-pattern declaring-type-pattern?name-pattern(param-pattern) throws-pattern?)

exectuion(접근제어자? 반환타입 선언타입?메서드이름(파라미터) 예외)
```

매칭 조건

- 접근 제어자? : public
- 반환 타입 : String
- 선언 타입? : hello.aop.member.MemberServiceImpl
- 메서드이름 : hello
- 파라미터 : (String)
- 예외? : 생략



```java
    @Test
    void exactMatch() {
        // helloMethod=public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(public String hello.aop.member.MemberServiceImpl.hello(String))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void allMatch() {
        // helloMethod=public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* *(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
```

가장 많이 생략한 포인트컷

매칭 조건

- 접근 제어자? : 생략
- 반환 타입 : *
- 선언 타입? : 생략
- 메서드이름 : *
- 파라미터 : (..)
  - 파라미터 타입과 파라미터 수가 상관없다는 뜻
- 예외? : 생략



패키지

- . : 정확하게 그 패키지
- .. : 해당 위치의 패키지와 그 하위패키지도 포함

----



execution 타입 매칭

- 인터페이스로 부모 타입도 할 수 있음
- 부모타입에 선언된 메서드 까지만 허용함



execution 파라미터 매칭

- 파라미터 , 타입, 개수도 다 봄
- .. 가 모두 포함시키는 것 



![스크린샷 2024-01-04 오후 10.45.51](/Users/jskang/Library/Application Support/typora-user-images/스크린샷 2024-01-04 오후 10.45.51.png)



within

- 타입만 
- exectuion 대신 within()에 타입만 넣은거
- 주의 : 부모타입 지정 불가. 정확하게 타입이 맞아야 한다.



args

- execution의 args 부분과 같음
- 파라미터의 부모 타입을 허용함 (execution은 비허용)

- execution은 메서드의 시그니처를 정적으로 판단
- args는 런타임에 전달된 인수로 판단

args는 단독으로는 사용 잘 안함



@target, @within

- @target : 인스턴스의 모든 메서드, 부모 클래스꺼까지
- @within : 해당 타입내에 있는 메서드
- ![스크린샷 2024-01-04 오후 10.55.28](/Users/jskang/Library/Application Support/typora-user-images/스크린샷 2024-01-04 오후 10.55.28.png)



args, @args, @target은 단독으로 사용하면 안된다. 

적용대상을 줄이고 나서 그 뒤에 사용해야한다.



@annotation

bean

- 스프링 빈의 이름으로 AOP 적용여부 지정.
- 스프링에서만 사용할 수 있음 (스프링 전용)



매개변수 전달

```java
@Around("allMember() && args(arg, ..)")
public Object logArgs2(ProceedingJoinPoint joinPoint, Object arg) throws Throwable {
  log.info("[logArgs]{}, arg ={}", joinPoint.getSignature(), arg);
  return joinPoint.proceed();
}

@Around("allMember() && args(arg, ..)")
public void logArgs3(String arg) {
  log.info("[logArgs3] arg={}", arg);
}

```

```java
@Before("allMember() && target(obj)")
public void thisArgs(JoinPoint joinPoint, MemberService obj) {
  log.info("[target]{}, obj={}", joinPoint.getSignature(), obj.getClass());
}
// obj = class hello.aop.member.MemberServiceImpl
// 실제 대상 구현체

@Before("allMember() && this(obj)")
public void targetArgs(JoinPoint joinPoint, MemberService obj) {
  log.info("[this]{}, obj={}", joinPoint.getSignature(), obj.getClass();)
}
// obj = class hello.aop.member.MemberServiceImpl$$EnhaneerBySrpingCGLIB$$a4e3559d
// 프록시객체
```

this - 프록시 객체를 전달 받음 

target - 실제 대상 객체를 전달 받음



- @target

  - ClassAop annotation으로 받아올 수 있음

- @within(annotation)

  - ClassAop annotation 받아올 수 있음

- @annotation(annotation)

  - MethodAop annotation으로 받아올 수 있음 -> value로 넣은거

  

**this, target**

정의

- this : 스프링 빈 객체(스프링 AOP 프록시)를 대상으로 하는 조인 포인트
- target : Target 객체 (스프링 AOP 프록시가 가르키는 실제 대상)을 대상으로 하는 조인 포인트

설명

- this, target은 다음과 같이 적용 타입 하나를 정확하게 지정해야한다.

- ```java
  this(hello.aop.member.MemberService)
  target(hello.aop.member.MemberService)
  ```

- *같은 패턴 사용할 수 없고, 부모 타입을 허용한다.



**차이**

스프링에서 AOP를 적용하면 실제 target 객체 대신에 프록시 객체가 스프링 빈으로 등록되는데,

- this: 정확히 '프록시'
- target: 그 '대상', target 객체





JDK : 인터페이스가 필수이고, 인터페이스를 구현한 프록시 객체를 생성

1. 인터페이스를 지정했을 때

   1. this, target 모두 AOP 적용됨

2. 구체 클래스를 지정했을 때

   1. this : JDK 동적프록시로 만들어진 프록시 객체는 인터페이스 기반으로 구현된 새로운 클래스라서, memberServiceImpl을 전혀 알지 못해 AOP 적용 대상이 아님

   1. target : 객체를 보고 판단하니깐 target객체가 MemberServiceImpl 타입이라 AOP 적용 대상이다.

CGLIB : 인터페이스가 있어도, 구체 클래스를 기반으로 프록시를 만듦

1. 인터페이스를 지정했을 때
   1. this: 프록시 보고 판단. 부모 타입 허용하므로 적용됨
   2. target: 객체 보고 판단. 부모타입 허용하므로 적용 됨
2. 구체 클래스를 지정했을 때
   1. this : 프록시 보고 판단. memberServiceImpl 기반으로 만들기때문에 AOP 적용된다.
   2. target: 객체 보고 판단. 당연히 됨



**결론 : 프록시를 대상으로 하는 this 의 경우 구체 클래스를 지정하면 프록시 생성 전략에 따라서 다른 결과가 나올 수 있다는 것을 알아 두자**



스프링 부트는 기본으로 쓰면 다 CGLIB로 만들어버림.

```
# application.properties

# CGLIB
#spring.aop.proxy-target-class= ture

# JDK
spring.aop.proxy-target-class= false
```



```bash
        /** CGLIB
         * memberService Proxy=class hello.aop.member.MemberServiceImpl$$SpringCGLIB$$0
         * [target-impl]String hello.aop.member.MemberServiceImpl.hello(String)
         * [target-interface]String hello.aop.member.MemberServiceImpl.hello(String)
         * [this-impl]String hello.aop.member.MemberServiceImpl.hello(String)
         * [this-interface]String hello.aop.member.MemberServiceImpl.hello(String)
         */

        /***
         * JDK
         * memberService Proxy=class jdk.proxy2.$Proxy55
         * [target-impl]String hello.aop.member.MemberService.hello(String)
         * [target-interface]String hello.aop.member.MemberService.hello(String)
         * [this-interface]String hello.aop.member.MemberService.hello(String)
         */
```

JDk일때 this-impl은 AOP가 적용되지 않는다.