import logigram
import gui
import sys

def WxGUI():
    domainsGUI = gui.domains.DomainsGUI()
    domainsGUI.app.MainLoop()
    if domainsGUI.isDone:
        domains = logigram.domains.Domains(domainsGUI.domains, domainsGUI.entities)
        solver = logigram.solver.Solver(domains)
        tableGUI = gui.table.TableGUI(solver.table)
        factsGUI = gui.facts.FactsGUI(solver, tableGUI)
        factsGUI.app.MainLoop()

def solveTextFile(name):
    try:
        fileparser = logigram.fileparser.FileParser(name)
        tableGUI = gui.table.TableGUI(fileparser.solver.table)
        tableGUI.drawValues()
        tableGUI.app.MainLoop()
    except Exception, e:
        print e
    
if __name__ == '__main__':
    solveTextFile('logigram2.txt')
else:
    if len(sys.argv) == 1:
        WxGUI()
    elif len(sys.argv) == 2:
        solveTextFile(sys.argv[1])
    else:
        print 'Usage: %s [optional: filename]\n' % (sys.argv[0],)
