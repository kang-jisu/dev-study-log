package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Slf4j
@Aspect
public class AspectV6Advice {

//    // dolog -> 어드바이스
//    @Around("hello.aop.order.aop.PointCuts.allOrder()") // -> 괄호 안의 값은 포인트컷
//    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
//        log.info("[log] {}", joinPoint.getSignature()); // join point 시그니처
//        return joinPoint.proceed();
//    }

    // hello.aop.order 패키지와 하위패키지 이면서 클래스 이름이 *Service
//    @Around("hello.aop.order.aop.PointCuts.orderAndService()")
//    public Object doTranscation(ProceedingJoinPoint joinPoint) throws Throwable {
//        try {
//            // Before
//            log.info("[트랜잭션 시작]{}", joinPoint.getSignature());
//            Object result = joinPoint.proceed();
//            // After
//            log.info("[트랜잭션 커밋]{}", joinPoint.getSignature());
//            return result;
//        } catch (Exception e) {
//            // AfterThrowing
//            log.info("[트랜잭션 롤백]{}", joinPoint.getSignature());
//            throw e;
//        } finally {
//            // AfterReturning
//            log.info("[리소스 릴리즈]{}", joinPoint.getSignature());
//        }
//    }

    @Before("hello.aop.order.aop.PointCuts.orderAndService()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("[before] {}", joinPoint.getSignature());
    }

    @AfterReturning(value = "hello.aop.order.aop.PointCuts.orderAndService()", returning = "result")
    public void doReturn(JoinPoint joinPoint, Object result) { // 위에 returning이랑 같은 값
        log.info("[return] {} return={}", joinPoint.getSignature(), result);
    }

    @AfterThrowing(value = "hello.aop.order.aop.PointCuts.orderAndService()", throwing = "ex")
    public void doThrowing(JoinPoint joinPoint, Exception ex) { // 위에 returning이랑 같은 값
        log.info("[ex] {} message=", joinPoint.getSignature(), ex);
    }

    @After(value = "hello.aop.order.aop.PointCuts.orderAndService()")
    public void doAfter(JoinPoint joinPoint) {
    log.info("[after] {}", joinPoint.getSignature());
    }
}
