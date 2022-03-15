# 메세지와 인터페이스

객체지향프로그래밍에 대한 가장 흔한 오해

> 애플리케이션이 클래스의 집합으로 구성된다는 것 

클래스는 그러나 도구일 뿐이다. 클래스라는 구현 도구에 지나치게 집착하면 경직되고 유연하지 못한 설계에 이를 확률이 높아진다.   

훌륭한 객체지향 코드를 얻기 위해서는 **클래스가 아니라 객체를 지향해야한다.** 

협력 안에서 객체가 수행하는 책임에 초점을 맞춰야한다. 여기서 중요한 것은 책임이 객체가 수신할 수 있는 메시지의 기반이 된다는 것이다. 

> 객체지향 애플리케이션에서 가장 중요한 재료는 클래스가 아니라 객체들이 주고받는 메시지다. 
>
> 애플리케이션은 클래스로 구성되지만 **메시지로 정의된다는 사실을 기억하라**  

객체가 수신하는 메시지들이 객체의 퍼블릭 인터페이스를 구성하며 유연하고 재사용 가능한 퍼블릭 인터페이스를 만드는 데 도움이 되는 설계 원칙과 기법을 익히고 적용해야한다.  



## 협력과 메시지

### 클라이언트-서버 모델

협력은 어떤 객체가 다른 객체에게 무언가를 요청할 때 시작된다. 메시지는 객체사이의 협력을 가능하게 하는 매개체다. 

두 객체 사이의 협력 관계를 설명하기 위한 전통적인 메타포는 **클라이언트-서버 모델**이다. 협력 안에서 메시지를 전송하는 객체를 클라이언트, 메시지를 수신하는 객체를 서버라고 부르며 **협력은 클라이언트가 서버의 서비스를 요청하는 단방향 상호작용이다.**    

- 객체는 협력에 참여하는 동안 클라이언트와 서버의 역할을 동시에 수행  
- 객체가 수신하는 메시지의 집합에만 초점을 맞추는것이 아닌 외부에 전송하는 메시지의 집합도 함께 고려하는 것이 협력에 적합한 객체를 설계하기에 바람직하다.   

> 객체가 독립적으로 수행할 수 있는 것보다 더 큰 책임을 수행하기 위해서는 다른 객체와 협력해야한다.  
>
> 그 두 객체 사이의 협력을 가능하게 해주는 매개체가 **메시지**



## 메시지와 메시지 전송

- **메시지**
  - 객체들이 협려하기 위해 사용할 수 있는 유일한 의사소통 수단
  - 메시지 전송(패싱) : 한 객체가 다른 객체에게 도움을 요청하는 것
    - 메시지 전송자 : 이때 메시지를 전송하는 객체
    - 메시지 수신자 : 이때 메시지를 수신하는 객체 
    - 클라이언트 - 서버 모델에서는 전송자:클라이언트, 수신자:서버
  - 구성
    - 오퍼레이션명(operation name)
    - 인자(argument)
- 메시지 전송 *메시지에 메시지 수신자를 추가하면 메시지 전송*
  - 오퍼레이션명, 인자
  - 메시지 수신자
- `condition.isSatisfiedBy(screening)` =`수신자.오퍼레이션명(인자)`

### 메시지와 메서드

메시지를 수신했을 때 실제로 어떤 코드가 실행되는지는 메시지 수신자의 실제 타입에 달려있다. (인터페이스의 실체화한 클래스, 구현체에 따라)  

- **메서드**
  - 메시지를 수신했을 때 실제로 실행되는 함수 또는 프로시저 
  - 코드 상에서 동일한 이름의 변수에게 동일한 메시지를 전송하더라도 객체에 타입에 따라 실행되는 메시지가 달라질 수 있다. 
  - 객<u>체는 메시지와 메서드라는 두 가지 서로 다른 개념을 `실행 시점` 에 연결해야 하기 때문에 컴파일 시점과 실행 시점의 의미가 달라질 수 있다.</u>
- 메시지와 메서드의 구분은 메시지 전송자와 메시지 수신자가 느슨하게 결합될 수 있게 함. 
  - 전송자는 자신이 어떤 메시지를 전송해야하는지만 알면 된다.
  - 메시지 수신자 역시 누가 메시지를 전송하는지 상관없이 메시지가 도착했다는 사실만 알면 된다. 메시지 수신자는 메시지를 처리하기 위해 필요한 메시지를 스스로 결정할 수 있다. 

