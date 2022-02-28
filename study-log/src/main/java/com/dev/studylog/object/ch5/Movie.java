package com.dev.studylog.object.ch5;

import java.time.Duration;
import java.util.List;

public abstract class Movie {

    private String title;
    private Money fee;
    private Duration runningTime;
    private List<DiscountCondition> discountConditions;

    public Movie(String title, Money fee, Duration runningTime, List<DiscountCondition> discountConditions) {
        this.title = title;
        this.fee = fee;
        this.runningTime = runningTime;
        this.discountConditions = discountConditions;
    }

    public Money calculateMovieFee(Screening screening) {
        if(isDiscountable(screening)) {
            return fee.minus(calculateDiscountAmount());
        }
        return fee;
    }

    private boolean isDiscountable(Screening screening){
        return discountConditions.stream()
                .anyMatch(condition-> condition.isSatisfiedBy(screening));
    }

    abstract protected Money calculateDiscountAmount();
    protected Money getFee(){
        return fee;
    }
}
