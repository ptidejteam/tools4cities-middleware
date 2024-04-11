from hierarchy.mammal import Mammal


class Human(Mammal):
    def __init__(self, name, age):
        super().__init__(name, age, "Human")
        self.nationality = "Unknown"

    def speak(self):
        print("Hello, I am a human")