import kotlinx.coroutines.*

class Lec05 {
}


fun main(): Unit = runBlocking {
    val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        printWithThread("예외")
    }

    val job = CoroutineScope(Dispatchers.Default).launch(exceptionHandler) {
        throw IllegalArgumentException();
    }
    delay(1_000L)
}


fun lec05Example06(): Unit = runBlocking {
    val job = launch {
        try {
            throw IllegalArgumentException()
        } catch (e: IllegalArgumentException) {
            printWithThread("정상 종료")
        }
    }
}

fun lec05Example05(): Unit = runBlocking {
    val job = async(SupervisorJob()) {
        throw IllegalArgumentException()
    }
    delay(1_000L)
}

fun lec05Example04(): Unit = runBlocking {
    val job = async {
        throw IllegalArgumentException()
    }
    delay(1_000L)
}

fun lec05Example03(): Unit = runBlocking {
    val job = CoroutineScope(Dispatchers.Default).async {
        throw IllegalArgumentException()
    }
    delay(1_000L)
    job.await()
}

fun lec05Example02(): Unit = runBlocking {
    val job = CoroutineScope(Dispatchers.Default).launch {
        throw IllegalArgumentException()
    }
    delay(1_000L)
}
fun lec05Example1(): Unit = runBlocking {
    val job1 = CoroutineScope(Dispatchers.Default).launch {
        delay(1_000L)
        printWithThread("Job 1 ")
    }
    val job2 = CoroutineScope(Dispatchers.Default).launch {
        delay(1_000L)
        printWithThread("Job 1 ")
    }

    delay(2_000L)
}