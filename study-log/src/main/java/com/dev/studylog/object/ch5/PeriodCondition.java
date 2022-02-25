package com.dev.studylog.object.ch5;

import com.dev.studylog.object.ch4.DiscountConditionType;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class PeriodCondition implements DiscountCondition{
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public boolean isSatisfiedBy(Screening screening){
        return dayOfWeek.equals(screening.getWhenScreened().getDayOfWeek()) &&
                startTime.compareTo(screening.getWhenScreened().toLocalTime())<=0 &&
                endTime.compareTo(screening.getWhenScreened().toLocalTime())>=0;
    }
}
