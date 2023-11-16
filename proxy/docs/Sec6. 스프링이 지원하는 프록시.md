## 6. 스프링이 지원하는 프록시



#### 동적 프록시를 사용할 때의 문제점

- 인터페이스가 있는 경우에는 JDK 동적 프록시, 그렇지 않은 경우에는 CGLIB를 적용하려면?
- 두 기술을 사용할 때 부가기능을 제공하기 위해 JDK 동적 프록시는 `InvocaionHandler`, CGLIB는 `MethodInterceptor` 를 각각 중복으로 만들어서 관리해야할까 ?
- 특정 조건에 맞을 때 프록시 로직을 적용하는 기능도 공통으로 제공되었으면?



스프링은 유사한 구체적인 기술이 있을 때, 통합해서 일관성있게 접근할 수 있고 더욱 편리하게 사용할 수 있는 추상화된 기술을 제공한다.

- 동적 프록시를 위한 **프록시 팩토리(`ProxyFactory`)**

![스크린샷 2023-10-31 오후 11.21.06](스크린샷%202023-10-31%20오후%2011.21.06.png)



**의문**

두 기술을 함께 사용하기 위해 Handler를 각각 만들어야할까?

-> 스프링에서 `Advice` 라는 개념을 도입해서 개발자는 `InvocationHandler` 나 `MethodInterceptor` 를 신경쓰지 않고, `Advice` 만 만들면 된다.

![스크린샷 2023-10-31 오후 11.23.17](스크린샷%202023-10-31%20오후%2011.23.17.png)



**특정 조건이 맞을 때 만 프록시 로직을 적용하는 기능도 공통으로 제공되었으면?**

-> 스프링에서 `Pointcut` 이라는 개념을 도입해 일관성있게 해결



### 프록시 팩토리 - 예제 코드 1

**Advice**

: **프록시에 적용하는 부가기능 로직이다.** 이것은 JDK 동적 프록시가 제공하는 `InvocationHandler` 와 CGLIB가 제공하는 `MethodInterceptor` 의 개념과 유사한데, 둘을 개념적으로 추상화한 것이다. 프록시 팩토리를 사용하면 둘 대신에 `Advice` 를 사용하면 된다.



`org.aopalliance.intercept` 

- MethodInterceptor -> Interceptor -> Advice 상속관계
- 스프링 AOP 모듈 (`spring-top`) 안에 들어있음



기존 코드와는 다르게 `invoke(MethodInvocation invocation)` 만 구현하면 되고, target을 따로 명시하지 않아도 된다.

- invocation.proceed()를 호출하면 target 클래스를 호출하고 그 결과를 받는다.
- target 클래스의 정보는 MethodInvocation안에 모두 포함되어있다.
- 그 이유는 프록시 팩토리로 프록시를 생성하는 단계에서 이미 target 정보를 파라미터로 전달받기 때문이다.



```java
    @Test
    @DisplayName("인터페이스가 있으면 JDK 동적 프록시 사용")
    void interfaceProxy() {
        ServiceInterface target = new ServiceImpl(); // 인터페이스가 있음

        ProxyFactory proxyFactory = new ProxyFactory(target); // 여기서 넣어주기 때문에 advice 구현 함수에 target이 명시되어있지 않아도 된다
        proxyFactory.addAdvice(new TimeAdvice());
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());
      
      
        proxy.save();
        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue();
        assertThat(AopUtils.isCglibProxy(proxy)).isFalse();
    }
```

```
23:54:34.481 [main] INFO hello.proxy.proxyfactory.ProxyFactoryTest - targetClass=class hello.proxy.common.service.ServiceImpl
23:54:34.484 [main] INFO hello.proxy.proxyfactory.ProxyFactoryTest - proxyClass=class jdk.proxy2.$Proxy9
```



**AopUtils**

- 프록시 팩토리로 만든 프록시에 대해서 몇가지 검사 하는 함수가 제공됨



### 프록시 팩토리 - 예제 코드 2

