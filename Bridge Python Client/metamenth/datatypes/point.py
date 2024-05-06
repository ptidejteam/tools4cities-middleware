class Point:
    """
    A geo-coordinate point with latitude and longitude.

    Author: Peter Yefi
    Email: peteryefi@gmail.com
    """

    def __init__(self, lat: float, lon: float):
        self._latitude = None
        self._longitude = None

        self.setLongitude(lon)
        self.setLatitude(lat)

    def setLatitude(self, value: float):
        self._latitude = value

    def getLatitude(self) -> float:
        return self._latitude

    def setLongitude(self, value: float):
        self._longitude = value

    def getLongitude(self) -> float:
        return self._longitude

    def toString(self):
        return f"Latitude: {self.getLatitude()}, Longitude: {self.getLongitude()}"
