package xj.mobile.model

import org.codehaus.groovy.ast.*

import xj.mobile.builder.ModelBuilder

import static xj.mobile.util.CommonUtil.encodeAttributeValue
import static xj.mobile.lang.ast.PrettyPrinter.*
import static xj.translate.Logger.info 

class ModelNode extends Expando { 

  // attributes with # prefix is internal, not settable by users 
  // #type: widget type 

  static final Null = new ModelNode('#NULL')

  ModelBuilder builder 

  List<ModelNode> children = []

  void add(ModelNode w) { 
    //println "add parent=${id} widget=${w}"
	if (w) { 
	  w.parent = this
	  children.add(w)
	}
  }

  ModelNode getChild(String id, boolean nested = false) { 
	def c = children.find { it.id == id }
	if (!c && nested) { 
	  for (w in children) { 
		if (w instanceof Composite) { 
		  c = w.getChild(id, true)
		  if (c) break
		}
	  }
	}
	return c 
  }

  String toString() { 
    String str = print()
	if (children?.size() > 0) { 
	  str += "\nchildren=${children}"
	}
	return str
  }

  List findAll(Closure cond) { 
    def result = []
    children.each { w -> 
      if (w instanceof Composite) { 
		result.addAll(w.findAll(cond))
      } else if (w instanceof ModelNode && cond(w)) { 
		result << w
      }      
    }
    return result 
  }

  void visit(closure) { 
	if (closure) {  
	  closure(this)
	  children.each { w -> 
		if (w instanceof Composite) { 
		  w.visit(closure)
		} else { 
		  closure(w)
		}
	  }
	}
  }

  void visit(pre, post, data) { 
	if (pre || post) {  
	  def cdata = data
	  if (pre) cdata = pre(this, data)
	  children?.each { w -> 
		w.visit(pre, post, cdata)
	  }
	  if (post) post(this, data)
	}
  }

  def methodMissing(String name, args) { 
	info "[ModelNode] ${this.class.name} methodMissing ${name} ${args}  ${this.hashCode()} (builder == null) ${this['builder'] == null} ${this.builder == null}"
	if (this['builder'] != null) { 
	  //builder.methodMissing(name, args)
	  this['builder'].methodMissing(this, name, args)
	} else { 
	  //info "[ModelNode] methodMissing ${name} builder == null"

	  println "${this.class.name} methodMissing ${name} ${args}"
	}
  }

  def getDesignAnnotation() { 
    this['@Design']
  }

  def getStyleAnnotation() { 
    this['@Style']
  }

  def getWidgetType() { 
	String wtype = this['#type']
	if (this['#subtype']) { 
	  wtype += ('#' + this['#subtype'])
	}
	return wtype
  }

  def getNodeType() { 
    this['#type']
  }

  String getModelClassName() { 
    String cname = null
    cname = getClass().name
    int i = cname.lastIndexOf('.')
    if (i >= 0) { 
      cname = cname.substring(i + 1)
    }
    return cname 
  }

  static tuples = [
	'latlon', 'span', 'size'
  ]

  static getTypeName(key, value) { 
    def tname = value?.class?.name
    if (value instanceof Closure) { 
      tname = 'Closure'
    } else if (value instanceof Map) { 
      def mtname = value.keySet().findAll{ !it.endsWith('.src') }.collect{ k -> "${k}:${getTypeName(k, value[k])}" }.join(',')
      tname = "Map<${mtname}>"
    } else if (value instanceof List) { 
	  if (key in tuples) { 
		def ctype = value.collect { v -> getTypeName(null, v) }
		def ctname = ctype.join(',')
		tname = "[${ctname}]"
	  } else { 
		def ctype = value.collect { v -> getTypeName(null, v) } as Set
		def ctname = ctype.join('|')
		tname = "List<${ctname}>"
	  }
    } else if (value instanceof GString) { 
      tname = 'String'
    } else if (tname) { 
	  if ((tname.startsWith('java.lang.') ||
		   tname.startsWith('java.util.') ||
		   tname.startsWith('java.math.'))) { 
		tname = tname[10 .. -1]
	  } else if (tname.startsWith('xj.mobile.model.properties.')) { 
		tname = tname[27 .. -1]
	  } else if (tname.startsWith('xj.mobile.model.')) { 
		tname = tname[16 .. -1]
	  }
    } 
    return tname
  }

  static String getValueString(value) { 
	if (value instanceof Closure) {
	  return '{closure}'
	} else if (value instanceof Map) { 
	  def sb = new StringBuffer('[')
	  boolean first = true
	  value.keySet().sort().each { k -> 
		if (!k.endsWith('.src')) { 
		  def v = getValueString(value[k])
		  if (first) first = false else sb << ';'
		  sb << "${k}:${v}"
		}
	  }
	  sb << ']'
	  return encodeAttributeValue(sb.toString())
	} else if (value instanceof List) { 
	  def sb = new StringBuffer('[')
	  boolean first = true
	  value.each { e -> 
		def v = getValueString(e)
		if (first) first = false else sb << ','
		sb << v
	  }
	  sb << ']'
	  return encodeAttributeValue(sb.toString())
	} else if (value instanceof String || value instanceof GString) {
	  return value
	} else { 
	  return encodeAttributeValue(value?.toString())
	}
  }

