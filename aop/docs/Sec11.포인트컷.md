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
- 