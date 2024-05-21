package com.middleware.metamenth.enums;

public enum MeterAccumulationFrequency {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    QUARTERLY("Quarterly"),
    YEARLY("Yearly"),
    NONE(null);

    private final String value;

    MeterAccumulationFrequency(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
