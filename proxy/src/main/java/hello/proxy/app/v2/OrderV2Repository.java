package hello.proxy.app.v2;

import hello.proxy.app.v1.OrderV1Repository;

public class OrderV2Repository {

    public void save(String itemId) {
        // 저장 로직
        if (itemId.equals("ex")) {
            throw new IllegalStateException("예외 발생!");
        }
        sleep(1000);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();;
        }
    }
}