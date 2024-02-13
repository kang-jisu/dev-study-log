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

정적인 `target.callA()`, `target.callB()` 코드를 리플렉션을 사용해서 `Method` 라는 **메타정보**로 추상화했다. 



**주의**

리플렉션을 사용하면 클래스와 메서드의 메타정보를 사용해서 애플리케이션을 동적으로 유연하게 만들 수 있다. **하지만 리플렉션 기술은 런타임에 동작하기 때문에, 컴파일 시점에 오류를 잡을 수 없다.**

리플렉션은 프레임워크 개발이나 매우 일반적인 공통 처리가 필요할 때 부분적으로 주의해서 사용해야한다.



### JDK 동적 프록시 - 소개

동적 프록시 기술을 사용하면 개발자가 직접 프록시 클래스를 만들지 않아도 된다. 이름 그대로 프록시 객체를 동적으로 런타임에 개발자 대신 만들어준다. 그리고 동적 프록시에 원하는 실행 로직을 지정할 수 있다.



**주의**
JDK 동적 프록시는 인터페이스를 기반으로 프록시를 동적으로 만들어준다. 따라서 인터페이스가 필수이다.



#### 예제

A, B 인터페이스/구현체를 만들고 프록시를 만들려면 A용 프록시, B용 프록시를 하나씩 만들어줘야했는데 동적프록시로 이를 해결해본다.



JDK 동적 프록시에 적용할 로직은 `InvocationHandler` 인터페이스를 구현해서 작성하면 된다. 

```java
package java.lang.reflect;

public interface InvocationHandler {
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
}
```

- `Object proxy` 프록시 자신
- `Method method` 호출한 메서드
- `Object[] args` 메서드를 호출할 때 전달한 인수



```java

@Slf4j
public class TimeInvocationHandler implements InvocationHandler {
    private final Object target;

    public TimeInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

        Object result = method.invoke(target, args);

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeProxy 종료 resultTime = {}" , resultTime);
        return result;
    }
}

```



```java
    @Test
    void dynamicA() {
        AInterface target = new AImpl();
        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        // AInterface.class.getClassLoader() : 어떤 클래스 로더에 할지 지정
        // new Class[]{AInterface.class : 어떤 인터페이스를 기반으로 만들지
        // handler : 프록시가 사용할 로직
        // 반환타입 Object에서 명시해주어야함
        AInterface proxy = (AInterface) Proxy.newProxyInstance(AInterface.class.getClassLoader(), new Class[]{AInterface.class}, handler);
        proxy.call();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());
    }
```

```bash
23:43:08.743 [main] INFO hello.proxy.jdkdynamic.code.TimeInvocationHandler - TimeProxy 실행
23:43:08.745 [main] INFO hello.proxy.jdkdynamic.code.AImpl - A 호출
23:43:08.746 [main] INFO hello.proxy.jdkdynamic.code.TimeInvocationHandler - TimeProxy 종료 resultTime = 0
23:43:08.748 [main] INFO hello.proxy.jdkdynamic.JDkDynamicProxyTest - targetClass=class hello.proxy.jdkdynamic.code.AImpl
23:43:08.748 [main] INFO hello.proxy.jdkdynamic.JDkDynamicProxyTest - proxyClass=class jdk.proxy2.$Proxy8
```



- `new TimeInvocationHandler(target)` : 동적 프록시에 적용할 핸들러 로직
- `Proxy.newProxyInstance(AInterface.calss.getClassLoader(), new Class[]{AInterface.class}, handler)` 
  - 동적 프록시는 `java.lang.reflect.Proxy` 를 통해서 생성
  - 클래스 로더 정보, 인터페이스, 핸들러 로직을 넣어주면 해당 인터페이스를 기반으로 동적 프록시를 생성하고 그 결과를 반환한다.



**실행 순서**

