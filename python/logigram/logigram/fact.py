
class Fact:
    def __init__(self, table, d1, e1, d2, e2):
        self.table = table
        self.d1 = d1
        self.e1 = e1
        self.d2 = d2
        self.e2 = e2
        self.facts = []

    def newFact(self, factclass, *args):
        fact = factclass(self.table, *args)
        print fact
        self.facts.append(fact)
        fact.register()

    def subFacts(self):
        if len(self.facts) > 0:
            return str(self.facts)
        else:
            return ''

    def entity(self, n):
        index = [ (self.d1, self.e1), (self.d2, self.e2) ]
        d, e = index[n-1]
        repr = 3
        if repr == 1:
            return '%s.%s %d.%d' % (self.table.domains.domains[d],
                                    self.table.domains.entities[d][e],
                                    d,
                                    e)
        elif repr == 2:
            return '%s.%s' % (self.table.domains.domains[d],
                              self.table.domains.entities[d][e])
        elif repr == 3:
            return '%d.%d' % (d, e)

    def intersect(self, value, class_):
        L = self.table.domains.lenD
        if self.d1 in range(1, L - 1):
            for d in range(self.d1 + 1, L):
                col = self.table.col(d, self.d1, self.e1)
                if value in col:
                    coords = (d, col.index(value), self.d2, self.e2)
                    if self.table.isValue(None, *coords):
                        self.newFact(class_, *coords)

        if self.d2 in range(1, L):
            for d in range(self.d2 - 1, L):
                row = self.table.row(self.d2, d, self.e2)
                if value in row:
                    coords = (self.d1, self.e1, d, row.index(value))
                    if self.table.isValue(None, *coords):
                        self.newFact(class_, *coords)


class Unequal(Fact):
    def __repr__(self):
        return '%s is not %s' % (self.entity(1), self.entity(2))

    def register(self):
        self.table.put(self.d1, self.e1, self.d2, self.e2, False)
        self.putPluses()

    def putPluses(self):
        emptyR = self.table.rowIndex(self.d1, self.d2, self.e1, None)
        trueR = self.table.rowIndex(self.d1, self.d2, self.e1, True)
        if (len(emptyR) == 1) and (len(trueR) == 0):
            self.newFact(Equal, self.d1, self.e1, self.d2, emptyR[0])
        emptyC = self.table.colIndex(self.d1, self.d2, self.e2, None)
        trueC = self.table.colIndex(self.d1, self.d2, self.e2, True)
        if (len(emptyC) == 1) and (len(trueC) == 0):
            self.newFact(Equal, self.d1, emptyC[0], self.d2, self.e2)


class Equal(Fact):
    def __repr__(self):
        return '%s is %s' % (self.entity(1), self.entity(2))

    def register(self):
        self.table.put(self.d1, self.e1, self.d2, self.e2, True)
        self.intersect(True, Equal)
        self.putMinuses()
        self.extendPluses()

    def putMinuses(self):
        emptyC = self.table.colIndex(self.d1, self.d2, self.e2, None)
        for i in emptyC:
            if i != self.e1:
                self.newFact(Unequal, self.d1, i, self.d2, self.e2)
        emptyR = self.table.rowIndex(self.d1, self.d2, self.e1, None)
        for i in emptyR:
            if i != self.e2:
                self.newFact(Unequal, self.d1, self.e1, self.d2, i)

    def extendPluses(self):
        print '<extendPluses ' + str(self) + '>'
        vert = range(self.table.domains.lenD-1, self.d2, -1)
        horiz = range(self.d1)
        vert.remove(self.d1)
        horiz.remove(self.d2)
        #if 0 in horiz:
        #    horiz.remove(0)
        #if self.table.domains.lenD - 1 in vert:
        #    vert.remove(self.table.domains.lenD - 1)

        print 'vert=' + str(vert)
        print 'horiz=' + str(horiz)

        for d in horiz:
            row = self.table.row(self.d1, d, self.e1)
            print 'd=%d d1=%d row=%s' % (d, self.d1, str(row))
            if True in row:
                coords = (d, row.index(True), self.d2, self.e2)
                print 'h' + str(coords)
                if self.table.isValue(None, *coords):
                    self.newFact(Equal, *coords)

        for d in vert:
            col = self.table.col(d, self.d2, self.e2)
            print 'd=%d d2=%d col=%s' % (d, self.d2, str(col))
            if True in col:
                #coords = (self.d1, self.e1, d, col.index(True))
                coords = (d, col.index(True), self.d1, self.e1)
                print 'v' + str(coords)
                if self.table.isValue(None, *coords):
                    self.newFact(Equal, *coords)
        print '</extendPluses ' + str(self) + '>'

                
class Difference(Fact):
    def __init__(self, table, d1, e1, d2, e2, domain, difference):
        Fact.__init__(self, table, d1, e1, d2, e2)
        self.domain = domain
        self.difference = difference

    def __repr__(self):
        return 'for %s, %s equals %s + %d' % (
            self.table.domains.domains[self.domain],
            self.entity(1),
            self.entity(2),
            self.difference)

    def register(self):
        # TODO: Differences.register()
        pass


class Comparison(Fact):
    def __init__(self, table, d1, e1, d2, e2, domain, operator):
        Fact.__init__(self, table, d1, e1, d2, e2)
        self.domain = domain
        self.operator = operator

    def __repr__(self):
        return 'for %s, %s/%s %s %s/%s' % (
            self.table.domains.domains[self.domain],
            self.entity(1),
            self.operator,
            self.entity(2))

    def register(self):
        # TODO: Comparison.register()
        pass
        
