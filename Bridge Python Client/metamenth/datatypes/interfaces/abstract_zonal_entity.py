from abc import ABC


class AbstractZonalEntity(ABC):

    def __init__(self, gateway):
        """
        """
        self._zones = gateway.jvm.java.util.ArrayList()

    def getZones(self):
        return self._zones

    def addZone(self, zone) -> bool:
        """
        Adds a zone to this entity
        :param zone: the zone
        :return:
        """
        # TODO: Add zone to building
        if not self._zones.contains(zone):
            self._zones.add(zone)
            return True
        return False

    def removeZone(self, zone) -> bool:
        """
        Removes a zone from a space: floor, room, open space
        :param zone: the zone to be removed
        :return:
        """

        rm_index = -1
        for i in range(len(self._zones)):
            if self._zones[i].getName() == zone.getName():
                rm_index = i
        if rm_index > -1:
            del self._zones[rm_index]
            return True
        return False

    def getZoneByName(self, name: str):
        """
        Search zones by name
        :param name:  the name of the zone
        :return:
        """
        for zone in self._zones:
            if zone.getName() == name:
                return zone

    class Java:
        implements = ['com.middleware.metamenth.interfaces.datatypes.IAbstractZonalEntity']



