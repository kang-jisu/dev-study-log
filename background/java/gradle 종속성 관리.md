JVM 프로젝트의 종속성 관리



모듈 종속성

- 현재 빌드 외부에 빌드로 된 특정 버전이 있는 모듈에 대한 종속성을 나타낸다.



종속성 구성

- dependencies 선언
  - 플러그인은 구성을 사용하여 빌드 작성자가 플러그인에 의해 정의된 작업을 실행하는 동안 다양한 목적에 필요한 다른 하위 프로젝트 또는 외부 아티펙트를 쉽게 선언할 수 있도록 한다.
- 종속성 해결
  - 구성을 사용하여 정의하는 작업에 대한 입력을 찾고 가능하면 다운로드함
  - Gradle은 Maven Central에서 Spring 웹 프레임워크 jar파일을 다운로드 해야한다.
- 아티팩트 노출
  - 플러그인은 다른 프로젝트에서 사용하기 위해 생성하는 아티팩트를 정의한다.



- implementation
  - 프로젝트의 프로덕션 코드를 컴파일하는데 필요한 종속성
  - 프로젝트에 의해 API의 일부가 노출되지 않음
- api
  - 프로젝트의 프로덕션 코드를 컴파일하는데 필요한 종속성
  - 프로젝트에 의해 API의 일부가 노출됨
  - Guava를 사용하고 메서드 서명에서 Guava 클래스가 있는 공용 인터페이스를 노출함

- compile(3.0부터는 deprecated됨)





출처

- https://docs.gradle.org/current/userguide/dependency_management_for_java_projects.html#sec:configurations_java_tutorial







api와 compile은 같은 역할을 함

api를 통해 라이브러리를 가져올 경우 라이브러리가 적용되는 범위 때문



api, compile의 적용 범위

모듈에서 api나 compile을 사용해 라이브러리를 가져오게 되면, 해당 라이브러리는 해당 모듈을 의존하는 모듈에도 가져와진다.



Module B.   module A.   

api(Module A).  <-   api(Library). <-  LIbrary

api로 module A를 가져온 module B는 module A와 module A가 가지고있는 Library까지 가져옴



이것은 프로그램 유지보수성 측면에서 매우 좋지 않다. Module을 사용할 때는 module의 인터페이스만이 외부에 노출되어야 하는데, Library의 인터페이스까지 같이 노출 되기 때문이다. 

그리고 Module A를 빌드하면서 이미 들어간 Library가 중복으로 Module B에 추가되어 memory Leak이 나타날 수 있다.



클린 아키텍처 측면에서 Entity 모듈은 비즈니스 로직을 만드는 어플리케이션의 Usecase 모듈에서만 사용되고, Usecase 모듈은 controller 모듈에서만 사용되는 구조로 이루어져있어 레이어별 분리가 확실하게 일어나는데, Controller에서 Entity 모듈을 알게된다면 모듈 계층을 건너뛴 참조가 일어날 수 있어 레이어를 나눈 이유가 없어지게된다!! 오호



이를 해결하기 위해서 한 모듈에서 사용하는 라이브러리는 그 모듈에서만 사용될 수 있도록 implementation을 써야 한다.



implementation의 적용범위

모듈에서 implementation을 사용해 가져오는 라이브러리는 해당 모듈을 의존하는 모듈에느 ㄴ가져와지지 않는다.

moduleA에서는 library를 가져오지만, module B에서는 ㄴmoduleA만 가져온다. 

https://kotlinworld.com/317 굿!!





빌드의 관점에서 보면

![스크린샷 2023-06-20 오전 12.30.19](/Users/jskang/Library/Application Support/typora-user-images/스크린샷 2023-06-20 오전 12.30.19.png)

A를 의존하고 있는 모듈이 B, C라고 하면 

compile, api를 사용할 경우 A모듈을 수정하게 되면 이 모듈을 직접 혹은 간접으로 의존하고 있는 B와 C는 모두 재빌드되어야한다.



implementation을 사용한 경우 A라는 모듈을 수정하게 되면 이 모듈을 직접 의존하고있는 B만 재 빌드한다.



java-library 플러그인을 쓰면 api를 쓸 수 있고, java plugin만 추가하면api 못씀

gradle 3.4에서 api, implemntation 처음 구분하고있음



출처

- https://bluayer.com/13



java plugin 과 java library plugin의 차이는 API가 노출되는지에 대한 개념에 있다.

library는 다른 컴포넌트에 의해 소비되는 컴포넌트를 의미하고, 멀티 프로젝트 빌드에서 자주 사용된다. 외부 의존성을 가지게 된다.

이 플러그인은 두개의 configurations을 노출한다. api 와 implementation

api는 라이브러리 API에서 내보낸 종속성을 선언하고 , implementation은 내부에 있는 종속성을 선언하는데 사용해야 한다.



api는 소비자의 컴파일 클래스 경로에 나타난다.

implementation은 소비자에게 노출되지 않는다.

- 실수로 전이된 종속성에 의존하지 않는다.
- 컴파일 속도가 빨라진다.
- 의존성 구현이 변경될 때 다시 컴파일 할 필요가 없다.
- compile runtime은 gradle 7.0에서 제거되었다. 



- 출처
  - https://docs.gradle.org/current/userguide/java_library_plugin.html#sec:java_library_separation



멀티모듀 ㄹ만드릭

java 프로젝트 gradle로 생성하고 

루트프로젝트는 그냥 모듈 이어주는 역할만 할거니까 src 삭제하고

new - module로 ㅁ나들어주기

settings.gradle에 rootProejct.name이랑 include {모듈명} 해주기