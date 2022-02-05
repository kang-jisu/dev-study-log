package com.dev.studylog.java;

import org.junit.jupiter.api.Test;

class Calculator {
    int a, b;

    int add() {
        return a + b;
    }

    static int add(int a, int b) {
        return a + b;
    }
}

class TestClass {
    int iv;
    static int cv;

    void instanceMethod() {
        System.out.println(iv); // 인스턴스 메소드는 클래스 변수 사용 가능
        System.out.println(cv);
    }
    static void staticMethod() {
//        Non-static field 'iv' cannot be referenced from a static context
//        System.out.println(iv);
        System.out.println(cv);
    }

    void callMethodByInstanceMethod() {
        instanceMethod();
        staticMethod();
    }

    static void callMethodByStaticMethod() {
//        Cannot resolve method 'instaceMethod' in 'TestClass'
//        instaceMethod();
        staticMethod();
    }
}

public class StaticMethodTest {

    @Test
    public void staticMethodTest() {
        int a = 1;
        int b = 2;
        int staticCall = Calculator.add(a, b);

        Calculator calculator = new Calculator();
        int instanceCall = calculator.add(a, b);
    }

}
