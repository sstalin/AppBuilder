package xj.mobile.codegen

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.*

import xj.mobile.api.AttributeHandler
import xj.mobile.codegen.templates.Templates

import xj.mobile.model.ModelNode
import xj.mobile.model.impl.ClassModel
import xj.translate.common.Unparser

import static xj.mobile.lang.AttributeMap.findCommonAttributeDef
import static xj.translate.Logger.info 

class CodeGenerator { 

  enum InjectionPoint {
	Framework,
	Import,
	SystemImport,
	Protocol,
	DelegateDeclaration,
	MethodDeclaration, 
	VariableDeclaration,
	Declaration,
	Method,
	PropertyDeclaration,
	PropertyName,

	LoadView,
	LoadViewTail,
	UpdateView,
	Draw, 

	ProjectInfo,
	AuxiliaryClass, 

	Creation, 
	PostCreation, 
	OnTap,
	OnDoubleTap, 
	OnFling,
	OnLongPress, 
	OnScale,
	OnRotate, 
	OnDrag,

	Execute, 
  }

  static InjectionPointMap = [
	framework:    InjectionPoint.Framework,
	import:       InjectionPoint.Import,
	systemImport: InjectionPoint.SystemImport,
	protocol:     InjectionPoint.Protocol,

	delegate:            InjectionPoint.DelegateDeclaration,
	methodDeclaration:   InjectionPoint.MethodDeclaration,
	variableDeclaration: InjectionPoint.VariableDeclaration,
	declaration:         InjectionPoint.Declaration,
	method:              InjectionPoint.Method,

	loadView:     InjectionPoint.LoadView,
	loadViewTail: InjectionPoint.LoadViewTail,
	creation:     InjectionPoint.Creation,
	postCreation: InjectionPoint.PostCreation,
	updateView:   InjectionPoint.UpdateView,
	draw:         InjectionPoint.Draw,

	onTap:        InjectionPoint.OnTap,
	onDoubleTap:  InjectionPoint.OnDoubleTap, 
	onFling:	  InjectionPoint.OnFling,
	onLongPress:  InjectionPoint.OnLongPress, 
	onScale:      InjectionPoint.OnScale,
	onRotate:     InjectionPoint.OnRotate, 
	onDrag:       InjectionPoint.OnDrag,

	projectInfo:  InjectionPoint.ProjectInfo,
	auxiliary:    InjectionPoint.AuxiliaryClass,
  ]

  static codegenBinding = [
	toNSArrayWithStrings: xj.mobile.codegen.IOSUtil.&toNSArrayWithStrings,
	toNSArrayWithObjects: xj.mobile.codegen.IOSUtil.&toNSArrayWithObjects,
	toNSDictionaryWithObjects: xj.mobile.codegen.IOSUtil.&toNSDictionaryWithObjects, 
  ]

  ActionHandler actionHandler
  AttributeHandler attributeHandler 

  @groovy.lang.Delegate
  Templates templates

  def engine 
  Unparser unparser

  //
  // code generation 
  //

  String instantiateCodeFromTemplate(codeTemplate, Map binding = null) { 
	String code = codeTemplate
	if (codeTemplate && binding) {
	  if (codeTemplate instanceof String) { 
		binding += (UnparserUtil.baseBinding + codegenBinding)
		code = engine.createTemplate(codeTemplate).make(binding) as String
	  } else if (codeTemplate instanceof Closure) { 
		codeTemplate.delegate = binding
		code = codeTemplate() as String 
	  }
	}
	return code
  }

  String instantiateCodeFromTemplateRef(String tempRef, Map binding = null) { 
	def temp = templates.getTemplateByRef(tempRef)
	if (temp) { 
	  return instantiateCodeFromTemplate(temp.code, binding)
	} else { 
	  info "[CodeGenerator] >>>> template not found: ${tempRef}"
	}
	return null
  }

  //
  // code injection 
  //

  static getLocationAndCode(temp) { 
	if (temp) { 
	  if (temp.location) { 
		return [temp.location, temp.code]
	  } else { 
		for (k in InjectionPointMap.keySet()) { 
		  if (temp[k]) { 
			return [ InjectionPointMap[k], temp[k] ]
		  }
		}
	  }
	}
	return [ null, null ]
  }

