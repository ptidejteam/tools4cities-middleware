import uuid
from metamenth.structure.layer import Layer


class Cover:
    """
       A building cover that forms the envelope of a building

       Author: Peter Yefi
       Email: peteryefi@gmail.com
       """

    def __init__(self, cover_type: str, gateway):
        """
        :param cover_type: the type of building cover
        """
        self._UID = str(uuid.uuid4())
        self._cover_type = None
        self._layers = gateway.jvm.java.util.ArrayList()  # the various layers in this building cover

        # validate cover type
        self.setCoverType(cover_type)

    def getUID(self) -> str:
        return self._UID

    def getCoverType(self) -> str:
        return self._cover_type

    def setCoverType(self, value: str):
        if value is None:
            raise ValueError("cover_type must be of type str")
        self._cover_type = value

    def addLayer(self, layer: Layer):
        """
        Add a layer (e.g., external wall) to the building cover e.g., wall
        :param layer: the layer to be added
        :return:
        """
        if layer.getMaterial().getMaterialType().split(":")[0].find(self.getCoverType()) != -1:
            self._layers.add(layer)
        else:
            raise ValueError("The layer you're trying to add has a different material from the cover.")

    def getLayerByUID(self, uid: str) -> Layer:
        """
        Retrieves a layer given the uid
        :param uid: the uid of the layer
        :return:
        """
        for layer in self._layers:
            if layer.getUID() == uid:
                return layer

    def toString(self):
        return self.__str__()

    def __str__(self):
        layer_str = "\n".join(str(layer) for layer in self._layers)
        return (
            f"Cover("
            f"UID: {self.getUID()}, "
            f"Cover Type: {self.getCoverType()}, "
            f"Layers:\n{layer_str})"
        )

    class Java:
        implements = ['com.middleware.metamenth.interfaces.structure.ICover']
