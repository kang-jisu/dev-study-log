## Cloudformation을 이용한 vCU 배포 환경 구축
### 1.  개요  
AWS CloudFormation은 AWS 리소스를 모델링하고 설정하여 리소스 관리 시간을 줄이고 AWS에서 실행되는 애플리케이션에 더 많은 시간을 사용하도록 해 주는 서비스입니다. 필요한 모든 AWS 리소스(예: Amazon EC2 인스턴스 또는 Amazon RDS DB 인스턴스)를 설명하는 템플릿을 생성하면 CloudFormation이 해당 리소스의 프로비저닝과 구성을 담당합니다. AWS 리소스를 개별적으로 생성하고 구성할 필요가 없으며 어떤 것이 무엇에 의존하는지 파악할 필요도 없습니다.
-   인프라 관리 간소화 
    -  템플릿을 사용하여 Cloudformation 스택을 생성해 AWS리소스를 자동으로 프로비저닝하고 리소스 모음을 단일 단위로 쉽게 관리할 수 있습니다. (생성, 실행, 삭제)
-   신속한 인프라 복제
    - Cloudformation 템플릿을 재사용하여 리소스를 일관되고 반복적으로 생성할 수 있습니다. 여러 리전에서 재사용하여 동일 리소스를 반복적으로 프로비저닝할 수 있습니다.
-   인프라 변경 사항을 쉽게 제어 및 추적
    - CloudFormation을 사용하여 인프라를 프로비저닝할 경우 프로비저닝되는 리소스와 해당 설정이 CloudFormation 템플릿에 정확히 설명됩니다. 템플릿은 텍스트 파일이므로 템플릿에서 차이점을 추적하여 인프라 변경 사항을 추적할 수 있습니다. 또한 개발자가 소스 코드에 대한 개정 사항을 제어하는 방식처럼 버전 제어 시스템을 템플릿과 함께 사용하여 변경된 내용, 변경한 사람, 변경 시간 등을 정확히 알 수 있습니다. 언제든지 인프라에 대한 변경 사항을 되돌려야 하는 경우 이전 버전의 템플릿을 사용할 수 있습니다.
### 2.  AWS Cloudformation 작동 방식
-   Cloudformation은 사용자가 수행할 수 있는 권한이 있는 작업만 수행할 수 있으므로 IAM을 사용한 권한 관리가 필요합니다.
-   Cloudformation에서 실행하는 호출은 모두 템플릿으로 선언됩니다..
    -  AWS Cloudformation Designer 또는 JSON, YAML 형식으로 템플릿을 생성하거나 수정할 수 있습니다. 또한 제공된 템플릿을 사용하도록 선택할 수 있습니다.
    -  템플릿을 로컬 또는 S3 버킷에 .json, .yaml, .txt와 같은 확장명으로 저장합니다.
    -  템플릿 위치를 지정하여 Cloudformation 스택을 생성하고 템플릿에 파라미터를 포함해 스택 생성시 동적으로 값을 지정할 수 있습니다. 입력된 파라미터를 통해 템플릿에 값을 전달하여 스택을 생성할 때마다 리소스를 사용자 지정할 수 있습니다.
    -  Cloudformation 콘솔,API, AWS CLI를 사용해 스택을 생성할 수 있습니다.
    -   모든 리소스가 생성된 후 Cloudformation에서는 스택이 생성되었음을 보고하며, 스택 생성에 실패할 경우 생성한 리소스를 삭제하여 변경 사항을 롤백합니다.
