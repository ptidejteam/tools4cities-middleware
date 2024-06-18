package ca.concordia.ngci.tools4cities.metamenth.interfaces.transducers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IAbstractMeasure;

public interface IAbstractTransducer {
    String getUID();
    String getName();
    void setName(String name);
    String getRegistryId();
    void setRegistryId(String registryId);
    IAbstractMeasure getSetPoint();
    void setTransducerSetPoint(IAbstractMeasure setpoint, String measure);
    Map<String, Object> getMetaData();
    void addData(List<Object> data);
    boolean removeData(Object data);
    void addMetaData(String key, Object value);
    boolean removeMetaData(String key);
    String toString();
    List<Object> getData(HashMap<String, Object> searchTerms);
    List<Object> getDataByDate(String fromDateStr, String toDateStr);
    boolean equals(IAbstractTransducer obj);
}
