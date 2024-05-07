package com.middleware.interfaces.metamenth.datatypes;

public interface IBinaryMeasure extends IAbstractMeasure{
    void setValue(float value);
    float getValue();
    String toString();
}
