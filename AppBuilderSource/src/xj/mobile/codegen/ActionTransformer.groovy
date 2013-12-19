package xj.mobile.codegen

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.control.*
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.*
import org.codehaus.groovy.syntax.Token
import org.codehaus.groovy.ast.builder.AstBuilder

import xj.mobile.common.*
import xj.mobile.model.ModelNode
import xj.mobile.lang.ast.*
import xj.mobile.lang.Language

import static xj.mobile.lang.ast.PrettyPrinter.*
import static xj.translate.Logger.info 

/*
 *  to transform expression to get/set view properties when necessary  
 */
class ActionTransformer extends ClassCodeExpressionTransformer { 

  ViewProcessor viewProcessor
  ModelNode widget
  Parameter[] params  // parameter names of the action closure 

  void setContext(ViewProcessor viewProcessor, ModelNode widget, Parameter[] params = null) {  
    this.viewProcessor = viewProcessor
    this.widget = widget
    this.params = params
  }

  // handle attributes with omitted view object (the current object)  
  Expression transformVariableExpression(VariableExpression exp, boolean transformResults = false) { 
    if (exp) { 
      def decl = findVariableDeclaration(exp.name, widget)
      info "[ActionTransformer] Variable: ${exp.name} findVariableDeclaration ${decl != null}"
      if (widget &&
		  !(exp.name in viewProcessor.widgetTable.keySet()) && 
		  decl == null && 
		  !isParam(exp.name)) { 
		if (viewProcessor.generator.attributeHandler.apiResolver.hasPropertyDef(widget.nodeType, exp.name)) { 
		  if (transformResults && needTransformGetViewProperty(widget.id, exp.name)) { 
			// consider an attribute of the current widget
			def gvpExp = new GetViewPropertyExpression(viewProcessor.view.id, 
													   widget.id, exp.name)
			def type = viewProcessor.getWidgetAttributeType(widget, exp.name)
			gvpExp.setAttributeType(type)
			gvpExp.viewType = widget.widgetType
			if (widget.parent.embedded) gvpExp.owner = 'owner'
			info "[ActionTransformer] GetViewPropertyExp: ${viewProcessor.view.id} ${widget.id} ${widget['#type']} ${exp.name} ${gvpExp.attributeType} ${xj.translate.typeinf.TypeCategory.getActualType(gvpExp)}"
			return gvpExp
		  } else { 
			return new PropertyExpression(
			  new VariableExpression(widget.id),
			  new ConstantExpression(exp.name))
		  }
		}
      }
      return super.transform(exp)
    }
    return exp 
  }

  Expression transformPostfixExpression(PostfixExpression exp) { 
	if (exp) { 
	  def exp1 = transform(exp.expression)
	  if (exp1 instanceof GetDummyPropertyExpression) { 
		String op = '+'
		if (exp.operation.text == '++') op = '+'
		else if (exp.operation.text == '--') op = '-'
		def exp2 = new SetDummyPropertyExpression(exp1.vp, exp1.entity, exp1.attribute,
												  new BinaryExpression(exp1, Token.newSymbol(op, 0, 0),
																	   new ConstantExpression(1, true)))
		exp2.attributeType = exp1.attributeType 
		return exp2
	  } else {
		exp.setExpression(exp1)
		return exp
	  }
	}
	return exp
  }

