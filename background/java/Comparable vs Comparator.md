# Comparable vs Comparator

## Comparable<T> 인터페이스
Comparable 인터페이스는 객체를 정렬하는데 사용되는 메소드인 `compareTo()`를 정의하고 있다.  
자바에서 같은 타입의 인스턴스를 서로 비교해야하는 클래스들은 **모두 Comparable 인터페이스를 구현하고 있다.**  
기본 정렬 순서는 작은값에서 큰 값으로 정렬되는 **오름차순**이다.
<br/>


## Comparator<T> 인터페이스
Comparator인터페이스는 객체를 정렬하는데 사용되는 인터페이스로, 기본적으로 오름차순으로 정렬되는 Comparable과 달리   
**내림 차순이나 다른 기준으로 정렬하고 싶을 때 **사용할 수 있다.   
`compare()`메소드를 재정의하여 사용하게 된다.  


-> 오름차순이 아닐때 Comparator를 사용. 이 규칙을 지키면서 개발해보자 !    
이걸 기억하면 return값에 대해서도 이해가 된다.

- a<b 일때, return 값이 음수일 때 : 유지
- a=b 일때, return 값이 0일때 : 유지  
- **a>b 일때, return 값이 양수일때 : 자리 변경**

<br/>

compareTo(T o), compare(T o1, T o2) 둘다 this 또는 o1 이 a, 뒤에오는 o, o2가 b라고 생각하고 내림차순으로 정렬하고 싶으면
```java

@Override
public int compare(T o1, T o2){
    if( o1<o2) return 1;
    else return -1;
}

// 같은 의미
@Override
public int compare(T o1, T o2){
    return o2 - o1; // 양수이면 자리 변경, o1.o2순서에서 o2.o1순서로 가게 하는것
}

```