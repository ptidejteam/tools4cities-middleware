package ca.concordia.ngci.tools4cities.metamenth.interfaces.structure;

import ca.concordia.ngci.tools4cities.metamenth.interfaces.measureinstruments.IMeter;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.transducers.IAbstractTransducer;

public interface IAbstractFloorSpace extends IAbstractSpace {
    String toString();
    String getName();
    void setName(String name);
    IMeter getMeter();
    void setMeter(IMeter meter);
    boolean addTransducer(IAbstractTransducer transducer);
    boolean removeTransducer(IAbstractTransducer transducer);
    IAbstractTransducer getTransducer(String name);
    boolean equals(Object obj);
}
