
package xj.mobile.model.ui

import xj.mobile.model.ModelNode
import xj.mobile.lang.Language

class Widget extends ModelNode { 

  View parent  

  Language.ViewType getViewType() { 
    Lanaguge.getViewType(widgetType)
  }

}