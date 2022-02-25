package com.dev.studylog.object.ch4;

public class ReservationAgency {

    public Reservation reserve(Screening screening, Customer customer, int audienceCount) {

        Money fee = screening.calculateFee(audienceCount);
        return new Reservation(customer, screening, fee, audienceCount);
    }
}
