package com.dev.studylog.object.ch2;

import java.time.LocalDateTime;

public class Screening {

    private Movie movie;
    private int sequence;
    private LocalDateTime whenScreened;

    public Screening(Movie movie, int sequence, LocalDateTime whenScreened) {
        this.movie = movie;
        this.sequence = sequence;
        this.whenScreened = whenScreened;
    }

    public LocalDateTime getStartTIme(){
        return whenScreened;
    }
    public boolean isSequence(int sequence){
        return this.sequence == sequence;
    }
    public Money getMovieFee(){
        return movie.getFee();
    }

    /*
    영화를 예매한 후 예매 정보를 담고있는 Reservation의 인스턴스를 생성해서 반환
    customer는 예매자에 대한 정보를 담고있고 audienceCount는 인원수
     */
    public Reservation reserve(Customer customer, int audienceCount){
        return new Reservation(customer, this, calculateFee(audienceCount), audienceCount);
    }

    private Money calculateFee(int audienceCount) {
        return movie.calculateMovieFee(this).times(audienceCount);
    }
}
