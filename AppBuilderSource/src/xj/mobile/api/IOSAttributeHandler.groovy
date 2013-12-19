
package xj.mobile.api

import xj.mobile.model.properties.Property

import static xj.translate.Logger.info 
import static xj.mobile.util.CommonUtil.escapeString

class IOSAttributeHandler extends AttributeHandler { 

  IOSAttributeHandler() { 
    apiResolver = APIResolver.getAPIResolver('iOS')
  }

  def getAttributeValue(String name, value, String type = null) { 
	info "[IOSAttributeHandler] getAttributeValue ${name}: ${value?.class?.name}  type = ${type}"

	if (value instanceof Property) { 
	  return value.toIOSString()
	} else if (type != null) { 
	  def ntype = IOSAPIResolver.nativeTypeMap[type]
	  if (ntype instanceof String) type = ntype 
	  if (type == 'String') {
		if (value != null) { 
		  value = escapeString(value.toString())
		} else { 
		  value = ''
		}
		return "@\"${value}\""
	  } else if (value instanceof List) { 
		if (type in [ 'CGSize', 'CGPoint' ] && 
			value.size() == 2) { 
		  return "${type}Make(${value.join(', ')})" 
		} else if (type == 'CGRect' && 
				   value.size() == 4) {
		  return "CGRectMake(${value.join(', ')})" 
		}
	  }
	} else {
	  // no type given 
	  if (name in [ 'text', 'prompt', 'url', 'html' ]) { // string type
		if (value) { 
		  value = escapeString(value.toString())
		} else { 
		  value = ''
		}
		return "@\"${value}\""
	  } else { 
		
	  }
    }
	return value
  }

}