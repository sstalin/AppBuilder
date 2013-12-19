
package xj.mobile.api

import xj.mobile.model.properties.Property

import static xj.translate.Logger.info
import static xj.mobile.util.CommonUtil.escapeString

class AndroidAttributeHandler extends AttributeHandler { 

  AndroidAttributeHandler() { 
    apiResolver = APIResolver.getAPIResolver('Android')

  }

  def getAttributeValue(String name, value, String type = null) { 
	info "[AndroidAttributeHandler] getAttributeValue ${name}: ${value?.class?.name}  type = ${type}"

	if (value instanceof Property) { 
	  //return value.toAndroidJavaString()
	} else if (type != null) { 
	  //def ntype = AndroidAPIResolver.nativeTypeMap[type]
	  //if (ntype instanceof String) type = ntype 
	  if (type == 'String') {
		if (value != null) { 
		  value = escapeString(value.toString())
		} else { 
		  value = ''
		}
		return "\"${value}\""
	  } else if (value instanceof List) { 
		if (type in [ 'CGSize', 'CGPoint' ] && 
			value.size() == 2) { 
		  //return "${type}Make(${value.join(', ')})" 
		} else if (type == 'CGRect' && 
				   value.size() == 4) {
		  //return "CGRectMake(${value.join(', ')})" 
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
		return "\"${value}\""
	  } else { 
		
	  }
    }
	return value
  }

  /*
  def getAttributeValue(String name, value, String type = null) { 
	switch (name) { 
	case 'text': 
	case 'url':
	  return value ? "\"${value}\"" : '""'
	default: 
	  return value  
	}
	return null
  }
  */

}