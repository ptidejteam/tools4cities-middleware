from datetime import datetime
import uuid
from abc import ABC
from typing import Union
from metamenth.misc.validate import Validate


class AbstractDataMeasure(ABC):
    """
    This class represents the data recorded by sensors and meters
    The unit of measurement depends on the phenomenon measured by a meter or sensor

    Author: Peter Yefi
    Email: peteryefi@gmail.com
    """

    def __init__(self, value: Union[float, str], timestamp: str = None, measurement_type: str = None):
        """
        :param value: The numerical/string value measured
        :param timestamp: the time of measurement
        :param measurement_type: the type of the measurment, e.g., electricity consumption

        """
        self._UID = str(uuid.uuid4())
        self._timestamp = datetime.now().replace(microsecond=0) if timestamp is None else Validate.parseDate(timestamp)
        self._value = None
        self._measurement_type = measurement_type

        # Apply validation
        self.setValue(value)

    def getUID(self) -> str:
        return self._UID

    def getValue(self) -> float:
        return self._value

    def setValue(self, value: Union[float, str]):
        if value is not None:
            self._value = value
        else:
            raise ValueError("Value must be a float")

    def getMeasurementType(self) -> str:
        return self._measurement_type

    def setMeasurementType(self, value: str):
        self._measurement_type = value

    def getTimeStamp(self) -> str:
        return str(self._timestamp)

    def __eq__(self, other):
        if isinstance(other, AbstractDataMeasure):
            return self.getUID() == other.getUID()
        return False

    def __str__(self):
        """
        :return: A formatted string of the meter readings.
        """
        return (f"DataMeasure (UID: {self.getUID()}, Value: {self.getValue()}, "
                f"Timestamp: {self.getTimeStamp()}, Measurement Type: {self.getMeasurementType() if self.getMeasurementType() else None})")

    def toString(self):
        return self.__str__()

    class Java:
        implements = ['com.middleware.interfaces.metamenth.measureinstruments.IAbstractDataMeasure']