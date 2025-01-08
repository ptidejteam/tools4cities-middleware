package ca.concordia.ngci.tools4cities.metamenth.enums;

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