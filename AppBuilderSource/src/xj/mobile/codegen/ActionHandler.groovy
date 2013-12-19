
package xj.mobile.codegen

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.control.*
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.*
import org.codehaus.groovy.ast.builder.AstBuilder

import xj.mobile.common.*
import xj.mobile.model.ModelNode
import xj.mobile.model.sm.Transition
import xj.mobile.model.impl.ClassModel
import xj.mobile.lang.ast.*

import xj.translate.common.Unparser
import xj.translate.common.ModuleProcessor

import static xj.mobile.lang.ast.PrettyPrinter.*
import static xj.mobile.lang.ast.Util.*
import static xj.mobile.util.CommonUtil.*
import static xj.translate.Logger.info 

class ActionHandler {

  ViewProcessor viewProcessor
  ClassModel classModel

  ActionTransformer transformer

  ActionHandler() { }

  ActionHandler(ViewProcessor viewProcessor, ClassModel classModel) { 
    this.viewProcessor = viewProcessor
	this.classModel = classModel
  }

  void setContext(ViewProcessor viewProcessor, ClassModel classModel = null) { 
    this.viewProcessor = viewProcessor
	this.classModel = classModel ?: viewProcessor?.classModel
	if (viewProcessor)
	  viewProcessor.generator.unparser.setExternalTypeInfo(viewProcessor.typeInfo)
  }

  Statement transformAction(Statement stmt, Parameter[] params, ModelNode widget) {
    transformer.setContext(viewProcessor, widget, params)
    stmt.visit(transformer)
    return stmt
  }

  // srcInfo.code is a ClosureExpression
  String generateActionCode(Map srcInfo, ModelNode widget) { 
    String actionCode = null
	def unparser = viewProcessor.generator.unparser

    def src = srcInfo?.code
    if (src instanceof ClosureExpression && viewProcessor) { 

	  if (srcInfo.param) { 
		viewProcessor.typeInfo.parameterMap = srcInfo.param
	  }

      info '[ActionHandler] Action code pre-transform:\n' + print(src, 2)
      def writer = new StringWriter()
      src.code.visit new groovy.inspect.swingui.AstNodeToScriptVisitor(writer)
      info '[ActionHandler] Action unparsed pre-transform:\n' + writer
      info "[ActionHandler] current class: ${ModuleProcessor.currentClassProcessor.name}"

      def code = xj.translate.ASTUtil.copyStatement(src.code, src.variableScope)
      def params = src?.parameters //getClosureParameters(src)

      def transformedAction = transformAction(code, params, widget)
      //info '[ActionHandler] Action code post-transform, before unparse:\n' + print(code, 2)

      info '[ActionHandler] Action params: ' + params
      info '[ActionHandler] Action code post-transform:\n' + print(code, 2)

      actionCode = unparser.unparse(transformedAction)

      info '[ActionHandler] Action unparsed post-transform:\n' + actionCode

      if (srcInfo.decl) { 
		srcInfo.decl.each { d -> 
		  info "[ActionHandler] updates ${d.updates}"
		  if (d.updates) { 
			def updateCode = generateUpdateCode(d.updates, widget, src.variableScope)
			if (updateCode) { 
			  actionCode += "\n${updateCode}"
			}
		  }
		}	
      }

	  viewProcessor.typeInfo.parameterMap = null
    }

	def updates = srcInfo?.updates
	if (updates) { 
	  def updateCode = generateUpdateCode(updates, widget, src?.variableScope)
	  if (updateCode) { 
		if (actionCode) { 
		  actionCode +=  "\n${updateCode}"
		} else { 
		  actionCode = updateCode
		}
	  }
	}

    return actionCode
  }

  def generateUpdateCode(Set updates, ModelNode widget, VariableScope scope = null) { 
	def updateCode = null

	if (updates) { 
	  updates.each { u -> 
		def w = viewProcessor.getWidget(u[0])
		if (w) { 
		  def updateSrc = w["${u[1]}.src"]?.code
		  if (updateSrc) { 
			def ucode = generateUpdateCode(updateSrc, widget, w.widgetType, u[0], u[1], scope)
			if (updateCode) { 
			  updateCode += "\n${ucode}"
			} else { 
			  updateCode = ucode
			}
		  }
		}
	  }
	}
	return updateCode
  }

  // src is an update Expression
  def generateUpdateCode(Expression src, ModelNode widget, String wtype, String wname, 
						 String attribute, VariableScope scope = null) { 
	if (src) { 
	  //def unparser = classModel.unparser
	  def unparser = viewProcessor.generator.unparser

	  def updateExp = xj.translate.ASTUtil.copyExpression(src, scope) 
	  def exp = new SetViewPropertyExpression(viewProcessor.view.id, wname, attribute, updateExp)	  
	  exp.viewType = wtype
	  if (widget?.parent.embedded) exp.owner = 'owner'
	  def stmt = new ExpressionStatement(exp)			
	  def transformedUpdate = transformAction(stmt, null, widget)
	  
	  info "[ActionHandler] update code ${stmt}"
	  def ucode = unparser.unparse(transformedUpdate)
	  info "[ActionHandler] update code unparsed: ${ucode}"
	  return ucode
	}		
	return null
  }

}