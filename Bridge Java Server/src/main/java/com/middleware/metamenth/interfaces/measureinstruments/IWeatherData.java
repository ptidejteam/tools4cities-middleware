package com.middleware.metamenth.interfaces.measureinstruments;

import com.middleware.metamenth.interfaces.datatypes.IAbstractMeasure;

public interface IWeatherData {

    String getUID();
    IAbstractMeasure getData();
    void setData(IAbstractMeasure data);
    String getTimestamp();
    String toString();
    
}
