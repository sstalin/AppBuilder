
package xj.mobile.builder

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.control.*
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.*
import org.codehaus.groovy.ast.builder.AstBuilder

import xj.mobile.lang.*
import xj.mobile.model.*
import xj.mobile.model.ui.*

import static xj.mobile.lang.ast.Util.* 
import static xj.mobile.lang.ast.PrettyPrinter.*
import static xj.translate.Logger.info 

  //////////////////////////////////////////////////////////////////////////////////////////
  //
  //  Analyze data dependencies among widgets 
  //
  //  * use set of exp: dependent on a) local vars; or b) other widgets  
  //
  //
  //////////////////////////////////////////////////////////////////////////////////////////


class DependencyAnalyzer {  

  @Delegate
  ModelBuilder builder

  static verbose = true //false

  DependencyAnalyzer(ModelBuilder builder) { 
	this.builder = builder
  }

  def analyzeExpression(owner, key, src) { 
    if (src) { 
      def useSet = getUseVarSet(src)
      if (verbose) info "[DependencyAnalyzer] useSet: ${useSet}"
      owner[key + '.src'] = [ code: src, useSet: useSet ]
      if (useSet) { 
		def view = findTopView(owner)
		if (view) { 
		  Set uset = view['#info'].useSet
		  if (uset == null) 
			uset = [] as Set
		  uset.addAll(useSet)
		  view['#info'].useSet = uset
		}

		useSet.each { name -> 
		  def vd = findDeclarationInView(name) 
		  if (vd) { // dependency on a local variable 
			def decl = vd[1]
			if (!decl.isDummy) { // ignore dummy var of an entity collection
			  if (decl.updates == null) decl.updates = [] as Set
			  if (verbose) info "[DependencyAnalyzer] update: ${owner.id} ${key}"
			  decl.updates << [ owner.id, key ] 
			}
		  }
		}
      }
    }
  }

  def analyzeAction(owner, key, closure, src) { 
    if (closure && src) { 
      if (verbose) info '[DependencyAnalyzer] actionCode:\n' + print(src.code, 2)
      //def writer = new StringWriter()
      //c.code.visit new groovy.inspect.swingui.AstNodeToScriptVisitor(writer)
      //println writer

      def defSet = getDefVarSet(src)
      def useSet = getUseVarSet(src)
      if (verbose) info "[DependencyAnalyzer] closure defSet: ${defSet}  useSet: ${useSet}" 
      //def declSet = findDeclSet(defSet)

	  // whether the action involves reading/writing an entity collection 
      def readEntity = false
      def writeEntity = false
      useSet.each { name ->
		def vd = findDeclarationInView(name) 
		if (vd) { 
		  def decl = vd[1]
		  if (decl.isDummy) readEntity = true 
		}
      }
      defSet.each { name ->
		def vd = findDeclarationInView(name) 
		if (vd) { 
		  def decl = vd[1]
		  if (decl.isDummy) writeEntity = true 
		}
      }
      owner[key + '.src'] = [ code: src, 
							  defSet: defSet, 
							  useSet: useSet, 
							  decl: findDeclSet(defSet), // declarations of all def vars
							  readEntity: readEntity,
							  writeEntity: writeEntity ]

      //decl: declSet.findAll { !it.isDummy} ]
    }
  }

  // return the top view that contains the widget
  static findTopView(widget) { 
	while (widget && 
		   !Language.isTopView(widget.nodeType)) { 
	  widget = widget.parent 
	}
	return widget
  }

  // return the [ viewEntry, declaration ] pair of the named local variables
  def findDeclarationInView(String varname) { 
    if (varname) { 
      int n = viewStack.size()
      for (int i = n - 1; i >= 0; i--) { 
		def viewEntry = viewStack[i]
		if (viewEntry.declarations) { 
		  def decl = viewEntry.declarations[varname]
		  if (decl) { 
			return [ viewEntry, decl ]
		  }
		}
      }
    }
    return null
  }

  def findDeclSet(names) { 
    def decl = [] as Set
    if (names) { 
      names.each { name -> 
		def vd = findDeclarationInView(name) 
		if (vd) decl << vd[1]
      }
    }
    return decl
  }

  //
  // post-process model
  // traverse model: 
  //   * analyze data dependency among widgets 
  //
  void postProcess(app) { 
	if (app) { 
	  app.views.each { view -> 
		view.visit({ w -> 
		  if (verbose) info "[DependencyAnalyzer] postProcess() visit widget ${w.id}"
		  w.properties.each { name, value -> 
			if (name.endsWith('.src') && 
				name != 'action.src' &&
				name != 'selection.src' &&
				value instanceof Map) { 
			  def useSet = value.useSet
			  if (verbose) info "       process widget ${w.id} ${name}: useSet=${useSet}"
			  useSet.each { var -> 
				def wdef = view.getChild(var, true)
				if (wdef) { 
				  if (verbose) info "         found widget ${wdef.id}"
				  def srcInfo = wdef['action.src']
				  if (!srcInfo) { 
					srcInfo = wdef['action.src'] = [ updates: [] as Set ]
				  }
				  if (!srcInfo['updates']) { 
					srcInfo['updates'] = [] as Set 
				  }
				  srcInfo['updates'] << [ w.id, name[0 .. -5] ]

				  /*
				  if (!wdef.'#updates')  
					wdef.'#updates' = [] as Set
				  wdef.'#updates' << [ w.id, name[0 .. -5] ]
				  */
				}
			  }
			}
		  }
		})
	  }
	}
  }


}