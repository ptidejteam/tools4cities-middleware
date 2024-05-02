package com.middleware.enums;

public enum RoomType {
    BEDROOM("Bedroom"),
    LIVING_ROOM("Living Room"),
    KITCHEN("Kitchen"),
    BATHROOM("Bathroom");

    private final String value;

    RoomType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}