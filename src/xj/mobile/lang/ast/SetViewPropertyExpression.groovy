
package xj.mobile.lang.ast

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.*

class SetViewPropertyExpression extends ViewPropertyExpression {

  Expression valueExpression;

  public SetViewPropertyExpression(String parentView,
								   String viewName,
								   String attribute, 
								   Expression valueExpression) {
	super(parentView, viewName, attribute)
    this.valueExpression = valueExpression
  }

  public Expression transformExpression(ExpressionTransformer transformer) {
    Expression ret = new SetViewPropertyExpression(parentView, viewName, attribute,
												   transformer.transform(valueExpression));
	ret.attributeType = attributeType
	ret.viewType = viewType
    ret.setSourcePosition(this);
    return ret;
  }

  public String toString() {
    return super.toString() + " = " + valueExpression;
  }

  public String getText() {
    return '' + viewName + '.' + attribute + ' = ' + valueExpression.getText() + ' : ' + 
	       attributeType + ' ' + viewType;
  }

  public ClassNode getValueType() {
    return valueExpression?.getType();
  }

}