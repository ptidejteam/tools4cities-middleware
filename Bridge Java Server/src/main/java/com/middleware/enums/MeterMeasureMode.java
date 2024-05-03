package com.middleware.enums;

public enum MeterMeasureMode {
    MANUAL("Manual"),
    AUTOMATIC("Automatic");

    private final String value;

    MeterMeasureMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}