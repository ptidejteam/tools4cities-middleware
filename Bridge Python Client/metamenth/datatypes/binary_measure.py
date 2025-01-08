from metamenth.datatypes.interfaces.abstract_measure import AbstractMeasure
from .measure import Measure


class BinaryMeasure(AbstractMeasure):
    """
    Represents a binary measure with a value and a measurement unit.

    Author: Peter Yefi
    Email: peteryefi@gmail.com
    """
    def __init__(self, measure: Measure):
        super().__init__(measure, None)
        self.setValue(measure.getMinimum())

    def setValue(self, value: float):
        self._value = value

    def getValue(self) -> float:
        return self._value

    def toString(self):
        return (
            f"BinaryMeasure("
            f"Value: {self.getValue()}, "
            f"{super().__str__()})"
        )

    class Java:
        implements = ['ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IBinaryMeasure']
