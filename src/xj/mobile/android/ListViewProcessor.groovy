package xj.mobile.android

import groovy.xml.MarkupBuilder

import xj.mobile.*
import xj.mobile.model.ui.*
import xj.mobile.lang.*

import xj.mobile.common.ViewProcessor
import xj.mobile.common.AppGenerator
import xj.mobile.common.ViewHierarchyProcessor
import xj.mobile.common.ListSectionDataMode
import xj.mobile.common.ListViewData
import xj.mobile.common.ListViewCategory
import xj.mobile.util.PrettyMarkupBuilder

import static xj.mobile.android.AndroidAppGenerator.*
import static xj.mobile.util.CommonUtil.*
import static xj.translate.Logger.info

@Mixin(ListViewCategory)
class ListViewProcessor extends DefaultViewProcessor { 

  String viewClass = 'ListView'
  String adapterClass = 'SectionedListItemAdapter'
  
  String actionListenerImport = 'android.widget.AdapterView.OnItemClickListener'
  String actionListener = 'OnItemClickListener'
  String actionMethodSignature = 'void onItemClick(AdapterView<?> parent, View view, int position, long id)'

  public ListViewProcessor(View view, String viewName = null) { 
    super(view, viewName)
    listData = new ListViewData()

	if (view.embedded && view.parent != null) { 
	  // embedded list view 
	  classModel = view.parent.viewProcessor.classModel
	  listVar = view.id
	} else { 
	  classModel.superClassName = 'ListActivity'
	  listVar = 'list'
	}
  }

  @Delegate 
  ListViewData listData

  boolean useList = false 
  boolean useViewHolder = false
  boolean useItemInterface = false
  String listItemInterfaceName = 'IListItem'

  def viewHolderMembers = [] 

  String sectionTitlesVar
  String dataVarName
  String itemType 
  String listAdapterClass

  String listItemLayout
  String listHeaderLayout
  String viewid
  String listVar 

  ListViewContentHandler contentHandler

  def listHeaderAttributes = [
    'xmlns:android': XMLNS_ANDROID,
    'android:layout_width': 'match_parent',
    'android:layout_height': 'wrap_content',
    'android:paddingTop': '2dip',  
    'android:paddingBottom': '2dip',  
    'android:paddingLeft': '5dip', 
    'android:textSize': '20px',
    'android:textStyle': 'bold',
    'android:typeface': 'serif'
  ]

  static listItemType = [
    'text'       : 'String',
    'detailText' : 'String',
    'image'      : 'int',
    'accessory'  : 'int',
    'checked'    : 'boolean',
    'next'       : 'Class',

	'data'       : 'String',
  ]

  static viewHolderTypes = [
    'detailText' : 'TextView',
    'image'      : 'ImageView',
    'accessory'  : 'ImageView',
  ]

  static viewHolderSetter = [
    'text'       : 'setText',
    'detailText' : 'setText',
    'image'      : 'setImageResource',
    'accessory'  : 'setImageResource',
    'checked'    : 'setChecked', 
  ]

  def getViewHolderType(f) { 
    if (f == 'text') { 
      return hasCheckBox ? 'CheckedTextView' : 'TextView'
    } else { 
      return viewHolderTypes[f]
    }
  }

  void process() { 
    if (view) { 
	  currentViewProcessor = this

      processListView()
	  handleInheritedNextView()

      if (hasNextView || hasDetailText || hasImage || hasAccessory) { 
		dataMode = ListSectionDataMode.Object
      }
      useViewHolder = (hasDetailText || hasImage || hasAccessory)

	  useList = handleReadWrite && hasEntityMenuAction

      contentHandler = new ListViewContentHandler(this)
      contentHandler.handleListItemClass()
	  
      handleSectionContents()

      handleChoiceMode()
      handleSelection()

      // menus, popups, widgets other than Item are not processed 
      processMenus()
      processPopups()

      generateViewLayout()

	  handleReadWrite()
    }
  }

