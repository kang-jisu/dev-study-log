import kotlinx.coroutines.*

class Lec04 {
}

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

fun lec04Example2(): Unit = runBlocking {
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

fun lec04Example1(): Unit = runBlocking {
    val job1 = launch {
        delay(1_000L)
        printWithThread("Job 1")
    }

    val job2 = launch {
        delay(1_000L)
        printWithThread("Job 2")
    }

    delay(100)
    job1.cancel()
}