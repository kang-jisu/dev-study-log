package hello.advanced.v0;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderV0Service {
    private final OrderV0Repository orderRepository;

    public void orderItem(String itemId) {
        orderRepository.save(itemId);
    }
}
