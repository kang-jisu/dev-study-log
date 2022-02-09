package com.dev.studylog.httpRequest;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestBodyDto {

    @NotNull
    String name;

    @Min(value = 20)
    Long age;

    // 일단 objectMapper로 Color값을 받아와야하므로 아예 Color Enum Class안에 존재하지 않는 값은 여기서 검증하는게 아니고 미리 400 bad request 처리됨
    // enumClass = Color.class 안에서 어떤 다른 처리를 해줘야할 때 (어떤 값은 되고, 어떤값은 안되고 ) 이런 validation 사용하면 될듯
    @EnumValid(enumClass = Color.class, message = "GREEN은 안됩니다.")
    Color favoriteColor;

    @Builder
    public RequestBodyDto(String name, Long age, Color favoriteColor) {
        this.name = name;
        this.age = age;
        this.favoriteColor = favoriteColor;
    }
}
