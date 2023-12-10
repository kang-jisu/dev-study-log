import kotlinx.coroutines.*
import java.util.concurrent.Executors

class Lec0607 {
}

fun main() {
    // 하나의 쓰레드를 가진 executor
    val threadPool = Executors.newSingleThreadExecutor()
    CoroutineScope(threadPool.asCoroutineDispatcher()).launch {
        printWithThread("새로운 코루틴")
    }
}
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
suspend fun lec07Example2() {
    val job = CoroutineScope(Dispatchers.Default).launch {
        delay(1_000L)
        printWithThread("Job1")
    }
    job.join()
}

fun lec07Example1() {
    CoroutineScope(Dispatchers.Default).launch {
        delay(1_000L)
        printWithThread("Job1")
    }
    Thread.sleep(1_500L)
}

fun lec06example(): Unit = runBlocking {
    launch {
        delay(600L)
        printWithThread("Job 1 ")
    }
    launch {
        delay(500L)
        throw IllegalArgumentException("코루틴 실패!")
    }
}