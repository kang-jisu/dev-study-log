## JVM

Java Virtual Machine의 약자로, 자바언어로 작성된 프로그램을 돌아가도록 만들어주는 프로그램이다.

운영체제에 맞는 실행파일로 컴파일 되는 것이 아닌, 자바 언어로 작성된 파일(.java)를 .class확장자를 가진 파일로 변환되어 JVM위에서 .class파일을 작동시킨다. 

미리 설치된 JVM은 운영체제 별로 동일하게 작동하도록 .class파일에게 환경을 제공한다.



Java의 첫번째 장점 

- 높은 이식성 ( 운영체제에 상관 없이 JVM이 구동될 수 있는 환경이라면 Java로 작성한 프로그램을 실행시킬 수 있다. )



## JRE

우리가 만든 프로그램이 원활하게 작동하기 위해서는 코드에 작성된 내용을 JVM위에서 실행시키는 것 외에도, 몇가지 요소가 필요하다.

이를 제공해주는 것이 JRE, Java Runtime Environment이다. 

JRE는 크게 JVM, Java Class Libraries, Class Loader라는 요소로 구성이 된다. 

### - Java Class Libraries

- 자바를 실행시키는데 필수적인 라이브러리
- `java.io` , `java.util`, `java.thread` 등 작동에 필수적인 요소들을 가지고 있다.

```java
//Java 코드
public class Main {
  public static void main(String[] args) {
    int a = 1;
    System.out.println(a);
  }
}
```

```java
//바이트코드
... 중략 ...
  L0
    LINENUMBER 3 L0
    ICONST_1
    ISTORE 1
   L1
    LINENUMBER 4 L1
    GETSTATIC java/lang/System.out : Ljava/io/PrintStream;
    ILOAD 1
    INVOKEVIRTUAL java/io/PrintStream.println (I)V
   L2
    LINENUMBER 5 L2
    RETURN
   L3
    LOCALVARIABLE args [Ljava/lang/String; L0 L3 0
    LOCALVARIABLE a I L1 L3 1
    MAXSTACK = 2
    MAXLOCALS = 2
...
```

JVM에 올라가는 클래스 파일의 바이트 코드에서도 필수 라이브러리를 참고하고 있는게 보인다. 즉 간단한 코드라도 필수 라이브러리가 필요하다는 의미이다. 

### - Class Loader

클래스 로더는 필요한 클래스들을 JVM 위로 올려주는 역할을 한다.



JRE의 구성요소들을 통해서, JVM은 원활히 작동할 수가 있다.

```bash
- 개발자 : Java 코드 작성
- ? 
- JVM : .class파일 실행
- JRE : 개발(코드 작성)이 아닌, 프로그램 구동에 집중 , JVM과, JVM이 필요한 클래스들을 JVM으로 올려주는 역할을 하는 Class Loader와  .class파일 실행시키는데 필요한 라이브러리를 제공해주는 Java Class Libraries
```

 지금까지 나온 내용을 정리하면 이런데, 그럼 java코드를 .class파일로 어떻게 변환시킬까 ?



## JDK

Java Development Kit의 약자로 Java를 활용하여 프로그램을 개발할 때 필요한 도구 모음이다. 

실제로 프로그램을 실행시켜보아야 하기 때문에 (->.class -> 로더 -> (클래스)-> JVM : JRE) JRE가 포함되어있다. 

JDK만 설치해도 JRE가 자동으로 설치되는 것을 알 수 있다. 그렇다면 JDK는 JRE외에 어떤 요소들로 구성되어있을 까?

- Java로 이루어진 코드를 클래스 파일로 컴파일하는 javac 기능
- 작성된 코드를 디버깅하는 jdb 기능

JDK에서도 개발과 실행에 필요한 환경 및 기능 제공의 폭에 따라 표준형인 SE (standard edition)과 여러 기능이 추가된 EE (Enterprise Edition)으로 나뉜다. 

이 외에도 출시된 순서에 따라서 숫자로 버전을 구분하는 방법도 있다.

### - JDK8

Java의 최초 LTS ( Long Term Support)버전으로 Sun이 Oracle에 인수되고 나서 출시되었다. 그 후 많이쓰이는 11버전도 LTS이다. 다음은 17버전이 예정되어있다. 



openJDK : 오픈소스로 운영되며 상업적으로 사용해도 무료이고, Linux배포판에서 저장소에 기본적으로 등록되어있다. 

oracleJDK : Oracle에서 관리하는 JDK로 상업적으로 사용하려면 비용을 지불해야하고 지원을 많이 해준다. 

- 출처 테코블 JVM에 관하여 - Part1 JVM, JRE, JDK https://tecoble.techcourse.co.kr/post/2021-07-12-jvm-jre-jdk/