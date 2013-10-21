package xj.mobile.ios

import xj.mobile.*
import xj.mobile.model.ui.*
import xj.mobile.lang.*
import xj.mobile.common.*
import xj.mobile.model.properties.Property
import xj.mobile.model.properties.ModalTransitionStyle
import xj.mobile.codegen.templates.WidgetTemplates
import xj.mobile.codegen.Delegate

import xj.mobile.codegen.CodeGenerator

import static xj.mobile.codegen.templates.IOSDelegateTemplates.*
import static xj.mobile.codegen.templates.IOSWidgetTemplates.*
import static xj.mobile.common.ViewProcessor.*
import static xj.mobile.util.CommonUtil.*

import static xj.translate.Logger.info 

class WidgetProcessor extends xj.mobile.common.WidgetProcessor { 

  WidgetProcessor(ViewProcessor vp) { 
    super(vp)
    widgetTemplates = WidgetTemplates.getWidgetTemplates('ios')
	generator = CodeGenerator.getCodeGenerator('ios')
  }

  void process(Widget widget) { 
    String name = getWidgetName(widget)
	String widgetType = widget.widgetType
    info "[WidgetProcessor] process ${name}"

	def classModel = vp.classModel
    def wtemp = getWidgetTemplate(widget)

    if (wtemp && wtemp.uiclass) { 
      def template
	  def uiclass = wtemp.uiclass
	  if (wtemp.uiclass instanceof Closure) { 
		uiclass = wtemp.uiclass(widget)
	  }
      def binding = [ name: name,
					  widgetType: widgetType,
					  uiclass: uiclass, 
					  frame: widget._frame ? widget._frame[0..3].join(', ') : '0, 0, 0, 0' ]

	  for (int i = 0; ; i++) { 
		if (wtemp["arg${i}"]) { 
		  def val = wtemp["arg${i}"]
		  if (val instanceof Closure) { 
			val = val(widget)
		  }
		  binding["arg${i}"] = val
		} else { 
		  break 
		}
	  } 

	  generator.injectCodeFromTemplateRef(classModel, "${widgetType}:header", binding)
	  generator.injectCodeFromTemplateRef(classModel, "${widgetType}:framework", binding)	  
	  generator.injectCodeFromTemplateRef(classModel, "${widgetType}:delegate", binding)

	  generator.injectCodeFromTemplateRef(classModel, "${widgetType}:create", binding)

	  if (wtemp.needsFrame && widget._frame) { 
		generator.injectCodeFromTemplateRef(classModel, "${widgetType}:setFrame", binding)
	  }

	  handleAttributes(widget, wtemp, classModel) 

	  if (wtemp.images) { 
		classModel.systemImageFiles.addAll(wtemp.images)
	  }

	  generator.injectCodeFromTemplateRef(classModel, "${widgetType}:addSubview", binding)
	  classModel.declareProperty(binding.uiclass, binding.name)

      if (wtemp.processor) { 
		wtemp.processor.process(widget, vp)
	  } else if (wtemp.template) { 
		binding += [ actionCode: genActionCode(widget) ]
		generator.injectCodeFromTemplateRef(vp.classModel, wtemp.template, binding)
      } else { 
		// default when no processor is specified 
		// handle action 
		handleAction(widget, wtemp) 
      }

      vp.handleImageFiles(widget)
    }
  }

  void handleAttributes(widget, wtemp, classModel) { 
	String name = getWidgetName(widget)
	def defaultAttrCode = []
	if (wtemp.defaultAttributes) { 
	  def defaultAttributes = wtemp.defaultAttributes
	  if (defaultAttributes instanceof Closure) { 
		defaultAttributes = defaultAttributes(widget)
	  }

	  defaultAttributes.each { attr, value -> 
		def code = generator.generateSetAttributeCode(widget.widgetType, name, attr, widget[attr], value)
		if (code != null) { 
		  defaultAttrCode << [ code[0], "${code[1]};" ]
		}			  
	  }
	}

	def attrs = getWidgetAttributes(widget) 
	def initAttrCode = []
	if (wtemp.initialAttributes) { 
	  wtemp.initialAttributes.each { attr -> 
		def code = null
		if (attr instanceof List) { 
		  // combination attributes with a signle setter 
		  if (attr.every{ widget[it] != null }) { 
			attrs.removeAll(attr)
			code = generator.generateSetCompoundAttributeCode(widget.widgetType, name, 
															  attr, attr.collect{ widget[it] })
		  }
		  if (code != null) { 
			initAttrCode << [ code[0], "${code[1]}" ]
		  }			  
		} else { 
		  if (widget[attr]) { 
			attrs.remove(attr)

			def src = widget["${attr}.src"]
			if (src) { 
			  def ucode = generator.generateUpdateCode(vp, src.code, widget, name, attr);
			  //generator.injectCodeFromTemplate(classModel, CodeGenerator.InjectionPoint.LoadViewTail, ucode)
			  generator.injectCodeFromTemplate(classModel, CodeGenerator.InjectionPoint.UpdateView, ucode)
			} else { 
			  code = generator.generateSetAttributeCode(widget.widgetType, name, attr, widget[attr])
			}
		  } else { 
			// no user specified value
		  }
		  if (code != null) { 
			initAttrCode << [ code[0], "${code[1]};" ]
		  }			  
		}
	  }
	}

	// handle other attributes 
	def attrCode = vp.setAttributes(widget, attrs, classModel)
	def actualAttrNames = attrCode.collect{ it[0] }
	def code = (defaultAttrCode.findAll { !(it[0] in actualAttrNames) } +
				initAttrCode + attrCode).collect { it[1] }.join('\n')
	generator.injectCodeFromTemplate(classModel, CodeGenerator.InjectionPoint.LoadView, code)
  }

  void handleAction(widget, wtemp) { 
    String actionCode = ''
    def codeTemplate = getTemplate(wtemp, 'actionCode') 
    if (codeTemplate) { 
      def binding = [ name : widget.id,
					  widget : widget ]
      def template = engine.createTemplate(codeTemplate).make(binding)
      actionCode = template.toString()
    }
    String userCode = genActionCode(widget)
    if (userCode) {
      if (actionCode) { 
		actionCode += ('\n' + userCode) 
      } else { 
		actionCode = userCode
      }
    }

	vp.classModel.injectActionCode(widget.nodeType, getWidgetName(widget), wtemp, actionCode) 
  }

}




