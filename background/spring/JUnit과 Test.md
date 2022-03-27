# JUnit 

```
xUnit이란 언어마다 단위테스트를 위한 프레임워크가 존재하는데 보통 xUnit이라 칭한다.
Java - JUnit
Python - PyUnit
```



## JUnit

JUnit이란 Java의 단위 테스팅 도구이다. 단 하나의 jar파일로 되어있으며 Test결과를 문서가 아닌 Test Class 그대로 남김으로써 추 후 개발자에게 테스트 방법 및 클래스의 Histoy를 넘겨줄 수 있다. 



### 특징

- 단위테스트 Framework중 하나
- 단정문으로 테스트케이스의 수행 결과를 판별
  - https://junit.org/junit5/docs/current/api/org.junit.jupiter.api/org/junit/jupiter/api/Assertions.html
  - `org.junit.jupiter.api`
  - `assertEquals`,`assertTrue`, `assertNotNull` 등 
    - 이것보다 assertThat을 사용하는것을 추천 (hamcrest, **assertJ**)
- Annotation으로 간결하게 사용 가능 (JUnit4부터) 
  - `@Test` , `@DisplayName`, `@BeforeEach`,`@BeforeAll`

### 기타

- spring-boot-start-test
  - JUnit, SpringTest, AssertJ, Hamcrest, Mockito, JSONassert, JsonPath 내장되어있음 



### JUnit4 vs JUnit5

|              | JUnit4                    | JUnit5                                                       |
| ------------ | ------------------------- | ------------------------------------------------------------ |
| java version | java5이상                 | java8, gradle 4.7이상<br />Spring 2.2.x버전 이후부터는 JUnit5기본으로 사용 |
| architecture | all in one                | JUnit Platform, Jupiter, Vintage<br />JUnit Platform : JVM에서 테스트 프레임워크를 시작하기 위한 기반 역할, API정의, 콘솔 실행기, 테스트 엔진 <br />JUnit Jupiter : JUnit5에서 테스트 및 확장을 작성하기 위한 새로운 프로그래밍 모델과 확장모델의 조합 <br />JUnit Vintage : Junit3,4기반 테스트를 실행하기 위함(하위 호환성) |
| Each         | @Before, @After           | @BeforeEach, @AfterEach                                      |
| All          | @BeforeClass, @AfterClass | @BeforeAll, @AfterAll                                        |
| @Test        | public선언되어야함        | 기본적으로 public으로 간주                                   |
| @DisplayName | X                         | 사용자 정의 이름을 표시 목적으로 지정할 수 있음.             |
| Assert       | Assert 클래스             | Assertions 클래스 , 모두 static<br />`org.junit.jupiter.api.Assertions` |
|              | @RunWith                  | @ExtendWith<br />메타어노테이션 사용시 ExtendWith생략 가능 ( @SpringBootTest, @WebMvcTest에 내장되어있음 ) |
|              |                           |                                                              |





### 사용

**초기화**

```java
@BeforeEach
public void setUp(){
  // 각각 테스트 메소드가 실행되기 전에 실행되어야하는 메소드 
  // JUnit의 @Before
  // 목업데이터 셋팅
}

@BeforeAll
public void beforeAll(){
  // 테스트가 시작하기 전에 딱 한번만 실행 
}
```

**해제**

```java
@AfterEach
public void tearDown() {
  // 테스트 메소드가 실행되고 난 후 실행 
  // JUnit4의 @After
}

@AfterAll
public void afterAll(){
  // 테스트가 완전히 끝난후 딱 한번만 실행
}
```

**테스트**

```java
@Test
public void test(){
  //test 
}
```

**테스트 메소드에 Exception지정**

```java
@Test(expected=RuntimeException.class)
public void testException(){
  //test runtimeException
}
```

**사용하지 않음표시**

```java
@Disabled
public void test(){
  // Junit4의 @Ignore
  // 이 어노테이션이 붙으면 테스트가 실행되지 않음
}
```

**전체 라이프 사이클**

```
BeforeAll->BeforeEach->Test->AfterEach->AfterAll
```



## JUnit assert vs Hamcrest vs AssertJ

**AssertJ**

자바 JUnit의 테스트코드에 사용되어 테스트코드의 가독성과 편의성을 높여주는 라이브러리  

- 메서드 체이닝 지원하여 직관적이고 읽기 쉬운 테스트코드 작성가능
  - 기존 assert 메서드는 메서드 안에 인자로 actual과 expect를 넣어야하고 인자 순서가 헷갈리는 등 사용이 불편함 ..
- 테스트에 필요한 풍부한 메소드들을 지원함
- import 하나의 static class를 가지고 모든 단언 포함 
  - `import static org.assertj.core.api.Assertions.*;`
