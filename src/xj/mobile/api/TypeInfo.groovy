package xj.mobile.api

import org.codehaus.groovy.ast.ClassNode

import xj.translate.typeinf.ExternalTypeInfo

import static xj.translate.common.ClassProcessor.MethodInfo

import static xj.translate.Logger.info 

class TypeInfo implements ExternalTypeInfo { 

  @Delegate
  FrameworkTypeInfo framework   // handle getPropertyType
  
  Map variableMap
  Map methodMap  // Map<String, List<MethodInfo>> 

  Map parameterMap

  TypeInfo(framework, variableMap, methodMap = null) { 
    this.framework = framework 
    this.variableMap = variableMap
	this.methodMap = methodMap
  }

  ClassNode getVariableType(String name) { 
    def type = null
    type = parameterMap ? parameterMap[name] : null
	if (type == null)
	  type = variableMap ? variableMap[name] : null

	info "[TypeInfo] getVariableType(${name}) ==> ${type?.name}"

	return type
  }

  void addVariable(String name, ClassNode type) { 
	if (variableMap == null) variableMap = [:]
	variableMap[name] = type
  }

}