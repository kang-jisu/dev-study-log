## 스프링 AOP 개념



### 핵심 기능과 부가 기능

- 핵심 기능 : 해당 객체가 제공하는 고유의 기능
- 부가 기능 : 핵심 기능을 보조하기 위해 제공되는 기능



#### 여러 곳에서 공통으로 사용하는 부가 기능

보통 부가 기능은 하나의 부가 기능이 여러 클래스에 걸쳐서 함께 사용된다. 



#### 부가 기능 적용 문제

부가 기능을 적용해야 하는 클래스가 100개면 100개 모두에 동일한 코드를 추가해야한다.



- 부가 기능을 적용할 때 아주 많은 반복이 필요하다.
- 부가 기능이 여러 곳에 퍼져서 중복 코드를 만들어낸다.
- 부가 기능을 변경할 때 중복 때문에 많은 수정이 필요하다.
- 부가 기능의 적용 대상을 변경할 때 많은 수정이 필요하다.



## AOP Aspect

#### 핵심 기능과 부가 기능을 분리

부가 기능을 핵심 기능에서 분리하고 한 곳에서 관리하도록 했다.

해당 부가 기능을 어디에 적용할지 선택하는 기능도 만들었다.



애스팩트 - @Aspect 

어드바이저도 동일 - 부가기능(어드바이스)과 해당 부가기능을 어디에 적용할 지(포인트 컷) 정의한 것 



이렇게 애스팩트를 사용한 프로그래밍 방식을 관점 지향 프로그래밍(AOP) 이라고 한다.

AOP는 OOP의 대체가 아니라, OOP의 부족한 부분을 보조하는 목적이다.



**AspectJ 프레임워크**

AOP의 대표적인 구현으로 AspectJ 프레임워크가 있다. 스프링도 AOP를 지원하지만 대부분 AspectJ의 문법을 차용하고, AspectJ가 제공하는 일부만 제공한다.



- 자바 프로그래밍 언어에 대한 완벽한 관점 지향 확장
- 횡단 관심사의 깔끔한 모듈화
  - 오류 검사 및 처리
  - 동기화
  - 성능 최적화(캐싱)
  - 모니터링 및 로깅



 

### AOP 적용 방식

AOP 부가기능 로직을 추가하는 방식

- 컴파일 시점
- 클래스 로딩 시점
- 런타임 시점(프록시)



#### 컴파일 타임

.java 소스 코드 컴파일러를 사용해서 .class를 만드는 시점에 부가 기능 로직을 추가할 수 있다. 

AspectJ 컴파일러가 AspectJ모듈에서 로그 추적 로직과 적용 대상을 가지고 주문 로직에 로그 추적 로직을 추가해서 컴파일한다. orderService.class에는 부가 기능이 들어감

**이렇게 원본 로직에 부가 기능 로직이 추가되는 것을 위빙(Weaving) 이라 한다.**



**컴파일 시점 - 단점**

컴파일 시점에 부가 기능을 적용하려면 특별한 컴파일러도 필요하고 복잡하다.



#### 클래스 로딩 시점

자바를 실행하면 자바 언어는 .class 파일을 JVM 내부의 클래스 로더에 보관한다. 

 JVM에 .class 파일을  저장하기 전에 조작할 수 있는 기능을 제공하는데 (instrumentation). 

이 시점에 애스팩트를 적용하는 것을 로드 타입 위빙이라고 한다.

**로드 타임 - 단점**

특별한 옵션을 통해 (`java -javaagent`) 로더 조작기를 지정해야하는데 이 부분이 번거롭고 운영하기 어렵다.



#### 런타임 시점

스프링과 같은 컨테이너의 도움을 받고 프록시와 DI, 빈 포스트 프로세서 같은 개념들을 총 동원해야한다. 프록시 방식의  AOP이다.

프록시를 사용하기 때문에 AOP 기능에 일부 제약이 있다.

다형성이 적용되는, 오버라이딩이 되는 것에만 가능하다는 제약이 있지만

