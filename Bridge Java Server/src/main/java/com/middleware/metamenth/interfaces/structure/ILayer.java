package com.middleware.metamenth.interfaces.structure;

import com.middleware.metamenth.interfaces.datatypes.IBinaryMeasure;

public interface ILayer {
    String getUID();
    IBinaryMeasure getHeight();
    void setHeight(IBinaryMeasure height);
    IBinaryMeasure getLength();
    void setLength(IBinaryMeasure length);
    IBinaryMeasure getThickness();
    void setThickness(IBinaryMeasure thickness);
    IMaterial getMaterial();
    void setMaterial(IMaterial material);
    Boolean getHasVapourBarrier();
    void setHasVapourBarrier(Boolean hasVapourBarrier);
    Boolean getHasAirBarrier();
    void setHasAirBarrier(Boolean hasAirBarrier);
    String toString();
}
