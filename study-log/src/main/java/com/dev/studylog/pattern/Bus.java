package com.dev.studylog.pattern;

public class Bus implements Transportation{
    @Override
    public void move(String start, String end) {
        System.out.println("출발점:"+start + "-> 목적지:"+end+"까지 버스로 이동 ");
    }
}
