package com.middleware.interfaces.metamenth.structure;

import com.middleware.interfaces.metamenth.measureinstruments.IMeter;

public interface IAbstractFloorSpace extends IAbstractSpace {
    String toString();
    String getName();
    void setName(String name);
    IMeter getMeter();
    void setMeter(IMeter meter);
}
