package hello.proxy.config.v2_dynamicproxy;

import hello.proxy.app.v1.*;
import hello.proxy.config.v2_dynamicproxy.handler.LogTraceBasicHandler;
import hello.proxy.config.v2_dynamicproxy.handler.LogTraceFilterHandler;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;

@Configuration
public class DynamicProxyFilterConfig {

    private static final String[] PATTERNS = {"request*", "order*", "save*"};

    @Bean
    public OrderV1Controller orderV1Controller(LogTrace logTrace) {
        OrderV1Controller orderController = new OrderV1ControllerImpl(orderV1Service(logTrace));
        OrderV1Controller proxy = (OrderV1Controller) Proxy.newProxyInstance(OrderV1Controller.class.getClassLoader(),
                new Class[]{OrderV1Controller.class},
                new LogTraceFilterHandler(orderController, logTrace, PATTERNS));
        return proxy;
    }

    @Bean
    public OrderV1Service orderV1Service(LogTrace logTrace) {
        OrderV1ServiceImpl orderService = new OrderV1ServiceImpl(orderV1Repository(logTrace));
        OrderV1Service proxy = (OrderV1Service) Proxy.newProxyInstance(OrderV1Service.class.getClassLoader(),
                new Class[]{OrderV1Service.class},
                new LogTraceFilterHandler(orderService, logTrace, PATTERNS));
        return proxy;
    }

    @Bean
    public OrderV1Repository orderV1Repository(LogTrace logTrace) {
        OrderV1Repository orderRepository = new OrderV1RepositoryImpl();
        OrderV1Repository proxy = (OrderV1Repository) Proxy.newProxyInstance(OrderV1Repository.class.getClassLoader(),
                new Class[]{OrderV1Repository.class},
                new LogTraceFilterHandler(orderRepository, logTrace, PATTERNS));

        return proxy;
    }
}
