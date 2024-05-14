package com.middleware.interfaces.metamenth.transducers;

import com.middleware.interfaces.metamenth.datatypes.IAbstractMeasure;
import com.middleware.interfaces.metamenth.datatypes.IAbstractRangeMeasure;

public interface ISensor extends IAbstractTransducer {
    String toString();
    String getMeasure();
    void setMeasure(String measure);
    IAbstractRangeMeasure getMeasureRange();
    void setMeasureRange(IAbstractRangeMeasure measureRange);
    float getDataFrequency();
    void setDataFrequency(float dataFrequency);
    String getUnit();
    void setUnit(String unit);
    float getCurrentValue();
    void setCurrentValue(float currentValue);
    String getMeasureType();
    void setMeasureType(String measureType);
    String getSensorLogType();
    void setSensorLogType(String sensorLogType);
    void setSetPoint(IAbstractMeasure setPoint);
}
