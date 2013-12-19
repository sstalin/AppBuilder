package xj.mobile.model.impl

import xj.mobile.common.AppGenerator
import xj.mobile.codegen.Delegate
import xj.mobile.model.properties.ModalTransitionStyle

import xj.mobile.codegen.CodeGenerator
import xj.mobile.codegen.CodeGenerator.InjectionPoint
import static xj.mobile.codegen.CodeGenerator.InjectionPoint.*

import static xj.mobile.util.CommonUtil.*
import static xj.mobile.common.ViewProcessor.*
import static xj.mobile.codegen.templates.IOSDelegateTemplates.*
import static xj.translate.Logger.info 

//
// represent a class in iOS   
//
class IOSClass extends ClassModel { 

  List<String> frameworks = []

  List<String> classDecls = []
  List<String> propertyNames = []

  List<String> delegates = []
  Map<String, Delegate> delegateActions = [:]

  String classExtensionScrap = ''
  String ivarDeclScrap = ''
  String propertyScrap = ''
  String methodDeclarationScrap = ''

  String projectInfoScrap = ''

  IOSClass(String name, String superClassName) { 
	this.name = name
	this.superClassName = superClassName
  }

  //
  // Accessing scraps 
  //

  String getSystemImportScrap() {
    if (systemImports) { 
      return systemImports.unique().sort().collect { f -> "#import ${f}\n" }.join()
    }
    return ''
  }

  String getImportScrap() {
    if (imports) { 
      return imports.unique().sort().collect { f -> "#import ${f}\n" }.join()
    }
    return ''
  }

  String getClassDeclarationScrap() {
    if (classDecls) { 
      return classDecls.unique().sort().collect { f -> "@class ${f};\n" }.join()
    }
    return ''
  }

  String getSynthesizerScrap() {
	boolean onePerLine = true
    if (propertyNames) { 
	  if (onePerLine) { 
		return propertyNames.sort().collect { "@synthesize ${it};" }.join('\n')
	  } else { 
		return '@synthesize ' + propertyNames.sort().join(', ') + ';'
	  }
    } else { 
      return ''
    }
  }
  
  String getIvarDeclarationScrap() { 
	if (ivarDeclScrap) { 
	  return """{
${indent(ivarDeclScrap)}
}
"""
	}
	return ''
  }

  String getSuperClassClause() { 
    String delegateStr = ''
    if (delegates) { 
      delegateStr = ' <' + delegates.unique().join(', ') + '>'
    }
    return superClassName + delegateStr
  }

  //
  //  Building components 
  //

  public void process() {}

  public void injectCode(InjectionPoint location, String code, Map binding = null) { 
	if (location && code) { 
	  switch (location) { 
	  case Framework: 
		frameworks << code
		break;
	  case Import:
		imports << "\"${code}.h\""
		classDecls << code
		break; 
	  case SystemImport:
		systemImports << "<${code}>"
		break; 
	  case Protocol:
		delegates << code
		break;
	  case DelegateDeclaration:
        addDelegate(code, binding?.'widgetType')
		break; 
	  case MethodDeclaration:
		methodDeclarationScrap += "\n${code}"
		break;
	  case VariableDeclaration:
		if (ivarDeclScrap != '') 
		  ivarDeclScrap += "\n${code}"
		else 
		  ivarDeclScrap = code 
		break;
	  case Declaration:
		declarationScrap += "\n${code}"
		break;
	  case Method:
		methodScrap += "\n\n${code}\n"
		break; 
	  case PropertyDeclaration:
		propertyScrap += "\n@property(nonatomic, strong) ${code};"
		/*
		if (binding) { 
		  String type = binding.type
		  String name = binding.name
		  if (type && name) { 
			declareProperty(type, name)
		  }
		}
		*/
		break;
	  case PropertyName:

		break; 

	  case ProjectInfo:
		if (projectInfoScrap != '') 
		  projectInfoScrap += "\n${code}"
		else 
		  projectInfoScrap = code
		break;
	  }
	}
  }

  public void addImport(String header, boolean system = false) { 
    if (header) {
	  if (system) { 
		systemImports << "<${header}>"
	  } else { 
		imports << "\"${header}.h\""
		classDecls << header
	  }
    }
  }

  public void addFramework(framework) { 
    if (framework) { 
	  frameworks << framework
    }
  }

  public void addDelegate(String delegateName, String widgetType) { 
	if (delegateName) { 
	  if (!(delegateName in delegates)) { 
		def delegate = new Delegate(widgetType: widgetType,
									delegateName: delegateName) 
		delegates << delegateName
		delegateActions[delegateName] = delegate
	  }
	}
  }

  void declareVariable(String type, String name) { 
	declarationScrap += "\n${type} *${name};"
  }

  void declareProperty(String type, String name, boolean strong = true) {
	String ref = strong ? 'strong' : 'weak'
	propertyScrap += "\n@property(nonatomic, ${ref}) ${type} *${name};"
	propertyNames += name
  }

  String setViewBackgroundCode(background, String view) { 
	if (background && 
		!(background instanceof xj.mobile.model.properties.Color)) { 
	  String imgFile = background
	  addImageFile(imgFile)
	  return "${view}.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@\"${imgFile}\"]];"
	} else {      
	  String bgcolor
	  if (background) { 
		bgcolor = background.toIOSString()
	  } else { 
		bgcolor = '[UIColor whiteColor]'
	  }
	  return "${view}.backgroundColor = ${bgcolor};"
	}
  }


  //
  // handle delegate 
  // 
						 
  def getDelegateTemplate(delegateName, templateName) { 
    def temp = null 
    if (delegateName) { 
      def dtemp = delegateTemplates[delegateName]
      if (dtemp)
        temp = dtemp[templateName]
    }
    return temp 
  }

  def handleDelegates() { 
    if (delegateActions) { 
      delegateActions.each { name, delegate -> 
		info "[ViewController.handleDelegates] ${name} ${delegate.widgetType} ${delegate.delegateName}";

        def actionCode = null
		def delegateAction = getDelegateTemplate(delegate.delegateName, 'action')
		def param = getDelegateTemplate(delegate.delegateName, 'action_var')
		if (delegate.actions) { 
          if (delegate.actions.size() == 1) { 
			actionCode = delegate.actions[0].code
          } else { 
			delegate.actions.each { action -> 
			  info "[ViewControllerClass.handleDelegates] param=${param} var=${action.name} code=${action.code}";
			  if (action.code) { 
				if (actionCode == null) { 
				  actionCode = ''
				} else { 
				  actionCode += ' else '
				}
				actionCode += """if (${param} == ${action.name}) {
${indent(action.code)}
}"""
               
			  }
            }  
          }
		  if (actionCode || delegate.widgetType == 'Text') { 
			CodeGenerator generator = CodeGenerator.getCodeGenerator('ios')
			generator.injectCode(this, name, null, indent(actionCode), null, delegateAction, null) 
		  }

		}
      } 
    }
  }

  def getDelegateTemplateForWidget(wtemp, name) { 
    def temp = null 
    def delegateName = getTemplate(wtemp, 'delegate')
    if (delegateName) { 
      temp = delegateTemplates[delegateName][name]
    }
    return temp 
  }

}