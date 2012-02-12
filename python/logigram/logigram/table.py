
class Table:
    def __init__(self, domains):
        self.domains = domains
        self.reset()

    def __str__(self):
        result = ''
        chars = { None:'.', False:'-', True:'+' }
        for (idx, row) in enumerate(self.table):
            for item in row:
                result += chars[item] + ' '
            result += str(idx) + '\n'
        return result

    def reset(self):
        'Fills table with None values.'
        self.table = self.genTable()

    def genTable(self):
        'Generates an empty table for a given number of domains and entities.'
        result = []
        vert = range(1, self.domains.lenD)
        vert.reverse()
        for v in vert:
            for X in range(self.domains.lenE):
                result.append([ None for XX in range(v * self.domains.lenE)])
        return result

    def toVertIdx(self, domain, entity):
        'Converts a domain/entity pair to a vertical index into self.table.'
        self.domains.isVertDomain(domain)
        self.domains.isEntity(entity)
        return (self.domains.lenD - 1 - domain) * self.domains.lenE + entity

    def toHorizIdx(self, domain, entity):
        'Converts a domain/entity pair to a horizontal index into self.table.'
        self.domains.isHorizDomain(domain)
        self.domains.isEntity(entity)
        return domain * self.domains.lenE + entity

    def put(self, d1, e1, d2, e2, value):
        'Puts a certain value on the intersection of to domain/entity nubmers.'
        i1 = self.toVertIdx(d1, e1)
        i2 = self.toHorizIdx(d2, e2)
        self.table[i1][i2] = value

    def get(self, d1, e1, d2, e2):
        'Returns the value of a certain domain/entity pair.'
        i1 = self.toVertIdx(d1, e1)
        i2 = self.toHorizIdx(d2, e2)
        return self.table[i1][i2]

    def isValue(self, value, *coords):
        'Returns a boolean.'
        return self.get(*coords) == value

    def out(self):
        'Outputs object onto stdout.'
        print str(self)

    def row(self, d1, d2, e):
        'Returns an entity row of two intersecting domains as a list.'
        ent_row = self.entireRow(d1, e)
        col_start = d2 * self.domains.lenE
        col_end = col_start + self.domains.lenE
        return ent_row[col_start:col_end]

    def col(self, d1, d2, e):
        'Returns an entity column of two intersecting domains as a list.'
        ent_col = self.entireCol(d2, e)
        row_start = (self.domains.lenD - 1 - d1) * self.domains.lenE
        row_end = row_start + self.domains.lenE
        return ent_col[row_start:row_end]

    def entireRow(self, domain, entity):
        'Returns an entire entity row for a given domain as a list.'
        v_idx = self.toVertIdx(domain, entity)
        return self.table[v_idx]

    def entireCol(self, domain, entity):
        'Returns an entire entity column for a given domain as a list.'
        h_idx = self.toHorizIdx(domain, entity)
        height = self.domains.lenD - 1 - domain
        row_indices = height * self.domains.lenE
        rows = self.table[:row_indices]
        return [ row[h_idx] for row in rows ]
    
    def rowIndex(self, d1, d2, e1, value):
        """Returns the indices that contain a certain value for a given entity row in 
           two domains."""
        row = self.row(d1, d2, e1)
        return [ i for (i, cell) in enumerate(row) if cell == value ]
    
    def colIndex(self, d1, d2, e2, value):
        """Returns the indices that contain a certain value for a given entity column in
           two domains."""
        col = self.col(d1, d2, e2)
        return [ i for (i, cell) in enumerate(col) if cell == value ]
        
