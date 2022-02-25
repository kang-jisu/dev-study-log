package com.dev.studylog.object.ch4;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

/*
제목, 상영시간, 기본요금, 할인 정책
 */
public class Movie {
    private String title;
    private Duration runningTime;
    private Money fee;

    /*
    데이터 중심 설계를 위한 필드로 변경해봄 ( 이거 안좋은 케이스 예시인거임 )
     */
    private List<DiscountCondition> discountConditions;

    private MovieType movieType;
    private Money discountAmount;
    private double discountPercent;

    public List<DiscountCondition> getDiscountConditions() {
        return Collections.unmodifiableList(discountConditions);
    }

    public void setDiscountConditions(List<DiscountCondition> discountConditions) {
        this.discountConditions = discountConditions;
    }

    public MovieType getMovieType() {
        return movieType;
    }

    public void setMovieType(MovieType movieType) {
        this.movieType = movieType;
    }

    public Money getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Money discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Money getFee(){
        return fee;
    }

    public void setFee(Money fee) {
        this.fee = fee;
    }

}