- 모든 테스트코드는 assertThat()으로 시작 

- spring-boot-starter-test에 포함되어있음 

  

**Hamcrest**

- import 각기 다른 class를 가짐 
- assertions메소드 안에 인자로 있음 
- matcher를 알고있어야함



**JUnit 기본 asserThat**

```java
org.junit.Assert.assertThat 
public static <T> void assertThat(T actual,
                                  org.hamcrest.Matcher<T> matcher)
```

actual인자에 검증대상을 넣고 비교하는 로직을 주입받아 검증단계에서 수행한다. Junit의 assertThat은 hamcrest의 구현된 matcher를 사용하도록 강제하고있다. 

**assertJ의 assertThat**

```java
public static AbstractAssert<SELF,T> assertThat(T actual)
```

인자로 검증대상만 받아 actual타입에 따라 assert클래스를 반환(SELF)하여 체이닝방식으로 테스트 로직 작성 가능 



```java
assertThat(a, equalTo(b)); //Hamcrest
assertThat(a).isEqualTo(b); //AssertJ
```

```java
assertThat("Error", a, equalTo(b)); //Hamcrest
assertThat(a).isEqualTo(b).overridingErrorMessage("Error"); //AssertJ
```

```java
//Hamcrest
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

//Assertj
import static org.assertj.core.api.Assertions.*;
```



## JUnit 통합테스트 @SpringBootTest

`@SpringBootTest`이용   

- 스프링부트에서는 이 어노테이션을 통해 애플리케이션 테스트에 필요한 거의 모든 의존성을 제공해준다. @SpringBootApplication을 찾아가 하위의 모든 Bean을 스캔하고 Test용 ApplicationContext를 만들면서 빈을 등록해주고 mock bean을 찾아 그 빈만 mockbean으로 교체해줌 

- Junit4 사용시 `@RunWith` 필수. Junit5부터는 `@..Test`에 `@ExtendWith`내장 
- MOCK(기본)
  - `ApplicationContext` 로드하고 모의 웹 환경 제공 , 내장 서버 시작되지 않음
  - 실제 서블릿 컨테이너를 띄우지 않고 서블릿 컨테이너를 mocking한것이 실행되어 보통 MockMvc를 주입받아 테스트
    - `MockMvcRequestBuilders` import  
- RANDOM_PORT
  - 실제 웹 환경 로드, 내장 서버 시작되고 임의의 포트에서 수신 대기
  - `@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)`
  - `WebClient`또는 `TestRestTemplate`을 주입받아 테스트 
- 테스트가 `@Transactional`인 경우 기본적으로 각 테스트 메서드가 끝날 때 트랜잭션을 롤백. RANDOM_PORT를 사용하면 암시적으로 실제 서블릿 환경을 사용하므로 HTTP클라이언트와 별도의 스레드에서 실행되고 별도의 트랜잭션으로 실행됨. 그래서 서버에서 시작된 트랜잭션이 롤백되지 않음



**MockMvc**

@SpringBootTest는 기본적으로 서버를 시작하지 않기 때문에 모의환경에 대해 테스트하려는 웹 엔드포인트가 있는경우 MockMvc사용 

```java
@RunWith(SpringRunner.class) 
@SpringBootTest 
@AutoConfigureMockMvc
 public class MockMvcExampleTests {

	@Autowired
	 개인 MockMvc mvc;

	@Test
	 public  void exampleTest() 는 예외를 던집니다 . {
		 this .mvc.perform(get( "/" )).andExpect(status().isOk())
				.andExpect(content().string( "Hello World" ));
	}

}
```



### Mock 객체

테스트시 모든 가변적인 영역(애플리케이션 서버, 미들웨어, 데이터베이스)는 관리하기 어려우며 상호작용시 단위 테스트 범위를 벗어난다. **비즈니스 로직을 테스트할 목적**이라면 의존성들을 모두 테스트 할 필요는 없으므로 Mock객체를 사용해 테스트환경에서 필요한 의존성을 대체하여 비즈니스로직 테스트를 실행할 수 있다.

**관련 어노테이션**

- `@Mock`
  - mock객체를 만들어 반환
  - 실제 인스턴스 없이 가상의 mock인스턴스를 직접 만들어 사용
- `@Spy`
  - spy객체를 만들어 반환
  - 실제 인스턴스를 사용해서 mocking하여 객체의 행위를 지정하지 않으면 객체를 만들 때 사용한 실제 인스턴스의 메서드를 호출함
