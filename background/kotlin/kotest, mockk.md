## Kotest

코틀린 진영에서 가장 많이 사용되는 테스트 프레임워크

코틀린 DSL을 활용해 테스트코드를 작성할 수 있다.

- 다양한 테스트 레이아웃(StringSpec, FunSpec, BehaviorSpec 등 ) 제공
- Kotlin DSL 스타일의 Assertion 기능 제공



```bash
# kotlin dsl이란
Domain Specific Language : 특정 도메인에 국한해 사용하는 언어

범용 프로그래밍 언어인 C, C++ 등을 가지고 알고리즘을 푼다.
SQL언어는 DB (특정영역, 도메인)에 초점을 맞추고 기능을 제한

특징
- 선언적 문법 ( <-> 명령적 )
- 깔끔한 코드작성이 가능하다.
  ㄴ 해당 코드가 어떤 로직을 가지고 작업할지 명확하게 이해할 수 있다.
  
더 자세한것은 코틀린 dsl 찾아보기
```



### 시작하기

#### 의존성 추가

```groovy
test {
    useJUnitPlatform()
}

dependencies {
    testImplementation("io.kotest:kotest-runner-junit5:${Versions.KOTEST}")
    testImplementation("io.kotest:kotest-assertions-core:${Versions.KOTEST}")
}

---
  
tasks.named('test') {
    useJUnitPlatform()
}
testImplementation 'io.kotest:kotest-runner-junit5:5.0.3'
testImplementation 'io.mockk:mockk:1.13.3' 
```

- kotest와 함께 사용할 mockk도 추가해주었음
- 5.1.0버전 이상에서 코루틴 오류가 나는데 나중에 알아보기 



#### Kotest Testing Styles

- https://kotest.io/docs/framework/testing-styles.html#free-spec
- 되게 다양한 Spec이 존재해서 상황에 따라 사용하면 될 듯하다.
- 나는 보통 가볍게 테스트할 때 StringSpec, FunSpec, BehaviorSpec(BDD형태) 를 많이 쓴다.



#### 수명주기 / 전후 처리

- https://kotest.io/docs/framework/lifecycle-hooks.html#overriding-callback-functions-in-a-spec
- <u>beforeContainer</u>, <u>afterContainer</u>
  - Given으로 묶이는 그 아래까지 포함한 영역
- <u>beforeEach</u>, <u>afterEach</u>
  - Then영역
- <u>beforeTest</u>, <u>afterTest</u>
- <u>beforeAny,</u> <u>afterAny</u> 
- <u>beforeSpec</u>, <u>afterSpec</u>
  - 테스트 영역 전체를 Spec이라고 한다
  - XxxSpec을 구현하는 <u>({})</u> 영역
- 등..



Given, When, Then을 막 만들다보면 테스트가 엄청 많이 중첩으로 생성되어서, 이 수명주기에 따라서 전역으로 관리하거나 처리하면 편리해지는 것 같다. 



#### Assertion

- https://kotest.io/docs/assertions/assertions.html
- <u>shouldBe</u>
  - <u>should</u> 로 시작하는 것 이것저것 많다..
  - shouldHaveSize, shouldContain, ShouldBeGreaterThan ,, 
  - shouldThrow<>

- data driven testing 읽어보기
  - https://isntyet.github.io/kotlin/Kotest-%ED%95%B4%EB%B3%B4%EA%B8%B0/



kotest로 BDD

https://jaehhh.tistory.com/118





kotest의 수명주기 

https://velog.io/@effirin/Kotest%EC%99%80-BDD-%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%BD%94%EB%93%9C-%EC%9E%91%EC%84%B1%ED%95%98%EA%B8%B0-%EC%A0%84-%EC%9D%BD%EC%96%B4%EB%91%90%EB%A9%B4-%EC%A2%8B%EC%9D%84-%EC%9E%90%EB%A3%8C%EB%93%A4





BDD : BehaviorSpec



Given, When, Then, And를 사용해서 BDD 방식의 테스트코드를 작성할 수 있다.



이 때 shouldBe 같은 assert문이 아닌 verify로 메서드가 호출되는지를 검증할 때, 모든 테스트에 걸쳐서 호출 횟수를 세기때문에 테스트코드의 호출 순서에 따라서 verify문이 실패할 수 있다. (테스트끼리 독립적이지 않다)



그래서 테스트마다 mock을 clear하고 다시 mocking을 해줘야하는데  beforeEach, afterTest등을 이용해 수명주기에 따라서 해당 동작을 전역적으로 관리할 수 있다.





justRun - 응답이 없는 경우

every - 응답이 있는 경우

(Junit과 비교해보기 )







참고할 링크

- https://www.youtube.com/watch?v=PqA6zbhBVZc&t=519s



출처

- kotest https://techblog.woowahan.com/5825/

- 수명주기 관련 https://velog.io/@effirin/Kotest%EC%99%80-BDD-%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%BD%94%EB%93%9C-%EC%9E%91%EC%84%B1%ED%95%98%EA%B8%B0-%EC%A0%84-%EC%9D%BD%EC%96%B4%EB%91%90%EB%A9%B4-%EC%A2%8B%EC%9D%84-%EC%9E%90%EB%A3%8C%EB%93%A4