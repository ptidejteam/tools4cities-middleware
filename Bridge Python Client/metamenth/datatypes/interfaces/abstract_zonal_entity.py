from abc import ABC
from uuid import uuid4


class AbstractZonalEntity(ABC):

    def __init__(self):
        """
        """
        self._UID = str(uuid4())
        self._zones = []

    @property
    def UID(self) -> str:
        return self._UID

    @property
    def zones(self):
        return self._zones

    @zones.setter
    def zones(self, value):
        if value is not None:
            self._zones = value
        else:
            raise ValueError('zones must be of type [Zone]')