  ////
  
  void handleInheritedNextView() { 
	sectionItems.eachWithIndex { section, i ->  
	  def sec = sections[i]
	  section.each { row -> 
		if (!row.next) row.next = sec?.next
		if (!row.next) row.next = view.next
	  }
	}
  }

  void handleListItemAdapter() { 
    def initViewHolder = viewHolderMembers.collect{ 
      "holder.${it} = (${getViewHolderType(it)}) convertView.findViewById(R.id.${it}_${viewid});" 
    }.join('\n')
    def setViewHolder = viewHolderMembers.collect{ 
      def receiver = (it != 'checked' ? it : 'text')
      def val = contentHandler.getItemAttributeScrap(it)
      "holder.${receiver}.${viewHolderSetter[it]}(item.${val});"
    }.join('\n')

	def binding = [
	  itemType: itemType, 
	  itemsType: (useList ? "List<${itemType}>" : "${itemType}[]"), 
	  listItemLayout: listItemLayout,
	  initViewHolder: initViewHolder,
	  setViewHolder: setViewHolder,
	  length: useList ? 'size()' : 'length', 
	  getPosition: useList ? '.get(position)' : '[position]'
	]
	generator.injectCodeFromTemplateRef(classModel, "ListView:adapter", binding)

  }

  ///

  String getDataModelType() { 
    if (numSections <= 1) { 
	  (useList ? "List<${itemType}>" : "${itemType}[]")
	} else { 
	  (useList ? "List<List<${itemType}>>" : "${itemType}[][]")
	}
  }

  String getDataModelInitializer(data) { 
	String prefix = ''
	String suffix = '' 
	if (useList) { 
	  def arrayType = (numSections <= 1 ? itemType : 'List') 
	  prefix = "new ArrayList(Arrays.asList(new ${arrayType}[] "
	  suffix = '))'	  
	  classModel.imports << 'java.util.List' << 'java.util.Arrays' << 'java.util.ArrayList'
	}
	"""${prefix}{
${indent(data, 1, '    ')}
}${suffix}"""
  }

  void handleSectionContents() { 
    if (numSections <= 1) { 
      handleSingleSectionContents()
    } else { 
      handleMultipleSectionContents()
    }
  }

  void handleSingleSectionContents() { 
    if (sectionTitles[0] == null) { 
      def textData = contentHandler.getInitDataScrap()
	  String modifiers
	  String initializer
	  if (handleReadWrite) { 
		modifiers = ''
		initializer = 'null'
		String prefix = ''
		if (!useList) { 
		  prefix = "new ${dataModelType} "
		}
		def initScrap = "${dataVarName} = ${prefix}${getDataModelInitializer(textData)};"
		classModel.onCreateScrap += """
${dataVarName} = readData();
if (${dataVarName} == null) {
${indent(initScrap, 1, '    ')}
}
"""
	  } else { 
		modifiers = 'static final '
		initializer = getDataModelInitializer(textData)
	  }

      classModel.declarationScrap += """
${modifiers}${dataModelType} ${dataVarName} = ${initializer};
"""
      //def adapter = "ArrayAdapter<${itemType}>"
      listAdapterClass = "ArrayAdapter<${itemType}>"
      if (useViewHolder) { 
		handleListItemAdapter()
		listAdapterClass = "ListItemAdapter"
      }
      def adapterInit = "new ${listAdapterClass}(this, R.layout.${listItemLayout}, ${dataVarName})"

      if (view.title) { 
		classModel.onCreateScrap += "\nsetTitle(\"${view.title}\");"
      }
	  String setAdapter = 'setListAdapter'
	  String getListView = "\nfinal ${viewClass} list = get${viewClass}();"
	  if (view.embedded) { 
		getListView = ''
		setAdapter = "${listVar}.setAdapter"
	  }
      classModel.onCreateScrap += """
${setAdapter}(${adapterInit});${getListView}
${listVar}.setTextFilterEnabled(true);
"""
    }
  } 

