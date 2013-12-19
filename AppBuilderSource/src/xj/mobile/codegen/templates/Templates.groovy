package xj.mobile.codegen.templates

import static xj.mobile.codegen.CodeGenerator.InjectionPoint.*

/*
 * Managing all templates
 *
 */
class Templates { 

  def templatesMap = null

  WidgetTemplates widgetTemplates
  PopupTemplates popupTemplates 

  def getTemplateByRef(String tempRef) { 
	if (tempRef) { 
	  def ref = tempRef.split(':')
	  if (ref.size() >= 2) { 
		if (templatesMap) { 
		  def t1 = templatesMap[ref[0]]
		  if (t1) {
			def t2 = t1[ref[1]]
			if (t2) return t2
		  }
		}
		def wtemp = getTemplateForWidgetType(ref[0])
		if (wtemp) { 
		  def code = widgetTemplates.getTemplate(wtemp, ref[1])
		  if (code) { 
			return [location: InjectionPointMap[ref[1]], code: code]
		  }
		}
	  }
	}
	return null
  }

  //
  //
  //

  static InjectionPointMap = [
	header: SystemImport,
	framework: Framework,
	delegate: DelegateDeclaration,
	create: LoadView, 
	setFrame: LoadView,
	addSubview: LoadView,
	
  ]


  //
  //
  //

  def getTemplate(wtemp, String name) { 
	widgetTemplates.getTemplate(wtemp, name)
  }

  def getActionTemplate(String popupType, boolean isMenu) { 
	popupTemplates.getActionTemplate(popupType, isMenu)
  }

  def getTemplateForWidgetType(String widgetType) { 
	def wtemp = widgetTemplates.getWidgetTemplateByName(widgetType)
	if (wtemp == null) { 
	  wtemp = popupTemplates.getPopupTemplateByName(widgetType)
	}
	return wtemp
  }

  String getWidgetNativeClass(String widgetType) { 
	getTemplateForWidgetType(widgetType)?.uiclass
  }

  def getAttributeSetterTemplate(String widgetType, String attr) { 
	def wtemp = getTemplateForWidgetType(widgetType)
	widgetTemplates.getAttributeSetterTemplate(wtemp, attr)
  }
  
  def getAttributeGetterTemplate(String widgetType, String attr, boolean indexed = false) { 
	def wtemp = getTemplateForWidgetType(widgetType)
	if (indexed) { 
	  widgetTemplates.getIndexedAttributeGetterTemplate(wtemp, attr)
	} else { 
	  widgetTemplates.getAttributeGetterTemplate(wtemp, attr)
	}
  }
  
  def getIndexedAttributeGetterTemplate(String widgetType, String attr) { 
	def wtemp = getTemplateForWidgetType(widgetType)
	widgetTemplates.getIndexedAttributeGetterTemplate(wtemp, attr)
  }



}