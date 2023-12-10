[2시간으로 끝내는 코루틴](https://www.inflearn.com/course/lecture?courseSlug=2%EC%8B%9C%EA%B0%84%EC%9C%BC%EB%A1%9C-%EB%81%9D%EB%82%B4%EB%8A%94-%EC%BD%94%EB%A3%A8%ED%8B%B4&unitId=174684&tab=curriculum) 강의 들은 내용 정리



## 코루틴

- co-routine : 협력하는 루틴 (=코드 모음)

- 루틴 
  - 진입하는 곳이 한 군데
  - 종료되면 해당 루틴의 정보가 초기화된다.
  - 시작되면 끝날 때 까지 멈추지 않는다.
  - 한 번 끝나면 루틴 내의 정보가 사라진다.
- 코루틴
  - 중단되었다가 재개될 수 있다.
  - 중단되더라도 루틴 내의 정보가 사라지지 않는다.



### 시작하기

build.gradle.kts dependencies추가 

```
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")
    testImplementation(kotlin("test"))
}
```





```kotlin
fun main(): Unit = runBlocking {
    println("START")
    launch {
        newRoutine()
    }
    yield()
    println("END")
}

suspend fun newRoutine() {
    val num1 = 1
    val num2 = 2
    yield() 
    println("${num1 + num2}")
}
```

- runBlocking {}
  - 일반 루틴과 코루틴을 연결
  - 이 함수를 사용하는 순간 새로운 코루틴이 만들어짐 ( {} 내부 )
- launch {}
  - 반환값이 없는 코루틴을 만든다
- suspend fun
  - 다른 suspend fun(예시에서는 yield도 suspend fun)을 호출할 수 있다.
- yield()
  - 지금 코루린을 중단하고 다른 코루틴이 실행되도록 한다.
  - 스레드를 양보한다.



#### 실행결과

```
START
END
3
```

- launch는 만들어진 코루틴을 바로 실행시키지 않음
- main의 yield()가 불리면 runBlocking block의 실행이 중단되고 launch block으로 주도권이 넘어감
- 그래서 launch안에 newRoutine이 불리고, 다시 한번 yield가 호출되어 나와서 END가 호출됨
- 그다음 main이 끝나고 newRoutine이 마저 되면서 3을 호출



- 중단과 재개라는 개념이 있음
- 그래서 그동안 메모리 관점으로도 루틴이 완전히 종료되기 전까지는 데이터를 보관하고있어야함
- **루틴이 중단되었다가 해당 메모리에 접근이 가능하다**



#### 디버깅

vmoption에 `-Dkotlinx.coroutines.debug ` 설정해주면 쓰레드 네임 호출할 때 어떤 코루틴인지도 보여줌



```kotlin
fun main(): Unit = runBlocking {
    printWithThread("START")
    launch {
        newRoutine()
    }
    yield()
    printWithThread("END")
}

suspend fun newRoutine() {
    val num1 = 1
    val num2 = 2
    yield()
    printWithThread("${num1 + num2}")
}

fun printWithThread(str: Any) {
    println("[${Thread.currentThread().name}] $str")
}
```

```bash
[main @coroutine#1] START
[main @coroutine#1] END
[main @coroutine#2] 3
```



## 스레드와 코루틴

- 프로세스
  - 실행되고 있는 프로그램
- 스레드
  - 프로세스에 소속되어 여러 코드를 동시에 실행할 수 있게 해줌



**종속 관계의 개념에서**

- 프로세스와 스레드
  - 쓰레드는 프로세스에 속해있고, 다른 프로세스로 바꿀 수 없음
- 쓰레드와 코루틴
  - 코루틴이 가지고 있는 코드를 쓰레드에 넘겨서 실행
  - 코루틴의 코드가 실행되려면 쓰레드가 있어야함 
  - 단, 특정 쓰레드에 종속되어있는건 아니라서 중단되었다가 재개될 때 다른 쓰레드에 배정될 수도 있다. (한 코루틴의 코드가 여러 쓰레드에서 실행될 수 있다.)



**context switching이 일어날 때**

- 각각의 프로세스는 완전히 독립적인 메모리영역, 모든 메모리 교체
- 쓰레드는 Heap은 공유하고 Stack은 각각 가지고 있어 프로세스보다 비용이 적다.
- 동일 쓰레드에서 실행되는 코루틴은 메모리 전부를 공유하고 있어 비용이 가장 적다.
  - 하나의 쓰레드에서도 동시성 확보 가능

**양보**

- 코루틴은 스스로 양보할 수 있다 (yield) - 비 선점
- 쓰레드는 OS가 개입해서 쓰레드 실행을 관리한다 - 선점



## 코루틴 빌더와 Job

코루틴 빌더 : 코루틴을 새로 만드는 함수



#### **(1) runBlocking**

```kotlin
fun main(): Unit = runBlocking {
    
}
```

- 새로운 코루틴을 만들고 루틴 세계와 코루틴 세계를 이어준다.
- runBlocking에 의해서 만들어진 코루틴이나 안에서 만든 코루틴이 **모두 완료될 때 까지 쓰레드를 Block 시킴**
  - 따라서 main 메서드를 실행시킬때나 테스트코드를 시작할 때 그 특정 테스트코드에서만 사용해주는 것이 좋음.

**예시**

```kotlin
fun main() {
    runBlocking {
        printWithThread("START")
        launch {
            delay(2_000L) // 특정 시간만큼 멈춘 후 yield()실행
            printWithThread("LAUNCH END")
        }
    }
    
    printWithThread("END")
}

---
start
LAUNCH END 
END
```

- delay를 통해 양보를 하더라도, runBlocking과 그 안에있는 launch로 실행한 코루틴을 모두 블락킹하기 때문에 END가 먼저 출력되지 않음



#### (2) launch

- 새로운 코루틴을 시작하는 코루틴 빌더

- **반환값이 없는** 코루틴 실행

  - launch의 반환 값은 **코루틴을 제어할 수 있는 객체(Job)** 

- ```
                                            wait children
      +-----+ start  +--------+ complete   +-------------+  finish  +-----------+
      | New | -----> | Active | ---------> | Completing  | -------> | Completed |
      +-----+        +--------+            +-------------+          +-----------+
                       |  cancel / fail       |
                       |     +----------------+
                       |     |
                       V     V
                   +------------+                           finish  +-----------+
                   | Cancelling | --------------------------------> | Cancelled |
                   +------------+                                   +-----------+
    
  ```



**job 예시** - start

```kotlin
fun main(): Unit = runBlocking {
    val job = launch(start = CoroutineStart.LAZY) {
        printWithThread("Hello launch")
    }
    delay(1_000L)
    job.start()
}
```

- start : 시작옵션 (CoroutineStart `LAZY`, `DEFAULT`, `ATOMIC`, `UNDISPATCHED`)
  - LAZY - 코루틴은 원래 만든 즉시 실행시킬 수 있는데, 명확한 시작 신호를 줄 때 까지 대기하도록 LAZY 설정을 해줌. `job.start()` 로 호출해야 시작됨 



**job 예시 - cancel**

```kotlin
fun main(): Unit = runBlocking {
    val job = launch {
        (1..5).forEach {
            printWithThread(it)
            delay(500)
        }
    }
    delay(1_000L)
    job.cancel()
}

---
[main @coroutine#2] 1
[main @coroutine#2] 2
```



**job 예시 - join**

```kotlin

fun main(): Unit = runBlocking {
    val job1 = launch {
        delay(1_000L)
        printWithThread("job 1")
    }
    job1.join() // 코루틴 1이 끝날 때 까지 대기

    val job2 = launch {
        delay(1_000L)
        printWithThread("job 2")
    }
}
```

- 코루틴이 완료될 때 까지 대기





#### (3) async, Deferred

```kotlin
fun main(): Unit = runBlocking {
    val job = async {
        3 + 5
    }
    val await: Int = job.await() // await : async의 결과를 가져오는 함수
    printWithThread(await)
}
```

- launch랑 비슷한데 async안에서 실행한 결과를 반환할 수 있다. (실은 Deferred)
- Deferred는 Job을 상속받고 있어서 동일한 기능 + `await()` 이 있음 (결과를 가져오는 함수)



- async를 사용하면 대기가 필요할 때 여러 코루틴을 동시에 시작시켜놓고 대기를 같이 시킨다음, 결과를 한번에 가져오게 할 수 있다.
- 또한 callback을 이용하지 않고 동기 스타일로 사용할 수 있음



**await**

```kotlin
fun main(): Unit = runBlocking {
    val time = measureTimeMillis {
        val job1 = async { apiCall1() }
        val job2 = async { apiCall2() }
        printWithThread(job1.await() + job2.await())
    }
    printWithThread("소요시간 : $time ms")
}

suspend fun apiCall1(): Int {
    delay(1_000L)
    return 1
}
suspend fun apiCall2(): Int {
    delay(1_000L)
    return 2
}

//
[main @coroutine#1] 3
[main @coroutine#1] 소요시간 : 1027 ms // 2초가 걸리지 않음 
```



**async를 이용한 callback 없는 동기식 호출**

```kotlin
fun main(): Unit = runBlocking {
    val time = measureTimeMillis {
        val job1 = async { api1() }
        val job2 = async { api2(job1.await()) }
        printWithThread(job2.await())
    }
    printWithThread("소요시간 : $time ms")
}

suspend fun api1(): Int {
    delay(1_000L)
    return 1
}
suspend fun api2(num: Int): Int {
    delay(1_000L)
    return 2 + num
}
---
[main @coroutine#1] 3
[main @coroutine#1] 소요시간 : 2029 ms

```

- callback패턴을 안써도 한줄한줄 동기식으로 작성할 수 있다. 



**async 주의사항**

- CoroutineStart.LAZY 옵션을 사용하면 await() 호출시 결과를 계속 기다리게된다. 그래서 앞에꺼가 끝날때까지 기다렸다가 실행함 (위 예제 2초걸림)
- 그 앞에 start함수 호출하게되면 괜찮음



### 코루틴의 취소

필요하지 않은 코루틴을 적절히 취소해 컴퓨터 자원을 아껴야 한다.



#### 취소에 협조하는 방법 1

delay()나 yield()같은 kotlinx.coroutines 패키지의 suspend fun 사용

![스크린샷 2023-12-10 오후 7.22.26](스크린샷%202023-12-10%20오후%207.22.26.png)



#### 취소에 협조하는 방법2

코루틴 스스로 본인의 상태를 확인해 취소 요청을 받았으면, 

CancellationException을 던지기

```java
fun main(): Unit = runBlocking {
    val job = launch(Dispatchers.Default) {
        var i = 1
        var nextPrintTime = System.currentTimeMillis()
        while (i<=5) {
            if (nextPrintTime <= System.currentTimeMillis()) {
                printWithThread("${i++}번째 출력!")
                nextPrintTime += 1_000L
            }
            if (!isActive) {
                // isActive: 코루틴이 launch에 의한 자신의 상태를 확인할 수 있음
                throw CancellationException()
            }
        }
    }
    delay(100L)
    job.cancel()
}
```

- isActive: 현재 코루틴이 활성화 되어있는지, 취소 신호를 받았는 확인

- `launch(Dispatchers.Default)`) 로 실행해서 main이 아닌 다른 스레드로 실행되게함

  - 

  

