package com.dev.studylog.Java.polymorphism;

public class BindingTest {

    public static void main(String[] args) {
        Parent p = new Child();
        Child c = new Child();

        System.out.println("p.x = " + p.x); // 100
        p.method(); // child method

        System.out.println("c.x = " + c.x); // 200
        c.method(); // child method
    }

}

class Parent {
    int x = 100;
    void method() {
        System.out.println("Parent method");
    }
}

class Child extends Parent {
    int x =200;
    void method(){
        System.out.println("Child Method");
    }
}