package com.middleware;

import java.util.HashMap;
import java.util.Map;
import com.middleware.interfaces.metamenth.structure.IFloor;
import com.middleware.interfaces.metamenth.structure.IOpenSpace;
import com.middleware.interfaces.metamenth.structure.IRoom;

public class MetamenthRepository {

    private Map<String, Object> repository = new HashMap<String, Object>();


    public void addEntity(IRoom element) {
        System.out.println(element);
        repository.put("room", element);
    }

    public void addEntity(IOpenSpace element) {
        System.out.println(element);
        repository.put("open_space", element);
    }

    public void addEntity(IFloor element) {
        System.out.println(element);
        repository.put("floor", element);
    }


    public Object getBuilding() {
        //does majic to provide a building instance, perhaps the varennes library building :)
        return null;
    }
}
