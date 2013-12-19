package xj.mobile.android

import xj.mobile.*
import xj.mobile.common.*
import xj.mobile.model.ui.*
import xj.mobile.codegen.UnparserUtil
import xj.mobile.codegen.templates.WidgetTemplates
import xj.mobile.lang.*

import xj.mobile.util.PrettyMarkupBuilder

import xj.mobile.codegen.CodeGenerator

import static xj.mobile.codegen.templates.AndroidWidgetTemplates.*
import static xj.mobile.common.ViewProcessor.*
import static xj.mobile.util.CommonUtil.*
import static xj.mobile.model.impl.ClassModel.getCustomViewName

import static xj.translate.Logger.info 

class WidgetProcessor extends xj.mobile.common.WidgetProcessor { 

  static config = AndroidAppGenerator.androidConfig

  WidgetProcessor(ViewProcessor vp) { 
    super(vp)
    widgetTemplates = WidgetTemplates.getWidgetTemplates('android')
	generator = CodeGenerator.getCodeGenerator('android')
  }

  void process(Widget widget, PrettyMarkupBuilder mainLayout) { 
    String name = getWidgetName(widget)
    info "[WidgetProcessor] process ${name}"

    def wtemp = getWidgetTemplate(widget)
    if (wtemp && wtemp.uiclass) { 
	  /*
      if (wtemp.import) { 
		vp.classModel.imports << wtemp.import
      }
	  */
      if (wtemp.library) { 
		vp.classModel.resources.libraries << wtemp.library
		vp.classModel.imports << "${wtemp.library}.*"
      }
	  if (wtemp.styleable) { 
		vp.classModel.resources.styleableResources.put(wtemp.uiclass, wtemp.styleable)
	  }

      if (wtemp.permission) { 
		if (wtemp.permission instanceof List) { 
		  vp.classModel.resources.permissions += wtemp.permission
		} else { 
		  vp.classModel.resources.permissions << wtemp.permission
		}
      }
      if (wtemp.activity) { 
		int i = wtemp.activity.lastIndexOf('.')
		if (i > 0) { 
		  vp.classModel.imports << wtemp.activity
		  vp.classModel.superClassName = wtemp.activity[i + 1 .. -1]
		} else {  
		  vp.classModel.superClassName = wtemp.activity
		}
      }

	  def uiclass0 = wtemp.uiclass
	  if (uiclass0 instanceof Closure) { 
		uiclass0 = uiclass0(widget)
	  }	  
	  if (wtemp.custom) { 
		vp.classModel.auxiliaryClasses.add(uiclass0)
	  }

	  def uiclass = uiclass0
      int i = uiclass.lastIndexOf('.')
      if (i > 0) { 
		vp.classModel.imports << uiclass
		uiclass = uiclass[i + 1 .. -1]
      }
      info "[WidgetProcessor] wtemp.uiclass ${uiclass}"
      
      def attributes = [:]
      if (widget.id) { 
		attributes['android:id'] = "@+id/${widget.id}"
      }
      if (widget.text) { 
		attributes['android:text'] = "${widget.text}"
      }

      def padding = widget.padding
      if (!padding) { 
		padding = config.defaults[widget.widgetType].padding
      }
      if (padding) { 
		attributes['android:padding'] = "${padding}dp"
      }

	  def binding = widget.properties + [ 
		name : name,
		uiclass: uiclass 
	  ]

      if (wtemp.defaultAttributes) { 
		def defaultAttributes = wtemp.defaultAttributes
		if (defaultAttributes instanceof Closure) { 
		  defaultAttributes = defaultAttributes(widget)
		}
		defaultAttributes.each { k, v ->
		  attributes[k] = engine.createTemplate(v).make(binding).toString()
		}
      }

	  if (widget.size) { 
		attributes['android:layout_width'] = "${widget.size[0]}dp"
		attributes['android:layout_height'] = "${widget.size[1]}dp"
		attributes['android:scaleType'] = 'fitXY'
	  } else { 
		def width = AndroidAppGenerator.androidConfig.defaults[widget.widgetType].layout_width 
		if (!width)
		  width = AndroidAppGenerator.androidConfig.defaults.layout_width  
		def height = AndroidAppGenerator.androidConfig.defaults[widget.widgetType].layout_height 
		if (!height)
		  height = AndroidAppGenerator.androidConfig.defaults.layout_height 
		attributes['android:layout_width'] = width
		attributes['android:layout_height'] = height
	  }

      // find widgets
      //def binding = [ name : name, uiclass : uiclass]
      def template = engine.createTemplate(findViewTemplate).make(binding)
      vp.classModel.onCreateScrap += ('\n' + template.toString())
      
      // add declaration of widgets	
      template = engine.createTemplate(declarationTemplate).make(binding)
      vp.classModel.declarationScrap += ('\n' + template.toString())

	  def attrs = getWidgetAttributes(widget) 

	  // handle attributes, must be handled before generating layout 
	  //attrs.removeAll('text', 'width', 'height') // exclude text
	  attrs.removeAll('width', 'height') 
	  if (wtemp.initialAttributes) 
		attrs.removeAll(wtemp.initialAttributes)
	  if (attrs) { 
		attrs.each { a ->
		  info "[WidgetProcessor] process attribute ${a} ${uiclass}"
		  //def prop = findPropertyDef(wtemp, a)
		  def prop = findCustomPropertyDef(wtemp, a)
		  if (!prop) { 
			prop = attributeHandler.apiResolver.findPropertyDef(uiclass, a, true)
		  }
		  if (prop) { 
			def value = attributeHandler.getAttributeValue(a, widget[a], prop.type)
			if (value != null &&
				a != 'text' && 
				!widget["${a}.src"]) {
			  if (a == 'font' && widget.font instanceof xj.mobile.model.properties.Font) { 
				attributes += widget.font.toAndroidXMLAttributes()
			  } else if (prop.xmlName) { 
				info "[WidgetProcessor] process attribute ${a} ${wtemp.uiclass} xmlName=${prop.xmlName}"
				if (value instanceof xj.mobile.model.properties.Property) { 
				  attributes[prop.xmlName] = value.toAndroidXMLString()
				} else {  
				  attributes[prop.xmlName] = value.toString()
				}
			  } else { 
				info "[WidgetProcessor] process attribute ${a} ${wtemp.uiclass} !xmlName"
				def code = generator.generateSetAttributeCode(widget.widgetType, name, a, widget[a])
				vp.classModel.onCreateScrap += ('\n' + code[1] + ';')
			  }
			} else { 
			  def src = widget["${a}.src"]
			  if (src) { 
				def ucode = generator.generateUpdateCode(vp, src.code, widget, name, a);
				vp.classModel.onCreateTailScrap += ('\n' + ucode)
			  }
			} 
		  }
		}
	  }

      if (wtemp.processor) { 
		wtemp.processor.process(widget, vp)
	  } else if (wtemp.template) { 
		binding += [ actionCode: genActionCode(widget) ]
		generator.injectCodeFromTemplateRef(vp.classModel, wtemp.template, binding)
      } else { 
		handleAction(widget, wtemp, attributes) 
      }

	  if (wtemp.layoutProcessor) { 
		wtemp.layoutProcessor.process(widget, mainLayout, uiclass0, attributes)
	  } else { 
		def layoutClass = uiclass0
		if (wtemp.custom &&
			vp.classModel.packageName) { 
		  layoutClass = vp.classModel.packageName + '.' + uiclass0
		}
		mainLayout."${layoutClass}"(attributes)
	  }

      if (wtemp.initialAttributes) { 
		// initial attributes are set in onCreate()
		wtemp.initialAttributes.each { attr -> 
		  if (attr instanceof List) { 
			// combination attributes with a signle setter 
			if (attr.every{ widget[it] }) { 
			  def setAttr = getAttributeSetterTemplate(wtemp, attr.join('_'))
			  if (setAttr) { 
				if (setAttr instanceof Closure) { 
				  setAttr = setAttr(widget)
				}	
				def binding3 = [ name : name ]
				attr.each { binding3[it] = attributeHandler.getAttributeValueForWidget(wtemp.uiclass, it, widget[it]) }
				template = engine.createTemplate(setAttr).make(binding3)
				vp.classModel.onCreateScrap += ('\n' + template.toString())
			  }
			}
		  } else { 
			if (widget[attr]) { 
			  def setAttr = getAttributeSetterTemplate(wtemp, attr)
			  if (setAttr) { 
				if (setAttr instanceof Closure) { 
				  setAttr = setAttr(widget)
				}	
				String value = attributeHandler.getAttributeValueForWidget(wtemp.uiclass, attr, widget[attr])
				if (value != null && 
					!widget["${attr}.src"]) {
				  def binding3 = [ name : name,
								   attribute : attr,
								   value : value ]
				  template = engine.createTemplate(setAttr).make(binding3)
				  vp.classModel.onCreateScrap += ('\n' + template.toString())
				} else { 
				  def src = widget["${attr}.src"]
				  if (src) { 
					def ucode = vp.generateUpdateCode(src.code, widget, name, attr);
					vp.classModel.onCreateTailScrap += ('\n' + ucode)
				  }
				}
			  }
			}
		  }
		}
      }

      vp.handleImageFiles(widget)
    }
      
  }

