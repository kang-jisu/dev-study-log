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

