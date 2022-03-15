package com.dev.studylog.object.ch6;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

public class RecuringSchedule {
    private String subject;
    private DayOfWeek dayOfWeek;
    private LocalTime from;
    private Duration duration;

    public RecuringSchedule(String subject, DayOfWeek dayOfWeek, LocalTime from, Duration duration) {
        this.subject = subject;
        this.dayOfWeek = dayOfWeek;
        this.from = from;
        this.duration = duration;
    }

    public DayOfWeek getDayOfWeek(){
        return dayOfWeek;
    }
    public LocalTime getFrom(){
        return from;
    }
    public Duration getDuration(){
        return duration;
    }
}
