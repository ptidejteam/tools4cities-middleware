from metamenth.structure.interfaces.abstract_space import AbstractSpace
from metamenth.datatypes.interfaces.abstract_measure import AbstractMeasure
from metamenth.structure.open_space import OpenSpace
from metamenth.structure.room import Room
from typing import List


class Floor(AbstractSpace):
    """
    A floor on a building

    Author: Peter Yefi
    Email: peteryefi@gmail.com
    """

    def __init__(
        self,
        area: AbstractMeasure,
        number: int,
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
        self._description = description
        self._height = height
        self._number = None
        self._floor_type = None
        self._open_spaces: List['OpenSpace'] = []
        self._rooms: List['Room'] = []

        # apply validation
        self.setNumber(number)
        self.setFloorType(floor_type)

        if open_space:
            self.add_open_space(open_space)

        if room:
            self.add_room(room)

        # A floor should have at least one open space or one room
        if not self._open_spaces and not self._rooms:
            raise ValueError("A floor must have at least one room or one open space.")

    def getDescription(self) -> str:
        return self._description

    def setDescription(self, value: str):
        self._description = value

    def getNumber(self) -> int:
        return self._number

    def setNumber(self, value: int):
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

    def add_open_space(self, value: OpenSpace):
        if value is not None:
            self._open_spaces.append(value)
        else:
            raise ValueError("open_spaces must be of type [OpenSpace]")

    def add_room(self, value: Room):
        if value is not None:
            self._rooms.append(value)
        else:
            raise ValueError("rooms must be of type [Room]")

    def toString(self):
        floor_details = (f"Floor {super().__str__()} {self.getNumber()} ({self.getFloorType()}): {self.getDescription()},"
                         f"Area: {self.getArea()}, Height: {self.getHeight()}, Location: {self.getLocation()}, "
                         f"UID: {self.getUID()}, "
                         f"Rooms Count: {len(self._rooms)}, Open Spaces Count: {len(self._open_spaces)})")

        rooms = "\n".join(str(room) for room in self._rooms)
        open_spaces = "\n".join(str(space) for space in self._open_spaces)

        return f"{floor_details}\nRooms:\n{rooms}\nOpen Space:\n {open_spaces})"

    class Java:
        implements = ['com.middleware.interfaces.metamenth.structure.IFloor']
