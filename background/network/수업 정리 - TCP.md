## TCP

- 각 방향으로 전송되는 데이터 바이트는 TCP에 의해서 번호가 매겨진다. 번호는 임의로 생성된 값에서 시작한다.(SYN의 SequenceNumber)
- 세그먼트 내의 Sequence Number 필드 값은 그 세그먼트에 포함되는 첫번째 데이터의 바이트번호를 나타낸다.
- 세그먼트 내의 확인응답번호의 값은 수신하기를 기대하는 다음 바이트의 번호를 나타낸다. `Cumulative ACK(누적)`
- TCP에서 checksum의 포함은 필수사항이다.

### TCP 연결 - SYN / 3 - way handshake

- SYN 세그먼트는 데이터를 전달하지는 않지만 하나의 sequence number를 소비한다.
- SYN+ACK 세그먼트는 데이터를 전달하지는 않지만 하나의 sequnce number를 소비한다.
- ACK 세그먼트는 데이터를 전달하지 않는 경우에는 sequence number를 소비하지 않는다.
- ACK은 같이 보낼 데이터가 있으면 같이 간다.

### TCP 종료 - FIN / 4 -way handshake

- 할일이 끝나면 연결을 종료해주어야한다.
  - 연결시 클라, 서버별로 BUFFER를 만들어놔서 종료 안하면 서버 다운될것
- **일반적으로 클라이언트가 먼저 요청**
- 데이터를 포함하지 않는 FIN 또는 FIN+ACK 세그먼트는 하나의 sequence number를 소비한다.

![TCP state transition diagram](https://www.ibm.com/docs/en/SSLTBW_2.1.0/com.ibm.zos.v2r1.halu101/dwgl0004.gif)

| 클라이언트                    | 패킷                       | 서버                                                   |
| ----------------------------- | :------------------------- | ------------------------------------------------------ |
| socket                        |                            | socket, bind, listen                                   |
|                               |                            | accept(block)                                          |
| connect (`SYN_SENT`)          | ->SYN-><br /><- SYN+ACK <- | `SYN_RCVD`                                             |
| `ESTABLISHED` connect returns | -> ACK ->                  | `ESTABLISHED` accept return<br />read block            |
| write<br />read block         | -> Data ->                 | read returns                                           |
| read return                   | <- Data + ACK <-           | write<br />read blocks                                 |
| active close `FIN_WAIT `      | -> FIN                     | CLOSE_WAIT paasive close<br />read return 0 (read종료) |
| `FIN_WAIT2`                   | <- ACK <-                  |                                                        |
| `TIME_WAIT`                   | <- FIN <-                  | close `LAST_ACK`                                       |
|                               | -> ACK ->                  | `CLOSED`                                               |

[![img](https://ssup2.github.io/images/theory_analysis/TCP_Connection_State/TCP_Handshake_Connection_State.PNG)](https://ssup2.github.io/images/theory_analysis/TCP_Connection_State/TCP_Handshake_Connection_State.PNG)[그림 2] TCP Handshake Connection State. 



**Half close**

```
클라이언트가 fin 보내고 응답 받는데 내가 더이상 보낼게 없다는 의미. DATA전송 이제 못함
fin 보내는건 SENDING BUFFER를 없애버리는것을 의미함
상대방은 아직 보낼게 남아있을 수 있으니 한쪽만 끊은거 (서버에게 받을 준비만)
close 말고 shutdown 사용

일반적인 data는 못보내지만 ack은 가고 다 보낸다음 더이상 보낼게 없을 때 연결 종료
```

- READ했더니 return 0인건 상대가 FIN 보냈다는것

- 종료안해도 프로그램 끝나거나 Ctrl C 강제종료해도 close. 

  

**일반적으로 클라이언트가 종료요청함 서버는 항상 켜놓음 



**TIME WAIT**

- 2MSL 만큼 기다리고 있다가 아무일도 안일어나면 CLOSED 상태로 감
- `MSL` : Maximum segment lifetime 보통 30초~1분, 1~2분 대기후 CLOSED

**TIME_WAIT이 필요한 이유**

```
1) 종료과정에 있는 마지막 ACK을 보내고 나서 분실될 수 있으니 기다려줌(fin재전송에 대한 ack보낼 수도 있으므로)

2) 포트번호 문제
종료한 다음 바로 연결요청했을 때 만약 같은 포트번호가 할당되어버리면 서버가 클라이언트 아이피를 구별하지 못함.

time wait이부분은 옵션으로 지울 수 있음
```



**SERVER**

- 서버는 마지막으로 ACK 받으면 바로 종료, 버퍼 다 지움

**연결 거절**

- 연결 거절은 RST 비트 패킷으로 거절할 수도 있음

**SYN Flooding**

- Client 주소 바꿔서 보내서 계속 syn에 대해서 대기큐 채워서 다른 사용자가 요청 못보내게함