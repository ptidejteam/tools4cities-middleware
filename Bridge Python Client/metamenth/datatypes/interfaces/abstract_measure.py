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
        return self._measurement_unit

    def setMeasurementUnit(self, measurementUnit: str):
        if not measurementUnit:
            raise ValueError('Measurement Unit is required')
        self._measurement_unit = measurementUnit

    def __str__(self):
        return f"Unit: {self.getMeasurementUnit()}, " \
               f"Measure Type: {None}"

    def toString(self):
       return self.__str__()

    class Java:
        implements = ['ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IAbstractMeasure']
