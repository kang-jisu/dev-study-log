package com.dev.studylog.pattern;

import java.util.Set;

public class NumberScratch implements LotteryCommand{

    private Set<Integer> winners;
    private NumberLottery numberLottery;
    private Account account;

    public NumberScratch(Set<Integer> winners, NumberLottery numberLottery, Account account) {
        this.winners = winners;
        this.numberLottery = numberLottery;
        this.account = account;
    }

    @Override
    public void scratch() {
//winner와 numberLottery비교
        //account에 금액 입금
    }


}
