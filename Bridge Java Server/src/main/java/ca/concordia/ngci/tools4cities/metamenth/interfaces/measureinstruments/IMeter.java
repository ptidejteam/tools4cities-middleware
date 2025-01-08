package ca.concordia.ngci.tools4cities.metamenth.interfaces.measureinstruments;

import java.util.HashMap;
import java.util.List;

import ca.concordia.ngci.tools4cities.metamenth.enums.MeasurementUnit;
import ca.concordia.ngci.tools4cities.metamenth.enums.MeterMeasureMode;
import ca.concordia.ngci.tools4cities.metamenth.enums.MeterType;

public interface IMeter {
    String toString();
    String getMeterLocation();
    void setMeterLocation(String location);
    String getUID();
    String getManufacturer();
    void setManufacturer(String manufacturer);
    double getMeasurementFrequency();
    void setMeasurementFrequency(double measurementFrequency);
    MeterMeasureMode getMeasureMode();
    void setMeasureMode(MeterMeasureMode meterMeasureMode);
    boolean getDataAccumulated();
    void setDataAccumulated(boolean dataAccummulated);
    String getAccumulationFrequency();
    void setAccumulationFrequency(String accummulationFrequency);
    String getMeasurementUnit();
    void setMeasurementUnit(String measurementUnit);
    String getMeterType();
    void setMeterType(MeterType meterType);
    void addMeterMeasure(IMeterMeasure measure);
    List<IMeterMeasure> getMeterMeasureByDate(String fromDateStr, String toDateStr);
    List<IMeterMeasure> getMeterMeasures(HashMap<String, Object> searchTerms);
}
