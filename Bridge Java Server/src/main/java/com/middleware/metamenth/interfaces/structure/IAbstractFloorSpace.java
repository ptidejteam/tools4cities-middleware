package com.middleware.metamenth.interfaces.structure;

import com.middleware.metamenth.interfaces.measureinstruments.IMeter;
import com.middleware.metamenth.interfaces.transducers.IAbstractTransducer;

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
