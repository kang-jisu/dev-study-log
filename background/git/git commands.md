## Git Commands

```
# 새로운 저장소 만들기
git init

# 저장소 받아오기
git clone /로컬/저장소/경로

# 원격 서버의 저장소 복제
git clone 사용자명@호스트:/원격/저장소/경로

```

**작업의 흐름**

- working directory
- 인덱스 (staging area)
- HEAD 최종 확정본 (commit)

**추가와 확정 (commit)**

```
git add <파일이름>
git add *

git commit -m "설명"
#HEAD에 반영, 원격저장소에는 아직 반영 안됨
```

**변경 내용 발행(push)하기**

```
git push origin master
git remote add origin <원격서버주소>
```

**가지치기**

```
git checkout -b feature_x # branch, switch로 사용
git branch -d feature_x

git push origin <branch_name> # push해야 다른사람 접근 가능
```

**갱신과 병합**

```
git pull # 로컬 저장소를 원격저장소에 맞춰 갱신 fetch, merge

# 다른 브랜치에 있는 변경 내용을 현재 브랜치에 병합
git switch 현재
git merge 브랜치이름
```



**되돌리기**

```
git checkout -- <파일이름> 
git restore <파일이름>

# 로컬에 있는 모든 변경내용,확정본 포기하고 원격저장소의 최신 이력을 가져와 로컬의 master가 저 이력을 가리키도록 함
git fetch origin
git reset --hard origin/master
```



- 출처
  - https://rogerdudler.github.io/git-guide/index.ko.html



### 브랜치

```
동일한 소스코드를 기반으로 서로 다른 작업을 할 때 동시에 독립적인 작업영역을 생성하고 원래 버전과 비교해 새로운 버전을 만들어내는 작업을 할 수 있다. 
```



```
git branch step1
```

```
% git branch
* main # 아직 main임
  step1
```

checkout 기능이 너무 많아서 분리

- switch
  - 브랜치 변경 담당
- restore
  - 워킹트리 파일 복원 (git add를 통해 stage에 이미 넣은 파일도 git reset이 아니라 git restore로 복원 )

```bash
$ git swtich step1
```



### 작업 합치기

커밋0(main)->커밋1 -- 커밋3--bugFix커밋

​                            ㄴㅡ 커밋2--step1커밋

bugFix된 브랜치를 step1에 합치고싶음

```
git swtich step1
git merge bugFix
```

커밋2 , 커밋3 같은부분 수정했다면 충돌이 발생함



**merge 와 rebase**

- merge
  - 합치는 과정이 모두 히스토리에 남음
- rebase
  - 새롭게 히스토리를 써서 로그가 변경되어서 보임 (외국은 merge선호)
  - 깔끔 장점, 기록이 없어지는 단점(한국은 rebase선호)



### HEAD

- 현재 checkout된, 작업중인 커밋을 가리킴  
- 작업트리의 최신커밋
- 작업트리에 변화를 주는 깃명령어들은 대부분 head를 변경하는것으로 시작

**상대참조**

- ^ : caret    ( ^ 개수만큼 뒤로감)  
- ~ : tilde (~뒤에 숫자붙이면 그 만큼 부모커밋을 찾아감)

```bash
git switch HEAD^ # 
git switch HEAD^^
git branch -f step2 step1~2
```



### 복구

작업 되돌리기

**reset : 완전히 지우는 방법**

히스토리를 고쳐쓰기때문에 remote브랜치에서는 사용하지않음

```
git reset HEAD~1 
```



**revert : 커밋 + -커밋 느낌으로 기록은 남지만 내용은 삭제됨**

```
git revert HEAD
```



**삭제된 커밋 , 브랜치 복구** 



### Remote 원격 저장소 등록

```
git clone https://my-repo.com/
git remote -v 
git remote add upstream https://mission-repo.com//
git fetch upstream wilder
git pull upstream wilder
```



### Cherry-pick 커밋 로그를 내 맘대로

좋은것만 골라서 사용한다  

```
git log

git cherry-pick 663453 
git cherry-pick 322132

git cherry-pick 324234^..242341f # 범위지정 또는 나열 
```