```java
    @Test
    @DisplayName("구체 클래스만 있으면 CGLIB 사용 ")
    void concreteProxy() {
        ConcreteService target = new ConcreteService(); // 인터페이스가 있음

        ProxyFactory proxyFactory = new ProxyFactory(target); // 여기서 넣어주기 때문에 advice 구현 함수에 target이 명시되어있지 않아도 된다
        proxyFactory.addAdvice(new TimeAdvice());
        ConcreteService proxy = (ConcreteService) proxyFactory.getProxy();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.call();
        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
    }
```

```
00:00:24.499 [main] INFO hello.proxy.proxyfactory.ProxyFactoryTest - targetClass=class hello.proxy.common.service.ConcreteService
00:00:24.502 [main] INFO hello.proxy.proxyfactory.ProxyFactoryTest - proxyClass=class hello.proxy.common.service.ConcreteService$$EnhancerBySpringCGLIB$$ea77b9f7
00:00:24.505 [main] INFO hello.proxy.common.advice.TimeAdvice - TimeProxy 실행
00:00:24.516 [main] INFO hello.proxy.common.service.ConcreteService - ConcreteService 호출
00:00:24.516 [main] INFO hello.proxy.common.advice.TimeAdvice - TimeProxy 종료 resultTime = 11
```





```java
    @Test
    @DisplayName("ProxytargetClass 옵션을 사용하면 인터페이스가 있어도 CGLIB를 사용하고 클래스 기반 프록시 사용")
    void proxyTargetClassTest() {
        ServiceInterface target = new ServiceImpl(); // 인터페이스가 있음

        ProxyFactory proxyFactory = new ProxyFactory(target); // 여기서 넣어주기 때문에 advice 구현 함수에 target이 명시되어있지 않아도 된다
        proxyFactory.setProxyTargetClass(true); // 이거를 넣으면 CGLIB 기반으로 만들어짐
        proxyFactory.addAdvice(new TimeAdvice());
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.save();
        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
    }
```

```
00:02:08.999 [main] INFO hello.proxy.proxyfactory.ProxyFactoryTest - targetClass=class hello.proxy.common.service.ServiceImpl
00:02:09.004 [main] INFO hello.proxy.proxyfactory.ProxyFactoryTest - proxyClass=class hello.proxy.common.service.ServiceImpl$$EnhancerBySpringCGLIB$$ccd7ddea
00:02:09.007 [main] INFO hello.proxy.common.advice.TimeAdvice - TimeProxy 실행
00:02:09.025 [main] INFO hello.proxy.common.service.ServiceImpl - save 호출
00:02:09.025 [main] INFO hello.proxy.common.advice.TimeAdvice - TimeProxy 종료 resultTime = 18
```



**프록시 팩토리의 기술 선택 방법**

- 대상에 인터페이스가 있으면 : JDK 동적 프록시, 인터페이스기반 프록시
- 대상에 인터페이스가 없으면 : CGLIB 구체 클래스 기반 프록시
- `proxyTargetClass=ture` : CGLIB, 구체클래스 기반 프록시, 인터페이스 여부와 상관 없음



**정리**

프록시 팩토리의 서비스 추상화 덕분에 구체적인 프록시 기술에 의존하지 않고 동적 프록시를 생성할 수 있다.

프록시의 부가 기능 로직도 특정 기술에 종속적이지 않게 Advice 하나로 편리하게 사용할 수 있다.



**참고, 주의!**

**스프링 부트는 AOP를 적용할 때 기본적으로 proxyTargetClass=true 로 설정해서 사용한다. 따라서 인터페이스가 있어도 항상 CGLIB를 사용해서 구체 클래스 기반으로 프록시를 생성한다.** 



### 포인트컷, 어드바이스, 어드바이저

- **포인트컷(Pointcut)** 
  - 어디에 부가 기능을 적용할지, 어디에 부가 기능을 적용하지 않을지 판단하는 필터링 로직
  - 클래스와 메서드 이름으로 필터링
  - 이름 그대로 어떤 포인트에 기능을 적용할지 않을지 잘라서 구분하는 것
