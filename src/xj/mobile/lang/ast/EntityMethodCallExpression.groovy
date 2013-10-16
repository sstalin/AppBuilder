
package xj.mobile.lang.ast

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.*

import xj.mobile.builder.ListEntity
import xj.mobile.common.ViewProcessor

import xj.translate.typeinf.TypeCategory

class EntityMethodCallExpression extends Expression {

  ViewProcessor vp
  ListEntity entity 
  String method
  Expression arguments 

  public EntityMethodCallExpression(ViewProcessor vp,
									ListEntity entity, 
									String method,
									Expression arguments = null) { 
	this.vp = vp
	this.entity = entity
	this.method = method 
	this.arguments = arguments
  }

  public Expression transformExpression(ExpressionTransformer transformer) {
	Expression ret = new EntityMethodCallExpression(vp, entity, method, arguments)
    ret.setSourcePosition(this)
    return ret
  }

  public String toString() {
    return super.toString() + "[" + vp.view.id + " . " + entity._name_ + " . " + mehtod + " (" + arguments + ")]";
  }

  public String getText() {
    return '' + entity._name_ + '.' + method + '()'
  }

  public void visit(GroovyCodeVisitor visitor)  { }
 
}