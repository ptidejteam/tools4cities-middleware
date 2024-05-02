from typing import List
from metamenth.structure.interfaces.abstract_space import AbstractSpace
from metamenth.datatypes.interfaces.abstract_measure import AbstractMeasure
from metamenth.measure_instruments.meter import Meter


class AbstractFloorSpace(AbstractSpace):
    """
    An abstract class for spaces on a floor

    Author: Peter Yefi
    Email: peteryefi@gmail.com
    """

    def __init__(self, area: AbstractMeasure, name: str, location: str = None, meter: Meter = None):
        """
        Models spaces on a building's floor
        :param area: the area of the space
        :param name: the name of the space
        :param location: the what3word location of the space
        """

        super().__init__(area, location)
        self._name = None
        self._adjacent_spaces: List[AbstractFloorSpace] = []
        self._meter = meter
        # apply validation through setters
        self.name = name

    @property
    def name(self) -> str:
        return self._name

    @name.setter
    def name(self, value: str):
        if value is not None:
            self._name = value
        else:
            raise ValueError("name must be a string")

    @property
    def meter(self) -> Meter:
        return self._meter

    @meter.setter
    def meter(self, value: Meter):
        if value:
            if value.meter_location != self.location:
                raise ValueError("what3words location of meter should be the same as space")
        self._meter = value

    def __eq__(self, other):
        # spaces on a floor are equal if they share the same name
        if isinstance(other, AbstractFloorSpace):
            # Check for equality based on the 'name' attribute
            return self.name == other.name
        return False

    def toString(self) -> str:
        return (
            f"{super().__str__()}"
            f"Name: {self.name}, "
            f"Meter: {self.meter}\n"
        )

    class Java:
        implements = ['com.middleware.interface.metamenth.IAbstractFloorSpace']