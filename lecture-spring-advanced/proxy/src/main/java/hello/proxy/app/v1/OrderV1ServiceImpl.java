package hello.proxy.app.v1;

public class OrderV1ServiceImpl implements OrderV1Service {
    private final OrderV1Repository orderRepository;

    public OrderV1ServiceImpl(OrderV1Repository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void orderItem(String itemId) {
        orderRepository.save(itemId);
    }
}
