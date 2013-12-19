
package xj.mobile.lang.ast

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.*

import xj.translate.common.Operators

class Util {

  static getClosureParameters(ClosureExpression c) { 
    if (c?.parameters) { 
      return c.parameters.collect { p -> p.name }
    }
    return null
  }

  static Operators operators = new Operators();

  /// use set

  static Set getUseVarSet(Expression exp) { 
    Set useSet = [] as Set 
    if (exp) { 
      switch (exp.class) { 
      case VariableExpression:
		if (exp.name) 
		  useSet << exp.name 
		break 

      case MethodCallExpression:
		def obj = exp.objectExpression
		if (obj) useSet += getUseVarSet(obj)
      case StaticMethodCallExpression:
      case ConstructorCallExpression:
		def args = exp.arguments
		if (args) useSet = args.collect{ e -> getUseVarSet(e) }.sum()
		break

      case ArgumentListExpression:
      case TupleExpression:
		if (exp.expressions && !exp.expressions.isEmpty()) 
		  useSet = exp.expressions.collect{ e -> getUseVarSet(e) }.sum()
		break
	
      case BinaryExpression:
		useSet = getUseVarSet(exp.rightExpression)
		if (!operators.isAssignmentOperator(exp.operation.text)) { 
		  useSet.addAll(getUseVarSet(exp.leftExpression))
		}
		break

      case DeclarationExpression:
		useSet = getUseVarSet(exp.rightExpression)
		break

      case BooleanExpression:
      case PostfixExpression:
      case PrefixExpression:
      case UnaryMinusExpression:
      case UnaryPlusExpression:
      case BitwiseNegationExpression:
      case NotExpression:
      case CastExpression:
		useSet = getUseVarSet(exp.expression)
		break

      case ElvisOperatorExpression:
      case TernaryExpression:
		useSet = [ exp.booleanExpression, exp.trueExpression, exp.falseExpression ].collect{ e -> getUseVarSet(e) }.sum()
		break

      case AttributeExpression:
      case PropertyExpression:
		useSet = getUseVarSet(exp.objectExpression)
		break

      case ListExpression:
		useSet = exp.expressions.collect{ e -> getUseVarSet(e)}.sum()
		break

      case MapEntryExpression:
		useSet = [ exp.keyExpression, exp.valueExpression ].collect{ e -> getUseVarSet(e) }.sum()
		break

      case NamedArgumentListExpression:
      case MapExpression:
		useSet = exp.mapEntryExpressions.collect{ e -> getUseVarSet(e) }.sum()
		break

      case ArrayExpression:
		useSet = exp.expressions.collect{ e -> getUseVarSet(e) }.sum()
		useSet.addAll(getUseVarSet(exp.sizeExpression))
		break

      case RangeExpression:
		useSet = [ exp.from, exp.to ].collect{ e -> getUseVarSet(e) }.sum()
		break

      case GStringExpression:
		useSet = exp.values.collect{ e -> getUseVarSet(e) }.sum()
		break

      case ClosureExpression:
		useSet = getUseVarSet(exp.code)
		break

      case AnnotationConstantExpression:
	
      case ClosureListExpression:
      case MethodPointerExpression:
      case SpreadExpression:
      case SpreadMapExpression:


      case FieldExpression:
      case ClassExpression:
      
      case EmptyExpression:
      case ConstantExpression:
      default:
		break
      }
    }
    //println "getUseVarSet() ${exp.class.name} ${useSet}"
    return useSet
  }

