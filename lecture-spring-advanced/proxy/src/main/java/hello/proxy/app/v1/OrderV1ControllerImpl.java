package hello.proxy.app.v1;

public class OrderV1ControllerImpl implements OrderV1Controller {
    private final OrderV1Service orderService;

    public OrderV1ControllerImpl(OrderV1Service orderService) {
        this.orderService = orderService;
    }

    @Override
    public String request(String itemId) {
        orderService.orderItem(itemId);
        return "ok";
    }

    @Override
    public String noLog() {
        return "ok";
    }
}
