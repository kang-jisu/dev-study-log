package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class AspectV2 {

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
}
