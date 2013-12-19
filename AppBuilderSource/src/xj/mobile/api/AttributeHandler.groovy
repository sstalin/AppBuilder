
package xj.mobile.api

/*
 *  Translate the attribute value to native value according to the type define in native API
 *  The values are in neutral form specified in the model  
 */
class AttributeHandler {

  APIResolver apiResolver 

  AttributeHandler() { 
  }

  def getAttributeValueForWidget(String widgetClass, String attrName, attrValue) { 
	def prop = apiResolver.findPropertyDef(widgetClass, attrName, true)
	return getAttributeValue(attrName, attrValue, prop?.type)
  }

  def getAttributeValue(String name, value, String type = null) { 

	return null
  }

}