  def findCustomPropertyDef(wtemp, attr) { 
	def propDef = null
	if (wtemp && attr) {
	  if (wtemp.custom) { 
		if (wtemp.styleable) { 
		  def aliases = attributeHandler.apiResolver.getAliases(wtemp.uiclass, attr)
		  for (pname in aliases) { 
			if (!propDef) { 
			  def type = wtemp.styleable[pname]
			  if (type) { 
				propDef = [name: pname, xmlName: 'custom:' + pname, type: type]
			  }
			}
		  }
		}
	  }
	}
	return propDef 
  }

  def handleAction(widget, wtemp, attributes) { 
    String actionCode = genActionCode(widget)
    injectActionCode(widget, wtemp, actionCode, attributes)
  }

  void injectActionCode(widget, wtemp, actionCode, attributes) { 
    def actionName = null
    if (actionCode) { 	  
      String name = getWidgetName(widget)
      actionName = "${getActionName(widget)}_${widget.id}"
      def binding = [ name : name,
					  actionName : actionName,
					  actionBody : indent(actionCode, 1, '    ') ] + UnparserUtil.baseBinding
      def template = engine.createTemplate(getTemplate(wtemp, 'action')).make(binding)
      vp.classModel.methodScrap += template.toString()
    } else if (widget.actionName) { 
      actionName = widget.actionName
    }

    if (actionName && wtemp?.xevent && attributes!= null) { 
	  def prefix = wtemp.custom ? 'custom' : 'android'
      attributes["${prefix}:${wtemp.xevent}"] = actionName
	}

  }

}