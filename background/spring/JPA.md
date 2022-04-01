**자바 ORM 표준 JPA 프로그래밍 - 기본편** 수강했다. - (22.03.01)

강의에 있는 내용을 한번이라도 훑고 중요한부분을 남이 블로그에 정리한 글 말고 직접 강의로 들어보고 싶어서 다 자리에서 들으면서 따라하진 못하고 만보걷기 할때마다 배속으로 켜놓고 들었다. ㅎ   

꼭 나중에 중요한부분 직접 정리하고 코드로 적용해보기!  

- [ ] 트랜잭션 공부
- [ ] 연관관계 매핑 넣어서 테이블 설계 해서 실제로 프로젝트 해보기 
- [ ] 앞부분 복습하기 (ORM, 영속성 컨텍스트)
- [ ] Spring Data JPA 와의 비교 
- [ ] QueryDsl 공부하기 



### 강의를 통해 배운점 , 정리할 부분 정리 

1. **내가 여태까지 써왔던 것은 Spring Data Jpa, 강의에서 알려주는건 Hibernate가 구현한 JPA**여서 강의에서는 EntityManger를 직접 호출하고 `em.persist`, `em.flush` `em.find` `em.clear()` 등의 함수를 사용한 것이었다. 나중에 Spring Data JPA 공식문서나 자료를 보면서 강의에서 배운 JPA 인터페이스를 어떻게 한번 더 구현해서 사용하게 해놨는지 정리하면 될 것 같다. 
2. 1:N 에서 연관관계의 주인은 N이며 N테이블에만 실제로 FK가 들어가 Join이 이루어진다. 
3. 모든 연관관계는 지연로딩으로 설정해주어야 한다. 즉시로딩(Eager)로 했다가 낭패볼 수 있음. 성능상 한번에 불러와야 할 때 fetch join을 사용한다 `N+1` 문제 
4. 트랜잭션 안에서만 영속성 컨텍스트가 생성되어 엔티티가 관리된다. 
5. [추가] N+1문제는 Eager,Lazy 시점의 차이가 아니라 연관관계에서 무조건 일어날 수 있는 문제이다, Eager는 그 즉시에 일어나기 때문에 더 위험한 것일 뿐.



### JPA(java Persistence API)

**자바 어플리케이션에서 관계형 데이터베이스를 사용하는 방식을 정의한 인터페이스** 로, Java SE와 Java EE에서 자바의 영속성 관리와 ORM을 위한 표준 기술이다.  

`인터페이스` 이므로 구현이 없고 `javax.persistence` 도 대부분 `interface`, `enum`, `exception` `annotation` 으로 이루어져있다.



### Hibernate

JPA인터페이스의 구현체이다. 직접 구현한 라이브러리.  

즉 JPA를 사용하기 위해서 반드시 Hibernate를 사용할 필요가 없다. 다른 JPA구현체나 직접 구현하는 방법도 있다.  

### Spring Data JPA

JPA를 쓰기 편하게 만들어놓은 모듈로, EntityManger, persist, find 등을 직접 사용하는 것이 아닌 `Repository`를 정의해서 사용한다.  

JPA를 한단계 추상화시킨 `Repository`라는 인터페이스를 제공함으로써 Spring에서 알아서 해당 메소드 이름에 적합한 쿼리를 날리는 구현체를 만들어 Bean으로 등록해준다.      

```java

@Repository
@Transactional(readOnly = true)
public class SimpleJpaRepository<T, ID> implements JpaRepositoryImplementation<T, ID> {

    private static final String ID_MUST_NOT_BE_NULL = "The given id must not be null!";

    private final JpaEntityInformation<T, ?> entityInformation;
    private final EntityManager em; // em
    private final PersistenceProvider provider;
  
  //...
}
```

> EntityManager는 Entity를 관리해주는 역할을 하는데, 쿼리를 개발자가 관리하지 않고 EntityManager에게 맡긴다.  

<br/>

## JPA에서 가장 중요한 2가지 - ORM, 영속성컨텍스트

> 이해된 간단하게 쓴거라 제대로 정리된게 아닐 수 있음 

### ORM

- 객체와 관계형 데이터베이스 매핑하기
- Object relational mapping
- 객체는 객체대로 설계
- 관계형 DB는 관계형 DB대로 설계
- ORM 프레임워크가 중간에서 매핑 

>  JPA는 애플리케이션과 JDBC사이에서 동작한다. 
>
> MemberDAO -> JPA가 Entity분석, INSERT SQL 생성, JDBC APi 사용, **패러다임 불일치 해결.**
>
> 쿼리를 JPA가 만들어줌

### JPA를 왜 사용해야 하는가?

- 생산성 - JPA 와 CRUD
  - 저장 - jpa.persist(member)
  - 조회 - Member member = jpa.find(membeID)
  - 수정 - member.setName("변경할 이름")
  - 삭제 - jpa.remove(member)
