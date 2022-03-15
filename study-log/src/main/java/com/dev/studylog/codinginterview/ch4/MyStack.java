package com.dev.studylog.codinginterview.ch4;

import java.util.Stack;

public class MyStack {

    class FixedMultiStack {
        private int numberOfStacks = 3;
        private int stackCapacity;
        private int[] values;
        private int[] sizes;

        public FixedMultiStack(int stackSize) {
            stackCapacity = stackSize;
            values = new int[numberOfStacks*stackSize];
            sizes = new int[numberOfStacks];
        }

        public void push(int stackNum, int value) throws IllegalArgumentException{
            if(isFull(stackNum)) throw new IllegalArgumentException();
            sizes[stackNum]++;
            values[indexOfTop(stackNum)] = value;
        }

        public int pop(int stackNum){
            if(isEmpty(stackNum)) throw new IllegalArgumentException();
            int topIndex = indexOfTop(stackNum);
            int value = values[topIndex];
            values[topIndex]= 0;
            sizes[stackNum]--;
            return value;
        }

        public int peek(int stackNum) {
            if(isEmpty(stackNum)) throw new IllegalArgumentException();
            int topIndex = indexOfTop(stackNum);
            return values[topIndex];

        }

        private boolean isFull(int stackNum) {
            return sizes[stackNum]==stackCapacity;
        }
        private int indexOfTop(int stackNum){
            int offSet = stackNum*stackCapacity;
            int size = sizes[stackNum];
            return offSet+size-1;
        }
        private boolean isEmpty(int stackNum){
            return sizes[stackNum]==0;
        }

    }


}
