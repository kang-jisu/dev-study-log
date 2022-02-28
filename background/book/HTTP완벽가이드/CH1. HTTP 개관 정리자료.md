# HTTP 개관

전 세계의 웹 브라우저, 서버, 웹 애플리케이션은 모두 HTTP(HyperText Transfer Protocol)을 통해 서로 대화한다. HTTP는 현대 인터넷의 공용어이다. 

1장의 내용

- 얼마나 많은 클라이언트와 서버가 HTTP를 이용해 통신하는지
- 리소스(웹 컨텐츠)가 어디서 오는지
- 웹 트랜잭션이 어떻게 동작하는지
- HTTP 통신을 위해 사용하는 메시지의 형식
- HTTP 기저의 TCP 네트워크 전송
- 여러 종류의 HTTP 프로토콜
- 인터넷 곳곳에 설치된 다양한 HTTP 구성요소

---



## 1. HTTP: 인터넷의 멀티미디어 배달부

- HTTP는 신뢰성 있는 데이터 전송 프로토콜을 사용
  - **HTTP는 신뢰성 있는 데이터 전송 프로토콜을 사용**하기 때문에, 사용자는 인터넷에서 얻은 정보가 손상된게 아닌지 염려하지 않아도 된다. 애플리케이션 개발자도 데이터가 전송 중 파괴되거나, 중복되거나, 왜곡되는 것, 즉 인터넷의 결함이나 약점에 대한 **걱정 없이 애플리케이션 고유의 기능을 구현하는데 집중할 수 있다.** 



## 2. 웹 클라이언트와 서버

**월드 와이드 웹의 기본 요소**

- 웹 서버 = HTTP 서버 
  - 웹 콘텐츠는 웹 서버에 존재한다. 
  - 역할 : HTTP 클라이언트가 요청한 데이터를 제공 (HTTP 응답으로 돌려줌)
- 웹 클라이언트 = HTTP 클라이언트 
  - 역할 : 서버에게 HTTP 객체를 요청
  - ex) 인터넷 익스플로러, 구글 크롬 같은 웹 브라우저 
    - 웹 브라우저는 요청에 대한 응답결과를 사용자의 화면에 보여준다. 

예를 들어 `http://www.google.com/index.html` 페이지를 열어볼 때 웹 브라우저는 HTTP 요청을 www.google.com 서버로 보낸다. 서버는 요청받은 객체 ("/index.html")를 찾고, 그것의 타입, 길이 등의 정보와 함께 HTTP 응답에 실어서 클라이언트에게 보낸다. 

## 3. 리소스

웹 리소스 - 웹 콘텐츠의 원천, 즉 **웹 콘텐츠를 제공하는 모든 것**. 웹 서버가 관리하고 제공한다. 

- 정적 파일 (가장 단순한 웹 리소스)
  -  텍스트 파일, HTML 파일, 마이크로소프트 워드 파일, JPEG 이미지 파일, 동영상 파일, 그 외 모든 종류의 파일
- 요청에 따라 콘텐츠를 생산하는 프로그램
  - 사용자가 누구인지, 어떤 정보를 요청했는지에 따라 다른 동적 콘텐츠를 생성
- **어떤 종류의 콘텐츠 소스든 리소스가 될 수 있음**
  - 스프레드 시트 파일, 탐색 웹 게이트웨이, 인터넷 검색 엔진 등등 ..

### 3.1 미디어 타입

웹 서버는 모든 HTTP 객체에 MIME 타입을 붙인다. 웹 브라우저는 서버로부터 객체를 돌려받을 때, 다룰 수 있는 객체인지 MIME 타입을 통해 확인한다. 

**MIME(Multipurpose Internet Mail Extensions)** 타입  

>  원래 각기 다른 전자메일 시스템 사이에서 주고받기 위해 설계된 MIME(Multipurpose Internet Mail Extensions)은 HTTP에서도 멀티미디어 콘텐츠를 기술하고 라벨을 붙이기 위해 채택되었다.  

- 사선(/)으로 구분된 `주 타입`과 `부타입`으로 이루어진 문자열 라벨
  - HTML로 작성된 텍스트 문서 : `text/html`
  - plain ASCII 텍스트 문서  : `text/plain`
  - JPEG, GIF 이미지 : `image/jpeg`, `image/gif`
  - 비디오, PPT 파일 등  : `video/quicktime`, `application/vnd.ms-powerpoint`  