![스크린샷 2023-12-10 오후 7.30.48](스크린샷%202023-12-10%20오후%207.30.48.png)

참고 - https://wooooooak.github.io/kotlin/2020/06/03/Coroutine_Cancellation/



#### 코루틴이 취소에 협조하는 방법

- kotlinx.coroutines 패키지의 suspend 함수 호출
- isActive로 CancellationException 던지기



delay()같은 함수도 내부적으로 CancellationException을 던지고 있어서, try catch로 delay를 잡아버리면 cancel이 수행되지 않음

```kotlin
fun main(): Unit = runBlocking {
    val job = launch {
        try {
            delay(1_000L)
        } catch(e: CancellationException) {
            // 아무것도 안함
        }
        printWithThread("delay에 의해 취소되지 않았다")
    }
    delay(100L)
    printWithThread("취소 시작")
    job.cancel()
}
```



### 코루틴의 예외 처리와 Job의 상태 변화

![스크린샷 2023-12-10 오후 7.45.05](스크린샷%202023-12-10%20오후%207.45.05.png)

- runBlocking으로 만들어진 최 상단의 코루틴을 root 코루틴이라고 한다.



새로운 루트 코루틴을  만들고 싶을 땐 새로운 영역(CoroutineScope)를 만들어야한다.





```kotlin
// as-is
fun main(): Unit = runBlocking { 
    val job1 = launch {
        delay(1_000L)
        printWithThread("Job 1 ")
    }
    val job2 = launch {
        delay(1_000L)
        printWithThread("Job 1 ")
    }
}

// to-be (with CourotineScope(Dispatchers.Default))
fun main(): Unit = runBlocking {
    val job1 = CoroutineScope(Dispatchers.Default).launch {
        delay(1_000L)
        printWithThread("Job 1 ")
    }
    val job2 = CoroutineScope(Dispatchers.Default).launch {
        delay(1_000L)
        printWithThread("Job 1 ")
    }
}
```

