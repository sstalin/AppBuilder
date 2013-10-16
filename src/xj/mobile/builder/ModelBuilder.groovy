
package xj.mobile.builder

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.control.*
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.*
import org.codehaus.groovy.ast.builder.AstBuilder

import xj.mobile.lang.*
import xj.mobile.model.*
import xj.mobile.model.sm.*
import xj.mobile.model.ui.*
import xj.mobile.model.graphics.*
import xj.mobile.model.properties.Duration

import static xj.mobile.lang.ast.Util.* 
import static xj.translate.Logger.info 

import static xj.mobile.model.PropertyInterpreter.interpretAttributeValue

/*
 * Build the app model tree 
 *
 */
class ModelBuilder { 

  static verbose = true //false

  static { 
	// handle n.second n.ms
	Integer.metaClass.getSecond = { -> new Duration(delegate, Duration.Unit.Second) }
	Integer.metaClass.getMs = { -> new Duration(delegate, Duration.Unit.Milli_Second) }
	BigDecimal.metaClass.getSecond = { -> new Duration((float)delegate, Duration.Unit.Second) }
  }

  @Delegate
  ErrorMessages errors = new ErrorMessages()

  DependencyAnalyzer analyzer

  ModelBuilder() { 
    info "[ModelBuilder] verbose=${verbose}"
	analyzer = new  DependencyAnalyzer(this)
  }

  Closure init = null

  def viewStack = []
  def parent = null
  def plist = []
  int pos = 0

  void reInit() { 
    init = null

    parent = null
    plist = []
    pos = 0
  }

  def getAppDefLineNumber() { 
    Throwable t = new Throwable()
    //t.printStackTrace()
    getAppDefLineNumber(t)
  }

  def getAppDefLineNumber(Throwable t) { 
    if (t) { 
      for (StackTraceElement ste in t.stackTrace) { 
		if (ste.fileName == 'app.groovy') { 
		  return ste.lineNumber
		}
      }
    }
    return 0
  }

  boolean isListEntityClass(String classname) { 
    def entities = parent['#entities'] 
    if (entities) { 
      info "[ModleBuilder] isListEntityClass() ${classname} entities: ${entities.collect{it['class']}}"
      for (e in entities) { 
		if (e['class'].name == classname) 
		  return true
      }
    }
    return false
  }

  def methodMissing(String name, args) { 
	methodMissing(null, name, args)
  }

