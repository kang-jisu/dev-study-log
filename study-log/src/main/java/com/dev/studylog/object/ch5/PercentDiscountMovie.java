package com.dev.studylog.object.ch5;

import java.time.Duration;
import java.util.List;

public class PercentDiscountMovie extends Movie{
    private double percent;

    public PercentDiscountMovie(String title, Money fee, Duration runningTime, List<DiscountCondition> discountConditions, double percent) {
        super(title, fee, runningTime, discountConditions);
        this.percent = percent;
    }

    @Override
    protected Money calculateDiscountAmount() {
        return getFee().times(percent);
    }
}
