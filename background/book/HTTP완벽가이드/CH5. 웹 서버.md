# 웹서버

- 여러 종류의 소프트웨어 및 하드웨어 웹 서버
- Perl을 사용한 간단한 웹 서버
- 웹 서버의 단계별 HTTP 트랜잭션 처리

## 1. 다채로운 웹 서버
웹 서버는 HTTP 요청을 처리하고 응답을 제공한다.
> 모든 웹 서버는 리소스에 대한 HTTP 요청을 받아서 콘텐츠를 클라이언트에게 돌려줌

### 1.1 웹 서버 구현
웹 서버는 HTTP 및 그와 관련된 TCP 처리를 구현한 것이다.
자신이 제공하는 리소스를 관리하고 웹 서버를 설정,통제,확장하기 위한 관리 기능을 제공한다.

**웹 서버의 여러가지 형태**
- 다목적 소프트웨어 웹 서버
- 임베디드 웹 서버

### 1.2 다목적 소프트웨어 웹 서버
다목적 소프트웨어 웹 서버는 네트워크에 연결된 표준 컴퓨터 시스템에서 동작한다.
웹 서버 소프트웨어는 거의 모든 컴퓨터와 운영체제에서 동작한다.

- 널리 사용되는 웹 서버 소프트웨어
  - apache Http Server
  - nginx


