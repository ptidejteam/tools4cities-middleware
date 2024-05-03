from dataclasses import dataclass
from metamenth.enumerations.measurement_unit import MeasurementUnit


@dataclass
class Measure:
    def __init__(self, unit: MeasurementUnit = None, minimum: float = 0.0, maximum: float = 0.0, slope: float = 0.0,
                 exponent: float = 0.0, mantissa: float = 0.0):
        self._unit = unit
        self._minimum = minimum
        self._maximum = maximum
        self._slope = slope
        self._exponent = exponent
        self._mantissa = mantissa

    def getUnit(self) -> MeasurementUnit:
        return self._unit

    def setUnit(self, unit: MeasurementUnit):
        self._unit = unit

    def getMinimum(self) -> float:
        return self._minimum

    def setMinimum(self, minimum: float):
        self._minimum = minimum

    def getMaximum(self) -> float:
        return self._maximum

    def setMaximum(self, maximum: float):
        self._maximum = maximum

    def getSlope(self) -> float:
        return self._slope

    def setSlope(self, slope: float):
        self._slope = slope

    def getExponent(self) -> float:
        return self._exponent

    def setExponent(self, exponent: float):
        self._exponent = exponent

    def getMantissa(self) -> float:
        return self._mantissa

    def setMantissa(self, mantissa: float):
        self._mantissa = mantissa

    def toString(self):
        return (f"Unit: {self.getUnit()}, "
                f"Max: {self.getMaximum()}, "
                f"Min: {self.getMinimum()}, "
                f"Slope: {self.getSlope()}, "
                f"Exponsent: {self.getExponent()}, "
                f"Mantissa: {self.getMantissa()}")

    class Java:
        implements = ['com.middleware.interfaces.metamenth.datatypes.IMeasure']
