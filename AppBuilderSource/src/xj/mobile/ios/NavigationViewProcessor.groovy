package xj.mobile.ios

import xj.mobile.*
import xj.mobile.lang.*
import xj.mobile.model.ui.*
import xj.mobile.model.properties.*
import xj.mobile.codegen.CodeGenerator

import static xj.mobile.ios.IOSAppGenerator.*
import static xj.mobile.model.impl.ViewControllerClass.getViewControllerName

class NavigationViewProcessor extends TabbedViewProcessor { 

  public NavigationViewProcessor(View view, String viewName = null) { 
    super(view, viewName)
    classModel.superClassName = 'UINavigationController'
  }

  def processSubviewAttributes(subview) { 
    if (subview) { 
      String name = subview.id
	  def binding = [
		title: subview.title
	  ]

      if (subview.title) { 
		generator.injectCodeFromTemplateRef(subview.viewProcessor.classModel, "NavView:subview1", binding)
      }

      String backButtonText = null
      if (subview.shortTitle) { 
		backButtonText = subview.shortTitle
      }
      if (backButtonText) { 
		binding += [
		  backButtonText: backButtonText
		]
		generator.injectCodeFromTemplateRef(subview.viewProcessor.classModel, "NavView:subview2", binding)
      }

      if (subview.rightButton) { 
		def systemButton = null
		def actionCode = null
		def actionName = null
		if (subview.rightButton instanceof SystemButton) { 
		  systemButton = subview.rightButton
		} else if (subview.rightButton instanceof Map) { 
		  if (subview.rightButton.type instanceof SystemButton) { 
			systemButton = subview.rightButton.type
		  }
	  
		  if (subview.rightButton.action instanceof Closure) { 
			actionCode = generator.generateActionCode(subview.viewProcessor, subview['rightButton.action.src'], subview)
		  }
		  if (actionCode) { 
			actionName = "rightButtonAction_${subview.id}"
			def wtemp = widgetProcessor.getWidgetTemplate(subview)
			def actionTemplate = widgetProcessor.getTemplate(wtemp, 'action')
			generator.injectCode(subview.viewProcessor, subview.id, 'UIControlEventTouchUpInside', actionCode, actionName, 
								 actionTemplate, null) 
		  }
		}
		if (systemButton) { 
		  def action = actionName ? "@selector(${actionName}:)" : 'nil'
		  def target = actionName ? 'self' : 'nil'
		  binding += [
			action: action,
			target: target,
			systemButton: systemButton,
		  ]
		  generator.injectCodeFromTemplateRef(subview.viewProcessor.classModel, "NavView:subview3", binding)
		}
      }
    }
  }

  def initializeView() { 
	def binding = [ viewname: topViews[0] ]
	generator.injectCodeFromTemplateRef(classModel, "NavView:init", binding)

  }


}