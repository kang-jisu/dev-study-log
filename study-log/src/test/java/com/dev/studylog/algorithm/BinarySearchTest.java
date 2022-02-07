package com.dev.studylog.algorithm;

import org.junit.jupiter.api.Test;

public class BinarySearchTest {

    int [] array;
    public int binarSearch(int target) {
        int left = 0;
        int right = array.length-1;

        while( left <= right) {
            int mid = (left+right)/2;
            if(array[mid]==target) return mid;
            else if(array[mid] > target) right = mid -1;
            else left = mid+1;
        }
        return -1;
    }

    public int lowerBound(int target) {
        int left=0;
        int right=array.length; // target이 array에 존재하는 값보다 클 수 있으므로
        while( left < right) {
            int mid = (left+right)/2;
            if( array[mid]< target) left = mid+1;// target보다 작을때만 left를 변경
            else right = mid;// mid값이 lowerbound가 될 수 있으므로, 그리고 최종연산후엔 항상 right이 답
        }
        return right;
    }

    public int upperBound(int target) {
        int left=0;
        int right=array.length; // target이 array에 존재하는 값보다 클 수 있으므로
        while( left < right) {
            int mid = (left+right)/2;
            if(array[mid] <= target) left = mid+1; // target보다 큰걸 찾았어도 left를 증가시킬것
            else right = mid;
        }
        return right;
    }

    @Test
    public void binarySearchTest(){
        array = new int[]{1,2,3,3,4,5,6,8,10,13};
        System.out.println(binarSearch(3));
        System.out.println(binarSearch(7));
        System.out.println(binarSearch(15));
        System.out.println(binarSearch(5));
        System.out.println(binarSearch(1));
    }

    @Test
    public void lowerBoundTest(){
        array = new int[]{1,2,3,3,4,5,6,8,10,13};
        System.out.println(lowerBound(3));
        System.out.println(lowerBound(7));
        System.out.println(lowerBound(13));
        System.out.println(lowerBound(5));
        System.out.println(lowerBound(1));
    }

    @Test
    public void upperBoundTest(){
        array = new int[]{1,2,3,3,4,5,6,8,10,13};
        System.out.println(upperBound(3));
        System.out.println(upperBound(7));
        System.out.println(upperBound(15));
        System.out.println(upperBound(5));
        System.out.println(upperBound(1));
    }
}
