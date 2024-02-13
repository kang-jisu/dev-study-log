package hello.proxy.app.v2;

import hello.proxy.app.v1.OrderV1Repository;
import hello.proxy.app.v1.OrderV1Service;

public class OrderV2Service {
    private final OrderV2Repository orderRepository;

    public OrderV2Service(OrderV2Repository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void orderItem(String itemId) {
        orderRepository.save(itemId);
    }
}
