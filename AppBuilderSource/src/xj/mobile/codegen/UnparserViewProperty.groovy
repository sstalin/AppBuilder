package xj.mobile.codegen

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.*

import xj.mobile.lang.ast.*
import xj.mobile.common.*

import static xj.mobile.model.PropertyInterpreter.interpretAttributeValue

@Category(xj.translate.common.Unparser)
class UnparserViewProperty { 

  // required 
  // String target
  
  String unparseSetViewProperty(SetViewPropertyExpression exp) { 
    def value //= expression(exp.valueExpression)   
    def attrType = exp.attributeType
	boolean valueExpUnparsed = false
    if (attrType == ClassHelper.STRING_TYPE) { 
      value = convertToString(exp.valueExpression)
	  valueExpUnparsed = true
    } else { 
	  value = interpretValueExpression(exp.valueExpression, exp.viewType, exp.attribute)
	  if (value == null) { 
		value = expression(exp.valueExpression)  
		valueExpUnparsed = true
	  }
    }
	String prefix = ''
	if (exp.owner && target == 'ios') 
	  prefix = exp.owner + '.'
	unparseSetViewProperty(target, exp.viewType, exp.viewName, exp.attribute, value, 			   
						   prefix, valueExpUnparsed)
  }

  String unparseGetViewProperty(GetViewPropertyExpression exp) { 
    def index = exp.indexExpression ? expression(exp.indexExpression) : null
	String prefix = ''
	if (exp.owner && target == 'ios') 
	  prefix = exp.owner + '.'
	unparseGetViewProperty(target, exp.viewType, exp.viewName, exp.attribute, index, prefix)
  } 

  static String unparseSetViewProperty(String target, 
									   String viewType, 
									   String viewName, 
									   String attribute, 
									   value, 
									   String prefix = null, 
									   boolean valueUnparsed = true) { 
	def generator = CodeGenerator.getCodeGenerator(target)
    if (generator) { 
	  def code = generator.generateSetAttributeCode(viewType, viewName, attribute, value, 
													null, prefix, valueUnparsed)	  
	  if (code) { 
		return code[1]
	  }

    }
    return "${viewName}.${attribute} = ${value}"
  }

  static String unparseGetViewProperty(String target, 
									   String viewType, 
									   String viewName, 
									   String attribute, 
									   index,
									   String prefix = null) {
	def generator = CodeGenerator.getCodeGenerator(target)
    if (generator) { 
	  def code = generator.generateGetAttributeCode(viewType, viewName, attribute, index, prefix)
	  if (code) { 
		return code
	  }
    }
    return "${viewName}.${attribute}"
  } 

  static interpretValueExpression(Expression valueExp, String viewName, String attrName) {
	def value = null
	if (valueExp instanceof VariableExpression) { 
	  value = interpretAttributeValue(viewName, attrName, null, valueExp.name)
	} else if (valueExp instanceof ConstantExpression) { 
	  value = interpretAttributeValue(viewName, attrName, null, valueExp.value)
	} else if (isConstantList(valueExp)) { 
	  def list = valueExp.expressions.collect{ e -> 
		if (e instanceof VariableExpression) 
		  e.name
		else if (e instanceof ConstantExpression)
		  e.value
		else 
		  null
	  }
	  value = interpretAttributeValue(viewName, attrName, null, list)
	} 
	return value
  }

  static boolean isConstantList(Expression exp) { 
	if (exp instanceof ListExpression) { 
	  return exp.expressions.every { e -> e instanceof VariableExpression || 
		                                  e instanceof ConstantExpression }
	} else { 
	  return false
	} 
  }

}