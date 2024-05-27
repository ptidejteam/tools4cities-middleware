from abc import ABC


class AbstractZonalEntity(ABC):

    def __init__(self, gateway):
        """
        """
        self._zones = gateway.jvm.java.util.ArrayList()

    def getZones(self):
        return self._zones

    def addZone(self, zone):
        """
        Adds a zone to this entity
        :param zone: the zone
        :return:
        """
        # TODO: Add zone to building
        self._zones.add(zone)

    def removeZone(self, zone):
        """
        Removes a zone from a space: floor, room, open space
        :param zone: the zone to be removed
        :return:
        """
        for z in self._zones:
            if z.getName() == zone.getName():
                self._zones.remove(z)
                break

    def getZoneByName(self, name: str):
        """
        Search zones by name
        :param name:  the name of the zone
        :return:
        """
        for zone in self._zones:
            if zone.getName() == name:
                return zone



