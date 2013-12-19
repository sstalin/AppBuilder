package xj.mobile.codegen.java

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
    }
	"data.${exp.attribute} = ${value}"
  }

  String unparseGetDummyProperty(GetDummyPropertyExpression exp) { 
	"data.${exp.attribute}"
  }

}