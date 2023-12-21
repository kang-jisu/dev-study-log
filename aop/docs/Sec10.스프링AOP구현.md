1. build.gradle에 추가

```
implementation 'org.springframework.boot:spring-boot-starter-aop'
```



빈으로 꼭 등록을 해줘야함. `@Import(:class)`

- @Bean
- @Component
- @Import

```java
[log] void hello.aop.order.OrderService.orderItem(String)
[orderService] 실행
[log] String hello.aop.order.OrderRepository.save(String)
[OrderRepository] 실행
  
isAopProxy, orderService=true
isAopProxy, orderRepository=true
```



### 포인트컷 분리

`@Around` 에 포인트컷 표현식을 직접 넣을 수도 있지만, `@Pointcut` 애노테이션을 사용해서 별도로 분리할 수도 있다.

```java
    // hello.aop.order 패키지와 하위 패키지
    @Pointcut("execution(* hello.aop.order..*(..))")
    private void allOrder(){} // pointcut signature


    // dolog -> 어드바이스
    @Around("allOrder()") // -> 괄호 안의 값은 포인트컷
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature()); // join point 시그니처
        return joinPoint.proceed();
    }
    
    // 이렇게 다른 어드바이스에 같은 포인트컷을 적용할 때 편리하다
    @Around("allOrder()") 
    public Object doLog2(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log2] {}", joinPoint.getSignature()); // join point 시그니처
        return joinPoint.proceed();
    }
```



포인트컷

- 메서드 이름과 파라미터를 합쳐서 포인트컷 시그니처라 한다.
- 메서드의 반환 타입은 void
- 코드 내용은 비워둔다.
- 포인트컷 시그니처를 Around에서 사용할 수 있다.
- 다른 Aspect에서 사용하려면 public 접근제어자를 쓰면 된다.



### 어드바이스 추가

로그를 출력하는 기능에 추가로 트랜잭션을 적용하는 코드 추가

```java
    // hello.aop.order 패키지와 하위패키지 이면서 클래스 이름이 *Service
    @Around("allOrder() && allService()")
    public Object doTranscation(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            log.info("[트랜잭션 시작]{}", joinPoint.getSignature());
            Object result = joinPoint.proceed();
            log.info("[트랜잭션 커밋]{}", joinPoint.getSignature());
            return result;
        } catch (Exception e) {
            log.info("[트랜잭션 롤백]{}", joinPoint.getSignature());
            throw e;
        } finally {
            log.info("[리소스 릴리즈]{}", joinPoint.getSignature());
        }
    }
```

- 포인트 컷은 && || ! 조합 가능



### 포인트컷 참조 (모듈화)

```java
    // dolog -> 어드바이스
    @Around("hello.aop.order.aop.PointCuts.allOrder()") // -> 괄호 안의 값은 포인트컷
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature()); // join point 시그니처
        return joinPoint.proceed();
    }
```

패키지까지 같이 가져오면 된다.



### 어드바이스 순서

어드바이스는 기본적으로 순서를 보장하지 않는다. 순서를 지정하고 싶으면 @Aspect 적용 단위로 @Order 애노테이션을 적용해야한다. 이것은 어드바이스 단위가 아니라 클래스 단위로 적용할 수 있다.

**지금처럼 하나의 애스펙트에 여러 어드바이스가 있으면 순서를 보장받을 수 없어서, 에스팩트를 별도의 클래스로 분리해야한다.**

```java
public class AspectV5Order {

    @Aspect
    @Order(2)
    public static class LogAspect {

        // dolog -> 어드바이스
        @Around("hello.aop.order.aop.PointCuts.allOrder()") // -> 괄호 안의 값은 포인트컷
        public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[log] {}", joinPoint.getSignature()); // join point 시그니처
            return joinPoint.proceed();
        }
    }
    
    @Aspect
    @Order(1)
    public static class TransactionAspect {

        // hello.aop.order 패키지와 하위패키지 이면서 클래스 이름이 *Service
        @Around("hello.aop.order.aop.PointCuts.orderAndService()")
        public Object doTranscation(ProceedingJoinPoint joinPoint) throws Throwable {
            try {
                log.info("[트랜잭션 시작]{}", joinPoint.getSignature());
                Object result = joinPoint.proceed();
                log.info("[트랜잭션 커밋]{}", joinPoint.getSignature());
                return result;
            } catch (Exception e) {
                log.info("[트랜잭션 롤백]{}", joinPoint.getSignature());
                throw e;
            } finally {
                log.info("[리소스 릴리즈]{}", joinPoint.getSignature());
            }
        } 
    }

}
```

개별 클래스나 inner class로 만들어서 순서 지정하고 각각 빈 지정해주면 된다.



```bash
[트랜잭션 시작]void hello.aop.order.OrderService.orderItem(String)
[log] void hello.aop.order.OrderService.orderItem(String)
[orderService] 실행
[log] String hello.aop.order.OrderRepository.save(String)
[OrderRepository] 실행
[트랜잭션 커밋]void hello.aop.order.OrderService.orderItem(String)
[리소스 릴리즈]void hello.aop.order.OrderService.orderItem(String)
```



### 어드바이스의 종류

어드바이스는 Around 외에 여러가지가 있다.

- `@Around` : 메서드 호출 전후에 수행, 가장 강력한 어드바이스, 조인 포인트 실행 여부 선택, 반환 값 변환, 예외 변환 등이 가능
- `@Before` : 조인포인트 실행 이전에 실행
- `@After Returning` : 조인포인트 이후 정상 완료된 후에
- `@After Throwing` : 예외 던져지는 경우에
- `@After` : 정상 또는 예외 상관 없는 finally