Dispatchers.Default (main쓰레드가 아닌 다른 쓰레드)에서 실행시키는 새로운 코루틴 영역을 만들어서 launch로 실행시킴

각각이 루트 코루틴이 됨



**launch**

```kotlin
fun main(): Unit = runBlocking {
    val job = CoroutineScope(Dispatchers.Default).launch {
        throw IllegalArgumentException()
    }
    delay(1_000L)
}

////
Exception in thread "DefaultDispatcher-worker-1" java.lang.IllegalArgumentException
	at Lec05Kt$main$1$job$1.invokeSuspend(Lec05.kt:8)
```

**async**

```kotlin
fun main(): Unit = runBlocking {
    val job = CoroutineScope(Dispatchers.Default).async {
        throw IllegalArgumentException()
    }
    delay(1_000L)
    job.await()
}
////
Exception in thread "main" java.lang.IllegalArgumentException
	at Lec05Kt$main$1$job$1.invokeSuspend(Lec05.kt:8)
```

**launch와 async 예외 발생 차이**

- launch는 안에서 예외가 발생하면 예외를 터뜨리고 종료됨
- async: 예외가 발생해도 예외를 출력하지 않고 예외를 확인하려면 await이 필용한데, 이거는 출력하는 쓰레드에서 발생된 에러로 봄



