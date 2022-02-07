## DTO <-> ENTITY

### Entity

JPA를 사용해서 개발하게되면 Entity클래스가 바로 DB테이블로 1:1 매핑되어진다. 따라서 테이블이 가지지 않는 컬럼을 필드로 가져서는 안되며 Entity클래스는 다른 클래스를 상속받거나, 인터페이스 구현체여서는 안된다. 

이때 사용하는 Entity를 그대로 구현시에 사용하지 않기 위해서 Dto를 사용한다.

### DTO ( Data Transfer Object)

DB에서 꺼낸 데이터를 저장하는 Entity를 가지고 일종의 Wrapper의 역할을 한다. Controller계층 처럼 클라이언트와 직접 마주하는 계층에서는 실제 데이터인 Entity대신 DTO를 사용해 데이터를 교환한다. 

DTO는 특별한 로직을 가지지 않는 순수한 데이터 객체여야하며 Setter를 만들지 않고 생성자에서 값을 할당한다. (Builder패턴 이용)



### Entity와 Dto 분리 이유

- 데이터의 변경이 많은 DTO 클래스를 분리해주어 DB로의 접근을 보호하기 위해서
- Entity의 필드를 모두 공개하지 않고 필요한 부분만 공개하기 위해서 
- 응답, 요청시 객체의 형태가 다를 수 있으므로 
- 컨트롤러에서 DTO를 통해 의 Validation을 해서 다음 계층에서 값에 대한 검증과정이 다시 이루어지지 않도록 함 



### DTO 사용 범위

Client 

- ↕ client-> controller : RequestDto를 통해서 요청을 받음
- ↕ controller->client : ResponseDto로 응답

Controller

- ↕ controller-> service : 서비스에게 요청받은 RequestDto 전달 
- ↕ service->controller : ResponseDto로 형태를 변환시켜 컨트롤러에게 전달

Service 

- ↕ toEntity함수를 통해서 entity로 변환하여 사용하거나 repoistory에게서 entity를 얻어내면 응답형식에 맞는 ResponseDto로 변환

Repository

이런식으로 사용하는 것 같다. 그래서 controller에서는 Entity를 사용하지 않으려고 하고있다. 


** Dto로 요청받을 때  `validation`을 이용해서 값에대한 검증을 마치면 service단으로 넘어갔을 때 값이 올바른지에 대한 검증은 하지 않아도 된다. 그리고 `javax.persistence.*`를 이용해서 테이블이 만들어질 때 entity의 제약사항들은 설정해주면 될 것 같다. 

> 참고
> - https://velog.io/@ohzzi/Entity-DAO-DTO%EA%B0%80-%EB%AC%B4%EC%97%87%EC%9D%B4%EB%A9%B0-%EC%99%9C-%EC%82%AC%EC%9A%A9%ED%95%A0%EA%B9%8C



### Dto <-> Entity 변환 

dto와 Entity를 변환하는 방식의 규칙을 좀 정해서 개발해봐야겠다. 

modelmapper를 사용해도 되는데 일단은 그냥 매핑이 필요할 때 직접 만드려고하고, Entity클래스에서 변환하는 코드가 있는것은 좋지 않은 것 같아서 `of`와 `toEntity` 모두 DTO클래스에서 적으려고 한다. 

- `XxxDto of(XxxEntity entity)`
  - Dto값에 관계 없이 인자로 들어오는 entity값에 대해서 결과를 반환하므로 static으로 만들면 될 것 같다. 
- `XxxEntity toEntity()`
  - dto함수에서 호출하므로 인자는 필요없고 값을 채울 때 바로 `builder().name(this.name).build()`이런식으로 만들면 될 것 같다. 

약간 `MemberDto.of(MemberEntity)` 멤버 엔티티의 Dto 이런느낌이라 of라는 이름을 쓰는거같다.
원래 날것->가공된것으로 바꿀때 `가공클래스.of(날거)` 이런느낌으로 짓는 느낌 ? ? `toDto`를 써도 되지만 그냥 of를 써야겠다.   

만들때는 되도록 `@builder`를 이용해서 생성하는게 좋을 것 같다. 



#### 사용 예시

<details>
  <summary> ErrorResponse 클래스의 ValidationError </summary>

```java
  public static class ValidationError {
    private final String field;
    private final String value;
    private final String message;
  public static ValidationError of(FieldError fieldError) {
    return ValidationError.builder()
            .field(fieldError.getField())
            .value(String.valueOf(fieldError.getRejectedValue()))
            .message(fieldError.getDefaultMessage())
            .build();
}
}
```
</details>

<details>
  <summary> LeagueResultDto <-> LeagueResultEntity </summary>

```java
    public static LeagueResultDto of(LeagueResultEntity leagueResultEntity) {
        return LeagueResultDto.builder()
                .gameType(leagueResultEntity.getGameType())
                .ordinalNum(leagueResultEntity.getOrdinalNum())
                .ranking(leagueResultEntity.getRanking())
                .build();
    }
```

```java
    public LeagueResultEntity toEntity() {
        return LeagueResultEntity.builder()
                .gameType(this.gameType)
                .ordinalNum(this.ordinalNum)
                .ranking(this.ranking)
                .build();
    }
```
</details>



### List\<Dto> <-> List\<Entity>

List형식을 바꿀때는 Stream과 람다, 메소드 참조를 이용하면 쉽게 바꿀 수 있다.

```java
List<LeagueResultDto> dtos = memberEntity.getLeagueResults()
  .stream()
  .map(LeagueResultDto::of)
  .collect(Collectors.toList());
```

`memberEntity.getLeagueResults()`를 하면 `List<LeagueResultEntity>`를 반환하는데, `stream().map(leagueResultEntity -> leagueResultDto.of(leagueResultEntity))`를 메소드 참조를 이용하면 좀 더 이쁘게 쓸 수 있다. 

> [메소드 참조란](../java/정적메소드.md)
>
> - 메소드를 참조해서 매개변수 및 정보의 리턴타입을 알아내어, 람다식에서 불필요한 매개 변수를 제거하는 것을 말한다. 
> - 정적 메소드를 참조할 경우에는 클래스 이름 뒤에 `::` 을  붙붙이고 정적 메소드 이름을 기술하면 된다. 

<details>
  <summary> 테스트코드 </summary>

```java
@Test
public void LeagueResultEntityListToDtoList() {
    LeagueResultEntity entity = new LeagueResultEntity(GameTypeCode.LEAGUE, 1L, 1L); // 이것도 빌더로 하는게 좋을 것 같다

    MemberEntity memberEntity = new MemberEntity();
    memberEntity.setAccountId(1L);
    memberEntity.addLeagueResult(entity);
    memberEntity.addLeagueResult(entity1);
    
    List<LeagueResultDto> dtos = memberEntity.getLeagueResults().stream().map(LeagueResultDto::of).collect(Collectors.toList());
    assertThat(dtos.size()).isEqualTo(2);
    
    assertThat(dtos.get(0).getGameType()).isEqualTo(entity.getGameType());
    assertThat(dtos.get(0).getOrdinalNum()).isEqualTo(entity.getOrdinalNum());
    assertThat(dtos.get(0).getRanking()).isEqualTo(entity.getRanking());
    assertThat(dtos.get(1).getGameType()).isEqualTo(entity1.getGameType());
    assertThat(dtos.get(1).getOrdinalNum()).isEqualTo(entity1.getOrdinalNum());
    assertThat(dtos.get(1).getRanking()).isEqualTo(entity1.getRanking());

}
```
</details>



