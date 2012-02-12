import domains, solver, fact

class FileParser:
    def __init__(self, filename):
        def printList(h, L):
            for x in L: print h, x
            print
        self.logigram = open(filename)
        self.factmap = {
            'Unequal':fact.Unequal,
            'Equal':fact.Equal,
            'Comparison':fact.Comparison,
            'Difference':fact.Difference
            }
        self.operators = [ '<', '<=', '>=', '>' ]
        self.domains = domains.Domains(*self.parseDomains())
        printList('VERTICAL:', self.domains.domains[self.domains.vertical])
        printList('HORIZONTAL:', self.domains.domains[self.domains.horizontal])
        printList('ENTITIES:', self.domains.entities)
        self.solver = solver.Solver(self.domains)
        self.parseFacts()

    def parseDomains(self):
        d = []
        e = []
        for line in self.logigram:
            line = line[:-1]
            if line == '':
                break
            else:
                if not line.startswith('#'):
                    domain, entity_str = line.split(': ')
                    entities = [ s.strip() for s in entity_str.split(',') ]
                    d.append(domain)
                    e.append(entities)
        return (d, e)

    def parseFacts(self):
        for line in self.logigram:
            line = line[:-1]
            if line == '':
                break
            else:
                if not line.startswith('#'):
                    classname, args_str = line.split(': ')
                    args_list = args_str.split(' ')
                    args = [ self.solver.table ]
                    if len(args_list) >= 2:
                        v1 = tuple(args_list[0].split('/'))
                        v2 = tuple(args_list[1].split('/'))
                        args += list(self.domains.strToIdx(*v1))
                        args += list(self.domains.strToIdx(*v2))
                    if len(args_list) == 4:
                        args.append(self.domains.domainToIdx(args_list[2]))
                        if args_list[3] in self.operators:
                            args.append(args_list[3])
                        else:
                            args.append(int(args_list[3]))
                    class_ = self.factmap[classname]
                    args = tuple(args)
                    fact = class_(*args)
                    print 'FACT:', fact
                    self.solver.addFact(fact)
                    fact.register()
