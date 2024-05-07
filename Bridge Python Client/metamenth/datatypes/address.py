from .point import Point
from metamenth.datatypes.binary_measure import BinaryMeasure


class Address:
    """
    Address of a building

    Author: Peter Yefi
    Email: peteryefi@gmail.com
    """
    def __init__(self, city: str, street: str, state: str, zip_code: str, country: str,
                 geocoordinate: Point = None,  what3word: str = None, north_orientation: BinaryMeasure = None):
        """
        :param city: The name of the city
        :param street: The street e.g., 6911 Ave De la Pepiniere.
        :param state: The state (or province) e.g., QC or Quebec.
        :param zip_code: The ZIP code e.g., H1N 1B9
        :param country: the country where the building is located
        :param what3word: The What3word address of the location.
        :param geocoordinate: The geographical coordinates of the location (an instance of Point).
        :param north_orientation: The north orientation of a building.
        """
        self._city = None
        self._street = None
        self._zip_code = None
        self._state = None
        self._country = None
        self._geocoordinates = None
        self._north_orientation = None
        self.setCity(city)
        self.setStreet(street)
        self.setZipCode(zip_code)
        self.setState(state)
        self.setCountry(country)
        self.setGeocoordinates(geocoordinate)

        self.north_orientation = north_orientation


    def setCity(self, value: str):
        self._city = value

    def getCity(self) -> str:
        return self._city

    def setStreet(self, value: str):
        self._street = value

    def getStreet(self) -> str:
        return self._street

    def setZipCode(self, value: str):
        self._zip_code = value

    def getZipCode(self) -> str:
        return self._zip_code

    def setState(self, value: str):
        self._state = value

    def getState(self) -> str:
        return self._state

    def setCountry(self, value: str):
        self._country = value

    def getCountry(self) -> str:
        return self._country

    def setGeocoordinates(self, value: Point):
        self._geocoordinates = value

    def getGeocoordinates(self) -> Point:
        return self._geocoordinates

    def setNorthOrientation(self, value: BinaryMeasure):
        self._north_orientation = value

    def getNorthOrientation(self) -> BinaryMeasure:
        return self._north_orientation

    def toString(self):
        geo_coordinate_str = (
            f", GeoCoordinate: (Latitude: {self.getGeocoordinates().getLatitude()}, "
            f"Longitude: {self.getGeocoordinates().getLongitude()})"
            if self.getGeocoordinates() is not None
            else None
        )

        north_orientation_str = (
            f", North Orientation: {self.getNorthOrientation().getValue()}"
            if self.getNorthOrientation() is not None
            else None
        )

        return (
            f"GeoLocation("
            f"City: {self.getCity()}, "
            f"Street: {self.getStreet()}, "
            f"State: {self.getState()}, "
            f"ZIP Code: {self.getZipCode()}, "
            f"Country: {self.getCountry()}, "
            f"{geo_coordinate_str}"
            f"{north_orientation_str})"
        )

    class Java:
        implements = ['com.middleware.interfaces.metamenth.datatypes.IAddress']

