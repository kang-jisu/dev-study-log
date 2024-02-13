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



#### 대안 2.  지연 조회

- applicationContext.getBean으로 지연 호출
- 근데 애플리케이션 컨텍스트는 기능이 너무 많음 
- ObjectProvider.getObject



#### 대안3 - 구조 변경

- 이게 가장 많이 쓰고, 올바른 방법일 것 같음



AOP는 주로 인터페이스에 메서드가 나올 정도 규모에서 적용하는 것이 적당하다. AOP는 public 메서드에만 적용한다. private 메서드같은 작은 단위에는 적용하지 않는다. 

AOP가 잘 적용되지 않는다면 내부 호출을 의심해보자.



