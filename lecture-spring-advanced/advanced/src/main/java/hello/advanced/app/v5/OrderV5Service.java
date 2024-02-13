package hello.advanced.app.v5;

import hello.advanced.trace.callback.TraceCallback;
import hello.advanced.trace.callback.TraceTemplate;
import hello.advanced.trace.logtrace.LogTrace;
import hello.advanced.trace.template.AbstractTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class OrderV5Service {
    private final OrderV5Repository orderRepository;
    private final TraceTemplate template;

    public OrderV5Service(OrderV5Repository orderRepository, LogTrace trace) {
        this.orderRepository = orderRepository;
        this.template = new TraceTemplate(trace);
    }

    public void orderItem(String itemId) {
        template.execute("OrderService.orderItem()",
                (TraceCallback<Void>) () -> {
                    orderRepository.save(itemId);
                    return null;
                }
        );
    }
}
