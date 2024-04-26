package com.middleware;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.middleware.interfaces.IMammal;
import com.middleware.interfaces.IDog;
import com.middleware.interfaces.IWhale;

public class Stack {
    private List<Object> internalList = new LinkedList<Object>();

    public void push(int element) {
        System.out.println("Integer Overloaded method called......");
        internalList.add(element);
    }

    public void push(String element) {
        System.out.println("String Overloaded method called......");
        internalList.add(element);
    }

    public void push(Map<Object, Object> element) {
        System.out.println("Hashmap Overloaded method called......");
        internalList.add(element);
    }

    public void push(List<Object> element) {
        System.out.println("List Overloaded method called......");
        internalList.add(element);
    }

    public void push(IDog element) {
        System.out.println("IDog Overloaded method called......");
        element.bark();
        processMammal(element);
    }

    public void push(IWhale element) {
        System.out.println("IWhale Overloaded method called......");
        element.swim();
        System.out.println(element.getSpecies());
        System.out.println(element.getFirstChild());
        System.out.println(element.getChildren());
        processMammal(element);
    }

    public void processMammal(IMammal element){
        element.eat("bones");
        element.sleep();
        element.reproduce();
        internalList.add(element);
    }

    public void push(IMammal element) {
        //Never called. More specific (lower in hierarchy objects considered)
        System.out.println("IMammal Overloaded method called......");
        element.eat("bones");
        element.sleep();
        element.reproduce();
        internalList.add(element);
    }

    public void push(Object element) {
        internalList.add(element);
    }

    public void pushMultiple(int number){
        for (int i = 0; i <= number; i++){
            internalList.add(i);
        }
    }

    public Object pop() {
        return internalList.remove(0);
    }

    public List<Object> getInternalList() {
        return internalList.stream().collect(Collectors.toList());
    }


    public void pushAll(List<Object> elements) {
        for (Object element : elements) {
            this.push(element);
        }
    }
}
