from metamenth.structure.interfaces.abstract_space import AbstractSpace
from metamenth.datatypes.interfaces.abstract_measure import AbstractMeasure
from metamenth.measure_instruments.meter import Meter
from metamenth.transducers.interfaces.abstract_transducer import AbstractTransducer


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
        self._meter = None
        self._transducers:  [AbstractTransducer] = []
        # apply validation through setters
        self.setName(name)
        self.setMeter(meter)

    def getName(self) -> str:
        return self._name

    def setName(self, value: str):
        if value is not None:
            self._name = value
        else:
            raise ValueError("name must be a string")

    def getMeter(self) -> Meter:
        return self._meter

    def setMeter(self, value: Meter):
        if value:
            if value.getMeterLocation() != self.getLocation():
                raise ValueError("what3words location of meter should be the same as space")
        self._meter = value

    def addTransducer(self, transducer: AbstractTransducer):
        if transducer not in self._transducers:
            self._transducers.append(transducer)
            return True
        return False

    def removeTransducer(self, transducer: AbstractTransducer):
        rm_index = -1
        for i in range(len(self._transducers)):
            if self._transducers[i].getName() == transducer.getName():
                rm_index = i
        if rm_index > -1:
            del self._transducers[rm_index]
            return True
        return False

    def getTransducer(self, name: str) -> AbstractTransducer:
        for transducer in self._transducers:
            if transducer.getName() == name:
                return transducer

    def equals(self, other):
        return self.__eq__(other)

    def __eq__(self, other):
        # spaces on a floor are equal if they share the same name
        if isinstance(other, AbstractFloorSpace):
            # Check for equality based on the 'name' attribute
            return self.getName() == other.getName()
        return False

    def toString(self) -> str:
        return (
            f"{super().__str__()}"
            f"Name: {self.getName()}, "
            f"Meter: {self.getMeter()}\n"
        )

    class Java:
        implements = ['ca.concordia.ngci.tools4cities.metamenth.interfaces.structure.IAbstractFloorSpace']