특별한 컴파일러나 복잡한 옵션과 클래스 로더 조작기를 설정하지 않아도 스프링만 있으면 얼마든지 AOP를 적용할 수 있다.



**정리**

- 컴파일, 클래스 로딩 시점 : 실제 대상 코드에 부가기능 코드 포함
- 런타임 시점 : 실제 대상 코드는 유지되고 프록시를 통해 부가 기능이 적용됨



#### AOP 적용 위치

- 적용 가능 지점(조인 포인트) : 생성자, 필드 값 접근, static 메서드 접근, 메서드 실행
- AspectJ를 사용해서 컴파일 시점과 클래스 로딩 시점에 적용하는 AOP는 바이트코드를 실제 조작하기 때문에 해당 기능을 모든 지점에 다 적용할 수 있다.



- **프록시 방식을 사용하는 스프링 AOP는 메서드 실행 시점에만 AOP를 적용할 수 있다.**
  - 프록시는 메서드 오버라이딩 개념으로 동작하기 때문에 생성자나 static 메서드, 필드 값 접근에는 프록시 개념이 적용될 수 없다.
  - 프록시를 사용하는 **스프링 AOP의 조인 포인트는 메서드 실행으로 제한** 된다. 
  - 프록시 방식을 사용하는 스프링 AOP는 스프링 컨테이너가 관리할 수 있는 **스프링 빈에만 AOP를 적용**할 수 있다.



#### 중요

스프링이 제공하는 AOP는 프록시를 사용한다. 따라서 프록시를 통해 메서드를 실행하는 시점에만 AOP가 적용된다. 

AspectJ를 사용하려면 공부할 내용도 많고, 자바 관ㄹㄴ 설정(컴파일러, AspectJ 전용 문법, 자바 실행 옵션)이 복잡하다. 반면에 스프링 AOP는 별도의 추가 자바 설정 없이 스프링만 있으면 편리하게 사용할 수 있다. 

실무에서는 스프링이 제공하는 AOP 기능만 사용해도 문제를 해결할 수 있다.



### 용어 정리

**조인 포인트**

- 어드바이스가 적용될수 있는 위치
- 생성자 호출, 필드 값 접근, static 메서드 접근 같은 프로그램 실행 중 지점
- 조인 포인트는 추상적인 개념으로, AOP를 적용할 수 있는 모든 지점
- 스프링 AOP 에서는 항상 **메소드 실행지점** 으로 제한된다.

**포인트컷**

- 어드바이스가 적용될 위치를 선별하는 기능
- 주로 AspectJ 표현식을 사용해서 지정
- 프록시를 사용하는 스프링 AOP는 메서드 실행 지점만 포인트컷으로 선별 가능

**타겟**

- 어드바이스를 받는 객체, 포인트 컷으로 결정

**어드바이스**

- 부가 기능 그 자체
- 특정 조인 포인트에서 애스팩트에 의해 취해지는 조치
- Around, Before, After 같은 다양한 종류의 어드바이스

**애스팩트(Aspect)**

- 어드바이스 + 포인트컷을 모듈화 한 것
- @Aspect
- 여러 어드바이스와 포인트 컷이 함께 존재

**위빙(Weaving)**

- 포인트 컷으로 결정한 타겟의 조인 포인트에 어드바이스를 적용하는 것
- 위빙을 통해 핵심 기능 코드에 영향을 주지 않고 부가 기능을 추가할 수 있음
- AOP 적용을 위해 애스팩트를 객체에 연결한 상태
  - 컴파일 타임(AspectJ compiler)
  - 로드 타임
  - 런타임, 스프링 AOP는 런타임, 프록시 방식

**AOP 프록시**

- AOP 기능을 구현하기 위해 만든 프록시 객체, 스프링에서 AOP 프록시는 JDK 동적 프록시 또는 CGLIB 프록시이다.

![스크린샷 2023-12-12 오후 11.17.54](/Users/jskang/Library/Application Support/typora-user-images/스크린샷 2023-12-12 오후 11.17.54.png)





### 정리

