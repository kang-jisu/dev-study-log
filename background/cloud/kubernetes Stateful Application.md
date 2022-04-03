## Cloud Native Storage for Kubernetes : Kubernetes Stateful Application데이터 저장 방법

- 출처 https://tv.naver.com/v/23652453



공부할내용 많은것같은데 초반 내용말고는 좀 어려워서 나중에 다시 봐도 좋을듯 ~~   



## Stateful Application

매번 요청시마다 동일하게 처리하면되는 웹서버와 달리, 요청 정보를 관리하고 저장하는 DB가 있다. 

웹서버 - stateless application

- 아키텍쳐가 단순
- 장애시 복구가 쉬움

mysql - stateful application

- 아키텍쳐 복잡
- 장애시 데이터 유지 위한 방안이 필요함



cloud native application

- msa로 구성
- 대규모환경에 적합
- 컨테이너화하여 동작
  - 컨테이너 : 제한된 공간에서 동작할 수 있도록 리눅스 네임스페이와 cgroup을 이용해 OS를 추상화
  - 컨테이너는 기동중일 때만 데이터를 유지
  - permenant하려면 volume mount가 필요
- stateful application은 데이터를 저장하기 위한 스토리지가 필요하지만, 컨테이너자체는 stateless하기 때문에 컨테이너를 외부에 저장하기위한 스토리지가 필요하다.



### Cloud Native Application in K8s

kubernetes 에서는 Deployment, statefulset을 제공하고 있으며 stateful application은 deployment를, stateful application은 Statefulset을 사용할 수 있다.

- deployment
  - 동일한 역할을 하는 pod의 복제본
  - pod name도 hash값
  - 생성, 삭제에 별다른 순서가 없음
  - 모든 파드에서 공유되는 스토리지 사용하면 됨
- statefulset
  - pod name에 고유한 이름 할당 (0,1,2,)
  - 이름별로 순차적으로 생성, 역순으로 삭제
  - 데이터 저장을 위한 persistent volumn은 pod와 1:1 매핑



```
즉, 
Stateful Application 서비스를 위해 Remote storage(외부저장, 확장가능), Thin provisioning(사용한 만큼만 할당), Cloud Native Storage 사용
```



## Cloud Native Storage

### Cloud Native의 특징

- 수평 확장 
- No single point of failure
- resilient and survivable
- 자동화를 통해 overhead최소화
- 느슨한 결합



### ceph storage 

```
오픈소스 스토리지이며, 레드햇을 통해 상용버전으로도 사용 가능
```

- 하나의 스토리지에서 Object, Block, Shared Storage 모두 제공 
  - Object
  - Block
    - RBD
  - CEPHFS
    - k8s의 stateless 에 사용중



### Multiple storage type

쿠버네티스에는 block, shared storage를 제공하고 있음  



### Open stack

```
클라우드 컴퓨팅 오픈소스 프로젝트, 다양한 하위 프로젝트로 구성되어있음

API를 통해 인프라 관리 
```



 