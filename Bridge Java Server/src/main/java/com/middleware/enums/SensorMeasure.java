package com.middleware.enums;

public enum SensorMeasure {
    TEMPERATURE("Temperature"),
    PRESSURE("Pressure"),
    GAS_VELOCITY("GasVelocity"),
    LIQUID_VELOCITY("LiquidVelocity"),
    LUMINANCE("Luminance"),
    CARBON_DIOXIDE("CarbonDioxide"),
    NOISE("Noise"),
    CURRENT("Current"),
    VOLTAGE("Voltage"),
    GLOBAL_RADIATION("GlobalRadiation"),
    DIRECT_RADIATION("DirectRadiation"),
    SMOKE("Smoke"),
    OCCUPANCY("Occupancy"),
    DAYLIGHT("Daylight"),
    AIR_VOLUME("AirVolume"),
    HUMIDITY("Humidity"),
    OTHER("Other");

    private final String value;

    SensorMeasure(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
