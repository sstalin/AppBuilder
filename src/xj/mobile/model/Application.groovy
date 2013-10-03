
package xj.mobile.model

import org.codehaus.groovy.ast.*

import xj.mobile.model.ui.View
import xj.mobile.model.ui.Widget

import xj.mobile.builder.Preprocessor

import static xj.mobile.model.ModelNode.getAttributeDescription

//
// represent the input source model 
//
class Application extends ModelNode { 

  Application() { 
	this['#type'] = 'app'
  }

  void add(ModelNode w) {
	if (!(w instanceof View) && w instanceof Widget) { 
	  View v = new View()
	  v['#type'] = 'View'
	  v['#line'] = w['#line']
	  v.id = Preprocessor.generateID('view')
	  v.add(w)
	  w = v
	}
   	super.add(w)
   	if (w instanceof View && !mainView) 
	  mainView = w
  }

  List getViews() { 
	children.findAll { n -> n instanceof View}
  }

  String print() { 
	String result = printMap(properties, 0, '\n') + '\n'

    int i = 0
    result += "children: size=${children.size()}"
    children.each { w -> 
      String pstr = "${i++}"
      result += ('\n=== child[' + pstr + '] ===\n' + w.print(1, '\n', pstr + '.'))
    }
    return result 
  }

  // collect attribute info
  def attributes() { 
    def attrSet = [] as Set 
    properties.each { key, value ->
      def attr = getAttributeDescription(key, value)
      if (attr) attrSet.add(attr)
    }
    def attrMap = [app: attrSet]
    
    views.each { w -> w.attributes(attrMap) }

    return attrMap
  }

  String getDefaultArgName(value = null) { 
	'name'
  }

}