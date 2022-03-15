package com.dev.studylog.Java.inheritance;

class Tv {
    boolean power;
    int channel;

    void power() {
        power = !power;
    }
    void channelUp() {
        ++channel;
    }
    void channelDown(){
        --channel;
    }
}

class CaptionTv extends Tv {
    boolean caption; // 자막  상태 on, off
    void displayCaption(String text) {
        if(caption) {
            System.out.println(text);
        }
    }
}


public class CaptionTvTest {
    public static void main(String[] args) {
        CaptionTv ctv = new CaptionTv();
        ctv.channel = 10;
        ctv.channelUp();
        System.out.println("channel:"+ ctv.channel);
        ctv.displayCaption("hello");
        ctv.caption = true;
        ctv.displayCaption("hello");
    }
}