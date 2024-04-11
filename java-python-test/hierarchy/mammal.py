from hierarchy.nervous_system import NervousSystem
from hierarchy.respiratory_system import RespiratorySystem
from hierarchy.digestive_system import DigestiveSystem


class Mammal:
    def __init__(self, name, age, species):
        self.name = name
        self.age = age
        self.species = species
        self.nervous_system = NervousSystem()
        self.digestive_system = DigestiveSystem()
        self.respiratory_system = RespiratorySystem()

    def eat(self, food):
        print(f"{self.name} is eating {food}")

    def sleep(self):
        print(f"{self.name} is sleeping")

    def reproduce(self):
        print(f"{self.name} is reproducing")

    def toString(self):
        return f"Name: {self.name}\nAge: {self.age}\nSpecies: {self.species}" \
               f"\nNervous System: {self.nervous_system.toString()}" \
               f"\nDigestive System: {self.digestive_system.toString()}" \
               f"\nRespiratory System: {self.respiratory_system.toString()}"

    class Java:
        implements = ['com.middleware.interface.IMammal']