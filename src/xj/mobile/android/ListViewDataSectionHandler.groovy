package xj.mobile.android

import xj.mobile.*
import xj.mobile.model.ui.*
import xj.mobile.lang.*
import xj.mobile.common.ListSectionDataMode
import xj.mobile.common.ListViewData
import xj.mobile.common.ListViewCategory
import xj.mobile.common.ViewProcessor
import xj.mobile.common.ViewHierarchyProcessor

import static xj.mobile.android.AndroidAppGenerator.*
import static xj.mobile.android.ListViewProcessor.*
import static xj.mobile.util.CommonUtil.*

import static xj.translate.Logger.info 

import xj.translate.common.ModuleProcessor

class ListViewDataSectionHandler 
extends xj.mobile.common.ListViewDataSectionHandler { 

  @Delegate
  ListViewProcessor lvp

  @Delegate
  DefaultViewProcessor dvp

  @Delegate
  ViewProcessor vp

  @Delegate
  ListViewData listData

  int pos // the scetion number 

  ListViewDataSectionHandler(ListViewProcessor lvp, pos) { 
	this.lvp = lvp
	vp = dvp = lvp
	this.listData = lvp?.listData
	this.pos = pos 

	sectionTitlesVar = "${view.id}SectionTitles"
    dataVarName = "${view.id}SectionData"

    viewid = view.id.toLowerCase()
    listItemLayout = "list_item_${viewid}"
    listHeaderLayout = "list_header_${viewid}"
  } 

  String getItemType() { 
	dataMode == ListSectionDataMode.Object ? 'ListItem' :  'String'
  }

  void handleListItemClass() { 
    //println "ViewHierarchyProcessor.viewProcessorMap: ${xj.mobile.common.ViewHierarchyProcessor.viewProcessorMap}"
    
    def ItemTypes = lvp.findAllItemTypes(false)
    def ItemTypesEachSection = lvp.findAllItemTypesEachSection(false)
    info "ItemTypes: ${ItemTypes}"
    info "ItemTypesEachSection: ${ItemTypesEachSection}"

    if (dataMode == ListSectionDataMode.Object) { 
	  def listItemMembers = getListItemMembers() 
	
	  def methods = getListItemMethods()
	  makeListItemClass(itemType, listItemMembers, methods)
    }
  }

  void makeListItemClass(itemType, listItemMembers, methods = '') { 
    def members = listItemMembers.collect{ "${it[1]} ${it[0]};" }.join('\n')
    def ctorArgs = listItemMembers.collect{ "${it[1]} ${it[0]}" }.join(', ')
    def ctorBody = listItemMembers.collect{ "this.${it[0]} = ${it[0]};" }.join('\n')
	def interfaces = []
	if (useItemInterface) { 
	  interfaces << listItemInterfaceName
	}
	if (handleReadWrite) { 
	  interfaces << 'Serializable'
	  vp.classModel.imports << 'java.io.Serializable'
	}
	def impl = ''
	if (interfaces) impl = ' implements ' + interfaces.join(', ')
    vp.classModel.declarationScrap += """
static class ${itemType}${impl} {
${indent(members, 1, '    ')}

    public ${itemType}(${ctorArgs}) { 
${indent(ctorBody, 2, '    ')}
    }

${indent(methods, 1, '    ')}
}
"""
  }

  def getListItemMembers() { 
	def mlist = []
	mlist << [ 'text', listItemType['text'] ]
	if (hasCheckBox) { 
	  mlist << [ 'checked', listItemType['checked'] ]
	}

	if (hasDetailText) { 
	  mlist << [ 'detailText', listItemType['detailText'] ]
	} 
	if (hasImage) { 
	  mlist << [ 'image', listItemType['image'] ]
	}
	if (hasAccessory) { 
	  mlist << [ 'accessory', listItemType['accessory'] ]
	}
	if (hasNextView) { 
	  mlist << [ 'next', listItemType['next'] ]
	}
	if (hasData) { 
	  mlist << [ 'data', listItemType['data'] ]
	}
	return mlist
  }

  def getListItemMethods() { 
	def methods = []
	if (useItemInterface) { 
	  methods << itemMethod('text')
	  if (hasDetailText) {
		methods << itemMethod('detailText')
	  }
	  if (hasCheckBox) { 
		methods << itemMethod('checked')	  
	  }
	}

	methods << '''public String toString() { 
    return text; 
}'''

	return methods.join('\n')
  }

  def itemMethod(name) { 
	def type = lvp.listItemType[name]
	def getter = (type == 'boolean' ? 'is' : 'get') + name[0].toUpperCase() + name[1 .. -1]
	def setter = 'set' + name[0].toUpperCase() + name[1 .. -1]
	def code = """public ${type} ${getter}() {
    return ${name};
}
"""
	if (name == 'checked') { 
	  code += """
public void ${setter}(${type} ${name}) {
    this.${name} = ${name};
}
"""
	}
	return code
  }

  def getItemAttributeScrap(attr) { 
	attr
  }

  def setItemAttributeScrap(attr, value) { 
	"${attr} = ${value}"
  }

  ////

  def getInitDataScrap(sp = '') { 
	sectionItems[pos].collect { 
		(dataMode == ListSectionDataMode.Object) ? initListItem(it) : "\"${it.text}\"" 
	}.join(',\n' + sp)
  }

  String initListItem(item) { 
    def args = [ "\"${item.text}\"" ]
    if (hasCheckBox) { 
      args << (item.checked ?: 'false')
    }
    if (hasDetailText) { 
      args << (item.detailText ? "\"${item.detailText}\"" : 'null')	
    }
    if (hasImage) { 
      if (item.image) { 
		String imgFile = item.image
		classModel.addImageFile(imgFile)
		int i = imgFile.lastIndexOf('.')
		if (i > 0) { 
		  imgFile = imgFile[0 .. i-1]
		}
		args << "R.drawable.${imgFile}"
      } else { 
		args << '0'
      }
    }
    if (hasAccessory) 
      args << (item.accessory?.toAndroidJavaString() ?: '0')
    if (hasNextView) { 
	  String nextView = null
	  
	  if (item.next instanceof Map) nextView = item.next.to?.toString()
	  else nextView = item.next?.toString()
      def act = nextView ? vhp.findViewProcessor(nextView)?.viewName : null
      args << (act ? "${act}.class" : 'null')
    }

	if (hasData) {
	  def data = '\"\"'
	  if (item.next instanceof Map && 
		  item.next.data != null) { 
		data = "\"${item.next.data}\""
	  }
	  args << data
	}
    "new ${itemType}(${args.join(', ')})" 
  }

  def getDataScrap() { 
	''
  }

}