package com.middleware.metamenth.interfaces.datatypes;

public interface IZone {

    String getUID();
    String getDescription();
    void setDescription(String description);
    String getName();
    void setName(String name);
    String getZoneType();
    void setZoneType(String zoneType);
    String getHvacType();
    void setHvacType(String hvacType);
    String toString();

}