  static Set getUseVarSet(Statement stmt) { 
    Set useSet = [] as Set 
    if (stmt) { 
      switch (stmt.class) { 
      case BlockStatement:
		if (!stmt.isEmpty()) { 
		  useSet = stmt.statements.collect { s -> getUseVarSet(s) }.sum()
		}
		break

      case AssertStatement: 
		useSet = [ stmt.booleanExpression, stmt.messageExpression ].collect { s -> getUseVarSet(s) }.sum()
		break

      case ExpressionStatement:
      case ReturnStatement:
      case ThrowStatement:
		useSet = getUseVarSet(stmt.expression)
		break

      case IfStatement:
		useSet = [ stmt.booleanExpression, stmt.ifBlock, stmt.elseBlock ].collect { s -> getUseVarSet(s) }.sum()
		break
      
      case WhileStatement:
      case DoWhileStatement:
		useSet = [ stmt.booleanExpression, stmt.loopBlock ].collect { s -> getUseVarSet(s) }.sum()
		break

      case ForStatement:
		useSet = [ stmt.collectionExpression, stmt.loopBlock ].collect { s -> getUseVarSet(s) }.sum()
		break

      case SwitchStatement:
		useSet = (stmt.caseStatements + stmt.expression + stmt.defaultStatement).collect { s -> getUseVarSet(s) }.sum()
		break

      case CaseStatement:
		useSet = [ stmt.expression, stmt.code ].collect { s -> getUseVarSet(s) }.sum()
		break

      case TryCatchStatement:
		useSet = (stmt.catchStatements + stmt.tryStatement + stmt.finallyStatement).collect { s -> getUseVarSet(s) }.sum()
		break

      case CatchStatement:
		useSet = getUseVarSet(stmt.code)
		break

      case SynchronizedStatement:
		useSet = [ stmt.expression, stmt.code ].collect { s -> getUseVarSet(s) }.sum()
		break

      case BreakStatement:
      case ContinueStatement:
      case EmptyStatement:
      default:
		break
      }
    }
    //println "getUseVarSet() ${stmt.class.name} ${useSet}"
    return useSet
  }

  /// def set  

  static Set getDefVarSet(Expression exp) { 
    Set defSet = [] as Set 
    if (exp) { 
      switch (exp.class) { 
      case VariableExpression:
		//if (exp.name) defSet << exp.name 
		break 

      case MethodCallExpression:
		def obj = exp.objectExpression
		if (obj) defSet += getDefVarSet(obj)
      case StaticMethodCallExpression:
      case ConstructorCallExpression:
		def args = exp.arguments
		if (args) defSet = args.collect{ e -> getDefVarSet(e) }.sum()
		break

      case ArgumentListExpression:
      case TupleExpression:
		if (exp.expressions && !exp.expressions.isEmpty()) 
		  defSet = exp.expressions.collect{ e -> getDefVarSet(e) }.sum()
		break
	
      case BinaryExpression:
		def ldef = getDefVarSet(exp.rightExpression)
		if (ldef) defSet = ldef 
		defSet.addAll(getDefVarSet(exp.leftExpression))
		if (operators.isAssignmentOperator(exp.operation.text)) { 
		  defSet.addAll(getLeftVars(exp.leftExpression))
		}
		break

      case DeclarationExpression:
		defSet = getDefVarSet(exp.rightExpression)
		break

      case PostfixExpression:
      case PrefixExpression:
		defSet = getDefVarSet(exp.expression)
		if (exp.operation.text in ['++', '--']) { 
		  defSet.addAll(getLeftVars(exp.expression))
		}
		break

      case BooleanExpression:
      case UnaryMinusExpression:
      case UnaryPlusExpression:
      case BitwiseNegationExpression:
      case NotExpression:
      case CastExpression:
		defSet = getDefVarSet(exp.expression)
		break

      case ElvisOperatorExpression:
      case TernaryExpression:
		defSet = [ exp.booleanExpression, exp.trueExpression, exp.falseExpression ].collect{ e -> getDefVarSet(e) }.sum()
		break

      case AttributeExpression:
      case PropertyExpression:
		defSet = getDefVarSet(exp.objectExpression)
		defSet.addAll(getLeftVars(exp.objectExpression))
		break

      case ListExpression:
		defSet = exp.expressions.collect{ e -> getDefVarSet(e)}.sum()
		break

      case MapEntryExpression:
		defSet = [ exp.keyExpression, exp.valueExpression ].collect{ e -> getDefVarSet(e) }.sum()
		break

      case NamedArgumentListExpression:
      case MapExpression:
		defSet = exp.mapEntryExpressions.collect{ e -> getDefVarSet(e) }.sum()
		break

      case ArrayExpression:
		defSet = exp.expressions.collect{ e -> getDefVarSet(e) }.sum()
		defSet.addAll(getDefVarSet(exp.sizeExpression))
		break

      case RangeExpression:
		defSet = [ exp.from, exp.to ].collect{ e -> getDefVarSet(e) }.sum()
		break

      case GStringExpression:
		defSet = exp.values.collect{ e -> getDefVarSet(e) }.sum()
		break

      case ClosureExpression:
		defSet = getDefVarSet(exp.code)
		break

      case ClosureListExpression:
      case MethodPointerExpression:
      case SpreadExpression:
      case SpreadMapExpression:


      case FieldExpression:
      case ClassExpression:
      
      case EmptyExpression:
      case ConstantExpression:
      case AnnotationConstantExpression:

      default:
		break
      }
    }
    //println "getDefVarSet() ${exp.class.name} ${defSet}"
    return defSet
  }

