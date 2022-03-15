package com.dev.studylog.Java.polymorphism;

public class ProductTest {
    public static void main(String[] args) {
        BuyerPoliy b = new BuyerPoliy();
        b.buy(new Tv());
        b.buy(new Computer());

        System.out.println("현재 남은 돈 "+ b.money+"만원 입니다");
        System.out.println("현재 보너스 점수는 "+ b.bonusPoint +"점 입니다");

        /*
TV를 구입하셨습니다
Computer를 구입하셨습니다
현재 남은 돈 700만원 입니다
현재 보너스 점수는 30점 입니다
         */
    }
}

class Product {
    int price;
    int bonusPoint;

    Product(int price) {
        this.price = price;
        bonusPoint = (int) (price/10.0);
    }
}

class Tv extends Product {
    Tv(){
        super(100);
    }
    public String toString() { return "TV";}
}
class Computer extends Product {
    Computer() {
        super(200);
    }
    public String toString() { return "Computer";}

}
class Audio extends Product {
    Audio(){
        super(200);
    }
    public String toString() { return "Audio";}

}

class Buyer {
    int money = 1000;
    int bonusPoint = 0;

    void buy(Tv t) {
        money = money - t.price;
        bonusPoint = bonusPoint + t.bonusPoint;
    }

    void buy(Computer c) {
        money = money - c.price;
        bonusPoint = bonusPoint + c.bonusPoint;
    }

    void buy(Audio a) {
        money = money - a.price;
        bonusPoint = bonusPoint + a.bonusPoint;
    }
}
// 메서드 매개변수에 다형성을 적용하면 하나의 메서드로 처리할 수 있다.
class BuyerPoliy {
    int money = 1000;
    int bonusPoint = 0;

    void buy(Product p) {
        if( money < p.price) {
            System.out.println("잔액이 부족하여 물건을 구매할 수 없습니다.");
            return;
        }
        money -= p.price;
        bonusPoint += p.bonusPoint;
        System.out.println(p+"를 구입하셨습니다");
    }
}