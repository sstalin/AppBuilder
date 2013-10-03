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
import static xj.translate.Logger.info 

class AndroidActionHandler extends ActionHandler { 

  static Android_Transformer = new AndroidTransformer()

  AndroidActionHandler() { 
    transformer = Android_Transformer
  }

  AndroidActionHandler(ViewProcessor viewProcessor, ClassModel classModel) { 
    super(viewProcessor, classModel)
    transformer = Android_Transformer
  }

}

class AndroidTransformer extends ActionTransformer { 
    
  Expression transform(Expression exp) { 
    info "[AndroidTransformer] transform: ${ASTClassName(exp)}  ${exp.text}"
      
    if (exp instanceof MethodCallExpression) { 
      //def obj = exp.objectExpression
      def mtd = exp.methodAsString
      def args = exp.arguments
	
      if (mtd == 'push') { 	  
		def target = args.getExpression(0).name
		def activityClass = viewProcessor.vhp.findViewProcessor(target)?.viewName

		// startActivity(new Intent(this, act.class))
		exp.setObjectExpression(null)
		exp.setMethod(new ConstantExpression('startActivity'))
		args = new ArgumentListExpression(
		  new ConstructorCallExpression(
			new ClassNode('Intent', 0, null),
			new  ArgumentListExpression(
			  new VariableExpression("${viewProcessor.viewName}.this"),
			  new ConstantExpression("${activityClass}.class"))))
		exp.setArguments(args)
		return exp
      } else if (mtd == 'println') { 
		return exp
      }
    }
    return super.transform(exp)
  }

  boolean needTransformSetViewProperty(String viewName, String attribute) { 
    return true
    //return false
  }

  boolean needTransformGetViewProperty(String viewName, String attribute) { 
    return true
    //return false
  }

}

