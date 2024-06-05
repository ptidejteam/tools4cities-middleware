package ca.concordia.ngci.tools4cities.metamenth.interfaces.structure;

import java.util.List;
import java.util.Map;

import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IAbstractMeasure;

public interface IFloor extends IAbstractSpace {
    String toString();
    String getDescription();
    void setDescription(String description);
    Object getNumber();
    void setNumber(double number);
    String getFloorType();
    void setFloorType(String flootType);
    IAbstractMeasure getHeight();
    void setHeight(IAbstractMeasure height);
    void addOpenSpace(IOpenSpace openSpace);
    void addRoom(IRoom room);
    IOpenSpace getOpenSpaceById(String id);
    IOpenSpace getOpenSpaceByName(String name);
    List<IOpenSpace> getOpenSpaces(Map<String, Object> searchTerms);
    IRoom getRoomById(String id);
    IRoom getRoomByName(String name);
    List<IRoom> getRooms(Map<String, Object> searchTerms);

}