  void injectCodeFromTemplate(ClassModel target, 
							  InjectionPoint location, 
							  codeTemplate, 
							  Map binding = null,
							  when = null) { 
	if (target && location && codeTemplate != null) {
	  boolean cond = true
	  if (when instanceof Closure) { 
		if (binding)
		  when.delegate = binding 
		cond = when()
	  } 
	  if (cond) { 
		if (location != InjectionPoint.Execute) { 
		  String code = instantiateCodeFromTemplate(codeTemplate, binding)
		  target.injectCode(location, code, binding)
		} else { 
		  if (codeTemplate instanceof Closure){ 
			target.injectCode(codeTemplate, binding)
		  }
		}
	  }
	}
  }

  void injectCodeFromTemplateRef(ClassModel target, String tempRef, Map binding = null) {  
	def temp = templates.getTemplateByRef(tempRef)
	if (temp) { 
	  if (temp instanceof List) { 
		temp.each { t -> 
		  if (t.do instanceof Closure) { 
			target.injectCode(t.do, binding)
		  } else { 
			def (location, code) = getLocationAndCode(t)
			injectCodeFromTemplate(target, location, code, binding, t.when)
		  }
		}
	  } else { 
		if (temp.do instanceof Closure) { 
		  target.injectCode(temp.do, binding)
		} else { 
		  def (location, code) = getLocationAndCode(temp)
		  injectCodeFromTemplate(target, location, code, binding, temp.when)
		}
	  }
	}
  }

  //
  // handling action
  //

  String generateActionCode(xj.mobile.common.ViewProcessor vp,
							Map srcInfo, ModelNode widget) { 
	actionHandler.setContext(vp)
	actionHandler.generateActionCode(srcInfo, widget)
  }

  String generateUpdateCode(xj.mobile.common.ViewProcessor vp,
							Set updates, ModelNode widget, VariableScope scope = null) { 
	actionHandler.setContext(vp)
	actionHandler.generateUpdateCode(updates, widget, scope)
  }

  String generateUpdateCode(xj.mobile.common.ViewProcessor vp,
							Expression src, ModelNode widget, 
							String wname, String attribute, VariableScope scope = null) { 
	actionHandler.setContext(vp)
	def w = vp.getWidget(wname)
	actionHandler.generateUpdateCode(src, widget, w.widgetType, wname, attribute, scope)
  }

  String injectCode(xj.mobile.common.ViewProcessor vp,
					String widgetName, String actionEvent, String actionCode, String actionName, 
					String actionTemplate, String targetTemplate) { 
	actionHandler.setContext(vp)
	actionHandler.injectCode(widgetName, actionEvent, actionCode, actionName, 
							 actionTemplate, targetTemplate)
  }

  String injectCode(ClassModel classModel,
					String widgetName, String actionEvent, String actionCode, String actionName, 
					String actionTemplate, String targetTemplate) { 
	actionHandler.setContext(null, classModel)
	actionHandler.injectCode(widgetName, actionEvent, actionCode, actionName, 
							 actionTemplate, targetTemplate)
  }

  //
  // generate transition code
  //

  String generatePopupTransitionCode(String popupName, String popupType, boolean isMenu = false) { 
	String actionCode = null
	def actionTemplate = getActionTemplate(popupType, isMenu)
	if (actionTemplate) { 
	  def binding = [ name : popupName ] + UnparserUtil.baseBinding
	  def template = engine.createTemplate(actionTemplate).make(binding)
	  actionCode = "${template}"
	}
	return actionCode
  }

  //
  //  generate get/set attribute code 
  //  

  // handler setter

  // look up native properties only 
  def generateSetNativeAttributeCode(String widgetClass, 
									 String widgetName, 
									 String attrName, 
                                     attrValue, 
									 String prefix = null,
									 boolean valueUnparsed = false) { 
	def prop = attributeHandler.apiResolver.findPropertyDef(widgetClass, attrName, true)
	info "[CodeGenerator] generateSetNativeAttributeCode() attrValue=${attrValue} attrValue.class=${attrValue.class}"
	if (prop) { 
	  def value = valueUnparsed ? attrValue : attributeHandler.getAttributeValue(attrName, attrValue, prop.type)
	  if (value != null) { 
		info "[CodeGenerator] generateSetNativeAttributeCode() value=${value} value.class=${value.class}"
		def setterTemp = prop.setterTemplate
		String name = (prefix ?: '') + widgetName
		def temp = ''
		if (setterTemp instanceof Closure) { 
		  //temp = setterTemp(widgetName, value)
		  temp = setterTemp(name, value)
		} else { 
		  def binding = [ name : name,
						  value : value.toString() ]
		  temp = engine.createTemplate(setterTemp).make(binding)
		}
		return [ prop.name,  temp.toString() ] 
	  }
	}
	return null
  }