- 유지보수 
  - 기존 : 필드명 변경시 모든 SQL 수정
  - JPA : 필드만 추가하면 됨. 
- JPA와 패러다임의 불일치 해결
  - 상속 
  - 연관관계

### 연관관계의 주인 - 핵심은 단방향 설계

- DB 
  - 1:N 관계에서 무조건 N에 FK가 들어간다. 
  - FK(외래키)를 넣는 이유는 그렇지 않으면 N 테이블에 1에 대한 중복 데이터가 쌓일 것. 중복된 테이블을 분리하고 하나의 테이블로 만들어 식별자 하나만 FK로 가져와서 관리한다. 
- JPA
  - 1:N 관계에서 `@JoinColum`을 어디든 붙일 수 있다. 관계를 이어주는 값이 id가 아니고 객체이다. 
  - 하지만 JoinColum에 지정해주는 값은 무조건 N에 들어간 FK (id) 이름이므로 N이 되는 엔티티에 코드를 작성하는것이 보기에도 이해가 되고, 1에 넣어 괜히 복잡한 조인이 더 생기는 것을 막을 수 있다. 
  - 1 관계의 엔티티에서 조회가 필요할 경우 `@OneToMany`를 이용해서 필드를 추가해주고, **mappedby를 넣어 연관관계의 주인을 설정**해준다.
    - JPA에서 두 엔티티에 서로 참조관계를 넣어도 DB테이블에서는 달라지는게 없다. 

>  1:N 관계에서 N에 (FK가 들어가는 위치)에 @ManyToOne으로 id가 아닌 객체를 넣어주고 설계하는게 우선
>
> 그 다음에 반대방향 조회가 필요할 때 넣으면되는데, 그땐 디비 설계에서 달라지는게 없음. 이때 넣을때 mappedby로 주인을 설정해준다.

### 영속성 컨텍스트

*Entity를 영구 저장하는 환경*

- EntityManagerFactory
  - 어플리케이션 로딩 시점에 DB당 딱 1개 생성
- EntityManager
  - 고객의 요청이 들어올 때 마다(쓰레드 생성될 때 마다) 생성 
  - 쓰레드간 공유 X 사용하고 버림
    - (참고)그래서 1차캐시가 쓰레드간 캐시가 공유되어 성능상 이점이 생기는건 아님
  - EntityManager를 통해서 영속성 컨텍스트에 접근 (1:1)
- EntityTransaction
  - **JPA의 모든 데이터 변경은 트랜잭션 안에서 실행** 
    - tx.begin()
    - tx.commit()
    - tx.rollback()

### 영속성 컨텍스트의 이점

- **1차캐시**
  - DB에서 가져오기 전에 1차캐시에서 먼저 조회해서 찾는 엔티티가 있으면 바로 가져온다.
  - 실제로 디비에 쿼리를 날리는 jpql, namedQuery(data jpa에서는 @Query)를 쓰면 데이터베이스와 1차캐시의 동기화가 안될 수도 있으니 주의해야한다. (-> 디비에는 반영됐는데 서버에선 수정 전 결과가 조회됨)
    - 벌크연산 시 @Modifying과 clearAutomatically
    - em.flush 또는 em.clear로 정리
- **동일성 보장**
  - ==가 true임을 보장한다. 
  - proxy와 실제 엔티티를 가져올때도 어쨌든 동일성을 보장
    - `em.find()`
    - `em.getReference()` or 지연로딩시 
    - 만약 영속성 컨텍스트에 찾는 엔티티가 이미 있다면 reference로 호출해도 엔티티를 반환한다 -> 이래야 동일성이 보장될것
    - proxy 생성, 조회 후 find로 조회하면 find호출시에도 proxy반환
- **트랜잭션을 지원하는 쓰기 지연**
  - persist()할때가 아니라 `tx.commit()`하는 시점에 DB에 보냄 
  - insert, delete 등 쿼리에 따라 보내는 순서가 달라서 생기는 문제도 있다고 한다->블로그에서봄
  - flush는 db에 반영시키는건데 그렇다고 트랜잭션이 커밋되는건 아님
- **변경감지(dirty checking)**
  - 영속성 컨텍스트 1차캐시에 들어있는 엔티티의 값이 변경되면 flush가 일어날 때 값을 비교해서 `save` (spring data jpa) or `persist`(jpa)를 호출하지 않아도 commit할 때 업데이트된 정보를 db에 반영한다 
  - `@transactional(readOnly=true)`
    - 읽기만 있는 경우 변경감지를 할 필요가 없으므로 readOnly옵션을 걸어주면 dirty checking을 위해 소모하는 일을 줄일 수 있을 것 