  void handleMultipleSectionContents() { 
    def textData = contentHandler.getInitDataScrap()
	String modifiers
	String initializer
	if (handleReadWrite) { 
	  modifiers = ''
	  initializer = 'null'
	  String prefix = ''
	  if (!useList) { 
		prefix = "new ${dataModelType} "
	  }
	  def initScrap = "${dataVarName} = ${prefix}${getDataModelInitializer(textData)};"
	  classModel.onCreateScrap += """
${dataVarName} = readData();
if (${dataVarName} == null) {
${indent(initScrap, 1, '    ')}
}
"""
	} else { 
	  modifiers = 'static final '
	  initializer = getDataModelInitializer(textData)
	}

    classModel.declarationScrap += """
${modifiers}${dataModelType} ${dataVarName} = ${initializer};
"""
    def titleData = sectionTitles.collect{ 
      it ? "\"${it}\"" : 'null' 
    }.join(',\n')
    classModel.declarationScrap += """
static final String[] ${sectionTitlesVar} = {
${indent(titleData, 1, '    ')}
};
"""

    listAdapterClass = "${adapterClass}<${itemType}>"
    if (useViewHolder) { 
      handleListItemAdapter()
      listAdapterClass = handleSectionAdapter()
    }
    def adapterInit = "new ${listAdapterClass}(this, R.layout.${listHeaderLayout}, R.layout.${listItemLayout}, ${sectionTitlesVar}, ${dataVarName})"
    classModel.auxiliaryClasses << adapterClass

    if (view.title) { 
      classModel.onCreateScrap += "\nsetTitle(\"${view.title}\");"
    }
	String setAdapter = 'setListAdapter'
	String getListView = "\nfinal ${viewClass} list = get${viewClass}();"
	if (view.embedded) { 
	  getListView = ''
	  setAdapter = "${listVar}.setAdapter"
	}
    classModel.onCreateScrap += """
${setAdapter}(${adapterInit});${getListView}
${listVar}.setTextFilterEnabled(true);
"""
  }

  String handleSectionAdapter() { 
	def binding = [
	  itemType: itemType, 
	  sectionType: useList ? "List<List<${itemType}>>" : "${itemType}[][]",
	  length: useList ? 'size()' : 'length', 
	  get_i: useList ? '.get(i)' : '[i]'
	]
	generator.injectCodeFromTemplateRef(classModel, "ListView:sectionAdapter", binding)
    return 'SectionAdapter'
  }

  ///

  String selectListItemCode() { 
	String temp = hasData ? 'startActivity1' : 'startActivity2'
	def binding = [
	  thisActivity: (view.embedded ? view.parent.viewProcessor.viewName : viewName),
	  itemType: itemType, 
	  viewClass: viewClass,
	  listView: (view.embedded) ? listVar : "get${viewClass}()"
	]
	generator.instantiateCodeFromTemplateRef("ListView:${temp}", binding)
  }

  String selectNextCode(next, data = null) { 
    return generateTransitionCode(next, true, view.embedded as boolean, true, null, data)
  }

