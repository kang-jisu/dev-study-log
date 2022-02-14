package com.dev.studylog.java;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConstantPoolTest {

    @Test
    @DisplayName("String literal로 생성한 두 객체는 값이 같다면 동일하다")
    void stringLiteral() {
        String str1 = "coffee";
        String str2 = "coffee";
        assertTrue(str1 == str2);
        assertTrue(str1.equals(str2));
    }

    @Test
    @DisplayName("new 연산자로 생성한 String 객체는 값이 같아도 동일하지 않다.")
    void newString() {
        String str1 = new String("coffee");
        String str2 = new String("coffee");
        assertFalse(str1 == str2);
        assertTrue(str1.equals(str2));
    }
}
