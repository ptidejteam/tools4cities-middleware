from metamenth.datatypes.binary_measure import BinaryMeasure
import uuid
from metamenth.misc.validate import Validate


class Material:
    """
    Material making up layers in the envelope of a building

    Author: Peter Yefi
    Email: peteryefi@gmail.com
    """

    def __init__(
        self,
        description: str,
        material_type: str,
        density: BinaryMeasure,
        heat_capacity: BinaryMeasure,
        thermal_transmittance: BinaryMeasure,
        thermal_resistance: BinaryMeasure,
        solar_heat_gain_coefficient: float = None,
    ):
        self._UID = str(uuid.uuid4())
        self._description = None
        self._material_type = None
        self._density = None
        self._heat_capacity = None
        self._thermal_transmittance = None
        self._thermal_resistance = None
        self._solar_heat_gain_coefficient = Validate.validateSolarHeatGainCoefficient(solar_heat_gain_coefficient)

        self.setDescription(description)
        self.setMaterialType(material_type)
        self.setDensity(density)
        self.setHeatCapacity(heat_capacity)
        self.setThermalResistance(thermal_resistance)
        self.setThermalTransmittance(thermal_transmittance)

    def getUID(self) -> str:
        return self._UID

    def getDescription(self) -> str:
        return self._description

    def setDescription(self, value: str):
        if value is None:
            raise ValueError("description must be a string")
        self._description = value

    def getMaterialType(self) -> str:
        return self._material_type

    def setMaterialType(self, value: str):
        if value is None:
            raise ValueError("material_type must be of type str")
        self._material_type = value

    def getDensity(self) -> BinaryMeasure:
        return self._density

    def setDensity(self, value: BinaryMeasure):
        if value is None:
            raise ValueError("density must be of type BinaryMeasure")
        self._density = value

    def getHeatCapacity(self) -> BinaryMeasure:
        return self._heat_capacity

    def setHeatCapacity(self, value: BinaryMeasure):
        if value is None:
            raise ValueError("heat_capacity must be of type BinaryMeasure")
        self._heat_capacity = value

    def getThermalTransmittance(self) -> BinaryMeasure:
        return self._thermal_transmittance

    def setThermalTransmittance(self, value: BinaryMeasure):
        if value is None:
            raise ValueError("thermal_transmittance must be of type BinaryMeasure")
        self._thermal_transmittance = value

    def getThermalResistance(self) -> BinaryMeasure:
        return self._thermal_resistance

    def setThermalResistance(self, value: BinaryMeasure):
        if value is None:
            raise ValueError("thermal_resistance must be of type BinaryMeasure")
        self._thermal_resistance = value

    def getSolarHeatGainCoefficient(self) -> float:
        return self._solar_heat_gain_coefficient

    def setSolarHeatGainCoefficient(self, value: float):
        self._solar_heat_gain_coefficient = Validate.validateSolarHeatGainCoefficient(value)

    def toString(self):
        return self.__str__()

    def __str__(self):
        return (
            f"Material("
            f"UID: {self.getUID()}, "
            f"Description: {self.getDescription()}, "
            f"Type: {self.getMaterialType()}, "
            f"Density: {self.getDensity().getValue()} {self.getDensity().getMeasurementUnit()}, "
            f"Heat Capacity: {self.getHeatCapacity().getValue()} {self.getHeatCapacity().getMeasurementUnit()}, "
            f"Thermal Transmittance: {self.getThermalTransmittance().getValue()} {self.getThermalTransmittance().getMeasurementUnit()}, "
            f"Thermal Resistance: {self.getThermalResistance().getValue()} {self.getThermalResistance().getMeasurementUnit()}"
            f"Solar Heat Gain Coefficient: {self.getSolarHeatGainCoefficient()}"
        )

    class Java:
        implements = ['com.middleware.metamenth.interfaces.structure.IMaterial']
