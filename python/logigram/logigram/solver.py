from domains import Domains
from table import Table
from fact import Unequal, Equal, Difference, Comparison

class Solver:
    def __init__(self, domains):
        self.domains = domains
        self.table = Table(self.domains)
        self.facts = []

    def addFact(self, fact):
        'Add a fact to the database of the solver.'
        self.facts.append(fact)

