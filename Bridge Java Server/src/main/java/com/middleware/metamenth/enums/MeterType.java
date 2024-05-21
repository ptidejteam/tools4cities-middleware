package com.middleware.metamenth.enums;

public enum MeterType {
    ELECTRICITY("Electricity"),
    CHARGE_DISCHARGE("ChargeDischarge"),
    POWER("Power"),
    FLOW("Flow"),
    HEAT("Heat"),
    GAS("Gas");

    private final String value;

    MeterType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
