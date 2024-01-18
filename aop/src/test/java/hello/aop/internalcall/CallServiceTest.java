package hello.aop.internalcall;

import hello.aop.internalcall.aop.CallLogAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(CallLogAspect.class)
@SpringBootTest
public class CallServiceTest {

    @Autowired
    CallServiceV0 callServiceV0; // 프록시

    @Test
    void externalTest() {
        callServiceV0.external();
    }

    @Test
    void internalTest() {
        callServiceV0.internal();
    }
}
