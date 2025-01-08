from metamenth.structure.interfaces.abstract_space import AbstractSpace
from metamenth.datatypes.interfaces.abstract_measure import AbstractMeasure
from metamenth.structure.open_space import OpenSpace
from metamenth.structure.room import Room
from typing import List, Dict
from metamenth.utils.search.structure_entity_search import StructureEntitySearch
from py4j.java_gateway import JavaGateway


class Floor(AbstractSpace):
    """
    A floor on a building

    Author: Peter Yefi
    Email: peteryefi@gmail.com
    """

    def __init__(
        self,
        area: AbstractMeasure,
        number,
        floor_type: str,
        height: AbstractMeasure = None,
        description: str = None,
        open_space: OpenSpace = None,
        room: Room = None,
        location: str = None
    ):
        """
        :param area (BinaryMeasure): The area of the floor.
        :param location: The location of the floor (three words terminated with a period).
        :param description: A description of the floor.
        :param number: The floor number.
        :param floor_type: The type of floor (enum).
        :param open_space: Initial open spaces(s) on floor.
        :param room: Initial room(s) on floor.
        """
        super().__init__(area, location)
        gateway = JavaGateway()
        self._description = description
        self._height = height
        self._number = None
        self._floor_type = None
        self._open_spaces: List['OpenSpace'] = []
        self._rooms: List['Room'] = []
        self._structure_entity_search = StructureEntitySearch()

        # apply validation
        self.setNumber(number)
        self.setFloorType(floor_type)

        if open_space:
            self.addOpenSpace(open_space)

        if room:
            self.addRoom(room)

        # A floor should have at least one open space or one room
        if not self._open_spaces and not self._rooms:
            raise ValueError("A floor must have at least one room or one open space.")

    def getDescription(self) -> str:
        return self._description

    def setDescription(self, value: str):
        self._description = value

    def getNumber(self):
        return self._number

    def setNumber(self, value):
        if value is not None:
            self._number = value
        else:
            raise ValueError("number must be an int")

    def getFloorType(self) -> str:
        return self._floor_type

    def setFloorType(self, value: str):
        if value is not None:
            self._floor_type = value
        else:
            raise ValueError("floor_type must be of type FloorType")

    def getHeight(self) -> AbstractMeasure:
        return self._height

    def setHeight(self, value: AbstractMeasure):
        if value is not None:
            self._height = value
        else:
            raise ValueError("height must be of type AbstractMeasure")

    def addOpenSpace(self, value: OpenSpace):
        if value is not None:
            self._open_spaces.append(value)
        else:
            raise ValueError("open_spaces must be of type [OpenSpace]")

    def addRoom(self, value: Room):
        if value is not None:
            self._rooms.append(value)
        else:
            raise ValueError("rooms must be of type [Room]")

    def getOpenSpaceById(self, uid: str) -> OpenSpace:
        """
        Retrieves an open space given the uid
        :param uid: the uid of the open space
        :return:
        """
        return self._structure_entity_search.searchByUid(self._open_spaces, uid)

    def getRoomById(self, uid: str) -> Room:
        """
        Retrieves a room given the uid
        :param uid: the uid of the room
        :return:
        """
        return self._structure_entity_search.searchByUid(self._rooms, uid)

    def getOpenSpaceByName(self, name: str) -> OpenSpace:
        """
        Retrieves an open space given the name
        :param name: the name of the open space
        :return:
        """
        return self._structure_entity_search.searchByName(self._open_spaces, name)

    def getRoomByName(self, name) -> Room:
        """
        Retrieves a room given the name
        :param name: the name of the room
        :return:
        """
        return self._structure_entity_search.searchByName(self._rooms, name)

    def getRooms(self, search_term: Dict = None) -> List[Room]:
        """
        Retrieves rooms that match attributes and their values
        :param search_term: attributes and their values
        :return:
        """
        return self._structure_entity_search.search(self._rooms, search_term)

    def getOpenSpaces(self, search_term: Dict = None) -> List[OpenSpace]:
        """
        Retrieves open spaces that match attributes and their values
        :param search_term: attributes and their values
        :return:
        """
        return self._structure_entity_search.search(self._open_spaces, search_term)

    def toString(self):
        floor_details = (f"Floor {super().__str__()} {self.getNumber()} ({self.getFloorType()}): {self.getDescription()},"
                         f"Area: {self.getArea()}, Height: {self.getHeight()}, Location: {self.getLocation()}, "
                         f"UID: {self.getUID()}, "
                         f"Rooms Count: {len(self._rooms)}, Open Spaces Count: {len(self._open_spaces)})")

        rooms = "\n".join(str(room) for room in self._rooms)
        open_spaces = "\n".join(str(space) for space in self._open_spaces)

        return f"{floor_details}\nRooms:\n{rooms}\nOpen Space:\n {open_spaces})"

    class Java:
        implements = ['ca.concordia.ngci.tools4cities.metamenth.interfaces.structure.IFloor']
