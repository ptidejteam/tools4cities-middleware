package com.middleware.metamenth;

import java.util.HashMap;
import java.util.Map;
import com.middleware.metamenth.interfaces.structure.IFloor;
import com.middleware.metamenth.interfaces.structure.IOpenSpace;
import com.middleware.metamenth.interfaces.structure.IRoom;
import com.middleware.metamenth.interfaces.transducers.ISensor;
import com.middleware.metamenth.enums.FloorType;
import com.middleware.metamenth.enums.RoomType;
import com.middleware.metamenth.interfaces.datatypes.IAddress;
import com.middleware.metamenth.interfaces.datatypes.IBinaryMeasure;
import com.middleware.metamenth.interfaces.structure.IBuilding;

public class MetamenthRepository {

    private Map<String, Object> repository = new HashMap<String, Object>();


    public void addEntity(IBuilding element) {
        repository.put("building", element);
    }

    public void addEntity(IFloor element) {
        repository.put("floor", element);
    }

    public void addEntity(IRoom element) {
        repository.put("room", element);
    }

    public void addEntity(IOpenSpace element) {
        repository.put("open_space", element);
    }

    public void addEntity(IBinaryMeasure element) {
        repository.put("binary_measure", element);
    }

    public void addEntity(IAddress element) {
        repository.put("address", element);
    }

    public void addEntity(ISensor element) {
        repository.put("sensor", element);
    }

    public Object getEntity(String key){
        return repository.getOrDefault(key, null);
    }

    


    public IBuilding getBuilding() {
        //This is just an example Use middle ware to populate building
        IRoom newRoom = (IRoom) repository.get("room");
        newRoom.setRoomType(RoomType.KITCHEN.getValue());
        newRoom.setName("Kitchen 102");
        IBinaryMeasure roomArea = (IBinaryMeasure) newRoom.getArea();
        roomArea.setValue(30);
        newRoom.addTransducer((ISensor) repository.get("sensor"));

        IFloor newFloor = (IFloor) repository.get("floor");
        newFloor.setNumber(1);
        newFloor.setFloorType(FloorType.BASEMENT.getValue());
        newFloor.addRoom(newRoom);

        IBuilding building = (IBuilding) repository.get("building");
        building.setConstructionYear(2025);
        building.addFloor(newFloor);
        return building;
    }
}
