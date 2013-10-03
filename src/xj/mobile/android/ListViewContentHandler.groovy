package xj.mobile.android

import xj.mobile.*
import xj.mobile.model.ui.*
import xj.mobile.lang.*
import xj.mobile.common.ListSectionDataMode
import xj.mobile.common.ListViewData
import xj.mobile.common.ListViewCategory
import xj.mobile.common.ViewProcessor
import xj.mobile.common.ViewHierarchyProcessor
import xj.mobile.builder.ListEntity

import static xj.mobile.android.AndroidAppGenerator.*
import static xj.mobile.util.CommonUtil.*

import static xj.translate.Logger.info 

import xj.translate.common.ModuleProcessor

class ListViewContentHandler extends xj.mobile.common.ListViewContentHandler { 

  @Delegate
  ListViewProcessor lvp

  @Delegate
  DefaultViewProcessor dvp

  @Delegate
  ViewProcessor vp

  @Delegate
  ListViewData listData

  ListViewContentHandler(ListViewProcessor lvp) { 
	this.lvp = lvp
	vp = dvp = lvp
	this.listData = lvp?.listData

	dataSectionHandler = new ListViewDataSectionHandler(lvp, 0) 
	if (numSections > 0) { 
	  sectionHandlers = (0 .. numSections - 1).collect { 
		sectionEntities[it] ? new ListViewEntitySectionHandler(lvp, it) : 
		                      new ListViewDataSectionHandler(lvp, it) 
	  }
	}

	if (numSections == 0) { 
	  itemType = dataSectionHandler.itemType
	} else if (numSections == 1 || !hasEntity) { 
	  itemType = sectionHandlers[0].itemType
	} else { 
	  if ((sectionHandlers.itemType as Set).size() == 1) { 
		itemType = sectionHandlers[0].itemType	
	  } else { 
		itemType = listItemInterfaceName
		useItemInterface = true
	  }
	}
	vp.viewHolderMembers = getViewHolderMembers()
  } 

  void handleListItemClass() { 
	if (numSections == 1) {  
	  sectionHandlers[0].handleListItemClass()
	} else if (numSections > 1) { 
	  if (useItemInterface) { 
		makeListItemInterface()
	  }

	  def classes = [] as Set
	  sectionHandlers.each { s ->
		if (!(s.itemType in classes)) { 
		  classes << s.itemType
		  s.handleListItemClass()
		}
	  }
	}
	if (useViewHolder) { 
	  makeViewHolderClass() 
	}
  }


  void makeViewHolderClass() { 
    def vhmembers = vp.viewHolderMembers.collect{ "${getViewHolderType(it)} ${it};"}.join('\n')
    vp.classModel.declarationScrap += """
static class ViewHolder {
${indent(vhmembers, 1, '    ')}
}
"""
  }

  void makeListItemInterface() { 
	def methods = getListItemMethodHeaders()
	vp.classModel.declarationScrap += """
static interface ${listItemInterfaceName} {
${indent(methods, 1, '    ')}
}
"""
  }  

  def getListItemMethodHeaders() { 
	def methods = []
	methods << itemMethodHeader('text')
	if (hasDetailText) {
	  methods << itemMethodHeader('detailText')
	}
	if (hasCheckBox) { 
	  methods << itemMethodHeader('checked')	  
	}
	return methods.join('\n')
  }


  def itemMethodHeader(name) { 
	def type = lvp.listItemType[name]
	def getter = (type == 'boolean' ? 'is' : 'get') + name[0].toUpperCase() + name[1 .. -1]
	def setter = 'set' + name[0].toUpperCase() + name[1 .. -1]
	def code = "public ${type} ${getter}();"
	if (name == 'checked') { 
	  code += "\npublic void ${setter}(${type} ${name});"
	}
	return code
  }

