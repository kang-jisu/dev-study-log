import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class Lec03 {
}

fun example1() {
    runBlocking {
        printWithThread("START")
        launch {
            delay(2_000L) // 특정 시간만큼 멈춘 후 yield()실행
            printWithThread("LAUNCH END")
        }
    }

    printWithThread("END")
}

fun example2(): Unit = runBlocking {
    val job = launch(start = CoroutineStart.LAZY) {
        printWithThread("Hello launch")
    }
    delay(1_000L)
    job.start()
}

fun example3(): Unit = runBlocking {
    val job = launch {
        (1..5).forEach {
            printWithThread(it)
            delay(500)
        }
    }
    delay(1_000L)
    job.cancel()
}

fun example4(): Unit = runBlocking {
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

fun example5(): Unit = runBlocking {
    val job = async {
        3 + 5
    }
    val await: Int = job.await() // await : async의 결과를 가져오는 함수
    printWithThread(await)
}

fun example6(): Unit = runBlocking {
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
