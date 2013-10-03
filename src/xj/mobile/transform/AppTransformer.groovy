package xj.mobile.transform

import xj.mobile.model.Application
import xj.mobile.model.ModelNode
import xj.mobile.model.ui.View
import xj.mobile.model.ui.Widget
import xj.mobile.builder.ModelBuilder
import static xj.translate.Logger.info 

class AppTransformer {  
  
  static transformers = [
    'common' :  new CommonAppTransformer(),

    'ios' :     new IOSAppTransformer(),
    'android' : new AndroidAppTransformer()
  ]

  static AppTransformer getAppTransformer(name) { 
    return transformers[name.toLowerCase()];
  }

  @Delegate
  ModelBuilder builder = new ModelBuilder()

  AppTransformer() { 
    init()
  }

  void init() { 
  }

  void transform(Application app) {
    app?.views?.each { v -> transformSubtree(v) }
  }

  ModelNode transformSubtree(ModelNode node) {
    info "[AppTransformer] transformView ${node.id}"

    def rules = getTransformationRules(node)
    if (rules) { 
      for (rule in rules) { 
		if (matchRuleCondition(node, rule[0])) { 
		  rule[1](node)
		}
      }
    }

    ListIterator iter = node?.children?.listIterator() 
    while (iter.hasNext()) { 
	  def w = iter.next()
      def result  = null
	  if (w instanceof ModelNode) { 
		if (w.children?.size() > 0) { 
		  result = transformSubtree(w)
		} else { 
		  result = transformLeaf(w)
		}
	  } else {
		result = w
      }
      if (result == ModelNode.Null) { 
		//info "[AppTransformer] transformView result==null"

		iter.remove()
      } else if (result instanceof ModelNode) { 
		//info "[AppTransformer] transformView result==Widget ${result.id}"

		if (result != w) { 
		  result.parent = node
		  iter.set(result)
		}
      } else if (result instanceof List &&
				 result.every{ it instanceof ModelNode }) { 
		//info "[AppTransformer] transformView result==List"

		iter.remove()
		result.each { wt -> 
		  //info "[AppTransformer] transformView result=List: ${wt.class}"
		  wt.parent = node
		  iter.add(wt)
		}
      }
    }
    return node
  }

  def transformLeaf(ModelNode node) {
    info "[AppTransformer] transformWidget ${node.nodeType} ${node.id}"

    if (node) {
      def rules = getTransformationRules(node)
      if (rules) { 
		for (rule in rules) { 
		  if (matchRuleCondition(node, rule[0])) { 
			def result = rule[1](node)
			if (result == null || 
				result instanceof ModelNode ||
				result instanceof List && result.every{ it instanceof ModelNode }) { 
			  return result
			} else { 
			  return node
			}
		  }
		}
      }
    }
    return node
  }

  def getTransformationRules(ModelNode node) {  
    return transformationRules[node?.nodeType]
  }

  // rule: [condition, rhs]
  def transformationRules = []

  boolean matchRuleCondition(ModelNode node, cond) { 
    if (node) { 
      if (cond instanceof Boolean) { 
		//println "[matchWidget] cond is Boolean"
		return cond
      } else if (cond instanceof Closure) {
		//println "[matchWidget] cond is Closure"
		return cond(node) // as Boolean 
      } else if (cond instanceof Map) { 
		//println "[matchWidget] cond is Map"
		return cond.every { key, value -> 
		  if (key == 'design') { 
			def design = node['@Design:Android']
			if (design == null || design['type'] != value) 
			  return false 
		  }
		  return true
		}
      }
    }
    return false
  }

  static widgetProperties(widget) { 
    if (widget) { 
      def result = widget.properties
      result.keySet().removeAll([ '#type', 'parent' ] as Set)
      return result 
    }
    return null
  }

  // change attribute names in prop, retain values 
  // pmap: a map of oldName:newName
  //   for action and selection also map oldName.src to newName.src 
  static mapProperties(prop, pmap) { 
    if (prop && pmap) { 
      pmap.each { k, v ->
		def pnames = [:]
		pnames[k] = v
		if (k in [ 'action', 'selection' ])
		  pnames[k + '.src'] = (v + '.src')
		pnames.each { k1, v1 -> 
		  info "[AppTransformer] mapProperties: ${k1}:${v1}"

		  if (prop[k1] != null) { 
			prop[v1] = prop[k1]
			if (prop instanceof Map) { 
			  prop.remove(k1)
			} else if (prop instanceof Expando) { 
			  prop.properties.remove(k1)
			} else { 
			  prop[k1] = null
			}
		  }
		}
      }
    }
  }

  static mapWidgetProperties(Widget widget, pmap) { 
	if (widget) { 
	  pmap.each { k, v ->
		if (widget[k] != null) { 
		  widget[v] = widget[k]
		  widget.properties.remove(k)
		}
	  }
	}
	return widget
  }

}