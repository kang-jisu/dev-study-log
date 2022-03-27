package com.dev.studylog.pattern;

public class InstantLottery {

    private boolean win;
    public InstantLottery(boolean win) {
        this.win = win;
    }

    public boolean isWin() {
        return win;
    }
}
