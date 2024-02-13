package hello.advanced.app.v5;

import hello.advanced.trace.callback.TraceCallback;
import hello.advanced.trace.callback.TraceTemplate;
import hello.advanced.trace.logtrace.LogTrace;
import hello.advanced.trace.template.AbstractTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderV5Controller {

    private final OrderV5Service orderService;
    private final TraceTemplate template;

    public OrderV5Controller(OrderV5Service orderService, LogTrace trace) {
        this.orderService = orderService;
        this.template = new TraceTemplate(trace); // 처음부터 스프링 빈으로 만들어도 됨
    }

    @GetMapping("/v5/request")
    public String request(String itemId) {
        return template.execute("OrderController.request()",
                new TraceCallback<>() {
                    @Override
                    public String call() {
                        orderService.orderItem(itemId);
                        return "ok";
                    }
                }
        );
    }
}
