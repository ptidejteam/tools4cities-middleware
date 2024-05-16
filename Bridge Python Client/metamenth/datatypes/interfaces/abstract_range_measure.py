from metamenth.datatypes.interfaces.abstract_measure import AbstractMeasure
from metamenth.datatypes.measure import Measure


class AbstractRangeMeasure(AbstractMeasure):
    """
    Defines properties shared by linear, continuous and exponential measures

    Author: Peter Yefi
    Email: peteryefi@gmail.com
    """

    def __init__(self, measure: Measure):
        super().__init__(measure)
        self._minimum = None
        self._maximum = None

        self.setMinimum(measure.getMinimum())
        self.setMaxmimum(measure.getMaximum())

    def setMinimum(self, value: float):
        if value is None:
            raise ValueError("minimum must be of type float")
        self._minimum = value

    def getMinimum(self) -> float:
        return self._minimum

    def setMaxmimum(self, value: float):
        if value is None:
            raise ValueError("maximum must be of type float")
        self._maximum = value

    def getMaximum(self) -> float:
        return self._maximum

    def __str__(self):
        return (
            f"Minimum: {self.getMinimum()}, "
            f"Maximum: {self.getMaximum()}, "
            f"{super().__str__()}"
        )

    def toString(self):
        return self.__str__()

    class Java:
        implements = ['com.middleware.interfaces.metamenth.datatypes.IAbstractRangeMeasure']
