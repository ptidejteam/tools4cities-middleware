package com.middleware.interfaces.metamenth.structure;

import com.middleware.interfaces.metamenth.measureinstruments.IMeter;
import com.middleware.interfaces.metamenth.transducers.IAbstractTransducer;

public interface IAbstractFloorSpace extends IAbstractSpace {
    String toString();
    String getName();
    void setName(String name);
    IMeter getMeter();
    void setMeter(IMeter meter);
    boolean addTransducer(IAbstractTransducer transducer);
    boolean removeTransducer(IAbstractTransducer transducer);
    IAbstractTransducer getTransducer(String name);
}