![dlalwl](https://news.netcraft.com/images/2022/01/wss-share.png)


### 1.3 임베디드 웹 서버
일반 소비자용 제품에 내장될 목적으로 만들어진 작은 웹서버

- 예
  - 프린터
  - 가전제품
  - 웹 브라우저로 접근 가능한 관리기능을 제공하는 공유기


## 2. 간단한 펄 웹서버
최소한으로 기능하는 HTTP 서버는 Perl 코드로 만들 수 있다.
이 코드가 웹서버를 흉내내는 방식은 **HTTP 요청 메시지를 정확하게 기록하고 어떤 HTTP 응답 메시지라도 돌려보낼 수 있도록 해준다.**
```perl
$ cat type-o-serve.sh
#!/usr/bin/perl

use Socket;
use Carp;
use FileHandle;

$port = (@ARGV ? $ARGV[0]: 8080);

$proto = getprotobyname('tcp');
socket(S, PF_INET, SOCK_STREAM, $proto) || die;
setsockopt(S, SOL_SOCKET,  SO_REUSEADDR, pack("l",1)) || die;
bind(S, sockaddr_in($port, INADDR_ANY)) || die;
listen(S, SOMAXCONN) || die;

printf(" << Type O Serve Accepting on Port %d >> \n\n",$port);

while(1) {

        $cport_caddr = accept(C,S);
        ($cport, $caddr) = sockaddr_in($cport_caddr);
        C->autoflush(1);

        $cname = gethostbyaddr($caddr, AF_INET);
        printf(" << Reqeust Form '%s' >>>\n", $cname);

        while($line = <C>){
                print $line;
                if ($line =~ /^\r/) {last;}
        }

        printf(" <<<Type Response FOllowed by '.' >>>\n");
        while( $line = <STDIN>){
                $line =~ s/\r//;
                $line =~ s/\r//;
                if($line =~ /^\./) {last;}
                print C $line . "\r\n";
        }
        close(C);
}

```

- 200요청 응답하는 코드

```bash
<< Reqeust Form 'localhost' >>>
GET / HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Cache-Control: max-age=0
sec-ch-ua: " Not A;Brand";v="99", "Chromium";v="96", "Google Chrome";v="96"
sec-ch-ua-mobile: ?0
sec-ch-ua-platform: "Linux"
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.93 Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
Sec-Fetch-Site: none
Sec-Fetch-Mode: navigate
Sec-Fetch-User: ?1
Sec-Fetch-Dest: document
Accept-Encoding: gzip, deflate, br
Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7
Cookie: csrftoken=CzATBwIr6X5ufvghugO8y4NWg0AWguhWhNlRQxuaLc9z8PlJjXbjpIKoFEwbVF8r

 <<<Type Response FOllowed by '.' >>>
HTTP/1.1 200 OK
.
```



### <추가> CGI (Common gateway interface)

![이미지](https://upload.wikimedia.org/wikipedia/commons/thumb/6/61/CGI_common_gateway_interface.svg/1920px-CGI_common_gateway_interface.svg.png)
[이미지 출처 - [위키](https://ko.wikipedia.org/wiki/%EA%B3%B5%EC%9A%A9_%EA%B2%8C%EC%9D%B4%ED%8A%B8%EC%9B%A8%EC%9D%B4_%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4) ]

- 웹 서버가 요청을 처리하기 위해 외부 프로그램을 실행할 수 있도록 하는 인터페이스
- 웹서버가 클라이언트의 요청을 CGI를 통해 웹서버상에 작동하는 프로그램에게 보냄

기본적인 CGI
- C, Perl을 사용해서 구현할 수 있다.
  - Perl은 CGI라이브러리를 지원하는 스크립트 언어로 텍스트 기반으로 작성되고, 문자열 처리 강점
- 범용적이고 간단하다
- 요청마다 추가적인 프로세스가 할당되어 CGI를 통해 프로그램 실행

모듈
- 웹서버와 같은 프로세스에서 동작

서블릿
- Java기반의 발전된 CGI 느낌
- 기본 컴퓨터 OS가 아닌 JVM에서 구동

그 외
- node는 내장되어있음
- python은 wsgi, fastcgi등으로 개선

> 출처
> - https://pkeu.notion.site/pkeu/Nginx-ab4282c71ccb4efaaaadf840deb8c06a  

 

## 3. 진짜 웹 서버가 하는 일
1. 커넥션을 맺는다
   1. 클라이언트의 접속을 받아들이거나, 원치 않는 클라이언트라면 닫는다.
2. 요청을 받는다.
   1. HTTP 요청 메시지를 해석하고 행동을 취한다.
3. 요청을 처리한다.
   1. 요청 메시지를 해석하고 행동을 취한다.
4. 리소스에 접근한다.
   1. 메시지에서 지정한 리소스에 접근한다.
5. 응답을 만든다.
   1. 올바른 헤더를 포함한 HTTP 응답 메시지를 생성한다.
6. 응답을 보낸다.
   1. 응답을 클라이언트에게 돌려준다.
7. 트랜잭션을 로그로 남긴다.
   1. 로그파일에 트랜잭션 완료에 대한 기록을 남긴다.

 


## 4. 단계 1: 클라이언트 커넥션 수락
클라이언트가 지속적 커넥션을 갖고 있다면, 클라이언트는 요청을 보내기 위해 그 커넥션을 사용할 수 있다. 그렇지 않다면 클라이언트는 서버에 대한 새 커넥션을 열 필요가 있다.

### 4.1 새 커넥션 다루기
클라이언트가 웹 서버에 TCP 커넥션을 요청하면 웹서버는 그 커넥션을 맺고 TCP 커넥션에서 IP주소를 추출하여 커넥션 맞은편에 어떤 클라이언트가 있는지 확인한다.
일단 새 커넥션이 맺어지고 받아들여지면, 서버는 새 커넥션을 커넥션 목록에 추가하고 커넥션에서 오가는 데이터를 지켜보기 위한 준비를 한다.
웹서버는 어떤 커넥션이든 마음대로 거절하거나 즉시 닫을 수 있으며 클라이언트 IP 주소나 호스트명이 인가되지 않았거나 악의적일 경우 커넥션을 닫는다.

### 4.2 클라이언트 호스트명 식별
대부분의 웹 서버는 `역방향 DNS(reverse DNS)`를 사용해서 **클라이언트의 ip 주소를 클라이언트의 호스트명으로 변환** 하도록 설정되어있다.
- 클라이언트의 호스트 명
  - 구체적인 접근 제어와 로깅을 위해 사용
    - ex) 메일 보낼 때 rDNS에 등록되어있어야 정상 메일 수신 가능 (스팸 필터링)
  - hostname lookup은 시간이 오래 걸려 웹 트랜잭션 지연이 발생하므로 설정을 on/off할 수 있다.
    - EX) 아파치
        ``` bash
            HostnameLookups off
            <Files ~"\.(html|htm|cgi)$">
                HostnameLookups on
            </Files>
        ```


### 4.3 ident를 통해 클라이언트 사용자 알아내기
ident 프로토콜은 서버에게 어떤 사용자 이름이 HTTP 커넥션을 초기화했는지 찾아낼 수 있게 해준다.
- 클라이언트가 113포트를 열어두면 요청을 받은 서버가 다시 클라이언트에게 ident 프로토콜로 사용자 정보 응답을 받음
- 웹 서버 로깅에 쓰임
  - 일반 로그 포맷(Common Log Format)의 두번째 필드는 각 HTTP 요청의 ident사용자 이름을 담고있음
  - `LogFormat "%h %l %u %t \"%r\" %>s %b" common` 의 `%l`
  - 113 port
- 폐쇄된 내부 private 네트워크에서는 사용할 수 있지만, 공공 인터넷에는 부적합하다.
  - HTTP 지연
  - 조작 가능
  - 기타 보안 측면,, 방화벽 ,,



## 5. 단계 2: 요청 메시지 수신
커넥션에 데이터가 도착하면 웹 서버는 네트워크 커넥션에서 그 데이터를 읽어들이고 파싱하여 요청 메시지를 구성한다.
- 요청 줄 파싱
  - 요청 메서드, URI, HTTP 버전 번호 (`스페이스`, `CRLF`로 파싱)
  - 메시지 헤더 (`CRLF`로 구분)
  - 헤더의 끝을 의미하는 빈 줄(`CRLF`)
  - 요청 본문 (길이는 헤더에 `Content-Length`로 정의)
- 네트워크로부터 불규칙적으로 받은 데이터를 파싱해서 이해 가능한 수준의 분량을 확보할 때 까지 메시지 일부분을 임시로 저장해둘 필요가 있다.

### 5.1 메시지의 내부 표현
몇몇 웹 서버는 요청 메시지를 쉽게 다룰 수 있도록 내부의 자료 구조에 저장한다.
- 룩업 테이블 <헤더 이름>:<값>
- 예를들면 내 생각엔 HttpServletRequest 객체 이런거..? `request.getMethod()`, `request.getRequestUri()`


### 5.2 커넥션 입력/출력 처리 아키텍처
고성능 웹서버는 수천 개의 커넥션을 동시에 열 수 있도록 지원한다. 이 커넥션들은 클라이언트들과 각각 한 개 이상의 커넥션을 통해 통신할 수 있게 해준다. 웹 서버 아키텍처의 차이에 따라 요청을 처리하는 방식이 달라진다.

**단일 스레드 웹 서버**
- 한번에 하나씩 요청 처리
- 처리 도중에 모든 다른 커넥션은 무시됨
- 심각한 성능 문제


**멀티프로세스와 멀티 스레드 웹 서버**
- 여러 요청을 동시에 처리하기 위해 여러개의 프로세스 혹은 고효율 스레드 할당
- 스레드/프로세스는 필요할때마다 만들어지거나, 미리 (Worker pool 혹은 thread pool) 만들어둠
- 수천, 수만개의 동시 커넥션을 처리할 때 그로 인해 만들어진 수많은 프로세스나 스레드는 너무 많은 메모리나 시스템 리소스를 소비한다.
- 보통 많은 멀티스레드 웹 서비스가 **스레드/프로세스 최대 처리 개수에 제한**을 둠


**다중I/O 서버**
- 대량의 커넥션을 지원하기 위해, 많은 웹서버가 채택한 방식
- 모든 커넥션은 동시에 그 활동을 감시당하며 커넥션의 상태가 바뀌면(데이터가 사용할 수 있게 되거나 에러 발생) 그 커넥션에 대한 작은 양의 처리가 수행된다.
  - 그 처리가 완료되면 다음번 상태 변경을 위해 열린 커넥션 목록으로 돌아간다.
- 스레드와 프로세스는 유휴상태의 커넥션에 매여 기다리느라 리소스를 낭비하지 않는다.


**다중 멀티스레드 웹 서버**
- 몇몇 시스템은 자신의 컴퓨터 플랫폼에 올라와 있는 CPU 여러개의 이점을 살리기 위해 멀티 스레딩과 다중화(multiplexing)결합
- 여러 개의 스레드는 각각 열려있는 커넥션을 감시하고 각 커넥션에 대해 조금씩 작업을 수행




## 6. 단계 3: 요청 처리
웹서버가 요청을 받으면 서버는 요청으로부터 메서드, 리소스, 헤더, 본문을 얻어내서 처리한다.
앞으로 다른 장에서 많이 다룰 내용

## 7. 단계 4: 리소스의 매핑과 접근 
웹 서버는 리소스 서버다.
웹 서버가 클라이언트에게 콘텐츠를 전달하려면 요청 메시지의 URI에 대응하는 알맞은 콘텐츠,콘텐츠 생성기를 웹 서버에서 찾아 그 콘텐츠의 원천을 식별해야 한다.

콘텐츠 종류
- 미리 만들어진 정적 콘텐츠 (HTML, JPEG)
- 서버 위에서 동작하는 리소스 생성 애플리케이션을 통해 만들어진 동적 콘텐츠

### 7.1 Docroot
리소스 매핑의 가장 단순한 형태는 요청 URI를 웹 서버의 파일시스템 안에 있는 파일 이름으로 사용하는것
일반적으로 웹 서버 파일 시스템의 특별한 폴더를 웹 콘텐츠를 위해 예약해둔다.
- 문서 루트 혹은 `docroot`

**예시 - httpd.conf 설정파일에 DocumentRoot 줄을 추가하여 아파치 웹 서버의 문서 루트를 설정**
- 아파치 예시
```apache
DocumentRoot /usr/local/httpd/files
```
만약 `/specials/saw-blade.gif`에 대한 요청이 들어오면 웹서버는 `/usr/local/httpd/files/specials/saw-blade.gif` 파일을 변환할 것이다.
이때 상대적인 url이 docroot를 벗어나 파일시스템의 docroot이외의 부분이 노출되는 것에 주의해야한다.
```apache
http://www.joes-hardware.com/../ # 상대경로로 root의 상위 폴더를 탐색하려고 하는 요청 허용하지 말아야함
```

**가상 호스팅된 docroot**
가상 호스팅된 웹 서버는 각 사이트에 그들만의 분리된 문서 루트를 주는 방법으로 한 웹 서버에 여러개의 웹 사이트를 호스팅한다.
서버는 두 웹 사이트를 HTTP host헤더나 서로 다른 IP주소를 이용해 구분할 수 있다.

- 아파치 예시
```apache
<VirtualHost www.joes-hardware.com>
  ServerName www.joes-hardware.com
  DocumentRoot /docs/joe
  TransferLog /logs/joe.access_log
  ErrorLog /logs/joe.error_log
</VirtualHost>

<VirtualHost www.marys-antiques.com>
  ServerName www.marys-antiques.com
  DocumentRoot /docs/mary
  TransferLog /logs/mary.access_log
  ErrorLog /logs/mary.error_log
</VirtualHost>

```

**사용자 홈 디렉터리 docroots**
docroot의 또 다른 대표적 활용은 사용자들이 한 대의 웹 서버에서 각자의 개인 웹 사이트를 만들 수 있도록 해주는 것이다.
보통 `/`과 `~` 다음에 사용자 이름이 오는것으로 시작하는 URI는 그 사용자의 개인 문서 루트를 가리킨다.

### 7.2 디렉터리 목록
웹서버는 경로가 파일이 아닌 **디렉터리**를 가리키는 URI 요청을 받을 수 있다. 대부분의 웹 서버는 다음과 같은 행동을 취하도록 설정할 수 있다.
- 에러 반환
- 디렉터리 대신 특별한 *색인파일* 반환
  - index.html, index.htm
- 디렉터리를 탐색해서 그 내용을 담은 HTML 페이지를 반환
  - 디렉터리 색인파일이 없고 디렉터리 색인 기능이 꺼져있지 않다면 웹서버는 자동으로 디렉터리의 파일들을 크기, 변경일 및 파일에 대한 링크와 함께 열거한 html 파일을 반환한다.
  - 아파치 옵션 `Options -Indexs`

**아파치 웹 서버 예**
- `DirectoryIndex`를 사용해 기본 디렉터리 파일로 사용될 파일이름 집합 설정
  - `DirectoryIndex index.html index.html home.html home.thm index.cgi`

### 7.3 동적 콘텐츠 리소스 매핑
웹서버는 URI를 동적 리소스에 매핑할 수도 있다. 즉 요청에 맞게 콘텐츠를 생성하는 프로그램에 URI를 매핑하는 것이다.
- 애플리케이션 서버는 웹 서버를 복잡한 백엔드 애플리케이션과 연결하는 일을 한다.
- 그에 대한 동적 콘텐츠 생성 프로그램이 어디 있고, 어떻게 실행하는지를 알려준다.

**아파치 예**
아파치는 URI의 경로명이 실행 가능한 프로그램이 위치한 디렉터리로 매핑되도록 설정하는 기능을 제공한다.

```apache
# 다음은 `/cgi-bin/`으로 시작하는 uri 요청을 받으면 ` /usr/local/etc/httpd/cgi-programs/`에서 프로그램을 찾아 실행하라는 의미이다.
ScriptAlias /cgi-bin/ /usr/local/etc/httpd/cgi-programs/

# 특정 확장자의 파일만 실행하도록 설정
AddHandler cgi-script .cgi
```

웹 초창기에 널리 쓰였던 CGI는 서버사이드 애플리케이션을 실행하기 위한 간단한 인터페이스로, 오늘날에는 자바 서블릿과 같은 더 강력하고 효과적인 서버 사이드 동적 컨텐츠 지원 기능을 갖고 있다.

### 7.4 서버사이드 인클루드(Server-Side includes, SSI)
만약 어떤 리소스가 서버사이드 인클루드를 포함하고 있는 것으로 설정되어 있다면, 서버는 그 리소스의 콘텐츠를 클라이언트에게 보내기 전에 처리한다.

- 서버사이드 인클루드 쓰이는 예시 
  - 네이버 D2 https://d2.naver.com/helloworld/6070967

### 7.5 접근 제어
웹 서버는 또한 각각의 리소스에 접근 제어를 할당할 수 있다.
접근제어되는 리소스에 대한 요청이 도착했을 때 웹서버는 클라이언트의 IP 주소에 근거하여 접근을 제어할 수도 있고, 혹은 비밀번호를 물어볼 수도 있다.

**아파치 예**
```apache
<Directory "/www/ihd/admin">
Order Deny, Allow # Allow 규칙 먼저 반영된 후 Deny적용
Deny from All
Allow from 192.168.22.0/24
</Directory>
```
**Nginx 예**
```nginx
server {
  listen 80;
  server_name www.example.com;
  location / { # 위에서부터 차례로 적용
    deny 192.168.56.101
    allow 192.168.56.0/24;
    deny all;
  }
}

```

## 8. 단계 5: 응답 만들기
서버가 리소스를 식별하면 서버는 요청 메서드로 서술되는 동작을 수행한 뒤 응답 메시지를 반환한다.
응답 메시지는 `응답 상태 코드`, `응답 헤더`, `응답 본문`(생성되었다면) 을 포함한다.

### 8.1 응답 엔티티
본문에는 주로 다음을 포함한다.
- 응답 본문의 MIME 타입을 서술하는 Content-Type 헤더
- 응답 본문의 길이를 서술하는 Content-Length 헤더
- 실제 응답 본문의 내용

### 8.2 MIME 타입 결정하기
- mime.types
  - 확장자별 MIME 타입이 담겨있는 파일을 탐색해 파일이름의 확장자로 값을 가져온다.
- 매직 타이핑(Magic typing)
  - 아파치 웹 서버는 파일 내용을 검사해서 잘 알려진 패턴에 대한 테이블에 해당하는 패턴이 있는지 찾는다.
  - 느리긴 하지만 표준 확장자 없이 이름지어진 경우에 편리하다.
- 유형 명시(Explicit typing)
  - 특정 파일이나 디렉터리 안의 파일들이 파일 확장자나 내용에 상관없이 어떤 MIME 타입을 갖도록 웹서버를 설정할 수 있다.
- 유형 협상(Type negotiation)
  - 어떤 웹 서버는 한 리소스가 여러 종류의 문서 형식에 속하도록 설정할 수 있다. 이때 웹 서버가 사용자와의 협상 과정을 통해 사용하기 가장 좋은 형식을 판별할 것인지의 여부도 설정할 수 있다.
  - 또한 웹 서버는 특정 파일이 특정 MIME 타입을 갖게끔 설정할 수도 있다.

### 8.3 리다이렉션
- 웹서버는 요청을 수행하기 위해 브라우저가 다른 곳으로 가도록 리다이렉트할 수 있다.
- 리다이렉션 응답은 `3XX` 상태 코드로 지칭된다.
-  Location 응답 헤더는 콘텐츠의 새로운 혹은 선호하는 위치에 대한 URI를 포함한다.

**영구히 리소스가 옮겨진경우**
- `301 Moved Permanently`

**임시로 리소스가 옮겨진경우**
- 새 위치로 리다이렉트를 하길 원하지만 나중에는 원래의 URI로 찾고 북마크도 갱신하지 않기를 원한다.
- `303 See Other`와 `307 Temporary Redirect`

**URI 증강**
- 재 작성된 URI로 리다이렉트
- 상태정보를 내포한 새 URL을 생성하고 사용자를 새 URL로 리다이렉트한다.
- 트랜잭션간 상태를 유지하는 방법
- `303 See Other`와 `307 Temporary Redirect`

**부하 균형**
- 과부하된 서버 요청을 받으면 다른 서버로 리다이렉트
- `303 See Other`와 `307 Temporary Redirect`

**친밀한 다른 서버가 있을 때**
- 웹서버는 사용자에 대한 정보를 가질 수 있는데 클라이언트에 대한 정보를 가지고 있는 서버로 리다이렉트
- `303 See Other`와 `307 Temporary Redirect`

**디렉터리 이름 정규화**
- `/`을 빠뜨렸을 때

## 9. 단계 6: 응답 보내기
커넥션 상태에 따라
서버는 모든 메시지를 전송했을 때 자신의 커넥션을 닫을 것이고, 지속 커넥션이라면 서버가 Content-Length 헤더를 바르게 계산하기 위해 특별한 주의를 필요로하는 경우나 클라이언트가 응답이 언제 끝나는지 알 수 없는 경우에 커넥션은 열린 상태를 유지할 것이다.

## 10. 단계 7: 로깅
트랜잭션이 완료되었을 때 웹 서버는 트랜잭션이 어떻게 수행되었는지에 대한 로그를 로그파일에 기록한다.





## nginx.conf

```nginx
user       www www;  ## Default: nobody
worker_processes  5;  ## Default: 1
error_log  logs/error.log;
pid        logs/nginx.pid;
worker_rlimit_nofile 8192;

events {
  worker_connections  4096;  ## Default: 1024
}

http {
  include    conf/mime.types;
  include    /etc/nginx/proxy.conf;
  include    /etc/nginx/fastcgi.conf;
  index    index.html index.htm index.php;

  default_type application/octet-stream;
  log_format   main '$remote_addr - $remote_user [$time_local]  $status '
    '"$request" $body_bytes_sent "$http_referer" '
    '"$http_user_agent" "$http_x_forwarded_for"';
  access_log   logs/access.log  main;
  sendfile     on;
  tcp_nopush   on;
  server_names_hash_bucket_size 128; # this seems to be required for some vhosts

  server { # php/fastcgi
    listen       80;
    server_name  domain1.com www.domain1.com;
    access_log   logs/domain1.access.log  main;
    root         html;

    location ~ \.php$ {
      fastcgi_pass   127.0.0.1:1025;
    }
  }

  server { # simple reverse-proxy
    listen       80;
    server_name  domain2.com www.domain2.com;
    access_log   logs/domain2.access.log  main;

    # serve static files
    location ~ ^/(images|javascript|js|css|flash|media|static)/  {
      root    /var/www/virtual/big.server.com/htdocs;
      expires 30d;
    }

    # pass requests for dynamic content to rails/turbogears/zope, et al
    location / {
      proxy_pass      http://127.0.0.1:8080;
    }
  }

  upstream big_server_com {
    server 127.0.0.3:8000 weight=5;
    server 127.0.0.3:8001 weight=5;
    server 192.168.0.1:8000;
    server 192.168.0.1:8001;
  }

  server { # simple load balancing
    listen          80;
    server_name     big.server.com;
    access_log      logs/big.server.access.log main;

    location / {
      proxy_pass      http://big_server_com;
    }
  }
}
```
**mime.types**
```nginx
types {
  text/html                             html htm shtml;
  text/css                              css;
  text/xml                              xml rss;
  image/gif                             gif;
  image/jpeg                            jpeg jpg;
  application/x-javascript              js;
  text/plain                            txt;
  text/x-component                      htc;
  text/mathml                           mml;
  image/png                             png;
  image/x-icon                          ico;
  image/x-jng                           jng;
  image/vnd.wap.wbmp                    wbmp;
  application/java-archive              jar war ear;
  application/mac-binhex40              hqx;
  application/pdf                       pdf;
  application/x-cocoa                   cco;
  application/x-java-archive-diff       jardiff;
  application/x-java-jnlp-file          jnlp;
  application/x-makeself                run;
  application/x-perl                    pl pm;
  application/x-pilot                   prc pdb;
  application/x-rar-compressed          rar;
  application/x-redhat-package-manager  rpm;
  application/x-sea                     sea;
  application/x-shockwave-flash         swf;
  application/x-stuffit                 sit;
  application/x-tcl                     tcl tk;
  application/x-x509-ca-cert            der pem crt;
  application/x-xpinstall               xpi;
  application/zip                       zip;
  application/octet-stream              deb;
  application/octet-stream              bin exe dll;
  application/octet-stream              dmg;
  application/octet-stream              eot;
  application/octet-stream              iso img;
  application/octet-stream              msi msp msm;
  audio/mpeg                            mp3;
  audio/x-realaudio                     ra;
  video/mpeg                            mpeg mpg;
  video/quicktime                       mov;
  video/x-flv                           flv;
  video/x-msvideo                       avi;
  video/x-ms-wmv                        wmv;
  video/x-ms-asf                        asx asf;
  video/x-mng                           mng;
}

