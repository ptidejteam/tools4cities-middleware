from metamenth.datatypes.interfaces.abstract_measure import AbstractMeasure
from metamenth.structure.interfaces.abstract_floor_space import AbstractFloorSpace
from metamenth.enumerations.room_type import RoomType


class Room(AbstractFloorSpace):
    """
    Defines rooms on a floor of a building

    Author: Peter Yefi
    Email: peteryefi@gmail.com
    """

    def __init__(
        self,
        area: AbstractMeasure,
        name: str,
        room_type: RoomType,
        location: str = None
    ):
        """
        :param area: The area of the room.
        :param location: The location of the room (three words terminated with a period).
        :param name: The name of the room.
        :param room_type: The type of the room.
        """
        super().__init__(area, name, location)
        self._name = None
        self._room_type = None

        # call setters to apply validation
        self.name = name
        self.room_type = room_type

    @property
    def room_type(self) -> RoomType:
        return self._room_type

    @room_type.setter
    def room_type(self, value: RoomType):
        if value is not None:
            self._room_type = value
        else:
            raise ValueError("room_type must be of type RoomType")

    def toString(self):
        room_details = (
            f"Room ({super().__str__()} Room, "
            f"Room Type: {self.room_type})"
        )

        return room_details

    class Java:
        implements = ['com.middleware.interface.metamenth.IRoom']