- **어드바이스(Advice)**
  - 프록시가 호출하는 부가 기능
- **어드바이저(Advisor)**
  - 하나의 포인트컷과 하나의 어드바이스를 가지고 있는 것 



**역할과 책임**

이렇게 구분한 것은 역할과 책임을 명확하게 분리한 것이다.

- 포인트컷은 대상 여부를 확인하는 필터 역할만 담당
- 어드바이스는 깔끔하게 부가 기능 로직만 담당한다.
- 둘을 합치면 어드바이저



### 예제코드 1.  어드바이저

```java
    @Test
    void advisorTest1() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);

        proxyFactory.addAdvisor(new DefaultPointcutAdvisor(Pointcut.TRUE, new TimeAdvice()));

        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        proxy.save();
        proxy.find();
    }
```



![스크린샷 2023-11-07 오후 11.37.09](스크린샷%202023-11-07%20오후%2011.37.09.png)

![스크린샷 2023-11-07 오후 11.37.18](스크린샷%202023-11-07%20오후%2011.37.18.png)

addAdvice하면 어쨌든 advisor가 들어가는 것



### 예제 코드 2.  직접 만든 포인트 컷

save에는 어드바이스 로직 적용하고, find에는 적용 안하게 구현해볼 것

```java
public interface Pointcut {

	/**
	 * Return the ClassFilter for this pointcut.
	 * @return the ClassFilter (never {@code null})
	 */
	ClassFilter getClassFilter();

	/**
	 * Return the MethodMatcher for this pointcut.
	 * @return the MethodMatcher (never {@code null})
	 */
	MethodMatcher getMethodMatcher();


	/**
	 * Canonical Pointcut instance that always matches.
	 */
	Pointcut TRUE = TruePointcut.INSTANCE;

}
```

스프링에서 제공하는 Pointcut 인터페이스



```java
@Test
    @DisplayName("직접 만든 포인트 컷")
    void advisorTest2() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);

        proxyFactory.addAdvisor(new DefaultPointcutAdvisor(new MyPointcut(), new TimeAdvice()));

        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        proxy.save();
        proxy.find();
    }

    static class MyPointcut implements Pointcut {
        @Override
        public ClassFilter getClassFilter() {
            return ClassFilter.TRUE;
        }

        @Override
        public MethodMatcher getMethodMatcher() {
            return new MyMethodMatcher();
        }
    }

    static class MyMethodMatcher implements MethodMatcher {
        private static String MATCH_NAME = "save";
        @Override
        public boolean matches(Method method, Class<?> targetClass) {

            boolean result = method.getName().equals(MATCH_NAME);
            log.info("포인트컷 호출 method={} targetClass={}", method.getName(), targetClass.getName());
            log.info("포인트컷 결과 result={}", result);
            return result;
        }

        /**
         * isRuntime이 false인 경우에는 정적 정보만 사용하기 때문에 스프링 내부에서 캐싱을 한다.
         * @return
         */
        @Override
        public boolean isRuntime() {
            return false;
        }

        @Override
        public boolean matches(Method method, Class<?> targetClass, Object... args) {
            return false;
        }
    }
```

```bash
23:44:19.926 [main] INFO hello.proxy.advisor.AdvisorTest - 포인트컷 호출 method=save targetClass=hello.proxy.common.service.ServiceImpl
23:44:19.930 [main] INFO hello.proxy.advisor.AdvisorTest - 포인트컷 결과 result=true
23:44:19.934 [main] INFO hello.proxy.common.advice.TimeAdvice - TimeProxy 실행
23:44:19.934 [main] INFO hello.proxy.common.service.ServiceImpl - save 호출
23:44:19.934 [main] INFO hello.proxy.common.advice.TimeAdvice - TimeProxy 종료 resultTime = 0
23:44:19.935 [main] INFO hello.proxy.advisor.AdvisorTest - 포인트컷 호출 method=find targetClass=hello.proxy.common.service.ServiceImpl
23:44:19.935 [main] INFO hello.proxy.advisor.AdvisorTest - 포인트컷 결과 result=false
23:44:19.935 [main] INFO hello.proxy.common.service.ServiceImpl - find 호출
```



