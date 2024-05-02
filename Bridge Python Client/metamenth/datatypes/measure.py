from dataclasses import dataclass
from metamenth.enumerations.measurement_unit import MeasurementUnit


@dataclass
class Measure:
    def __init__(self, unit: float = 0.0, minimum: float = 0.0, maximum: float = 0.0, slope: float = 0.0,
                 exponent: float = 0.0, mantissa: float = 0.0):
        self.unit = unit
        self.minimum = minimum
        self.maximum = maximum
        self.slope = slope
        self.exponent = exponent
        self.mantissa = mantissa

    def toString(self):
        return (f"Unit: {self.unit}, "
                f"Max: {self.maximum}, "
                f"Min: {self.minimum}, "
                f"Slope: {self.slope}, "
                f"Exponsent: {self.exponent}, "
                f"Mantissa: {self.mantissa}")

    class Java:
        implements = ['com.middleware.interface.metamenth.IAbstractMeasure']
