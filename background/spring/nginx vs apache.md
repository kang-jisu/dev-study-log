## Apache HTTP Server (Multi-Process Module 방식)

- 요청당 스레드/프로세스 기반 구조
- I/O 작업중에는 스레드/프로세스가 Block됨
- CGI 프로세스를 fork/kill 함
- 1)prefork
  - 하나의 요청당 하나의 프로세스로 처리
- 2)worker
  - thread생성해 처리

> MPM 방식
>
> 1. perfork MPM (다중 프로세스)
>
> - 응답 프로세스를 미리 올려놓고 클라이언트 요청시 자식 프로세스를 생성해 처리
> - 하나의 자식 프로세스는 하나의 스레드를 가지며 스레드(프로세스)간 메모리 공유 하지 않아 안정적이지만 메모리 사용이 큼
>
> 2. worker MPM (멀티프로세스-스레드)
>
> - 스레드간에 메모리 공유 , prefork보다 메모리 사용량이 적음

![이미지](https://media.vlpt.us/images/moonyoung/post/48161dd4-ceed-4861-a82f-4a9effb91fb0/image.png)  

## C10K Problem

>  C10K ( Current 10 thousand Clients ) Problem

- 하나의 시스템당 동시 접속자수가 1만명을 넘어갈 때 운영방안에 관한 문제

- 하드웨어 자원이 충분함에도 불구하고 I/O 처리 방식때문에 프로세스가 제대로 처리하지 못함

- 실제로 한번에 동작하는 스레드 수는 CPU 코어 수에 종속적

- 동시 요청이 1만개이고, 요청을 다 받아들여도 실제로 처리할 수 있는 물리적인 개수는 코어 수 만큼이기 때문에 많은 커넥션이 있으면 성능이 저하됨

  

## nginx

![이미지](https://media.vlpt.us/images/moonyoung/post/79b0843e-14b4-479c-9013-1cb57c21665a/image.png)

- apache의 C10K(10000개 이상의 동시 요청 처리 문제) 해결을 위해 **비동기 이벤트 기반 구조**로 만들어짐
- 동시 요청 처리에 적합
- 고정된 프로세스에서 비동기 방식으로 task처리
- 요청이 많이 들어와도 `프로세스를 늘리지 않는다.`
- I/O같은 작업 시 별도의 스레드 풀에서 처리하고, event loop에서 에서 다른 요청 순서대로 처리 ( I/O 작업의 결과를 궁금해 하지 않으며, 기다리지 않고 I/O 작업은 끝나면 queue에 들어가 순서가 되면 처리)



**nginx 동작 방식**

![nginx](https://www.nginx.com/wp-content/uploads/2015/06/infographic-Inside-NGINX_process-model.png)
이미지 출처 - [nginx.com](https://www.nginx.com/blog/inside-nginx-how-we-designed-for-performance-scale/)
4코어 서버에 마스터 프로세스가 캐시 관리 프로세스2개 , 워커 프로세스 4개를 만듦

- 마스터 프로세스는 설정파일 읽기 및 포트 바인딩과 같은 권한있는 작업 수행 후 소수의 자식 프로세스를 만듦

  - 캐시로더 프로세스

    - 디스크 기반 캐시를 메모리에 로드한 다음 종료

  - 캐시 관리자 프로세스

    - 캐시 구성된 크기내에서 유지, 정리

  - 작업자 프로세스

    - **이벤트 드리븐**  

      - TCP,UDP 커넥션 연결, 유저의 HTTP Request 처리, Connection 종료까지의 모든 절차를 `이벤트` 라는 개념으로 취급한다. 
      - 워커노드는 끊임없이 이벤트(커넥션 연결, http 요청 처리, 커넥션 종료) 작업을 처리하게 된다. 	
      
    - 모든 작업 수행. 네트워크 연결 처리, 콘텐츠 읽기, 디스크 쓰기, 업스트림 서버와 통신

      ```conf
      worker_process auto;
      # CPU 코어당 하나의 작업자 프로세스 실행하여 하드웨어 리소스를 가장 효율적으로 사용 ->auto
      ```
    
     - 각 작업자 프로세스는 싱글스레드 방식으로 동작
    
     - 따라서 keep-alive로 커넥션을 유지하고, 요청이 들어오지 않아 대기하고 있는 동안 메모리만 축내고 idle상태에 있는 apache의 프로세스와 다르게 쉬지않고 일을 하게됨

**오래 걸리는 작업으로 인해 이벤트들이 Blocking 된다면 ? -> Thread Pool**

working queue에서 disk I/O같은 작업이 들어온다면 뒤에 있는 작업들은 모두 대기해야할 것이다. nginx는 오래걸리는 작업(disk 읽기, 파일 전송하기)같은 작업을 처리하는 Thread Pool을 따로 만들어 놓았다.  

생성비용이 비싼 쓰레드를 미리 만들어놓고 I/O가 오래걸리는 작업이 감지되면 Thread Pool에 처리를 위임하고 worker process는 다른 이벤트를 처리한다. ( 32개의 default 스레드 수와 65535개의 default working queue)



**nginx의 단점**

- 개발자가 실수로 프로세스를 종료하게 되면 프로세스가 관리하고 있던 모든 커넥션이 끊기게 된다. 모듈을 직접 만들기 까다롭다

**nginx의 동적인 설정 변경**

![이미지](https://oopy.lazyrockets.com/api/v2/notion/image?src=https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2Ff5826432-bf1c-4e93-b16b-d5310f22ec8e%2FUntitled.png&blockId=3320f53c-31da-4b11-8b02-b6e9f1ed2d1b)

  

- 엔진엑스 서버가 동작하고 있을 때도, 재부팅 없이 동적으로 설정을 변경할 수 있다. 
- service reload
- 마스터 프로세스는 새롭게 워커노드를 만들고, 이전 워커노드에는 새롭게 이벤트를 할당하지 않고 기존 커넥션을 모두 처리하고 나면 새로운 워커 프로세스에만 이벤트를 전달하고 커넥션이 모두 처리되면 이전 워커노드 프로세스는 종료한다. 



**아파치와 엔진엑스**

- 아파치는 Window, Unix모두 동일한 성능을 보장하지만, nginx는 window에서는 완벽한 지원이 안된다.
- 아파치는 디렉토리별로 추가 구서을 하는 것을 허용하지만 nginx는 추가구성을 허용하지 않는다. 
- 아파치는 동적 모듈을 지원하는데, 지원하는 모듈에 대한 오버라이딩으로 커스텀할 수 있다. Nginx는 아직 제약이 있다.
- 보통 커넥션 제어와 정적 컨텐츠 처리는 nginx를 리버스 프록시 서버로 두어 활용하고, 아파치를 백엔드 서버로 두어 안정적인 구성을 이룰 수 있다. 
- 추가적으로 nginx는 L7 로드밸런싱, 캐싱, SSL 등의 처리도 할 수 있다. 

- 출처
  - https://www.nginx.com/blog/inside-nginx-how-we-designed-for-performance-scale/
  - https://ariels1996.github.io/til/til22/
  - https://sihyung92.oopy.io/server/nginx_feat_apache
- 이미지 출처 - [velog moonyoung]



---

**Blocking I/O**

- 서버가 클라이언트로 부터 데이터를 받기 위해 recv() 함수를 호출
- 클라이언트로부터 데이터가 다 올때까지 멈춰있는 상태
- 많은 일을 하는, 많은 사용자를 처리하는 서버에는 적합하지 않음

**Non-Blocking I/O**

- 요청한 I/O를 그 상황에서 할 수 있으면 하고 할 수 없으면 다른 작업을 함
- 클라이언트로부터 데이터를 다 받을 때 까지 **확인**하는 작업이 필요해짐
  - Polling

### I/O 다중화(Multiplexing)

하나의 통신 채널(프로세스)를 통해서 둘 이상의 데이터를 전송하는 기술  

각 파일을 처리할 때 각각의 I/O 통로를 만들고 각각 프로세스(스레드)를 할당하게 되면 프로세스 간의 통신을 위해 IPC가 필요하고 프로세스간 동기화, 컨텍스트 스위칭의 오버헤드가 발생한다.  

이를 보완하기 위해 하나의 채널을 통해 프로세스 수를 최소한으로 유지하면서 여러개의 파일을 처리하기 위한 방법이다.  한 프로세스에서 여러개의 파일을 다루기 위하여 fd배열을 통해 관리하며, 데이터 변경을 감시할 fd을 배열에 넣고 배열에 포함된 fd에 변경(읽기, 쓰기, 에러)등이 발생하면 fd에 대응되는 배열에 flag로 표시된다.  

개발자는 fd 배열의 flag로 변경을 감지하고 처리할 수 있다. 

> - 파일 디스크립터  
>   - 유닉스 계열 시스템에서 프로세스가 파일을 다룰 때 사용하는 개념으로 프로세스에서 특정 파일에 접근할 때 사용하는 추상적인 값이다.   
>   - 프로세스에서 열린 파일의 목록을 관리하는 테이블의 인덱스
>   - 리눅스는 소켓, 파일 모두 파일로 취급하고 각각의 프로세스는 File descriptor 테이블을 가지고 있다. 
> - Polling
>   - 다른 장치나 프로그램의 상태를 주기적으로 검사하여 조건에 만족할 때 처리하는 방식

poll(), select(), epoll 시스템 호출을이용해 여러 파일 디스크립터를 하나의 프로세스로 관리한다. 이러한 시스템 콜은 파일 디스크립터 상태 변화를 모니터링 할 수 있다. 

- **select** 
  - 지정한 소켓의 변화를 확인하고자 사용하는 함수로 소켓의 변화가 생길 때 까지 기다리다가 소켓이 동작을 하면 동작한 소켓을 제외한 나머지 소켓을 제외하고 해당 소켓에 대한 확인을 진행한다. 
  - 디스크립터 수가 제한되어있다. 
  - 파일 디스크립터 수만큼 계산을 해야한다 O(N)
  - 사용이 쉽고 OS 이식성이 좋다.
- **poll**
  - 디스크립터 수 제한이 없고, 처리방식은 select와 비슷하다.
  - 여러개의 파일 디스크립터를 동시에 모니터링하다가 한개라도 읽을 수 있는 상태가 되면 블록킹을 해제한다. 
  - 하나의 fd 이벤트 처리를 위해 64비트를 전송해야한다.(단점)
  - 이식성이 나쁜 편이다.
- select와 poll은 감시하는 파일디스크립터 수만큼 루프를 돌려야하고, 변화가 생길때까지 block된다.
- **epoll**
  - ms, freebsd계열에서는 또 다른 방식이 있다 (kqueue 등..)
  - Linux 2.5.44에서 도입되어 파일디스크립터 수에 제한이 없으며 상태변화 모니터링이 개선되었다.
  - 파일 디스크립터 상태를 **커널에서** 감시하고 변화된 내용을 직접 확인하기 때문에 루프를 사용한 모니터링이 필요하지 않다. 
  - 커널에 추가적인 자료구조를 구현한다. 
  - events에 들어있는 데이터를 O(1)에 읽어온다. 



### Async-Blocking I/O model

- kernel space로 부터 데이터를 user space로 전달하는 것을 kernel 완료 시점에서 (sync)할것인지, 완료응답을 kernel space에서 받고 user space에서 system call을 통해 요청하여 완료된 데이터를 받을것인지(async)

- async-blocking을 사용하면 한개의 process가 다중으로 Socket에 대한 처리를 할 수 있는데, I/O의 다중화를 위해 poll(), select(), epoll() 시스템 콜을 이용해 여러 fd를 하나의 process로 관리하여 공유한다. 
- **이때 poll(), epoll()같은 멀티플렉싱 관련 시스템콜에 대한 kernel의 응답을 기다리는 동안 Blocking된다** 

### Async-NonBlocking I/O

- user space에서 kernel space로 완료 응답을 받기위해 대기하지 않고, 응답이 오기 전까지 다른 일을한다. 이때 nginx는 epoll(linux)를 사용한다. 
- epoll (linux 2.6이상부터) 
  - select와 다르게 커널 공간이 fd를 관리하여 select보다 빠르게 event processing이 가능해진다(어떤 fd에서 변경이 일어났는지)
- master process에서 worker process를 생성할 때 kernel 공간에 있는 epoll structrue에 epoll_create를 실행해 epoll 구조체를 생성하고, listen socket descriptor를 등록한다. worker process는 listen socket에 액세스할 수 있으며 동시에 들어오는 요청을 공유하고 수행할 작업이 없으면 epoll_wait을 사용해 대기상태로 빠진다. 
- listen socket에 새로운 client 요청이 들어오면 kernel은 대기중인 epoll중에 가장 최근에 추가된 worker process를 선택하여 event를 전송하고 worker process는 non blocking으로 accept system call을 전송하여 client와 connection socket을 생성한다. 



- 출처 

  - https://cyuu.tistory.com/172

  - https://grip.news/archives/1304
  - https://applefarm.tistory.com/144

---