1. 클라이언트는 JDK 동적 프록시의 `call()` 을 실행한다. - 인터페이스의 메소드
2. JDK 동적 프록시는 `InvocationHandler.invoke()` 를 호출
3. `TimeInvocationHandler` 가 내부 로직을 수행하고, `method.invoke(target, args)` 를 통해서 target인 실제 객체 `AImpl` 을 호출한다.
4. `AImpl` 인스턴스의 `call()` 이 실행되고, TimeInvocationHandler 의 이후 로직이 실행되어 시간 로그를 출력하고 결과를 반환한다.

![스크린샷 2023-09-26 오후 11.58.37](./스크린샷%202023-09-26%20오후%2011.58.37.png)



![스크린샷 2023-09-27 오전 12.00.06](./스크린샷%202023-09-27%20오전%2012.00.06.png)



### JDK 동적프록시 적용

**JDK 동적프록시는 인터페이스가 필수**이므로 V1 애플리케이션에만 적용할 수 있다.



- InvocationHandler 인터페이스를 구현해서 JDK 동적프록시에서 사용

![스크린샷 2023-10-10 오후 11.56.12](/Users/jskang/Library/Application Support/typora-user-images/스크린샷 2023-10-10 오후 11.56.12.png)



- no log 호출해도 no log가 찍혀버림 (invoke 호출되어버림)
  - 메서드 이름 필터 추가 







### CGLIB(Code Generator Library)

인터페이스 없이 바이트코드 조작

- CGLIB는 바이트코드를 조작해서 동적으로 클래스를 생성하는 기술을 제공
- 인터페이스가 없어도 구체 클래스만 가지고 동적 프록시를 만들어냄
- CGLIB는 원래 외부 라이브러리인데, 스프링 프레임워크가 내부에 포함했다. 



CGLIB는 `MethodInterceptor` 를 제공한다.

```java
package org.springframework.cglib.proxy;

public interface MethodInterceptor extends Callback {
  Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable;
}
```

- `Object result = methodProxy.invoke(target, args);` 
- methodProxy를 사용하는걸 권장





```java
    @Test
    void cglib() {
        ConcreteService target = new ConcreteService();

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(ConcreteService.class); // 상속받을 부모 클래스 타입 지정
        enhancer.setCallback(new TimeMethodInterceptor(target));
        ConcreteService proxy = (ConcreteService) enhancer.create();

        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());
    }
}
```

```bash
00:16:22.239 [main] INFO hello.proxy.cglib.CbligTest - targetClass=class hello.proxy.common.service.ConcreteService
00:16:22.242 [main] INFO hello.proxy.cglib.CbligTest - proxyClass=class hello.proxy.common.service.ConcreteService$$EnhancerByCGLIB$$25d6b0e3

```

Cglib가 ConcreteService를 상속받아서 EnhancerByCGLIB를 만들어냄

- `대상클래스$$EnhancerByCGLIB$$임의코드`

JDK프록시가 생성한 클래스

- `proxyClass=class com.sun.proxy.$Proxy1`



- CGLIB 는 `Enhancer` 를  사용해서 프록시를 생성한다.



JDK프록시는 인터페이스를 구현해서 프록시를 만들고, CGLIB는 구체 클래스를 상속해서 프록시를 만든다.



![스크린샷 2023-10-11 오전 12.19.34](/Users/jskang/Library/Application Support/typora-user-images/스크린샷 2023-10-11 오전 12.19.34.png)



#### CGLIB 제약

- 클래스 기반 프록시는 상속을 사용하기 때문에 제약이 있다.
  - 부모 클래스의 생성자 -> CGLIB는 자식 클래스를 동적으로 생성하기 때문에 기본 생성자가 필요하다
  - 클래스에 final키워드가 붙으면 상속이 불가능하여 CGLIB 예외
  - 메서드에 final키워드가 붙으면 메서드오버라이딩 불가 -> 프록시 동작 안함





**두 기술 함께 사용할 때** 

- MethodHandler, InvocationHandler 두개 중복으로 만들어서 관리해야할까?
- 특정 조건에 맞을 때 프록시 로직을 적용하는 기능도 공통으로 제공되길 원할때?
- 스프링의 ProxyFactory



