package hello.proxy.config;

import hello.proxy.app.v1.*;
import hello.proxy.app.v2.OrderV2Controller;
import hello.proxy.app.v2.OrderV2Repository;
import hello.proxy.app.v2.OrderV2Service;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppV2Config {
    @Bean
    public OrderV2Controller orderV2Controller() {
        return new OrderV2Controller(orderV2Service());
    }

    @Bean
    public OrderV2Service orderV2Service() {
        return new OrderV2Service(orderV2Repository());
    }

    @Bean
    public OrderV2Repository orderV2Repository() {
        return new OrderV2Repository();
    }
}
