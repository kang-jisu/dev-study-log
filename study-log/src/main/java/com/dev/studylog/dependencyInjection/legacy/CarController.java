package com.dev.studylog.dependencyInjection.legacy;

import java.util.List;

public class CarController {

    private List<String> getCarNamesFromUser(){
        try {
            return InputView.getCarNames();
        }catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return getCarNamesFromUser();
        }
    }
}
