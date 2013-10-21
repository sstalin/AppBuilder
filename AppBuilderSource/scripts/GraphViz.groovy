

def command = "dot -Tpng -ohello.png hello.gv"
def p = command.execute()

p.waitFor()

println 'GraphViz done: hello.png'

command = "dot -Tplain -ohello.txt hello.gv"

p = command.execute()

p.waitFor()

println 'GraphViz done: hello.txt'