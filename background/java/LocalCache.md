### Local Cache vs Global Cache

- Local Cache
  - 서버마다 캐시 따로 저장
  - 로컬 서버 장비의 리소스 이용 (Memory, Disk)
  - 서버 내에서 작동하므로 속도가 빠름 (네트워크를 타지 않음)
  - 코드 내에서 Map에 넣고 꺼내오는 방식과 유사하다. (직접 로컬캐시를 구현 한다면 `ConcurrentHashMap`) 
  - 분산 시스템에서 데이터 정합성이 깨질 수 있다. 
  - ex) Caffeine Cache, Ehache
- Global Cache
  - 여러 서버에서 캐시 서버에 접근하여 참조
  - 서버간 데이터 공유 가능
  - 네트워크 트래픽 이용
  - 데이터 분산하여 저장 (Master / Slave, Sharding)
- ex) Redis

- 출처
  - [Local Cache, Global Cache 차이](https://goldfishhead.tistory.com/29)



### Guava Cache

- Google Guava에서 지원하는 Local Cache 라이브러리
- Key-Value형태의 데이터 구조로 표현된다.
- 캐시 크기, 리프레시, 로딩방법 등을 지정할 수 있음 
- 내부적으로 ConcurrentHashMap과 유사하게 되어있어 thread safe하다.
  - ConcurrentMap과 차이라고 한다면 캐시는 메모리 사용공간을 제한하기 위해서 값을 자동으로 제거하는 로직이 들어가있다.

```java
// https://guava.dev/releases/19.0/api/docs/com/google/common/cache/LoadingCache.html
Implementations of this interface are expected to be thread-safe, and can be safely accessed by multiple concurrent threads.
```



#### 종류 

**1. LoadingCache**

- 필요한 값이 없을 때 데이터를 다시 로드해줌.

- CacheLoader와 함께 사용됨 

- ```java
  LoadingCache<Key, Graph> graphs = CacheBuilder.newBuilder()
         .maximumSize(1000)
         .build(
             new CacheLoader<Key, Graph>() {
               public Graph load(Key key) throws AnyException {
                 return createExpensiveGraph(key);
               }
             });
  
  ...
  try {
    return graphs.get(key);
  } catch (ExecutionException e) {
    throw new OtherException(e.getCause());
  }
  ```

build()에 CacheLoader 구현 객체를 넣어야하고 CacheLoader구현체는 load메서드를 구현해야한다. 인스턴스를 만든 이후에는 Map을 쓰듯이 쓰면 됨..



**2. Cache**

- 데이터를 자동으로 로드하지 않음 



#### **캐시에서 키와 관련된 값을 가져오는 메서드**

\-    **get() :** 데이터를 로딩하는 중 Checked Exception이 발생할 경우 ExecutionException을 던진다. 그러므로 예외 처리 코드를 반드시 작성해주어야 한다.

\-    **getUnchecked() :** get()과 달리 CheckedException을 던지지 않는다. 그러므로 CacheLoader가 CheckedException을 던지지 않는 상황에서만 사용해야 한다. 예외가 발생하면 RuntimeException을 던진다.



#### 리소스 제약으로 데이터 없애는 시점 결정

- size-based eviction : 캐시 사이즈 제한
  - maximumSize
  - maximumWeight
  - .weigher 으로 가중치를 설정할 수 있음 
- time-based eviction : 시간 기반 제거
  - expireAfterAccess(long, TimeUnit)
    - 항목이 읽기 또는 쓰기로 마지막으로 액세스된 이후 지정된 기간이 지난 후에만 항목이 만료됩니다. [항목이 제거되는 순서는 크기 기반 제거](https://github.com/google/guava/wiki/CachesExplained#Size-based-Eviction) 순서와 비슷합니다 .
  - expireAfterWrite(long, TimeUnit)
    - 항목이 생성된 이후 지정된 기간이 경과하거나 값이 가장 최근에 대체된 후 항목을 만료합니다. 일정 시간이 지나면 캐시된 데이터가 오래되면 바람직할 수 있습니다.
  - `CacheBuilder.ticker(Ticker)` 
    - 시간기반 테스트할때는 2초후에 만료되는지 테스트하려고 2초를 기다릴필요 없이 Ticker를 쓰면된다.
- reference-based eviction: 참조기반으로 제거 
  - weakKeys(), weakValues() 값이나 키가 weakReference로 감싸져 weakReference만 남으면 GC됨
    - 가비지 수집은 equals대신 ==로 비교 
  - softKeys(), softValues 

- 제거하기위해서 자동으로 막 찾는건 아니고 실제로 요청이왔을 때 근처에 수행함
  - 그래서 주기적으로 Cache.cleanup해주거나 ScheduledExecutorService를 예약하는게 좋을수도 있음 



#### null값 처리

- null값 캐시는 의미가 없어 null을 로드하려고 하면 예외를 발생시킴

- Optional클래스를 활요해야함 

- ```java
  @Test
  public void whenNullValue_thenOptional() {
      CacheLoader<String, Optional<String>> loader;
      loader = new CacheLoader<String, Optional<String>>() {
          @Override
          public Optional<String> load(String key) {
              return Optional.fromNullable(getSuffix(key));
          }
      };
  
      LoadingCache<String, Optional<String>> cache;
      cache = CacheBuilder.newBuilder().build(loader);
  
      assertEquals("txt", cache.getUnchecked("text.txt").get());
      assertFalse(cache.getUnchecked("hello").isPresent());
  }
  private String getSuffix(final String str) {
      int lastIndex = str.lastIndexOf('.');
      if (lastIndex == -1) {
          return null;
      }
      return str.substring(lastIndex + 1);
  }
  ```

  



#### 다른 캐시 구현 라이브러리나 기술 

(일단 나는 이미 guava라이브러리를 의존성에 추가해놓고 있기 때문에 쉽게 사용할 수 있다는 점이 좋은 것 같다.)

- apache commons
- EHcache (@Cacheable) 
  - JVM의 힙 메모리가 아닌 offheap, disk에 저장하기 위해서 직렬화가 필요함. 캐싱할 데이터는 Serializable을 상속받은 클래스여야함 
  - https://medium.com/finda-tech/spring-%EB%A1%9C%EC%BB%AC-%EC%BA%90%EC%8B%9C-%EB%9D%BC%EC%9D%B4%EB%B8%8C%EB%9F%AC%EB%A6%AC-ehcache-4b5cba8697e0



#### 출처

- Guava Cache (구아바 캐시)
  -  http://okminseok.blogspot.com/2019/03/guava-cache.html
  - https://ijbgo.tistory.com/10 메서드 잘 나와있음 
  - https://guava.dev/releases/19.0/api/docs/com/google/common/cache/LoadingCache.html
  - https://github.com/google/guava/wiki/CachesExplained
  - https://www.baeldung.com/guava-cache



----

# 시간당 실행 횟수 제한(처리율 제한) 

시간 당 실행 횟수 제한 라이브러리 3종 소개 : RateLimiter 

- https://javacan.tistory.com/entry/ratelimiter-ratelimitj-bucket4j-intro



### RateLimiter

- guava
- https://www.baeldung.com/guava-rate-limiter

```java
private RateLimiter limiter = RateLimiter.create(4.0); // 초당 4개 
// 여기를 만약에 0.2로 주면 5초당 1개를 허용한다는 의미 

public void someLimit() {
  if (limiter.tryAcqurie()) { 
    
  } else {
    
  }
}
```

limiter.acquire는 허가를 받을 때 까지 blocking, limiter.tryAcquire는 허가를 받지 못하면 false를 리턴 (호출 분산)



```
public boolean tryAcquire(int permits,
                 long timeout,
                 TimeUnit unit)
```

- `permits` - the number of permits to acquire
- `timeout` - the maximum time to wait for the permits. Negative values are treated as zero.
- `unit` - the time unit of the timeout argument



누출 버킷 알고리즘이라 timeout시간에 따라서 요청이 버려질 수 있다.

- https://jobc.tistory.com/230 
- https://github.com/google/guava/issues/5262
- https://sowhat4.tistory.com/76



### Bucket4j

- https://khj93.tistory.com/entry/Bucket4j%EB%A5%BC-%EC%82%AC%EC%9A%A9%ED%95%9C-Spring-API-%EC%9A%94%EC%B2%AD-%ED%95%9C%EB%8F%84-%ED%8A%B8%EB%9E%98%ED%94%BD-%EC%A0%9C%ED%95%9C%ED%95%98%EA%B8%B0
- Bucket4j는 Token bucket 알고리즘을 기반으로 하는 Java 속도 제한 라이브러리 입니다. Bucket4j는 독립 실행형 JVM 애플리케이션 또는 클러스터 환경에서 사용할 수 있는 스레드로부터 안전한 라이브러리입니다.
- https://www.baeldung.com/spring-bucket4j
- https://github.com/bucket4j/bucket4j/blob/master/README.md





구현방법은 생각보다 간단해서 사용하는 알고리즘에 따라서 원하는 동작이 될 지 생각하면 될 것 같다. 



----

**(참고 - 대규모 시스템 설계 4장)**

**처리율 제한 장치란**

- 클라이언트 또는 서비스가 보내는 트래픽의 처리율을 제어하기 위한 장치
- 특정 기간 내에 전송되는 클라이언트의 요청 횟수 제한
- 요청 횟수가 임계치를 넘어서면 추가로 도달한 호출은 모두 중단됨
- ex) 오픈 API에서 일정 시간당 몇 회 호출등의 제한을 걸 때

**필요성**

- Dos(Denial of Service) 공격에 의한 자원 고갈 방지
- 비즈니스 모델 활용 
- 비용 절감



**처리율 제한 알고리즘**

1. 토큰 버킷 알고리즘

   1. 버킷 크기, 토큰 공급률 
   2. 각 요청은 하나의 토큰을 소비하며 버킷에 주기적으로 공급되는 토큰의 존재 여부에 따라 요청을 전달할지, 버릴지 결정
   3. 장점
      1. 구현이 쉽고 메모리 사용 측면에서 효율적이다
      2. 버킷에 토큰이 충분하기만 하면 시스템에 전달되기 때문에 짧은 시간에 집중되는 트래픽 처리도 가능하다.
   4. 단점
      1. 버킷의 크기와 토큰 공급률이라는 인자를 적절하게 튜닝하는게 까다롭다.
   5. 채용 플랫폼 - AWS(API Gateway, EC2, EBS, CPU Credit), Spring Cloud Netflix Zuul, Bucket4j

2. 누출 버킷 알고리즘

   1. 버킷 크기, 처리율(지정된 시간에 몇 개의 항목을 처리할지), 큐
   2. 장점
      1. 큐의 크기가 제한되어있어 메모리 사용측면에서 효율적
      2. 고정된 처리율로 안정적 출력 가능
   3. 단점
      1. 단기간에 몰리는 트래픽의 경우 큐에 오랜 요청이 쌓이게 되고 제때 처리되지 못하면 최신 요청이 버려짐
         1. 입력 속도가 출력 속도보다 크면 버킷에서 누적이 발생하고 누적이 버킷 용량보다 큰 경우 오버플로가 발생하여 데이터 패킷 손실이 발생할 수 있다.
      2. 버킷의 크기와 처리율이라는 인자를 적절하게 튜닝하는게 까다롭다.
   4. 채용 플랫폼: Amazon MWS, NGINX, Uber-go rate limiter, Shopify**, Guava RateLimiter**

3. 고정 윈도우 카운터 알고리즘

   1.  타임라인을 고정된 간격의 window로 나누고 각 window마다 counter를 붙임
   2. 요청이 접수될 때마다 counter가 1씩 증가하며, counter값이 threshold에 도달하면 새로운 요청은 새 윈도우가 열릴 때 까지 버려짐 
   3. 장점
      1. 메모리 효율이 좋고 이해하기 쉬움
      2. window가 닫히는 시점에 카운터를 초기화하는 방식은 특정한 트래픽 패턴을 처리하기에 적합하다.
   4. 단점
      1. 경계시간에 몰리면 window에 할당된 양보다 더 많은 요청이 처리될 수 있다.

4. 이동 윈도우 로깅 알고리즘

   1. 타임스탬프를 redis의 sorted set같은 캐시에 보관해서 고정 윈도 카운터 알고리즘을 해결
   2. 단점
      1. 타임스탬프를 보관하는 메모리 사용

5. 이동 윈도우 카운터 알고리즘

   1. 3+4
   2. 채용 플랫폼: RateLimitJ

   

   

출처

- 대규모 시스템 설계 
- 참고할 링크 https://hechao.li/2018/06/25/Rate-Limiter-Part1/
- 서비스 가용성 확보에 필요한 Rate Limiting Algorithm에 대해 https://www.mimul.com/blog/about-rate-limit-algorithm/



----

Jmeter로 트래픽 테스트 

