
package xj.mobile.lang.ast

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.*

import xj.mobile.builder.ListEntity
import xj.mobile.common.ViewProcessor

import xj.translate.typeinf.TypeCategory

class GetDummyPropertyExpression extends Expression {

  ViewProcessor vp
  ListEntity entity 
  String attribute
  //Expression indexExpression

  ClassNode attributeType // the type of the attribute 

  public GetDummyPropertyExpression(ViewProcessor vp,
									ListEntity entity, 
									String attribute) { 
	this.vp = vp
	this.entity = entity
	this.attribute = attribute
  }

  public Expression transformExpression(ExpressionTransformer transformer) {
    Expression ret = new GetDummyPropertyExpression(vp, entity, attribute);
    ret.setSourcePosition(this);
    return ret;
  }

  public String toString() {
    return super.toString() + "[" + vp.view.id + " . " + entity._dummy_ + " . " + attribute +  "]";
	//+ " @ " + (indexExpression ?: '') +  "]";
  }

  public String getText() {
    return '' + entity._dummy_ + '.' + attribute + ' : ' + attributeType 
	// (indexExpression ? "[${indexExpression}]" : '') + ' : ' + attributeType 
  }

  public void setAttributeType(type) { 
    attributeType = type
    if (type != null) { 
      TypeCategory.setActualType(this, type)
    }
  }

  public void visit(GroovyCodeVisitor visitor)  { }

}