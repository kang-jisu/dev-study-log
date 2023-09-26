## 동적 프록시 기술

대상 클래스 수 만큼 프록시 클래스를 만들어야한다는 단점을 극복하는 방법

자바가 기본으로 제공하는 JDK 동적 프록시 기술이나, CGLIB 같은 프록시 생성 오픈소스 기술을 활용하면 프록시 객체를 동적으로 만들어낼 수 있다.



### 리플렉션

그 전에 JDK 동적 프록시를 이해하기 위해서는 자바의 리플렉션 기술을 이해해야한다.

리플렉션 기술을 사용하면 클래스나 메서드의 메타정보를 동적으로 획득하고, 코드도 동적으로 호출할 수 있다.



```java
@Slf4j
public class ReflectionTest {

    @Test
    void reflection0() {
        Hello target = new Hello();

        // 공통 로직 1 시작
        log.info("start"); 
        String result1 = target.callA(); // 호출하는 메서드가 다름 
        log.info("result={}", result1);
        // 공통 로직 1 종료

        // 공통 로직 2 시작
        log.info("start");
        String result2 = target.callB(); // 호출하는 메서드가 다름 
        log.info("result={}", result2);
        // 공통 로직 2 종료
    }

    @Slf4j
    static class Hello {
        public String callA() {
            log.info("callA");
            return "A";
        }
        public String callB() {
            log.info("callB");
            return "B";
        }
    }
}

```

```bash
00:21:56.553 [main] INFO hello.proxy.jdkdynamic.ReflectionTest - start
00:21:56.556 [main] INFO hello.proxy.jdkdynamic.ReflectionTest$Hello - callA
00:21:56.556 [main] INFO hello.proxy.jdkdynamic.ReflectionTest - result=A
00:21:56.557 [main] INFO hello.proxy.jdkdynamic.ReflectionTest - start
00:21:56.557 [main] INFO hello.proxy.jdkdynamic.ReflectionTest$Hello - callB
00:21:56.557 [main] INFO hello.proxy.jdkdynamic.ReflectionTest - result=B
```



- 다음 예제는 아주 똑같은 로직인데, 호출하는 메서드만 다른 상태
- 공통로직1과 공통로직2를 하나의 메서드로 뽑아서 합칠 수 있을까?

이럴 때 사용하는 기술이 바로 리플렉션이다. 리플렉션은 클래스나 메서드의 메타 정보를 사용해서 동적으로 호출하는 메서드를 변경할 수 있다.



참고 : 람다를 사용해서 공통화 하는것도 가능하지만, 람다를 사용하기 어려운 상황이라 가정



```java
    @Test
    void reflection1() throws Exception {
        // 클래스의 메타정보 획득. 내부 클래스는 구분을 위해 $를 사용
        Class<?> classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");
        Hello target = new Hello();

        //callA의 메서드 정보
        Method methodCallA = classHello.getMethod("callA");
        Object result1 = methodCallA.invoke(target); // target의 인스턴스에 있는 메서드를 호출하는 것
        log.info("result1={}", result1);

        //callB의 메서드 정보
        Method methodCallB = classHello.getMethod("callB"); // <-- 이 부분을 .callB가 아니라 "callB" 문자로 바꿨기 때문에 파라미터로 넘길 수 있을듯
        Object result2 = methodCallB.invoke(target); // target의 인스턴스에 있는 메서드를 호출하는 것
        log.info("result2={}", result2);
    }
}
```

```java
    @Test
    void reflection2() throws Exception {
        // 클래스의 메타정보 획득. 내부 클래스는 구분을 위해 $를 사용
        Class<?> classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");
        Hello target = new Hello();

        //callA의 메서드 정보
        Method methodCallA = classHello.getMethod("callA");
       dynamicCall(methodCallA, target);

        //callB의 메서드 정보
        Method methodCallB = classHello.getMethod("callB");
        dynamicCall(methodCallB, target);
    }

    private void dynamicCall(Method method, Object target) throws Exception {
        log.info("start");
        Object result = method.invoke(target);
        log.info("result={}", result);
    }
```



- dynamicCall은 공통로직1,2를 한 번에 처리할 수 있는 통합된 공통 처리 로직
- `Method method` : 첫 번째 파라미터는 호출할 메서드 정보가 넘어온다. 기존에는 메서드 이름을 직접 호출했지만, 이제는 `Method` 라는 메타정보를 통해서 호출할 메서드 정보가 동적으로 제공된다.
- `Object target` 실제 실행할 인스턴스 정보가 넘어온다. 타입이 Object이므로 어떠한 인스턴스도 받을 수 있지만, method.invoke(target)을 사용할 때 호출할 클래스와 메서드 저옵가 다르면 예외가 발생한다. `NoSuchMethodException` 



**정리**

정적인 `target.callA()`, `target.callB()` 코드를 리플렉션을 사용해서 `Method` 라는 메타정보로 추상화했다. 



**주의**

리플렉션을 사용하면 클래스와 메서드의 메타정보를 사용해서 애플리케이션을 동적으로 유연하게 만들 수 있다. **하지만 리플렉션 기술은 런타임에 동작하기 때문에, 컴파일 시점에 오류를 잡을 수 없다.**

리플렉션은 프레임워크 개발이나 매우 일반적인 공통 처리가 필요할 때 부분적으로 주의해서 사용해야한다.



### JDK 동적 프록시 - 소개

동적 프록시 기술을 사용하면 개발자가 직접 프록시 클래스를 만들지 않아도 된다. 이름 그대로 프록시 객체를 동적으로 런타임에 개발자 대신 만들어준다. 그리고 동적 프록시에 원하는 실행 로직을 지정할 수 있다.



**주의**
JDK 동적 프록시는 인터페이스를 기반으로 프록시를 동적으로 만들어준다. 따라서 인터페이스가 필수이다.

