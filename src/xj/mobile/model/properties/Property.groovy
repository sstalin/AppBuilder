
package xj.mobile.model.properties

//
// common super class of property value types 
//

class Property {

  String toShortString() { 
	toString()
  }

  String toIOSString() { 
	''
  }

  String toAndroidJavaString() { 
	''
  }

  String toAndroidXMLString() { 
	''
  }

  String getRawString() { 
	toString()
  }

  static boolean isCompatible(value) { 
	(value instanceof String)
  }

} 
