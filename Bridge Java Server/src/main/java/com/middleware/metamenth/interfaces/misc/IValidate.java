package com.middleware.metamenth.interfaces.misc;

public interface IValidate {
    String validateWhat3word(String inputString);
    float validateSolarHeatGainCoefficient(float solarHeatGainCoefficient);
    String parseDate(String dateString);
    boolean validateSensorType(String sensorMeasor, String unit);
}
