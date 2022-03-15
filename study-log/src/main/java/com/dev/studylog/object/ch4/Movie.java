package com.dev.studylog.object.ch4;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/*
제목, 상영시간, 기본요금, 할인 정책
 */
public class Movie {
    private String title;
    private Duration runningTime;
    private Money fee;

    /*
    데이터 중심 설계를 위한 필드로 변경해봄 ( 이거 안좋은 케이스 예시인거임 )
     */
    private List<DiscountCondition> discountConditions;

    private MovieType movieType;
    private Money discountAmount;
    private double discountPercent;

    public MovieType getMovieType() {
        return movieType;
    }

    public Money calculateAmountDiscountedFee() {
        if (movieType != MovieType.AMOUNT_DISCOUNT) {
            throw new IllegalArgumentException();
        }
        return fee.minus(discountAmount);
    }

    public Money calculatePercentDiscountedFee() {
        if (movieType != MovieType.PERCENT_DISCOUNT) {
            throw new IllegalArgumentException();
        }
        return fee.minus(fee.times(discountPercent));
    }

    public Money calculateNoneDiscountedFee() {
        if (movieType != MovieType.NONE_DISCOUNT) {
            throw new IllegalArgumentException();
        }
        return fee;
    }

    public boolean isDiscountable(LocalDateTime whenScreend, int sequence) {
        for (DiscountCondition condition : discountConditions) {
            if (condition.getType() == DiscountConditionType.PERIOD) {
                if (condition.isDiscountable(whenScreend.getDayOfWeek(), whenScreend.toLocalTime())) {
                    return true;
                }
            } else {
                if (condition.isDiscountable(sequence)) {
                    return true;
                }
            }
        }
        return false;
    }

}
