### Structered Concurrency

완료가 COMPLETING -> COMPLETED 두 단계로 나눠진 이유

- 자식 코루틴을 기다려야하기 때문
  - 자식 코루틴을 기다리다가 예외가 발생하면 예외가 부모로 전파되어 다른 자식 코루틴에게 취소요청을 보낸다.

```kotlin
fun main(): Unit = runBlocking {
    launch {
        delay(600L)
        printWithThread("Job 1 ")
    }
    launch {
        delay(500L)
        throw IllegalArgumentException("코루틴 실패!")
    }
}
```

첫 번째 코루틴은 delay를 호출해서 취소에 협조적이기 때문에, 두번째 코루틴에서 실패된 예외가 부모로 전파되어 부모에서 자식코루틴에 취소요청을 보냈을 때 바로 취소된다.

-> 부모, 자식 코루틴이 한 몸처럼 움직이는 것을 Structured Concurrency라고 함



**Structured Concurrency**

- 코드 내의 에러가 유실되거나 누수되지 않도록 보장한다.
- 코드 내의 에러가 유실되지 않고 적절히 보고될 수 있도록 보장한다.



### CoroutineScope와 CoroutineContext

launch와 async는 couroutineScope의 확장함수

```kotlin
fun main() {
    CoroutineScope(Dispatchers.Default).launch {
        delay(1_000L)
        printWithThread("Job1")
    }
    Thread.sleep(1_500L)
}
```

runBlocking없이도 CoroutineScope를 만들어 실행하면 된다. 대신 runBlocking이 없기 때문에 자체적으로 Thread.Sleep으로 충분히 실행될 시간을 줘야한다.

```kotlin
suspend fun main() {
    val job = CoroutineScope(Dispatchers.Default).launch {
        delay(1_000L)
        printWithThread("Job1")
    }
    job.join()
}
```

아니면 join으로 코루틴이 끝날 때 까지 대기시켜줄 수도 있다 (main을 suspend fun으로 만들어야함)



**CoroutineScope의 주요 역할**

- CoroutineContext라는 데이터를 보관하는 것
- 코루틴이 탄생할 수 있는 영역

```kotlin
public interface CoroutineScope {
  public val coroutineContext: CoroutineContext
}
```



**CoroutineContext란**

- 코루틴과 관련된 여러가지 데이터를 보관
- CoroutineExceptionHandler, 코루틴 이름, 코루틴 그 자체, CoroutineDispatcher 등



**Dispatcher**

- 코루틴이 어떤 스레드에 배정될지 관리하는 역할



![스크린샷 2023-12-10 오후 8.28.18](스크린샷%202023-12-10%20오후%208.28.18.png)



**클래스 내부에서 독립적인 CoroutineScope을 관리**

클래스 내에서 CoroutineScope을 가지고 있다면 포함된 코루틴을 한번에 종료시킬 수도 있다. 부모 자식관계도 적절히 만들어준다.

```kotlin
class AsyncLogic {
    private val scope = CoroutineScope(Dispatchers.Default)

    fun doSomthing() {
        scope.launch {
            // 무언가 코루틴이 시작되어 작업
        }
    }

    fun destroy() {
        scope.cancel()
    }
}
```



#### CoroutineContext

map + set을 합쳐놓은 형태로, key-value로 데이터를 저장하며 같은 key의 데이터는 유일하다.

```kotlin
public interface CoroutineContext {
    /**
     * Returns the element with the given [key] from this context or `null`.
     */
    public operator fun <E : Element> get(key: Key<E>): E?
...
}
```



코루틴 컨텍스트의 덧셈 기호를 이용해 이런식으로 합칠수도 있고, 코루틴 내에 CoroutineContext 값에 접근해서 조작할 수도 있다.

```kotlin
fun main() {
    CoroutineName("나만의 코루틴") + SupervisorJob()
    CoroutineName("나만의 코루틴") + Dispatchers.Default
}

///
CoroutineScope(Dispatchers.Default).launch {
  coroutineContext + CoroutineName("이름")
  coroutineContext.minusKey(CoroutineName.Key)
}
```



#### CoroutineDispatcher

코루틴을 스레드에 배정하는 역할을 함

 **종류**

- Dispatchers.Default
  - 가장 기본적인 디스패처, CPU 자원을 많이 쓸 때 권장
  - 별다른 설정이 없으면 이 디스패처가 사용된다.
- Dispatchers.IO
  - I/O 작업에 최적화
- Dispatchers.Main
  - 보통 UI 컴포넌트를 조작하기 위한 디스패처
  - 특정 의존성을 갖고 있어야 정상적으로 활용할 수 있다. (안드나 ios)



**ExecutorService를 디스패처로**

`asCoroutineDispatcher()` 확장함수 활용

```koltin
fun main() {
    // 하나의 쓰레드를 가진 executor
    val threadPool = Executors.newSingleThreadExecutor()
    CoroutineScope(threadPool.asCoroutineDispatcher()).launch {
        printWithThread("새로운 코루틴")
    }
}
```



### suspending function

suspend가 붙은 함수로, 다른 suspend가 붙은 함수를 호출할 수 있다.



