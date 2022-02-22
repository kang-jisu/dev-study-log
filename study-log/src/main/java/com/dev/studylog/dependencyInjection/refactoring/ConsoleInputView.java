package com.dev.studylog.dependencyInjection.refactoring;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ConsoleInputView implements InputView{


    private static final Scanner scanner = new Scanner(System.in);
    private static final String DELIMITER = ",";

    private static final String CAR_NAME_INPUT_MESSAGE = "자동차 이름을 입력하세요 (쉼표','로 구준)";
    private static final String COUNT_INPUT_MESSAGE = "시도할 횟수는?";

    public List<String> getCarName() {
        System.out.println(CAR_NAME_INPUT_MESSAGE);
        String input = scanner.nextLine();
        return Arrays.asList(input.split(DELIMITER));
    }

    public int getCount(){
        System.out.println(COUNT_INPUT_MESSAGE);
        String input = scanner.nextLine();
        try {
            return Integer.parseInt(input);
        }catch (NumberFormatException e) {
            throw new IllegalArgumentException("숫자를 입력해주세요");
        }
    }
}
