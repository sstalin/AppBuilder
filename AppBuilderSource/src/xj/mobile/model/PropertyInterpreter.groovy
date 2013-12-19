
package xj.mobile.model

class PropertyInterpreter { 

  static ImageFileTypes = [
	'.jpg', '.jpeg',
	'.gif',
	'.png',
	'.bmp'
  ]

  static interpretAttributeValue(Class context, boolean plural, value) { 
	if (value instanceof PropertyModel) { 
	  return value.eval(context, plural)
	} else if (context?.isCompatible(value)) { 
	  def pmodel = new PropertyModel(value)
	  return pmodel.eval(context, plural)
	}
	return null
  }

  static interpretAttributeValue(String owner, String attr, String key, value) { 
	Class context = getContext(owner, attr, key, value)	
	boolean plural = isPlural(attr)
	return interpretAttributeValue(context, plural, value)
  }

  static boolean isPlural(String attr) { 
	attr && attr[-1] == 's'
  }

  static contextMap = [
	View: [
	  initialOrientation: 'InterfaceOrientations', 
	  supportedOrientations: 'InterfaceOrientations', 
	],
	Item: [ 
	  accessory: 'AccessoryType' 
	],
	Alert: [ 
	  style: 'AlertViewStyle' 
	]
  ] 

  static Class getContext(String owner, String attr, String key, value) {
    String clazz = null 
	if (attr == 'rightButton' && 
		(key == null || key == 'type')) { 
	  clazz = 'SystemButton'
    } else if (attr == 'color' ||
			   attr == 'shadow' ||
			   attr.endsWith('Color')) { 
	  clazz = 'Color' 
	} else if (attr == 'background') { 
	  def fname = value as String 
	  if (ImageFileTypes.every { ft -> !fname.endsWith(ft) }) { 
		clazz = 'Color' 
	  }
	} else if (attr == 'font' ||
			   attr.endsWith('Font')) { 
	  if (owner == 'Button') { 
		clazz = 'ButtonFont'
	  } else if (owner == 'Label') { 
		clazz = 'LabelFont'
	  } else { 
		clazz = 'Font'
	  } 
	} else if (attr == 'style') { 
	  if (owner == 'Item') { 
		clazz = 'CellStyle'
	  } else if (owner in [ 'ListView', 'ExpandableListView' ]) { 
		clazz = 'ListStyle'
	  }
	  //} else if (attr == 'accessory' && owner == 'Item') { 
	  //clazz = 'AccessoryType'
	} 
	if (!clazz && contextMap[owner]) { 
	  clazz = contextMap[owner][attr]
	}  
	if (!clazz && PropertyContextMap.contextMap[owner]) { 
	  clazz = PropertyContextMap.contextMap[owner][attr]
	}
	
    if (clazz) 
      return "xj.mobile.model.properties.${clazz}" as Class
    else
      return null
  }

}