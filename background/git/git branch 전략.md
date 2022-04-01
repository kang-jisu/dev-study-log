## git branch 전략

- 여러개발자가 협업하는 환경에서 git 저장소를 효과적으로 활용하기 위한 work-flow
- 브랜치의 생성, 삭제, 병합이 자유로운 git의 유연한 구조를 활용하여 다양한 방식으로 소스 관리
- 브랜치전략이 있기 때문에 최신브랜치, 특정 기준 브랜치를 구분할 수 있음



### 자주 쓰이는 브랜치 전략

- git-flow
  - 5가지의 브랜치를 이용해 운영하는 전략
- github-flow
  - master브랜치와 Pull request를 활용한 전략



### git-flow

2개의 메인브랜치와 3개의 보조브랜치로 구성

- 메인 브랜치 : 항상 유지
  - master : 제품으로 출시될 수 있는 브랜치
  - develop : 다음 출시 버전을 개발하는 브랜치
- 보조 브랜치 : merge되면 사라짐
  - feature : 기능을 개발하는 브랜치
  - release : 이번 출시 버전을 준비하는 브랜치
  - hotfix : master 출시 버전에서 발생한 버그를 수정하는 브랜치

1. 개발자는 develop브랜치로부터 본인이 개발할 기능을 위한 feature브랜치를 만든다.
2. feature브랜치에서 오류가 발생한다면 release 브랜치 내에서 수정한다. QA가 끝났다면 버정늘 배포하기 위해 matser브랜치로 merge, bugfix가 있었다면 해당 내용을 반영하기위해 develop브랜치에도 merge
3. master에서 버그가 발생하면 hotfix 브랜치를 만들어 버그 수정이 끝나면  develop, master로 merge



### 미션방식

```
master(upstream공통 저장소/master브랜치)

develop(upstream/id브랜치)

기능구현(feature)가 origin/step1, local/step1브랜치

origin은 fork한거, local은 컴퓨터에 받아온거 
```

- develop 브랜치에서 개발해야할 feature브랜치를 만든다.
  - upstream/본인id에서 origin/step1을 만듦
- feature에서 만들다가 기능이 완성되면 develop에 merge
  - local/step1에서 만들다가 완성되면 origin/step1으로 push후 upstream/본인id로 pr요청
  - 리뷰를 마치면 upstream/본인id 브랜치로 merge
- master->main으로도 많이 명명



### git-flow특징

- 주기적으로 배포하는 서비스에 적합
- 가장 유명한 전략인 만큼 많은 IDE가 지원



## github-flow

제품이 릴리즈되는 최신버전인 master브랜치만 존재



### 개발 프로세스

- 어떠한 이유로든 branch를 생성, git-flow처럼 체계적인 분류가 없기때문에 브랜치 이름은 의도가 잘 드러나도록 작성
- 개발, 커밋메시지 상세히 작성
- pull request생성
- 충분한 리뷰, 토의
- 서버나 테스트환경에 배포 
- 이상이 없다면 master에 merge, push후 즉시 배포 

배포 자동화도구로 merge가 일어나면 바로 배포하도록 설정해주는 경우가 많음.



### 특징

- 브랜치 전략이 단순
- CI/CD가 자연스러움



## 어떤 전략을 사용할까

1. 한달이상의 긴 호흡, 주기적 배포, QA, hotfix를 수행한다면 git flow
2. 항상 릴리즈가 되어야하며 지속적으로 테스트하고 배포하는 팀이라면 githubflow



- 출처
  - 웨지의 Git 브랜치전략 https://www.youtube.com/watch?v=jeaf8OXYO1g

