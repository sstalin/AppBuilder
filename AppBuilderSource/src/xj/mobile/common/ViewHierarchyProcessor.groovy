package xj.mobile.common

import xj.mobile.*
import xj.mobile.model.*
import xj.mobile.model.impl.*
import xj.mobile.model.ui.*
import xj.mobile.lang.*

import static xj.translate.Logger.info 

// process the entire view hierarchy, containing children that are top views
class ViewHierarchyProcessor { 

  Application app
  View rootView

  AppInfo appInfo

  @Delegate
  Project project 

  Map viewProcessorMap

  public ViewHierarchyProcessor(Application app, AppInfo appInfo) { 
	this.app = app
	this.appInfo = appInfo

    rootView = app.mainView
    viewProcessorMap = [:]

	project = new Project() 
	project.packageName = appInfo.packageName
    project.rootViewName = toViewName(rootView?.id)

    if (rootView) { 
      setup(rootView)
    }
	
	app.children.each { view -> 
	  if (view != rootView) { 
		setup(view)
	  }
	}
	
  }

  ViewProcessor getViewProcessor(String id) { 
    viewProcessorMap[id]
  }

  void setup(View view) { 
    info "[ViewHierarchyProcessor] setup() ${view.id}"

    if (Language.isTopView(view.widgetType)) {  
      String viewName = toViewName(view.id)
      def vp = ViewProcessorFactory.getViewProcessor(view, viewName)
      if (vp) { 
		vp.init(appInfo)
		viewProcessorMap[view.id] = vp
		if (vp.classModel) { 
		  if (!view.embedded || vp.platform != 'Android') { 
			project.classes << vp.classModel
			vp.classModel.packageName = packageName
			if (vp.classModel.isMainView) { 
			  project.mainViewClass = vp.classModel
			}
		  }
		}

		vp.vhp = this
		view.viewProcessor = vp

		if (view == rootView) { 
		  vp.processRootView()
		}

		vp.processWidgetTable()
		info "[ViewHierarchyProcessor] WidgetTable ${view.id}: ${vp.widgetTable.keySet()}"
      

		view.children.each { widget -> 
		  if (widget instanceof Widget &&
			  Language.isTopView(widget.widgetType)) {  
			info "[ViewHierarchyProcessor] setup() ${view.id}: process widget ${widget.id} ${widget.widgetType}"

			String name = widget.id
			vp.topViews << name
	    
			setup(widget)
		  }
		}
      }
    }
  }

  void process() { 
    if (rootView) { 
      process(rootView)
    }
	
	app.children.each { view -> 
	  if (view != rootView) { 
		process(view)
	  }
	}
	
  }

  // process views bottom up
  void process(View view) { 
    info "[ViewHierarchyProcessor] process() ${view.id}"

    if (Language.isTopView(view.widgetType)) {  
      view.children.each { widget -> 
		if (widget instanceof Widget &&
			Language.isTopView(widget.widgetType) && 
			!widget.embedded) {  
		  // non-embedded top view 
		  info "[ViewHierarchyProcessor] process() ${view.id}: process widget ${widget.id} ${widget.widgetType}"
		  process(widget)
		}
      }
      
      info "[ViewHierarchyProcessor] process() ${view.id}: vp.process() ${view.id} ${view.widgetType}"
      view.viewProcessor?.process()

      view.children.each { widget -> 
		if (widget instanceof Widget &&
			Language.isTopView(widget.widgetType) && 
			widget.embedded) {  
		  // embedded top view 
		  info "[ViewHierarchyProcessor] process() ${view.id}: process widget ${widget.id} ${widget.widgetType}"
		  process(widget)
		}
      }
    }
  }

  def findViewProcessor(id) { 
    viewProcessorMap[id]
  }

  def getViewProcessors() { 
    def result = []
    viewProcessorMap.each { key, vp ->
      result << vp
    }
    return result
  }

  static String toViewName(name) { 
    if (name) { 
      return name[0].toUpperCase() + name[1 .. -1]
    }
    return name
  }

}
