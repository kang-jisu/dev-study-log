```
스프링 핵심원리 강의 내용을 숙지한상태로 고민해봐야할 문제긴함 
```

**기본 개념**

- 스프링 빈의 기본 Scope는 싱글톤
- 스프링 환경은 멀티쓰레드
- 스프링은 기본적으로 Thread-safe하지 않으므로 스프링빈(@Controller, @Service, @Repository, @Component)의 전역변수에 스프링 빈이 아닌 VO, DTO, Map같은 가변 객체가 존재하면 안된다. 만약 상태값을 가지게 만든다면 Thread-safe하지 않을것이고, synchronized나 concurrent 처리를 해주어야한다. 



![img](https://blog.kakaocdn.net/dn/bHXtFr/btq7ivlyRjX/sxP8aQCKvGTBR0Gk7nc7zk/img.png)  

Spring Framework는 기본적으로 multi thread 방식을 사용하며 Singleton pattern을 이용해 모든 Bean을 공유한다.   

Thread pool에서 Thread를 생성해 요청이 들어오면 미리 생성된 쓰레드를 할당하는 방식이고, heap, code, data영역은 공유된다.  



그래서 코드를 짤때도 다음과같은 방식이 권장된다.

- final, 생성자 주입 사용
- builder패턴 이용
- setter허용X



### 쓰레드마다 새로운 빈을 생성하고 싶을때는 ?

Prototype, Request등의 빈 스코프 생성 -> 핵심원리 강의 들으면서 다시 개념잡기 

```java
@Scope('') // 로 지정
```



---

Thread safe하게 싱글톤 생성하는 방법

1. 클래스 초기화시 인스턴스 생성 

```java
class Singleton {
  private static Singleton instance = new Singleton();
  
  public static Singleton getInstance() {
    return instance;
  }
  private Singleton() {}
}
```

- 미사용 인스턴스도 생성되어 불필요한 시스템 리소스를 낭비한다는 단점이 존재



2. 정적 팩토리 메서드에서 인스턴스 생성 + 메소드 동기화 -> 성능 저하

```java
class Singleton {
  private static Singleton instance = null;
  public synchronized static Singleton getInstance() {
    if( instance == null) {
      instance = new Singleton();
    }
    return instance;
	}
  
  private Singleton();
}
```



3. double checked locking 

```java
public class Singleton {
  private static Singleton instance;
  private Singleton(){}
  
  public static Singleton getInstance(){
    if(instance == null) {
      synchronized (Singleton.class) {
        if(instance == null) {
          instance = new Singleton();
        }
      }
    }
  }
}
```





4. LazyHoder -> 추천 (Bill Pugh 고안 방식 )

```java
class Singleton {
  private static Singleton getInstance() {
    return LazyHolder.INSTANCE; // 필요할때만 생성하도록 하면서도 생성과 동시에 초기화 
  } 
  private static class LazyHolder { 
    private static final Singleton INSTANCE = new Singleton();
  }
  private Singleton() {} //기본생성 못하게 막음
}
```



- 출처
  - https://readystory.tistory.com/116
  - https://kkambi.tistory.com/161