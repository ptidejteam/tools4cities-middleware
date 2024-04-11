package com.middleware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.middleware.interfaces.IMammal;
import com.middleware.interfaces.IDog;
import com.middleware.interfaces.IWhale;

public class Stack {
    private List<Object> internalList = new LinkedList<Object>();

    public void push(Object element) {

        
        if (element instanceof ArrayList){
            List list = (ArrayList) element;
            System.out.println(list.get(0));
        }
        
        if (element instanceof HashMap) {
            Map map = (HashMap) element;
            System.out.println(map.get("age"));
        }

        if (element instanceof IMammal){
            
            IMammal mammal =  (IMammal) element;
           
            mammal.eat("bones");
            mammal.sleep();
            mammal.reproduce();

            if (element instanceof IDog){
                IDog dog =  (IDog) element;
                dog.bark();
            } else if(element instanceof IWhale){
                IWhale whale = (IWhale) element;
                whale.swim();
                System.out.println(whale.getSpecies());
                System.out.println(whale.getFirstChild());
                //Calling whale.getChildren() results in an error. This is being investigated
                //whale.getChildren();
            }
           
        }
        internalList.add(0, element);
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