  static getAttributeDescription(key, value) { 
    if (key && value &&
		key != 'parent' && key != 'id' && key != 'builder' &&
		key[0] != '#' &&
		!key.endsWith('.src') &&
		!(value instanceof ModelNode)) {      
      return [ name: key, type: getTypeName(key, value), value: getValueString(value), class: value?.class?.name ]
    }
    return null
  }

  String getDefaultArgName(value = null) { 
	'text'
  }

  def setDefaultArg(arg) { 
    this[getDefaultArgName(arg)] = arg
  }

  String print(level = 0, sep = ', ', prefix = '') { 
    String indent = '' 
    if (level > 0) indent = ' ' * level
	String result = indent + "class=${modelClassName}" + sep + printMap(properties, level, sep)
	if (children?.size() > 0) { 
	  result += (sep + indent + "children: size=${children.size()}")
	  int i = 0 
	  children.each { w -> 
		String pstr = "${prefix}${i++}"
		result += ('\n' + indent + '=== child[' + pstr + '] ===\n' + w.print(level + 1, sep, pstr + '.'))
	  }
	}
	return result
  }

  String printMap(map, level = 0, sep = ', ') {
	if (map) { 
	  def pstr = []
	  map.each { key, value -> 
		if (value instanceof ModelBuilder) { 
		  // skip  
		} else if (value instanceof Closure) { 
		  pstr << "${key}=[${value.class}]"
		} else if (value instanceof ASTNode) { 
		  pstr << "${key}=\n${print(value, level + 1)}"
		} else if (value instanceof ModelNode) { 
		  pstr << "${key}=${value.id}"
		} else if (value instanceof List) { 
		  if (value.every { isSimpleType(it) }) { 
			pstr << "${key}=${value}" 
		  } else { 
			pstr << "${key}=\n${printList(value, level + 1, sep)}"
		  }
		} else if (value instanceof Map) { 
		  pstr << "${key}=\n${printMap(value, level + 1, sep)}"
		} else { 
		  //pstr << "${key}=${value}" + (value ? "  [${value.class.name}]" : '')
		  pstr << "${key}=${value}" + (value ? "  [${getTypeName(key, value)}]" : '')
		}
	  }
	  String indent = '' 
	  if (level > 0) indent = ' ' * level
	  indent + pstr.join(sep + indent) 
	} else { 
	  ''
	}
  }

  String printList(list, level = 0, sep = ', ') {
	if (list) { 
	  def pstr = []
	  list.eachWithIndex { value, i -> 
		if (value instanceof Closure) { 
		  pstr << "[${i}]=[${value.class}]"
		} else if (value instanceof ASTNode) { 
		  pstr << "[${i}]=\n${print(value, level + 1)}"
		} else if (value instanceof ModelNode) { 
		  pstr << "[${i}]=${value.id}"
		} else if (value instanceof List) { 
		  if (value.every { isSimpleType(it) }) { 
			pstr << "${key}=${value}" 
		  } else { 
			pstr << "${key}=\n${printList(value, level + 1, sep)}"
		  }
		  //pstr << "[${i}]=\n${printList(value, level + 1, sep)}"
		} else if (value instanceof Map) { 
		  pstr << "[${i}]=\n${printMap(value, level + 1, sep)}"
		} else { 
		  pstr << "[${i}]=${value}" // [${value.class}]"
		}
	  }
	  String indent = '' 
	  if (level > 0) indent = ' ' * level
	  indent + pstr.join(sep + indent) 
	} else { 
	  ''
	}
  }


  // collect attribute info
  def attributes(attrMap) {
	if (widgetType) { 
	  def attrSet = null
	  if (attrMap) { 
		attrSet = attrMap[widgetType]
	  }
	  if (attrSet == null) { 
		attrSet = [] as Set
	  }

	  properties.each { key, value -> 
		def attr = getAttributeDescription(key, value)
		if (attr) attrSet.add(attr)
	  }

	  attrMap[widgetType] = attrSet

	  if (children) { 
		children.each { w -> w.attributes(attrMap) }
	  }
	}
    return attrMap
  }

  ModelNode() { }
  ModelNode(type) { 
	this['#type'] = type
  }

  static copyProperties(ModelNode from, ModelNode to) { 
    if (from && to) { 
      from.properties.each { key, value ->
		if (!(key in ['#type', 'parent']))
		  to.properties[key] = value
      }
    }
  }

  static boolean isSimpleType(v) { 
	if (v) { 
	  return (v instanceof String || v instanceof Number || v instanceof Boolean)
	}
	return true
  }

}