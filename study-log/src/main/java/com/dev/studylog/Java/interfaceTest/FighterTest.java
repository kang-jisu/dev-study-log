package com.dev.studylog.Java.interfaceTest;

public class FighterTest {
    public static void main(String[] args) {
        Fighter f = new Fighter();
        if ( f instanceof Unit){
            System.out.println("unit의 자손");
        }
        if(f instanceof Fightable) {
            System.out.println("fightable 인터페이스 구현");
        }
        if(f instanceof Movable) {
            System.out.println("movable 인터페이스 구현");
        }
        if(f instanceof Attackable) {
            System.out.println("attackable 인터페이스 구현");
        }
        if( f instanceof Object) {
            System.out.println("Object클래스의 자손 ");
        }
    }
}

class Fighter extends Unit implements Fightable {
    public void move(int x, int y) {
        System.out.println("move");
    }
    public void attack(Unit u) {
        System.out.println("attack");
    }
}
class Unit {
    int currentHP;
    int x;
    int y;
}
interface Fightable extends Movable, Attackable {}

interface  Movable {
    void move(int x, int y);
}
interface  Attackable {
    void attack(Unit u);
}
