package xj.mobile.ios

import xj.mobile.*
import xj.mobile.model.ui.*
import xj.mobile.lang.*

import xj.mobile.common.ViewProcessor

import static xj.mobile.ios.IOSAppGenerator.*
import static xj.mobile.util.CommonUtil.*

import static xj.mobile.model.impl.ViewControllerClass.getViewControllerName

class TabbedViewProcessor extends DefaultViewProcessor { 

  public TabbedViewProcessor(View view, String viewName = null) { 
    super(view, viewName)
    classModel.superClassName = 'UITabBarController'
    classModel.initViewWithSuper = true
  }

  void process() { 
    if (view) { 
	  currentViewProcessor = this

      initializeTopView()

	  boolean first = true
      view.children.each { Widget widget -> 
		if (Language.isTopView(widget.widgetType)) {  
		  String name = widget.id
		  String uiclass = getViewControllerName(widget.viewProcessor.viewName)

		  if (view.widgetType == 'TabbedView' || first) { 
			classModel.addImport(uiclass) 
			classModel.declareProperty(uiclass, name)

			def binding = [ name: name, uiclass: uiclass ]
			generator.injectCodeFromTemplateRef(classModel, "TabbedView:tab1", binding)

			if (first) first = false
		  }

		  processSubviewAttributes(widget)	  
		}	
      }
      
      initializeView() 
      
    }
  }

  def processSubviewAttributes(subview) { 
    if (subview) { 
	  def binding = [
		name: subview.id,
		title: subview.title,
		image: subview.tabImage
	  ]

      if (subview.title) { 
		generator.injectCodeFromTemplateRef(classModel, "TabbedView:sub1", binding)
      }
      if (subview.tabImage) { 
		generator.injectCodeFromTemplateRef(classModel, "TabbedView:sub2", binding)
		classModel.addImageFile(subview.tabImage)
      }
    }
  }

  def initializeView() { 
	def binding = [ views: topViews ]
	generator.injectCodeFromTemplateRef(classModel, "TabbedView:init", binding)
  }

}
