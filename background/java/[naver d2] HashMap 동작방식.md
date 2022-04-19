# Java HashMap 동작 방식

## HashMap과 HashTable
- 정의
  - 키에 대한 해시값을 사용하여 값을 저장하고 조회하며, 키-값 쌍의 개수에 따라 동적으로 크기가 증가하는 Associative array(Map, Dictionary)
- 차이점
  - HashMap
    - Java Collection Framework
    - **thread-safe하지 않음**
    - 보조 해시 함수 사용 -> 충돌 덜 발생
      -  `h = 31 * h + val[i]; (= n<<5 -n)`
    - key에 Null값 허용
  - HashTable
    - JDK 1.0부터 있었던 Java의 API
    - **thread-safe**
    - 보조 해시 함수 사용 X
      - ` index = X.hashCode()%M`
    - key에 Null값 허용하지 않음

<br/>

## 해시 분포와 해시 충돌
동일하지 않은 객체 X와 Y가 있을 때 (즉 X.equals(Y)!=true) 일 때 `X.hashCode()!=Y.hashCode()` 이라면 이 때 사용하는 해시 함수는 **완전한 해시 함수** 라고 한다.   
- String, Pojo는 사실상 완전한 해시 함수를 만드는 것은 불가능하다.  
- HashMap은 기본적으로 각 객체의 hashCode()가 반환하는 값을 사용하며 자료형은 int(32비트) 이다. 생성 가능한 객체수가 2^32보다 많을 경우 O(1)을 보장하지 못한다.   

### 버킷 인덱스 값
associative array(연관배열,=map,dictionary)는 메모리를 절약하기 위해 실제 해시 함수의 표현 정수 범위(N)보다 작은 M개 원소가 있는 배열을 사용한다.   
```java
int index = X.hashCode() % M;
```

따라서 서로 다른 해시코드가 가지는 서로 다른 객체가 1/M 확률로 같은 해시 버킷을 사용하게 된다 (해시 충돌)

### 해시 충돌 후 저장 방식
![이미지](https://d2.naver.com/content/images/2015/06/helloworld-831311-4.png)  

출처 - [Naver D2 Java의 HashMap](https://d2.naver.com/helloworld/831311)


### Open Addressing
데이터를 삽입하려는 해시 버킷이 이미 사용중인 경우 다른 해시 버킷에 해당 데이터를 삽입.

- 장점
  - 연속된 공간에 데이터를 저장하여 캐시 효율이 높다.
  - 데이터개수가 적을때 효과적이다.  
- 단점  
  - 배열 크기가 커질수록 캐시 적중률이 낮아진다.   


데이터 저장/조회할 해시 버킷을 찾는 방법
- Linear Probing
  - 바로 옆 빈칸에 채운다
- Quadratic Probing
  - 다음에 채울 인덱스의 폭이 제곱으로 늘어난다.
- Double Hashing
  - 충돌 후 해싱을 한번 더 한다.

### Seperate Chaining ( Java가 사용하는 방법 )
각 배열의 인자는 인덱스가 같은 해시 버킷을 연결한 링크드리스트의 첫 부분(head)이다.
```text
해시 -> 인덱스 추출 -> 링크드 리스트 -> addEntry
```

HashMap은 remove가 빈번하게 일어나므로 데이터를 삭제할 때 효율적이다.   
**Java8에서는 데이터의 개수가 많아지면(하나의 버킷에 threshold=8) 링크드리스트가 아닌 트리를 사용한다.**  
- 링크드 리스트 O(N/M)
- 트리 O(logN/M)
  - 레드블랙트리 사용

<br/>

## 해시 버킷 동적 확장
해시 버킷 개수의 기본값은 16이고 데이터의 개수가 임계점에 이를때마다 (데이터개수가 버킷 개수 3/4 넘을때마다) 두배로 확장한다.   
**단점: 버킷 개수가 증가할 때 마다 seperate chaining 재배치가 일어남**

- HashMap 객체에 저장될 데이터의 개수가 어느정도인지 예측 가능한 경우에는 생성자의 인자로 미리 버킷 size를 지정하여 재 구성을 방지한다. (적정한 해시 버킷 개수 지정 중요)
- 해시 버킷 개수를 두배로 확장하게되면 해시 버킷의 개수 M이 2의 제곱형태가 되어 `index = X.hashCode()%M`을 계산할 때 X.hashCode()의 하위 a개의 비트만 사용하게 된다.
- **이 때문에 보조 해시 함수가 필요하다.**

<br/>


## 보조 해시 함수
`index=X.hashCode() % M` 을 계산할 때 M이 소수여야 index값의 분포가 가장 균등한데 M이 소수가 아니기 때문에 별도의 보조함수를 사용하여 index를 균등하게 생성한다.   
보조 해시 함수의 목적은 키의 해시값을 변형하여 해시 충돌 가능성을 줄이는 것이다.
 - Java8 HashMap의 보조 해시 함수는 상위 16비트를 XOR연산하는 매우 단순한 형태인데, 이는 Java 8 부터 링크드 리스트 대신 트리를 사용하며 해시 충돌시 발생하는 성능 문제를 완화켰기 때문이다.

### String객체에서 해시함수
String객체에 대한 해시 함수는 문자열 길이에 비례한다.
```java
public int hashCode() {  
        int h = hash;
        if (h == 0 && value.length > 0) {
            char val[] = value;

            for (int i = 0; i < value.length; i++) {
                h = 31 * h + val[i];
            }
            hash = h;
        }
        return h;
    }
```
- 31을 사용하는 이유는 계산했을 때 2^5가 32이므로 shift연산시 구현이 쉬움. (N*31 = N<<5 - N)

결론:
Java에서는 HashMap을 사용하면 되고 HashMap은 seperate Chaining과 보조 해시 함수를 이용하고 있다. 현재 배열 사이즈의 3/4가 넘어가면 2배로 증설하며 다시 chaining을 재배치하므로 초기 사이즈를 미리 할당할 수 있다면 하는것이 좋고, 그렇다고 너무 많은 사이즈를 잡는것은 좋지 않다. Java8부터는 체이닝이 한 엔트리에 8개가 넘어가면 트리구조로 저장되어 탐색시간이 logN으로 절감되었다. HashMap은 쓰레드에 safe하지 않으므로 쓰레드,병렬처리가 필요하다면 concurrentHashMap같은걸 사용하는것이 좋을 것 -> 이부분은 따로 조사