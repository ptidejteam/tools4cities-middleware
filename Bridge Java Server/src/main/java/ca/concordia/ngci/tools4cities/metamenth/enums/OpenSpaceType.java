package ca.concordia.ngci.tools4cities.metamenth.enums;


public enum OpenSpaceType {
    PARKING_GARAGE("Parking Garage"),
    POOL_AREA("Pool Area"),
    OPEN_OFFICE("Open Office"),
    HALL("Hall"),
    CORRIDOR("Corridor"),
    STAIR_AREA("StairArea"),
    DINNING_AREA("DinningArea"),
    ESCALATOR_AREA("EscalatorArea"),
    RECEPTION_AREA("ReceptionArea"),
    TODDLERS_AREA("ToddlersArea"),
    OPEN_AREA("OpenArea"),
    SITTING_AREA("SittingArea"),
    OTHER("Other");

    private final String value;

    OpenSpaceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
