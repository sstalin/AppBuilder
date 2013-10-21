
package xj.mobile.codegen

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.control.*
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.*
import org.codehaus.groovy.ast.builder.AstBuilder

import xj.mobile.common.*
import xj.mobile.lang.ast.*
import xj.mobile.model.impl.ClassModel

import xj.translate.common.Unparser
import xj.translate.common.ModuleProcessor

import static xj.mobile.lang.ast.PrettyPrinter.*
import static xj.mobile.lang.ast.Util.*
import static xj.mobile.util.CommonUtil.*
import static xj.translate.Logger.info 

class IOSActionHandler extends ActionHandler {

  static iOS_Transformer = new IOSTransformer()   

  IOSActionHandler() { 
    transformer = iOS_Transformer
  }

  IOSActionHandler(ViewProcessor viewProcessor, ClassModel classModel) { 
    super(viewProcessor, classModel)
    transformer = iOS_Transformer
  }
  
  def injectCode(String widgetName, String actionEvent, String actionCode, String actionName, 
				 String actionTemplate, String targetTemplate) { 
	def engine = CodeGenerator.getCodeGenerator('ios').engine

	def binding = [ name : widgetName,
					actionName : actionName,
					event : actionEvent, 
					actionBody : actionCode ?: '',
					indent: xj.mobile.util.CommonUtil.&indent ]
	if (actionTemplate) { 
	  def template = engine.createTemplate(actionTemplate).make(binding)
	  classModel.methodScrap += template.toString()
	}
	if (targetTemplate) { 
	  def template = engine.createTemplate(targetTemplate).make(binding)
	  classModel.loadViewBodyScrap += ('\n' + template.toString() + '\n')
	}
  }

}

class IOSTransformer extends ActionTransformer { 

  Expression transform(Expression exp) { 
    info "[IOSTransformer] transform: ${ASTClassName(exp)}  ${exp.text}"
      
    if (exp instanceof MethodCallExpression) { 
      def obj = exp.objectExpression
      def mtd = exp.methodAsString
      def args = exp.arguments
	
      if (mtd == 'push') { 
		// push for navigation view, require View as the current class 
		exp.objectExpression = new PropertyExpression(
		  new VariableExpression("this"),
		  new ConstantExpression("navigationController"))
		return exp
      } else if (mtd == 'println') { 
		return exp
      }
    }
    return super.transform(exp)
  }

  boolean needTransformSetViewProperty(String viewName, String attribute) { 
    return true
  }

  boolean needTransformGetViewProperty(String viewName, String attribute) { 
    return true
  }

} 
