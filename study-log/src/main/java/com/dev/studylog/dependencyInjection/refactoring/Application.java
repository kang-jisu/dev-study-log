package com.dev.studylog.dependencyInjection.refactoring;

public class Application {

    public static void main(String[] args) {
        InputView inputView = new ConsoleInputView();
        CarController controller = new CarController(inputView);
        controller.run();
    }
}
