package ca.concordia.ngci.tools4cities.metamenth.enums;

public enum BuildingType {
    COMMERCIAL("Commercial"),
    NON_COMMERCIAL("NonCommercial"),
    RESIDENTIAL("Residential");

    private final String value;

    BuildingType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