### 3.  AWS Cloudformation 개념  
#### 3-1. 템플릿  
CloudFormation 템플릿은 JSON 또는 YAML 형식의 텍스트 파일입니다. .json, .yaml, .template 또는 .txt 등 모든 확장명으로 파일을 저장할 수 있습니다.
```yaml
--- # 예 (YAML)
AWSTemplateFormatVersion: '2010-09-09'
Description: A simple EC2 instance
Resources:
MyEC2Instance:
Type: AWS::EC2::Instance
Properties:
  ImageId: ami-0ff8a91507f77f867
  InstanceType: t2.micro
```
#### 3-2. 스택    
CloudFormation을 사용할 경우 스택이라는 하나의 단위로 관련 리소스를 관리합니다. 스택을 생성, 업데이트, 삭제하여 리소스 모음을 생성, 업데이트 및 삭제합니다. 스택의 모든 리소스는 스택의 CloudFormation 템플릿으로 정의합니다.  
#### 3-3. 변경 세트  
스택에서 실행 중인 리소스를 변경해야 하는 경우 스택을 업데이트합니다. 리소스를 변경하기 전에 제안된 변경 사항이 요약된 변경 세트를 생성할 수 있습니다. 변경 세트를 사용하면 변경 사항을 구현하기 이전에 해당 변경이 실행 중인 리소스 특히, 중요 리소스에 미치는 영향을 확인할 수 있습니다.  

### 4.  Cloudformation 템플릿 구조 (YAML 기준 설명)  
4-1. 템플릿 섹션
```yaml
---
AWSTemplateFormatVersion: "version date"
Description:
  String
Metadata:
  template metadata
Parameters:
  set of parameters
Rules:
  set of rules
Mappings:
  set of mappings
Conditions:
  set of conditions
Transform:
  set of transforms
Resources:
  set of resources
Outputs:
  set of outputs
  ```
Resources 섹션만 필수 섹션이며, 템플릿 일부 섹션은 임의 순서대로 배열할 수 있습니다. 다만 템플릿 작성시 한 섹션의 값이 이전 섹션의 값을 참조할 수도 있으므로 다음과 같은 목록의 논리적 순서를 사용하는 것이 유용합니다.
-   포맷 버전
  -     템플릿이 따르는 AWS Cloudformation 템플릿 버전
  -     현재 유일한 유효값은 "2010-09-09"
-   Description
  -     템플릿을 설명하는 텍스트 문자열(0~1023바이트 리터럴 문자열)
  -     항상 템플릿 포맷 버전 섹션 다음에 이어져야합니다.
-   Metadata
  -     템플릿에 대한 추가 정보 제공
  -     특정 리소스에 대한 템플릿 구현 세부 정보 포함
-   Parameters (https://docs.aws.amazon.com/ko_kr/AWSCloudFormation/latest/UserGuide/parameters-section-structure.html#parameters-section-structure-properties-type)
  -     스택을 생성하거나 업데이트할 때 실행시간에 템플릿에 전달하는 값
  -     템플릿의 Resources 및 Outputs 섹션에서 파라미터를 참조할 수 있습니다.
  -     Cloudforamtion 템플릿에서는 최대 60개의 파라미터 지정 가능하며 각 파라미터마다 논리적인 이름을 고유하게 지정해야합니다. 또한 각 파라미터마다 지원하는 파라미터 유형을 할당해야 합니다.
  -     Ref 내장 함수를 사용하여 Resources 및 Outputs에서 파라미터를 참조할 수 있습니다.
-   Mappings
  -     키를 해당하는 명령된 값 세트와 맞춥니다. 예를 들어 리전에 따라 값을 설정하려면 리전 이름을 키로 사용하고 각각의 특정 리전에 대해 지정할 값을 포함하는 매핑을 생성할 수 있습니다. Fn::FindInMap 내장 함수를 사용하여 맵에서 값을 불러올 수 있습니다.
-   Conditions
  -     스택을 생성하거나 업데이트할 때 파라미터 또는 파라미터의 조합을 검증
-   Transform
-   Resources(필수)
  Resources:
  Logical ID: {고유한 영숫자 이름}
    Type: Resource type
    Properties:
      Set of properties
  -     리소스 타입 – 링크 참조 https://docs.aws.amazon.com/ko_kr/AWSCloudFormation/latest/UserGuide/aws-template-resource-type-ref.htm
  -     스택 리소스 및 해당 속성을 지정합니다.
  -     템플릿의 Resources 및 Outputs 섹션에서 리소스를 참조할 수 있습니다.
-   Outputs
  -     스택의 속성을 볼 때 반환되는 값을 설명합니다.


















출처
-   https://docs.aws.amazon.com/ko_kr/AWSCloudFormation/latest/UserGuide/cfn-ug.pdf#Welcome
-