package com.middleware.metamenth.interfaces.measureinstruments;

import java.util.List;

public interface IWeatherStation {
    String getUID();
    String getName();
    void setName(String name);
    String getLocation();
    void setLocation(String location);
    void addWeatherData(List<IWeatherData> weatherData);
    List<IWeatherData> getWeatherData();
    String toString();
    
}
