package hello.proxy.config.v1_proxy;

import hello.proxy.app.v2.OrderV2Controller;
import hello.proxy.app.v2.OrderV2Repository;
import hello.proxy.app.v2.OrderV2Service;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderControllerConcreteProxy;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderRepositoryConcreteProxy;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderServiceConcreteProxy;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConcreteProxyConfig {

    @Bean
    public OrderV2Controller orderV2Controller(OrderV2Service orderV2Service, LogTrace logTrace) {
        OrderV2Controller controllerImpl = new OrderV2Controller(orderV2Service(logTrace));
        return new OrderControllerConcreteProxy(controllerImpl, logTrace);
    }

    @Bean
    public OrderV2Service orderV2Service(LogTrace logTrace) {
        OrderV2Service serviceImpl = new OrderV2Service(orderV2Repository(logTrace));
        return new OrderServiceConcreteProxy(serviceImpl, logTrace);
    }
    @Bean
    public OrderV2Repository orderV2Repository(LogTrace logTrace) {
        OrderV2Repository repositoryImpl = new OrderV2Repository();
        return new OrderRepositoryConcreteProxy(repositoryImpl, logTrace);
    }
}
