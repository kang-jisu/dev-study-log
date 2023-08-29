package hello.proxy.config;

import hello.proxy.app.v1.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppV1Config {
    @Bean
    public OrderV1Controller orderV1Controller() {
        return new OrderV1ControllerImpl(orderV1Service());
    }

    @Bean
    public OrderV1Service orderV1Service() {
        return new OrderV1ServiceImpl(orderV1Repository());
    }

    @Bean
    public OrderV1Repository orderV1Repository() {
        return new OrderV1RepositoryImpl();
    }
}
