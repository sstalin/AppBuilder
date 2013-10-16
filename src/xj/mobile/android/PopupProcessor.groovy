package xj.mobile.android

import xj.mobile.*
import xj.mobile.model.ui.*
import xj.mobile.lang.*
import xj.mobile.codegen.*
import xj.mobile.common.ViewProcessor
import xj.mobile.codegen.UnparserUtil
import xj.mobile.codegen.templates.PopupTemplates

import static xj.mobile.common.ViewProcessor.*
import static xj.mobile.util.CommonUtil.*
import static xj.mobile.lang.ast.Util.*
import static xj.translate.Logger.info 

class PopupProcessor extends xj.mobile.common.PopupProcessor { 

  PopupProcessor(ViewProcessor vp) { 
    super(vp)
	popupTemplates = PopupTemplates.getPopupTemplates('android')
  }

  void process(Widget popup) { 
	//if (Language.isMenu(popup.widgetType)) return 

    String name = getWidgetName(popup)
    info "[PopupProcessor] process ${name}"

    def temp = getPopupTemplate(popup)
    if (temp) { 
      def ctemp = temp.create
      def builderSetters = ''
      if (popup.title) { 
		builderSetters = "\n    builder.setTitle(\"${popup.title}\");"
      }
      if (popup.message) { 
		builderSetters += "\n    builder.setMessage(\"${popup.message}\");"
      }

      String actionCode = vp.widgetProcessor.genActionCode(popup) 
      def closure = WidgetProcessor.getActionInfo(popup)?.code
      def params = getClosureParameters(closure)
      def binding
      def template
      def buttonCode = ''
      String other = null
      def preAction = ''

      if (popup.cancel) { 
		def cancelText = "\"${popup.cancel}\""
		def cancelAction = 'dialog.cancel();'
		if (actionCode) { 
		  if (params) { 
			preAction = "String ${params[0]} = ${cancelText};\n"
		  }
		  cancelAction = (preAction + actionCode + '\n' + cancelAction) 
		}
		binding = [ actionCode : cancelAction 
				  ] + UnparserUtil.baseBinding
		template = engine.createTemplate(temp.onclick).make(binding)
		def cancelCode = "${template}"
		binding = [ type : 'Negative',
					text : cancelText,
					listener : cancelCode ]
		template = engine.createTemplate(temp.button).make(binding)
		buttonCode += "\n${template}"
      } 

      if (popup.affirm || !popup.children && !popup.buttons) { 
		def affirmText = popup.affirm ? "\"${popup.affirm}\"" : '\"OK\"'
		def affirmCode = 'null'
		preAction = ''
		if (actionCode) { 
		  if (params) { 
			preAction = "String ${params[0]} = ${affirmText};\n"
		  }
		  binding = [ actionCode : preAction + actionCode 
					] + UnparserUtil.baseBinding
		  template = engine.createTemplate(temp.onclick).make(binding)
		  affirmCode = "${template}"
		}
		binding = [ type : 'Positive',
					text : affirmText,
					listener : affirmCode
				  ] + UnparserUtil.baseBinding
		template = engine.createTemplate(temp.button).make(binding)
		buttonCode += "\n${template}"   
      }

      if  (popup.children) {
		other = popup.children.findAll { it.widgetType == 'Item' }.collect {  "\"${it.text}\"" }.join(',\n')
		def itemActions = []
		popup.children.eachWithIndex { item, i -> 
		  def code = vp.widgetProcessor.genActionCode(item) 
		  if (code) { 
			itemActions << "if (id == ${i}) {\n" + indent(code, 1, '    ') + '\n}'
		  }
		}
		if (itemActions) { 
		  actionCode = itemActions.join(' else ') 
		}
      } else if (popup.buttons) { 
		other = popup.buttons.collect { "\"${it}\"" }.join(',\n')
      }

      if (other) { 
		binding = [ name : name,
					list : other 
				  ] + UnparserUtil.baseBinding
		template = engine.createTemplate(temp.listdata).make(binding)
		vp.classModel.popupActionScrap += template.toString()

		def itemCode = 'null'
		if (actionCode) { 
		  if (params) { 
			preAction = "String ${params[0]} = ${name}Data[id];\n"
		  }
		  binding = [ actionCode : preAction + actionCode 
					] + UnparserUtil.baseBinding
		  template = engine.createTemplate(temp.onclick).make(binding)
		  itemCode = "${template}"
		}
		binding = [ type : '',
					items : "${name}Data",
					listener : itemCode]
		template = engine.createTemplate(temp.items).make(binding)
		buttonCode += "\n${template}"   
      }

      binding = [ name : name,
				  cancellable : !popup.cancel,
				  setters : builderSetters,
				  buttons : buttonCode, 
				] + UnparserUtil.baseBinding
      template = engine.createTemplate(ctemp).make(binding)
      vp.classModel.popupActionScrap += template.toString()
      vp.classModel.imports << 'android.content.DialogInterface'
    } else if (widget.widgetType == 'Menu') { 
      vp.menuProcessor.process(widget)
    }
  }


}