<br/>

#### 부록 내용 정리

MIME 미디어 타입은 메시지 엔티티 본문의 콘텐츠를 설명하는 표준화된 이름이다. 

1. 배경

본래 멀티미디어 이메일을 위해 개발되었지만, 데이터 객체의 포맷과 목적 설명이 필요한 HTTP를 비롯해 여러 다른 프로토콜에서 재 사용되어 왔다.

2. MIME 타입 구조

HTTP에서 MIME 타입은 Content-Type과 Accept 헤더에서 널리 사용된다.

```bash
Content-Type : video/quicktime  
Content-Type: text/html; charset="iso-8859-6"  
Content-Type: multipart/mixed; boundary=asfeasdfasd  
Accept: image/gif  
```

- 분리형

  - 객체타입, 다른 객체타입의 컬렉션이나 패키지로 묘사될 수 있다.
  - MIME타입이 객체타입을 직접 묘사한다면 분리형이다. 
  - text, video, audio, image, application 
  - IETF-TOKEN, X-TOKEN (x-로 시작하기만 하면 되는 모든 토큰) 과 같은 확장타입 분리형

- 혼합형

  - 다른 콘텐츠의 컬렉션이나 요약본인 MIME타입 
  - multipart, message

- 멀티파트형

  - 혼합형으로, 여러개의 컴포넌트 타입으로 이루어져있다. 

  - ```html
    <form action="http://localhost:8000/" method="post" enctype="multipart/form-data">
      <input type="text" name="myTextField">
      <input type="checkbox" name="myCheckBox">Check</input>
      <input type="file" name="myFile">
      <button>Send the file</button>
    </form>
    ```

  - ```http
    POST / HTTP/1.1
    Host: localhost:8000
    User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:50.0) Gecko/20100101 Firefox/50.0
    Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
    Accept-Language: en-US,en;q=0.5
    Accept-Encoding: gzip, deflate
    Connection: keep-alive
    Upgrade-Insecure-Requests: 1
    Content-Type: multipart/form-data; boundary=---------------------------8721656041911415653955004498
    Content-Length: 465
    
    -----------------------------8721656041911415653955004498
    Content-Disposition: form-data; name="myTextField"
    
    Test
    -----------------------------8721656041911415653955004498
    Content-Disposition: form-data; name="myCheckBox"
    
    on
    -----------------------------8721656041911415653955004498
    Content-Disposition: form-data; name="myFile"; filename="test.txt"
    Content-Type: text/plain
    
    Simple file.
    -----------------------------8721656041911415653955004498-- <- boundary끝은 --로 끝남
    ```

    

