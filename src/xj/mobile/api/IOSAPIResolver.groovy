package xj.mobile.api

import org.ho.yaml.Yaml

import xj.mobile.codegen.templates.WidgetTemplates

import static xj.translate.Logger.info 
import static xj.mobile.lang.AttributeMap.*

class IOSAPIResolver extends APIResolver {  
  
  static baseDir = 'lib/api/ios'

  static attributeDef = [
	UIButton: [
	  titleColor: new PropertyDef(name: 'titleColor', className: 'UIButton', type: 'UIColor', 
								  isRef: true, readOnly: false,
								  setter: '[${name} setTitleColor:${value} forState:UIControlStateNormal]', 
								  getter: '[${name} titleColorForState:UIControlStateNormal]'), 
	]
  ]

  static attributeDelegation = [
	UIButton: [
	  titleLabel: [ type: 'UILabel', prefix: 'title' ]
	]
  ]

  static nativeTypeMap = [
	'BOOL' : 'Boolean',
	'CGFloat' : 'BigDecimal',
	'NSInteger' : 'Integer',
	'NSUInteger' : 'Integer',
	'NSString' : 'String',
	'UIColor' : 'Color',
	'UIFont' : 'Font',

	'CGAffineTransform' : 'AffineTransform',					  
	'NSCalendar' : 'Calendar', 
	'NSDate' : 'Date', 
	'NSLocale' : 'Locale', 
	'NSTimeZone' : 'TimeZone',
	'NSTimeInterval' : 'TimeInterval', //'BigDecimal',

	'CGSize' : [ [ type: 'BigDecimal', name: 'width' ], 
				 [ type: 'BigDecimal', name: 'height' ] ], 
	'CGPoint' : [ [ type: 'BigDecimal', name: 'x' ], 
				  [ type: 'BigDecimal', name: 'y' ] ], 
	'CGRect' : [ [ type: 'BigDecimal', name: 'x' ], 
				 [ type: 'BigDecimal', name: 'y' ], 
				 [ type: 'BigDecimal', name: 'width' ], 
				 [ type: 'BigDecimal', name: 'height' ] ],
	
	'UIOffset' : [ [ type: 'BigDecimal', name: 'horizontal' ], 
				   [ type: 'BigDecimal', name: 'vertical' ] ],
	'UIEdgeInsets' : [ [ type: 'BigDecimal', name: 'top' ], 
					   [ type: 'BigDecimal', name: 'left' ], 
					   [ type: 'BigDecimal', name: 'bottom' ], 
					   [ type: 'BigDecimal', name: 'right' ] ], 


	// CGAffineTransform 
	// enum types 
  ]

  static UIKitRef = [:]
  static UIKitRelation = [:]

  static { 
    info "[IOSAPIResolver] initialize"

    def ref = Yaml.load(new File(baseDir + "/UIKit_FrameworkRefs.yml").text)
    //def classes = ref.Class.name
	def allNames = ref.Class.name + ref.Protocol.name + ref.Other.name

    //info "[IOSAPIResolver] UIKit classes: ${classes}"

    allNames.each { cname -> 
	  UIKitRef[cname] = cname
    }

    UIKitRelation = Yaml.load(new File(baseDir + "/UIKit_Relation.yml").text)

  }

  static getUIKitDef(String className) { 
	if (className) {  
	  def cdef = UIKitRef[className]
	  if (cdef instanceof String) { 
		try { 
		  cdef = Yaml.load(new File(baseDir + "/UIKit/${className.replaceAll(' ', '_')}_Def.yml").text)
		  UIKitRef[className] = cdef

		  def attrDels = attributeDelegation[className]
		  if (attrDels) { 
			attrDels.each { attrName, delInfo -> 
			  def delDef = getUIKitDef(delInfo.type) //UIKitRef[delInfo.type]
			  if (delDef) { 
				delDef.properties.each { name, prop ->
				  def delName = name
				  if (delInfo.prefix) { 
					delName = delInfo.prefix + name[0].toUpperCase() + name[1 .. -1]
				  }
				  def delProp = prop.clone()
				  delProp.delegate = "${attrName}.${name}"
				  cdef.properties[delName] = delProp

				  info "[IOSAPIResolver] add delegate attribute ${delName} in ${className}"
				}
			  }
			  cdef.properties.remove(attrName)
			}
		  }
		} catch (IOException e) { 
		  cdef = null
		}
	  }
	  return cdef 
	}
	return null
  }

