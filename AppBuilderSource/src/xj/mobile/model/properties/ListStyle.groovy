
package xj.mobile.model.properties

class ListStyle extends Property { 

  static values = [:]

  static final ListStyle Default = new ListStyle('Default')
  static final ListStyle Grouped = new ListStyle('Grouped')

  String style
  
  private ListStyle(style) { 
    this.style = style
    values[style] = this
  }

  String toIOSString() { 
    def name = style
    switch (style) { 
    case 'Default':
      name = 'Plain'; break;
    }
    return 'UITableViewStyle' + name
  }

  String toShortString() { 
    style
  }

  String toString() { 
    "ListStyle.${style}"
  }

  static hasValue(name) { 
    values.hasKey(name)
  }

  static getValue(name) { 
    values[name]
  }

}

