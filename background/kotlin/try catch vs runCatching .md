출처

티스토리 - Kotlin Exception 처리 방법 #RunCatching과 try, catch

https://developer88.tistory.com/entry/RunCatching%EC%9D%84-%EC%9D%B4%EC%9A%A9%ED%95%9C-Kotlin-Exception%EC%B2%98%EB%A6%AC-try-catch



### 기본 try catch finally

```kotlin
try {
  if (condition) {
    
  }
  // do something
} catch (e: NullPointerException) {
  e.printStackTrace()
} finally {
 // do close resources 
}
```



### runCatching

- 코틀린 1.3 버전에서부터 제공하는 api

```kotlin
runCatching {
  k ?: throw NullPointerException("")
}.onSuccess {
  // success
}.onFailure { e -> 
  e.printStackTrace()
}.also {
  // finally에 들어있는 구문
}
```

