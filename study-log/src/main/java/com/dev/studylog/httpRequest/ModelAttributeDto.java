package com.dev.studylog.httpRequest;

import lombok.*;
import org.hibernate.validator.constraints.Range;

@Setter
@Getter
public class ModelAttributeDto {

    private String name;

    @Range(min=20, message = "20이상이어야 합니다.")
    private Long id;

}
