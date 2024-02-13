package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class AspectV3 {

    // hello.aop.order 패키지와 하위 패키지
    @Pointcut("execution(* hello.aop.order..*(..))")
    private void allOrder(){} // pointcut signature

    // 클래스 이름 패턴이 *Service인거
    @Pointcut("execution(* *..*Service.*(..))")
    private void allService(){}

    // dolog -> 어드바이스
    @Around("allOrder()") // -> 괄호 안의 값은 포인트컷
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature()); // join point 시그니처
        return joinPoint.proceed();
    }

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

}