**MDN WEB DOCS 추가 설명** [링크](https://developer.mozilla.org/ko/docs/Web/HTTP/Basics_of_HTTP/MIME_types)  

  

### 3.2 URI(Uniform Resource Identifier) 

- URL (Uniform Resource Locator)
  - 리소스 접근 위치를 알려줌
  - 오늘날 대부분의 URI는 URL이다. 
- URN (Uniform Resource name)
  - 위치에 영향받지 않는 유일무이한 이름

대부분의 URL은 세 부분으로 이루어진 표준 포맷을 따른다.  



> http://www.joes-hardware.com/specials/saw-blade.gif

- URL의 첫번째  부분은 스킴(scheme)이라고 불리는데, 리소스에 접근하기 위해 사용되는 프로토콜을 서술한다. 보통 HTTP프로토콜(http://)이다.
- 두번째 부분은 서버의 인터넷 주소를 제공한다. 
- 마지막은 웹 서버의 리소스를 가리킨다. 



## 4. 트랜잭션

HTTP 트랜잭션은 요청과 응답으로 구성되어 있고, HTTP 메세지를 이용해 이루어진다. 

- HTTP 요청 메시지는 명령과 URI를 포함한다.
- HTTP 응답 메시지는 트랜잭션의 결과를 포함한다.  

### 4.1 메서드

모든 HTTP 요청 메시지는 한개의 메서드를 갖는다. 메서드는 서버에게 어떤 동작이 취해져야 하는지 말해준다.  (3장에서 자세히 다룸 )

<table>
  <tr>
  <th>HTTP 메서드</th>
  <th>설명</th>
  </tr>
  <tr>
  	<td>GET</td>
    <td>서버에서 클라이언트로 지정한 리소스를 보내라</td>
  </tr>
  <tr>
  	<td>POST</td>
    <td>클라이언트 데이터를 서버 게이트웨이 어플리케이션으로 보내라</td>
  </tr>
  <tr>
  	<td>PUT</td>
    <td>클라이언트에서 서버로 보낸 데이터를 지정한 이름의 리소스로 저장하라</td>
  </tr>
  <tr>
  	<td>DELETE</td>
    <td>지정한 리소스를 서버에서 삭제하라</td>
  </tr>
  <tr>
  	<td>HEAD</td>
    <td>지정한 리소스에 대한 응답에서, HTTP 헤더부분만 보내라 </td>
  </tr>
</table>

### 4.2 상태 코드

 모든 HTTP 응답 메시지는 상태 코드와 함께 반환된다. 상태코드는 클라이언트에게 요청이 성공했는지 아니면 추가 조치가 필요한지 알려주는 세자리 숫자다. 

- 200 : OK
- 302
- 404 : NOT FOUND

HTTP는 상태 코드에 텍스트로 된 사유 구절(reason phrase)도 함께 보내지만, 이 부분은 설명만을 위한것일 뿐 실제 응답 처리에는 숫자로된 코드가 사용된다. 

### 4.3 웹 페이지는 여러 객체로 이루어질 수 있다.

애플리케이션은 보통 하나의 작업을 수행하기 위해 여러 HTTP 트랜잭션을 수행한다.   

페이지 레이아웃을 서술하는 HTML뼈대를 한번에 트랜잭션으로 가져온 뒤, 첨부된 이미지, 그래픽 조각, 자바 애플릿 등 을 가져오기 위해 추가로 HTTP 트랜잭션을 수행한다. 이 리소스들은 각각 다른 서버에 위치할 수도 있다. 

> 웹페이지는 하나의 리소스가 아닌 리소스의 모음이다. 웹페이지는 첨부된 리소스들에 대해 각각 별개의 HTTP 트랜잭션을 필요로 한다.

## 5.메시지

HTTP는 일반 텍스트로 이루어진 단순한 줄 단위 문자열이다. 

- 요청 메시지 : 웹 클라이언트에서 서버로 보낸 HTTP 메시지
- 응답 메시지 : 웹 서버에서 클라이언트로 가는 메시지

그 외 다른 종류의 HTTP 메시지는 없다. 



![Requests and responses share a common structure in HTTP](https://mdn.mozillademos.org/files/13827/HTTPMsgStructure2.png)  출처 - [MDN HTTP 메시지](https://developer.mozilla.org/ko/docs/Web/HTTP/Messages)

- 시작 줄 
  - 메시지 의 첫 줄은 시작 줄로, 요청이라면 무엇을 해야하는지, 응답이라면 무슨 일이 일어났는지 나타낸다. 
- 헤더
  - 시작줄 다음에는 0개 이상의 헤더 필드가 이어진다. 각 헤더 필드는 쉬운 구문분석을 위해 `:` 로 구분되어 있는 하나의 이름과 하나의 값으로 구성된다. 헤더 필드를 추가하려면 한 줄을 더하면 된다. 헤더는 빈줄로 끝난다.
- 본문
  - 빈줄 다음에는 어떤 종류의 데이터든 들어갈 수 있는 메시지 본문이 필요에 따라 올 수 있다. 텍스트 뿐 아니라 이진 데이터도 포함할 수 있다. 



## 6. TCP 커넥션

어떻게 메시지가 TCP(Transmission Control Protocol, 전송 제어 프로토콜) 커넥션을 통해 한 곳에서 다른 곳으로 옮겨가는지

### 6.1 TCP/IP

HTTP는 애플리케이션 계층 프로토콜이다. HTTP는 네트워크 통신의 핵심적인 세부사항에 대해서 신경 쓰지 않는다. 대신 대중적이고 신뢰성있는 프로토콜인 TCP/IP 에게 맡긴다. 

- TCP 제공
  - 오류 없는 데이터 전송
  - 순서에 맞는 전달(데이터는 언제나 보낸 순서대로 도착한다)
  - 조각나지 않는 데이터 스트림 (언제든 어떤 크기로든 보낼 수 있다)

TCP/IP는 TCP와 IP가 층을 이루는 패킷 교환 네트워크 프로토콜의 집합이다. TCP/IP는 각 네트워크와 하드웨어의 특성을 숨기고, 어떤 종류의 컴퓨터나 네트워크든 서로 신뢰성 있는 의사소통을 하게 해준다.   

일단 TCP 커넥션이 맺어지면 클라이언트와 서버 컴퓨터간에 교환되는 메시지가 없어지거나, 손상되거나, 순서가 뒤바뀌어 수신되는 일은 결코 없다.  

> HTTP 네트워크 프로토콜 스택
>
> - HTTP : 애플리케이션 계층 - HTTP, FTP, Telnet
> - TCP : 전송 계층 - TCP, UDP
> - IP : 네트워크 계층 - IP, ICMP, ARP, RARP
> - 네트워크를 위한 링크 인터페이스 : 데이터 링크 계층
> - 물리적인 네트워크 하드웨어 : 물리 계층

### 6.2 접속, IP 주소 그리고 포트번호

HTTP 클라이언트가 서버에 메시지를 전송할 수 있게 되기 전에, IP주소와 포트번호를 사용해 클라이언트와 서버 사이에 TCP/IP 커넥션을 맺어야한다.  -> **URL을 이용**하면 된다.  

URL은 리소스를 가지고 있는 장비에 대한 IP주소와 포트번호를 알려줄 수있다. 

![img](https://t1.daumcdn.net/cfile/tistory/99B32A335C7338C603)

출처 - [티스토리 블로그](https://real-dongsoo7.tistory.com/72?category=716262)

- http://207.200.83.29:80/index.html
  - IP : 207.200.83.29 , Port: 80
- http://www.netscape.com:80/index.html
  - www.netscape.com 이라는 도메인 이름 혹은 호스트명을 가지고 있는데, 호스트 명은 IP 주소에 대한 이해하기 쉬운 형태의 별명이다. 호스트명은 도메인 이름 서비스(Domain Name Service, DNS)서버를 통해서 쉽게 IP로 변환될 수 있다. 
- port 번호 기본값은 80

> - 웹브라우저는 서버의 URL에서 호스트 명을 추출한다.
> - 웹브라우저는 서버의 호스트 명을 IP로 변환한다.
> - 웹브라우저는 URL에서 포트번호(있다면)를 추출한다.
> - 웹브라우저는 웹서버와 TCP 커넥션을 맺는다.
> - 웹브라우저는 서버에 HTTP 요청을 보낸다. 
> - 서버는 웹브라우저에 HTTP 응답을 돌려준다.
> - 커넥션이 닫히면, 웹브라우저는 문서를 보여준다.

### 6.3 텔넷(Telnet)을 이용한 예제

텔넷 - 원격 터미널 세션을 위해 사용되며, HTTP 서버를 포함한 일반적인 TCP 서버에 연결하기 위해 사용될 수도 있다. 

```bash
(base) jskang@jsKang-MacBookPro ~ % telnet www.google.com 80
Trying 142.250.196.132...
Connected to www.google.com.
Escape character is '^]'.
GET / HTTP/1.1

HTTP/1.1 200 OK
Date: Sat, 26 Feb 2022 08:48:07 GMT
Expires: -1
Cache-Control: private, max-age=0
Content-Type: text/html; charset=ISO-8859-1
P3P: CP="This is not a P3P policy! See g.co/p3phelp for more info."
Server: gws
X-XSS-Protection: 0
X-Frame-Options: SAMEORIGIN
Set-Cookie: 1P_JAR=2022-02-26-08; expires=Mon, 28-Mar-2022 08:48:07 GMT; path=/; domain=.google.com; Secure
Set-Cookie: NID=511=fBcmc_CQPxeJIVOuk7ArVg1sTLAMo60wouPBApZ2PhKk7-8CTmZruTDU7_ThYzhVqBoQWYBmvNgk-0zfSJito7H2AY0tV-pcPT3AOA54qLCmYpWvO1eZKgYFTYEKyGVpTFpHZGvkt3TlMl7G32eT2eSgA_xYte1dSd9jHOnUbvg; expires=Sun, 28-Aug-2022 08:48:06 GMT; path=/; domain=.google.com; HttpOnly
Accept-Ranges: none
Vary: Accept-Encoding
Transfer-Encoding: chunked

4d10
<!doctype html><html itemscope="" itemtype="http://schema.org/WebPage" lang="ko"><head><meta content="text/html; charset=UTF-8" http-equiv="Content-Type"></></>..</>

```



## 7. 프로토콜 버전

오늘날 쓰이고 있는 HTTP 프로토콜은 버전이 여러가지다. 

**HTTP/0.9**

- 초기 버전, 원 라인 프로토콜 
- 버전 번호 없음 ( 이후에 차후 버전과 구별하기 위해 0.9 붙임)
- GET 메서드만 지원 
- 멀티미디어 콘텐츠에 대한 MIME 타입, HTTP 헤더 등 지원하지 않음 . HTML파일만 전송 가능.  금방 HTTP/1.0으로 대체

**HTTP/1.0**

- 처음으로 널리 쓰이기 시작한 HTTP 버전 
- 버전 번호, HTTP 헤더, 추가 메서드, 응답 메서드에 상태 코드, 멀티미디어 객체 처리를 추가
- 웹페이지와 상호작용 하는 폼을 실현했다.

**HTTP/1.0+**

- keep-alive 커넥션, 가상 호스팅 지원, 프락시 연결 지원을 포함해 많은 기능이 추가되었다. 이 규격 외 확장된 HTTP 버전을 HTTP/1.0+라고 함

**HTTP/1.1**

- HTTP 설계의 구조적 결함 교정, 두드러진 성능 최적화, 잘못된 기능 제거에 집중
  - 커넥션 재사용 등
- 더 복잡해진 웹 애플리케이션과 배포를 지원
- 현재의 HTTP 버전

**HTTP/2.0**

- HTTP/1.1 성능 문제를 개선하기 위해 구글의 SPDY 프로토콜을 기반으로 설계가 진행중인 프로토콜

![Compares the performance of the three HTTP/1.x connection models: short-lived connections, persistent connections, and HTTP pipelining.](https://mdn.mozillademos.org/files/13727/HTTP1_x_Connections.png)

출처 - [MDN HTTP/1.x의 커넥션 관리](https://developer.mozilla.org/ko/docs/Web/HTTP/Connection_management_in_HTTP_1.x)

## 8. 웹의 구성요소 

### 8.1 프락시

> HTTP 프락시 서버

- 클라이언트와 서버 사이에 위치한 HTTP 중개자 
  - 클라이언트의 모든 요청을 받아 대개 요청을 수정한 후에  사용자를 대신해 서버에 접근
- **웹 보안, 애플리케이션 통합, 성능 최적화**를 위한 중요한 구성요소
  - 모든 웹 트래픽 흐름 속에서 신뢰할만한 중개자 역할
  - 요청과 응답을 필터링
  - 바이러스 검출, 콘텐츠 차단 등 (6장에 계속)  

### 8.2 캐시

> 많이 찾는 웹페이지를 클라이언트 가까이에 보관하는 HTTP 창고

웹 캐시와 캐시 프락시는 자신을 거쳐가는 문서들 중 자주 찾는 것의 사본을 저장해두는, 특별한 종류의 HTTP 프락시 서버이다. 다음번에 클라이언트가 같은 문서를 요청하면 그 캐시가 갖고 있는 사본을 받을 수 있다.  

HTTP는 캐시를 효율적으로 동작하게 하고 캐시된 콘텐츠를 최신버전으로 유지하면서 동시에 프라이버시도 보호하기 위한 많은 기능을 정의한다. (7장에 계속)

### 8.3 게이트웨이

- 다른 서버들의 중개자로 동작하는 특별한 서버
- 주로 **HTTP 트래픽을 다른 프로토콜로 변환**하기 위해 사용

> HTTP/FTP 게이트웨이는 FTP URI에 대한 HTTP요청 -> FTP 통신 -> HTTP 응답

### 8.4 터널

- 두 커넥션 사이에서 raw 데이터를 열어보지 않고 그대로 전달해주는 HTTP 어플리케이션
- 주로 비 HTTP 데이터를 하나 이상의 HTTP 연결을 통해 그대로 전송해주기 위해 사용

HTTP 터널을 활용하는 대표적인 예로, 암호화된 SSL 트래픽을 HTTP 커넥션으로 전송함으로써 웹 트래픽만 허용하는 사내 방화벽을 통과시키는 것이 있다.   

우선 HTTP/SSL 터널은 HTTP 요청을 받아들여 목적지의 주소와 포트번호로 커넥션을 맺는다. 이후부터는 암호화된 SSL 트래픽을 HTTP 채널을 통해 목적지 서버로 전송할 수 있게 된다. 

### 8.5 에이전트

사용자 에이전트는 사용자를 위해 HTTP 요청을 만들어주는 클라이언트 프로그램이다. 웹 요청을 만드는 애플리케이션은 뭐든 HTTP 에이전트다. 

- 웹 브라우저 
- 스파이더, 웹 로봇 (검색엔진) 등 



---

<div id="mime"></div>

## MIME (Multipurpose Internet Mail Extensions)

### 전자우편

- 7비트 ASCII 코드를 사용하여 전송
- 문자 이외의 바이너리 데이터(이미지, 동영상, 워드 문서 등)를 전송할 수 없음
- MIME 사양에 따라 멀티미디어 파일의 데이터를 ASCII 데이터로 변환후 전송
- **송신측에서 전송 ASCII 데이터가 원래 어떤 형식이었는지 MIME타입을 기록하여 전송하면, 수신측에서는 해당 MIME 타입을 참고하여 수신한 ASCII 데이터를 원래의 멀티미디어 바이너리 파일로 변환하여 해석한다.** 
- 이렇게해서 인터넷 메일의 한계를 극복하며, 여러가지 타입의 데이터를 주고받을 수 있게 되었다.

따라서 메세지 헤더와 그 값은 항상 ASCII 문자를 사용해야한다. 

![이미지](https://t1.daumcdn.net/cfile/tistory/997B2D395A7078321D)    

출처 - 티스토리 [[HTTP\] MIME TYPE](https://dololak.tistory.com/130)

### 참고

> application/octet-stream

- 8비트 단위의 바이너리 데이터. 표현할만한 MIME가 없을 경우(알려지지 않은 파일 타입 or .bin, .arc)를 위한 기본값
- 주의해야함

> multipart/form-data

- HTML form태그안에서 post방식으로 여러 데이터를 함께 전송하는 경우 사용 

> multipart/byteranges

- HTTP 응답에 대한 멀티 파트 타입으로 `206 Partial Content`와 함께 범위응답을 하게 된다. 

- 여러번에 걸쳐 파일을 리턴해줄 것을 요청할 때 사용한다

- ```http
  HTTP/1.1 206 Partial Content
  Accept-Ranges: bytes
  Content-Type: multipart/byteranges; boundary=3d6b6a416f9b5
  Content-Length: 385
  
  --3d6b6a416f9b5
  Content-Type: text/html
  Content-Range: bytes 100-200/1270
  
  eta http-equiv="Content-type" content="text/html; charset=utf-8" />
      <meta name="vieport" content
  --3d6b6a416f9b5
  Content-Type: text/html
  Content-Range: bytes 300-400/1270
  
  -color: #f0f0f2;
          margin: 0;
          padding: 0;
          font-family: "Open Sans", "Helvetica
  --3d6b6a416f9b5--
  ```

- 출처
  - https://dololak.tistory.com/130



### MIME 타입 확인하는 java 코드

1. File.probeContentType 확장자를 이용하여 마임타입을 판단 (실제로 파일이 존재하지 않아도)

```bash
String mimeType = Files.probeContentType(source);
```

2. URLConnection 확장자를 이용하여 마임타입을 판단 (실제로 파일이 존재하지 않아도)

```bash
String mimeType = URLConnection.guessContentTypeFromName("D:/sample.txt");
```

3. MimeTypesFileTypeMap객체 (JDK 6) - 확장자 이용해 결정 , 판단하지 못하면 `application/octet-stream`반환 

```bash
String mimeType = mimeTypesMap.getContentType("D:/sample.txt");
```

4. Apache Tika 라이브러리(컨텐츠 분석 툴 킷)  - 확장자가 아닌 실제 파일의 내용을 검사 

```bash
String mimeType = new Tika().detect(file);
```

- 출처 티스토리 [자바(Java)로 파일의 마임 타입(MIME Type) 확인하기](https://offbyone.tistory.com/330)

