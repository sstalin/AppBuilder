
package xj.mobile.builder

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.control.*
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.*
import org.codehaus.groovy.ast.builder.AstBuilder

import xj.mobile.lang.Language

import static xj.translate.Logger.info 

class Preprocessor {  

  static verbose = false //true 

  // Sanity check
  //   1. uniqueness of id
  //          (within a top view, not completed)  
  //   2. id must be a legal var name 
  //
  // Save source code 
  //   1. closure source to actionMap 
  //   2. declaration source to viewMap['declaration']
  //   3. non-constant expression source to viewMap['expression'] 
  //
  static boolean checkViewBuilder(Statement code, String filename, def errors = null) { 
    info "[Preprocessor] checkViewBuilder verbose=${verbose}"
    viewMap = new ViewMap()
    actionMap = [:]
    idSet = [] as Set

    def visitor = new ViewVisitor(filename)
    code.visit(visitor)
    
    if (verbose) { 
      actionMap.keySet().sort().each { 
		info "[Preprocessor] actionMap: ${it}"
      }
      viewMap.keys()
    }
 
    if (errors != null) { 
      errors.addAll(visitor.errorMessages)
    }
    return visitor.okay
  }

  static CID_PREFIX = '__$closure$__'
  static VID_PREFIX = '__$view$__'
  static EID_PREFIX = '__$expression$__'

  static KEYWORDS = [ 'true', 'false', 'null', 'class' ] as Set

  static ViewMap viewMap
  static Map actionMap
  static Set idSet

  static reset() { 
	idSet.removeAll()
	//viewMap.clear()
	//actionMap.clear()
  }

  static getValue(obj) { 
    if (obj) { 
      if (obj.hasProperty('value')) { 
		return obj.value
      } else if (obj instanceof VariableExpression) { 
		return obj.name
      }
    }
    return null
  }

  static boolean isIdentifier(ident) { 
    return ident && (ident ==~ /[a-zA-Z_][a-zA-Z_0-9]*/) && !(ident in KEYWORDS)
  }

  static boolean isIDUsed(name) { 
    name && !(name in idSet)
  }

  static String getID(name) { 
	if (name && !(name in idSet)) 
	  name 
	else
	  generateID(name)
  }

  static String generateID(name, idSet = null) { 
    if (idSet == null)
      idSet = Preprocessor.idSet

    if (!name) name = 'widget'
    String prefix = "${name[0].toLowerCase() + name[1 .. -1]}"
    int i = 1
    def id = "${prefix}${i}"
    while (id in idSet) { 
      id = "${prefix}${++i}"
    }
    idSet << id
    return id
  }

  static class ViewVisitor extends CodeVisitorSupport { 

    String filename
    def errorMessages = []

    // structural positions for view def, from top to current view (leaf)
    def plist = []
    int pos = 0

    ViewVisitor(filename) { 
      this.filename = filename
    }

    /*
    void visitClass(ClassNode node) { 
      info "[Preprocessor] visit class ${node.name}"
    }
    */

    void visitMethodCallExpression(MethodCallExpression call)  {
      String method = call.methodAsString
      def args = call.arguments
      boolean uinode = Language.isUI(method)
      boolean transition = Language.isTransition(method)
      boolean state = Language.isState(method)
      boolean action = Language.isAction(method)

      int myPos = pos       
      if (uinode || transition || state || action) { 
		//def vkey = VID_PREFIX + "${method}:${call.lineNumber}"
		if (method != 'app') plist << pos
		if (verbose) { 
		  info "[Preprocessor] visit ${method} ${plist.join('.')}"
		  info "[Preprocessor]       method ${call.lineNumber}:${call.columnNumber} - ${call.lastLineNumber}:${call.lastColumnNumber}"
		  info "[Preprocessor]       args ${args.lineNumber}:${args.columnNumber} - ${args.lastLineNumber}:${args.lastColumnNumber}"
		}
		pos = 0

		def viewExp = [:]   // expression src in the view  
		def viewDecl = [:]  // local var declared in the view
		def viewEntry = [:] // contains viewExp and viewDecl

		if (transition || state || action) { 
		  processViewAttributes(call, null)
		  processAction(call)
		} else { 
		  processViewAttributes(call, viewExp)
		  processViewBody(call, viewDecl)

		  def lines = [args.lineNumber, args.lastLineNumber]
		  if (lines[0] <= 0) { 
			lines = [call.lineNumber, call.lastLineNumber]
		  }
		  viewMap.putViewInfo(method, lines, viewEntry)
		  viewEntry['name'] = method
		  if (viewExp) { 
			viewEntry['expressions'] = viewExp
		  }
		  if (viewDecl) { 
			viewEntry['declarations'] = viewDecl
		  }
		}
      }

      super.visitMethodCallExpression(call)

      if (uinode || transition) { 
		if (plist) 
		  plist.pop()
		pos = myPos
		pos++;
      }
    }

