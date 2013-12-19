
package xj.mobile.model.properties

class PropertyList extends Property {

  def values = []

  String toShortString() { 
	'[' + values.collect{ v -> v.toShortString() }.join(', ') + ']'
  }

  String toIOSString() { 
	values.collect{ v -> v.toIOSString() }.join(' | ') 
  }

  String toAndroidJavaString() { 
	values.collect{ v -> v.toAndroidJavaString() }.join(' | ')
  }

  String toAndroidXMLString() { 
	'[' + values.collect{ v -> v.toAndroidXMLString() }.join(', ') + ']'
  }

  String getRawString() { 
	values.toString()
  }

  String toString() { 
	values.toString()
  }

} 
