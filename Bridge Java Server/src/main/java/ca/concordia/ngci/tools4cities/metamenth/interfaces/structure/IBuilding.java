package ca.concordia.ngci.tools4cities.metamenth.interfaces.structure;

import java.util.List;
import java.util.Map;

import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IAbstractMeasure;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IAddress;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IZone;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.measureinstruments.IMeter;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.measureinstruments.IWeatherStation;

public interface IBuilding {
    String getUID();
    int getConstructionYear();
    void setConstructionYear(int year);
    IAbstractMeasure getHeight();
    void setHeight(IAbstractMeasure height);
    IAbstractMeasure getFloorArea();
    void setFloorArea(IAbstractMeasure floorArea);
    IAbstractMeasure getInternalMass();
    void setInternalMass(IAbstractMeasure internalMass);
    IAddress getAddress();
    void setAddress(IAddress address);
    String getBuildingType();
    void setBuildingType(String buildingType);
    void addFloor(IFloor floor);
    void addMeter(IMeter meter);
    IBuilding addWeatherStation(IWeatherStation weatherStation);
    IWeatherStation getWeatherStation(String name);
    Boolean removeWeatherStation(IWeatherStation weatherStation);
    IEnvelope getEnvelope();
    void setEnvelope(IEnvelope envelope);
    IZone getZones();
    IFloor getFloorById(String id);
    IFloor getFloorByNumber(double number);
    List<IFloor> getFloors(Map<String, Object> searchTerms);
    IMeter getMeterById(String id);
    List<IMeter> getMeterByType(String meterType);
    List<IMeter> getMeters(Map<String, Object> searchTerms);
    String toString();

}
