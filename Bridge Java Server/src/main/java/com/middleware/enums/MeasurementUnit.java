package com.middleware.enums;

public enum MeasurementUnit {
    DEGREE_CELSIUS("°C"),
    PASCAL("Pa"),
    METERS_PER_SECOND("m/s"),
    R_VALUE("R-Value"),
    KILOWATTS("kW"),
    KILOWATTS_PER_HOUR("kWh"),
    DEGREES("Deg"),
    PERCENTAGE("Percentage"),
    WATTS_PER_METER_SQUARE("W/m2"),
    PRESENCE("Presence"),
    GRAM_PER_KILOGRAM("g/kg"),
    GRAM_PER_CUBIC_METER("g/m3"),
    LITERS_PER_SECOND("l/s"),
    CUBIC_METERS_PER_HOUR("m3/h"),
    CUBIC_FEET_PER_MINUTE("ft3/min"),
    MILLI_AMPERE("mA"),
    AMPERE("A"),
    VOLT("V"),
    WATTS("W"),
    KILOGRAM_PER_CUBIC_METER("kg/m3"),
    POUNDS_PER_CUBIC_FOOT("lb/ft3"),
    WATTS_PER_SQUARE_METER_PER_DEGREE_CELSIUS("w/(m2.°C)"),
    CALORIES_PER_GRAM_PER_DEGREE_CELSIUS("cal/g°C"),
    JOULES_PER_KILOGRAM_PER_DEGREE_CELSIUS("J/kg°C"),
    KILOWATTS_PER_SQUARE_METER("kW/m2"),
    DECIBELS("dB"),
    PARTS_PER_MILLION("ppm"),
    LUX("lx"),
    CUBIC_METER_PER_SECOND("m3/s"),
    GALLONS_PER_SECOND("gal/s"),
    JOULES_PER_KELVIN("J/K"),
    WATTS_PER_SQUARE_METER_KELVIN("W/(m2.K)"),
    SQUARE_METERS_KELVIN_PER_WATTS("m2.K/W"),
    METERS("m"),
    CENTIMETERS("cm"),
    SQUARE_METERS("m2"),
    KILOGRAMS("Kg"),
    CUBIC_METER("m3"),
    CUBIC_CENTIMETER("cm3"),
    LITER("l"),
    CUBIC_FEET("ft3"),
    FEET_PER_SECOND("ft/s"),
    CANDELA_PER_SQUARE_METER("cd/m2"),
    NITS("Nits (nt)"),
    MICROGRAM_PER_CUBIC_METER("µg/m3"),
    SQUARE_FEET("ft2"),
    RELATIVE_HUMIDITY("%RH"),
    REVOLUTIONS_PER_MINUTE("RMP");

    private final String value;

    MeasurementUnit(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
