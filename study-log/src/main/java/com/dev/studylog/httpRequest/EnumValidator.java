package com.dev.studylog.httpRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<EnumValid, Enum<?>> {


    private EnumValid annotaion;

    @Override
    public void initialize(EnumValid constraintAnnotation) {
        this.annotaion = constraintAnnotation;
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        boolean result = true;
        Object[] enumValues = this.annotaion.enumClass().getEnumConstants();

        if (String.valueOf(value).equals(Color.GREEN.toString())) return false;

        return result;
    }
}
