## Set 컬렉션 클래스

**특징**
- 요소의 저장 순서 유지하지 않음
- 같은 요소의 중복 저장 허용하지 않음
- thread-safe 하지않다.

**종류**
- HashSet<E>
- TreeSet<E>

### HashSet 클래스
- **해시 알고리즘 사용**하여 검색속도가 매우 빠르다.
- 내부적으로 HashMap인스턴스를 이용하여 요소를 저장한다.
  - `hashCode()`를 호출해 반환된 해시값으로 검색할 범위를 정하고, 해당 범위 내 요소들을 `equals()`로 비교
  - 따라서 상황에 맞게 hashCode와 equals를 오버라이딩 해야한다.
- null을 허용한다.


### TreeSet 클래스
- 데이터가 정렬된 상태로 저장되는 **이진 검색 트리(Binary Search Tree)**형태로 요소를 저장한다.
- 이진 검색트리는 데이터를 추가하거나 제거하는 등의 기본 동작 시간이 매우 빠르다. `O(logN)`
- 레드 블랙 트리로 구현되어있다.
- null을 허용하지 않는다.


> 생각해볼 점

### HashSet vs TreeSet

- 정렬이 필요하지 않은 경우 HashSet이 빠르다. 
- 정렬이 필요한 경우 TreeSet을 사용할 수 있을 것이다.
- 메모리 소비보다 성능이 중요하다면 HashSet, 메모리가 부족하면 TreeSet
- 개체가 입력된 순서를 유지하고 엑세스 이점을 얻으려면 LinkedHashSet



- 출처
  - [TCPSCHOOL](http://www.tcpschool.com/java/java_collectionFramework_set)
  - BAELDUNG [HashSet vs TreeSet](https://www.baeldung.com/java-hashset-vs-treeset)
  - 본문과 같은 내용이지만 읽어볼만한 stackoverflow글 [ Java: Hashset Vs TreeSet - when should I use over the other? [duplicate]](https://stackoverflow.com/questions/25602382/java-hashset-vs-treeset-when-should-i-use-over-the-other)


---
## Map 컬렉션 클래스
Map 인터페이스는 Collcetion 인터페이스와는 다른 저장방식을 가진다.   
Map인터페이스를 구현한 컬렉션 클래스들은 키와 값을 하나의 쌍으로 저장하는 key-value 방식을 사용한다.  

**특징**
- 요소의 저장 순서를 유지하지 않는다.
- 키는 중복을 허용하지 않지만, 값 중복은 허용한다.

**종류**
- HashMap<K,V>
- Hashtable<K,V>
- TreeMap<K,V>
<br/>

### HashMap 클래스
Map 컬렉션 클래스에서 가장 많이 사용되는 클래스이며 해시 알고리즘을 사용하여 검색 속도가 매우 빠르다.  

### Hashtable 클래스
JDK 1.0 부터 사용해온 HashMap과 같은 동작을 하는 클래스이다.
기존 코드와의 호환성을 위해 남아있으므로 HashMap을 사용하는것이 좋다. 
thread-safe하다.(하지만 이걸 사용하기보단 다른 HashMap을 사용하는것이 낫다)

### TreeMap 클래스
데이터를 **이진 검색 트리(Binary Search Tree)** 형태로 저장한다.  
이진 검색트리는 데이터를 추가하거나 제거하는 등의 기본 동작 시간이 빠르다. 레드블랙트리로 구현되어있다.   


- 출처
  - [TCPSCHOOL](http://www.tcpschool.com/java/java_collectionFramework_map)


---

# 컬렉션 클래스의 동기화, 병렬처리 


## Hashtable vs HashMap vs ConcurrentHashMap

- Hashtable
   - put,get같은 주요 메소드에 `synchronized` 키워드가 선언되어있고 key,value에 `null`을 허용하지 않는다.
- HashMap
  - thread-safe하지 않으며 null을 허용한다.
- synchronizedMap
  - 전체 lock -> 동시성에 문제가 생김
- **ConcurrentHashMap**
  - HashMap을 **thread-safe** 하도록 만든 클래스이다. JDK 1.5에 검색과 업데이트시 동시성 성능을 높이기 위해 나왔다.
  - key, value에 null을 허용하지 않는다.
  - 읽기작업에는 여러 쓰레드가 동시에 읽을 수 있지만 쓰기 작업에는 `synchronized`블럭을 이용한다. 메소드 전체에 lock이 걸리지 않는다는 것이 hashtable과 다르다.
  - 또한 **버킷 단위로 lock**을 사용하기 때문에 같은 버킷이 아니라면 lock을 기다릴 필요가 없다.
  - 버킷 사이즈와 동일하게 동시 접근 가능한 쓰레드의 수를 지정했다.
  - 읽기에는 동기화가 적용되지 않아 put,remove와 겹칠 수 있는데 가장 최근에 완료된 업데이트 결과가 반영된다.
  - 또한 배열의 크기를 증가시킬 때 기존 테이블에서 새로운 테이블로 버킷단위로 전송(transfer)하여 여러 쓰레드에서 동시에 새로운 버킷을 채울수 있다.


하단 출처에 남긴 블로그에 나온 예시이다.
10개의 스레드에서 1000번을 반복하여 1~1000 값을 집어넣었을때의 결과이다. Map은 key의 중복을 허용하지 않으므로 1000개만 들어가야 할 것이다.

```java
Hashtable size is 1000
HashMap size is 1281
ConcurrentHashMap size is 1000
HashMap(synchronized) size is 1000
synchronizedMap size is 1000
```

HashMap은 동기화가 이루어지지 않아(중복값인지 구분을 못해서) 엔트리 사이즈가 비정상적으로 나왔다.  
동기화 이슈가 있을 땐 HashMap을 쓰지 않거나 동기화를 보장하는 HashMap콜렉션 , synchronized 키워드를 이용하는 것이 좋다.  

- 출처
  - jdm.kr [Hashtable  vs HashMap vs ConcurrentMap](http://jdm.kr/blog/197)
  - [ConcurrentHashMap이란?](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Java/Collection/Concurrent/ConcurrentHashMap%EC%9D%B4%EB%9E%80%3F.md)
    - Baeldung [Collections.synchronizedMap 대 ConcurrentHashMap](https://www.baeldung.com/java-synchronizedmap-vs-concurrenthashmap)