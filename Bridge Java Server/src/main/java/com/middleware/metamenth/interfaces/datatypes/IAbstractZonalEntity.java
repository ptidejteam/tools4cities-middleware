package com.middleware.metamenth.interfaces.datatypes;

import java.util.List;

public interface IAbstractZonalEntity {
    List<IZone> getZones();
    void addZone(IZone zone);
    void removeZone(IZone zone);
    IZone getZoneByName(String name);
}
