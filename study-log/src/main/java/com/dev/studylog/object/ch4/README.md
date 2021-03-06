# 설계 품질과 트레이드 오프

영화 예매 시스템을 책임이 아닌 상태로 표현하는 데이터 중심의 설계를 살펴보고 객체지향적으로 설계한 구조와 어떤 차이점이 있는지 살펴본다. 

## 데이터 중심의 영화 예매 시스템
객체지향 설계에서는 '상태'를 분할의 중심축으로 삼는 방법과 '책임'을 분할의 중심축으로 삼는 방법이 있다.    

데이터(=상태) 중심의 관점  
- 객체는 자신이 포함하고 있는 데이터를 조작하는 데 필요한 오퍼레이션을 정의한다.
- 객체의 상태에 초점을 맞춘다.
- 객체를 독립된 데이터 덩어리로 본다.   
> 객체의 상태는 구현에 속하며 구현에 관한 세부사항이 객체의 인터페이스에 스며들어 캡슐화의 원칙이 무너진다.   
  

책임 중심의 관점
- 개체는 다른 객체가 요청할 수 있는 오퍼레이션을 위해 필요한 상태를 보관한다.
- 객체의 행동에 초점을 맞춘다.
- 객체를 협력하는 공동체의 일원으로 본다.   
> 객체의 책임은 인터페이스에 속한다. 책임을 드러내는 안정적인 인터페이스 뒤로 책임을 수행하는데 필요한 상태를 캡슐화하여 안정적인 설계를 얻을 수 있다.



결론부터 말하면 시스템을 분할하기 위해서는 데이터와 책임 중 **책임에 초점을 맞춰야한다.**  

## 설계 트레이드 오프

### 캡슐화
상태와 행동을 하나의 객체 안에 모으는 이유는 객체의 내부 구현을 외부로부터 감추기 위해서다.   
객체를 사용하면 변경 가능성이 높은 부분은 내부에 숨기고 외부에는 상대적으로 안정적인 부분만 공개함으로써 변경의 여파를 통제할 수 있다.   
> 변경될 가능성이 높은 부분을 **구현** 이라고 부르고 상대적으로 안정적인 부분을 **인터페이스** 라고 부른다. 

객체를 설계하기 위한 가장 기본적인 아이디어는 변경의 정도에 따라 구현과 인터페이스를 분리하고 외부에서는 인터페이스에만 의존하도록 관계를 조절하는 것이다.   
-> 즉 캡슐화란 변경 가능성이 높은 부분을 객체 내부로 숨기는 추상화 기법이다.  


### 응집도와 결합도 
- 응집도 
  - 모듈에 포함된 내부 요소들이 연관돼 있는 정도
- 결합도
  - 의존성의 정도를 나타내며 다른 모듈에 대해 얼마나 많은 지식을 갖고 있는지를 나타내는 척도이다. 

## 데이터 중심의 영화 예매 시스템의 문제점
기능적인 측면에서 보면 책임 중심(2장), 데이터중심(4장) 이 동일하지만 데이터 중심의 설계는 캡슐화를 위반하고 객체의 내부 구현을 인터페이스의 일부로 만들고 있다.  

### 캡슐화 위반

```java
public class Movie {
    private Money fee;
    public Money getFee() {
        return fee;
    }
    public void setFee(Money fee) {
        this.fee = fee;
    }
}
```
다음 코드는 객체 내부 상태에 대한 어떤 정보도 캡슐화하지 않는다. 오히려 getFee, setFee는 Movie내부에 Money타입의 fee라는 이름의 인스턴스 변수가 존재한다는 사실을 퍼블릭 인터페이스에 노골적으로 드러낸다.   

-> 이는 Movie가 객체가 수행할 책임이 아니라 저장할 데이터에 초첨을 맞췄기 때문이다. 설계할 때 협력에 관해 고민하지 않으면 캡슐화를 위반하는 과도한 접근자와 수정자를 가지게 되는 경향이 있다.  

### 높은 결합도
객체 내부 구현이 인터페이스에 드러난다는 것은 클라이언트가 구현에 강하게 결합됨을 의미한다.
단지 객체의 내부 구현을 변경했음에도 이 인터페이스에 의존하는 모든 클라이언트들도 함께 변경해야한다.  

현재 `ReservationAgency`가 모든 데이터 객체에 의존한다는 것을 알 수 있다. DiscountConditon이 변경되면 ReservationAgency도 함께 수정해야한다. Screening이 변경되면 ReservationAgency도 함께 수정해야한다.   