  def getViewHolderMembers() {
 	def mlist = []
	if (useViewHolder) { 
	  mlist << 'text'
	}
	if (hasDetailText) { 
	  mlist << 'detailText'
	} 
	if (hasImage) { 
	  mlist << 'image'
	}
	if (hasAccessory) { 
	  mlist << 'accessory'
	}
	return mlist
  }

  /////

  def getItemAttributeScrap(attr) {
	if (hasEntity) { 
	  sectionHandlers[0].getItemAttributeScrap(attr)
	} else { 
	  dataSectionHandler.getItemAttributeScrap(attr)
	}
  }

  def setItemAttributeScrap(attr, value) {
	if (hasEntity) { 
	  sectionHandlers[0].setItemAttributeScrap(attr, value)
	} else { 
	  dataSectionHandler.setItemAttributeScrap(attr, value)
	}
  }

  def getInitDataScrap() { 
	if (numSections == 0) {
	  ''
	} else if (numSections == 1) {
	  sectionHandlers[0].getInitDataScrap()
	} else { 
	  if (useList) { 
		sectionHandlers.collect { sec -> 
		  String prefix = "new ArrayList(Arrays.asList(new ${sec.getItemType()}[] {\n"
		  String suffix = ' } ))'

		  (prefix + indent(sec.getInitDataScrap(), 1, '    ')  + suffix)
		}.join(',\n') 
	  } else { 
		sectionHandlers.collect { sec -> 
		  ('{ ' + sec.getInitDataScrap('  ')  + ' }')
		}.join(',\n') 
	  }


	}
  }

  // 

  def getAdapterScrap() { 
	String adapter = (viewClass == 'ExpandableListView' ? 'ExpandableListAdapter' : 'Adapter')
	String list = (view.embedded ? view.id : "get${viewClass}()")
	"${listAdapterClass} adapter = (${listAdapterClass}) ${list}.get${adapter}();"
  }

  def getDataScrap() { 
	if (hasEntity) { 
	  sectionHandlers[0].getDataScrap() 
	} else { 
	  dataSectionHandler.getDataScrap() 
	}
  }

  ///////
  //
  //  read/write data 
  //
  ///////

  def fileType = 'Serialization'

  def generateReadWrite() { 
	if (handleReadWrite) { 

	  classModel.imports << 'java.io.FileInputStream' << 'java.io.FileOutputStream' <<
		'java.io.ObjectInputStream' << 'java.io.ObjectOutputStream' <<
		'java.io.IOException' << 'java.io.Serializable'

	  def binding = [ DataVar : dataVarName,
					  DataType : getDataModelType() ]

	  def writeBodyTemp = writeDataCode[fileType] 
	  def template = engine.createTemplate(writeBodyTemp).make(binding)
	  def writeBody = template.toString()

	  def readBodyTemp = readDataCode[fileType] 
	  template = engine.createTemplate(readBodyTemp).make(binding)
	  def readBody = template.toString()

	  classModel.methodScrap += """
private String dataFileName = \"ListData.ser\";
"""

	  classModel.methodScrap += """
public void writeData() {
	try {
${indent(writeBody, 2, '    ')}
	} catch (IOException e) {
	}
}
"""

	  classModel.methodScrap += """
public ${binding.DataType} readData() {
	try {
${indent(readBody, 2, '    ')}
	} catch (ClassNotFoundException e) {
	} catch (IOException e) {
	}
	return null;
}
"""

	}
  }

  static writeDataCode = [
	Serialization: 
	'''FileOutputStream fos = openFileOutput(dataFileName, Context.MODE_PRIVATE);
ObjectOutputStream os = new ObjectOutputStream(fos);
os.writeObject(${DataVar});
os.close();''',

	JSON: 
	'''
'''
  ]

  static readDataCode = [
	Serialization: 
	'''FileInputStream fis = openFileInput(dataFileName);
ObjectInputStream is = new ObjectInputStream(fis);
${DataType} data = (${DataType}) is.readObject();
is.close();
return data;''',

	JSON: 
	'''
'''
  ]

}