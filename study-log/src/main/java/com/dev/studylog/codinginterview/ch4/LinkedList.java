package com.dev.studylog.codinginterview.ch4;


public class LinkedList {

    /**
     * 할 수 있다면 이런 방식보다는 Node를 포함한 LinkedList 클래스를 만드는것이 좋다. head Node를 단 하나만 정의해 놓음으로써 head가 변경될 때 영향을 없앨 수 있다.
     */
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
    class LinkedListNode {
        LinkedListNode next;
        int data;
    }
    /*
    추가 공간을 사용하지 않고 중복 제거
     */
    void deleteDups(LinkedListNode head) {
        LinkedListNode current = head;
        while( current != null ){
            LinkedListNode runner = current;
            while(runner.next !=null ){
                if( runner.next.data == current.data) {
                    runner.next = runner.next.next;
                }
                else runner = runner.next;
            }
            current = current.next;
        }
    }
    /*
    뒤에서 k번째 수를 찾는 방법
     */
    static class Index {
        public int value= 0;
    }
    static Node kthToLast(Node head, int k) {
        Index index = new Index();
        return kthToLast(head, k , index);
    }
    static Node kthToLast(Node head, int k, Index index) {
        if( head==null){
            return null;
        }
        Node node = kthToLast(head.next, k, index);
        index.value = index.value+1;
        if(index.value == k) {
            return head;
        }
        return node;
    }

    static Node nthToLoast(Node head, int k) {
        Node p1 = head;
        Node p2 = head;
        for(int i=0; i<k; i++){
            if(p1==null) return null;
            p1 = p1.next;
        }
        while( p1!=null){
            p1 = p1.next;
            p2 = p2.next;
        }
        return p2;
    }
    public static void main(String[] args) {
//        Node node = new Node(-1);
//        for(int i=0; i<10; i++){
//            node.appendToTail(i);
//        }

//        System.out.println(kthToLast(node, 3).data);
//        System.out.println(nthToLoast(node,4).data);
//
//        Node node1 = new Node(11);
//        for(int i=0; i<10; i++){
//            node1.appendToTail(10-i);
//        }
//        Divide(node1, 4);
        Node node = new Node(6);
        node.appendToTail(1);
        node.appendToTail(7);

        Node node1 = new Node(2);
        node1.appendToTail(9);
        node1.appendToTail(5);

        Node node2 = new Node(7);
        node2.appendToTail(1);

        Node node3 = new Node(5);
        node3.appendToTail(9);
        node3.appendToTail(2);
        sumListReverse(node2, node3);
        addLists(node2, node3);
    }

    /*
    2.4 분할
     */
    public static void Divide(Node head, int k){
        Node left = null;
        Node right =null;
        Node start = null;
        Node rightStart = null;
        while(head!=null){
            Node next = head.next;
            head.next = null;
            if(head.data < k){
                if(left==null) {
                    start = head;
                    left = start;

                }
                else {
                    left.next = head;
                    left = left.next;
                }
            }
            else {
                if( right == null) {
                    rightStart = head;
                    right = rightStart;
                }
                else {
                    right.next = head;
                    right = right.next;
                }
            }
            head =  next;
        }
        // left가 널일때 처리 필요
        left.next = rightStart;
        while(start.next!=null) {
            System.out.println(start.data);
            start = start.next;
        }
    }

    /**
     * 2.5 리스트의 합
     */
    public static void sumList(Node p1, Node p2) {
        Node newNode = null;
        Node start = null;
        while( p1!=null && p2 !=null) {
            int a = p1.data + p2.data;
            Node node = new Node(a%10);
            if(start==null) {
                start = node;
                newNode = node;
            }else {
                newNode.data += a / 10;
                newNode.next = node;
                newNode = newNode.next;
            }
            p1 = p1.next;
            p2 = p2.next;
        }
        while( p1!=null ) {
            int a = p1.data ;
            Node node = new Node(a%10);
            newNode.next = node;
            p1 = p1.next;
            newNode = newNode.next;
        }
        while( p2!=null ) {
            int a = p2.data;
            Node node = new Node(a%10);
            newNode.next = node;
            p2 = p2.next;
            newNode = newNode.next;
        }

        while(start!=null){
            System.out.println(start.data);
            start = start.next;
        }

    }
    public static void sumListReverse(Node p1, Node p2) {
        Node newNode = null;
        Node start = null;
        int tmp = 0;
        while( p1!=null && p2 !=null) {
            int a = p1.data + p2.data + tmp;
            Node node = new Node(a%10);
            if(start==null) {
                start = node;
                newNode = node;
            }else {
                newNode.next = node;
                newNode = newNode.next;
            }
            tmp = a/10;
            p1 = p1.next;
            p2 = p2.next;
        }
        while( p1!=null ) {
            int a = p1.data + tmp ;
            Node node = new Node(a%10);
            tmp = a/10;
            newNode.next = node;
            p1 = p1.next;
            newNode = newNode.next;
        }
        while( p2!=null ) {
            int a = p2.data + tmp;
            Node node = new Node(a%10);
            tmp = a/10;
            newNode.next = node;
            p2 = p2.next;
            newNode = newNode.next;
        }
        if(tmp!=0){
            Node node = new Node(tmp);
            newNode.next = node;
        }

        while(start!=null){
            System.out.println(start.data);
            start = start.next;
        }

    }

    /**
     * 리스트의 합 해설 풀이
     */
    static class PartialSum {
        public Node sum = null;
        public int carry = 0;
    }
    static int length(Node l1){
        int len = 0;
        while(l1!=null) {
            len++;
            l1 = l1.next;
        }
        return len;
    }
    static Node addLists(Node l1, Node l2) {
        int len1 = length(l1);
        int len2 = length(l2);

        /*
        길이가 맞지 않을 경우 앞에 0을 붙여줌
         */
        if(len1<len2) {
            l1 = padList(l1, len2-len1);
        }else {
            l2 = padList(l2, len1-len2);
        }

        PartialSum sum = addListHelper(l1, l2);

        if( sum.carry==0) {
            return sum.sum;
        }
        else {
            Node result = insertBefore(sum.sum, sum.carry);
            return result;
        }
    }

    static PartialSum addListHelper(Node l1, Node l2) {
        if(l1==null && l2 ==null) {
            PartialSum sum = new PartialSum();
            return sum;
        }
        PartialSum sum = addListHelper(l1.next, l2.next);
        int val = sum.carry + l1.data + l2.data;
        Node full_result = insertBefore(sum.sum, val%10);
        sum.sum = full_result;
        sum.carry = val/10;
        return sum;
    }

    static Node padList( Node l, int padding) {
        Node head = l;
        for(int i=0; i<padding; i++){
            head = insertBefore(head, 0);
        }
        return head;
    }
    static Node insertBefore(Node list, int data){
        Node node = new Node(data);
        if(list!=null){
            node.next = list;
        }
        return node;
    }
}