### 낮은 응집도
서로 다른 이유로 변경되는 코드가 하나의 모듈 안에 공존할 때 모듈의 응집도가 낮다고 말한다.   
현재 ReservationAgency는
 - 할인 정책 추가
 - 할인 정책별로 요금 계산 방법 변경
 - 할인 조건 추가
 - 할인 조건별로 할인 여부 판단 방법 변경
 - 예매 요금 계산 방법 변경  

이 경우에 모두 코드가 수정되어야 한다. 

> **단일 책임 원칙**
> 모듈의 응집도가 변경과 연관이 있다는 사실을 강조하기 위해 다음 원칙을 제시했다.   
> *클래스는 단 한가지 변경 이유만 가져야 한다*  
> 단일 책임 원칙에서 '책임' 은 '변경의 이유' 라는 의미로 사용된다. 

## 자율적인 객체를 향해

### 캡슐화를 지켜라
> 객체가 자기 스스로 책임을 지도록 책임을 이동시킨다.

```java
class Rectangle{
    private int left;
    private int top;
    private int right;
    private int bottom;
    
    //생성자
    //getter
    //setter
}
```
```java
class Main {
    void anyMethod(Rectangle rectangle, int multiple) {
        rectangle.setRight(rectangle.getRight()*multiple);
        rectangle.setBottm(rectangle.getBottom()*multiple);
    }
}
```
유사한 코드가 중복될 수 있고, 사각형 표시를 right, bottom이 아닌 다른 값으로 하게되면 이 함수도 변경해주어야 한다.   

```java
class Rectangle{
    public void enlarge(int multiple) {
        right *= multiple;
        bottom *= multiple;
    }
}
```
이렇게 Rectangle을 변경하는 주체를 Rectangle로 이동시켜 캡슐화를 강화시키면 해결할 수 있다.   

### 스스로 자신의 데이터를 책임지는 객체

다시 수정 ! 
결합도 측면에서 ReservationAgency의 의존성이 몰려있던 것이 개선되긴 했다. 

### 하지만 여전히 부족하다
```java
public class DiscountCondition {
  public boolean isDiscountable(DayOfWeek dayOfWeek, LocalTime time) {
    if (type != DiscountConditionType.PERIOD) {
      throw new IllegalArgumentException();
    }
    return this.dayOfWeek.equals(dayOfWeek) &&
            this.startTime.compareTo(time) <= 0 &&
            this.endTime.compareTo(time) >= 0;
  }
}
```
현재 DiscountCondition 객체 내부에 dayOfWeek, time가 포함되어있다는 것을 인터페이스를 통해 외부에 노출하고 있다.  
또한 setType은 없지만 getType메서드를 통해 type을 포함하고 있음을 노출시키고 있다.  
DiscountCondition을 수정하게 되면 isDiscountable의 파라미터를 수정하고 해당 메서드를 사용하는 클라이언트도 수정해야할 것이다.   

movie 또한 내부에 amount, percent, none 정책이 있다는 것을 드러내고 있다.  

### 높은 결합도
```java
public class Movie{
  public boolean isDiscountable(LocalDateTime whenScreend, int sequence) {
    for (DiscountCondition condition : discountConditions) {
      if (condition.getType() == DiscountConditionType.PERIOD) {
        if (condition.isDiscountable(whenScreend.getDayOfWeek(), whenScreend.toLocalTime())) {
          return true;
        }
      } else {
        if (condition.isDiscountable(sequence)) {
          return true;
        }
      }
    }
    return false;
  }

}
```
- dicsountCondition 조건 명칭이 변경된다면 movie를 수정해야한다.
- 추가나 삭제된다면 movie의 if else문이 변경되어야한다. 
- discountCondition 만족여부를 판단하는데 필요한 정보가 변경된다면 파라미터를 변경해야한다.  

### 낮은 응집도
screening 
### 데이터 중심 설계는 객체를 고립시킨 채 오퍼레이션을 정의하도록 만든다. 

데이터 중심 설계는 행동보다 데이터를 먼저 결정하고, 협력이라는 문맥을 벗어나 고립된 객체의 상태에 초점을 맞추기 때문에 캡슐화를 위반하기 쉽고, 요소들 사이에 결합도가 높아지며, 
코드를 변경하기 어려워진다.   

-> 5장에서는 해결방법을 알아본다. 