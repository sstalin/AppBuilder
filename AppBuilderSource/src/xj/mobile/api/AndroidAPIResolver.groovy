package xj.mobile.api

import org.ho.yaml.Yaml

import xj.mobile.codegen.templates.WidgetTemplates
import xj.mobile.model.properties.Font
import xj.mobile.common.ViewProcessor

import static xj.translate.Logger.info 
import static xj.mobile.lang.AttributeMap.*

class AndroidAPIResolver extends APIResolver {  

  static baseDir = 'lib/api/android'

  static fontAttrDef = 
    new AndroidPropertyDef(name: 'font', className: 'TextView', type: 'Font', 
						   readOnly: true,
						   setter: { name, value -> 
							 def code = []
							 String typeface = value.toAndroidTypeface() 
							 if (typeface) { 
							   code << "${name}.setTypeface(${typeface})"
							   ViewProcessor.currentViewProcessor.classModel.addImport('android.graphics.Typeface')
							 }
							 if(value.size > 1) { 
							   code << "${name}.setTextSize(TypedValue.COMPLEX_UNIT_SP, ${value.size})"
							   ViewProcessor.currentViewProcessor.classModel.addImport('android.util.TypedValue')
							 }
							 code.join(';\n')
						   } )
					
  static attributeDef = [
	TextView: [
	  font: fontAttrDef
	],

	TextEdit: [
	  font: fontAttrDef
	],
	Button: [
	  font: fontAttrDef
	],

  ]

  static UIRef = [:]

  static AndroidTypeMap = [
	'CharSequence,TextView.BufferType': 'String',
	'CharSequence': 'String',
  ]

  static String mapAndroidType(String rawType) { 
    AndroidTypeMap[rawType] ?: rawType
  }

  // attributes should not be accessible to user





  static {  
    info "[AndroidAPIResolver] initialize"

	def uipackages = [  
	  'android.app', 
	  'android.view', 
	  'android.webkit', 
	  'android.widget', 
	]
	
	uipackages.each {  pkg ->  
	  def ref = Yaml.load(new File(baseDir + "/${pkg}_PackageRefs.yml").text)
	  def classes = ref.Classes.name

	  //info "[AndroidAPIResolver] UI classes: ${classes}"

	  classes.each { cname -> 
		UIRef[cname] = pkg + '/' + cname
	  }
	}					 
  }

  static getUIDef(String className) { 
	if (className) {  
	  def cdef = UIRef[className]
	  if (cdef instanceof String) { 
		try { 
		  cdef = Yaml.load(new File(baseDir + "/${cdef}_Def.yml").text)
		  UIRef[className] = cdef
		} catch (IOException e) { 
		  cdef = null
		}
	  }
	  return cdef 
	}
	return null
  }

  AndroidAPIResolver() {
  }

  String getPlatform() { 'Android' }

  String getPlatformClassName(name) { 
	WidgetTemplates widgetTemp = WidgetTemplates.getWidgetTemplates('android') 
	def wtemp = widgetTemp.getWidgetTemplateByName(name)
	wtemp?.uiclass
  }
  
  PropertyDef findPropertyDef(String className, String propName, boolean writable = true) {
    info "[AndroidAPIResolver]  findPropertyDef() ${className} ${propName}"
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

	def cdef = getUIDef(className) // include all inherited attributes
	if (cdef) {
	  for (pname in aliases) { 
		cdef.attributes.each { name, prop ->
		  if (!result) { 
			if (name == pname) { 
			  // match found 
			  info "[AndroidAPIResolver]  findPropertyDef() ${className} ${propName} (${pname}) ==> ${className} ${name} ${prop.type}"

			  result = new AndroidPropertyDef(name: name, 
											  xmlName: prop.name, 
											  className: className, 
											  type: mapAndroidType(prop.type),
											  description: prop.explanation)
			}
		  }
		}
		if (result) break
	  }

	}

    return result
  }

  // find all the property defs for the specified class 
  // className is platform specific class 
  def findAllPropertyDefs(String className, excludeNames = null) { 
    def result = [:]
	def cdef = getUIDef(className) // include all inherited attributes
	if (cdef) {
	  cdef.attributes.each { name, prop ->
		if (!(name in filteredAttributes['Android']) &&
			(!excludeNames || !(name in excludeNames)))
		result[name] = new AndroidPropertyDef(name: name,
											  xmlName: prop.name, 
											  className: className, 
											  type: mapAndroidType(prop.type),
											  description: prop.explanation)
	  }
	}
    info "[AndroidAPIResolver]  findAllPropertyDefs() ${className} ${cdef? 'found' : 'not found'}"
    return result
  }

}