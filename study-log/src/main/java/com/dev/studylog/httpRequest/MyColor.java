package com.dev.studylog.httpRequest;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MyColor {
    RED,
    GREEN,
    BLUE;

    @JsonCreator
    public static MyColor create (String value)  {
        for( MyColor myColor : values()) {
            if(myColor.toString().equals(value)) {
                return myColor;
            }
        }
        throw new RuntimeException("잘못된 값 "+ String.valueOf(value));
    }

}
