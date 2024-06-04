package ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes;


public interface IAddress {
    void setCity(String city);
    String getCity();
    void setStreet(String street);
    String getStreet();
    void setZipCode(String zipCode);
    String getZipCode();
    void setState(String state);
    String getState();
    void setCountry(String country);
    String getCountry();
    void setGeocoordinates(IPoint geoCoordinates);
    IPoint getGeocoordinates();
    void setNorthOrientation(IBinaryMeasure northOrientation);
    IBinaryMeasure getNorthOrientation();
    String toString();

}
