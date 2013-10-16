package xj.mobile.api

import xj.mobile.api.APIResolver

import org.codehaus.groovy.ast.ClassNode

import xj.translate.typeinf.ExternalTypeInfo

import static org.codehaus.groovy.ast.ClassHelper.*

import static xj.mobile.api.APIResolver.makeType 

import static xj.translate.Logger.info 

class FrameworkTypeInfo { 

  private static typeInfo = [
	'iOS' : new FrameworkTypeInfo('iOS'),
	'Android' : new FrameworkTypeInfo('Android'),
  ]

  public static FrameworkTypeInfo getFrameworkTypeInfo(platform) { 
	typeInfo[platform]
  }

  static DEFAULT_TYPE = STRING_TYPE

  def apiResolver
  String platform 

  FrameworkTypeInfo(String platform) { 
	this.platform = platform
	apiResolver = APIResolver.getAPIResolver(platform)
  }
  
  ClassNode getPropertyType(String className, String propertyName) { 
    def type = null
	if (className.startsWith('xj.mobile.lang.madl.')) { 
	  className = className.substring(20)
	}

    def c = className
    while (type == null && c) { 
      def cmap = widgets[c]
      //println "${c}: ${cmap}"

      if (cmap)
		type = cmap[propertyName]

	  if (!type) { 
		type = apiResolver?.findWidgetPropertyType(className, propertyName) 
	  }

	  if (!type) 
		c = cmap?.super
	}

	if (!type) type = DEFAULT_TYPE
	//if (!type) type = OBJECT_TYPE

	info "[FrameworkTypeInfo] getPropertyType(${className}, ${propertyName}) ==> ${type.name}"

    return type 
  }

  Map widgets = [

    Label: [ super: 'Widget',
			 text: STRING_TYPE,
			 font: makeType('Font') ],

    Button: [ super: 'Label',
			  font: makeType('Font')],

    RadioButton: [ super: 'Button'], 

    CheckBox: [ super: 'Label',
				checked: boolean_TYPE ],

	/*
	NumberStepper: [ super: 'Widget' ], 
	*/

    Widget: [], 

  ]


}

