package com.dev.studylog.pattern;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

// 커맨드패턴
// 요청을 홀로 처리할 수 있도록 요청을 수행하는 여러 인자를 함께 패키징하여 나중에 처리할 수 있도록 만들어주는 행동 중심 디자인패턴
/*
커맨드를 실행시키는 객체는 커맨드 내부의 ㅛ소에 대해서 숨김으로써 코드의 유연성을 가질 수 있다.

즉석 복권을 긁는 행위를 캡슐화
 */



public class PeopleWithLottery {
    private List<LotteryCommand> lotteryCommands;

    public PeopleWithLottery(List<LotteryCommand> lotteryCommands) {
        this.lotteryCommands = lotteryCommands;
    }

    public void addLotteryCommand(LotteryCommand lotteryCommand) {
        lotteryCommands.add(lotteryCommand);
    }

    public void scratchAllLottery(){
        for(int i=0; i<lotteryCommands.size();i++){
            LotteryCommand lotteryCommand = lotteryCommands.get(i);
            lotteryCommand.scratch();
        }
        lotteryCommands = new LinkedList<>();
    }

    public static void main(String[] args) {

        // 각각의 객체를 분리하고 행위의 구성요소를 독립적으로 씀

        // 보권을 ㅡㄹㄱ는 행위를 호출하는 객체
        PeopleWithLottery jisu = new PeopleWithLottery(new LinkedList<>());
        // 명령 수행으로 영향을 받는 객체
        Account account = new Account();

        for(int i=0; i<10; i++){

            InstantLottery instantLottery = new InstantLottery(false);
            //명려을 담당하는 ㅐㄱ체
            InstantScratch command = new InstantScratch(instantLottery, account);
            jisu.addLotteryCommand(command);
        }
        Set<Integer> win = new HashSet<>();
        win.add(1);
        for (int i=0; i<10; i++){
            Set<Integer> set = new HashSet<>();
            set.add(i);
            NumberLottery numberLottery = new NumberLottery(set);
            NumberScratch numberScratch = new NumberScratch(win, numberLottery, account);
            jisu.addLotteryCommand(numberScratch);
        }


        jisu.scratchAllLottery();
    }
}
