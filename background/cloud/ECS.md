## Aws Elastic Container Service (ECS)

1. 작업 명세(Task Definition)과 작업 (Task)

- 작업 명세 : 컨테이너로 배포할 어플리케이션에 대한 다양한 파라미터 집합
  - 어플리케이션에 사용할 컨테이너 이미지, 포트 정보, 데이터 볼륨을 정의
- 작업 : 작업 명세 인스턴스화 한 객체

2. 클러스터
   - EC2 리소스의 논리적인 그룹으로 작업이 실행되는 공간

3. 컨테이너 에이전트
   - ECS 클러스터를 구성하는 인프라 자원마다 실행
   - 인프라 자원의 리소스 사용률을 수집하여 ECS에 전달하고, ECS로부터 작업의 시작/중지 요청을 받아 Agent가 실행중인 인프라에서 수행한다.

![img](https://blog.kakaocdn.net/dn/b5IRlB/btqFuV0dDyU/8sexjXcw6lKwUVukyqBlqK/img.png)



4. 클러스터 템플릿
   1. 네트워킹 전용 ( Fargate Launch type)
      1. AWS가 관리하는 서버리스 인프라 클러스터 위에 배포
   2. EC2 + 네트워킹
      1. EC2인스턴스들로 구성된 클러스터에 Task 배포
      2. 인스턴스 운영체재를 Linux, Window 선택할 수 있음



### Task definition

- json 파일 복사해다가 쓸 수 있음
- httpd 컨테이너 80포트로 노출
- 작업 정의는 실제로 작업으로 만들어줄 서비스 객체를 배포해야함