package xj.mobile.common

import xj.mobile.model.ui.View

class ViewProcessorFactory { 

  static factory

  static iosFactory = [ 
    View:               xj.mobile.ios.DefaultViewProcessor,
    TabbedView:         xj.mobile.ios.TabbedViewProcessor,
    NavigationView:     xj.mobile.ios.NavigationViewProcessor,
    ListView:           xj.mobile.ios.ListViewProcessor,  
    ExpandableListView: xj.mobile.ios.ListViewProcessor,  
    PageView:           xj.mobile.ios.PageViewProcessor,    
  ]

  static androidFactory = [ 
    View:               xj.mobile.android.DefaultViewProcessor,
    TabbedView:         xj.mobile.android.TabbedViewProcessor,
    NavigationView:     xj.mobile.android.NavigationViewProcessor,
    ListView:           xj.mobile.android.ListViewProcessor,
    ExpandableListView: xj.mobile.android.ExpandableListViewProcessor,
    PageView:           xj.mobile.android.PageViewProcessor,        
  ]

  static factories = [
    'ios' : iosFactory,
    'android' : androidFactory,  
  ]
 
  static ViewProcessor getViewProcessor(View view, String viewName) { 
    if (view && factory) { 
      def c = factory[view.widgetType]
      if (c)
	return c.newInstance(view, viewName) 
    }
    return null
  }

  static setFactory(String target) { 
    factory = factories[target.toLowerCase()]
  }

}
