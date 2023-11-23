# 빈 후처리기(BeanPostProcessor)

스프링이 빈 저장소에 등록할 목적으로 생성한 객체를 빈 저장소에 등록하기 직전에 조작하고 싶을 때 사용하면 된다.





**빈 후처리기의 기능**

- 객체 조작
- 완전히 다른 객체로 바꿔치기 하기



**빈 등록 과정을 빈 후처리기와 함께 살펴보기**

- 1. 생성 : 스프링 빈 대상이 되는 객체 생성
  2. 전달 : 생성된 객체를 빈 저장소에 등록하기 직전에 빈 후처리기에 전달
  3. 후 처리 작업 : 빈 후처리기는 전달된 스프링 빈 객체를 조작하거나 다른 객체로 바꿔치기 할 수 있다.
  4. 등록: 빈 후처리기는 빈을 반환한다. 전달된 빈을 그대로 반환하면 해당 빈이 등록되고, 바꿔치기 하면 다른 객체가 빈 저장소에 등록된다.

![스크린샷 2023-11-16 오후 11.45.50](/Users/jskang/Library/Application Support/typora-user-images/스크린샷 2023-11-16 오후 11.45.50.png)





## 빈 후처리기 - 예제 코드

![스크린샷 2023-11-16 오후 11.51.57](/Users/jskang/Library/Application Support/typora-user-images/스크린샷 2023-11-16 오후 11.51.57.png)

빈 후처리기를 사용하려면 BeanPostProcessor 인터페이스를 구현하고 스프링 빈으로 등록하면 된다.

- Before : @PostConstruct같은 초기화가 발생하기 전 호출
- After : @PostConstruct같은 초기화가 발생한 다음 호출





```java
  @Test
    void basicConfig() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanPostProcessorConfig.class);
        // beanA이름으로 B 객체가 빈으로 등록된다.
        B beanB = applicationContext.getBean("beanA", B.class);
        beanB.helloB();

        // A는 빈으로 등록되지 않는다.
        Assertions.assertThrows(NoSuchBeanDefinitionException.class,
                () -> applicationContext.getBean(A.class));
    }

    @Slf4j
    @Configuration
    static class BeanPostProcessorConfig {
            @Bean(name = "beanA")
            public A a() {
                return new A();
            }

            @Bean
            public AToBPostProcessor helloPostProcessor() {
                return new AToBPostProcessor();
            }

    }

    @Slf4j
    static class A {
        public void helloA() {
            log.info("hello A");
        }
    }

    @Slf4j
    static class B {
        public void helloB() {
            log.info("hello B");
        }
    }

    @Slf4j
    static class AToBPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            log.info("beanName={} bean={}", beanName, bean);
            if (bean instanceof A) {
                return new B();
            }
            return bean;
        }
    }
```

```bash
 INFO org.springframework.context.support.PostProcessorRegistrationDelegate$BeanPostProcessorChecker - Bean 'beanPostProcessorTest.BeanPostProcessorConfig' of type [hello.proxy.postprocessor.BeanPostProcessorTest$BeanPostProcessorConfig$$EnhancerBySpringCGLIB$$6c688924] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
 DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'beanA'
 INFO hello.proxy.postprocessor.BeanPostProcessorTest$AToBPostProcessor - beanName=beanA bean=hello.proxy.postprocessor.BeanPostProcessorTest$A@623e088f
```



빈 후처리기를 사용하면 개발자가 등록하는 모든 빈을 조작할 수 있어서, 빈 객체를 프록시로 교체하는 것도 가능하다. 



[참고] PostConstruct

@PostConstruct는 스프링 빈 생성 이후에 빈을 초기화하는 것이다. 이것은 단순히 CommonAnnotationBeanPostProcessor라는 빈 후처리기를 자동으로 스프링이 등록해서, @PostConstruct가 붙은 메서드를 호출해서 조작한 것 



### 빈 후처리기 - 적용

빈 후처리기를 사용해서 실제 객체 대신 프록시를 스프링 빈으로 등록해보자. 이렇게하면 수동으로 등록하는 빈은 물론이고 컴포넌트 스캔을 사용하는 빈까지 모두 프록시를 적용할 수 있다.



```java
@Slf4j
public class PackageLogTracePostProcessor implements BeanPostProcessor {
        private final String basePackage;
        private final Advisor advisor;

    public PackageLogTracePostProcessor(String basePackage, Advisor advisor) {
        this.basePackage = basePackage;
        this.advisor = advisor;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        log.info("param beanName={}, bean={}", beanName, bean.getClass());

        // 프록시 적용 대상 여부 체크
        // 프록시 적용 대상이 아니면 원본을 그대로 진행
        String packageName = bean.getClass().getPackageName();
        if (!packageName.startsWith(basePackage)) {
            return bean;
        }

        // 프록시 대상이면 프록시를 만들어서 반환
        ProxyFactory proxyFactory = new ProxyFactory(bean);
        proxyFactory.addAdvisor(advisor);
        Object proxy = proxyFactory.getProxy();
        log.info("create proxy: target={}, proxy={}", bean.getClass(), proxy.getClass());
        return proxy;
    }
}
```



PackageLogTracePostProcessor

