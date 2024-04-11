from hierarchy.mammal import Mammal


class Whale(Mammal):
    def __init__(self, name, age):
        super().__init__(name, age, "Whale")
        self.species = "Blue Whale"
        self.children: ['Whale'] = []

    def swim(self):
        print("Swimming gracefully")

    def addChild(self, child: 'Whale'):
        self.children.append(child)

    def getChildren(self):
        return self.children

    def getSpecies(self):
        return self.species

    def getFirstChild(self):
        return self.children[0]

    class Java:
        implements = ['com.middleware.interfaces.IWhale']