    void processViewAttributes(call, viewExp) { 
	  def arg = call.arguments.expressions[0]
      if (arg instanceof MapExpression) { 
		def map = call.arguments.expressions[0]
		String id = null  // specified id 
		map.mapEntryExpressions.each { entry -> 
		  def key = getValue(entry.keyExpression)
		  def value = entry.valueExpression
		  if (key == 'id') {   	     
			id = getValue(value)
			checkID(id, location(value))
		  } else { 
			processViewAttrKeyValue(key, value, viewExp)
		  }
		}
      } else if (!(arg instanceof ClosureExpression)) { 
		processViewAttrKeyValue('text', arg, viewExp)
	  }
    }

	void processViewAttrKeyValue(key, value, viewExp) { 
	  if (verbose) info "[Preprocessor]   key=${key}  value=${value}"
		 
	  if (value instanceof ClosureExpression) { 
		def ckey = CID_PREFIX + "${value.lineNumber}:${value.columnNumber}"
		if (verbose) info "[Preprocessor] add actionMap[${ckey}.${key}]"
		actionMap["${ckey}.${key}"] = value
	  } else if (value instanceof MapExpression) { 
		value.mapEntryExpressions.each { e -> 
		  def k = getValue(e.keyExpression)
		  def v = e.valueExpression
		  if (v instanceof ClosureExpression) {
			def ckey = CID_PREFIX + "${v.lineNumber}:${v.columnNumber}" 
			if (verbose) info "[Preprocessor] add actionMap[${ckey}.${key}.${k}]"
			actionMap["${ckey}.${key}.${k}"] = v 
		  } else if (!(v instanceof ConstantExpression) && 
					 viewExp != null) { 
			if (verbose) info "[Preprocessor] add expression [${key}.${k}]"
			viewExp["${key}.${k}"] = v
		  }
		}
	  } else if (value instanceof ListExpression) { 
		value.expressions.eachWithIndex { exp, i -> 
		  if (exp instanceof MapExpression) { 
			exp.mapEntryExpressions.each { e -> 
			  def k = getValue(e.keyExpression)
			  def v = e.valueExpression
			  if (v instanceof ClosureExpression) {
				def ckey = CID_PREFIX + "${v.lineNumber}:${v.columnNumber}" 
				if (verbose) info "[Preprocessor] add actionMap[${ckey}.${key}.${i}.${k}]"
				actionMap["${ckey}.${key}.${i}.${k}"] = v 
			  } else if (!(v instanceof ConstantExpression) && 
						 viewExp != null) { 
				if (verbose) info "[Preprocessor] add expression [${key}.${i}.${k}]"
				viewExp["${key}.${i}.${k}"] = v
			  }
			}
		  }
		}
	  } else if (!(value instanceof ConstantExpression) && 
				 viewExp != null) { 
		if (verbose) info "[Preprocessor] add expression [${key}]"
		viewExp[key] = value //[ name: key, src: value ] 
	  }
		  
	}

    void checkID(id, loc) { 
      if (id) { 
		if (verbose) info "[Preprocessor]   id=${id}"
		if (id in idSet) { 
		  def msg = "${filename} ${loc}: duplicate id: ${id}"
		  errorMessages << msg
		  println "[Error] ${msg}"
		} else {  
		  if (isIdentifier(id)) { 
			idSet << id
		  } else { 
			def msg = "${filename} ${loc}: illegal id: ${id}"
			errorMessages << msg
			println "[Error] ${msg}"
		  }
		}
      } else { 
		def msg = "${filename} ${loc}: id must be a string"
		errorMessages << msg
		println "[Error] ${msg}"
      }
    }

    void processViewBody(call, viewDecl) { 
      def args = call.arguments.expressions
      if (args.size() > 0) { 
		def last = args[-1]
		if (last instanceof ClosureExpression) { 
		  if (verbose) info "[Preprocessor] process view body"
		  def body = last.code
		  if (body instanceof BlockStatement) { 
			// find decl in code
			for (s in body.statements) { 
			  if (s instanceof ExpressionStatement &&
				  s.expression instanceof DeclarationExpression) { 
				def var = s.expression.variableExpression 
				def t = var.type
				t = ClassHelper.getUnwrapper(t)
				if (verbose) info "[Preprocessor] process decl ${s.expression.text} type: ${t} isPrimitive: ${ClassHelper.isPrimitiveType(t)}\n\t${s}"
				viewDecl[var.name] = [src: s.expression, line: s.lineNumber ]
			  }
			}
		  }	  
		}
      }
    }

    void processAction(call) { 
      def args = call.arguments.expressions
      if (args.size() > 0) { 
		def last = args[-1]
		if (last instanceof ClosureExpression) { 
		  if (verbose) info "[Preprocessor] process action"
		  processViewAttrKeyValue('#action', last, null)
		}
	  }
	}

    boolean isOkay() { 
      errorMessages.size() == 0
    }

    def location(node) { 
      [ node.lineNumber - xj.mobile.Main.SCRIPT_HEADER_LINE, node.columnNumber ]
    }

  }

}