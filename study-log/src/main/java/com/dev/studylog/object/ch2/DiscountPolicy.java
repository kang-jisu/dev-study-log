package com.dev.studylog.object.ch2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface DiscountPolicy {
//public abstract class DiscountPolicy {
    Money calculateDiscountAmount(Screening screening);

}
