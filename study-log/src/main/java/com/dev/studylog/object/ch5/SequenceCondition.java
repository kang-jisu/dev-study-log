package com.dev.studylog.object.ch5;

import com.dev.studylog.object.ch4.DiscountConditionType;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class SequenceCondition implements  DiscountCondition{
    private int sequence;

    public SequenceCondition(int sequence){
        this.sequence = sequence;
    }

    public boolean isSatisfiedBy(Screening screening){
        return sequence == screening.getSequence();
    }
}
