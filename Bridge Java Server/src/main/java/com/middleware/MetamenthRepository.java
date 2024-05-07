package com.middleware;

import java.util.HashMap;
import java.util.Map;
import com.middleware.interfaces.metamenth.structure.IFloor;
import com.middleware.interfaces.metamenth.structure.IRoom;
import com.middleware.enums.FloorType;
import com.middleware.enums.RoomType;
import com.middleware.interfaces.metamenth.datatypes.IBinaryMeasure;
import com.middleware.interfaces.metamenth.structure.IBuilding;

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


    public IBuilding getBuilding() {
        //This is just an example Use middle ware to populate building
        IRoom newRoom = (IRoom) repository.get("room");
        newRoom.setRoomType(RoomType.KITCHEN.getValue());
        newRoom.setName("Kitchen 102");
        IBinaryMeasure roomArea = (IBinaryMeasure) newRoom.getArea();
        roomArea.setValue(30);

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
