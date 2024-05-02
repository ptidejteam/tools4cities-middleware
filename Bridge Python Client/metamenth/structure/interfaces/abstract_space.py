import uuid
from metamenth.datatypes.interfaces.abstract_measure import AbstractMeasure


class AbstractSpace:
    """
    An abstract class for spaces in a building
    """

    def __init__(self, area: AbstractMeasure, location: str = None):
        """
        :param area: The area of the space.
        :param location: The location of the space (three words delimited with two periods).
        """
        super().__init__()
        self._UID = str(uuid.uuid4())
        self._area = None
        self._location = None

        # Apply validation
        self.area = area
        self.location = location

    @property
    def UID(self) -> str:
        return self._UID

    @property
    def area(self) -> AbstractMeasure:
        return self._area

    @area.setter
    def area(self, value: AbstractMeasure):
        if value is None:
            raise ValueError('area must be of type BinaryMeasure')
        self._area = value

    @property
    def location(self) -> str:
        return self._location

    @location.setter
    def location(self, value: str):
        self._location = value

    @property
    def schedules(self):
        raise AttributeError("Cannot get schedules")

    def toString(self):
        return (
            f"UID: {self.UID}, "
            f"Area: {self.area}, "
            f"Location: {self.location}, "
        )

    class Java:
        implements = ['com.middleware.interface.metamenth.IAbstractSpace']