**launch는 suspend 람다가 붙은 block을 호출(suspending lambda)**

suspend fun인 `delay()`을 부르려면 호출하는 함수도 suspend여야하는데, launch의 block이 `suspend CoroutineScope.() -> Unit` 형태이다.



**suspend function의 다른 기능**

코루틴이 중지 되었다가 재개**될 수 있는 지점** (suspending point)

- 될 수도 있고 , 안될 수도 있음



**예제**

이 비동기 예시는 async의 결과가 Deferred라서 비동기 호출 방식을 바꾸게 될 경우 main 함수 로직에 똑같이 영향을 미친다.

```kotlin

fun main(): Unit = runBlocking {
    val result1: Deferred<Int> = async {
        call1()
    }
    val result2 = async {
        call2(result1.await())
    }

    printWithThread(result2.await())
}

// 외부 IO 호출을 한다고 가정
fun call1(): Int {
    Thread.sleep(1_000L)
    return 100
}

fun call2(num: Int): Int {
    Thread.sleep(1_000L)
    return num * 2
}

```



suspend fun으로 변경하면 main함수는 코틀린의 순수한 타입에 의존하게되고, 내부 구현을 모르고 중단될 수도 있는 메소드를 호출하는 것이 된다.

```kotlin

fun main(): Unit = runBlocking {
    val result1:Int =  call1()
    val result2 =  call2(result1)

    printWithThread(result2)
}

// 외부 IO 호출을 한다고 가정
suspend fun call1(): Int {
    return CoroutineScope(Dispatchers.Default).async {
        Thread.sleep(1_000L)
        100
    }.await()

}

suspend fun call2(num: Int): Int {
    return CompletableFuture.supplyAsync{
        Thread.sleep(1_000L)
        num * 2
    }.await()
}

```



**활용 - 여러 비동기 라이브러를 사용할 수 있도록 도와줌 **

```kotlin
interface AsyncCaller {
    suspend fun call()
}

class AsyncCallerImpl: AsyncCaller {
    override suspend fun call() {
        TODO("Not yet implemented")
    }
}
```



#### 추가 suspend 함수들

- coroutineScope
  - 추가적인 코루틴을 만들고 주어진 함수 블록이 바로 실행된다.
  - 만들어진 코루틴이 모두 완료되면 다음 코드로 넘어간다.
  - ![스크린샷 2023-12-10 오후 8.48.37](스크린샷%202023-12-10%20오후%208.48.37.png)
  - START  / 30 / END로 출력됨
- withContext
  - coroutineScope과 기본적으로 유사하다
  - context에 변화를 주는 기능이 추가적으로 있다.
  - ![스크린샷 2023-12-10 오후 8.49.38](스크린샷%202023-12-10%20오후%208.49.38.png)
- withTimeout / withTimeoutOrNull
  - coroutineScope과 기본적으로 유사
  - 주어진 시간 안에 새로 생긴 코루틴이 완료되어야 한다.
  - 주어진 시간 안에 코루틴이 완료되지 못하면 예외를 던지거나 null을 반 



### 코루틴과 Continuation

**예제**

`suspend fun findUser()` -> `suspend fun findProfile()`, `suspend fun findImage()` 호출



continuation을 만들었고, 이것을 지속적으로 전달하며 callback으로 활용해 라벨마다 불려지게 만드는것.

```kotlin
interface Continuation {
    // 라벨을 가지고 있을 것
}
class UserService {
    private val userProfileRepository = UserProfileRepository()
    private val userImageRepository = UserImageRepository()

    suspend fun findUser(userId: Long): UserDto {

        // state machine
        val sm = object: Continuation{
            var label = 0
            var profile: Profile? = null
            var image: Image? = null
        }

        when (sm.label) {
            0 -> {
                // 0단계 - 초기 시작
                println("프로필을 가져오겠습니다")
                sm.label = 1
                val profile = userProfileRepository.findProfile(userId)
                sm.profile = profile
            }
            1 -> {
                // 1단계 - 1차 중단 후 재시작
                println("이미지를 가져오겠습니다")
                sm.label = 2
                val image = userImageRepository.findImage(sm.profile!!)
                sm.image = image
            }
            2 -> {
                // 2단계 - 2차 중단 후 재시작
                return UserDto(sm.profile!!, sm.image!!)
            }
        }
        
    }
}
```

findUser가 findProfile, findImage (또다른 suspend를 부를 때) continutaion도 넘겨지도록 함



핵심은 Continuation을 전달하는 것이고, 전달하며 Callback으로 활용한다.

어렵당..

Continuation Passing Style (CPS)



**실제 Continuation 인터페이스**

```kotlin
public  interface Continuateion<in T> {
  public val context: CoroutineContext
  public fun resumeWith(result: Result<T>)
}
```



### 코루틴의 활용과 마무리

- callback hell을 해결 
- kotlin 언어 키워드가 아닌 라이브러리
- 비동기 non-blocking 혹은 동시성이 필요한것
- 클라이언트 비동기 ui
- 서버 여러 api 동시에 호출
- 웹플럭스
- 동시성 테스트