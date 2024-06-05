import uuid
from metamenth.datatypes.binary_measure import BinaryMeasure
from metamenth.structure.material import Material


class Layer:
    """
    A layer in the envelope of a building

    Author: Peter Yefi
    Email: peteryefi@gmail.com
    """
    def __init__(
        self,
        height: BinaryMeasure,
        length: BinaryMeasure,
        thickness: BinaryMeasure,
        material: Material,
        has_vapour_barrier: bool = False,
        has_air_barrier: bool = False,

    ):
        self._UID = str(uuid.uuid4())  # Generate a unique identifier
        self._height = None
        self._length = None
        self._thickness = None
        self._material = None
        self._has_vapour_barrier = has_vapour_barrier
        self._has_air_barrier = has_air_barrier

        # apply validation with setters
        self.setHeight(height)
        self.setLength(length)
        self.setThickness(thickness)
        self.setMaterial(material)

    def getUID(self) -> str:
        return self._UID

    def getHeight(self) -> BinaryMeasure:
        return self._height

    def setHeight(self, value: BinaryMeasure):
        if value is None:
            raise ValueError("height should be of type BinaryMeasure")
        self._height = value

    def getLength(self) -> BinaryMeasure:
        return self._length

    def setLength(self, value: BinaryMeasure):
        if value is None:
            raise ValueError("length should be of type BinaryMeasure")
        self._length = value

    def getThickness(self) -> BinaryMeasure:
        return self._thickness

    def setThickness(self, value: BinaryMeasure):
        if value is None:
            raise ValueError("thickness should be of type BinaryMeasure")
        self._thickness = value

    def getMaterial(self) -> Material:
        return self._material

    def setMaterial(self, value: Material):
        if value is None:
            raise ValueError("material cannot be None")
        self._material = value

    def getHasVapourBarrier(self) -> bool:
        return self._has_vapour_barrier

    def setHasVapourBarrier(self, value: bool):
        self._has_vapour_barrier = value

    def getHasAirBarrier(self) -> bool:
        return self._has_air_barrier

    def setHasAirBarrier(self, value: bool):
        self._has_air_barrier = value

    def toString(self) -> str:
        return self.__str__()

    def __str__(self):
        material_str = f"Material: {str(self.getMaterial())}" if self.getMaterial() else "Material: None"
        return (
            f"Layer("
            f"UID: {self.getUID()}, "
            f"Height: {self.getHeight().getValue()} {self.getHeight().getMeasurementUnit()}, "
            f"Length: {self.getLength().getValue()} {self.getLength().getMeasurementUnit()}, "
            f"Thickness: {self.getThickness().getValue()} {self.getThickness().getMeasurementUnit()}, "
            f"Vapour Barrier: {self.getHasVapourBarrier()}, "
            f"Air Barrier: {self.getHasAirBarrier()}, "
            f"{material_str})"
        )

    class Java:
        implements = ['ca.concordia.ngci.tools4cities.metamenth.interfaces.structure.ILayer']
