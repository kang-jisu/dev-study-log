package com.dev.studylog.Java.interfaceTest;

public class DefaultMethodTest {
    public static void main(String[] args) {
       ChildC c = new ChildC();
       c.method1();
       c.method2();
       MyInterface.staticMethod();
       MyInterface2.staticMethod();
    }
}

class ChildC extends Parent implements MyInterface, MyInterface2 {
    public void method1(){
        System.out.println("method1() in child"); //오버라이딩 안하면 오류남
    }
}

class Parent {
    public void method2(){
        System.out.println("method2() in parent");
    }
}
interface MyInterface {
    default void method1(){ //중복
        System.out.println("method1() in Myinterface");
    }
    default void method2(){
        System.out.println("method2() in Myinterface");
    }
    static void staticMethod(){
        System.out.println("staticMethod() in Myinterface");
    }
}

interface  MyInterface2 {
    default void method1(){ //중복
        System.out.println("method1() in Myinterface2");
    }

    static void staticMethod(){
        System.out.println("staticMethod() in Myinterface2");
    }
}