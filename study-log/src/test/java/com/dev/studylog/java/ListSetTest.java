package com.dev.studylog.java;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ListSetTest {


    @Test
    @DisplayName("set")
    public void setList(){
        List<String> members = new ArrayList<>();
        members.add("아아");
        members.add("안녕");
        members.add("ㅎㅇ");
        members.add("아아");
        Set<String> set = new HashSet<>(members);
        List<String> newMembers = new ArrayList<>(set);
        System.out.println(newMembers);
    }

    @Test
    @DisplayName("stream")
    public void Stream(){
        List<String> members = new ArrayList<>();
        members.add("아아");
        members.add("안녕");
        members.add("ㅎㅇ");
        members.add("아아");
        Set<String> set = new HashSet<>(members);
        List<String> newMembers = members.stream().distinct().collect(Collectors.toList());
        System.out.println(newMembers);
    }
}
