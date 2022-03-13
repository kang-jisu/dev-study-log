package com.dev.studylog.Java.inheritance;

public class DrawShape {

    public static void main(String[] args) {
        Point[] p = {
                new Point(100,100),
                new Point(140,50),
                new Point(200,100),
        };

        Triangle t = new Triangle(p);
        Circle c = new Circle(new Point(150,150), 50);
        t.draw();
        c.draw();
    }
}

class Point {
    int x;
    int y;
    Point( int x, int y) {
        this.x = x;
        this.y = y;
    }
    Point(){
        this(0,0);
    }
    String getPoint(){
        return "("+x+","+y+")";
    }
}

class Shape {
    String color = "black";
    void draw(){
        System.out.println("color = " + color);
    }
}
//원은 도형이다
//원은 좌표를 가지고있다.
class Circle extends Shape {
    Point center; //원점좌표
    int r; //반지름

    Circle(){
        this(new Point(0,0),100);
    }
    Circle(Point center, int r) {
        this.center = center;
        this.r= r;
    }
    void draw() {
        //원을 그리는 대신에 원의 정보를 출력
        System.out.printf("[center=(%d, %d), r=%d, color=%s]\n", center.x, center.y, r, color);
    }
}

class Triangle extends Shape {
    Point[] p = new Point[3];
    Triangle(Point[] p) {
        this.p = p;
    }
    void draw() {
        System.out.printf("[p1 = %s, p2=%s, p3=%s, color=%s]\n", p[0].getPoint(), p[1].getPoint(), p[2].getPoint(), color);
    }
}