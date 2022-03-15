package com.dev.studylog.dependencyInjection.legacy;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class InputView {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String DELIMITER = ",";

    public static List<String> getCarNames() {
        System.out.println("자동차 이름을 입력하세요 (쉼표','로 구준)");
        String input = scanner.nextLine();
        return Arrays.asList(input.split(DELIMITER));
    }
}
