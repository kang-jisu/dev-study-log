## Graceful shutdown

```
우아한 끝내기 
; 프로그램이 종료될 때 최대한 side effect가 없도록 로직들을 잘 처리하고 종료하는 것
```



### SIGINT, SIGTERM, SIGKILL

- SIGINT, SIGTERM
  - 유저가 직접 프로그램을 종료한 것이기 때문에 로직 처리가능
  - 시그널 핸들링 가능
  - `kill {PID}`
  - sigint는 키보드로부터 트리거 입력받음 (ctrl+c)
- SIGKILL
  - 프로세스를 즉시 종료시키고, 종료되기 전에 수행되어야 하는 절차를 실행하지 않고 즉시 종료하므로 graceful shutdown을 하기 위해서는 피해야한다.
  - 프로세스를 kill하는것이라 catch가 불가능하다.
  - 시그널 핸들러를 만들수없다.
  - ` kill -9 {PID}`



### JVM의 종료와 Graceful shutdown

```
JVM은 다음과 같은 경우에 정상적인 종료 절차를 밟는다.
- 데몬 스레드가 아닌 일반 스레드가 모두 종료되는 시점
- System.exit메서드가 호출 될 경우
- 프로세스가 종료 시그널을 받게 된 경우
```

shutdown-hook은 Runtime클래스의 `addShutdownHook(Thread hook)`메서드를 통해 등록하여 실행시킬 수 있다. thread-safe하게 코드를 작성해야한다.  

JVM에서 종료절차가 시작되더라도 실행중인 스레드는 따로 중단 절차가 진행되지 않아 종료절차가 끝나는 시점에 강제로 종료된다. 

log closing, bean삭제, bean factory삭제, 등 .. 



### k8s 파드 종료 처리1

쿠버네티스는 Pod가 종료될 때 프로세스들이 정상 종료될 수 있도록 컨테이너의 프로세스에 `SIGTERM` 시그널을 보낸다.`terminationGracePeriodSeconds` 설정 시간 이후에는 `SIGKILL`을 보내어 프로세스를 모두 강제종료한다. (기본값은 30초)

- 파드 종료 프로세스
  - preStop hook 실행
  - SIGTERM 프로세스 전달
  - SIGKILL로 프로세스 종료 

### k8s 파드 종료 처리2, 고려해야할 옵션

```\
쿠버네티스에서 새로운 버전의 Pod를 배포할 때 blue-green 방식으로 Rolling update된다. 기존 pod를 삭제하고 새로운 pod로 트래픽을 절체하는데 기존 pod 내부 어플리케이션의 task가 완료되지 않은 상태에서 기존 pod를 삭제하게 된다면 에러가 발생할 수 있다. 
```

- preStop hook을 이용하기 
- pod의 롤링 업데이트를 위한 maxSurge와 maxUnavailable 옵션
  - maxSurge : deployment에 설정되어있는 기본 pod 개수보다 여분의 pod가 몇개 더 추가될 수 있는지 설정
  - maxUnavailable : 업데이트를 하는 동안 몇 개의 pod가 이용불가능하게 되어도 되는지 설정
  - 둘다 0으로 설정할 수는 없다
- readinessProbe설정
  - pod의 healthcheck 방식은 2가지가 livenessProbe, readinessProbe가 있다. 
  - livenessProbe
    - 컨테이너가 살아있는지 확인하고, 헬스체크가 실패하면 kubelet이 컨테이너를 죽여 restart policy에 따라 컨테이너가 재시작됨
  - readinessProbe
    - 실제로 컨테이너가 '서비스 요청을 처리할 준비가 되었는지'를 확인. ok 상태여야지 pod와 연결된 service에 pod의 ip가 추가되고 트래픽을 받을 수 있음 . 실제 무중단 배포할땐 이게 더 중요 





- 출처
  - https://2kindsofcs.tistory.com/53
  - https://americanopeople.tistory.com/366
  - https://bscnote.tistory.com/123
  - 카카오 kubernetes를 이용한 서비스 무중단 배포
    - https://tech.kakao.com/2018/12/24/kubernetes-deploy/