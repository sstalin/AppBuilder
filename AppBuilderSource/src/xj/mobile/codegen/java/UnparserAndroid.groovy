
package xj.mobile.codegen.java

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.*

import xj.translate.java.UnparserJava

import xj.mobile.codegen.EntityUnparser
import xj.mobile.codegen.UnparserViewProperty
import xj.mobile.lang.ast.*

@Mixin(xj.mobile.codegen.UnparserViewProperty)
class UnparserAndroid extends UnparserJava { 

  String target = 'android'
  
  EntityUnparser entityUnparser 

  @Delegate
  UnparserDummyProperty dummyUnparser = new UnparserDummyProperty(this)

  String expression(Expression exp, ClassNode expectedType = null) { 
    if (exp) { 
      switch (exp.class) { 
      case SetViewPropertyExpression:
		return unparseSetViewProperty(exp)
      case GetViewPropertyExpression:
		return unparseGetViewProperty(exp)
	  case SetDummyPropertyExpression:
		return unparseSetDummyProperty(exp)
	  case GetDummyPropertyExpression:
		return unparseGetDummyProperty(exp)
	  case EntityMethodCallExpression:
		def eh = exp.vp.contentHandler?.findSectionHandler(exp.entity)
		if (eh) { 
		  return eh.unparseEntityMethodCallExpression(exp)
		}
		return super.expression(exp, expectedType)  
	  
	  case PropertyExpression:
		def text = entityUnparser?.unparsePropertyExpression(exp)
		return text ?: super.expression(exp, expectedType)  
	  case MethodCallExpression:
		def text = entityUnparser?.unparseMethodCallExpression(exp)
		return text ?: super.expression(exp, expectedType)  
      default:
		return super.expression(exp, expectedType)      
      }
    }
    return ''
  }

}