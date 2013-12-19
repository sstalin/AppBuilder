
package xj.mobile.builder

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.*

import static xj.translate.Logger.info 

class ViewMap { 

  def vmap = [:]

  def putViewInfo(name, lines, info) {
	def views = vmap[name]
	if (!views) { 
	  views = []
	  vmap[name] = views
	}
	views << [lines, info]
  }

  def getViewInfo(name, line) { 
	def result = null
	def views = vmap[name]
	if (views) { 
	  for (v in views) {  
		if (line >= v[0][0] && line <= v[0][1]) { 
		  result = v[1]
		  break
		}
	  }
	}
	return result 
  }

  def keys() { 
	vmap.each { k, v ->
	  v.each { vi -> 
		info "\t[ViewMap] ${k} ${vi[0]}"
	  }
	}
  }

  void clear() { 
	vmap.clear()
  }

}