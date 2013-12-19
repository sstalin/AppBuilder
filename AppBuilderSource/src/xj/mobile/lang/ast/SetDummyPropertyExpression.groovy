
package xj.mobile.lang.ast

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.*

import xj.mobile.builder.ListEntity
import xj.mobile.common.ViewProcessor

import xj.translate.typeinf.TypeCategory

class SetDummyPropertyExpression extends Expression {

  ViewProcessor vp
  ListEntity entity 
  String attribute
  Expression valueExpression;

  ClassNode attributeType // the type of the attribute 

  public SetDummyPropertyExpression(ViewProcessor vp,
									ListEntity entity, 
									String attribute,
									Expression valueExpression) { 
	this.vp = vp
	this.entity = entity
	this.attribute = attribute
	this.valueExpression = valueExpression
  }

  public Expression transformExpression(ExpressionTransformer transformer) {
    Expression ret = new SetDummyPropertyExpression(vp, entity, attribute,
													transformer.transform(valueExpression));
    ret.setSourcePosition(this);
    return ret;
  }

  public String toString() {
    return super.toString() + "[" + vp.view.id + " . " + entity._dummy_ + " . " + attribute + " = " + valueExpression + "]";
  }

  public String getText() {
    return '' + entity._dummy_ + '.' + attribute + ' = ' + valueExpression.getText() + ' : ' + attributeType;
  }

  public ClassNode getValueType() {
    return valueExpression?.getType();
  }

  public void visit(GroovyCodeVisitor visitor)  { }
 
}