from uuid import uuid4


class Zone:
    """
    A zone in a building e.g. HVAC (thermal) zone

    Author: Peter Yefi
    Email: peteryefi@gmail.com
    """

    def __init__(self,
                 name: str,
                 zone_type: str,
                 gateway,
                 hvac_type: str = None,
                 description: str = None
                 ):
        """
        :param name: The name of the zone
        :param description: The description of the zone.
        :param zone_type: The type of the zone.
        :param hvac_type: The HVAC type of the zone. Defaults to HVACType.NONE if zone_type is not HVAC.
        """
        self._UID = str(uuid4())
        self._description = None
        self._name = None
        self._zone_type = None
        self._hvac_type = None
        self.gateway = gateway

        # Apply validation
        self.setName(name)
        self.setZoneType(zone_type)
        if zone_type == self.gateway.jvm.com.middleware.metamenth.enums.ZoneType.HVAC.getValue():
            self.setHvacType(hvac_type)
        self.setDescription(description)

    def getUID(self) -> str:
        return self._UID

    def getDescription(self) -> str:
        return self._description

    def setDescription(self, value: str):
        self._description = value

    def getName(self) -> str:
        return self._name

    def setName(self, value: str):
        if value is None:
            raise ValueError('name must be a string')
        self._name = value

    def getZoneType(self) -> str:
        return self._zone_type

    def setZoneType(self, value: str):
        if value is None:
            raise ValueError('zone_type must be of type str')
        self._zone_type = value

    def getHvacType(self) -> str:
        return self._hvac_type

    def setHvacType(self, value: str):
        if self.getZoneType() != self.gateway.jvm.com.middleware.metamenth.enums.ZoneType.HVAC.getValue():
            raise ValueError("HVAC type is only applicable for zones with ZoneType.HVAC.")
        self._hvac_type = value

    def toString(self):
        return self.__str__()

    def equals(self, other):
        return self.__eq__(other)

    def hashCode(self):
        return hash(self.getName())

    def __eq__(self, other):
        # zones are equal if they share the same name
        if isinstance(other, Zone):
            # Check for equality based on the 'name' attribute
            return self.getName() == other.getName()
        return False

    def __str__(self):

        zone_details = (
            f"Zone("
            f"UID: {self.getUID()}, "
            f"Name: {self.getName()}, "
            f"Description: {self.getDescription()}, "
            f"ZoneType: {self.getZoneType()}, "
            f"HVACType: {self.getHvacType()})"
        )

        return zone_details

    class Java:
        implements = ['com.middleware.metamenth.interfaces.datatypes.IZone']
