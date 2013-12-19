package xj.mobile.api

class PropertyDef {  
  
  String name
  String className
  String type
  String description
  String delegate
  boolean isRef
  boolean readOnly 

  def setter = null
  def getter = null 
  def indexedSetter = null
  def indexedGetter = null 

  def getSetterTemplate() {
    setter ?: "\${name}.${this.delegate ?: this.name} = \${value}"
  }
  
  def getGetterTemplate() {
    getter ?: "\${name}.${this.delegate ?: this.name}"
  }
  
}