package com.middleware.metamenth.interfaces.structure;

import com.middleware.metamenth.interfaces.datatypes.IBinaryMeasure;

public interface IMaterial {
    String getUID();
    String getDescription();
    void setDescription(String description);
    String getMaterialType();
    void setMaterialType(String materialType);
    IBinaryMeasure getDensity();
    void setDensity(IBinaryMeasure density);
    IBinaryMeasure getHeatCapacity();
    void setHeatCapacity(IBinaryMeasure heatCapacity);
    IBinaryMeasure getThermalTransmittance();
    void setThermalTransmittance(IBinaryMeasure thermalTransmittance);
    IBinaryMeasure getThermalResistance();
    void setThermalResistance(IBinaryMeasure thermalResistance);
    IBinaryMeasure getSolarHeatGainCoefficient();
    void setSolarHeatGainCoefficient(IBinaryMeasure solarHeatGainCoefficient);
    String toString();

}
