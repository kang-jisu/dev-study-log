package com.dev.studylog.httpRequest;

import lombok.*;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@NoArgsConstructor
public class ModelAttributeDto {

    private String name;

    @Range(min=20, message = "20이상이어야 합니다.")
    private Long id;

    @Builder
    public ModelAttributeDto(String name, Long id) {
        this.name = name;
        this.id = id;
    }
}
