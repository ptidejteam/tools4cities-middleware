package ca.concordia.ngci.tools4cities.metamenth.interfaces.measureinstruments;

import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IAbstractMeasure;

public interface IWeatherData {

    String getUID();
    IAbstractMeasure getData();
    void setData(IAbstractMeasure data);
    String getTimeStamp();
    String toString();
    
}
