package com.dev.studylog.dependencyInjection.refactoring;

import java.util.List;

public class CarController {

    private final InputView inputView;

    public CarController(InputView inputview){
        this.inputView = inputview;
    }

    private int getCountFromUser(){
        int count = inputView.getCount();
        return count;
    }

    private List<String> getCarNameFromUser(){
        return inputView.getCarName();
    }

    public void run(){
        List<String> result = getCarNameFromUser();
        int count = getCountFromUser();
        for(String str : result ) {
            System.out.println(str);
        }
        System.out.println(count);
    }
}
