## Docker 이미지 크기 줄이는 방법

- 경량 Base Image 사용하기

  - alpine
  - ubi8

- **Dockerfile 명령을 체인으로 사용하기 **-> 이거 좀 꿀팁인듯!! 

  - 개별로 실행할 때 마다 중간 이미지가 생성되는데, 체인으로 생성하면 한개만 만들어지기 때문에 크기가 줄어든다

  - ```
    RUN wget -nv
    RUN tar -xvf someutility-v1.0.0.tar.gz
    RUN mv /tmp/someutility-v1.0.0/someutil /usr/bin/someutil
    ```

  - ```
    RUN wget -nv \
    tar -xvf someutility-v1.0.0.tar.gz \
    mv /tmp/someutility-v1.0.0/someutil /usr/bin/someutil
    ```

- 불필요한 파일, 디버깅 툴, 빌드 도구를 설치하지 않음

  - `--no-install-recommends` 옵션이나 rm -rf로 패키지 삭제 

- 패키지 관리자 정의

- 불필요한 파일 정리

  - curl을 통한 파일 다운로드시에 파일을 설치한 후 삭제
  - Docker명령어에 수행해야하며 체인을 사용해야 최종 이미지에 포함되지 않는다.

- docker image에서 불필요한 layer가 있는지 확인

  - `--no-trunc`명령어를 사용하면 전체 설명과 layer마다 사용된 용량이 표시됨

- ADD

  - ADD 대신 RUN 사용(체인으로)

- 멀티 스테이지 사용하기 

  - FROM으로 각 stage를 정의하고 각 stage에서 다른 stage에서 빌드된 아티팩트를 카피해서 사용하여 최종 stage에서는 저장하지 않기때문에 용량이 줄어듦 

- fromlatest.io 사이트에서 이미지 사이즈 줄일 수 있는 방법 제시해줌

- 등등 



- 출처
  - https://velog.io/@idnnbi/Docker-image-%ED%81%AC%EA%B8%B0-%EC%A4%84%EC%9D%B4%EA%B8%B0
  - https://luidy.tistory.com/entry/Docker-Docker-Image-%EC%82%AC%EC%9D%B4%EC%A6%88-%EC%A4%84%EC%9D%B4%EA%B8%B0