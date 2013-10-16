package xj.mobile.model.graphics

import xj.mobile.model.*

class Shape extends ModelNode { 

  String getDefaultArgName(value = null) { 
	if (value instanceof List && 
		value.every{ it instanceof Number }) { 
	  if (value.size() == 4)
		return 'frame'
	  else if (value.size() == 2)
		return 'position'
	} else {  
	  return 'arg'
	}
  }

}