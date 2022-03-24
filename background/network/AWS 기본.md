

### 클라우드 컴퓨팅

```
- 개인용 컴퓨터가 아닌 인터넷을 통해 연결된 컴퓨터를 활용하는 기술
- 성능이 뛰어난 컴퓨터나 저장장치 등의 컴퓨터 자원을 다른곳에서 빌려 사용할 수 있도록 처리 

특징
- 언제 어디서나 인터넷을 사용해 쉽게 자원에 접근 가능
- 자원을 늘리거나 줄이는 것도 가능
- 사용량 기반 과금
```



### 클라우드 컴퓨팅 서비스 이용 방식

- IaaS (Infrastructure-as-a-service)
  - 물리적 서버(CPU, Memory, OS), 네트워크, 스토리지를 가상화하여 다수의 고객을 대상으로 유연하게 제공하는 인프라 서비스
- PaaS(Platform-as-a-services)
  - WEB 기반의 서비스 또는 애플리케이션 등의 개발 및 실행을 위한 표준 플랫폼 환경을 서비스 형태로 제공
- SaaS(Software-as-a-services)
  - 구글의 Gmail이나 MS offices같이 응용프로그램을 인터넷 및 웹브라우저를 통해 제공하는 서비스 



### VPC Virtual Private Cloud

- AWS 클라우드에 논리적으로 격리된 공간을 프로비저닝하여 고객이 정의하는 가상 네트워크에서 AWS 리소스 시작할 수 있음 
- 예시
  - 인터넷에 액세스 할 수 있는 웹 서버는 퍼블릿 서브넷에 
  - 인터넷 액세스가 없는 프라이빗 서브넷에 데이터베이스나 어플리케이션 서버같은 백엔드 시스템 배치
  - 보안그룹 및 네트워크 ACL을 포함한 다중 보안계층을 사용하여 서브넷에서 EC2인스턴스에 대한 액세스 제어 

### Subnet

- 서비스 목적에 따라 나누어 구분된 IP Block 
- Public Subnet
  - 네트워크 트래픽이 인터넷 게이트웨이로 라우팅되는 서브넷
  - 라우팅 테이블에서 0.0.0.0/0을 igw로 라우팅 
- Private Subnet
  - 인터넷 게이트웨이로 라우팅되지 않는 서브넷
  - 라우팅 테이블에서 0.0.0.0/0을 nat로 라우팅 ( private->외부는 가능한데 외부->private은 안됨 )

### IGW 인터넷 게이트웨이

- VPC 내부 네트워크가 외부와 연결되는 구성을 담당
- 한개의 VPC에 연결되며 퍼블리 서브넷의 인스턴스들과 1:1 NAT를 완전관리형으로 제공 
  - public 주소가 할당된 인스턴스에 대해 NAT(네트워크 주소 변환)을 수행 
- 생성한 인터넷 게이트웨이를 VPC에 연결해주고 라우팅 테이블에 Public Subnet이 IGW로 가도록 설정해주어야함

### NAT Network Address Translation

- 내부 IP주소를 외부 IP주소로 변환하는 작업을 수행하는 서비스

  - 사설 네트워크에 속한 여러개의 호스트가 하나의 공인 IP주소를 사용하여 인터넷에 접속하기 위함

- private subnet 내에 있는 인스턴스를 인터넷, 다른 aws 서비스에 연결하고 외부망에서 해당 인스턴스에 연결하지 못하도록 함 

- NAT자체의 동일한 Public IP를 생성해야 하므로 EIP 할당 필요 

- private subnet과 RT에 nat 연결

- > private -> private RT -> nat gw(public subnet에 존재)
  >
  > nat gw -> public RT -> igw rnat

### VPC Endpoint

- IGW, NAT, VPN 연결 또는 Direct connect 연결 없이 VPC 엔드포인트 서비스에 여녈할 수 있음 

### 네트워크 ACL(Access Control List) 액세스 제어 목록

- 서브넷의 패킷 권한을 확인하는 VPC 구성요소
- 서브넷 수준에서 인바운드 및 아웃바운드 트래픽 제어 
- 기본 네트워크 ACL : 모든 인바운드, 아웃바운드 허용
  - 사용자 지청 네트워크 ACL : 사용자가 지정한 허용 트래픽 이외의 모든 트래픽 거부
  - 명시적 거부 규칙
