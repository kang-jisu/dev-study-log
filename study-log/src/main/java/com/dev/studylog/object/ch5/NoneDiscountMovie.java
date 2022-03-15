package com.dev.studylog.object.ch5;

import java.time.Duration;
import java.util.List;

public class NoneDiscountMovie extends Movie{
    public NoneDiscountMovie(String title, Money fee, Duration runningTime, List<DiscountCondition> discountConditions) {
        super(title, fee, runningTime, discountConditions);
    }

    @Override
    protected Money calculateDiscountAmount() {
        return Money.ZERO;
    }
}
