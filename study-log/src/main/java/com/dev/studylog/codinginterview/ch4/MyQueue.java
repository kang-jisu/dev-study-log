package com.dev.studylog.codinginterview.ch4;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class MyQueue {
    public static class QueueNode{
        private int data;
        private QueueNode next;
        public QueueNode(int data){
            this.data = data;
        }
    }
    private QueueNode first;
    private QueueNode last;

    public int remove(){
        if(first==null) throw new NoSuchElementException();
        int data = first.data;
        first = first.next;
        if(first==null){
            last = null;
        }
        return data;
    }
    public void add(int data){
        QueueNode newNode = new QueueNode(data);
        if(last!=null){
            last.next = newNode;
        }
        last = newNode;
        if(first==null){
            first= newNode;
        }
    }
    public int peek(){
        if(first==null) throw new NoSuchElementException();
        return first.data;
    }
    public boolean isEmpty(){
        return first==null;
    }
}

abstract class Animal {
    private int order;
    protected String name;
    public Animal(String n){
        this.name = n;
    }
    public void setOrder(int order){
        this.order =order;
    }
    public int getOrder(){
        return this.order;
    }
    public boolean isOrderThan(Animal a){
        return this.order < a.getOrder();
    }
}

class Dog extends Animal{
    public Dog(String n) {
        super(n);
    }
}

class Cat extends Animal{
    public Cat(String n){
        super(n);
    }
}
class AnimalQueue{
    LinkedList<Dog> dogs = new LinkedList<>();
    LinkedList<Cat> cats = new LinkedList<>();
    private int order= 0;
    public void enqueue(Animal a){
        a.setOrder(order++);
        if(a instanceof Dog) dogs.add((Dog) a);
        else cats.add((Cat) a);
    }

    public Animal dequeueAny(){
        if(dogs.size()==0) return dequeueCat();
        if(cats.size()==0) return dequeueDog();
        Dog dog = dogs.peek();
        Cat cat = cats.peek();
        if(dog.isOrderThan(cat)) return dequeueDog();
        else return dequeueCat();
    }
    public Dog dequeueDog(){
        return dogs.poll();
    }
    public Cat dequeueCat(){
        return cats.poll();
    }
}


class QueueMain {
    public static void main(String[] args) {
        MyQueue myQueue = new MyQueue();
        myQueue.add(1);
        System.out.println(myQueue.isEmpty());
        System.out.println(myQueue.peek());
        myQueue.add(2);
        myQueue.remove();
        System.out.println(myQueue.peek());
    }
}