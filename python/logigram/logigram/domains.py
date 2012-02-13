import types

class Domains:
    def __init__(self, domains, entities):
        'Initialize the Domains class.'
        self.domains = domains
        self.entities = self.normalizeEntities(entities)
        self.lenD = len(domains)
        self.lenE = len(entities[0])
        self.vertical = slice(self.lenD-1, 0, -1)
        self.horizontal = slice(0, self.lenD-1)
        self.all = slice(self.lenD)

    def normalizeEntities(self, entities):
        'Change every list of entity to ints if the entire list is numeric.'
        def isNumber(s): # sorry for short circuit abuse
            return len(s) > 0 \
                   and (s.startswith('-') and s[1:].isdigit()) or s.isdigit()
        result = []
        for list_ in entities:
            isN = [ isNumber(x) for x in list_ ]
            if False in isN:
                result.append([ str(x) for x in list_ ])
            else:
                result.append([ int(x) for x in list_ ])
        return result

    def getEntities(self, domain):
        'Return a list of entities for a given domain number.'
        idx = self.domains.index(domain)
        return self.entities[idx]

    def domainToIdx(self, domain):
        'Return the index of a domain.'
        self.isDomain(domain)
        return self.domains.index(domain)

    def strToIdx(self, domain, entity):
        'Return the index of a certain domain/entity pair.'
        self.isDomain(domain)
        idx_domain = self.domains.index(domain)
        # dirty hack, assume all types in self.entities[x] are equal
        if isinstance(self.entities[idx_domain][0], types.IntType):
            entity = int(entity)
        self.isValidEntity(idx_domain, entity)
        idx_entity = self.entities[idx_domain].index(entity)
        return (idx_domain, idx_entity)

    def isDomain(self, domain):
        'Raises an exception if domain is not a valid domain or domain number.'
        if isinstance(domain, types.StringType):
            if not domain in self.domains:
                raise Exception('No such domain %s' % (domain,))
        elif isinstance(domain, types.IntType):
            if (domain < 0) or (domain >= self.lenD):
                raise Exception('Domain %d does not exist.' % (domain,))

    def isValidEntity(self, domain, entity):
        'Raises an exception if entity is an invalid entity for domain number.'
        if entity not in self.entities[domain]:
            raise Exception('Entity %s not in domain %s.' % \
                            (str(entity), self.domains[domain]))

    def isHorizDomain(self, domain):
        'Raises exception if domain is not a horizontal domain number.'
        self.isDomain(domain)
        if (domain < 0) or (domain >= (self.lenD - 1)):
            raise Exception('Domain %s is not on horizontal scale.' %
                            (self.domains[domain],))

    def isVertDomain(self, domain):
        'Raises exception if domain is not a vertical domain number.'
        self.isDomain(domain)
        if (domain < 1) or (domain >= self.lenD):
            raise Exception('Domain %s is not on vertical scale.' % 
                            (self.domains[domain],))

    def isEntity(self, entity):
        'Raises exception if entity is not a valid entity number.'
        if (entity < 0) or (entity >= self.lenE):
            raise Exception('No such entity %d' % (entity,))
