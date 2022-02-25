package com.dev.studylog.object.ch5;

import java.time.Duration;
import java.util.List;

public class AmountDiscountMovie extends Movie{
    private Money discountAmount;

    public AmountDiscountMovie(String title, Money fee, Duration runningTime, List<DiscountCondition> discountConditions, Money discountAmount) {
        super(title, fee, runningTime, discountConditions);
        this.discountAmount = discountAmount;
    }

    @Override
    protected Money calculateDiscountAmount() {
        return discountAmount;
    }
}