> 실행시점 바인딩 메커니즘은 두 객체 사이의 결합도를 낮춤으로써 유연하고 확장 가능한 코드를 작성할 수 있게 만든다.   

### 퍼블릭 인터페이스와 오퍼레이션

외부의 객체는 오직 객체가 공개하는 메시지를 통해서만 객체와 상호작용 할 수 있다. 이처럼 객체가 의사소통을 위해 외부에 공개하는 메시지의 집합을 **퍼블릭 인터페이스**라 부른다.  

- 오퍼레이션
  - 퍼블릭 인터페이스에 포함된 메시지
  - 수행가능한 어떤 행동에 대한 **추상화** 
  - 메시지와 관련된 시그니처 
  - `isSatisfiedBy`
  - 실행하기 위해 객체가 호출될 수 있는 변환이나 정의에 관한 명세 
- 메서드
  - 메시지를 수신했을 때 실제로 수행되는 코드 , 실제 **구현**
  - `SequenceCndition.isSatisfiedBy`와 `PeriodCondition.isSatisfiedBy`
  - 두 메서드는 Discondition 인터페이스에 정의된 isSatisfied오퍼레이션의 여러 가능한 구현중 하나.    

### 시그니처

오퍼레이션 또는 메서드의 이름과 파라미터 목록을 합쳐 시그니처라고 부른다.    

다형성을 위해서는 오퍼레이션에 대해 다양한 메서드를 구현해야 한다. 오퍼레이션의 관점에서 다형성이란 동일한 오퍼레이션 호출에 대해 서로 다른 메서드들이 실행되는 것. 



### 용어 정리

- 메시지 
  - 객체가 다른 객체와 협력하기 위해 사용하는 의사소통 메커니즘
- 메시지 전송
  - 객체의 오퍼레이션이 실행되도록 요청하는것
  - 협력에 참여하는 전송자와 수신자 양쪽 모두를 포함하는 개념
- 오퍼레이션
  - 객체가 다른 객체에게 제공하는 추상적인 서비스
  - 메시지가 전송자와 수신자 사이의 협력관계를 강조하는데 비해 오퍼레이션은 메시지를 수신하는 객체의 인터페이스를 강조함 
  - 메시지 전송자는 고려하지 않은 채 메시지 수신자의 관점만을 다룸 
- 메시지 수신
  - 메시지에 대응되는 개체의 오퍼레이션을 호출하는 것
- 메서드
  - 메시지에 응답하기위해 실행되는 코드 블록
  - 오퍼레이션의 구현
  - 동일한 오퍼레이션이라고 해도 메서드는 다를 수 있다  (다형성)
- 퍼블릭 인터페이스
  - 객체가 협력에 참여하기 위해 외부에서 수신할 수 있는 메시지의 묶음
  - 클래스의 퍼블릭 메서드들의 집합이나 메시지의 집합을 가리키는데 사용됨 
  - **객체 설계시 가장 중요한 것은 훌륭한 퍼블릭 인터페이스를 설계하는 것**
- 시그니처
  - 오퍼레이션이나 메서드의 명세를 나타낸 것
  - 이름과 인자의 목록 (일부는 반환타입도 포함)

> 객체가 수신할 수 있는 메시지가 객체의 퍼블릭 인터페이스와 그 안에 포함될 오퍼레이션을 결정한다는 것   

<br/>

## 인터페이스와 설계 품질

> 좋은 인터페이스는 **최소한의 인터페이스**와 **추상적인 인터페이스**
>
> 최소한의 인터페이스 : 꼭 필요한 오퍼레이션만을 인터페이스에 포함
>
> 추상적인 인터페이스 : '어떻게'가 아니라 '무엇을'수행하는지 표현

**책임 주도 설계**방법을 따르면 메시지를 먼저 선택함으로써 협력과는 무관한 오퍼레이션이 인터페이스에 스며드는것을 방지한다.  또한 **메시지가 객체를 선택**하게 함으로써 클라이언트의 의도를 메시지에 표현할 수 있게 한다.     

다음은 퍼블릭 인터페이스의 품질에 영향을 미치는 원칙과 기법에 관한 내용이다.  

### 디미터 법칙

> 객체의 내부 구조에 강하게 결합되지 않도록 협력 경로를 제한하라

자바같이 `.`로 메시지 전송을 표현하는 언어에서는 `오직 하나의 도트만 사용해라` 라는 말로 요약되기도 한다.   

디미터 법칙을 따르기 위해서는 클래스가 특정한 조건을 만족하는 대상에게만 메시지를 전송하도록 프로그래밍해야한다.

