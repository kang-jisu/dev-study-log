## @Aspect AOP



### @Aspect 프록시 - 적용

스프링 애플리케이션에 프록시를 적용하려면 포인트컷과 어드바이스로 구성되어있는 어드바이저를 만들어서 스프링 빈으로 등록하면 된다.



스프링은 @Aspect 애노테이션으로 포인트컷과 어드바이스로 구성되어있는 어드바이저 생성 기능을 지원한다.



`@Aspect` 는 관점 지향 프로그래밍을 가능하게 하는 AspectJ에서 제공하는 애노테이션이고, 스프링은 이것을 사용해서 프록시를 통한 AOP를 가능하게 한다.



```java
@Slf4j
@Aspect
public class LogTraceAspect {

    private final LogTrace logTrace;

    public LogTraceAspect(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    @Around("execution(* hello.proxy.app..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceStatus status = null;
        try {
            String message =  joinPoint.getSignature().toShortString();
            status = logTrace.begin(message);

            // 로직 호출
            Object result = joinPoint.proceed();

            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}

```

- @Aspect : 애노테이션 기반 프로시 적용할 때 필요
- @Around()
  - @Around의 값에 포인트컷 표현식을 넣는다.
  - @Around의 메서드는 어드바이스가 된다.
- `ProceedingJoinPoint joinPoint` 어드바이스에서 살펴본 `MethodInvation invocation` 과 유사한 기능이다. 내부에 실제 호출 대상, 전달 인자, 그리고 어떤 객체와 어떤 메서드가 호출되었는지 정보가 포함되어있다.
- `JointPoint.proceed()` 실제 호출 대상(target)을 호출한다.



AopConfig.class에 LogTraceAspect 등록



### @Aspect 프록시 동작 방식 설명

자동 프록시 생성기(`AnnotationAwareAspectJAutoProxyCreator`) 는 어드바이저를 자동으로 찾아와서 필요한 곳에 프록시를 생성하고 적용해준다.

그리고 `@Aspect` 를 찾아서 이것을 `Advisor` 로 만들어준다. 



어드바이저 = 포인트컷 + 어드바이스

![스크린샷 2023-12-07 오후 11.50.09](스크린샷%202023-12-07%20오후%2011.50.09.png)

- `@Aspect` 를 보고 어드바이저로 변환해서 저장
- 어드바이저를 기반으로 프록시를 생성



1. `@Aspect` 를 어드바이저로 변환해서 저장하는 과정

1. 어플리케이션이 로딩시점에 자동 프록시 생성기 호출, 실행
2. 스프링 빈에서 `@Aspect` 어노테이션이 붙은 스프링 빈을 모두 조회
3. 어드바이저 빌더를 통해 @Aspect 어노테이션을 기반으로 어드바이저 생성
4. 생성한 어드바이저를 Aspect 어드바이저 빌더 내부에 저장



`@Aspect` 어드바이저 빌더

`BeanFactoryAspectAdvisorBuilder` 클래스. `@Aspect`의 정보를 기반으로 포인트컷, 어드바이스, 어드바이저를 생성하고 보관하는 것응ㄹ 담당

@Aspect 정보 기반으로 어드바이저를 만들고, 빌더 내부 저장소에 캐시



2. 어드바이저를 기반으로 프록시 생성

- 1. 생성 : 스프링 빈 대상이 되는 객체를 생성
- 2. 전달생성된 객체를 빈 저장소에 등록하기 전에 빈 후처리기에 전달
- 3. Advisor 빈 조회 : 스프링 컨테이너에서 Advisor 빈을 모두 조회
- 3. @Aspect Advisor 조회 : @Aspect 어드바이저 빌더 내부에 저장된 어드바이저를 모두 조회
- 4. 프록시 적용 대상 체크 
- 5. 프록시 생성



### 정리

`@Aspect` 를 사용한 애노테이션 기반 프록시 

이것은 횡단 관심사(cross-custting concerns)



