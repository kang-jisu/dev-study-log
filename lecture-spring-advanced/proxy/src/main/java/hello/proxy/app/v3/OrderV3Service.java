package hello.proxy.app.v3;

import hello.proxy.app.v2.OrderV2Repository;
import org.springframework.stereotype.Service;

@Service
public class OrderV3Service {
    private final OrderV3Repository orderRepository;

    public OrderV3Service(OrderV3Repository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void orderItem(String itemId) {
        orderRepository.save(itemId);
    }
}
