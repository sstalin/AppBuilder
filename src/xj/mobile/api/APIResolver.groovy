package xj.mobile.api

import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.ClassHelper

import static org.codehaus.groovy.ast.ClassHelper.*

import static xj.mobile.lang.AttributeMap.*

abstract class APIResolver {  

  static def apiResolverMap = [
	'iOS'     : new IOSAPIResolver(),
	'Android' : new AndroidAPIResolver()
  ] 
  
  public static APIResolver getAPIResolver(platform) { 
	apiResolverMap[platform]
  }

  // name is platform neutral widget name 
  boolean hasPropertyDef(String name, String propName) { 
	def wdef = findCommonAttributeDef(name, propName) 
	if (wdef != null) return true
	wdef = findPropertyDef(getPlatformClassName(name), propName, false)
	return (wdef != null)
  }

  // name is platform neutral widget name 
  ClassNode findWidgetPropertyType(String name, String propName) { 
	def pdef = findPropertyDef(getPlatformClassName(name), propName, false)
	return makeType(pdef?.type)
  }

  String getPlatformClassName(name) { name } 

  // className is platform specific class 
  PropertyDef findPropertyDef(String className, String propName, boolean writable = true) { null }

  // find all the property defs for the specified class 
  // className is platform specific class 
  def findAllPropertyDefs(String className, excludeNames = null) { null }

  // find all the method defs for the specified class 
  // className is platform specific class 
  def findAllMethodDefs(String className, excludeNames = null) { null }

  // return a list of possible aliases of the propName, with the propName at the head
  List getAliases(String className, String propName) { 
	def aliases = [ propName ]

    def map = platformAliases[platform][className]
	if (map) { 
	  def a = map[propName]
	  if (a) { 
		if (a instanceof List)
		  aliases.addAll(a)
		else 
		  aliases << a
	  }
	}
	
	def a = commonAliases[propName]
	if (a) { 
	  if (a instanceof List)
		aliases.addAll(a)
	  else 
		aliases << a
	}
	return aliases
  }

  boolean matchProperty(String apiname, String name) { 
    if (apiname && name) { 
      return apiname.toLowerCase().contains(name.toLowerCase())
    }
    return false
  }

  static ClassNode makeType(String tname) { 
	if (tname) { 
	  switch (tname.toLowerCase()) { 
	  case 'bool': case 'boolean':
		return boolean_TYPE

	  case 'nsstring': case 'string': 
		return STRING_TYPE

	  case 'int': case 'integer':
		return int_TYPE

	  case 'short': return short_TYPE
	  case 'long': return long_TYPE
	  case 'float': return float_TYPE
	  case 'double': return double_TYPE

	  default: return ClassHelper.make(tname)
	  }
	}
	return null
  }

}