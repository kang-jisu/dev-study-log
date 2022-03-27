package com.dev.studylog.pattern;

public class InstantScratch implements LotteryCommand{

    private InstantLottery instantLottery;
    private Account account;

    public InstantScratch(InstantLottery instantLottery, Account account) {
        this.instantLottery = instantLottery;
        this.account = account;
    }

    @Override
    public void scratch() {
        // instantLottery의ㅣ 당첨을 확인하고 account에 돈을 집어넣는 로직
    }
}
