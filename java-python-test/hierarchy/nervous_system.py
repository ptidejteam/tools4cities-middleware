class NervousSystem:
    def __init__(self):
        self.brain = "Brain"
        self.spinal_cord = "Spinal Cord"

    def toString(self):
        return f"Brian: {self.brain}, Spinal Cord: {self.spinal_cord}"

    class Java:
        implements = ['com.middleware.interface.INervousSystem']