  def methodMissing(ModelNode parentNode, String name, args) { 
    def curLine = appDefLineNumber
	def curParent = parentNode ?: parent 
    if (verbose) info "[ModelBuilder] methodMissing ${name} ${curLine} parent: ${parent?.'#type'}"

    if (name == 'ListEntity') { 
      info "[ModelBuilder] methodMissing ${args}"
	  // args[0] is the map of arguments, or the entity class name 
	  def map = (args[0] instanceof Map) ? args[0] : [class: args[0]]
      def entity = new ListEntity(this, map) 
      if (curParent['#entities']) { 
		curParent['#entities'] << entity
      } else { 
		curParent['#entities'] = [ entity ]
      }
      info "[ModelBuilder] entity dummy ${entity.'_dummy_'}"
      def viewDecl = viewStack[-1].declarations
      info "[ModelBuilder] viewDecl ${viewDecl}"
      for (vname in viewDecl.keySet()) { 
		def decl = viewDecl[vname]
		//info "[ModelBuilder] decl.line ${decl.line}"
		if (decl.line == curLine) { 
		  decl['entity'] = entity
		  entity['_name_'] = vname
		  info "[ModelBuilder] entity name ${vname}"
		  break
		}
      }
      return entity
    }

	boolean ui = Language.isUI(name)
	boolean transition = Language.isTransition(name)
	boolean state = Language.isState(name)
	boolean action = Language.isAction(name)
	boolean graphics = Language.isGraphics(name)
	
    if (ui || transition || state || action || graphics) { 
      int myPos = pos      
      if (name != 'app') plist << pos
      pos = 0

	  // handle views/widgets
	  def viewEntry = Preprocessor.viewMap.getViewInfo(name, curLine)
	  
	  if (!Language.isTransition(name)) { 
		if (viewEntry) {  
		  viewStack << viewEntry
		} else { 
		  if (verbose) info "+++ View ${name}:${curLine} not found in viewMap"
		  //info "    viewMap: ${Preprocessor.viewMap.keys()}"
		}

		def views = viewStack.collect{ it.name }.join(', ')
		if (verbose) info "[ModelBuilder] Method ${name} ${curLine} ${plist.join('.')} [${views}]"
	  }

	  def owner    
	  if (transition) { 
		owner = new Transition()
	  } else if (state) { 
		owner = new State()
	  } else if (action) { 
		owner = new Action()
	  } else if (graphics) { 
		if (Language.isShape(name)) { 
		  owner = new Shape()
		} else if (Language.isPathElement(name)) { 
		  owner = new PathElement()
		} else if (name == 'Path') { 
		  owner = new Path()
		} 
	  } else if (Language.isView(name) || 
				 Language.isComposite(name)) { 
		owner = new View()
	  } else { 
		owner = new Widget()
	  }
	  owner['#type'] = name  // set widgetType 
	  if (Language.Aliases[name]) { 
		owner['#type-original'] = name
		owner['#type'] = Language.Aliases[name]
	  }
	  owner['#pos'] = plist.join('.')
	  owner['#info'] = viewEntry
	  owner['#line'] = curLine
	  owner.builder = this

	  if (verbose) info "[ModelBuilder] parent: ${parent?.id}  child: ${name}"
	  curParent?.add(owner)

	  if (init) { 
		init(owner)
	  }
    
	  def closure = null
	  if (args.length > 0 && args[-1] instanceof Closure) { 
		closure = args[-1]
	  }
	  def prevParent = parent
	  if (transition || action) { 
		handleParameters(owner, args)
		handleParameter(owner, '#action', closure)
	  } else { 
		handleChildren(owner, args, closure)
	  }
	  parent = prevParent

	  if (viewEntry) { 
		viewStack.pop()
	  }	  

      if (plist) plist.pop()
      pos = myPos
      pos++;

	  //if (verbose) info "[ModelBuilder] methodMissing(): @end ${owner.hashCode()} (owner.builder == null): ${owner.builder == null}"
      return owner
    } else { 
      return null
    }
  }

  def handleChildren(owner, args, closure) { 
    handleParameters(owner, args)
    if (closure) { 
	  parent = owner 
      closure.delegate = this
      try { 
		closure() // call method body
      } catch (MissingPropertyException e) { 
		info "[ModelBuilder] MPE: ${e.type.name}"
		if (isListEntityClass(e.type.name)) { 
		  int line = getAppDefLineNumber(e) - 2
		  errorMessages << "Line ${line}: " + e.message
		} else { 
		  throw e
		}
      }

	  if (owner instanceof Application) { 
		analyzer.postProcess(owner)
	  }
    }
    return owner
  }

  // groovy hook for processing undefined property 
  def propertyMissing(String name) { 
    if (verbose) info "[ModelBuilder] propertyMissing(): ${name}"
	if (name in [ 'data', 'previous' ]) { 
	  // treated as special var name 
	  return name
	} else { 
	  return new PropertyModel(name)
	}
	//return name
  }

  // groovy hook for processing undefined property setters  
  def propertyMissing(String name, value) { 
    if (verbose) info "[ModelBuilder] propertyMissing(): ${name} = ${value}"


  }

  // context sensitive value for a given key
  def builtinValueForKey(String key, String value) { 
    
  }

