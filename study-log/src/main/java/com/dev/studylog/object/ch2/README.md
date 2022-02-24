# 2. 객체지향 프로그래밍

객체가 어떤 상태에 놓여있는지, 어떤 생각을 하고 있는지 알아서는 안되며 결정에 직접적으로 개입하려고 해서도 안된다. 
개체에게 원하는것을 요청하고는 객체가 스스로 최선의 방법을 결정할 수 있을 것이라는 점을 믿고 기다려야한다.

- 외부에서 접근 가능한 부분 : 퍼블릭 인터페이스
- 내부에서만 접근 가능한 부분 : 구현  
인터페이스와 구현의 분리 원칙은 객체지향프로그래밍의 핵심 원칙이다.  
  객체는 상태를 숨기고 행동만 외부에 공개해야한다. 
  


### Template Method 패턴
  DiscountPolicy는 할인 여부와 요금 계산에 필요한 전체적인 흐름은 정의하지만 실제로 요금을 계산하는 부분은 추상메서드에게 위임한다.  
  실제로는 DiscountPolicy를 상속받은 자식 클래스에서 오버라이딩한 메서드가 실행 될 것이다.   
  이처럼 부모 클래스에 기본적인 알고리즘의 흐름을 구현하고 중간에 필요한 처리를 자식클래스에게 위임하는 디자인 패턴을 **Template method 패턴** 이라고 한다.

```java 
    public Money calculateDiscountAmount(Screening screening) {
        for(DiscountCondition each : conditions) {
            if( each.isSatisfiedBy(screening)) {
                return getDiscountAmount(screening);
            }
        }
        return Money.ZERO;
    }
    abstract protected Money getDiscountAmount(Screening screening);
```

### 생성자를 이용해 초기화에 필요한 정보를 전달하도록 강제
```java
// DiscountPolicy를 하나만 받는다. 
    public Movie(String title, Duration runningTime, Money fee, DiscountPolicy discountPolicy) {
        this.title = title;
        this.runningTime = runningTime;
        this.fee = fee;
        this.discountPolicy = discountPolicy;
    }
```
Movie에서는 DiscountPolicy와 의존성이 존재한다. ( 접근할 수 있는 경로를 가지거나 해당 클래스 객체의 메서드를 호출할 경우)  
하지만 어떤 할인 정책인지를 판단하지 않는다. 오지 추상 클래스인 DiscountPolicy에만 의존 -> 상속과 다형성   

*코드의 의존성과 실행 시점의 의존성이 서로 다를 수 있다. 즉 클래스 사이의 의존성과 객체사이의 의존성은 동일하지 않을 수 있다.*  
설계가 유연해질수록 코드를 이해하고 디버깅하기는 어렵지만, 재사용성과 확장 가능성은 높아진다. 

### 상속, 차이에 의한 프로그래밍
객체지향에서 코드를 재사용하기 위해 가장 널리 사용되는 방법  
클래스 사이에 관계를 설정하는 것 만으로 기존 클래스가 가지고 있는 모든 속성과 행동을 새로운 클래스에 포함시킬 수 있다.   
상속을 이용하면 부모클래스의 **구현**은 공유하면서도 **행동이 다른** 자식 클래스를 쉽게 추가할 수 있다. 

부모 클래스와 다른 부분만을 추가해서 새로운 클래스를 쉽고 빠르게 만드는 방법을 **차이에 의한 프로그래밍** 이라고 한다. 

#### 상속과 인터페이스
상속이 가치있는 이유는 *부모 클래스가 제공하는 모든 인터페이스를 자식 클래스가 물려받을 수 있기 때문이다.*  
-> 외부 객체는 자식 클래스와 부모 클래스를 동일한 타입으로 간주한다. (Movie는 누군지 상관 없이 `DiscountPolicy.calculateDiscountAmount`를 수신할 객체가 있다는 사실만 중요하다. )   

- "업캐스팅" : 자식 클래스가 부모 클래스를 대신하는것

### 다형성
Movie는 동일한 메세지를 전송하지만 실제로 어떤 메서드가 실행될 것인지는 메시지를 수신하는 개체의 클래스가 무엇이냐에 따라 달라진다.  
이를 **다형성** 이라고 부른다.  
<br/>
다형성이란 동일한 메시지를 수신했을 때 객체의 타입에 따라 다르게 응답할 수 있는 능력을 의미한다. 

### 구현 상속과 인터페이스 상속  
 
- 구현 상속(implementation inheritance)
  - 서브클래싱(subclassing)
  - 순수한 코드 재사용 목적으로 상속을 사용
- 인터페이스 상속(interface inheritance) <- 이게 상속의 진짜 목적이다 
  - 서브타이핑(subtyping)
  - 다형적인 협력을 위해 부모클래스와 자식클래스가 인터페이스를 고융할 수 있도록 상속을 이용하는 것   
  
### 추상화와 유연성

추상화를 사용하면 추상화 계층만 떼어 놓고 보면 요구사항의 정책을 높은 수준에서 서술할 수 있다.  
또한 설계가 유연해진다. 기본 구조를 수정하지 않고도 새로운 기능을 추가하고 확장할 수 있다. 

### 추상 클래스와 인터페이스 트레이드 오프 
```java
public class NoneDiscountPolicy extends DiscountPolicy{
    @Override
    protected Money getDiscountAmount(Screening screening) {
        return Money.ZERO;
    }
}
```
NoneDiscountPolicy -> getDiscountAmount() 메서드가 어떤 값을 반환해도 상관이 없다.   
이것은 NoneDiscountPolicy와 DiscountPolicy를 개념적으로 결합시키게 된다.  

-> DiscountPolicy를 추상클래스에서 인터페이스로 바꿈  
- DiscountPolicy -> DefaultDiscountPolicy -> Amount, Percent
- DiscountPolicy -> NoneDiscountPolicy


### 코드 재사용
상속은 객체지향에서 코드를 재사용하기 위해 널리 사용되는 기법이다. 하지만 상속은 캡슐화를 위반하고 설계를 유연하지 못하게 만든다.   

- 캡슐화 위반
  - 부모 클래스의 구조를 자식클래스가 알고있어야 한다. 캡슐화가 약화되고 부모와 자식이 강하게 결합됨. 
- 설계 유연하지 않음
  - 상속은 부모 클래스와 자식 클래스 사이의 관계를 컴파일 시점에 결정한다.런타임에 객체 종류 변경 불가능
  - 인스턴수 변수로 설정하면 런타임에 `changeDiscountPolicy()` 와 같은 방법으로 변경할 수 있음.
  

### 합성
인터페이스에 정의된 메시지를 통해서만 코드를 재사용하는 방법

코드를 재사용하는 경우에는 상속보다 합성, 다형성을 위해 인터페이스를 재사용하는 경우에는 상속과 합성을 조합해서 사용  


**객체지향 설계의 핵심은 적절한 협력을 식별하고 협력에 필요한 역할을 정의한 후에 여할을 수행할 수 있는 적절한 객체에게 적절한 책임을 할당하는것.**

