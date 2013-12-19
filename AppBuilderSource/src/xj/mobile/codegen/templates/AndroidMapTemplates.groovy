package xj.mobile.codegen.templates

import xj.mobile.model.properties.PropertyType

import static xj.mobile.codegen.CodeGenerator.InjectionPoint.*

import static org.codehaus.groovy.ast.ClassHelper.*

class AndroidMapTemplates { 

  static templates = [
	map1: [
	  method: '''@Override
protected boolean isRouteDisplayed() {
    return false;
}
'''
	]
  ]

}