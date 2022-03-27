package com.dev.studylog.pattern;

public class Bicycle implements Transportation {

    @Override
    public void move(String start, String end) {
        System.out.println("출발점:"+start + "-> 목적지:"+end+"까지 자전거로 이동 ");
    }
}