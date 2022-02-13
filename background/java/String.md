### String의 Immutable

Java의 String은 immutable하다. 결국 값을 수정한다면 새로운 객체를 생성하고 그 값을 재 할당해야한다.  

만약 `str="abc"`를 한 후에 `str="def"`로 str이라는 변수가 갖는 참조값이 0x11(abc)에서 0x22(def)로 바뀐다고 하더라도, str변수가 갖는 참조 값이 변경된것이지 실제 abc가 저장되어있는 0x11주소의 데이터가 변경된 것이 아니다.

String이 Java에서 Immutable한 이유

- 캐싱
  - JVM이 String Constant Pool이라는 영역을 만들고 문자열들을 Constant화 하여 다른 변수 혹은 객체들과 공유하게 되는데, 이 과정에서 데이터 캐싱이 일어나고 그만큼 성능적인 이득을 취할 수 있다. String이 불변이 아니었다면, 해당 메모리의 값이 언제 바뀔지 알 수 없기 때문에 String pool형태로 관리할 수 없었을 것이다. a,b,c가 모두 같은 값을 가리키고 있는 상태에서 a의 값을 바꾸게 되면 b,c의 값도 바뀌는 문제가 생겼을 것이다. 
  - String의 hashcode 생성 단계에서부터 캐싱이 이루어져 hashcode는 쓰일때마다 매번 계산되지 않는다. 이는 hashCode를 key로 사용하는 hashMap을 사용할 때 효과가 있다. 
- 동기화
  - Multi-thread 환경에서 safe하다
- 보안성
  - DB의 username,pw 통신에서 host의 port와 같은 String값이 특정 공격에 의해 변형되어 해킹에 노출되는 위험으로부터 예방할 수 있다. 



### Java에서 String 객체를 생성하는 방법

1. String Literal ("")
2. new String("")

```java
    @Test
    @DisplayName("String literal로 생성한 두 객체는 값이 같다면 동일하다")
    void stringLiteral() {
        String str1 = "coffee";
        String str2 = "coffee";
        assertTrue(str1 == str2);
        assertTrue(str1.equals(str2));
    }

    @Test
    @DisplayName("new 연산자로 생성한 String 객체는 값이 같아도 동일하지 않다.")
    void newString() {
        String str1 = new String("coffee");
        String str2 = new String("coffee");
        assertFalse(str1 == str2);
        assertTrue(str1.equals(str2));
    }
```

String Literal로 생성한 객체는 내용이 같다면 같은 객체, 즉 동일한 메모리 주소를 가리키고, new 연산자로 생성한 String 객체는 내용이 같더라도 개별적인 객체로 처리된다. 



### Java String Pool

String literal로 생성하면 해당 String값은 Heap영역 내에 `String Constant pool`에 저장되어 재사용되지만, new 연산자로 생성하면 같은 내용이라도 여러개의 객체가 각각 Heap 영역에 생성된다.

String은 재사용될 확률이 높기 때문에 이에 대한 대처방법으로 Heap영역 내에 문자열 상수 Pool을 유지한다.





출처 

- https://starkying.tistory.com/entry/what-is-java-string-pool 
- https://readystory.tistory.com/140?category=784159