정리중 ~_~



### Guava Cache

Key-Value형태의 데이터 구조로 표현된다.

캐시 크기, 리프레시, 로딩방법 등을 지정할 수 있음 

내부적으로 ConcurrentHashMap과 유사하게 되어있어 thread safe하다.

- ConcurrentMap과 차이라고 한다면 캐시는 메모리 사용공간을 제한하기 위해서 값을 자동으로 제거하는 로직이 들어가있다.

```java
// https://guava.dev/releases/19.0/api/docs/com/google/common/cache/LoadingCache.html
Implementations of this interface are expected to be thread-safe, and can be safely accessed by multiple concurrent threads.
```





LoadingCache - 필요한 값이 없을 때 데이터를 다시 로드해줌.

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

- 

Cache - 데이터를 자동으로 로드하지 않음 

CacheBuilder를 이용해 생성 

Guava Cache는 캐시의 최대 사이즈나 시간을 기준으로 캐시가 적정 사이즈를 유지할 수 있도록 지원한다.

 expireAfterAccess

- 마지막 참조 이후 설정된 시간이 지나면 삭제

 expireAfterWrite

- 마지막 갱신 이후 설정된 시간이 지나면 캐시에서 삭제 

CacheStats : hitRate 측정 가능



```java
LoadingCache<Key, Graph> graphs = CacheBuilder.newBuilder()
  .maxinumSize(1000) // 캐시 크기
  .expireAfterWrite(10, TimeUnit.MINUTES)
  .removalListener(MY_LISTENER)
  .build(
  new CacheLoader<Key, Graph>() {
    public Graph load(Key key) {
      return createExpensiveGraph(key);
    }
  }
)
```

build()에 CacheLoader 구현 객체를 넣어야하고 CacheLoader구현체는 load메서드를 구현해ㅑㅇ한다. 인스턴스를 만든 이후에는 Map을 쓰듯이 쓰면 됨..



**캐시에서 키와 관련된 값을 가져오는 메서드**

\-    **get() :** 데이터를 로딩하는 중 Checked Exception이 발생할 경우 ExecutionException을 던진다. 그러므로 예외 처리 코드를 반드시 작성해주어야 한다.

\-    **getUnchecked() :** get()과 달리 CheckedException을 던지지 않는다. 그러므로 CacheLoader가 CheckedException을 던지지 않는 상황에서만 사용해야 한다. 예외가 발생하면 RuntimeException을 던진다.



리소스 제약으로 데이터 없애는 시점 결정

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



null값 처리

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

- 



- 실제로 thread safe한지 찾아보기
- 또 다른 Local Cache 구현사항은 없을지 찾아보기
  - 일단 이미 guava라이브러리를 의존성에 추가해놓고 있기 때문에 쉽게 사용할 수 있다는 점이 좋은 것 같다.
  - apache commons
  - EHcache (@Cacheable)
    - redis같이 별도의 서버를 사용하여 생길 수 있는 네트워크 지연 혹은 단절같은 이슈에서 자유롭고 같은 로컬 환경 일지라도 별도로 구동하는 memcached와는 다르게 ehcache는 서버 어플리케이션과 라이프사이클을 같이 하므로 사용하기 더욱 간편하다.
    - JVM의 힙 메모리가 아닌 offheap, disk에 저장하기 위해서 직렬화가 필요함. 캐싱할 데이터는 Serializable을 상속받은 클래스여야함 
    - https://medium.com/finda-tech/spring-%EB%A1%9C%EC%BB%AC-%EC%BA%90%EC%8B%9C-%EB%9D%BC%EC%9D%B4%EB%B8%8C%EB%9F%AC%EB%A6%AC-ehcache-4b5cba8697e0





출처

- Guava Cache (구아바 캐시)
  -  http://okminseok.blogspot.com/2019/03/guava-cache.html
  - https://ijbgo.tistory.com/10 메서드 잘 나와있음 
  - https://guava.dev/releases/19.0/api/docs/com/google/common/cache/LoadingCache.html
  - https://github.com/google/guava/wiki/CachesExplained
  - https://www.baeldung.com/guava-cache



----

시간당 실행 횟수 제한 라이브러리

- RateLimiter
  - guava

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

뭔가 tryAcquire에 인자로 대기시간을 걸어야 원하는대로 동작한다는 것도 같음 

- https://jobc.tistory.com/230 

- 기간을 100ms 이상으로 연장하면 원하는 결과를 얻는 데 더 많은 행운이 있을 수 있습니다.

  어쨌든 이 문제는 아직 해결되지 않았습니다. 여기보다 stackoverflow.com에서 질문을 보는 사람이 더 많을 수 있습니다.

  - https://github.com/google/guava/issues/5262
  - 쓰레드로부터 안전한지? https://github.com/google/guava/issues/5262





- 

  ```
  public boolean tryAcquire(int permits,
                   long timeout,
                   TimeUnit unit)
  ```

  - `permits` - the number of permits to acquire
  - `timeout` - the maximum time to wait for the permits. Negative values are treated as zero.
  - `unit` - the time unit of the timeout argument

- 

주의

- RateLimiter의 내부 구현은 1초 동안 사용하지 않은 개수를 누적한다. 예를 들어 RateLimiter.create(10)으로 만든 RateLimiter를 1초 동안 사용하지 않았다면 5개가 누적되어 이후 1초 동안은 10개를 사용할 수 있다.

- 내부적으로는 **synchronized**로 동작하여 권한 획득 시 대기 시간을 반환받고 대기 시간 이후 작업을 수행한다.

  즉, 작업 권한을 부여받은 작업(Task)이 완료하여 종료되지 않더라도 다른 작업이 대기 시간 이후 작업을 수행할 수 있다.

  Threadpool size를 고정적으로 사용하는 경우에는 큰 문제가 발생하지 않지만 동적으로 poolsize를 조절하는 *newCachedThreadPool()* 같은 경우 병목 현상이 발생한다면 최대 **Integer.MAX_VALUE** 만큼 thread가 생성될 수 있기에 유의하여 사용해야 한다. 출처 : https://sowhat4.tistory.com/76

 

- 시간 당 실행 횟수 제한 라이브러리 3종 소개 : RateLimiter 
  - https://javacan.tistory.com/entry/ratelimiter-ratelimitj-bucket4j-intro
- baeldung
  - https://www.baeldung.com/guava-rate-limiter





- Bucket4j
  - https://khj93.tistory.com/entry/Bucket4j%EB%A5%BC-%EC%82%AC%EC%9A%A9%ED%95%9C-Spring-API-%EC%9A%94%EC%B2%AD-%ED%95%9C%EB%8F%84-%ED%8A%B8%EB%9E%98%ED%94%BD-%EC%A0%9C%ED%95%9C%ED%95%98%EA%B8%B0
  - Bucket4j는 Token bucket 알고리즘을 기반으로 하는 Java 속도 제한 라이브러리 입니다. Bucket4j는 독립 실행형 JVM 애플리케이션 또는 클러스터 환경에서 사용할 수 있는 스레드로부터 안전한 라이브러리입니다.





---

낼읽어보기

https://12bme.tistory.com/547 [API] Rate Limiting & Resource ID 난독화

고처리량분산비율제한기 https://engineering.linecorp.com/ko/blog/high-throughput-distributed-rate-limiter