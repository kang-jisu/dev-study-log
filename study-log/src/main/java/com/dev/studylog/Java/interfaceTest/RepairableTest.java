package com.dev.studylog.Java.interfaceTest;

public class RepairableTest {
    public static void main(String[] args) {
        Tank tank = new Tank();
        Dropship dropship = new Dropship();
        Marine marine = new Marine();
        SCV scv = new SCV();
        scv.repair(tank);
        scv.repair(dropship);
        // scv.repair(marine); error -> no repairable
    }
}

interface Repairable {}

class GameUnit {
    int hitPoint;
    final int MAX_HP;
    GameUnit(int hp) {
        MAX_HP = hp;
    }
    // ...
}

class GroundUnit extends GameUnit {
    GroundUnit(int hp) {
        super(hp);
    }
}

class AirUnit extends GameUnit {
    AirUnit(int hp) {
        super(hp);
    }
}

class Tank extends GroundUnit implements Repairable {
    Tank() {
        super(150);
        hitPoint = MAX_HP;
    }
    public String toString(){
        return "Tank";
    }
}

class Dropship extends AirUnit implements Repairable {
    Dropship() {
        super(125);
        hitPoint = MAX_HP;
    }
    public String toString(){
        return "Dropship";
    }
}

class Marine extends GroundUnit {
    Marine() {
        super(40);
        hitPoint = MAX_HP;
    }
}

class SCV extends AirUnit implements Repairable {
    SCV () {
        super(60);
        hitPoint = MAX_HP;
    }
    void repair(Repairable r){
        if( r instanceof GameUnit) {
            GameUnit u = (GameUnit) r;
            while(u.hitPoint !=u.MAX_HP) {
                u.hitPoint++;
            }
            System.out.println(u.toString()+"의 수리가 끝났습니다.");
        }
    }
}