  static loadAllUIKitDefs() { 
	UIKitRef.keySet().each { cname -> 
	  getUIKitDef(cname)
	}
  }

  static boolean isViewClass(String c) { 
	inheritsFrom(c, 'UIView')
  }

  static boolean inheritsFrom(String c1, String c2) { 
	if (c1 && c2 && c1 != c2) { 
	  def inheritsRel = UIKitRelation['inherits']
	  String c = c1
	  while (c != null) { 
		c = inheritsRel[c]
		if (c == c2) return true
		else if (c == 'NSObject') return false
	  }
	}
	return false 
  }

  static boolean conformsTo(String c1, String c2) { 
	if (c1 && c2 && c1 != c2) { 
	  def conformsRel = UIKitRelation['conforms'][c1]
	  if (conformsRel) return c2 in conformsRel
	}
	return false 
  }

  static boolean has_a(String c1, String c2) { 
	if (c1 && c2 && c1 != c2) { 
	  def hasRel = UIKitRelation['has'][c1]?.collect{ it[1] }
	  if (hasRel) return c2 in hasRel
	}
	return false 
  }

  static hasType(String c1) { 
	if (c1) { 
	  return UIKitRelation['has'][c1]?.collect{ it[1] }
	}
	return null 
  }

  // requires: fully loaded UIKitRef
  static allSubtypes(String c) { 
	if (c) { 
	  loadAllUIKitDefs()
	  return UIKitRef.keySet().findAll { n -> inheritsFrom(n, c) }
	}
	return null
  }

  // requires: fully loaded UIKitRef
  static getEnumDef(String name) { 
	loadAllUIKitDefs()
	def enumDef = null
	UIKitRef.each { cname, cdef -> 
	  if (cdef instanceof Map) { 
		cdef.constants.each{ constName, constDef ->
		  if (enumDef == null &&
			  constDef.name == name && //constDef.isEnum && 
			  constDef.values != null) { 
			enumDef = constDef
		  }
		}
	  }
	}
	return enumDef
  }

  IOSAPIResolver() {
  }

  String getPlatform() { 'iOS' }

  String getPlatformClassName(name) { 
	WidgetTemplates widgetTemp = WidgetTemplates.getWidgetTemplates('ios') 
	def wtemp = widgetTemp.getWidgetTemplateByName(name)
	wtemp?.uiclass
  }

  // class name is platform specific class 
  PropertyDef findPropertyDef(String className, String propName, boolean writable = true) {
    info "[IOSAPIResolver]  findPropertyDef() ${className} ${propName}"
    def result = null
	def aliases = getAliases(className, propName)
	def attrDef = attributeDef[className]
	if (attrDef) { 
	  for (pname in aliases) { 
		result = attrDef[pname]
		if (result) { 
		  return result
		}
	  }
	}

	def cdef = getUIKitDef(className)
    def cname = className
    while (cdef && !result) {
      //def aliases = getAliases(cname, propName)
	  for (pname in aliases) { 
		cdef.properties.each { name, prop ->
		  if (!result && (!writable || !prop.readonly)) { 
			if (name == pname) { 
			  // match found 
			  info "[IOSAPIResolver]  findPropertyDef() ${className} ${propName} (${pname}) ==> ${cname} ${name} ${prop.type}${prop.ref} ${prop.readonly ? 'read-only' : ''}"

			  result = new PropertyDef(name: name, className: cname, type: prop.type, 
									   isRef: prop.ref == '*',
									   readOnly: prop.readonly,
									   delegate: prop.delegate)
			}
		  }
		}
	   
		if (result) break
	  }

	  if (!result && cdef.conform) { 
		cdef.conform.each { protoName -> 
		  if (!result) { 
			int sp = protoName.indexOf(' ')
			if (sp > 0)
			  protoName = protoName[0 .. (sp - 1)]
			if (!(protoName in [ 'NSCoding', 'NSObject' ])) { 
			  def result2 = findPropertyDef(protoName, propName, writable)
			  if (result2) { 
				result = result2
			  }
			}
		  }
		}
		if (result) break
	  }

      if (cdef.inherit) { 
		cname = cdef.inherit[0]
		cdef = getUIKitDef(cname)
      } else { 
		cdef = null
      }
    }

	if (!result) { 
	  if (attributeDelegation[className]) { 
		attributeDelegation[className].each { attrName, delInfo ->
		  if (!result) { 
			result = findPropertyDef(delInfo.type, propName, writable) 
			if (result) { 
			  result.delegate = "${attrName}.${propName}"
			}
		  }
		}
	  }
	}

    return result
  }

