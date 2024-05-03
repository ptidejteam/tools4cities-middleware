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
        self.setArea(area)
        self.setLocation(location)

    def getUID(self) -> str:
        return self._UID

    def getArea(self) -> AbstractMeasure:
        return self._area

    def setArea(self, value: AbstractMeasure):
        if value is None:
            raise ValueError('area must be of type BinaryMeasure')
        self._area = value

    def getLocation(self) -> str:
        return self._location

    def setLocation(self, value: str):
        self._location = value

    def toString(self):
        return (
            f"UID: {self.getUID()}, "
            f"Area: {self.getArea()}, "
            f"Location: {self.getLocation()}, "
        )

    class Java:
        implements = ['com.middleware.interface.metamenth.structure.IAbstractSpace']
