from uuid import uuid4
from metamenth.datatypes.address import Address
from metamenth.structure.floor import Floor
from metamenth.measure_instruments.meter import Meter
from metamenth.datatypes.interfaces.abstract_measure import AbstractMeasure
from metamenth.measure_instruments.weather_station import WeatherStation
from metamenth.structure.envelope import Envelope


class Building:
    """
    A representation of a building

    Author: Peter Yefi
    Email: peteryefi@gmail.com
    """

    def __init__(self, construction_year: int, height: AbstractMeasure, floor_area: AbstractMeasure,
                 address: Address, building_type: str, floor: Floor, gateway):
        """
        :param construction_year: The construction year of the building
        :param height: The height of the building
        :param floor_area: The floor area of the building
        :param internal_mass: The internal mass of the building
        :param address: The address of the building
        :param building_type: The type of building
        """
        super().__init__()
        self._UID = str(uuid4())
        self._construction_year = None
        self._height = None
        self._floor_area = None
        self._internal_mass = None
        self._address = None
        self._building_type = None
        self._floors = gateway.jvm.java.util.ArrayList()
        self._meters = gateway.jvm.java.util.ArrayList()
        self._zones = gateway.jvm.java.util.ArrayList()
        self._weather_stations: [WeatherStation] = []
        self._envelope = None

        # apply validation
        self.setConstructionYear(construction_year)
        self.setHeight(height)
        self.setFloorArea(floor_area)
        self.setAddress(address)
        self.setBuildingType(building_type)
        self.addFloor(floor)

    def getUID(self) -> str:
        return self._UID

    def getConstructionYear(self) -> int:
        return self._construction_year

    def setConstructionYear(self, value: int):
        if value is not None:
            self._construction_year = value
        else:
            raise ValueError("construction_year must be a number")

    def getHeight(self) -> AbstractMeasure:
        return self._height

    def setHeight(self, value: AbstractMeasure):
        if value is not None:
            self._height = value
        else:
            raise ValueError("height must be of type AbstractMeasure")

    def getFloorArea(self) -> AbstractMeasure:
        return self._floor_area

    def setFloorArea(self, value: AbstractMeasure):
        if value is not None:
            # write state before updating
            self._floor_area = value
        else:
            raise ValueError("floor_area must be of type AbstractMeasure")

    def getInternalMass(self) -> AbstractMeasure:
        return self._internal_mass

    def setInternalMass(self, value: AbstractMeasure):
        if value is not None:
            self._internal_mass = value
        else:
            raise ValueError("internal_mass must be of type AbstractMeasure")

    def getAddress(self) -> Address:
        return self._address

    def setAddress(self, value: Address):
        if value is not None:
            self._address = value
        else:
            raise ValueError("address must be of type Address")

    def getBuildingType(self) -> str:
        return self._building_type

    def setBuildingType(self, value: str):
        if value is not None:
            self._building_type = value
        else:
            raise ValueError("building_type must be of type str")

    def addFloor(self, floor: Floor):
        if floor is not None:
            self._floors.add(floor)
        else:
            raise ValueError("floors must be of type Floor")
        return self

    def addMeter(self, meter: Meter):
        if meter is not None:
            self._meters.add(meter)
        else:
            raise ValueError("meters must be of type Meter")
        return self

    def addWeatherStation(self, weather_station: WeatherStation):
        if weather_station not in  self._weather_stations:
            if weather_station is not None:
                self._weather_stations.append(weather_station)
            else:
                raise ValueError("weather_station must be of type Weather Station")
        return self

    def getWeatherStation(self, name: str) -> WeatherStation:
        for weather_station in self._weather_stations:
            if weather_station.getName() == name:
                return weather_station

    def removeWeatherStation(self, weather_station: WeatherStation) -> bool:
        for station in self._weather_stations:
            if station.getName() == weather_station.getName():
                self._weather_stations.remove(station)
                return True
        return False

    def getZones(self):
        return self._zones

    def getEnvelope(self) -> Envelope:
        return self._envelope

    def setEnvelope(self, value: Envelope):
        self._envelope = value

    def toString(self):
        floors_info = "\n".join([f"  - Floor {floor.getNumber()}: {floor}" for floor in self._floors])
        meter_info = "\n".join([f"  - {meter}" for meter in self._meters])

        return (f"Building("
                f"UID: {self.getUID()}, "
                f"Construction Year: {self.getConstructionYear()}, "
                f"Height: {self.getHeight()}, "
                f"Floor Area: {self.getFloorArea()}, "
                f"Internal Mass: {self.getInternalMass()}, "
                f"Address: {self.getAddress()}, "
                f"Building Type: {self.getBuildingType()}, "
                f"Envelope: {self.getEnvelope()}, "
                f"Floor Count: {len(self._floors)}, "
                f"Floors:\n{floors_info}, "
                f"Meters:\n{meter_info})")

    class Java:
        implements = ['com.middleware.metamenth.interfaces.structure.IBuilding']
