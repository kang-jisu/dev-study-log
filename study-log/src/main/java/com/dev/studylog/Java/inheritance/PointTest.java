package com.dev.studylog.Java.inheritance;

public class PointTest {
    public static void main(String[] args) {
        Point13D p3 = new Point13D();
        System.out.println("p3.x = " + p3.x);
        System.out.println("p3.y = " + p3.y);
        System.out.println("p3.z = " + p3.z);
    }
}

class Point1 {
    int x = 10;
    int y = 20;
    
    Point1(int x, int y){
        this.x= x;
        this.y= y;
    }
}


class Point13D extends Point1 {
    int z = 30;
    Point13D() {
        this(100,200,300);
    }
    
    Point13D(int x, int y, int z){
        super(x,y);
        this.z=z;
    }
}