  //
  // methods to retrieve all definitions in a class 
  //

  // find all the property defs for the specified class 
  // className is platform specific class 
  def findAllPropertyDefs(String className, excludeNames = null) { 
    def result = [:]
	def cdef = getUIKitDef(className)
    def cname = className
    while (cdef) {
	  cdef.properties.each { name, prop ->
		if (!result.containsKey(name) &&
			!(name in filteredAttributes['iOS']) &&
			(!excludeNames || !(name in excludeNames))) { 
		  result[name] = new PropertyDef(name: name, className: cname, type: prop.type, 
										 isRef: prop.ref == '*',
										 readOnly: prop.readonly,
										 description: prop.explanation)
		}
	  }
	  
	  if (cdef.conform) { 
		cdef.conform.each { protoName -> 
		  int sp = protoName.indexOf(' ')
		  if (sp > 0)
			protoName = protoName[0 .. (sp - 1)]
		  if (!(protoName in [ 'NSCoding', 'NSObject' ])) { 
			def result2 = findAllPropertyDefs(protoName, excludeNames)
			if (result2) { 
			  result2.each { k, d -> 
				if (!result.containsKey(k)) { 
				  result[k] = d
				}
			  }
			}
		  }
		}
	  }

      if (cdef.inherit) { 
		cname = cdef.inherit[0]
		cdef = getUIKitDef(cname)
      } else { 
		cdef = null
      }
    }
    info "[IOSAPIResolver]  findAllPropertyDefs() ${className} ${result? 'found' : 'not found'}"

    return result
  }

  // find all the method defs for the specified class 
  // className is platform specific class 
  def findAllMethodDefs(String className, excludeNames = null) { 
    def result = [:]
	def cdef = getUIKitDef(className)
    def cname = className
    while (cdef) {
	  cdef.methods.each { name, prop ->
		if (!result.containsKey(name) &&
			!(name in filteredAttributes['iOS']) &&
			(!excludeNames || !(name in excludeNames))) { 
		  result[name] = prop
		}
	  }
	  
	  if (cdef.conform) { 
		cdef.conform.each { protoName -> 
		  int sp = protoName.indexOf(' ')
		  if (sp > 0)
			protoName = protoName[0 .. (sp - 1)]
		  if (!(protoName in [ 'NSCoding', 'NSObject' ])) { 
			def result2 = findAllPropertyDefs(protoName, excludeNames)
			if (result2) { 
			  result2.each { k, d -> 
				if (!result.containsKey(k)) { 
				  result[k] = d
				}
			  }
			}
		  }
		}
	  }

      if (cdef.inherit) { 
		cname = cdef.inherit[0]
		cdef = getUIKitDef(cname)
      } else { 
		cdef = null
      }
    }
    info "[IOSAPIResolver]  findAllMethodDefs() ${className} ${result? 'found' : 'not found'}"

    return result
  }


}