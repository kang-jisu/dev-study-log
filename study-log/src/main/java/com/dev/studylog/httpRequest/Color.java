package com.dev.studylog.httpRequest;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Color {
    RED,
    GREEN,
    BLUE;

    @JsonCreator
    public static Color create (String value)  {
        for( Color color : values()) {
            if(color.toString().equals(value)) {
                return color;
            }
        }
        throw new RuntimeException("잘못된 값 "+ String.valueOf(value));
    }

}
