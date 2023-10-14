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



