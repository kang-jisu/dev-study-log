## AOP 실무 주의사항



#### 프록시와 내부 호출 - 문제

스프링 - 프록시 방식의 AOP 사용

프록시를 통해서 대상 객체를 호출해야하는데, 객체의 내부 메서드 호출이 발생할 때 프록시를 거치지 않고 대상 객체를 직접 호출하는 문제가 발생한다.



```bash
CallLogAspect     : aop = void hello.aop.internalcall.CallServiceV0.external()
CallServiceV0     : call external
CallServiceV0     : call internal
```



external -> internal 호출할 때 Aspect aop 거치지 않음

![스크린샷 2024-01-18 오후 11.24.14](/Users/jskang/Library/Application Support/typora-user-images/스크린샷 2024-01-18 오후 11.24.14.png)



자기 자신 인스턴스의 내부 메서드를 호출하는 this.internal() 은 this는 대상객체이기 때문에 프록시를 거치지 않는다. 그래서 어드바이스도 적용할 수 없다.



스프링의 프록시 방식의 AOP는 내부호출에 프록시를 적용할 수가 없다. 

AspectJ를 사용하면 이런 경우가 나타나지 않는데, 스프링 AOP를 사용하면 이렇게 된다.



#### 대안 1 - 자기 자신 주입

자기 자신을 의존관계 주입 해버리기

- this가 아닌 자기자신.내부메서드 호출 

