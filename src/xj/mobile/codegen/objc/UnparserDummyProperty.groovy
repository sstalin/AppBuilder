package xj.mobile.codegen.objc

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.*

import xj.mobile.lang.ast.*

import xj.translate.common.Unparser

import static org.codehaus.groovy.ast.ClassHelper.*
import static xj.translate.typeinf.TypeUtil.* 

class UnparserDummyProperty { 

  Unparser unparser

  UnparserDummyProperty(Unparser unparser) { 
	this.unparser = unparser
  }

  String unparseSetDummyProperty(SetDummyPropertyExpression exp) { 
    def value 
    def type = exp.attributeType
    if (type == ClassHelper.STRING_TYPE) { 
      value = unparser.convertToString(exp.valueExpression)
    } else { 
      value = unparser.expression(exp.valueExpression)  
	  if (isNumericalType(type)) { 
		type = wrapSafely(type)
		def tname = type.nameWithoutPackage
		value = "[NSNumber numberWith${tname}: ${value}]"
	  }
    }
	return "[data setObject:${value} forKey:k${exp.attribute.capitalize()}Key]"
  }

  String unparseGetDummyProperty(GetDummyPropertyExpression exp) { 
	def value = "[data objectForKey:k${exp.attribute.capitalize()}Key]"
	def type = exp.attributeType
	if (isNumericalType(type)) { 
	  type = wrapSafely(type)
	  def tname = type.nameWithoutPackage.toLowerCase()
	  if (tname == 'integer') tname = int
	  value = "[${value} ${tname}Value]"
	}
	return value
  }

}