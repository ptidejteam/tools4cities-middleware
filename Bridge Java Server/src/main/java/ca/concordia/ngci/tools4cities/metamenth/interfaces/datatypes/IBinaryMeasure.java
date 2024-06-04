package ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes;

public interface IBinaryMeasure extends IAbstractMeasure{
    void setValue(float value);
    float getValue();
    String toString();
}
