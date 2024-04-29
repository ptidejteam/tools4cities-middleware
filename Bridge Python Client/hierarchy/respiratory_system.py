class RespiratorySystem:
    def __init__(self):
        self.lungs = "Lungs"
        self.nose = "Nose"

    def toString(self):
        return f"Brian: {self.lungs}, Spinal Cord: {self.nose}"

    class Java:
        implements = ['com.middleware.interface.IRespiratorySystem']
