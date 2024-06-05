package ca.concordia.ngci.tools4cities.metamenth;

import java.util.HashMap;
import java.util.Map;

import ca.concordia.ngci.tools4cities.metamenth.enums.FloorType;
import ca.concordia.ngci.tools4cities.metamenth.enums.RoomType;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IAddress;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IBinaryMeasure;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.structure.IBuilding;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.structure.IFloor;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.structure.IOpenSpace;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.structure.IRoom;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.transducers.ISensor;

public class MetamenthRepository {

    private Map<String, Object> repository = new HashMap<String, Object>();


    public void addEntity(String key, Object entity){
        repository.putIfAbsent(key, entity);
    }
    
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
