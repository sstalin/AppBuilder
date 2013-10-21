
package xj.mobile.model.properties

import org.codehaus.groovy.ast.ClassNode

class PropertyType extends ClassNode { 

  String propName 
  Class context

  PropertyType(String propName) { 
	super(propName, 0, null)
	this.propName = propName
	context = "xj.mobile.model.properties.${propName}" as Class
  }

  boolean isEnum() { true }

}