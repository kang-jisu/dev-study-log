import kotlinx.coroutines.*
import kotlinx.coroutines.future.await
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

class Lec08 {
}

interface AsyncCaller {
    suspend fun call()
}

class AsyncCallerImpl: AsyncCaller {
    override suspend fun call() {
        TODO("Not yet implemented")
    }
}

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