  String listItemActionCode() { 
	String actionCode = ''
	def actions = []
	if (numSections <= 1) { 
	  // Single section 
	  def section = sectionItems[0]
	  section.eachWithIndex { item, i -> 
		if (item.selection) {
		  String code = generator.generateActionCode(this, item['selection.src'], null)
		  actions << "case ${i}: ${code} break;"
		}
	  }
	  actionCode = """switch (position) {
${actions.join('\n')}
}"""
	} else { 
	  // multiple section 
	  sectionItems.eachWithIndex { sec, i ->
		def section = sections[i]
		String sectionActionCode = '' 
		if (section.selection) {
		  sectionActionCode = generator.generateActionCode(this, section['selection.src'], null)
		} 
		def actionItems = []
		sec.each { item ->
		  if (item.selection) { 
			actionItems << generator.generateActionCode(this, item['selection.src'], null)
		  }	else { 
			actionItems << ''
		  }    
		}

		def itemsCode = ''
		if (actionItems.size() == 1) { 
		  itemsCode = actionItems[0]
		} else if (actionItems.size() > 1) {
		  def cases = []				
		  actionItems.eachWithIndex{ code, j -> 
			if (code) 
			  cases << "case ${j}: ${code} break;" 
		  }
		  if (cases.size() > 0) { 
			itemsCode = """switch (position - adapter.firstInSection(${i})) {
${cases.join('\n')}
}"""
		  }
		}
		if (sectionActionCode) { 
		  if (itemsCode) 
			itemsCode = sectionActionCode + '\n' + itemsCode
		  else 
			itemsCode = sectionActionCode
		}
		if (itemsCode) { 
		  actions << """if (adapter.inSection(position) == ${i}) {
${indent(itemsCode, 1, '    ')}
}"""
		}

	  }
	  if (actions) { 
		actionCode = actions.join(' else ')
	  }
	}
	return actionCode
  }

  void handleChoiceMode() { 
    if (hasCheckBox) { 
      if (view.choiceMode == 'Single') { 
		classModel.onCreateScrap += """
${listVar}.setItemsCanFocus(false);
${listVar}.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
"""
      } else if (view.choiceMode == 'Multiple') { 
		classModel.onCreateScrap += """
${listVar}.setItemsCanFocus(false);
${listVar}.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
"""
      }

    }
  }

  void handleSelection() { 
    String selectionCode = null
    String actionDecl = ''

	def binding = [
	  itemType: itemType, 
	  listVar: listVar,
	  viewid: viewid,
	  contentHandler: contentHandler,
	]
    if (hasCheckBox) { 
      if (hasDetailText) { 
		if (view.choiceMode == 'Single') { 
		  actionDecl = generator.instantiateCodeFromTemplateRef("ListView:checkbox1", binding)
		  selectionCode = generator.instantiateCodeFromTemplateRef("ListView:checkbox2", binding)
		} else if (view.choiceMode == 'Multiple') { 
		  selectionCode = generator.instantiateCodeFromTemplateRef("ListView:checkbox3", binding)
		}
		generator.injectCodeFromTemplateRef(classModel, "ListView:checkbox4", binding)
	  } 
    }

	String listActionCode = ''
	String listMenuCode = ''
	String listTransitionCode = ''
    if (view.selection instanceof Closure) { 	
	  listActionCode = generator.generateActionCode(this, view['selection.src'], null)
    } else if (view.next) {
      listTransitionCode = selectNextCode(view.next)
    } else if (view.menu) {
      listMenuCode = generateTransitionCode(view.menu, true) 
    } 

    String actionCode = ''
	if (hasSelection) { 
	  actionCode = listItemActionCode()
	}
	if (hasNextView) { 
	  if (actionCode) actionCode += '\n'
      actionCode += selectListItemCode()
    }
	if (hasNextView || hasSelection) { 
	  actionCode = contentHandler.getAdapterScrap() + '\n' + actionCode
	}

	if (!actionCode) 
	  actionCode = listTransitionCode
	if (listActionCode) { 
	  if (actionCode)
		actionCode = listActionCode + '\n' + actionCode
	  else 
		actionCode = listActionCode
	}

    if (actionCode) { 
      if (selectionCode) { 
		selectionCode += ('\n' + actionCode)
      } else { 
		selectionCode = actionCode
      }
    }

    if (selectionCode) {
      classModel.imports << actionListenerImport

      classModel.onCreateScrap += """
${listVar}.set${actionListener}(new ${actionListener}() {
${indent(actionDecl, 1, '    ')}
    public ${actionMethodSignature} {
${indent(selectionCode, 2, '    ')}
    }
});
"""
    }
  }


  //
  // handling context menus 
  // 

