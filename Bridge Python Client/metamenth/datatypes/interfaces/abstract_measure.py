from abc import ABC
from metamenth.datatypes.measure import Measure


class AbstractMeasure(ABC):
    """
    Defines properties shared by all measures

    Author: Peter Yefi
    Email: peteryefi@gmail.com
    """
    def __init__(self, measure: Measure, measure_type: str = None):
        self._measurement_unit = None
        self._measure_type = measure_type
        self.setMeasurementUnit(measure.getUnit())

    def getMeasurementUnit(self) -> str:
        return self._measurement_unit

    def setMeasurementUnit(self, measurement_unit: str):
        if not measurement_unit:
            raise ValueError('Measurement Unit is required')
        self._measurement_unit = measurement_unit

    def getMeasureType(self) -> str:
        return self._measure_type

    def setMeasureType(self, measure_type: str):
        self._measure_type = measure_type

    def __str__(self):
        return f"Unit: {self.getMeasurementUnit()}, " \
               f"Measure Type: {self.getMeasureType()}"

    def toString(self):
       return self.__str__()

    class Java:
        implements = ['ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IAbstractMeasure']
