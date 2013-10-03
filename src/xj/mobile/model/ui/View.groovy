package xj.mobile.model.ui

import xj.mobile.model.*

class View extends Widget implements Composite { 

  List getWidgets() { 
	children.findAll { n -> n instanceof Widget }
  }

}