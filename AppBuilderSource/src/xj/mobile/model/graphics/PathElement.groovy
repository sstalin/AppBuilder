package xj.mobile.model.graphics

import xj.mobile.model.*

class PathElement extends ModelNode { 

  String getDefaultArgName(value = null) { 
	if (value instanceof List) 
	  return 'location'
	else 
	  return 'arg'
  }

}