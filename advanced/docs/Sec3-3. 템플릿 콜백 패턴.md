## 템플릿 콜백 패턴

다른 코드의 인수로서 넘겨주는 실행 가능한 코드를 **콜백(callback)** 이라 한다.

> 콜백을 넘겨받는 코드는 이 콜백을 필요에 따라 즉시 실행할 수도 있고, 아니면 나중에 실행할 수도 있다.



쉽게 이야기 해서 `callback` 은 코드가 호출(call) 되는데 코드를 넘겨준 곳 뒤(back)에서 실행된다는 뜻이다.

- `ContextV2` 예제에서 콜백은 `Strategy`
  - 클라이언트에서 직접 `Strategy` 를 실행하는 것이 아니라, 클라이언트가 `ContextV2.execute()` 를 실행할 때 `Strategy` 를 넘겨주고, `ContextV2` 뒤에서 `Strategy` 가 실행된다.



**자바 언어에서 콜백**

- 자바에서는 실행 가능한 코드를 인수로 넘기려면 객체가 필요하다.
- 자바 8부터는 람다를 사용할 수 있다.
- 자바 8 이전에는 하나의 메소드를 가진 인터페이스, 익명 내부 클래스를 사용했다.



### **템플릿 콜백 패턴**

- `ContextV2` 와 같은 방식의 전략패턴을 스프링에서는 템플릿 콜백 패턴이라고 한다. 
- 스프링 내부에서 자주 사용하는 패턴



- 스프링에서 `JdbcTemplate`, `RestTemplate`, `TransactionTemplate` 등 다양한 템플릿 콜백 패턴이 사용된다. 스프링에서 이름에 `XxxTemplate` 이 있다면 템플릿 콜백 패턴으로 만들어져있다고 생각하면 된다.



- `Context`  -> `Template`
- `Strategy` -> `Callback`



### 예제

```java
    @Test
    void callbackV1() {
        TimeLogTemplate template = new TimeLogTemplate();
        template.execute(new Callback() {
            @Override
            public void call() {
                log.info("비즈니스 로직1 실행");
            }
        });
        template.execute(new Callback() {
            @Override
            public void call() {
                log.info("비즈니스 로직2 실행");
            }
        });
    }
    @Test
    void callbackV2() {
        TimeLogTemplate template = new TimeLogTemplate();
        template.execute(() -> log.info("비즈니스 로직1 실행"));
        template.execute(() -> log.info("비즈니스 로직2 실행"));
    }
```

