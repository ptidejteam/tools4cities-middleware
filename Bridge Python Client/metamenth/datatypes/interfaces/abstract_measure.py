from abc import ABC
from metamenth.datatypes.measure import Measure


class AbstractMeasure(ABC):
    """
    Defines properties shared by all measures

    Author: Peter Yefi
    Email: peteryefi@gmail.com
    """
    def __init__(self, measure: Measure):
        self._measurement_unit = None
        self.setMeasurementUnit(measure.getUnit())

    def getMeasurementUnit(self) -> str:
        return self._measurement_unit.value

    def setMeasurementUnit(self, measurementUnit):
        if not measurementUnit:
            raise ValueError('Measurement Unit is required')
        self._measurement_unit = measurementUnit

    def toString(self):
        return f"Unit: {self.getMeasurementUnit()}, " \
               f"Measure Type: {None}"

    class Java:
        implements = ['com.middleware.interface.metamenth.IAbstractMeasure']
