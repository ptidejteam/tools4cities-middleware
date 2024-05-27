package com.middleware.metamenth.enums;

public enum HvacType {
    PERIMETER("Perimeter"),
    INTERIOR("Interior"),
    NONE(null);

    private final String value;

    HvacType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