  Expression transformBinaryExpression(BinaryExpression exp) { 
	if (exp) { 
	  def vp = viewProcessor
	  boolean embedded = viewProcessor.view.embedded
	  if (embedded) { 
		vp = viewProcessor.view.parent?.viewProcessor
	  }

      if (exp.operation.text == '=') { 
		// handle possible set view property 
		if (exp.leftExpression instanceof VariableExpression) { 
		  exp.leftExpression = transformVariableExpression(exp.leftExpression, false)
		}
		if (exp.leftExpression instanceof PropertyExpression) { 
		  def objExp = exp.leftExpression.objectExpression
		  def propExp = exp.leftExpression.property
		  if (objExp instanceof VariableExpression &&
			  propExp instanceof ConstantExpression) { 
			if (vp && objExp.name in vp.widgetTable.keySet()) { 
			  if (needTransformSetViewProperty(objExp.name, propExp.text)) { 
				def svpExp = new SetViewPropertyExpression(viewProcessor.view.id, 
														   objExp.name, propExp.text,
														   transform(exp.rightExpression))
				def w = vp.widgetTable[objExp.name]
				if (w) { 
				  def type = vp.getWidgetAttributeType(w, propExp.text)
				  svpExp.viewType = w.widgetType
				  svpExp.attributeType = type
				  if (embedded) svpExp.owner = 'owner'
				}
				info "[ActionTransformer] SetViewPropertyExp: ${viewProcessor.view.id} ${objExp.name} ${w['#type']} ${propExp.text} ${svpExp.attributeType}"
				return svpExp
			  }

			} else if (isParam(objExp.name)) { 
			  def type = params.find { p -> p.name == objExp.name }.type 
			  String tname = type.name
			  if (tname.startsWith('xj.mobile.lang.madl.')) { 
				tname = tname.substring(20)
			  }
			  if (Language.isWidget(tname)) { 
				// obj is a parameter and its type is a widget 
				def svpExp = new SetViewPropertyExpression(viewProcessor.view.id, 
														   objExp.name, propExp.text,
														   transform(exp.rightExpression))
				def attrType = viewProcessor.getWidgetAttributeType(tname, null, propExp.text)
				svpExp.viewType = tname
				svpExp.attributeType = attrType
				if (embedded) svpExp.owner = 'owner'
				info "[ActionTransformer] SetViewPropertyExp: ${viewProcessor.view.id} ${objExp.name} ${tname} ${propExp.text} ${svpExp.attributeType}"
				return svpExp
			  }
			} else { 
			  boolean isDummy = false
			  def eh = viewProcessor.findEntityHandler(widget)
			  if (eh) { 
				isDummy = (eh.entityDef._dummy_ == objExp.name)
			  }
			  if (isDummy) { 
				// obj is a dummy
				def sdpExp = new SetDummyPropertyExpression(viewProcessor, eh.entityDef, propExp.text,
															transform(exp.rightExpression))
				//def f = eh.entityClass.fields.find { it.name == propExp.text } 
				//def type = ClassHelper.getUnwrapper(new ClassNode(f.type))
				sdpExp.attributeType = getType(eh, propExp.text)
				return sdpExp
			  }
			}
		  }
		}
      } else if (exp.operation.text == '[') { 
		exp.leftExpression = transform(exp.leftExpression)
		exp.rightExpression = transform(exp.rightExpression)
		if (exp.leftExpression instanceof GetViewPropertyExpression) { 
		  exp.leftExpression.indexExpression = exp.rightExpression
		  return exp.leftExpression
		} else { 
		  return exp
		}
      }
	  return super.transform(exp)
	}
	return exp
  }

  Expression transformPropertyExpression(PropertyExpression exp) { 
	if (exp) { 
	  def vp = viewProcessor
	  boolean embedded = viewProcessor.view.embedded
	  if (embedded) { 
		vp = viewProcessor.view.parent?.viewProcessor
	  }

      def objExp = exp.objectExpression
      def propExp = exp.property
      if (objExp instanceof VariableExpression &&
		  propExp instanceof ConstantExpression) {  
		if (vp && objExp.name in vp.widgetTable.keySet()) { 
		  // obj is a widget in current view 
		  if (needTransformGetViewProperty(objExp.name, propExp.text)) { 
			def gvpExp = new GetViewPropertyExpression(viewProcessor.view.id, 
													   objExp.name, propExp.text)
			def w = vp.widgetTable[objExp.name]
			if (w) { 
			  def type = vp.getWidgetAttributeType(w, propExp.text)
			  gvpExp.setAttributeType(type)
			  gvpExp.viewType = w.widgetType
			  if (embedded) gvpExp.owner = 'owner'
			}
			info "[ActionTransformer] GetViewPropertyExp: ${viewProcessor.view.id} ${objExp.name} ${w['#type']} ${propExp.text} ${gvpExp.attributeType} ${xj.translate.typeinf.TypeCategory.getActualType(gvpExp)}"
			return gvpExp
		  }
		} else if (isParam(objExp.name)) { 
		  def type = params.find { p -> p.name == objExp.name }.type 
		  String tname = type.name
		  if (tname.startsWith('xj.mobile.lang.madl.')) { 
			tname = tname.substring(20)
		  }
		  if (Language.isWidget(tname)) { 
			// obj is a parameter and its type is a widget 
			def gvpExp = new GetViewPropertyExpression(viewProcessor.view.id, 
													   objExp.name, propExp.text)
			def attrType = viewProcessor.getWidgetAttributeType(tname, null, propExp.text)
			gvpExp.setAttributeType(attrType)
			gvpExp.viewType = tname
			if (embedded) gvpExp.owner = 'owner'
			info "[ActionTransformer] GetViewPropertyExp: ${viewProcessor.view.id} ${objExp.name} ${tname} ${propExp.text} ${gvpExp.attributeType} ${xj.translate.typeinf.TypeCategory.getActualType(gvpExp)}"
			return gvpExp
		  }
		} else { 
		  boolean isDummy = false
		  def eh = viewProcessor.findEntityHandler(widget)
		  if (eh) { 
			isDummy = (eh.entityDef._dummy_ == objExp.name)
		  }
		  if (isDummy) { 
			// obj is a dummy
			def gdpExp = new GetDummyPropertyExpression(viewProcessor, eh.entityDef, propExp.text)		
			gdpExp.setAttributeType(getType(eh, propExp.text))
			return gdpExp 
		  } else if (widget && needTransformGetViewProperty(widget.id, objExp.name)) { 
			// obj is not a widget in current view, assume it is a property of the current view 
			def gvpExp = new GetViewPropertyExpression(viewProcessor.view.id, 
													   widget.id, objExp.name)
			def type = viewProcessor.getWidgetAttributeType(widget, objExp.name)
			gvpExp.setAttributeType(type)
			gvpExp.viewType = widget.widgetType
			if (embedded) gvpExp.owner = 'owner'
			return new PropertyExpression(gvpExp, propExp)										 
		  }
		}
      }
	  return super.transform(exp)
	}
	return exp
  }

