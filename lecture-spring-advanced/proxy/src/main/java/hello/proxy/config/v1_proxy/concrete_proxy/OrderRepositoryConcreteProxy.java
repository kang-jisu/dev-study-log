package hello.proxy.config.v1_proxy.concrete_proxy;

import hello.proxy.app.v2.OrderV2Repository;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderRepositoryConcreteProxy extends OrderV2Repository {
    private final OrderV2Repository target;
    private final LogTrace trace;

    public OrderRepositoryConcreteProxy(OrderV2Repository target, LogTrace trace) {
        this.target = target;
        this.trace = trace;
    }

    @Override
    public void save(String itemId) {
        TraceStatus status = null;
        try {
            status = trace.begin("OrderRepository.request()");
            // target 호출
            target.save(itemId);
            trace.end(status);
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}
