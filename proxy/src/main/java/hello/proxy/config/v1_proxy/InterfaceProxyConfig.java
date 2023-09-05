package hello.proxy.config.v1_proxy;

import hello.proxy.app.v1.*;
import hello.proxy.config.v1_proxy.interface_proxy.OrderControllerInterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderRepositoryInterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderServiceInterfaceProxy;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InterfaceProxyConfig {
    @Bean
    public OrderV1Controller orderController(LogTrace logTrace) {
        OrderV1ControllerImpl controllerImpl = new OrderV1ControllerImpl(orderService(logTrace));
        return new OrderControllerInterfaceProxy(controllerImpl, logTrace);
    }

    @Bean
    public OrderV1Service orderService(LogTrace logTrace) {
        OrderV1ServiceImpl serviceImpl = new OrderV1ServiceImpl(orderRepository(logTrace));
        return new OrderServiceInterfaceProxy(serviceImpl, logTrace);
    }

    @Bean
    public OrderV1Repository orderRepository(LogTrace logTrace) {
        OrderV1RepositoryImpl repositoryImpl = new OrderV1RepositoryImpl();
        return new OrderRepositoryInterfaceProxy(repositoryImpl, logTrace);
    }
}
