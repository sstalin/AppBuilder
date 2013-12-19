package xj.mobile.common

import xj.mobile.*
import xj.mobile.model.ui.*
import xj.mobile.lang.*

@Category(ViewProcessor)
class PageViewCategory { 

  // Assume the following declaration
  // def pages = []

  void processPageView() { 
    if (view) { 
      view.children.each { Widget widget -> 
		if (Language.isTopView(widget.widgetType)) {  
		  pages << widget
		}
      }
    }
  }

}