  // look up common attributes first, then native properties 
  // if default value is provided use widget template to generate code. 
  // default value is in native syntax  
  def generateSetAttributeCode(String widgetType, 
							   String widgetName, 
							   String attrName, 
							   attrValue, 
							   String defaultValue = null, 
							   String prefix = null,
							   boolean valueUnparsed = false) { 
	def attrDef = findCommonAttributeDef(widgetType, attrName)
	if (attrDef || defaultValue != null) { 
	  def setAttr = getAttributeSetterTemplate(widgetType, attrName)
	  if (setAttr) { 
		String value = valueUnparsed ? attrValue : attributeHandler.getAttributeValue(attrName, attrValue, attrDef?.type)
		if (value == null) value = defaultValue
		String name = (prefix ?: '') + widgetName
		def binding = [ name : name,
						attribute : attrName,
						value : value 
					  ] + UnparserUtil.baseBinding
		def temp = engine.createTemplate(setAttr).make(binding)
		return [ attrName, temp.toString() ] 
	  }
	} else { 
	  String widgetClass = getWidgetNativeClass(widgetType)
	  return generateSetNativeAttributeCode(widgetClass, widgetName, attrName, attrValue, prefix, valueUnparsed) 
	}
	return null
  }

  def generateSetCompoundAttributeCode(String widgetType, 
									   String widgetName, 
									   List attrs, 
									   List values,
									   String prefix = null) { 
	def setAttr = getAttributeSetterTemplate(widgetType, attrs.join('_'))
	if (setAttr) { 
	  String name = (prefix ?: '') + widgetName
	  def binding = [ name : name ]
	  attrs.eachWithIndex { attr, i -> binding[attr] = attributeHandler.getAttributeValue(attr, values[i]) }
	  def temp = engine.createTemplate(setAttr).make(binding)
	  return [ attrs, "${temp}"]
	}
  }

  // handler getter

  // look up native properties only 
  String generateGetNativeAttributeCode(String widgetClass, 
										String widgetName, 
										String attrName, 
										index,
										String prefix = null) { 
	def prop = attributeHandler.apiResolver.findPropertyDef(widgetClass, attrName, false)
	if (prop) { 
	  String name = (prefix ?: '') + widgetName
	  def binding = [ name : name ]
	  return engine.createTemplate(prop.getterTemplate).make(binding).toString()
	}
	return null
  }

  String generateGetAttributeCode(String widgetType,
								  String widgetName, 
								  String attrName, 
								  index,
								  String prefix = null) { 
	String nodeType = widgetType 
	int i = widgetType.indexOf('#')
	if (i > 0) nodeType = widgetType.substring(0, i)
	def attrDef = findCommonAttributeDef(nodeType, attrName)
	if (attrDef) { 
	  def getAttr = getAttributeGetterTemplate(widgetType, attrName, index != null)
	  if (getAttr) { 
		String name = (prefix ?: '') + widgetName
		def binding = [ name : name,
						attribute : attrName,
						index : index ?: '' 
					  ] + UnparserUtil.baseBinding
		return engine.createTemplate(getAttr).make(binding).toString()
	  }
	} else { 
	  String widgetClass = getWidgetNativeClass(nodeType)
	  return generateGetNativeAttributeCode(widgetClass, widgetName, attrName, index, prefix) 
	}
	return null
  }

  String typeName(ClassNode type, boolean mapType = true) { 
	unparser.typeName(type)
  }

  //
  //  initialization 
  //

  static generatorMap = [:]

  static CodeGenerator getCodeGenerator(String platform) { 
	if (generatorMap[platform] == null) { 
	  if (platform == 'ios') { 
		generatorMap[platform] = new IOSCodeGenerator()
	  } else if (platform == 'android') { 
		generatorMap[platform] = new AndroidCodeGenerator()
	  }
	}
	return generatorMap[platform]
  }

}