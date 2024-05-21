from uuid import uuid4
from metamenth.measure_instruments.weather_data import WeatherData
from metamenth.misc.validate import Validate


class WeatherStation:
    def __init__(self, name: str, gateway, location: str = None):
        """
        :param location: The location of the weather station.
        """
        self._UID = str(uuid4())
        self._name = None
        self._location = Validate.validateWhat3word(location)
        self._weather_data = gateway.jvm.java.util.ArrayList()
        self.gateway = gateway

        self.setName(name)

    def getUID(self) -> str:
        return self._UID

    def getName(self) -> str:
        return self._name

    def setName(self, value: str):
        if value is not None:
            self._name = value
        else:
            ValueError('name must be of type str')

    def getLocation(self) -> str:
        return self._location

    def setLocation(self, value: str):
        self._location = Validate.validateWhat3word(value)

    def addWeatherData(self, weather_data: [WeatherData]):
        """
        Adds some data recordings to this WeatherStation.
        :param weather_data: some weather data recorded for the weather station.
        """
        for data in weather_data:
            self._weather_data.add(data)

    def getWeatherData(self):
        """
        Search weather data by attributes values
        :return [WeatherData]:
        """
        return self._weather_data

    def __eq__(self, other):
        # Weather stations are equal if they share the same name
        if isinstance(other, WeatherStation):
            # Check for equality based on the 'name' attribute
            return self.getName() == other.getName()
        return False

    def toString(self):
        return self.__str__()

    def __str__(self):
        weather_station_details = (
            f"WeatherStation("
            f"UID: {self.getUID()}, "
            f"UID: {self.getName()}, "
            f"Location: {self.getLocation()}, "
            f"WeatherData Count: {len(self._weather_data)})"
        )
        weather_data = "\n".join(str(data) for data in self._weather_data)
        return f"{weather_station_details}\nWeather Data:\n{weather_data}"

    class Java:
        implements = ['com.middleware.metamenth.interfaces.measureinstruments.IWeatherStation']