  Expression transformMethodCallExpression(MethodCallExpression exp) { 
	if (exp) { 
	  if (exp.objectExpression instanceof VariableExpression) { 
		String objname = exp.objectExpression.name
		String methodname = exp.methodAsString
		if (viewProcessor.hasClosureDef(objname) && 
		   methodname == 'call') { 
		  return new MethodCallExpression(new VariableExpression('this'), objname,
										  transform(exp.arguments))
		}

		def entities = viewProcessor.view.'#entities' 
		if (entities) { 
		  def entity = entities.find { it._name_ == objname }
		  if (entity) { 
			return new EntityMethodCallExpression(viewProcessor, entity, 
												  exp.methodAsString, 
												  transform(exp.arguments))
		  }
		}
	  }
	  return super.transform(exp)
	}
	return exp
  }

  Expression transform(Expression exp) { 
    info "[ActionTransformer] transform: ${ASTClassName(exp)}  ${exp.text}"
      
    if (exp instanceof VariableExpression) { 
      // handle attributes with omitted view object 
      info "[ActionTransformer] Variable: ${exp.name}"
      return transformVariableExpression(exp, true)
    } else if (exp instanceof PostfixExpression) {
	  return transformPostfixExpression(exp)
    } else if (exp instanceof BinaryExpression) { 
	  return transformBinaryExpression(exp)
    } else if (exp instanceof PropertyExpression) { 
	  return transformPropertyExpression(exp)
    } else if (exp instanceof MethodCallExpression) { 
	  return transformMethodCallExpression(exp)
    }
    return super.transform(exp)
  }

  boolean isParam(String name) { 
	name && params && params.any { p -> p.name == name }
  }

  static ClassNode getType(eh, name) { 
	//def f = eh.entityClass.fields.find { it.name == name }
	def f = eh.entityDef.fields.find { it.name == name }
	if (f) { 
	  //def type = ClassHelper.getUnwrapper(new ClassNode(f.type))

	  info "[ActionTransformer] getType() ${eh.entityDef._dummy_} ${name}: ${f.type} ${f.type.class}"
	  return f.type
	} 
	return ClassHelper.OBJECT_TYPE
  }

  protected SourceUnit getSourceUnit() { null }

  boolean needTransformSetViewProperty(String viewName, String attribute) { 
    return false
  }

  boolean needTransformGetViewProperty(String viewName, String attribute) { 
    return false
  }

  static findVariableDeclaration(String varname, ModelNode widget) { 
    if (varname && widget) { 
      def w = widget
      while (w instanceof ModelNode) { 
		//def decl = w['#info']?.declaration?.'varname'
		def viewInfo = w['#info']
		if (viewInfo) { 
		  def declarations = viewInfo.declarations
		  if (declarations) {  
			def decl = viewInfo.declarations[varname]
			if (decl) return decl
		  }
		}
		w = w.parent
      }
    }
    return null
  }


} 
