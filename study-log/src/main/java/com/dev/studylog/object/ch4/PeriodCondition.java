package com.dev.studylog.object.ch4;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class PeriodCondition implements DiscountCondition {
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public PeriodCondition(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /*
    상영 시작 시간이 특정한 기간 안에 포함되는지 여부
    요일이 같고 시작 시간이 startTime과 endTime사이에 있을 경우 True를 반환
     */
    @Override
    public boolean isSatisfiedBy(Screening screening) {
        return screening.getStartTIme().getDayOfWeek().equals(dayOfWeek) &&
                startTime.compareTo(screening.getStartTIme().toLocalTime()) <= 0 &&
                endTime.compareTo(screening.getStartTIme().toLocalTime()) >= 0;
    }
}
