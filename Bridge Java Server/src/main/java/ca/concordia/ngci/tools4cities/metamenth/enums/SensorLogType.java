package ca.concordia.ngci.tools4cities.metamenth.enums;

public enum SensorLogType {
    POLLING("Polling"),
    CHANGE_OF_VALUE("ChangeOfValue");

    private final String value;

    SensorLogType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
