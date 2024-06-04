package ca.concordia.ngci.tools4cities.metamenth.enums;

public enum ZoneType {
    HVAC("HVAC"),
    LIGHTING("Lighting");

    private final String value;

    ZoneType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
