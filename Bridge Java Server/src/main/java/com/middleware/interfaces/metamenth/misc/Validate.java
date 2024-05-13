package com.middleware.interfaces.metamenth.misc;

public interface Validate {
    String validateWhat3word(String inputString);
    float validateSolarHeatGainCoefficient(float solarHeatGainCoefficient);
    String parseDate(String dateString);
    boolean validateSensorType(String sensorMeasor, String unit);
}
