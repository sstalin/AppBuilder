package xj.mobile.lang.ast

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.*

import xj.translate.typeinf.TypeCategory

abstract class ViewPropertyExpression extends Expression {

  String viewName         // the id of the widget 
  String viewType         // the type of the widget

  String attribute        // the name of the attribute   
  ClassNode attributeType // the type of the attribute 


  String parentView       // the id of the view containing the widget, ?? not used 
  String owner            // owner of the widget in an embedded view 

  public ViewPropertyExpression(String parentView,
								String viewName,
								String attribute) { 
    this.parentView = parentView
    this.viewName = viewName
    this.attribute = attribute
  }

  public String toString() {
    return super.toString() + "[" + parentView + " . " + viewName + " . " +  attribute +  "]";
  }

  public void setAttributeType(ClassNode type) { 
    attributeType = type
    if (type != null) { 
      TypeCategory.setActualType(this, type)
    }
  }

  public void visit(GroovyCodeVisitor visitor)  { }

}