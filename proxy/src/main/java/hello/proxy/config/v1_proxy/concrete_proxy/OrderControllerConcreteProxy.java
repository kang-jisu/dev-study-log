package hello.proxy.config.v1_proxy.concrete_proxy;

import hello.proxy.app.v2.OrderV2Controller;
import hello.proxy.app.v2.OrderV2Service;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;

public class OrderControllerConcreteProxy extends OrderV2Controller {
    private final OrderV2Controller target;
    private final LogTrace logTrace;

    public OrderControllerConcreteProxy(OrderV2Controller target, LogTrace logTrace) {
        super(null);
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public String request(String itemId) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("OrderController.orderItem()");
            // target 호출
            String result = target.request(itemId);
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

    @Override
    public String noLog() {
        return target.noLog();
    }
}