  static Set getDefVarSet(Statement stmt) { 
    Set defSet = [] as Set 
    if (stmt) { 
      switch (stmt.class) { 
      case BlockStatement:
		if (!stmt.isEmpty()) { 
		  defSet = stmt.statements.collect { s -> getDefVarSet(s) }.sum()
		}
		break

      case AssertStatement: 
		defSet = [ stmt.booleanExpression, stmt.messageExpression ].collect { s -> getDefVarSet(s) }.sum()
		break

      case ExpressionStatement:
      case ReturnStatement:
      case ThrowStatement:
		defSet = getDefVarSet(stmt.expression)
		break

      case IfStatement:
		defSet = [ stmt.booleanExpression, stmt.ifBlock, stmt.elseBlock ].collect { s -> getDefVarSet(s) }.sum()
		break
      
      case WhileStatement:
      case DoWhileStatement:
		defSet = [ stmt.booleanExpression, stmt.loopBlock ].collect { s -> getDefVarSet(s) }.sum()
		break

      case ForStatement:
		defSet = [ stmt.collectionExpression, stmt.loopBlock ].collect { s -> getDefVarSet(s) }.sum()
		break

      case SwitchStatement:
		defSet = (stmt.caseStatements + stmt.expression + stmt.defaultStatement).collect { s -> getDefVarSet(s) }.sum()
		break

      case CaseStatement:
		defSet = [ stmt.expression, stmt.code ].collect { s -> getDefVarSet(s) }.sum()
		break

      case TryCatchStatement:
		defSet = (stmt.catchStatements + stmt.tryStatement + stmt.finallyStatement).collect { s -> getDefVarSet(s) }.sum()
		break

      case CatchStatement:
		defSet = getDefVarSet(stmt.code)
		break

      case SynchronizedStatement:
		defSet = [ stmt.expression, stmt.code ].collect { s -> getDefVarSet(s) }.sum()
		break

      case BreakStatement:
      case ContinueStatement:
      case EmptyStatement:
      default:
		break
      }
    }
    //println "getDefVarSet() ${stmt.class.name} ${defSet}"
    return defSet
  }

  static Set getLeftVars(Expression exp) { 
    def vars = [] as Set
    if (exp) { 
      switch (exp.class) { 
      case VariableExpression:
		if (exp.name) vars << exp.name 
		break 

      case MethodCallExpression:
		def obj = exp.objectExpression
		if (obj) { 
		  vars = getDefVarSet(obj)
		  vars.addAll(getLeftVars(obj))
		}
		break;

      case AttributeExpression:
      case PropertyExpression:
		def obj = exp.objectExpression
		if (obj) { 
		  vars = getDefVarSet(obj)
		  vars.addAll(getLeftVars(obj))
		}
		//vars = getLeftVars(exp.objectExpression)
		break

      default:
		break
      }
    }
    
    return vars
  }



}