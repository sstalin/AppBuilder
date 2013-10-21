package xj.mobile.api

import static xj.mobile.util.CommonUtil.capitalize

class AndroidPropertyDef extends PropertyDef {  

  String xmlName

  def getSetterTemplate() {
    setter ?: "\${name}.set${capitalize(this.delegate ?: this.name)}(\${value})"
  }
  
  def getGetterTemplate() {
    getter ?: "\${name}.get${capitalize(this.delegate ?: this.name)}()"
  }

}