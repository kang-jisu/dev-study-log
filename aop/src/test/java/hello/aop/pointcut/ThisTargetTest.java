package hello.aop.pointcut;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(ThisTargetTest.ThisTargetAspect.class)
@SpringBootTest(properties = "spring.aop.proxy-target-class=true")
public class ThisTargetTest {

    @Autowired
    MemberService memberService;

    @Test
    void success() {
        log.info("memberService Proxy={}", memberService.getClass());
        memberService.hello("helloA");
    }

    @Slf4j
    @Aspect
    static class ThisTargetAspect {

        // 부모 타입 허용
        @Around("this(hello.aop.member.MemberService)")
        public Object doThisInterface(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[this-interface]{}", joinPoint.getSignature());
            return joinPoint.proceed();
        }


        // 부모 타입 허용
        @Around("target(hello.aop.member.MemberService)")
        public Object doTargetInterface(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[target-interface]{}", joinPoint.getSignature());
            return joinPoint.proceed();
        }


        // 부모 타입 허용
        @Around("this(hello.aop.member.MemberServiceImpl)")
        public Object doThis(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[this-impl]{}", joinPoint.getSignature());
            return joinPoint.proceed();
        }



        // 부모 타입 허용
        @Around("target(hello.aop.member.MemberServiceImpl)")
        public Object doTarget(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[target-impl]{}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        /** CGLIB
         * memberService Proxy=class hello.aop.member.MemberServiceImpl$$SpringCGLIB$$0
         * [target-impl]String hello.aop.member.MemberServiceImpl.hello(String)
         * [target-interface]String hello.aop.member.MemberServiceImpl.hello(String)
         * [this-impl]String hello.aop.member.MemberServiceImpl.hello(String)
         * [this-interface]String hello.aop.member.MemberServiceImpl.hello(String)
         */

        /***
         * JDK
         * memberService Proxy=class jdk.proxy2.$Proxy55
         * [target-impl]String hello.aop.member.MemberService.hello(String)
         * [target-interface]String hello.aop.member.MemberService.hello(String)
         * [this-interface]String hello.aop.member.MemberService.hello(String)
         */
    }
}