  def handleParameters(owner, args) { 
    if (verbose) info "[ModelBuilder] handleParameters(): args = ${args}  [${args.class.name}]"
	//(owner.builder == null): ${owner.builder == null}"
	if (!args.any{ it instanceof Map } && args.size() > 1) { 
	  def val = args as List
	  String dkey = owner.getDefaultArgName(val)
	  handleParameter(owner, dkey, val)
	} else { 
	  for (def a in args) { 
		if (a instanceof Map) { 
		  if (a['id']) { 
			owner['id'] = a['id'] 
		  } else { 
			owner['id'] = Preprocessor.generateID(owner.widgetType ?: 'app')
		  }
	
		  a.each { key, value -> 
			if (value instanceof Class) { 
			  value = propertyMissing(value.simpleName)
			}
			handleParameter(owner, key, value)
		  }
		} else if (!(a instanceof Closure)) { 
		  String dkey = owner.getDefaultArgName(a)
		  handleParameter(owner, dkey, a)
		}
	  }
	}

    if (!owner['id']) { 
      owner['id'] = Preprocessor.generateID(owner.widgetType ?: 'app')
    }
  }

  def handleParameter(owner, key, value) { 
    def actionMap = Preprocessor.actionMap
    def viewExp = null
    def viewDecl = null
    if (viewStack) { 
      viewExp = viewStack[-1].expressions
      viewDecl = viewStack[-1].declarations
    }

	def pvalue = interpretAttributeValue(owner.widgetType, key, null, value)
	if (pvalue != null) {
	  value = pvalue
	} else if (value instanceof Map) { 
	  value.each { k, v -> 
		if (k && k[0] != '#') { // prevent setting internal attributes 
		  if (v instanceof Closure) { 
			def cmeta = v.metaClass.classNode
			def ckey = Preprocessor.CID_PREFIX + "${cmeta.lineNumber}:${cmeta.columnNumber}" //owner['#pos']
			def src = actionMap["${ckey}.${key}.${k}"]
			if (verbose) info "[ModelBuilder] retrive actionMap[${ckey}.${key}.${k}]"
			analyzer.analyzeAction(owner, "${key}.${k}", v, src)
		  } else { 
			pvalue = interpretAttributeValue(owner.widgetType, key, k, v)
			if (pvalue != null) { 
			  value[k] = pvalue
			} else if (viewExp) { 
			  def src = viewExp["${key}.${k}"] 
			  analyzer.analyzeExpression(owner, "${key}.${k}", src)
			}
		  }
		}
	  }
	} else if (value instanceof List) { 
	  value.eachWithIndex { exp, i ->  
		if (exp instanceof Map) { 
		  def exp1 = [:]
		  exp.each { k, v -> 
			if (v instanceof Closure) { 
			  def cmeta = v.metaClass.classNode
			  def ckey = Preprocessor.CID_PREFIX + "${cmeta.lineNumber}:${cmeta.columnNumber}" //owner['#pos']
			  def src = actionMap["${ckey}.${key}.${i}.${k}"]
			  if (verbose) info "[ModelBuilder] retrive actionMap[${ckey}.${key}.${i}.${k}]"
			  analyzer.analyzeAction(exp1, k, v, src)
			} else { 
			  pvalue = interpretAttributeValue(owner.widgetType, key, k, v)
			  if (pvalue != null) { 
				exp[k] = pvalue
			  } else if (viewExp) { 
				def src = viewExp["${key}.${i}.${k}"] 
				analyzer.analyzeExpression(exp1, k, src)
			  }
			}
		  }
		  if (exp1) { 
			exp << exp1
		  }

		}
	  }	    
	} else if (viewExp) { 
	  def src = viewExp[key] 
	  if (verbose) info "[ModelBuilder] retrieve exp source for ${key}: ${src?.text}"
	  analyzer.analyzeExpression(owner, key, src)
	}

	owner[key] = value
	if (verbose) info "[ModelBuilder] handleParameter(): Parameter: key=${key}" + 
	                  (!(value instanceof Closure)? " value=${value}" : '' ) +  " : ${value?.class?.name}"
		  
	if (value instanceof Closure) { 
	  def cmeta = value.metaClass.classNode
	  if (cmeta) { 
		def ckey = Preprocessor.CID_PREFIX + "${cmeta.lineNumber}:${cmeta.columnNumber}" 
		def src = actionMap["${ckey}.${key}"]
		if (verbose) info "[ModelBuilder] retrive actionMap[${ckey}.${key}]"
		analyzer.analyzeAction(owner, key, value, src)
	  }
	}
  }

}