- **지연 로딩** 
  - 실제로 사용되기 전까지 SQL로 조회해오지 않는다. 
  - 연관관계에서 @ManyToOne, @OneToOne은 기본이 즉시로딩(`fetchType이 EAGER`)이다. 
    - member에서 team이라는 필드를 가지고 있는데, EAGER로 되어있는 상태로 member를 조회하면 team필드를 채우기 위해 추가로 team을 조회하는 쿼리가 나가게된다. 
    - 만약 member가 100명, 그와 연관된 team이 100명 이라면 member리스트를 조회하는 `select *  from member` 쿼리 외에도 `select team ..` 쿼리가 100개가 더 나가게될것 -> **N+1 문제**
    - 하나의 연관관계가 아니고 더 복잡한 연관관계가 이루어지면 EAGER로 설정하면 실무에서 엄청난 문제가 생기므로 무조건 지연로딩을 걸어놓는게 좋다. 그리고 만약 member조회시 team을 가져오고 싶을 때는 하나의 쿼리만 나가도록 fetch join`을 사용한다.
  - @OneToMany 는 기본이 지연로딩이다. 
    - OneToMany도 결국 연관관계 엔티티를 호출하면 N+1과같은 쿼리가 나가긴 하는데, 기본이 지연로딩이므로 EAGER 기본값으로 설정되었음을 간과하고 생기는 N+1 문제를 얘기할 땐 @ManyToOne관계를 예시로 드는 것이 좋을 것 같다. 여튼 이것도 fetch join을 통해서 한번에 쿼리를 만드는 것을 사용할 수 있을 것이다. 
  - 연관관계 엔티티의 `toString()`의 무한 루프를 주의하여야 한다. 

### 플러시 

- 영속성 컨텍스트 플러시 하는 방법
  - em.flush() 직접 호출
  - 트랜잭션 커밋 - 플러시 자동 호출
  - JPQL 쿼리 실행 - 플러시 자동 호출 (옵션 선택 가능)

- 주의
  - 영속성 컨텍스트를 비우는 것이 아님 
  - 영속성 컨텍스트의 변경 내용을 DB에 동기화 하는 것
  - 트랜잭션이라는 단위가 중요 -> 커밋 직전에만 동기화 하면 됨 

### 패치 조인 (fetch join) , N+1

- SQL 조인 종류 X , JPA에서 성능 최적화를 위해 제공하는 기술
- 연관된 엔티티나 컬렉션을 **SQL 한번에 함께 조회하는 기능**
  - `select m from member`+`select team ..` 
  - -> ` select m, t from member join ....`
- 따라서 N+1문제를 해결하려면 Fetch join, 또는 batch Size등을 이용할 수 있을 것이다. 



### 영속성 상태

- 비영속 
  - 영속성 컨텍스트와 전혀 관계 없는 상태
  - `Member m = new Member();`만 한 상태
- 영속
  - 영속성 컨텍스트에 저장된 상태
  - `em.persist()`
- 준영속
  - 영속성 컨텍스트에 저장되어 있다가 분리된 상태
  - `em.detach()` 또는 DB에서 id로 불러온상태
- 삭제
  - 영속성 컨텍스트에서 삭제된상태
  - `em.remove()`

영속상태가 되면 식별자(`@Id`)로 구분되고 영속성 컨텍스트에 저장된 엔티티는 항상 같은 인스턴스를 반환한다. 수정하면 데이터 값도 수정된다.   

준영속상태의 엔티티들은 `save`가 아닌 merge를 사용해야하며, 또는 FindById로 받아온 후 dirth chekcing으로 수정해야한다. 

---

## Spring Data JPA의 사실과 오해

유튜브 링크 : https://www.youtube.com/watch?v=rYj8PLIE6-k 

- 연관관계 매핑
  - 사실상 단방향 매핑만으로 연관관계의 매핑은 이미 완료된다.
  - 대개의 겨우 단방향 매핑이면 충분하다
  - 일대다 단방향 연관관계 매핑에서 영속성 전이를 사용할 경우 양방향으로 변경 (그렇지 않으면 update쿼리가 추가로나감)
- Spring Data JPA Repository
  - 웬만한 CRUD, Paging, Sorting 사용 가능함
    - Paging과 Slice가 있는데 paging은 total을 조회하기 위해 한번 더 count(*)쿼리를 조회한다.
    - Slice는 limit+1까지 조회해서 next를 판단한다. 
    - 레코드가 많을 때 전체 개수가 필요하지 않을때는 Page가 아닌 slice를 사용해 부담을 줄일 수 있다. 
  - 메서드 이름 규칙을 이용한 쿼리 생성
  - Join 쿼리 수행 가능 -> Entity내의 필드를 `_`를 이용해서 탐색할 수 있음
  - 엔티티 뿐 아니라 DTO Projection 지원

## N+1 문제는 EAGER Fetch 전략때문에 발생하는게 아니다.

Eager, Lazy는 하나의 쿼리로 연관관계 엔티티들도 그 즉시 가져오나, 나중에 가져오나의 문제이지 실제 참조가 일어날때는 결국 N+1이 발생하게된다. 

- Fetch join으로 N+1 문제 해결시 흔히 하는 실수
  - pagination 쿼리에 fetch join을 적용하면 모든 레코드를 가져오는 쿼리가 실행된다. 