# Microservice와 Spring Cloud소개

## MicroService란

하드웨어 중심 -> 분산화된 시스템 -> Cloud Native

확장성, 안정성 강화 



**Antifragile**

- Auto scaling
  - 유지되어야하는 size를 지정하여 scale in/out
  - 수동적인 작업이 아닌 cpu, 메모리, db 사용량에 따른 자동 오토스케일링
- microservice
  - 복잡한 서비스들의 연결 
  - cloud native의 핵심으로 전체 서비스를 구축하고 있는 개별적인 모듈, 기능을 독립적으로 개발, 배포, 운영
- chaos engineering
  - 시스템이 급격한 트래픽이나 예측하지 못하는 상황에도 견딜 수 있고 신뢰성을 쌓기 위한 방법들
  - 변동, 예견된 불확실성, 예견되지 않는 불확실성, 카오스 불확실성
- Continous deployments (CI/CD)
  - 수백개의 서비스를 일일이 빌드, 테스트, 배포하는 과정을 수작업으로 하는것은 불가능
  - 자동화된 시스템을 구축

### Cloud Native Architecture

**확장 가능한 아키텍처**

- 시스템의 수평적 확장에 유연
- 확장된 서버로 시스템의 부하 분산, 가용성 보장
- 시스템 또는 서비스 애플리케이션 단위의 패키지 (컨테이너 기반 패키지)
- 모니터링

**탄력적 아키텍처**

- 분리된 서비스 생성 - 통합 - 배포, 비즈니스 환경 변화에 대응 시간 단축 (CI/CD)
- 분할된 서비스 구조
- 무상태 통신 프로토콜
- 서비스의 추가와 삭제 자동으로 감지
- 변경된 서비스 요청에 따라 사용자 요청 처리(동적 처리)

**장애 격리(Fault isolation)**

- 특정 서비스에 오류가 발생해도 다른 서비스에 영향을 주지 않음



## Cloud Native Application

- microservice
- CI/CD
- DevOps
- containers

**CI/CD**

- 지속적인 통합
  - 통합 서버, 소스 관리, 빌드 도구, 테스트 도구
  - jenkins, team ci, travis ci
  - git과같은 형상 시스템과 연동 
- 지속적 배포
  - continous delivery
    - 패키지화된 결과물을 수작업으로 배포
  - continous deployment
    - 완벽한 자동화된 배포, 자동 반영
  - pipe line
- 카나리 배포와 블루그린 배포
  - 카나리 배포
    - 일부(소수) 사용자만 새 버전 서비스를 미리 사용하여 문제가없는지 확인
  - 블루그린배포
    - 블루(이전) -> 새버전(그린)배포시 바로 변경

**DevOps**

- Deployment + Operations + QA
- 자주 테스트하고 피드백, 업데이트하며 지속적으로 끊임없이 개선

**Container 가상화**

기존 하드웨어, 서버 가상화에 비해 적은 리소스를 가지고 가상화를 할 수 있음  

하드웨어+OS -> host OS(or 하드웨어)+하이퍼바이저+guest OS -> 호스트 OS 공유하여 container runtime 위에 필요한 라이브러리, 리소스만을 바탕으로 독립적인 환경 생성



### 12 Factors

Cloud native application 개발시 고려해야할 항목 12가지 

- 코드 베이스 CODE BASE
  - 버전 제어, 형상관리, 코드 통일적 관리
- Dependency isolation 
  - 각 마이크로 서비스는 전체 시스템에 영향을 주지 않는 상태에서 변경되어야함
- Configurations
  - 코드 외부에서 구성관리도구를 통해서 작업 제어 
- Linkable backing service
  - 보조 서비스를 이용해서 지원 (메세지서비스나,, )
- stage of creation
  - 빌드,릴리즈 등 실행환경 분리 
  - 롤백, ci/cd
- stateless processes
  - 각각의 ms는 분리된 채 운영될 수 있어야함 
  - 필요한 자원은 캐시, 데이터저장소 사용 
- port binding
  - 자체 포트에서 노출되는 인터페이스 및 기능과 함께 자체 포함되어있는 기능이 있어야함
- concurrency
  - 동시성 , 여러 서비스가 동시에 
- disposability
  - 삭제가 가능해야되고, 정상 종료, 확장 가능해야함 
- development & production parity
  - 개발단계와 production단계 분리 
- logs
  - 로깅시스템 
  - 로그를 이벤트스트림으로 처리 -> 모니터링 도구 사용 
- admin processes for eventual proccess
  - 어떻게 운영되고있는지 파악하기 위한 적절한 관리도구 



**12 factors +3 **

1. API first
   1. API로 서비스되어야함 , 사용자측에서 어떻게 쓸것인지 고민해야함
2. Telemetry
   1. 모든 접근은 수치화, 시각화
3. Authentication and authorization
   1. API 사용에 있어서 적절한 인증 로직이 구현되어야함 

### Monolithic vs MSA

**monolithic**

- 모든 요소를 하나의 소프트웨어 안에서 처리 , 의존성을 가짐 
- 모든 업무 로직이 하나의 애플리케이션 형태로 패키지되어 서비스

