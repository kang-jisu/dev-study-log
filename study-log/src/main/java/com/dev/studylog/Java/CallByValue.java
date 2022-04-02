package com.dev.studylog.Java;

import java.util.Stack;

public class CallByValue {

    public static void swap(int x, int y) {
        int tmp = x;
        x = y;
        y = tmp;
    }
    public static void main(String[] args) {
        int a = 10;
        int b = 20;

        System.out.println("swap 호출 전 : "+ a+","+b);
        swap(a,b);
        System.out.println("swap 호출 후 : "+ a+","+b);


        //-----

        String s = new String("abc");
        String s2 = s;

        foo(s);
        System.out.println("#3 "+ s.equals("abc")); // true
        System.out.println("#4 "+ s.equals("ccc")); // false
        System.out.println("#5 "+ s.equals("aaa")); // false
        System.out.println("#6 "+ (s==s2 )); // true
    }

    static void foo(String str) {
        String newStr = new String("aaa");
        System.out.println("#0 "+ str.equals("abc")); // true
        str = newStr;
        System.out.println("#1 "+ str.equals("ccc")); // true
        System.out.println("#2 "+ str.equals("aaa")); // true
    }
}
