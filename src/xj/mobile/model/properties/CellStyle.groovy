
package xj.mobile.model.properties

class CellStyle extends Property { 

  static values = [:]

  static final CellStyle Default = new CellStyle('Default')
  static final CellStyle Subtitle = new CellStyle('Subtitle')
  static final CellStyle DetailRight = new CellStyle('DetailRight')
  static final CellStyle DetailCenter = new CellStyle('DetailCenter')

  String style
  
  private CellStyle(style) { 
    this.style = style
    values[style] = this
  }

  String toIOSString() { 
    def name = style
    switch (style) { 
    case 'DetailRight':
      name = 'Value1'; break;
    case 'DetailCenter':
      name = 'Value2'; break;
    }
    return 'UITableViewCellStyle' + name
  }

  String toShortString() { 
    style
  }

  String toString() { 
    "CellStyle.${style}"
  }

  static hasValue(name) { 
    values.hasKey(name)
  }

  static getValue(name) { 
    values[name]
  }

}
