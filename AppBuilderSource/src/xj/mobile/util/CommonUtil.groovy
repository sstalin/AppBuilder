package xj.mobile.util

import org.apache.commons.lang3.StringEscapeUtils

class CommonUtil { 

  // strip the extension of a filename
  static String getFileName(name) { 
    if (name) { 
      int i = name.lastIndexOf('.')
      if (i > 0) { 
		return name[0 ..< i]
      }
    }
    return name
  }

  static String capitalize(String name) { 
    if (name) { 
      return name.capitalize()
    }
    return null
  }

  static String indent(String code, int level = 1, String tab = '\t') { 
    if (code && level > 0) { 
      String pad = tab * level
      code = pad + code.replaceAll('\n', '\n' + pad)
    }
    return code
  }

  static int linesInString(String text) { 
	if (text != null) { 
	  return text.count('\n') + 1
	}
	return 1
  }

  static String escapeString(String str) { 
	String result = str
	if (str) { 
	  result = StringEscapeUtils.escapeJava(str)

	  //result = result.replaceAll('\\r?\\n', '\\\\n')
	}
	return result 
  }


  //
  // Encode/decode attribute values 
  //

  static String encodeAttributeValue(value) { 
	//println "Encode ${value}"
	String result = value
	if (value) { 
	  if (result.indexOf('\n') >= 0) 
		result = result.replaceAll('(\\r?\\n)', '&br;')
	  if (result.indexOf(',') >= 0) 
		result = result.replaceAll(',', '&comma;')	  
	}
	return result 
  }


  static String decodeAttributeValue(value) { 
	//println "Encode ${value}"
	String result = value
	if (value) { 
	  if (result.indexOf('&br;') >= 0) 
		result = result.replaceAll('&br;', '\\\\n')
	  if (result.indexOf('&comma;') >= 0) 
		result = result.replaceAll('&comma;', ',')	  
	}
	return result 
  }
}