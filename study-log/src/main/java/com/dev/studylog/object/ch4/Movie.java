package com.dev.studylog.object.ch4;

import java.time.Duration;

/*
제목, 상영시간, 기본요금, 할인 정책
 */
public class Movie {
    private String title;
    private Duration runningTime;
    private Money fee;
    private DiscountPolicy discountPolicy;

    public Movie(String title, Duration runningTime, Money fee, DiscountPolicy discountPolicy) {
        this.title = title;
        this.runningTime = runningTime;
        this.fee = fee;
        this.discountPolicy = discountPolicy;
    }

    public Money getFee(){
        return fee;
    }

    /*
    어떤 할인 정책을 사용할 것인지는 결정하지 않는다. 단지 discountPolicy에게 메세지를 전송한다.
     */
    public Money calculateMovieFee(Screening screening){
        // -> 할인 정책이 없는 경우 할인 금액 계산의 책임을 Movie가 가짐 . 좋지 않음 . NoneDiscountPolicy도 생성
        // if( discountPolicy==null) return fee;
        return fee.minus(discountPolicy.calculateDiscountAmount(screening));
    }

    public void changeDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }
}
