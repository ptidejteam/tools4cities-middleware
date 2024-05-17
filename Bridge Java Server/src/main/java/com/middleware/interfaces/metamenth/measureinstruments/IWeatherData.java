package com.middleware.interfaces.metamenth.measureinstruments;

import com.middleware.interfaces.metamenth.datatypes.IAbstractMeasure;

public interface IWeatherData {

    String getUID();
    IAbstractMeasure getData();
    void setData(IAbstractMeasure data);
    String getTimestamp();
    String toString();
    
}