모든 클래스 C와 C에 구현된 모든 메서드 M에 대해서 , M이 메시지를 전송할 수 있는 모든 객체는 다음에 서술된 클래스의 인스턴스여야한다. 이때 M에 의해 생성된 객체나 M이 호출하는 메서드에 의해 생성된 객체, 전역변수로 선언된 객체는 모두 M의 인자로 간주된다. 

- M의 인자로 전달된 클래스 (C자체를 포함)
- C의 인스턴스 변수의 클래스 
- 즉
  - this 객체
  - 메서드의 매개변수
  - this의 속성
  - this의 속성인 컬렉션의 요소
  - 메서드 내에 생성된 지역 객체

> 디미터 법칙은 캡슐화를 다른 관점에서 표현한것. 디미터 법칙이 가치있는 이유는 클래스를 캡슐화하기 위해 따라야하는 구체적인 지침을 제공하기 때문이다. 캡슐화원칙이 클래스 내부의 구현을 감춰야한다는 사실을 강조한다면 디미터 법칙은 협력하는 클래스의 캡슐화를 지키기 위해 접근해야하는 요소를 제한한다.  

`screening.getMovie().getDiscountConditions()`

- 기차충돌
- 클래스 내부구현이 외부로 노출
- `screening.calculateFee(audienceCount)` 
  - 메시지 전송자는 수신자의 내부구조에 관해 묻지 않고, 자신이 원하는 것을 명시하고 단순히 수행하도록 요청한다. 

### 묻지 말고 시켜라

객체의 상태에 관해 묻지말고 원하는 것을 시켜라. 메시지 전송자는 메시지 수신자의 상태를 기반으로 결정을 내린 후 메시지 수신자의 상태를 바꿔서는 안된다. 지금 구현하고 있는 로직은 메신지 수신자가 담당해야할 책임이다.   

### 의도를 드러내는 인터페이스

```java
public class PeriodCondition{
  public boolean isSatisfiedByPeriod(){..}
}
public class SequenceCondition{
  public boolean isSatisfiedBySequence(){...}
}
```

다음과 같은 스타일은 좋지 않다. 

- 두 메서드 모두 할인 조건을 판단하는 동일한 작업을 수행하는데 이름이 다르기 때문에 내부 구현을 정확하게 이해하지 못한다면 두 메서드가 동일한 작업을 수행한다는 사실을 알아채기 어렵다. 
- 캡슐화 위반. 협력하는 객체의 종류를 알도록 강요함 

두 메서드는 할인 여부를 판단하기 위한 작업을 수행한다. 두 메서드 모두 클라이언트의 의도를 담을 수 있도록 `isSatisfiedBy`로 변경하는 것이 적절할 것이다.   

자바같은 정적 타이핑 언어는 메서드 이름이 같다고 동일한 메시지라고 처리할 수 없기 때문에, 클라이언트가 두 메서드를 가진 객체를 동일한 타입 계층으로 묶어야한다. 

```java
public interface DiscountCondition {
  boolean isSatisfiedBy(Screeing screening);
}
public class PeriodCondition implements DiscountCondition {
  public boolean isSatisfiedBy(Screening screening){..}
}
```

> 메서드가 어떻게 수행하느냐가 아니라 무엇을 하느냐에 초점을 맞추면 클라이언트의 관점에서 동일한 작업을 수행하는 메서드들을 하나의 타입 계층으로 묶을 수 있는 가능성이 커진다. 
>
> => **의도를 드러내는 선택자** -> 의도를 드러내는 인터페이스

### 함께 모으기 

디미터, 묻지말고 시켜라, 의도를 드러내는 인터페이스 이런 원칙을 위반하는 코드를 보자. 

**디미터 법칙을 위반하는 티켓 판매 도메인**

```java
public class Theater {
  private TicketSeller ticketSeller;
  public Theater(TicketSeller ticketSeller) {
    this.ticketSeller = ticketSeller;
  }
  public void enter(Audience audience) {
    if(audience.getBag().hasInvitation()){
      Ticket ticket = ticketSeller.getTicketOffice().getTicket();
      audience.getBag().setTicket(ticket);
    } else {
      Ticket ticket = ticketSeller.getTicketOffice().getTicket();
      audience.getBag().minusAmount(ticket.getFee());
      ticketSeller.getTicketOffice().plusAmount(ticket.getFee());
      audience.getBag().setTicket(ticket);
    }
  }
}
```