- 상태 비저장 패킷 필터링
  - stateless하다
  - 아무것도 기억하지 않고 각 방향으로 서브넷 경계를 통과하는 패킷만 확인
  - 패킷이 서브넷에 들어간 후에는 서브냇 네의 리소스에 대한 권한은 보안그룹에 의해 평가된다.

### 보안그룹 Security Group

- Amazon EC2 인스턴스에 대한 인바운드 및 아웃바운드 트래픽을 제어하는 가상 방화벽
- 기본적으로 모든 인바운드 트래픽을 거부하고 모든 아웃바운드 트래픽을 허용한다.
- 서브냇 내에 여러 인스턴스가 있는 경우 동일한 보안그룹에 연결하거나 서로 다른 보안그룹을 사용
- 네트워크 트래픽에 대한 **허용(Allow)** 가능 (차단 기능은 ACL을 통해 서브넷 수준에서 제어)
- 상태 저장 패킷 필터링
  - statefull 
  - 들어오는 패킷에 대한 이전 결정을 기억한다. 



## 스토리지

### Amazon EBS ( Elastic Block Store )

- 블록 수준 스토리지 볼륨
- 파일을 수정하면 **변경된 부분만 업데이트**
- EC2 인스턴스 종료후에도 생존
- *단일 가용영역*에 데이터 저장 , EC2 인스턴스와 같은 가용영역에 상주해야함
- SSD+ HDD

### Amazon S3 (Simple Storage Service)

- 객체 스토리지
  - 각 객체는 데이터, 메타데이터, 키로 구성됨
  - 데이터 : 이미지, 동영상, 텍스트 문서 또는 기타 유형의 파일
  - 메타데이터 : 데이터의 내용, 사용방법, 객체 크기 등에 대한 정보
  - 객체의 키 : 고유한 식별자
- 파일을 수정하면 **전체 개체가 업데이트**
- 데이터를 버킷에 객체로 저장 
- 특징
  - 한번 쓰기, 여러번 읽기 ( WORM )
  - 모든 유형의 파일 업로드 가능
  - 저장공간 무제한, 객체 최대 파일 크기 5TB
  - 파일 업로드시 파일에 대한 표시 여부 및 액세스 권한 제어 가능
  - S3 버전 관리 기능을 사용해 시간경과에 따른 객체 변경사항 추적 가능
- 데이터 보관 이외에도 정적 웹사이트 호스팅 및 다양한 서비스로 활용 가능 

### Amazon EFS (Elastic File System)

- 파일 스토리지
- 여러 클라이언트가 **공유 파일 폴더에 저장된 데이터에 액세스**할 수 있음 
- 스토리지 서버가 블록 스토리지를 로컬 파일 시스템과 함께 사용하여 파일을 구성하고 클라이언트는 파일 경로를 통해 데이터에 액세스 
- **리전별 서비스**로, *여러 가용영역에 걸쳐* 데이터 저장 

### Amazon RDS (Relational Database Service)

- 관계형 데이터베이스
- SQL을 사용하여 데이터를 저장하고 쿼리
- 하드웨어 프로비저닝, 데이터베이스 설정, 패지 적용, 백업과 같은 작업을 자동화하는 관리형 서비스 
- Amazon Aurora, PostgreSQL, MySQL, MariaDB, Oracle, Microsoft SQL Server 등
- 비즈니스적 복잡한 관계의 join
- 고객이 데이터, 스키마를 소유하고 네트워크를 제어 

### Amazon DynamoDB 

- 비 관계형 데이터베이스 (noSQL)
- 행과 열이 아닌 구조를 사용하여 데이터를 구성
- 키-값 
- 대규모 처리
- 복잡한 관계성이 없는 단순 look up 테이블

### 기타 데이터베이스 서비스

- Amazon DocumentDB 

  - MongoDB

- Amazon ElasticCache

  - Redis, Memcached  

    



### 로드밸런서 

- NLB
  - network LB (L4)
  - connection-based
  - low latency
  - static ip지원
  - AWS가 관리
- ALB
  - Application LB (L7)
- 로드밸런싱 방식
  - Round Robin 
    - 연결을 순차적으로 맺어주는 방식
    - Session 보장 X
  - Hash
    - 해쉬 알고리즘 이용 
    - Client-Server간 세션 유지
    - Session 보장 
  - Least Connection
    - 가장 작은 Session 보유한 서버로 Session을 맺어줌
    - Session에 대한 보장 X