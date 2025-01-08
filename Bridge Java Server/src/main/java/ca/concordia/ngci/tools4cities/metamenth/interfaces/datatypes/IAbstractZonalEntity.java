package ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes;

import java.util.List;

public interface IAbstractZonalEntity {
    List<IZone> getZones();
    Boolean addZone(IZone zone);
    Boolean removeZone(IZone zone);
    IZone getZoneByName(String name);
}
