from metamenth.structure.interfaces.abstract_floor_space import AbstractFloorSpace
from metamenth.datatypes.interfaces.abstract_measure import AbstractMeasure
from metamenth.enumerations.open_space_type import OpenSpaceType


class OpenSpace(AbstractFloorSpace):
    """
    Defines an open space on a floor of a building

    Author: Peter Yefi
    Email: peteryefi@gmail.com
    """

    def __init__(self, name: str, area: AbstractMeasure, space_type: str, location: str = None):
        """
        :param area: The area of the open space.
        :param location: The location of the open space (three words delimited with a period).
        :param space_type: The type of open space (enum).
        """
        super().__init__(area, name, location)
        self._space_type = None

        # Apply validation
        self.setSpaceType(space_type)

    def getSpaceType(self) -> str:
        return self._space_type.value

    def setSpaceType(self, value: str):
        if value is None:
            raise ValueError('space_type must be of type OpenSpaceType')
        self._space_type = value

    def toString(self):
        return f"OpenSpace ({super().__str__()} Space Type: {self.getSpaceType()})"

    class Java:
        implements = ['com.middleware.interfaces.metamenth.structure.IOpenSpace']
