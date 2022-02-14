package com.dev.studylog.httpRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<EnumValid, Enum<?>> {

    private EnumValid enumValid;

    @Override
    public void initialize(EnumValid constraintAnnotation) {
        enumValid = constraintAnnotation;
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        boolean result = true;
        Object[] enumValues = enumValid.enumClass().getEnumConstants();

        if (String.valueOf(value).equals(MyColor.GREEN.toString())) return false;

        return result;
    }
}
