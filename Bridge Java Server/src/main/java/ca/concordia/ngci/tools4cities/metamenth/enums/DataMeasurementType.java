package ca.concordia.ngci.tools4cities.metamenth.enums;

public enum DataMeasurementType {
    OUTSIDE_TEMPERATURE("OutsideTemperature"),
    HUMIDITY("Humidity"),
    RELATIVE_HUMIDITY("RelativeHumidity"),
    PRESSURE("Pressure"),
    PRECIPITATION("Precipitation"),
    WIND_SPEED("WindSpeed"),
    WIND_DIRECTION("WindDirection"),
    MOISTURE_CONTENT("MoistureContent"),
    SOLAR_RADIATION("SolarRadiation"),
    GLOBAL_NOMINAL_IRRADIANCE("GlobalNominalIrradiance"),
    DIFFUSE_HORIZONTAL_IRRADIANCE("DiffuseHorizontalIrradiance"),
    DIRECT_NOMINAL_IRRADIANCE("DirectNominalIrradiance"),
    GLOBAL_HORIZONTAL_IRRADIANCE("GlobalHorizontalIrradiance"),
    EXPORTED_ELECTRICITY("ExportedElectricity"),
    IMPORTED_ELECTRICITY("ImportedElectricity"),
    CONSUMED_ELECTRICITY("ConsumedElectricity");

    private final String value;

    DataMeasurementType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