  def processMenus() { 
    menuProcessor.processMenus(view)

    if (hasMenu || view.menu) { 
      menuProcessor.generateCreateMenuMethod()
      menuProcessor.generateActionMethod()	  
    } 

  }

  def handleReadWrite() { 
    contentHandler.generateReadWrite()
  }


  //
  //  generating layout 
  //

  void generateViewLayout() { 
    classModel.resources.viewLayouts["${listItemLayout}"] = generateListItemLayoutXML(view)

    int numSections = sectionTitles.size()
    if (numSections > 1) { 
      classModel.resources.viewLayouts["${listHeaderLayout}"] = generateListHeaderLayoutXML(view) 
    }
  }

  String generateListItemLayoutXML(view) { 
    def writer = new StringWriter()
    writer.println "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
    def xml = new PrettyMarkupBuilder(writer)    
    xml.setDoubleQuotes(true)

    def textView = 'TextView'
    def textAttr = [ 'android:id': "@+id/text_${viewid}",  
					 'android:layout_width': 'match_parent',
					 'android:layout_height': 'wrap_content',
					 'android:padding': '4dp',
					 'android:textSize': '16sp' ]    

    if (view.choiceMode) { 
      textView = 'CheckedTextView'
      if (view.choiceMode == 'Single') { 
		textAttr += [ 'android:checkMark': '?android:attr/listChoiceIndicatorSingle']
      } else if (view.choiceMode == 'Multiple') { 
		textAttr += [ 'android:checkMark': '?android:attr/listChoiceIndicatorMultiple']
      }
    }

    if (!useViewHolder) { 
      xml."${textView}"([ 'xmlns:android': XMLNS_ANDROID ] + textAttr)
    } else { 
      xml.LinearLayout('xmlns:android': XMLNS_ANDROID,
					   'android:layout_width': 'match_parent',
					   'android:layout_height': '?android:attr/listPreferredItemHeight',
					   'android:padding': '6dip') { 
		if (hasImage) { 
		  ImageView('android:id': "@+id/image_${viewid}",
					'android:layout_width': 'wrap_content',
					'android:layout_height': 'match_parent',
					'android:layout_marginRight': '6dip')
		}
		if (hasDetailText) { 
		  LinearLayout('android:orientation': 'vertical',
					   'android:layout_width': '0dip',
					   'android:layout_weight': '1',
					   'android:layout_height': 'match_parent') { 
			"${textView}"(textAttr)
			TextView('android:id': "@+id/detailText_${viewid}",  
					 'android:layout_width': 'match_parent',
					 'android:layout_height': '0dip',
					 'android:layout_weight': '1',
					 'android:singleLine': 'true',
					 'android:ellipsize': 'marquee',
					 'android:padding': '4dp',
					 'android:textSize': '12sp')
		  }	  
		} else { 
		  "${textView}"(textAttr)
		}
		if (hasAccessory) { 
		  ImageView('android:id': "@+id/accessory_${viewid}",
					'android:layout_width': 'wrap_content',
					'android:layout_height': 'match_parent',
					'android:layout_marginLeft': '6dip')
		}
      }      
    }

    def text = writer.toString()
    info text
    return text
  }

  String generateListHeaderLayoutXML(view) { 
    def writer = new StringWriter()
    writer.println "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
    def xml = new PrettyMarkupBuilder(writer)    
    xml.setDoubleQuotes(true)
    listHeaderAttributes['android:id'] = "@+id/header_${viewid}"
    xml.TextView(listHeaderAttributes)
    def text = writer.toString()
    info text
    return text
  }


  //
  //  generate code for expressions with entity dummy variable
  //

  String generateExpressionCode(widget, attr, eh) { 
    if (widget && attr) { 
      def src = widget["${attr}.src"]
      if (src != null && eh) { 
		eh.generateExpressionCode(src, 'data')
      }  else { 
		"\"${widget[attr]}\"" 
      }
    } else { 
      null
    }
  }

}