- `@InjectMocks`
  - @Spy, @Mock 어노테이션이 붙은 객체가 @InjectMocks이 붙은 객체의 멤버 클래스라면 주입시킴
  - e.g. @InjectMocks(Service) , @mock(DAO)
- `@MockBean`
  - @SpringBootTest에서 ApplicationContext에 mock객체를 추가 
  - Mock객체들을 Spring의 ApplicationContext에 넣어주고, 동일한 타입이 존재할 경우 MockBean으로 교체해준다.

**@MockBean vs @Mock**

테스트도중 Spring에서 어느 의존성도 필요하지 않다면 Mockito의 Mock을 사용하고, Spring Container가 관리하는 bean중 하나 이상을 Mocking하고 싶다면 MockBean을 사용하면 된다.

| Mock 종류 | 의존성 주입 Target |                                                              |
| --------- | ------------------ | ------------------------------------------------------------ |
| @Mock     | @InjectMocks       | @Mock은 @InjectMocks에 대해서만 해당 클래스 안에 정의된 객체를 찾아 의존성을 해결 |
| @MockBean | @SpringBootTest    | @MockBean은 mock객체를 스프링 컨텍스트에 등록해 SpringBootTest를 통해 @Autowired에 의존성이 주입됨 |



## JUnit 슬라이스테스트

**슬라이스테스트를 하는 이유** 

- 실제 구동되는 어플리케이션의 설정과 모든 Bean을 다 로드하기 때문에 무겁다
- 테스트 단위가 커 디버깅이 어렵다

**주의**

- 하위 레이어를 Mock하기때문에 실제 환경에서는 제대로 동작하지 않을 수도 있다. 



전체를 다 ApplicationContext에 올리지 않고 레이어 별로 잘라서 테스트하고싶을 때 사용

```java
@JsonTest
@WebMvcTest
@DataJpaTest
@RestClientTest 등 
```

### @WebMvcTest

SpringMVC 컨트롤러 테스트

```java
// 스캔 대상 
@Controller, @ControllerAdvice, @JsonComponent, @Converter, @GenericConverter, Filter, WebMvcConfigurer, HandlerMethodArgumentReolver, @Component 
```

- `@MockMvc`로 HTTP서버를 시작할 필요 없이 빠르게 테스트
- `@MockBean`으로 의존대상 모의 구현 
- 테스트할 특정 컨트롤러 명시 

  

**Controller Test - MockMVC 이용**

유효성 검사 및 컨트롤러 동작 여부를 위한 테스트

- ```
  org.springframework.test.web.servlet.request
  ```

  - `contentType()`
  - `content()`
  - `header()`
  - `param()`
  - `cookie()`

- static 

  ```
  org.springframework.test.web.servlet.result
  ```

  - `status()`
  - `header()`
  - `content()`
  - `jsonPath()`

### @DataJpaTest

JPA 어플리케이션 테스트.기본적으로 @Entity를 검색하고 SpringDataJpa 저장소를 구성한다.   

- 기본적으로 @Transactional이 적용되어있어 테스트가 끝날 때 롤백된다. 
- 테스트용으로 `TestEntityManager` 주입받아 사용
- 인메모리 데이터베이스를 사용하는데 실제 데이터베이스로 테스트하고 싶은 경우 `@AutoConfigureTestDatabase`로 설정
- 기본 설정에서는 persist는 되지만 insert 쿼리 실행되지 않음 (commit시점에 flush되는데 롤백되므로 )



# TDD(Test Driven Development)

반복 테스트를 이용한 소프트웨어 방법론으로, 작은 단위의 테스트케이스를 작성하고 이를 통과하는 코드를 추가하는 단계를 반복하여 구현하는 방법이다. 

1. RED(실패하는 테스트코드 작성)

2. Green(TC를 성공하기 위한 실제 코드 작성)

3. Yellow(리팩토링 수행)

```java
BDD(Behaviour driven development) 와의 차이
  - TDD는 테스트코드를 이용한 구현에 초점
  - BDD는 TDD를 수행하려는 어떠한 행동과 기능을 개발자가 더 이해하기 쉽게 하는것이 목적
  - given/when/then구조(A상태에서 출발해 상태 변화를 가했을 때 기대하는 상태로 완료되어야함)로 테스트를 작성 
  - given : 테스트를 위해 주어진 상태, 조건, 환경
  - when : 테스트 대상에게 가해진 상태, 조건, 상태변경 환경
  - then : 결과 
```



## 단위 테스트의 F.I.R.S.T 원칙

### F - Fast (빠르게)

테스트코드를 실행하는 일이 오래걸리면 안된다.

### I - Independent (독립적으로)

각 테스트는 서로 의존하면 안된다. 테스트가 서로에게 의존하면 하나가 실패할 때 나머지도 잇달아 실패하므로 원인을 진단하기 어려워진다.

