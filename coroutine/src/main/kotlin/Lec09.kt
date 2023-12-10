import kotlinx.coroutines.*
import kotlinx.coroutines.future.await
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

class Lec09 {
}

suspend fun main() {
    val service = UserService()
    println(service.findUser(1L, null))
}
interface Continuation {
    // 라벨을 가지고 있을 것

    // suspend fun에서 불릴 애들. callback
    suspend fun resumeWith(data: Any?)
}

private abstract class FindUserContinuation() : Continuation {
    var label = 0
    var profile: Profile? = null
    var image: Image? = null
}
class UserService {
    private val userProfileRepository = UserProfileRepository()
    private val userImageRepository = UserImageRepository()

    suspend fun findUser(userId: Long, continuation: Continuation?): UserDto {
        // state machine
        val sm = continuation as? FindUserContinuation ?: object : FindUserContinuation() {
            // 일종의 재귀함수
            override suspend fun resumeWith(data: Any?) {
                when (label) {
                    0 -> {
                        profile = data as Profile
                        label = 1
                    }
                    1 -> {
                        image = data as Image
                        label = 2
                    }
                }
                findUser(userId, this)
            }
        }

        when (sm.label) {
            0 -> {
                // 0단계 - 초기 시작
                println("프로필을 가져오겠습니다")
                val profile = userProfileRepository.findProfile(userId, sm)
            }
            1 -> {
                // 1단계 - 1차 중단 후 재시작
                println("이미지를 가져오겠습니다")
                val image = userImageRepository.findImage(sm.profile!!, sm)
            }
        }
        // 2단계 - 2차 중단 후 재시작
        return UserDto(sm.profile!!, sm.image!!)
    }
}

class UserProfileRepository {
    suspend fun findProfile(userId: Long, continuation: Continuation){
        delay(100L)
        continuation.resumeWith(Profile())
    }
}

class UserImageRepository {
    suspend fun findImage(profile: Profile, continuation: Continuation){
        delay(100L)
        continuation.resumeWith(Image())
    }
}

data class Profile(
    val value: String = ""
)

data class Image(
    val value: String = ""
)

data class UserDto (
    val profile: Profile,
    val image: Image
)