`audience.getBag().minusAmount(ticket.getFee());`

> 근본적으로 디미터 법칙을 위반하는 설계는 인터페이스와 구현의 분리 원칙을 위반한다. 

Audience가 Bag을 포함한다는 사실은 Audience의 내부구현에 속하는데, 퍼블릭 인터페이스에 getBag을 포함시키는 순간 객체의 구현이 외부로 새어나가버린다.   

```java
Ticket ticket = ticketSeller.getTicketOffice().getTicket();
audience.getBag().minusAmount(ticket.getFee());
```

Theater는 반환된 TicketOffice가 getTicket메시지를 수신할 수 있으며 이 메서드가 반환하는 Ticket 인스턴스가 getFee 메시지를 이해할 수 있다는 사실도 알아야한다.   

> Audience와 TicketSeller의 내부구조를 묻는 대신 Audience와 TicketSeller가 직접 자신의 책임을 수행하도록 시키기

**묻지말고 시켜라**

```java
public class TicketSeller{
  public void setTicket(Audience audience) {
if(audience.getBag().hasInvitation()){
      Ticket ticket = ticketOffice().getTicket();
      audience.getBag().setTicket(ticket);
    } else {
      Ticket ticket = ticketOffice().getTicket();
      audience.getBag().minusAmount(ticket.getFee());
  		ticketOffice.plusAmount(ticket.getFee());
      audience.getBag().setTicket(ticket);
  }
}
  
public class Theater {
  public void enter(Audience audience) {
    ticketSeller.setTicket(Audience);
  }
}
```

enter메서드의 로직을 setTicket으로 옮겨 Theater가 TicketSeller에게 Audience가 Ticket을 가지도록 시킨다. 

```java
public class Audience{
  public Long setTicket(Ticket ticket ){
    if(bag().hasInvitation()){
      bag().setTicket(ticket);
      return 0L;
    } else {
      bag().minusAmount(ticket.getFee());
  		bag().setTicket(ticket);
      return ticket.getFee();
  }
  }
}
public class TicketSeller {
  public void setTicket(Audience audience){
    ticketOffice.plusAmount(audience.setTicket(ticketOffice.getTicket));
  }
}
```

Audience가 hasInvation 메서드를 이용해 초대권을 가지고 있는지 묻고있는 것도 위반. 

```java
public class Bag {
  public Long setTikcet(Ticket ticket) {
    if(hasInvitation()){
      this.ticket = ticket;
      return 0;
    }else {
     this.ticket = ticket;
      minusAmount(ticket.getFee());
      return ticket.getFee();
    }
  }
  private boolean hasInvitation(){
    return invitation !=null;
  }
  private void minusAmount(Long amount){
    this.amount-=amount;
  }
}
public class Audienece{
  public Long setTicket(Ticket ticket){
    return bag.setTicket(ticket);
  }
}
```

**인터페이스의 의도를 드러내자** 

setTicket들은 클라이언트의 의도를 명확하게 드러내지 못한다.   

- Theater가 TicketSeller에게 setTicket을 통해 얻고싶었던 것은? 
  - sellTo
- TicketSeller -> Audience
  - buy
- Audienece -> Bag
  - hold



## 원칙의 함정

디미터와 묻지말고 시켜라 스타일이 절대적인 법칙은 아니다. 

### 디미터 법칙이 하나의 도트를 강제하는 규칙이 아니다.

```java
IntStream.of(1,15,20,3,9).filter(x->x>10).distinct().count();
```

이 메서드는 모두 IntStream이라는 동일한 인스턴스를 반환한다. 따라서 이 코드는 디미터 법칙을 위반하지 않는다.   

기차충돌처럼 보이더라도 객체의 내부 구현에 대한 어떤 정보도 외부로 노출하지 않는다면 그것은 디미터 법칙을 준수한것이다.   

### 결합도와 응집도의 충돌

어떤 객체의 상태를 물어본 후 반환된 상태를 기반으로 결정을 내리고 그 결정에 따라 객체의 상태를 변경하는 코드는 **묻지 말고 시켜라** 스타일로 변경해야한다. 

하지만 맹목적으로 위임 메서드를 추가하다가 서로 상관없는 책임이 함께 뭉쳐있는 클래스는 응집도가 낮으며 작은 변경으로도 쉽게 무너질 수 있다. 

