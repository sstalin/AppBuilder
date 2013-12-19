package xj.mobile.tool

import xj.mobile.lang.*
import xj.mobile.model.*

import static xj.translate.Logger.info 

public class GraphViz { 

  static gvDir = 'test/gvoutput'

  static void generateDiagram(String name, Application app) { 
	if (app) { 
	  def data = [states: [], transitions: []]
	  app.visit(GraphViz.&visitNodePre, GraphViz.&visitNodePost, data)

	  info "[GraphViz] " + data 

	  ///	node [shape = circle];
	  
	  File gv = new File("${gvDir}/${name}.gv")
	  gv.text = """digraph ${name} {
	compound=true;
	node [shape=rect, style=rounded];
	rankdir=LR;\n\t""" + graphBoday(data, data) + '\n}'

	  callGV(name)
	}
  }

  static getState(gdata, name) { 
	if (gdata && name) { 
	  for (s in gdata.states) { 
		if (name == s.id) return s
		if (s.subgraph) { 
		  def s0 = getState(s.subgraph, name)
		  if (s0) return s0
		}
	  }
	}
	return null
  }

  static graphBoday(data, g) { 
	if (data) { 
	  return data.states.collect { s -> 
		if (s.subgraph) { 
		  subgraph(s, g)
		} else { 
		  "${s.id}" + (s.label ? " [ label = <${s.label}> ]" : '') + ';' 
		}
	  }.join('\n\t') + '\n\t' + 
	  data.transitions.collect { t -> 
		def s0 = getState(g, t[0])
		def s1 = getState(g, t[1])
		String from = t[0]
		String to = t[1]
		String ltail = null
		String lhead = null
		if (s0?.subgraph) { 
		  ltail = 'cluster_' + from
		  //from = s0.subgraph.states[0].id
		  from = s0.subgraph.states.find { it.id != to }.id

		  //if (getState(s0.subgraph, to) != null) { // self loop			
		}
		if (s1?.subgraph) { 
		  lhead = 'cluster_' + to
		  //to = s1.subgraph.states[0].id
		  to = s1.subgraph.states.find { it.id != from }.id
		}
		def attr = []
		if (ltail) attr << "ltail = ${ltail}"
		if (lhead) attr << "lhead = ${lhead}"
		if (t[2]) attr << "label = \"${t[2]}\""
		"${from} -> ${to}" + (attr ? " [ ${attr.join(', ')} ]" : '' ) + ';' 
	  }.join('\n\t')
	}
	return null
  }
  
  static String subgraph(data, g) { 
	def subg = ''
	if (data && data.subgraph) { 
	  subg = """subgraph cluster_${data.id} {
	style = rounded;
	label = <${data.label}>;\n\t""" + graphBoday(data.subgraph, g) + '\n}'
	}
	return subg
  }

  static boolean isState(node) { 
	node && (Language.isTopView(node.nodeType) || Language.isState(node.nodeType))
  }

  static String getStateLabel(node) { 
	if (node) { 
	  String label = "<FONT FACE=\"Sans Serif\">${node.nodeType}:${node.id}</FONT>"
	  if (node.title) label += "<BR/>\"${node.title}\""
	  return label
	}
	return null
  }

  static String getTransitionLabel(node) { 
	if (node) { 
	  if (node.text) return node.text
	}
	return null
  }

  static getEnclosingState(node) { 
	while (node && !isState(node)) node = node.parent 
	return node
  }

  static hasSubstate(node) { 
	if (node) { 
	  return node.children.any { c -> isState(c) || hasSubstate(c) }
	}
	return false
  }

  static visitNodePre(node, data) { 
	info "[GraphViz] Visiting: ${node.id} (Pre) data=${data}"
	def cdata = data 
	if (isState(node)) {
	  String label = getStateLabel(node)
	  if (hasSubstate(node)) {
		cdata = [states: [], transitions: []]
		data.states << [ id: node.id, label: label, subgraph: cdata ]
	  } else { 
		data.states << [ id: node.id, label: label ]
	  }
	}
	if (node.next) { 
	  def es = getEnclosingState(node)
	  String from = es.id
	  String to = node.next
	  String label = getTransitionLabel(node)
	  data.transitions << [from, to, label] 
	}
	return cdata
  }

  static visitNodePost(node, data) { 
	info "[GraphViz] Visiting: ${node.id} (Post) data=${data}"

  }

  static callGV(String name) { 
	def command = "dot -Tpng -o${gvDir}/${name}.png ${gvDir}/${name}.gv"
	def p = command.execute()	
	p.waitFor()
  }

}