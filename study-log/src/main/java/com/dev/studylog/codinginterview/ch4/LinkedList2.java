package com.dev.studylog.codinginterview.ch4;

import java.util.Stack;

public class LinkedList2 {

    static class Node {
        Node next = null;
        int data;
        public Node( int d) {
            data = d;
        }
        void appendToTail(int d) {
            Node end  = new Node(d);
            Node n = this;
            while(n.next !=null) {
                n = n.next;
            }
            n.next= end;
        }
    }
    /*
    스택, slow faster 사용
     */
    static boolean isPalindrome(Node head) {
        Node fast = head;
        Node slow = head;

        Stack<Integer> stack = new Stack<>();
        while(fast!=null && fast.next!=null) {
            stack.push(slow.data);
            slow = slow.next;
            fast = fast.next.next;
        }
        // 홀수
        if(fast!=null) slow= slow.next;

        while(slow!=null) {
            int pop = stack.pop();
            if(pop!=slow.data) return false;
            slow = slow.next;
        }
        return true;
    }

    /*
    교집합
     */

    static class Result {
        int len;
        Node last;
        Result(int len, Node last){
            this.len  = len;
            this.last = last;
        }
    }
    public static  Node intersection(Node l1, Node l2) {
        Result res1 = findLengthAndLastNode(l1);
        Result res2 = findLengthAndLastNode(l2);

        if(res1.last != res2.last) return null;
        if(res1.len>res2.len){
            int cnt = res1.len - res2.len;
            while(cnt-->0){
                l1 = l1.next;
            }
        }
        else {
            int cnt = res2.len - res1.len;
            while(cnt-->0){
                l2 = l2.next;
            }
        }
        while(l1!=l2){
            l1=l1.next;
            l2=l2.next;
        }

        return l1;
    }

    static Result findLengthAndLastNode(Node l1){
        int len = 0;
        while(l1.next!=null){
            len++;
            l1 = l1.next;
        }
        if(l1!=null) len++;
        return new Result(len,l1);
    }


    public static void main(String[] args) {
        Node node = new Node(0);
        node.appendToTail(1);
        node.appendToTail(2);
        node.appendToTail(1);
        node.appendToTail(0);
        System.out.println( isPalindrome(node ));

        Node node1 = new Node(1);

        intersection(node, node1);
    }
}