**runBlocking의 자식 async로 예외를 터뜨릴 경우 자식 코루틴의 예외는 부모에게 전파된다.**

```kotlin
fun main(): Unit = runBlocking {
    val job = async {
        throw IllegalArgumentException()
    }
    delay(1_000L)
}
///
Exception in thread "main" java.lang.IllegalArgumentException
	at Lec05Kt$main$1$job$1.invokeSuspend(Lec05.kt:8)
```



**SupervisorJob**

```kotlin
fun main(): Unit = runBlocking {
    val job = async(SupervisorJob()) {
        throw IllegalArgumentException()
    }
    delay(1_000L)
}
```

SupervisorJob을 쓰면 자식코루틴의 예외를 부모코루틴에게 전파하지 않는다.



#### launch의 예외를 다루는 방법

기본적으로는 발생한 예외를 바로 던짐

1. try - catch- finally 

```kotlin
fun main(): Unit = runBlocking {
    val job = launch {
        try {
            throw IllegalArgumentException()
        } catch (e: IllegalArgumentException) {
            printWithThread("정상 종료")
        }
    }
}
```

2. CoroutineExceptionHandler 
   1. 공통 로직으로 **예외 발생 이후** 에러 로깅, 에러메시지 전송 등에 활용
   2. try catch는 그 launch쪽 로직이 예외가 발생하지 않은 것 처럼 처리하는거라면, 이거는 예외는 발생 했지만 처리를 다른곳에서 하는느낌

```kotlin
fun main(): Unit = runBlocking {
    val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        printWithThread("예외")
    }

    val job = CoroutineScope(Dispatchers.Default).launch(exceptionHandler) {
        throw IllegalArgumentException();
    }
    delay(1_000L)
}
```

**주의할점**

- launch에만 적용 가능하고, 부모 코루틴이 있으면 동작하지 않는다.



#### 취소와 예외 구분

- CASE1. 발생한 예외가 CancellationException 인 경우 취소로 간주하고 부모 코루틴에게 전파하지 않는다.
- CASE2. 그 외 다른 예외가 발생할 경우 실패로 간주하고 부모 코루틴에게 전파한다.
- 내부적으로는 취소나 실패 모두 취소됨으로 관리한다



**Job(코루틴)의 life Cycle**

```
NEW -> ACTIVE   - COMPLETING - COMPLETED
        예외발생 \  /  - 예외, 취소 모두 cancel로 간주
          CANCELLING -> CANCELLED
```

