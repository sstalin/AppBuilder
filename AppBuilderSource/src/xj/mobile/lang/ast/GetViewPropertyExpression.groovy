
package xj.mobile.lang.ast

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.*

import xj.translate.typeinf.TypeCategory

class GetViewPropertyExpression extends ViewPropertyExpression {

  Expression indexExpression

  public GetViewPropertyExpression(String parentView,
								   String viewName,
								   String attribute,
								   Expression indexExpression = null) { 
	super(parentView, viewName, attribute)
    this.indexExpression = indexExpression
  }

  public Expression transformExpression(ExpressionTransformer transformer) {
    Expression ret = new GetViewPropertyExpression(parentView, viewName, attribute);
	ret.attributeType = attributeType
	ret.viewType = viewType
    ret.setSourcePosition(this);
    return ret;
  }

  public String toString() {
    return super.toString() +  " @ " + (indexExpression ?: '');
  }

  public String getText() {
    return '' + viewName + '.' + attribute + (indexExpression ? "[${indexExpression}]" : '') + 
           ' : ' + attributeType 
  }

}