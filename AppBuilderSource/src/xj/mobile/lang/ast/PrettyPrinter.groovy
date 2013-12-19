
package xj.mobile.lang.ast

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.*

class PrettyPrinter { 
  
  static String print(ASTNode node, int level = 0) {
    if (node == null) return ''

    String indent = '' 
    if (level > 0) indent = ' ' * level

    switch (node.class) { 
      // expressions 
    case ClosureExpression:
      return indent + ASTClassName(node) + '\n' + print(node.code, level + 1)
    case PostfixExpression:
    case PrefixExpression:
      return indent + ASTClassName(node) + ": operator: \"${node.operation.text}\"\n" + 
	         print(node.expression, level + 1) 
    case DeclarationExpression:
    case BinaryExpression: 
      return indent + ASTClassName(node) + ": operator: \"${node.operation.text}\"\n" + 
	         print(node.leftExpression, level + 1) + '\n' +
		     print(node.rightExpression, level + 1)
    case RangeExpression:
      return indent + ASTClassName(node) + '\n' + 
	         print(node.fromExpression, level + 1) + '\n' +
		     print(node.toExpression, level + 1)
    case ElvisOperatorExpression:
    case TernaryExpression:
      return indent + ASTClassName(node) + '\n' + 
	         print(node.booleanExpression, level + 1) + '\n' + 
	         print(node.trueExpression, level + 1) + '\n' + 
	         print(node.falseExpression, level + 1)
    case AttributeExpression:
    case PropertyExpression:
      return indent + ASTClassName(node) + ': ' + node.text + '\n' + 
	         print(node.objectExpression, level + 1) + '\n' +
		     print(node.property, level + 1)


    case SetViewPropertyExpression:
      return indent + ASTClassName(node) + ': ' + node.text + '\n' + indent + 
	         ' owner: ' + node.owner + 
	         ' parentView: ' + node.parentView + 
	         '  viewName: ' + node.viewName + 
			 '  attribute: ' + node.attribute + '\n' + 
	  print(node.valueExpression, level + 1)      
    case GetViewPropertyExpression:
      return indent + ASTClassName(node) + ': ' + node.text + '\n' + indent + 
	         ' owner: ' + node.owner + 
	         ' parentView: ' + node.parentView + 
			 '  viewName: ' + node.viewName + 
			 '  attribute: ' + node.attribute + 
	  (node.indexExpression? '\n' + print(node.indexExpression, level + 1) : '')    
    case SetDummyPropertyExpression:
      return indent + ASTClassName(node) + ': ' + node.text + '\n' + indent + 
	         ' view: ' + node.vp.view.id + 
			 '  entity: ' + node.entity['class'].name + 
			 '  attribute: ' + node.attribute + '\n' + 
	  print(node.valueExpression, level + 1)      
    case GetDummyPropertyExpression:
      return indent + ASTClassName(node) + ': ' + node.text + '\n' + indent + 
	         ' view: ' + node.vp.view.id + 
			 '  entity: ' + node.entity['class'].name + 
			 '  attribute: ' + node.attribute
    case EntityMethodCallExpression:
      return indent + ASTClassName(node) + ': ' + node.text + '\n' + indent + 
	         ' view: ' + node.vp.view.id + 
			 '  entity: ' + node.entity['class'].name + 
			 '  method: ' + node.method + '\n' + 
	  print(node.arguments, level + 1)    

    case ArrayExpression:
      return indent + ASTClassName(node) + ': ' + node.type + '\n' + 
	         node.expressions.collect{ e -> print(e, level + 1) }.join('\n')
    case ArgumentListExpression:
    case TupleExpression:
    case ClosureListExpression:
    case ListExpression:
      return indent + ASTClassName(node) + '\n' + 
	         node.expressions.collect{ e -> print(e, level + 1) }.join('\n')      
    case ConstructorCallExpression:
      return indent + ASTClassName(node) + ': ' + node.type + '\n' + 
	         print(node.arguments, level + 1)
    case StaticMethodCallExpression:
    case MethodCallExpression:
      return indent + ASTClassName(node) + '\n' +
             print(node.objectExpression, level + 1) + '\n' +
		     print(node.method, level + 1) + '\n' + 
             print(node.arguments, level + 1)
    case MethodPointerExpression:
    case MethodCallExpression:
      return indent + ASTClassName(node) + '\n' +
		     print(node.methodName, level + 1) + '\n' +
             print(node.expression, level + 1)
    case ClassExpression:
      return indent + ASTClassName(node) + ': ' +  node.type
    case CastExpression:
      return indent + ASTClassName(node) + ': ' +  node.type + '\n' + 
	         print(node.expression, level + 1) 
    case SpreadExpression:
    case SpreadMapExpression:
    case BitwiseNegationExpression:
    case NotExpression:
    case UnaryMinusExpression:
    case UnaryPlusExpression:
    case BooleanExpression:
      return indent + ASTClassName(node) + '\n' + print(node.expression, level + 1) 
    case GStringExpression:
      return indent + ASTClassName(node) + '\n' + 
	         node.strings.collect{ e -> print(e, level + 1) }.join('\n') + '\n' + 
	         node.values.collect{ e -> print(e, level + 1) }.join('\n')    
    case NamedArgumentListExpression:
    case MapExpression:
      return indent + ASTClassName(node) + '\n' + 
       	     node.mapEntryExpressions.collect{ e -> print(e, level + 1) }.join('\n')
    case MapEntryExpression:
      return indent + ASTClassName(node) + '\n' + 
	         print(node.keyExpression, level + 1) + '\n' + 
	         print(node.valueExpression, level + 1)  
    case EmptyExpression:
    case FieldExpression:
    case AnnotationConstantExpression:
    case ConstantExpression:
    case Expression: 
      return indent + ASTClassName(node) + (node.hasProperty('text') ? ': ' + node.text : '')

      // statements 
    case BlockStatement: 
      return indent + ASTClassName(node) + '\n' + node.statements.collect{ s -> print(s, level + 1) }.join('\n')
    case IfStatement:
      return indent + ASTClassName(node) + '\n' + 
	         print(node.booleanExpression, level + 1) + '\n' +
		     (node.ifBlock ? print(node.ifBlock, level + 1) + '\n' : '') + 
		     (node.elseBlock ? print(node.elseBlock, level + 1)  : '') 
    case WhileStatement:
    case DoWhileStatement:
      return indent + ASTClassName(node) + '\n' + 
	         print(node.booleanExpression, level + 1) + '\n' +
		     (node.loopBlock ? print(node.loopBlock, level + 1) : '')
    case ForStatement:
      return indent + ASTClassName(node) + ': ' + node.parameter + '\n' + 
	         print(node.collectionExpression, level + 1) + '\n' +
		     (node.loopBlock ? print(node.loopBlock, level + 1) : '')
    case AssertStatement: 
      return indent + ASTClassName(node) + '\n' + 
	         print(node.booleanExpression, level + 1) + '\n' +
             print(node.messageExpression, level + 1) 
    case BreakStatement:
    case ContinueStatement:
      return indent + ASTClassName(node) + ': ' + node.label  
    case ReturnStatement:
      return indent + ASTClassName(node) + 
	         (node.expression ? '\n' + print(node.expression, level + 1) : '')
    case SwitchStatement:
      return indent + ASTClassName(node) + '\n' +
		     print(node.expression, level + 1) + '\n' + 
			 //"=== caseStatements: " + node.caseStatements.collect { s -> ASTClassName(s) } + "\n" + 
		     node.caseStatements.collect{ s -> print(s, level + 1) }.join('\n') + 
		     (node.defaultStatement ? '\n' + print(node.defaultStatement, level + 1) : '')
    case CaseStatement:
    case SynchronizedStatement:
      return indent + ASTClassName(node) + '\n' +
		     print(node.expression, level + 1) + '\n' + 
		     print(node.code, level + 1) 
    case TryCatchStatement:
      return indent + ASTClassName(node) + '\n' +
		     print(node.tryStatement, level + 1) + '\n' + 
		     node.catchStatements.collect{ s -> print(s, level + 1) }.join('\n') + 
		     (node.finallyStatement ? '\n' + print(node.finallyStatement, level + 1) : '')
    case CatchStatement:
      return indent + ASTClassName(node) + ': ' + node.expressionType + '\n' + 
	         print(node.code, level + 1)
    case ThrowStatement:
    case ExpressionStatement: 
      return indent + ASTClassName(node) + '\n' + print(node.expression, level + 1) 
    case EmptyStatement:
    case Statement:
      return indent + ASTClassName(node) 

    default: 
      return indent + ASTClassName(node) + ': ' + node.toString()
    }
  }

  static ASTClassName(ASTNode node) { 
    String cname = null
    if (node) { 
      cname = node.class.name
      int i = cname.lastIndexOf('.')
      if (i >= 0) { 
		cname = cname.substring(i + 1)
      }
    }
    return cname 
  }

}
