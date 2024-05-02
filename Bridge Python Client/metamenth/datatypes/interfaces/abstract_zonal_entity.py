from abc import ABC
from uuid import uuid4
from utils import EntityInsert
from utils import EntityRemover
from utils import StructureEntitySearch
from typing import Dict
from enumerations import BuildingEntity


class AbstractZonalEntity(ABC):

    def __init__(self):
        """
        """
        self._UID = str(uuid4())
        self._zones = []

    @property
    def UID(self) -> str:
        return self._UID

    @property
    def zones(self):
        return self._zones

    @zones.setter
    def zones(self, value):
        if value is not None:
            self._zones = value
        else:
            raise ValueError('zones must be of type [Zone]')

    def add_zone(self, zone, building):
        """
        Adds a zone to this floor
        :param zone: the zone
        :param building, the building which spaces requires a zone
        :return:
        """
        EntityInsert.insert_zone(self, zone, building)

    def remove_zone(self, zone):
        """
        Removes a zone from a space: floor, room, open space
        :param zone: the zone to be removed
        :return:
        """
        EntityRemover.remove_building_entity(self._zones, zone, BuildingEntity.ZONE.value, self)

    def get_zone_by_name(self, name):
        """
        Search zones by name
        :param name:  the name of the zone
        :return:
        """
        return StructureEntitySearch.search_by_name(self._zones, name)

    def get_zone_by_uid(self, uid):
        """
        Search zones by uid
        :param uid: the unique identifier of the overlapping zone
        :return:
        """
        return StructureEntitySearch.search_by_id(self._zones, uid)

    def get_zones(self, search_terms: Dict = None):
        """
        Search zones by attributes values
        :param search_terms: a dictionary of attributes and their values
        :return:
        """
        return StructureEntitySearch.search(self._zones, search_terms)