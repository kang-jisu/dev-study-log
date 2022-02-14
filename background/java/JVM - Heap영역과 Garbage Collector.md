# Java Garbage Collection
Naver D2 링크를 읽고 정리하였습니다.
## 가비지 컬렉션 과정 - Generatioanl Garbage Collection
[Naver D2](https://d2.naver.com/helloworld/1329)
### stop-the-world
GC를 실행하기 위해 JVM이 애플리케이션 실행을 멈추는 것을 말한다.
stop-the-world가 발생하면 GC를 실행하는 쓰레드를 제외한 나머지 쓰레드는 모두 작업을 멈춘다. GC작업을 완료한 이후에야 중단했던 작업을 다시 시작한다.
어떤 GC알고리즘을 사용하더라도 stop-the-world는 발생한다.  
대개 GC튜닝이란 stop-the-world시간을 줄이는 것을 말한다.
<br/>
### Garbage Collection
자바에서는 개발자가 프로그램의 코드로 메모리를 명시적으로 해제하지 않기 때문에 GC가 더이상 필요 없는 객체를 찾아 지우는 작업을 한다.
- 대부분의 객체는 금방 접근 불가능 상태(unreachable)가 된다.
- 오래된 객체에서 젊은 객체로의 참조는 아주 적게 존재한다.
다음과 같은 가설을 살려 VM은 2개의 물리적 공간으로 나누어졌다. Young영역과 Old영역이다.
#### Young
Young Generation 영역으로, 새롭게 생성한 객체의 대부분이 여기 위치하며 대부분의 객체가 금방 접근 불가능 상태가 되기 때문에 매우 많은 객체가 Young영역에 생성되었다가 사라진다.  
이 영역에서 객체가 사라질 때 Minor GC가 발생한다고 말한다.
#### Old
Old Generation 영역으로, 접근 불가능 상태가 되지 않아 Young 영역에서 살아남은 객체가 여기로 복사된다.
대부분 Young영역보다 크게 할당하며, 크기가 큰 만큼 Young영역보다 GC는 적게 발생한다.  
이 영역에서 객체가 사라질 때 Major GC(Full GC)가 발생한다고 말한다.
<br/>
![gc](https://d2.naver.com/content/images/2015/06/helloworld-1329-1.png)

<br/>

### Young 영역의 구성  
Young 영역은 3개의 역인 Eden, Survivor0,Survivor1영역으로 나뉜다.
- 새로 생성한 대부분의 객체는 Eden 영역에 위치한다.
- **Eden영역에서 GC가 한 번 발생한 후 살아남은 객체는 Survivor영역중 하나로 이동**된다.
- Eden영역에서 GC가 발생하면 이미 살아남은 객체가 존재하는 Survivor영역으로 객체가 계속 쌓인다.
- 하나의 Survivor영역이 가득 차게 되면 그 중에서 살아남은 객체를 다른 Survivor영역으로 이동한다. 그리고 가득찬 Survivor영역은 아무 데이터도 없는 상태가 된다.
- 이 과정을 반복하다가 **계속해서 살아남아 있는 객체는 Old영역으로 이동**하게된다.
Survivor영역중 하나는 반드시 비어있는 상태로 남아있어야 한다.  

<br/>

![gc](https://d2.naver.com/content/images/2015/06/helloworld-1329-3.png)
<br/>
### Old 영역에 대한 GC
Old영역은 기본적으로 데이터가 가득 차면 GC를 실행한다.  GC 방식은 다음 종류들이 있다.
- Serial GC - CPU코어가 하나만 있을 때 사용하기 위해 만들어진 방식으로, 운영 서버에서 사용하면 안된다.
- Parallel GC
- Parallel Old GC
- Concurrent Mark & Sweep GC
- G1(Garbage First) GC
#### 1. Serial GC
mark-sweep-compact 알고리즘을 사용해 Old영역에 살아있는 객체를 식별(Mark), 힙의 앞부분부터 확인하여 살아있는것만 남김(Sweep), 객체들이 연속되게 쌓이도록 Compaction 하는 단계로 이루어진다.  
적은 메모리와 CPU코어 개수가 적을 때 적합한 방식이다.  
#### 2. Parallel GC (= Throughput GC)
Parallel GC는 Serial GC와 기본 알고리즘은 같지만, 처리하는 쓰레드가 여러개이다. 그렇기 때문에 Serial GC보다 빠르게 객체를 처리할 수 있다.
- Serial GC와 Paralle GC의 차이
![s vs p](https://d2.naver.com/content/images/2015/06/helloworld-1329-4.png)
#### 3. Parallel Old GC
Mark-Summary-Compaction단계를 거친다. Sweep과 다르게 Summary단계는 앞서 GC를 수행한 영역에 대해서 별도로 살아있는 객체를 식별한다.  
#### 4. CMS GC (Concurrent Mark and Sweep)
![s vs cms](https://d2.naver.com/content/images/2015/06/helloworld-1329-5.png)  

초기 Initial Mark 단계에서는 클래스 로더에서 가장 가까운 객체중 살아있는 객체만 찾는것으로 끝낸다. 따라서 멈추는 시간이 짧다.  
그리고 Concurrent Mark단계에서 방금 살아있다고 확인한 객체에서 참조하고있는 객체들을 따라가면서 확인한다. 이 단계의 특징은 **다른 쓰레드가 실행중인 상태에서 동시 진행된다는 것이다.**  
다음 Remark단계에서는 Concurrent Mark단계에서 새로 추가되거나 참조가 끊긴 객체를 확인하고, Concurrent Sweep단계에서는 쓰레기를 정리하는 작업을 실행한다.  
<br/>
CMS GC는 stop-the-world시간이 짧다는 장점이 있지만, 다음과 같은 단점이 존재한다.
- 다른 GC방식보다 메모리와 CPU를 더 많이 사용한다.
- Compaction단계가 기본적으로 제공되지 않는다.  
#### 5. G1 GC
![g1gc](https://d2.naver.com/content/images/2015/06/helloworld-1329-6.png)
바둑판의 각 영역에 객체를 할당하고 GC를 실행한다. 그러다가 해당 영역이 꽉차면 다른 영역에서 객체를 할당하고 GC를 실행한다. G1 GC는 어떠한 다른 GC방식보다도 빠르다.
## GC 모니터링
[Naver D2 - Garbage Collection 모니터링 방법](https://d2.naver.com/helloworld/6043)  
<br/>
- `jstst` : cui 애플리케이션 이용
- `-verbosegc` : JVM가동시 옵션 이용
<br/>
## Java Reference와 GC

Java의 가비지 컬렉터는 그 동작 방식에 따라 매우 다양한 종류가 있지만, 공통적으로 크게 다음 2가지 작업을 수행한다.  
- 힙(heap)내의 객체 중에서 가비지를 찾아낸다.  
- 찾아낸 가비지를 처리해서 힙의 메모리를 회수한다.  

### GC와 Reachability  

Java GC는 객체가 가비지인지 판별하기 위해서 `reachability`라는 개념을 사용한다. 어떤 객체에 유효한 참조가 있으면 `reachable`로, 없으면 `unreachable`로 구별하고, unreachable객체를 가비지로 간주해 GC를 수행한다.   
객체들은 참조 사슬을 이루기 때문에 유효한 참조 여부를 파악하려면 항상 유효한 최초의 참조가 있어야하는데 이를 객체 참조의 **root set**이라고 한다.  

![runtime data area](https://d2.naver.com/content/images/2015/06/helloworld-329631-1.png)  
<br/>
런타임데이터영역은 위와 같이 스레드가 차지하는 영역들과, 객체를 생성 및 보관하는 하나의 큰 힙, 클래스 정보가 차지하는 메서드 영역 크게 세 부분으로 나눌 수 있다. 위 그림에서 객체에 대한 참조는 화살표로 표시되어있다.  
힙에 있는 객체들에 대한 참조는 다음 4가지 종류 중 하나이다.
- 힙 내에 다른 객체에 의한 참조
- Java 스택, 즉 Java 메서드 실행시 사용하는 변수들에 의한 참조
- 네이티브 스택에 의해 생성된 객체에 대한 참조
- 메서드 영역의 정적 변수에 의한 참조
이때 힙 내에 다른 객체에 의한 참조를 제외한 나머지가 **root set**으로 reachability를 판가름 하는 기준이 된다.  
<br/>
root set과 힙 내의 객체를 중심으로 다시 그리면 다음과 같다.

![rootset](https://d2.naver.com/content/images/2015/06/helloworld-329631-2.png)

### Soft, Weak, Phantom Reference
내용이 어려워서 눈으로만 읽으려고 하니 https://d2.naver.com/helloworld/329631 링크 참조.
<br/>
### 정리
- Java의 GC는 GC 대상 객체를 갖고, 대상 객체를 처리하고, 할당된 메모리를 회수하는 작업으로 구성된다.
- 애플리케이션은 사용자 코드에서 객체의 reachability를 조절하여 Java GC에 일부 관여할 수 있다.
- 객체의 reachability를 조절하기 위해서 java.lang.ref 패키지의 SoftReference, WeakReference, PhantomReference, ReferenceQueue 등을 사용한다.