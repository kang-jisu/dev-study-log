# 컬렉션 클래스
컬렉션 클래스들 각각의 특징과 다른 컬렉션 클래스들과의 차이점을 위주로 정리해본다.

## List 컬렉션 클래스

**특징**
- 요소의 저장 순서가 유지된다.
- 같은 요소의 중복 저장을 허용한다.

**종류**
- ArrayList<E>
- LinkedList<E>
- Vector<E>
- Stack<E>

(Stack은 List(Vector)를 상속받은 클래스이지만 Queue는 개별적으로 존재한다.)



### ArrayList 클래스
- 배열을 이용하므로 인덱스를 이용해 배열 요소에 빠르게 `O(1)` 접근할 수 있다.
- 크기를 늘리기 위해서는 새로운 배열을 생성하고 기존 배열을 옮겨야한다.
  - 기존 배열의 크기를 넘게 추가하면 1.5배씩 증가시킨다.
  - 사용할 초기 용량을 알고 있다면 설정하는것이 좋다. (default=10)

### LinkedList 클래스
- ArrayList, 즉 배열의 단점(크기 변경, 삭제/삽입)을 극복하기 위해 고안되었다. 
- 내부적으로 연결리스트(linked list)를 이용하여 요소를 저장한다.
- 삽입과 삭제가 빠르다. `O(1)`

- 단일 연결 리스트
  - 이전 요소로의 접근이 어려움
- 이중 연결 리스트
  - prev, next를 가지고 이전과 다음 요소에 쉽게 접근할 수 있음

### Vector 클래스
JDK 1.0 부터 사용해온 클래스이다.
기존 코드와의 호환성을 위해 남아있기 때문에 Vector보다는 ArrayList를 사용하는것이 좋다.


### Stack 클래스
Vector를 상속받아 전형적인 스택 메모리 구조의 클래스를 제공한다.
선형 메모리 공간에 LIFO 방식을 따르는 자료구조이다.

#### Stack의 주의점
- Vector를 상속받았기 때문에 다중 상속을 지원하지 않는다.
- 초기용량 설정이 불가능해 삽입이 많아지면 배열 복사가 많이 일어난다.
- ArrayDeque를 대신 사용한다면 모든 스택작업을 Lock(Thread동기화처리) 없이 사용한다.
  - 다중 스레드에서 문제가 발생하지만 ArrayDeque에 대한 동기화 처리는 따로 할 수 있다.
  - 멀티스레드 환경에서 단일 스레드 : LinkedBlockingDeque, 멀티스레드 : ConcurrentLinkedDeque
  (참고만)Vector를 상속받았기 때문에 스택임에도 불구하고 인덱스를 통한 접근이 가능하다


- 출처
  - [TCP SCHOOL](http://www.tcpschool.com/java/java_collectionFramework_list)
  - [tecoble](https://tecoble.techcourse.co.kr/post/2021-05-10-stack-vs-deque/)


<br/>

> 생각해볼 점

### ArrayList vs LinkedList
- 공통점
  - 중복 허용, 순서대로 저장
- 차이점
  - ArrayList 
    - 용량이 꽉찼을 경우 더 큰 용량의 배열을 만들어 옮겨야함
    - 삽입,삭제시 요소들의 재배치가 일어난다. 단 순차적인 데이터 삽입,삭제는 오래걸리지 않는다.
  - LinkedList
    - 용량의 제한이 없음
    - 중간 데이터의 삽입/삭제가 빠르다. 
    - n번째 데이터를 찾기위해서는 차례대로 읽어야한다. 

### Vector vs ArrayList
- Vector
  - 자바 컬렉션 프레임워크가 나오기 전부터 있었던 클래스
  - Thread-safe -> 동기화로 인해 성능이 좋지 않다.
  - 초기 용량 초과시 2배로 늘린다.
- ArrayList
  - Vector의 단점을 보완하기 위해 나온 클래스이다.
  - Thread-safe 하지 않음
  - 초기 용량 초과시 1.5배로 늘린다.

### Stack의 주의점
Vector로 구현되었다. 따라서 Vector의 단점인 동기화로 인한 성능 문제가 있다. -> Collection Framework의 다른 자료구조 클래스(ArrayDeque)를 사용할 수 있다. 

<br/>

---


## Queue 인터페이스
클래스로 구현된 스택과는 달리 자바에서 큐 메모리구조는 별도의 인터페이스 형태로 제공된다.

하위 인터페이스는 다음과 같다.

- Deque<E>
- BlockingDeque<E>
- BlockingQueue<E>
- TransferQueue<E>


Queue인터페이스를 직간접적으로 구현한 클래스는 상당히 많고, 그 중에서도 Deque인터페이스를 구현한 **LinkedList 클래스**가 큐 메모리 구조를 구현하는데 가장 많이 사용된다.

큐 메모리 구조는 선형 메모리 공간에 데이터를 저장하면서 FIFD 방식을 따르는 자료구조이다.   
더 복잡하고 빠른 큐를 구현하고 싶다면 Deque인터페이스를 ArrayDeque클래스로 구현하면 된다.

> Java SE 6부터 지원되는 ArrayDeque 클래스는 스택과 큐 메모리 구조를 모두 구현하는데 가장 적합한 클래스입니다.
>   - thread-safe하지 않다.
>   - 용량제한이 없고 초과되면 2배만큼 증가시킨다.


> 생각해볼 점

### LinkedList vs ArrayDeque
Queue를 구현할 때 LinkedList보다 ArrayDeque로 구현하는게 더 빠르다고 Java API Document에서 설명하고있다.

- 이유
  - ArrayDeque는 Array에 의해 지원되는데, Array는 LinkedList보다 cache-locality에 더 친숙하다. (수행속도)
  - ArrayDeque는 다음 노드에 대한 추가 참조를 유지할 필요가 없다. (메모리)


- 출처
  - [TCP SCHOOL](http://www.tcpschool.com/java/java_collectionFramework_stackQueue)
  - blogspot [LinkedList vs ArrayDeque](http://javaqueue2010.blogspot.com/)