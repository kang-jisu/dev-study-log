package hello.proxy.jdkdynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
}