**microservice**

- 어플리케이션을 구성하는 각각의 구성요소를 분리해서 개발 , 유지보수에 유리 , 하나가 변경되었을 때 필요한 부분만 개발하고 영향을 최소화하며 독립적으로 배포 가능, 전체가 down되는 위험 줄어듦
- 함께 작동하는 작은규모의 서비스들 
- HTTP 통신을 이용해서 API통신을 할 수 있는 작은 규모의 여러 서비들의 묶음
- 비즈니스 기능을 중심으로 구축되며 완전하게 자동화된 배포시스템을 사용한다. 
- 각각의 서비스들은 최소한의 중앙집중식 관리, 서로 다른 프로그래밍 언어와 저장 기술을 사용한다. 

**Monolith vs front & back 분리 vs microservice**  

하나의 어플리케이션에 모든 로직과 서비스가 포함되기보단은 front, back을 분리하는 방식도 microservice로 가는 단계 

- 프론트엔드를 구분하고, 통합적인 레이어를 두고, 그다음 상품 서비스, 장바구니 서비스, 결제 서비스등을 나누는 마이크로서비스로 발전

```
API Gateway - Zuul Edge Server
Service Discovery - Eureka
Load Balancer - Ribbon
Circuit Breaker - Hysterix 등
```



### Microservice Architecture란?

```
2002년 편지 
- 모든 서비스 인터페이스를 통해서만 통신 (다른형태는 허용 X)
- 모든 서비스 인터페이스는 외부에 공개될 수 있어야함 . 
```

1. Challenges
   1. 기존 개발 방식, 패러다임을 바꿈
2. Small well chosen Deployable units
   1. 각각의 서비스는 기능에 따라 작게 나눠지기도하고 단일화된 하나가 되기도 하고
3. bounded context
   1. 경계
4. restful
   1. 서로 상태에대해서 Rest api방식으로 통신 (HTTP 기반)
5. configuration management
   1. 환경설정정보를 외부시스템에 의해서 관리 (하드코딩 X)
6. cloud enabled
   1. cloud native기술 최대한 사용 , gateway등 
7. dynamic scale up and scale down
   1. 부하분산, scape up, out, 동적으로 처리. 서비스마다 개수 다를수도있음
8. ci/cid
   1. 자동빌드,테스트, 배포
9. visibility
   1. 시각화



## SOA

service oriented architecture : 재사용을 통한 비용 절감

- 기술 방식
  - 공통 서비스를 enterprise service bus(esb)에 모아 공통 서비스형식으로 서비스 제공

msa : 서비스간의 결합도를 낮추어 변화에 능동적으로 대응 

- 기술 방식
  - 각 독립된 서비스가 노출된 REST API를 사용
  - 각각의 서비스들은 다른 기술, 언어 사용
  - **kafka**
    - Event stream방식과 같은 메세징 서비를 이용해서 데이터 전달 
    - 각각의 데이터베이스안에 저장된 데이터들을 카프카로 전달하기만하면 데이터가 관심있다고 등록(subscribe)했던 개체들한테 배달해줌 



### RESTful Web Service

- 용도와 상태에 맞게 HTTP Method 제공 , uri로 리소스 표현 (LEVEL2) -- 여기까지는 해야함 
- Hateoas (데이터로 어떤 액션을 할 수 있는지) 추가 (LEVEL3)



**고려사항**

```
- 소비자(API 사용자) 입장
- HTTP 장점 최대한 살림
- Request methods GET POST PUT DELETE
- Response Status 상황에 맞게 
	 - 200(정상처리),404(요청한리소스가없음),400(일반실패),201(생성),401(인증실패) 
- URI에 secure 정보 담지 않음
- 복수형태의 uri (/users/1)
- 가능한 명사형태로 표시
- 일괄적인 endpoint사용 ->api gateway활용할 수 있을 것 
```



### MSA 표준 구성요소

Service Discovery, API Gateway, Orchestration, 컨텍스트 바운더리 등 연동 

![img](https://t1.daumcdn.net/cfile/tistory/99A060455C70137A29)  



클라이언트 - > API gateway로 요청 -> service router, service discovery로 어디로가야할지 정함 -> A,B 인스턴스 나누어짐 

로드밸런서에 의해 분산된 서비스 중 어디로 보내질지 정함 

환경설정정보는 Config store에 지정(외부) , 컨테이너 가상화기술을 이용해 배포 

배포는 CI/CD 사용

Backing Service, Messaging service, Telemtry(모니터링,진단)



**Service Mesh - 추상적 계층 **

- 설정정보, 라우팅, 인증, 로드밸런싱, 암호화 등등을 지원

- 서비스간 통신을 추상화하여 빠르고 신뢰할 수 있게 만드는 infrastructure layer

```
- Service Discovery
- Circuit Breaking
- Distributed Tracing
- Load Balancing
- Retry and Timeout metrics
- Dynamic Request Routing
- TLS 
```

서버 인스턴스 앞단에 Proxy를 두어 healthcheck, routing 제외, 500에러 내면 제외 등 

ex) istio, envoy

