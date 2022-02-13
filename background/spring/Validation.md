# Validation - 유효성 검증

우리는 입력하는 값에 대한 유효성 검사를 해야할 필요가 있다. 단순한 예로  회원가입 ID/PW 값에 대해서도 ID는 4자리 이상이어야 하며, PW에는 소문자,영어,특수문자와 같은 문자들이 들어가야한다는 조건이 있다. 더 단순하게는 null에 대한 처리도 있을 것이다. 프론트엔드 단에서도 이 값에 대해서 검사를 하겠지만, 그것만 믿고 요청 부분에 유효성 검사를 하지 않으면 business, db 레이어에서 올바르지 않은 값으로 인해 서버에 문제가 생길 수 있다.   

그렇다면 유효성 검사(Validation)은 어디서 해야할까? 데이터 검증이 여러 계층에 걸쳐서 이루어 진다면, 동일한 내용에 대한 검증 로직이 중복되거나 계층간 검증 로직의 불일치로 오류가 생길 것이다. 이를 해결하기 위해서 데이터 검증을 위한 로직을 도메인 모델 자체에 묶어서 표현하는 방식이 있다.

![img](https://miro.medium.com/max/1300/0*ZP1-gltlXubb8SSP.png)

Java에서는 2009년부터 Bean Validation이라는 데이터 유효성 검사 프레임워크를 제공하고 있다. Bean Validation은 위에서 말한 문제들을 해결하기 위해 다양한 제약(Contraint)을 도메인 모델(Domain Model)에 어노테이션(Annotation)로 정의할 수 있게한다. 이 제약을 유효성 검사가 필요한 객체에 직접 정의하는 방법으로 기존 유효성 검사 로직의 문제를 해결한다.

## Dependency

Spring Boot Validation Starter를 추가한다.  (Bean Validation 구현체로 Hibernate Validator를 사용한다.)

```bash
implementation group: 'org.springframework.boot', name: 'spring-boot-starter-validation', version: '2.5.2'
```

<br/>

## 어노테이션 종류

검증할 수 있는 어노테이션 종류는 여러가지가 있어서 몇가지만 적어봤다. 

```java
@NotNull // 필드값으로 null을 허용하지 않는다. ""와 " "은 허용한다
@NotEmpty // 필드값으로 null,""을 허용하지 않는다.  "은 허용한다. 
@NotBlank // 필드값으로 null,""," "을 허용하지 않는다. 
@Positive // 필드값이 양수인지 확인한다. 숫자에만 사용 가능
@Negative // 필드값이 0이 아닌 음수인지 확인한다.
@Size(min= , max=) // 필드 값이 Min과 max사이인지 확인할 수 있다.
@Min(value= )
@Max(value=)
```

<br/>

## 제약 조건에 대한 유효성 검증

### 1. **ValidatorFactory**에서 Validator를 가져와 `validate()`를 사용해 빈의 유효성을 검증

```java
ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
Validator validator = factory.getValidator();

Set<ConstraintViolation<Participants>> constraintViolations = validator.validate(participants);
```

제약조건을 위반한 내용은 Constraintiolation 인터페이스에 저장되고, 검증에 실패했다고 바로 Exception이 발생하진 않는다.    

<br/>

### 2. `@Valid` 를 이용한 검증

@Valid는 제약 조건이 부여된 Java Bean 객체에 Bean Validator를 이용해서 검증하도록 지시하는 어노테이션이다. 

컨트롤러 메소드 파라미터에 @Valid를 붙여주면 유효성 검증을 할 수 있다.

```java
@RequestMapping("/valid")
public void valid(@Valid @RequestBody Person person) {
    log.info("{} {}" ,person.getName(), person.getAge());
}
```

```bash
Resolved [org.springframework.web.bind.MethodArgumentNotValidException
```

@Valid는 ArgumentResolver에 의해 처리되어, 오류가 있다면 `MethodArgumentNotValidException`예외가 발생하여 400 BadRequest에러가 발생한다.  

<br/>

### 3. `@Validated`를 이용한 검증

@Validated는 AOP기반으로 메소드의 요청을 가로채서 유효성을 검증하는 방식이며, JSR 표준 기술이 아니라 Spring 프레임워크에서 제공하는 어노테이션 및 기능이다. 

클래스 레벨에 `@Validated`를 붙여주고 유효성을 검증할 메소드 파라미터에 `@Valid`를 붙여주면 된다.

> 파라미터의 유효성 검증은 컨트롤러에서만 처리해야 하위 계층에서 검증 로직이 중복되지 않기 때문에 바람직하지는 않지만, 불가피하게 검증해야될 경우에 사용한다. 데이터 유효성 검사가 중복으로 실행되지 않도록 성능에 미치는 영향을 유의해야한다.

만약 제약조건에 위반되는 내용이 발견되면 `ConstraintViolationException`이 발생하게된다.

#### Validated 동작 원리

인터셉터(MethodValidationInterceptor)가 유효성 검증을 진행하며, 스프링 빈이라면 유효성 검증을 진행할 수 있다. 

<br/>

#### @Validated vs @Valid

#### 검증 대상

@Valid는 기본적으로 컨트롤러에서만 동작하며 Java Bean 객체만 검증할 수 있기 때문에 Dto가 아닌 @RequestParam, @PathVariable은 숫자나 String값이기 때문에 처리하지 못한다. Param으로 받아온 값이나 다른 계층에서 파라미터를 검증하기 위해서는 @Validated를 사용해야한다.

#### 그룹 지정

그리고 Valid는 @Valid를 붙여준 객체에 대해 전체 검증을 수행하는데, @Validated는 제약 조건에 대해 그룹을 만들어 원하는 속성만 유효성 검사를 할 수 있다.

<br/>

#### MethodArgumentNotValidException vs ConstraintViolationException

위에 적은 내용에 대해 이해하면 왜 Exception이 달라지는 지 알 수 있다.

https://jongmin92.github.io/2019/11/18/Spring/bean-validation-1/ 이 글도 잘 적혀있다. 

<br/>

### 4. Custom constraint annotation

기본으로 제공하는 어노테이션으로는 검증하기 어렵다면 직접 만들 수 있다.

검증 로직이나 특정 그룹에 대해서 지정할 때 커스텀하면 좋다.

https://jongmin92.github.io/2019/11/21/Spring/bean-validation-2/ 참고 



- 출처 
  - [Hibernate Validator 6.0.11 Final ](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#validator-gettingstarted-createproject)
  - medium [Java와 Spring의 Validation](https://medium.com/@gaemi/java-%EC%99%80-spring-%EC%9D%98-validation-b5191a113f5c)
  - [MangKyu's Diary] https://mangkyu.tistory.com/174
  - toast meetp up https://meetup.toast.com/posts/223
  - [https://velog.io/@damiano1027/Spring-Valid-Validated를-이용한-데이터-유효성-검증](https://velog.io/@damiano1027/Spring-Valid-Validated%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EC%9C%A0%ED%9A%A8%EC%84%B1-%EA%B2%80%EC%A6%9D)



---

### @NotNull vs @NonNull vs @Column(nullable=false)

`@NotNull`과 `@NonNull` 둘다 어노테이션 방식이지만 @NonNull은 validator의 validation이 아니라 lombok에서 entity에 대한 제약조건 검증 어노테이션이다. 값이 null이 들어가면 NullPointException이 일어나며 커스텀 예외로 처리할 수 없다. 값의 null을 처리하고 핸들링 하고 싶다면 `@NotNull`을 사용하고 혹시나 엔티티에 null이 들어가는 것 자체를 막기 위해서 `@NonNull`을 사용하면 될 것 같다.  

엔티티 필드에 붙은 `@NotNull`은 @Valid 어노테이션과 별개로 JPA가 읽어 동작하기 때문에 ConstraintViolaitionExcpeiton이 발생한다. 

@Column(nullable=false)는 db에 들어갈 때 null이면 hibernate쪽에서 오류를 발생시키는것.

엔티티 -> column nullable (쿼리실행시 예외처리) / @NotNull(JPA엔티티 필드값이 null로 채워질 때 예외처리) / 엔티티가 아닌 값은 @Valid의 NotNull 또는 lombok의 NonNull

- 출처
  - [@NotNull vs @Column(nullable = false)](https://github.com/Hyeon9mak/WIL/blob/main/jpa/not-null-vs-column-nullable-false.md)

---

### **ValidatorFactory**에서 Validator를 가져와 `validate()`를 사용해 빈의 유효성을 검증 테스트

다음과같이 테스트해보았다. 

```java
@Builder
class Participants{

    @NotBlank(message = "닉네임을 입력해주세요")
    private String summonerName;

    @NotNull(message = "주 포지션을 입력해주세요.")
    @Pattern(regexp= "TOP|JUG|MID|ADC|SUP", message = "올바른 형식의 주포지션을 입력해주세요(TOP,JUG,MID,ADC,SUP)")
    private String mainPosition;
}

```

```java
    @Test
    @DisplayName("validator 테스트")
    public void vadlidatorTest() {
        Person person = Person.builder()
                .name(null)
                .age(-1L)
                .build();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        for(ConstraintViolation<Person> constraintViolation : constraintViolations) {
            log.info("{}",constraintViolation.getPropertyPath());  //age
            log.info("{}",constraintViolation.getRootBeanClass()); //class com.dev.studylog.validation.Person
            log.info("{}",constraintViolation.getInvalidValue()); //-1
            log.info("{}",constraintViolation.getMessage()); //0보다 커야 합니다
        }
    }
```

<br/>

### @Valid 테스트

```java
    @RequestMapping("/valid")
    public void valid(@Valid @RequestBody Person person) {
        log.info("{} {}" ,person.getName(), person.getAge());
    }

    @Test
    @DisplayName("valid 어노테이션 검증")
    public void validTest() throws Exception {
        Person person = Person.builder()
                .name(null)
                .age(-1L)
                .build();

        mockMvc.perform(get("/valid-annotation")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(person)))
                .andDo(print());
    }
```

````bash
Resolved [org.springframework.web.bind.MethodArgumentNotValidException: Validation failed for argument [0] in public void com.dev.studylog.validation.ValidController.valid(com.dev.studylog.validation.Person) with 2 errors: [Field error in object 'person' on field 'age': rejected value [-1]; codes [Positive.person.age,Positive.age,Positive.java.lang.Long,Positive]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [person.age,age]; arguments []; default message [age]]; default message [must be greater than 0]] [Field error in object 'person' on field 'name': rejected value [null]; codes [NotNull.person.name,NotNull.name,NotNull.java.lang.String,NotNull]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [person.name,name]; arguments []; default message [name]]; default message [must not be null]] ]
````

<br/>

### Custom annotation 예시

다음은 해당 값이 Enum Class의 Green에 속하지 않는지를 확인하기 위한 validator 예시이다.

```java
public enum MyColor {
    RED,
    GREEN,
    BLUE;
}

public class DTO {
  //...
      @EnumValid(enumClass = MyColor.class, message = "GREEN은 안됩니다.")
  		MyColor favoriteColor;
  //...
}
```

```java
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {EnumValidator.class}) 
public @interface EnumValid {
  // message는 어노테이션에서 지정해주지 않으면 default로 나가거나, properties로 설정해줄 수도 있다.
    String message() default "Invalid value. This is not permitted.";

  // 유효성 검사가 어떤 상황에서 실행되는지 정의할 수 있는 매개변수 그룹
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends java.lang.Enum<?>> enumClass();
}

```

```java
public class EnumValidator implements ConstraintValidator<EnumValid, Enum<?>> {
    private EnumValid enumValid;
  
  // 어노테이션에서 설정한 값을 가져오려면 초기화해주어야 한다. 만약 isValid함수에서 그냥 해결할 수 있으면 안해도됨
    @Override
    public void initialize(EnumValid constraintAnnotation) {
        enumValid = constraintAnnotation;
    }

  // isValid를 오버라이드해서 작성하면 된다.
    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        boolean result = true;
        Object[] enumValues = enumValid.enumClass().getEnumConstants();

        if (String.valueOf(value).equals(MyColor.GREEN.toString())) return false;

        return result;
    }
}
```

<br/>

### ExceptionHandling 

```java
package com.mayo.lol.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.validation.FieldError;
import javax.validation.ConstraintViolation;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    private int status;
    private String code;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ValidationError> errors;

    @Getter
    @Builder
    @RequiredArgsConstructor
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

        public static ValidationError of(ConstraintViolation violation) {
            return ValidationError.builder()
                    .field(String.valueOf(violation.getPropertyPath()))
                    .value(String.valueOf(violation.getInvalidValue()))
                    .message(violation.getMessageTemplate())
                    .build();
        }
    }

    @Builder
    public ErrorResponse(int status, String code, String message, List<ValidationError> errors) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.errors = errors;
    }
}

```

```java

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        ErrorResponse er = getErrorResponse(e, errorCode);
        log.error("handleValidationException[{}]", er);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(er);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        ErrorResponse er = getErrorResponse(e, errorCode);
        log.error("ConstraintViolationException[{}]", er);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(er);
    }

    public static ErrorResponse getErrorResponse(BindException e, ErrorCode code) {

        List<ErrorResponse.ValidationError> validationErrorList = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ErrorResponse.ValidationError::of)
                .collect(Collectors.toList());

        return ErrorResponse.builder()
                .code(code.getCode())
                .message(code.getMessage())
                .errors(validationErrorList)
                .status(code.getStatus())
                .build();
    }
```