### R - Repeatable (반복가능하게)

테스트는 어떤 환경에서도 반복 가능해야한다.
반복 가능한 테스트는 외부 서비스나 리소스같은 항상 사용가능하지 않은 것에 의존하지 않는다.

### S - Self Validating (자가 검증하는)

테스트는 bool값으로 결과를 내야한다. 성공 아니면 실패다.

### T - Timely (적시에)

테스트는 적시에 작성해야한다. 실제 코드를 구현하기 직전에 구현한다.



## Mockito

```java
TestDouble
- xUnit 테스트를 진행할 때 연관된 객체를 사용하기 어렵고 모호할 때 대신해줄 수 있는 객체 

종류
  - Dummy : 객체만 필요하고 기능은 필요하지 않은 경우 
  - Fake : 복잡한 로직을 단순하게 구현 
  - Stub : Dummy가 실제로 동작하는 것 처럼 보이게 만들어놓음 , 최소한의 구현과 요청에 대한 미리 준비해둔 결과
  - Spy : Stub의 역할을 가지면서 약간의 정보를 기록 
  - Mock : 호출에 대한 기대를 명세하고 내용에 따라 동작하도록 프로그래밍된 객체
```

Mock객체를 만들어주는 프레임워크로 테스트하는데 유용하다.
목 객체란 테스트를 진행할 때 해당 코드 외에 의존하는 객체를 가짜로 만드는 것을 지칭한다. 테스트 코드하고 싶은 코드에 대해서 정확하게 테스트하기 위해서 사용한다.  

스프링에서 DI를 통해 객체간 의존성을 관리해주는데 테스트를 할 때는 의존성을 가지는 객체에 의해 테스트 결과가 영향받을 수 있기 때문에 의존을 가지는 객체를 Mock하여 우리가 원하는 동작만 하도로함. 





**Mockito vs BDDMockito**

- Mockito는 BDD의 given/when/then구조가 `when`, `thenReturn`, `verify`등의 메서드를 이용해서 이름이 헷갈림 
- BDDMockito는 `given`, `then`이 의미 그대로를 내포해서 이해하기 더 편리함 
  - BDDMockito는 Mockito를 상속받은 클래스 

### BDDMockito

```java
import static org.mockito.BDDMockito.*;
```



**Mockito**

```java
//given
when(phoneBookRepository.contains(momContactName))
  .thenReturn(false);
 
//when
phoneBookService.register(momContactName, momPhoneNumber);
 
//then
verify(phoneBookRepository)
  .insert(momContactName, momPhoneNumber);
```

**BddMockito**

```java
given(phoneBookRepository.contains(momContactName))
  .willReturn(false);
 
//when
phoneBookService.register(momContactName, momPhoneNumber);
 
then(phoneBookRepository)
  .should()
  .insert(momContactName, momPhoneNumber);
```



**예외 발생**

```java
given(phoneBookRepository.contains(xContactName))
  .willReturn(false);
willThrow(new RuntimeException())
  .given(phoneBookRepository)
  .insert(any(String.class), eq(tooLongPhoneNumber));

try {
    phoneBookService.register(xContactName, tooLongPhoneNumber);
    fail("Should throw exception");
} catch (RuntimeException ex) { }

then(phoneBookRepository)
  .should(never())
  .insert(momContactName, tooLongPhoneNumber);
```





- 출처

  - 공식문서

    - [JUnit5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
    - https://joel-costigliola.github.io/assertj/
    - [스프링 공식문서 테스트](https://docs.spring.io/spring-boot/docs/2.1.5.RELEASE/reference/html/boot-features-testing.html)
    - [baeldong](https://github.com/eugenp/tutorials/tree/master/testing-modules/mockito)

  - 블로그

    - https://galid1.tistory.com/476
    - https://dzone.com/articles/hamcrest-vs-assertj-assertion-frameworks-which-one
    - [JUnit, AssertJ](https://bibi6666667.tistory.com/231)

    - [스프링부트활용-테스트](https://velog.io/@dsunni/Spring-Boot-%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-%ED%99%9C%EC%9A%A9-%ED%85%8C%EC%8A%A4%ED%8A%B8)

    - [테코블 슬라이스테스트](https://tecoble.techcourse.co.kr/post/2021-05-18-slice-test/)
    - [테코블 테스트더블](https://tecoble.techcourse.co.kr/post/2020-09-19-what-is-test-double/)
    - [Mock,MockBean,Spy등 차이점](https://cobbybb.tistory.com/16)
    - [TDD vs BDD](https://wonit.tistory.com/493)



