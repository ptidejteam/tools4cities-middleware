from hierarchy.mammal import Mammal


class Dog(Mammal):
    def __init__(self, name, age):
        super().__init__(name, age, "Dog")
        self.breed = "Unknown Breed"

    def bark(self):
        print("Woof!")

    class Java:
        implements = ['com.middleware.interfaces.IDog']