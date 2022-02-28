package com.dev.studylog.object.ch5;

import java.time.LocalDateTime;

public class Screening {
    private Movie movie;
    private int sequence;
    private LocalDateTime whenScreened;

    public Reservation reserve( Customer customer, int audienceCount) {
        return null;
    }

    /*
    movie의 내부 구현에 대한 어떤 지식도 없ㄷ이 전송할 메시지를 결정했다.
     */
    public Money calculateFee(int audienceCount){
        return movie.calculateMovieFee(this).times(audienceCount);
    }

    public int getSequence() {
        return sequence;
    }

    public LocalDateTime getWhenScreened() {
        return whenScreened;
    }
}
