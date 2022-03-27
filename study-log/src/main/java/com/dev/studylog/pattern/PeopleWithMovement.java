package com.dev.studylog.pattern;

//전략을 행할 주체
public class PeopleWithMovement {
    // 전략
    private Transportation transportation;

    public PeopleWithMovement(Transportation transportation) {
        this.transportation = transportation;
    }

    public void move(String start, String end) {
        transportation.move(start, end);
    }
    public void changeTransportation(Transportation transportation) {
        this.transportation = transportation;
    }


    public static void main(String[] args) {
        //전략패턴
        //전략을 행할 주체는 인터페이스를 의존하고
        //인터페이스를 구현하는 전략을 따로 두어
        // 캡슐화된 로직을 선택할 수 있게 해줌. 중간에 전략을 바꾸는것도 가능
        // 전략
        Bicycle bicycle = new Bicycle();
        Bus bus = new Bus();

        //전략을 행할 주체
        PeopleWithMovement first = new PeopleWithMovement(bicycle);
        first.move("시작점","끝점");

        PeopleWithMovement second = new PeopleWithMovement(bus);
        second.move("시작점","끝점");

        PeopleWithMovement change = new PeopleWithMovement(bicycle);
        change.move("시작점","끝점");
        change.changeTransportation(bus);
        change.move("시작점","끝점");
    }
}
