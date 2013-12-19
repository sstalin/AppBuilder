package xj.mobile.android

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

import static xj.mobile.android.AndroidAppGenerator.*
import static xj.mobile.util.CommonUtil.*

import static xj.mobile.lang.ast.PrettyPrinter.*
import static xj.mobile.lang.ast.Util.*
import static xj.translate.Logger.info 

import xj.translate.common.ModuleProcessor

class ListViewEntitySectionHandler 
extends ListViewDataSectionHandler 
implements EntityUnparser { 

  @Delegate
  xj.mobile.common.ListViewEntitySectionHandler entityHandler = new xj.mobile.common.ListViewEntitySectionHandler()

  ListViewEntitySectionHandler(ListViewProcessor lvp, int pos) {  
    super(lvp, pos)
	initEntity(listData, pos)

    info "[ListViewEntityHandler.Android] entity: ${entityVar} ${entityClass} ${entityValues}"
  }

  String getItemType() { 
	entityClassName
  }

  def getListItemMembers() { 
	def mlist = []   

	entityDef.fields.each { f -> 
	  String type = generator.typeName(f.type, false)
	  mlist << [ f.name, type ]
	}
	if (hasNextView) { 
	  mlist << [ 'next', lvp.listItemType['next'] ]
	}
	
	return mlist
  }

  def getListItemMethods() { 
	def methods = []
	methods << """public ${getItemType()}() {}
"""

	methods << itemMethod('text')
	if (hasDetailText) {
	  methods << itemMethod('detailText')
	}
	if (hasCheckBox) { 
	  methods << itemMethod('checked')	  
	}

	methods << '''public String toString() {
    return getText();
}'''
	return methods.join('\n')
  }

  def itemMethod(name) { 
	def type = lvp.listItemType[name]
	def getter = (type == 'boolean' ? 'is' : 'get') + name[0].toUpperCase() + name[1 .. -1]
	def setter = 'set' + name[0].toUpperCase() + name[1 .. -1]
	def getterCode = getEntityPropertyCode(name)
	def code = """public ${type} ${getter}() {
    return ${getterCode};
}
"""
	if (name == 'checked') { 
	  code += """
public void ${setter}(${type} ${name}) {
    this.${getterCode} = ${name};
}
"""
	}
	return code
  }

  def getItemAttributeScrap(attr) { 
	def getter = 'get' + attr[0].toUpperCase() + attr[1 .. -1]
	"${getter}()"
  }

  def setItemAttributeScrap(attr, value) { 
	def setter = 'set' + attr[0].toUpperCase() + attr[1 .. -1]
	"${setter}(${value})"
  }

  def toEntityValueMap(v) { 
    if (v) { 
	  def args = []
	  entityDef.fields.each { f -> 
		def value = v.properties[f.name]
		info "[ListViewEntityHandler.Android] getInitDataScrap() ${f.name} ${f.type} ${f.type.class} ${value} ${value.class}"	

		if (value instanceof String) { 
		  args << "\"${value}\""
		} else if (value instanceof Float) { 
		  def vstr = value.toString() + 'f'
		  //if (f.type == ClassHelper.float_TYPE) vstr += 'f'
		  args << vstr
		} else { 
		  args << value.toString()
		}
	  }
	  args
	} else { 
	  []
	}
  }

  def getInitDataScrap(sp = '') { 
    if (!entityValues) { 
	  ''
	} else { 
	  entityValues.collect { v -> 
		info "[ListViewEntityHandler.Android] getInitDataScrap() ${v} ${v.properties}"	
		def args = toEntityValueMap(v)
		"new ${itemType}(${args.join(', ')})" 
	  }.join(',\n' + sp)
	}
  }

  def getDataScrap() { 
	//if (useList) { 
	if (numSections > 1) { 
"""ArrayAdapter<${itemType}> section = adapter.getAdapter(${pos});
${itemType} data = section.getItem(position - adapter.firstInSection(${pos}));"""
	} else { 
	  "${itemType} data = (${itemType}) adapter.getItem(position);"
	}
  }

  def getCellTextScrap() {   
    getEntityPropertyCode('text', 'data')
  }

  def getCellDetailTextScrap() {   
    getEntityPropertyCode('detailText', 'data')
  }

  def getCellState() { 
    getEntityPropertyCode('checked', 'data')
  }

  private String getEntityPropertyCode(pname, obj = null) {
    def psrc = "${pname}.src"
    if (dummy[psrc]) { 
      def srcInfo = dummy[psrc] 
      def dummyVar = entityDef._dummy_
      generateExpressionCode(srcInfo, obj)
      //if (dummyVar in src.useSet) { }
    } else if (dummy[pname]) { 
      "\"${dummy[pname]}\""
    } else { 
      'null'
    }
  }



  ///////
  //
  //  generating code 
  //
  ///////

  def String dummyObjectName = null

  def generateExpressionCode(srcInfo, obj = null) { 
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
	  dummyObjectName = obj
	  expCode = unparser.unparse(code)
	  unparser.entityUnparser = null 
	  dummyObjectName = null

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
      info "[ListViewEntitySectionHandler.android] unparsePropertyExpression()"
	  if (exp.objectExpression instanceof VariableExpression &&
		  exp.objectExpression.name == entityDef._dummy_) { 
		def pname = exp.propertyAsString
		if (!dummyObjectName) { 
		  return "${pname}" 
		} else { 
		  return "${dummyObjectName}.${pname}"
		}
		// "[data objectForKey:k${pname.capitalize()}Key]"
	  }
	}
	null
  }

  // method on dummy
  String unparseMethodCallExpression(exp) { 
    if (exp instanceof MethodCallExpression) { 
      info "[ListViewEntitySectionHandler.android] unparseMethodCallExpression()"
      def mname = exp.methodAsString
      if (exp.objectExpression instanceof VariableExpression) {
		if (exp.objectExpression.name == entityDef._dummy_) { 
		  if (mname == 'delete') { 
			return  "${numSections > 1 ? 'section' : 'adapter'}.remove(data);" 
		  }
		}
      }
    }
    null
  }

  // method on variable declared as a collection of entity objects 
  String unparseEntityMethodCallExpression(exp) { 
    if (exp instanceof EntityMethodCallExpression) { 
      info "[ListViewEntitySectionHandler] unparseEntityMethodCallExpression()"
      if (exp.method == 'add') { 
		//@"Apple", kTextKey,
		//[NSNumber numberWithInteger:FALSE], kStateKey,
		//@"Quantity: 3", kDetailKey,
		def vlist = null
		def proto = entityDef?.prototype	
		if (proto) { 
		  vlist = toEntityValueMap(proto)
		} else { 
		  vlist = []
		}

		return "${numSections > 1 ? 'section' : 'adapter'}.add(new ${entityDef.entityClass.name}(${vlist.join(', ')}))"
      }
    }
    null
  }

}