- 원본 객체를 프록시 객체로 변환하는 역할
- 이때 사용할 프록시 팩토리는 advisor가 필요하기 때문에 이 부분은 외부에서 주입받는다.



이제 프록시를 등록하는 코드가 설정파일에 필요없게된다.

대상 여부 체크하는것도 basePacakge를 사용할 수도 있고, Pointcut으로 할수도있음.



### 정리

문제 1.  너무 많은 설정

- ProxyFactoryConfigV1,V2와 같은 설정 파일은 프록시 관련 설정이 지나치게 많다는 문제가 있다.

문제 2. 컴포넌트 스캔

- 컴포넌트 스캔으로 사용하면 지금까지 방법으로는 프록시 적용이 불가능했다.

문제 해결. 빈 후처리기

- 프록시 생성 부분을 하나로 집중할 수 있고, 프록시관련 코드는 전혀 변경하지 않아도 되고, 컴포넌트 스캔을 사용해도 프록시가 모두 적용된다.



**중요**

- 포인트컷을 사용해서 프록시 적용 대상 여부를 설정하는것이 더 깔끔할 것 같다.
- 포인트컿은 
  - 프록시 적용 대상 여부를 체크해서 필요한 곳에만 프록시를 적용한다
  - 프록시의 어떤 메소드가 호출되었을 때 어드바이스를 적용할지 판단한다.



### 스프링이 제공하는 빈 후처리기 1

```groovy

	implementation 'org.springframework.boot:spring-boot-starter-aop'
```

추가

- aspectjweaver라는 aspectJ관련 라이브러리를 등록하고 스프링 부트가 AOP관련 클래스를 자동으로 스프링 빈에 등록한다.



#### 자동 프록시 생성기 - AutoProxyCreator 

- 스프링이 제공하는 빈 후처리기
- `AnnotationAwareAspectJAutoProxyCreator`



![스크린샷 2023-11-23 오후 11.48.18](/Users/jskang/Library/Application Support/typora-user-images/스크린샷 2023-11-23 오후 11.48.18.png)

3. 모든 Advisor 빈 조회 
   1. 자동 프록시 생성기 - 빈 후처리기는 스프링 컨테이너에서 모든 advisor를 조회한다.
4. 프록시 적용 대상 체크
   1. 앞서 조회한 Advisor에 포함되어있는 포인트컷을 사용해서 해당 객체가 프록시를 적용할 대상인지 아닌지 판단한다. 
5. 프록시 적용 대상이면 프록시를 생성하고 반환해서 프록시를 스프링 빈으로 등록한다.



```java
@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class AutoProxyConfig {

    @Bean
    public Advisor advisor1(LogTrace logTrace) {
        // Pointcunt
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");

        LogTraceAdvice advice = new LogTraceAdvice(logTrace);

        return new DefaultPointcutAdvisor(pointcut, advice);
    }
}

```

그냥 Advisor를 빈으로 등록하기만 하면 끝남



**중요: 포인트컷은 2가지에 사용된다.**

1. **프록시 적용 여부 판단 - 생성 단계**

- 자동 프록시 생성기는 포인틑컷을 사용해서 해당 빈이 프록시를 생성할 필요가 있는지 없는지 체크한다.
- 클래스+메서드 조건 모두 비교, 하나라도 있으면 프록시 생성
- 조건에 맞는것이 하나도 없으면 프록시를 생성하지 않음

2. **어드바이스 적용 여부 판단 - 사용 단계**

- 프록시가 호출되었을 때 부가 기능인 어드바이스를 적용할지 말지 판단

**프록시를 모든 곳에 생성하는 것은 비용 낭비**

- 꼭 필요한곳에 최소한의 프록시를 적용해야한다.
- 포인트컷으로 한번 필터링해서 어드바이스가 사용될 가능성이 있는 곳에만 프록시를 생성한다.



### 스프링이 제공하는 빈 후처리기 2

- 어플리케이션 처음 실행할 때 어드바이저가 걸림
- `"request*", "order*", "save*"` 이런거 들어있는곳 모두 적용되기 때문.
- 따라서 패키지 + 메서드 이름까지 함께 지정할 수 있는 정밀한 포인트컷이 필요



**AspectJExpressionPointcut**

- AOP에 특화된 포인트컷을 표현식에 적용할 수 있다.
- 실무에서는 이것만 쓴다.



```java
    @Bean
    public Advisor advisor2(LogTrace logTrace) {
        // pointcut
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* hello.proxy.app..*(..))");
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);

        return new DefaultPointcutAdvisor(pointcut, advice);
    }
```

- `*` 모든 반환 타입

- `hello.proxy.app..` 해당 패키지와 하위 패키지

  `*(..)` : `*` 모든 메서드 이름, `(..)` 파라미터는 상관 없음



근데 no-log가 찍혀버리는 문제가 있음.

단순히 패키지기준으로만 해버려서. && !등을 써서 더 보완했음

```java
    @Bean
    public Advisor advisor3(LogTrace logTrace) {
        // pointcut
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* hello.proxy.app..*(..)) && !execution(* hello.proxy.app ..noLog(..))");
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);

        return new DefaultPointcutAdvisor(pointcut, advice);
    }
```

