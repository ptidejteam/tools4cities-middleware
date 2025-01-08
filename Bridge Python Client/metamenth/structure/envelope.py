import uuid
from metamenth.structure.cover import Cover
from py4j.java_gateway import JavaGateway


class Envelope:
    """
    The envelope of a building

    Author: Peter Yefi
    Email: peteryefi@gmail.com
    """
    def __init__(self):
        self._UID = str(uuid.uuid4())
        gateway = JavaGateway()
        self._covers = gateway.jvm.java.util.ArrayList()

    def getUID(self) -> str:
        return self._UID

    def addCover(self, cover: Cover):
        """
        Adds a cover to a building
        :param cover: the building cover e.g., wall, roof
        :return:
        """
        if cover is None:
            raise ValueError("cover must be of type Cover")
        self._covers.add(cover)

    def getCovers(self):
        return self._covers

    def getCoverByUID(self, uid: str) -> Cover:
        """
        Retrieves a cover given the uid
        :param uid: the uid of the cover
        :return:
        """
        for cover in self._covers:
            if cover.getUID() == uid:
                return cover

    def toString(self):
        return self.__str__()

    def __str__(self):
        cover_details = "\n".join(str(cover) for cover in self._covers)
        return (
            f"Envelope("
            f"UID: {self.getUID}, "
            f"Layers:\n{cover_details})"
        )

    class Java:
        implements = ['ca.concordia.ngci.tools4cities.metamenth.interfaces.structure.IEnvelope']
