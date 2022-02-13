package com.dev.studylog.enumtest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.util.EnumUtils;

import java.util.Arrays;

public class EnumLowerCaseTest {

    private enum GameType{
        REGULAR,
        LEAGUE,
    }

    @Test
    @DisplayName("소문자 변환 실패")
    public void enumTest(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> GameType.valueOf("regular"));

    }

    @Test
    @DisplayName("EnumUtil")
    public void enumUtil(){
        // 내부에서 equalsIgnoreCase로 검사 하고있음
        GameType gameType= EnumUtils.findEnumInsensitiveCase(GameType.class, "regular");
        System.out.println(gameType);

        boolean Tf = Arrays.stream(GameType.values()).filter(
                e -> e.name().equalsIgnoreCase("regular")
        )
        .findAny().isPresent();
        System.out.println(String.valueOf(Tf));
    }
}