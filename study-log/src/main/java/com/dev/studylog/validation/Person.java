package com.dev.studylog.validation;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
public class Person {

    @NotNull
    String name;

    @Positive
    Long age;

    @Builder
    public Person(String name, Long age) {
        this.name = name;
        this.age = age;
    }
}
