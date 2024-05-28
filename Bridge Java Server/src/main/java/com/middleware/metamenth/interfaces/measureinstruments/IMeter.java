package com.middleware.metamenth.interfaces.measureinstruments;

import com.middleware.metamenth.enums.MeasurementUnit;
import com.middleware.metamenth.enums.MeterMeasureMode;
import com.middleware.metamenth.enums.MeterType;

public interface IMeter {
    String toString();
    String getMeterLocation();
    void setMeterLocation(String location);
    String getUID();
    String getManufacturer();
    void setManufacturer(String manufacturer);
    float getMeasurementFrequency();
    void setMeasurementFrequency(float measurementFrequency);
    MeterMeasureMode getMeasureMode();
    void setMeasureMode(MeterMeasureMode meterMeasureMode);
    boolean getDataAccumulated();
    void setDataAccumulated(boolean dataAccummulated);
    float getAccumulationFrequency();
    void setAccumulationFrequency(float accummulationFrequency);
    MeasurementUnit getMeasurementUnit();
    void setMeasurementUnit(MeasurementUnit measurementUnit);
    MeterType getMeterType();
    void setMeterType(MeterType meterType);
}