MSA 내부 - mesh한 네트워크이기 때문에 추상화해서 복잡한 내부 네트워크 제어, 추적, URL 경로, 호스트 헤더, API 버전 

Routing Rule, circuit breaker 설정





**CNCF (Cloud native computing foundation)**

**MSA 기반 기술**

- gateway
  - nginx, netflix zuul
- service mesh
  - istio, envoy, etcd, zookeeper, grpc,
- runtime (운영 환경, 컨테이너가상화)
  - rkt, docker, k8s, aws ecs, lambda,
- frameworks
  - spring boot, spring cloud flask, 
- backing services
  - rabbitmq, kafka, redis, mongoDB
- automation
  - gradle, maven, jenkins, 
- telemetry
  - elasticsearch, fluentd



## Spring Cloud란

https://spring.io/cloud



### API Gateway Service

사용자가 설정한 라우팅 설정에 따라서 각각 엔드포인트로 클라이언트 대신해서 요청해주는 프락시역할을 한다. 

주문, 결제, 상품 서비스 엔드포인트를 직접적으로 호출하는것보다 단일 진입점을 가지고있는 게이트웨이 역할을 해주는 프록시서버를 두어 일괄적으로 처리 

- 인증 및 권한 부여
- 서비스 검색 통합
- 응답 캐싱
- 정책, 회로 차단 
- 속도 제한
- 부하 분산
- 로깅 , 추적, 상관관계 (진입점, 중간단계 등 )
- 헤더, 쿼리 문자열 및 청구 변환
- IP 허용 목록에 추가 



### Apache kafka 

- Apache software foundation의 scalar언어로 된 오픈소스 메시지 브로커 프로젝트
  - Open source message broker project
- 링크드인에서 개발
- 실시간 데이터 피드를 관리하기 위해 통일된 높은 처리량, 낮은 지연시간을 지닌 플랫폼 제공
- RabbitMQ와 비슷하지만 kafka가 대용량 시스템에 더 만히 사용



```
Mysql, Oracle, MongoDB, APP, file 등 다양하게 데이터가 쌓이고 있음  

Hadoop을 이용한 대용량 처리, search engine, monitoring, email등이 필요한 상태
```

- End to end 연결 방식의 아키텍처
- 데이터 연동의 복잡성 증가 (HW, 운영체제, 장애 등)
- 서로 다른 데이터 pipeline 연결구조
- 확장이 어려운구조 

이런 문제점을 해결하기 위해서 중간에 카프카라는 시스템을 두어 전송하는 데이터가 어떤 시스템에 저장되는지 관계없이 카프카 하나만 처리, 카프카에 쌓인 데이터들은 카프카를 통해서 얻어오면되니 보내는쪽과 받는쪽이 누가 보냈는지 신경쓰지 않아도 됨 

- Producer / Consumer 분리 
- 메세지를 여러 Consumer에게 허용 
- 높은 처리량을 위한 메시지 최적화
- Scale-out 가능
- Eco-system 



**Kafka Broker**

- 실행된 Kafka 애플리케이션 서버
- 3대이상의 broker cluster 구성 권장 (가용성)
- zookeeper 연동
  - 역할 : 메타데이터(broker id, controller id)저장
  - controller 정보 저장 
- n개 borker 중 1대는 Controller기능 수행
  - Controller 역할
    - 각 broker에게 담당 파티션 할당 수행 
    - borker 정상 동작 모니터링 관리 



## 가상화

물리적인 컴퓨터 리소스를 다른 시스템이나 애플리케이션에서 사용할 수 있도록 제공

- 플랫폼 가상화
- 리소스 가상화

하이퍼바이저

- native or baremetal 하드웨어에서 직접적으로
- hosted - 하드웨어 위에 os+ 하이퍼바이저 소프트웨어 

**OS 가상화**

- Host os위에 guest os 전체를 가상화 
- vmware, virtual box (하이퍼바이저 역할)
- host os의 자원을 쪼개서 사용하기 때문에 자유도가 높으나 시스템에 부하가 많고 느려짐

**컨테이너 가상화**

- host os가 가진 리소스를 적게 사용하며 **필요한 프로세스 실행**
- **최소한의 라이브러리**와 도구만 포함
- 하이퍼바이저 위치에 docker engine, 컨테이너 가상화 솔루션이 올라옴 
- container 생성 속도 빠름 ( 리눅스 부팅 과정 이런것도 없고 최소한의 내용만 가지고 생성가능)

**컨테이너 이미지-> 실체화 컨테이너**

이미지 저장소 - registry, docker hub또는 private registry 공간에 이미지를 올림

docker host에서는 저장소에서 local repository에 이미지를 다운로드받아 컨테이너로 실행 (create생성, start실행, run생성+실행)

**Dockerfile**

이미지를 생성하기위한 스크립트파일 , 자체적인 문법 사용하여 이미지 생성과정 기술

```
FROM
ENV
ADD
EXPOSE
CMD
```



### 도커 컨테이너 네트워크

- ip주소대역 172.17.0.x
- 