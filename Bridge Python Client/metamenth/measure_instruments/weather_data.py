from datetime import datetime
from uuid import uuid4
from metamenth.datatypes.interfaces.abstract_measure import AbstractMeasure
from metamenth.misc.validate import Validate


class WeatherData:
    def __init__(self, data: AbstractMeasure, timestamp: str = None):
        """
        :param data: The binary measure (value and unit) of the weather data.

        """
        self._UID = str(uuid4())
        self._timestamp = datetime.now() if timestamp is None else Validate.parseDate(timestamp)
        self._data = None

        self.setData(data)

    def getUID(self) -> str:
        return self._UID

    def getData(self) -> AbstractMeasure:
        return self._data

    def setData(self, value: AbstractMeasure):
        self._data = value

    def getTimestamp(self) -> str:
        return str(self._timestamp)

    def toString(self):
        return self.__str__()

    def __str__(self):
        """
        Returns a string representation of the BuildingWeatherData instance.

        :return: A formatted string representing the BuildingWeatherData details.
        """
        return (
            f"BuildingWeatherData("
            f"UID: {self.getUID()}, "
            f"Timestamp: {self.getTimestamp()}, "
            f"Data: {self.getData()})"
        )

    class Java:
        implements = ['ca.concordia.ngci.tools4cities.metamenth.interfaces.measureinstruments.IWeatherData']
