package ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes;

import ca.concordia.ngci.tools4cities.metamenth.enums.MeasurementUnit;

public interface IAbstractMeasure {
    String toString();
    String getMeasurementUnit();
    void setMeasurementUnit(MeasurementUnit measurementUnit);
}
