from metamenth.datatypes.interfaces.abstract_measure import AbstractMeasure
from metamenth.structure.interfaces.abstract_floor_space import AbstractFloorSpace


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
        room_type: str,
        gateway,
        location: str = None
    ):
        """
        :param area: The area of the room.
        :param location: The location of the room (three words terminated with a period).
        :param name: The name of the room.
        :param room_type: The type of the room.
        """
        super().__init__(area, name, gateway, location)
        self._room_type = None

        # call setters to apply validation
        self.setRoomType(room_type)

    def getRoomType(self) -> str:
        return self._room_type

    def setRoomType(self, value: str):
        if value is not None:
            self._room_type = value
        else:
            raise ValueError("room_type must be of type RoomType")

    def toString(self):
        room_details = (
            f"Room ({super().toString()} Room, "
            f"Room Type: {self.getRoomType()})"
        )

        return room_details

    class Java:
        implements = ['com.middleware.metamenth.interfaces.structure.IRoom']
