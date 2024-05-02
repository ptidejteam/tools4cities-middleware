from abc import ABC
from metamenth.datatypes.measure import Measure


class AbstractMeasure(ABC):
    """
    Defines properties shared by all measures

    Author: Peter Yefi
    Email: peteryefi@gmail.com
    """
    def __init__(self, measure: Measure):
        self.measurement_unit = measure.unit

    def toString(self):
        return f"Unit: {self.measurement_unit.value}, " \
               f"Measure Type: {None}"

    class Java:
        implements = ['com.middleware.interface.metamenth.IAbstractMeasure']
