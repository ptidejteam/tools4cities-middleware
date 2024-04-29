class DigestiveSystem:
    def __init__(self):
        self.stomach = "Stomach"
        self.intestines = "Intestines"

    def toString(self):
        return f"Brian: {self.stomach}, Spinal Cord: {self.intestines}"

    class Java:
        implements = ['com.middleware.interface.IDigestiveSystem']
