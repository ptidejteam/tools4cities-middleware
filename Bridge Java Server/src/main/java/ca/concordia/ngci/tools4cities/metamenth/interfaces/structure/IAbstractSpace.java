package ca.concordia.ngci.tools4cities.metamenth.interfaces.structure;

import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IAbstractMeasure;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IAbstractZonalEntity;

public interface IAbstractSpace extends IAbstractZonalEntity {
    String toString();
    String getUID();
    IAbstractMeasure getArea();
    void setArea(IAbstractMeasure abstractMeasure);
    String getLocation();
    void setLocation(String location);
}
