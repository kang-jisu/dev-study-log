package hello.proxy.config.v1_proxy.concrete_proxy;

import hello.proxy.app.v2.OrderV2Repository;
import hello.proxy.app.v2.OrderV2Service;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;

public class OrderServiceConcreteProxy extends OrderV2Service {
    private final OrderV2Service target;
    private final LogTrace logTrace;

    public OrderServiceConcreteProxy(OrderV2Service target, LogTrace logTrace) {
        super(null); // 첫번째 줄에는 들어가야함 . 근데 프록시라서 안들어가도됨 -> 이게 프록시 기반의 단점
        // 이게 없으면 자동으로 super()을 호출하는데 OrderV2Service는 기본생성자가 없음. 오류가 날 것
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public void orderItem(String itemId) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("OrderService.orderItem()");
            // target 호출
            target.orderItem(itemId);
            logTrace.end(status);
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
