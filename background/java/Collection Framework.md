# 컬렉션 프레임워크 (Collection Framework)

자바에서 컬렉션 프레임워크란 다수의 데이터를 쉽고 효과적으로 처리할 수 있는 표준화된 방법을 제공하는 클래스의 집합이다.   
즉 데이터를 저장하는 **자료구조**와 데이터를 처리하는 **알고리즘**을 구조화하여 클래스로 구현해놓은 것이다.   

이러한 컬렉션 프레임워크는 자바의 인터페이스를 사용하여 구현된다.

---

## 컬렉션 프레임워크 주요 인터페이스

![cf](https://media.geeksforgeeks.org/wp-content/cdn-uploads/20200811210521/Collection-Framework-1.png)

![map](https://media.geeksforgeeks.org/wp-content/cdn-uploads/20200811210611/Collection-Framework-2.png)  

이미지 출처 - [geeksforgeeks](https://www.geeksforgeeks.org/how-to-learn-java-collections-a-complete-guide/)

크게 List,Set,Map으로 정의되며 List와 Set 인터페이스는 Collection 인터페이스를 상속받지만, 구조 차이로 인해 Map은 별도로 정의된다.


### 간략한 특징
| 인터페이스 | 설명                                                         | 구현 클래스                                 |
| ---------- | ------------------------------------------------------------ | ------------------------------------------- |
| List<E>    | **순서가 있는** 데이터의 집합<br/> 데이터의 **중복 허용**    | Vector, ArrayList, LinkedList, Stack, Queue |
| Set<E>     | **순서가 없는** 데이터의 집합<br/> 데이터의 **중복을 허용하지 않음** | HashSet, TreeSet                            |
| Map<K,V>   | **키,값 한쌍**으로 이루어진 **순서가 없는** 데이터의 집합<br/> 키의 중복은 허용하지 않지만 값은 중복될 수 있음 | HashMap, TreeMap, Hashtable, Properties     |


### 컬렉션 클래스
컬렉션 프레임워크에 속하는 인터페이스를 구현한 클래스  
컬렉션 프레임워크의 모든 컬랙션 클래스는 List,Set,Map중 하나의 인터페이스를 구현하고 있으며 클래스 이름에도 인터페이스 이름이 포함되어 있어 구분할 수 있다.   

Vector나 Hashtable은 예전부터 사용해 와서 기존 코드와의 호환을 위해 남아있지만, 기존에 사용하던 클래스보다는 새로 추가된 ArrayList나 HashMap 클래스를 사용하는 것이 더 낫다.

> 생각해볼 점
> - Vector vs ArrayList
> - Hashtable vs HashMap

출처
http://www.tcpschool.com/java/java_collectionFramework_concept