### 스프링이 제공하는 포인트컷

```java

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("save");
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, new TimeAdvice());

```

- `NameMatchMethodPointcut` : 메서드 이름 기반 매칭
  - PatternMatchUtils사용해서 ** 이런거 허용
- `JdkRegexpMethodPointcut`  : JDK 정규표현식 기반 
- `TruePointcut` : 항상 참을 반환
- `AnnotationMatchingPointcut` : 애노테이션으로 매칭
- `AspectJExpressionPointcut` : aspectJ표현식으로 매칭
  - 실무에서는 이거를 기반으로한걸 제일 많이 씀



### 여러 어드바이저 함께 적용

 ```java
     @Test
     @DisplayName("여러 프록시")
     void multiAdvisorTest1() {
         // client -> proxy2(advisor2) -> proxy1(advisor1) -> target
 
         // 프록시1 생성
         ServiceInterface target = new ServiceImpl();
         ProxyFactory proxyFactory1 = new ProxyFactory(target);
         DefaultPointcutAdvisor advisor1 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice1());
         proxyFactory1.addAdvisor(advisor1);
         ServiceInterface proxy1 = (ServiceInterface) proxyFactory1.getProxy();
 
         // 프록시2 생성, target -> proxy1 입력
         ProxyFactory proxyFactory2 = new ProxyFactory(proxy1);
         DefaultPointcutAdvisor advisor2 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice2());
         proxyFactory2.addAdvisor(advisor2);
         ServiceInterface proxy2 = (ServiceInterface) proxyFactory2.getProxy();
 
         // 실행
         proxy2.save();
     }
 
 ```

```bash
23:55:39.527 [main] INFO hello.proxy.advisor.MutliAdvisorTest$Advice2 - advice2 호출
23:55:39.529 [main] INFO hello.proxy.advisor.MutliAdvisorTest$Advice1 - advice1 호출
23:55:39.530 [main] INFO hello.proxy.common.service.ServiceImpl - save 호출
```





![스크린샷 2023-11-07 오후 11.56.12](스크린샷%202023-11-07%20오후%2011.56.12.png)



=> 적용하려는 advice만큼 프록시를 생성해야함





**하나의 프록시, 여러 어드바이저**

```java
    @Test
    @DisplayName("하나의 프록시, 여러 어드바이저")
    void multiAdvisorTest2() {
        // client -> proxy -> advisor2 -> advisor1 -> target

        DefaultPointcutAdvisor advisor1 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice1());
        DefaultPointcutAdvisor advisor2 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice2());

        // 프록시1 생성
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvisor(advisor2);
        proxyFactory.addAdvisor(advisor1);
        ServiceInterface proxy2 = (ServiceInterface) proxyFactory.getProxy();

        // 실행
        proxy2.save();
    }
```

addAdvisor로 등록하면 된다.



- 스프링 AOP를 적용할 때, 최적화를 진행해서 지금처럼 프록시는 하나만 만들고 하나의 프록시에 여러 어드바이저를 적용한다.
- **정리하면 하나의 target에 여러 AOP를 동시에 적용하더라도, 스프링의 AOP는 target마다 하나의 프록시만 생성한다.** --> 기억하기!



### 정리

**남은 문제**

1. 너무 많은 설정

Config파일이 너무 많다. 애플리케이션에 스프링 빈이 100개가 있다면 부가기능을 적용하기 위해 100개의 동적프록시생성 코드를 만들어야한다.

프록시 코드에 빈 생성 코드까지 다 넣어야함

2. 컴포넌트 스캔

컴포넌트 스캔을 사용하는 경우 지금까지 학습한 방법으로 프록시 적용이 불가능하다.

-> 이것들을 해결하기 위한 방법이 다음의 빈 후처리기
