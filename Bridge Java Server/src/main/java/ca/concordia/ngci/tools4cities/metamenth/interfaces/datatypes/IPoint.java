package ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes;

public interface IPoint {
    void setLatitude(float latitude);
    float getLatitude();
    void setLongitude(float longitude);
    float getLongitude();
    String toString();
}
