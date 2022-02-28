package com.dev.studylog.codinginterview.ch3;

public class timeComplexity {

    /*
    EX1
     */
    int sum(int n) {
        if (n <= 0) return 0;
        return n + sum(n - 1);
    }

    /*
    EX2
     */
    int pairSumSequence(int n){
        int sum=0;
        for (int i=0; i<n; i++){
            sum+=pairSum(i, i+1);
        }
        return sum;
    }
    int pairSum(int a, int b) {
        return a+b;
    }

    static void permutation(String str) {
        permutation(str, "");
    }
    static void permutation(String str, String prefix) {
        if(str.length()==0) {
            System.out.println(prefix);
        }
        else {
            for(int i=0; i<str.length(); i++) {
                String rem = str.substring(0,i) + str.substring(i+1);
                permutation(rem , prefix + str.charAt(i));
            }
        }
    }

    public static void main(String[] args)  {
        permutation("hel");
    }
}
