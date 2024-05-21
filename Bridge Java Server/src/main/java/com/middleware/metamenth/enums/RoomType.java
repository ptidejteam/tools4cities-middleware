package com.middleware.metamenth.enums;

public enum RoomType {
    BEDROOM("Bedroom"),
    LIVING_ROOM("Living Room"),
    KITCHEN("Kitchen"),
    BATHROOM("Bathroom"),
    OFFICE("Office"),
    STUDY_ROOM("Study Room");

    private final String value;

    RoomType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}