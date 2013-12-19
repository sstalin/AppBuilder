package xj.mobile.ios

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.control.*
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.*

import xj.mobile.*
import xj.mobile.model.ui.*
import xj.mobile.lang.*
import xj.mobile.lang.ast.*
import xj.mobile.common.ListSectionDataMode
import xj.mobile.common.ListViewData
import xj.mobile.common.ListViewCategory
import xj.mobile.codegen.EntityUnparser
import xj.mobile.builder.ListEntity

import static xj.mobile.ios.IOSAppGenerator.*
import static xj.mobile.util.CommonUtil.*
import static xj.mobile.codegen.IOSUtil.*

import static xj.mobile.lang.ast.PrettyPrinter.*
import static xj.mobile.lang.ast.Util.*
import static xj.translate.Logger.info 

import xj.translate.common.ModuleProcessor

class ListViewEntitySectionHandler 
extends ListViewDataSectionHandler 
implements EntityUnparser { 

  @groovy.lang.Delegate
  xj.mobile.common.ListViewEntitySectionHandler entityHandler = new  xj.mobile.common.ListViewEntitySectionHandler()

  ListViewEntitySectionHandler(ListViewProcessor lvp, int pos) {  
    super(lvp, pos)
	initEntity(listData, pos)

    info "[ListViewEntityHandler.iOS] entity: ${entityVar} ${entityClass} ${entityValues}"
  }

  def declarations() { 
    def decl = [] //super.declarations()

    if (sectionItems && entityClass) { 
      info "[ListViewEntityHandler.iOS] entity dummy: ${entityDef._dummy_}"	

      //entityClass.fields.each { f -> 
	  entityDef.fields.each { f -> 
		def vname = "k${f.name.capitalize()}Key"
		decl << [ vname, "static NSString *${vname} = @\"${f.name}Key\"" ]
      }
    }
    return decl 
  }

  def toEntityValueMap(v) { 
    if (v) { 
      def maplist = []
      //entityClass.fields.each{ f -> 
	  entityDef.fields.each{ f -> 
		def value = v.properties[f.name]
		info "[ListViewEntityHandler.iOS]    ${f.name} ${value} ${value.class}"
		def valueStr = value.toString()
		if (value instanceof String) { 
		  valueStr = "@\"${value}\""
		} else if (value instanceof Boolean) { 
		  def bvalue = value ? 'YES' : 'NO'
		  valueStr = "[NSNumber numberWithInteger:${bvalue}]"
		} else if (value instanceof Number) { 
		  def tname = value.class.name
		  if (tname.startsWith('java.lang.')) { 
			tname = tname.substring(10)
		  } 
		  if (tname == 'Character') tname = 'Char'
		  valueStr = "[NSNumber numberWith${tname}:${value}]"
		} 
		def keyStr = "k${f.name.capitalize()}Key"
		maplist << [ valueStr, keyStr ]
      }
      toNSDictionaryWithObjects(maplist, '\n\t\t\t', true)
    } else { 
      'nil'
    }
  }

  def getInitDataSectionScrap() { 
    if (!entityValues) { 
      "[${dataVarType} arrayWithCapacity: 32]"
    } else { 
      // initial values, single section of items  
      def itemlist = entityValues.collect { v -> 
		info "[ListViewEntityHandler.iOS] getInitDataScrap() ${v} ${v.properties}"	
		toEntityValueMap(v)
      }
      toNSArrayWithObjects(itemlist, '\n\t\t', true)
    }
  }

  def getDataScrap() { 
    if (view.choiceMode == 'Multiple') { 
      """NSMutableArray *sec = (NSMutableArray *) [${dataVarName} objectAtIndex:indexPath.section];    
NSMutableDictionary* data = (NSMutableDictionary *) [sec objectAtIndex:indexPath.row];"""
    } else {
      "NSMutableArray *data = (NSMutableArray *) [${dataVarName} objectAtIndex:indexPath.section];"
    }
  }

  def getCellTextScrap() {   
    getEntityPropertyCode('text')
  }

  def getCellDetailTextScrap() {   
    getEntityPropertyCode('detailText')
  }

  def getCellState() { 
    getEntityPropertyCode('checked')
  }

  private String getEntityPropertyCode(pname) {
    def psrc = "${pname}.src"
    if (dummy[psrc]) { 
      def srcInfo = dummy[psrc] 
      def dummyVar = entityDef._dummy_
      generateExpressionCode(srcInfo)
      //if (dummyVar in src.useSet) { }
    } else if (dummy[pname]) { 
      "@\"${dummy[pname]}\""
    } else { 
      'nil'
    }
  }

  /// handle selection 
  
  def getChoiceScrap() { 
    if (view.choiceMode) { 
      //if (view.choiceMode == 'Single') { 
      //} else if (view.choiceMode == 'Multiple') { 
      //}
	  
      def stateKey = 'kStateKey'
      if (dummy['checked.src']) { 
		def exp = dummy['checked.src'].code
		if (exp instanceof PropertyExpression) { 
		  if (exp.objectExpression instanceof VariableExpression &&
			  exp.objectExpression.name == entityDef._dummy_) { 
			stateKey = "k${exp.propertyAsString.capitalize()}Key"
		  }
		}
      }
      def actionCode = '''
[self writeData];
'''

      def binding = [ DataVar : dataVarName, 
					  StateKey : stateKey,
					  action : actionCode ]
      def template = engine.createTemplate(choiceTemplate).make(binding)
      template.toString()
    } else { 
      null
    }
  }
  

  ///////
  //
  //  generating code 
  //
  ///////

  def generateExpressionCode(srcInfo) { 
	String expCode = null
	def src = srcInfo?.code
	if (src instanceof Expression) { 
	  info '[ListViewEntityHandler] Expression code pre-transform:\n' + print(src, 2)
	  def writer = new StringWriter()
	  src.visit new groovy.inspect.swingui.AstNodeToScriptVisitor(writer)
	  info '[ListViewEntitySectionHandler] Expression unparsed pre-transform:\n' + writer
	  //info "[ListViewEntityHandler] current class: ${ModuleProcessor.currentClassProcessor.name}"

	  def code = xj.translate.ASTUtil.copyExpression(src, null)
	  def unparser = vp.generator.unparser
	  unparser.entityUnparser = this 
	  expCode = unparser.unparse(code)
	  unparser.entityUnparser = null 

	  info '[ListViewEntitySectionHandler] Expression unparsed:\n' + expCode

	  //def transformedExpression = transformAction(code, params, widget)
	  //info '[ListViewEntityHandler] Expression code post-transform, before unparse:\n' + print(code, 2)
	  //expCode = unparser.unparse(transformedAction)

	  //info '[ListViewEntityHandler] Expression params: ' + params
	  //info '[ListViewEntityHandler] Expression code post-transform:\n' + print(code, 2)
	  //info '[ListViewEntityHandler] Expression unparsed post-transform:\n' + actionCode

	}
	return expCode
  }

  ///////
  //
  //  implementing EntityUnparser
  //
  ///////

  // property on dummy
  String unparsePropertyExpression(exp) { 
	if (exp instanceof PropertyExpression) { 
      info "[ListViewEntitySectionHandler.ios] unparsePropertyExpression()"
	  if (exp.objectExpression instanceof VariableExpression &&
		  exp.objectExpression.name == entityDef._dummy_) { 
		def pname = exp.propertyAsString
		return "[data objectForKey:k${pname.capitalize()}Key]"
	  }
	}
	null
  }

  // method on dummy
  String unparseMethodCallExpression(exp) { 
    if (exp instanceof MethodCallExpression) { 
      info "[ListViewEntitySectionHandler.ios] unparseMethodCallExpression()"
      def mname = exp.methodAsString
      if (exp.objectExpression instanceof VariableExpression) {
		if (exp.objectExpression.name == entityDef._dummy_) { 
		  if (mname == 'delete') { 
			return "[sec removeObjectAtIndex:indexPath.row]"
		  }
		}
      }
    }
    null
  }

  // method on variable declared as a collection of entity objects 
  String unparseEntityMethodCallExpression(exp) { 
    if (exp instanceof EntityMethodCallExpression) { 
      info "[ListViewEntitySectionHandler.ios] unparseEntityMethodCallExpression()"
      if (exp.method == 'add') { 
		def vlist = null
		def proto = entityDef?.prototype	
		if (proto) { 
		  vlist = toEntityValueMap(proto)
		} else { 
		  vlist = '[NSMutableDictionary dictionary]'
		}
		
		return """NSMutableDictionary* entry = ${vlist};
NSMutableArray *sec = (NSMutableArray *) [${dataVarName} objectAtIndex:${pos}];
[sec addObject:entry];
[self.tableView reloadData];"""
      }
    }
    null
  }


  // getAttr: "[${target} objectForKey:${key}]"
  // setAttr: "[${target} setObject:${value} forKey:${key}]"

}