```java
public class PeriodCondition implements DiscountCondition {
  public boolean isSatisfiedBy(Screening screening){
    return screening.getStartTime().getDayOfWeek().equals(dayOfWeek) && 
      statTime.compareTo(screening.getStartTime().toLocalTime()) <= 0 &&
      endTime.compareTo(screening.getStartTime().toLocalTime()) =>0;
  }
}
```

```java
public class Screening {
    public boolean isDiscountable(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime){
    return screening.getStartTime().getDayOfWeek().equals(dayOfWeek) && 
      statTime.compareTo(screening.getStartTime().toLocalTime()) <= 0 &&
      endTime.compareTo(screening.getStartTime().toLocalTime()) =>0;
  }
}
public class PeriodCondition implements DiscountCondition {
  public boolean isSatisfiedBy(Screening screening){
    return sceening.isDiscountable(dayOfWeek,startTime, endTime);
  }
}
```

캡슐화를 위반한거 처럼 보여서 할인 여부 판단 로직을 isDiscountable로 옮기고 이를 호출하도록 변경한다면 ?

- Screening이 기간에 따른 할인 조건을 판단하는 책임을 떠안게되는데, 이것은 Screening이 담당해야하는 본질적인 책임이 아니다. Screening의 본질적인 책임은 예매하는 것이다
- 또한 PeriodCondition의 변수를 인자를 받기 때문에 인스턴스 변수 목록이 벼녕되면 영향을 받는다. 두 클래스 사이의 결합도를 높인다. 
- 캡슐화를 향상시키는 것 보다 결합도를 낮추고 응집도를 높이는 것이 더 좋은 방법이다.  

> 경우에 따라 다르다는 사실을 명심하라



<br/>

## 명령-쿼리 분리 원칙

- 루틴
  - 어떤 절차를 묶어 호출 가능하도록 이름을 부여한 기능 모듈
  - 프로시저 (= 명령)
    - 정해진 절차에 따라 내부의 **상태를 변경**하는 루틴의 한 종류
    - 부수효과를 발생시킬 수 있지만 **값을 반환할 수 없다.** 
  - 함수 (= 쿼리)
    - 어떤 절차에 따라 필요한 값을 계산해서 반환해주는 루틴 
    - **값을 반환할 수 있지만 부수효과를 발생시킬 수 없다.** 

> 질문이 답변을 수정해서는 안된다. 

명령 버튼을 누르면 기계의 상태가 변경되지만 실행결과를 제공하지 않기 때문에 쿼리버튼을 이용해 상태를 확인해야한다. 

### 반복 일정의 명령과 쿼리 분리하기

- 이벤트
  - 특정 일자에 실제로 발생하는 사건
  - "회의"
- 반복 일정
  - 특정 시간 간격에 발생하는 사건을 포괄적으로 지칭하는 용어
  - 매주 수요일 10시 30분부터 11시까지 회의 반복

코드 생략

조건에 만족하는지 검사하는 메소드(쿼리)에서 조건을 변경하는 구현(명령)를 포함해버려서 이해하기 어려웠고 잘못 사용하기 쉬우며 버그를 양산하게 되었다.  

reschedule을 isSatisfied 밖으로 뺀다.   

### 명령 쿼리 분리와 참조 투명성

- 쿼리는 객체의 상태를 변경하지 않기 때문에  반복 호출을 해도 된다.
- 명령이 개입하지 않는 한 쿼리의 결과를 예측하기 쉬워진다.
- 쿼리들의 순서를 자유롭게 변경할 수 있다.

> 불변성, 부수효과, 참조투명성 

참조투명성을 만족한다면

- 모든 함수를 이미 알고있는 하나의 결괏값으로 대체할 수 있기 때문에 식을 쉽게 계산할 수 있다.
- 모든 곳에서 함수의 결괏값이 동일하기 때문에 식의 순서를 변경하더라도 각 식의 결과는 달라지지 않는다. 

객체지향 패러다임이 객체의 상태변경이라는 부수효과를 기반으로 하기 때문에 참조 투명성이 예외에 가깝지만, 명령-쿼리 분리 원칙을 사용하면 균열을 그나마 줄일 수 있다. 

> **명령형 프로그래밍**
>
> 부수효과를 기반으로 하는 프로그래밍 방식 
>
> 상태 변경 연산을 적절한 순서대로 나열함

> **함수형 프로그래밍**
>
> 부수효과가 존재하지 않는 수학적인 함수에 기반
>
> 참조 투명성의 장점을 극대화 , 프로그램의 실행 결과 예측이 쉬움

### 책임에 초점을 맞춰라

