
package xj.mobile.model.properties

class AccessoryType extends Property { 

  static values = [:]

  static final AccessoryType None       = new AccessoryType('None')
  static final AccessoryType Disclosure = new AccessoryType('Disclosure')
  static final AccessoryType Detail     = new AccessoryType('Detail')
  static final AccessoryType Checkmark  = new AccessoryType('Checkmark')

  String type

  private AccessoryType(type) { 
    this.type = type
    values[type] = this
  }

  String toIOSString() { 
    def name = type
    switch (type) { 
    case 'Disclosure':
      name = 'DisclosureIndicator'; break;
    case 'Detail':
      name = 'DetailDisclosureButton'; break; 
    }
    return 'UITableViewCellAccessory' + name
  }

  String toShortString() { 
    type
  }

  String toString() { 
    "AccessoryType.${type}"
  }

  String toAndroidJavaString() { 
    String ic = androidIconNames[type]
    if (ic) 
      return 'android.R.drawable.' + ic
    else 
      return null
  }

  String toAndroidXMLString() { 
    String ic = androidIconNames[type]
    if (ic) 
      return '@android:drawable/' + ic
    else 
      return null
  }

  static hasValue(name) { 
    values.hasKey(name)
  }

  static getValue(name) { 
    values[name]
  }

  static androidIconNames = [ 
    'None'       : '',
    'Disclosure' : 'ic_media_play',
    'Detail'     : 'ic_menu_more',
    'Checkmark'  : 'checkbox_on_background',
  ]

}

