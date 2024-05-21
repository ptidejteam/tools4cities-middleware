package com.middleware.metamenth.enums;

public enum CoverType {
    ROOF("Roof"),
    WALL("Wall"),
    FLOOR("Floor"),
    WINDOW("Window"),
    DOOR("Door");

    private final String coverType;

    CoverType(String type) {
        this.coverType = type;
    }

    public String getValue() {
        return coverType;
    }
}
