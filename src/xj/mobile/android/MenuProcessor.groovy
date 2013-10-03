package xj.mobile.android

import groovy.xml.MarkupBuilder

import xj.mobile.*
import xj.mobile.model.ui.*
import xj.mobile.lang.Language
import xj.mobile.common.ViewProcessor

import static xj.mobile.android.DefaultViewProcessor.*
import static xj.mobile.common.ViewProcessor.*
import static xj.mobile.util.CommonUtil.*
import static xj.mobile.lang.ast.Util.*
import static xj.translate.Logger.info 

class MenuProcessor { 

  ViewProcessor vp

  List menuItemActions = [] // element [id: , code: ] for each menuitem
  List menuActions = []  // element [id: items: dataCode: ] for each menu 
  
  def eh = null
  boolean hasRead = false
  boolean hasWrite = false

  MenuProcessor(ViewProcessor vp) { 
    this.vp = vp;
  }

  void process(Widget widget) { 
    String name = getWidgetName(widget)
    info "[Android.MenuProcessor] process ${name}"

	eh = getEntityHandlerForMenu(widget)
    if (eh) { 
	  hasRead = widget.'#readEntity'
	  hasWrite = widget.'#writeEntity'
    }

    def writer = new StringWriter()
    writer.println "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
    def menuBuilder = new MarkupBuilder(writer)    
    menuBuilder.setDoubleQuotes(true)
	
    def idlist = []
    menuBuilder.menu('xmlns:android': XMLNS_ANDROID) { 
      widget.children.each { Widget w ->
		makeMenu(w, menuBuilder, idlist)
      }
    }

    def text = writer.toString()
    info "[Android.MenuProcessor] menu ${name}:\n" + text
    vp.classModel.resources.menuResources[name] = text

    def dataCode = null
    if (eh && (hasRead || hasWrite)) { 
      dataCode = eh.getDataScrap() 
	}
    menuActions << [id: widget.id, items: idlist, dataCode: dataCode]

  }

  void makeMenu(Widget widget, menuBuilder, idlist) { 
    if (widget) { 
      if (widget.widgetType == 'Item') { 
		menuBuilder.item('android:id': "@+id/${widget.id}",
						 'android:title': widget.text)
		//android:icon="@drawable/ic_help"    
		vp.generator.unparser.entityUnparser = eh
		def code = vp.widgetProcessor.genActionCode(widget) 
		if (code) { 
		  if (hasWrite) { 
			code = code + '''
adapter.notifyDataSetChanged();
writeData();'''		
		  }
		  menuItemActions << [id: widget.id, code: code]
		  idlist << widget.id
		}
		vp.generator.unparser.entityUnparser = null
      } else if (widget.widgetType == 'Group' ||
				 widegt.widgetType == 'Menu') { 
		String ename = widget.widgetType.toLowerCase()
		menuBuilder.ename() { 
		  widget.children?.each { Widget w ->  
			makeMenu(w, menuBuilder, idlist)
		  }
		}
      }
    }
  }

  def processMenus(group) { 
    //eh = getEntityHandlerForMenu(group)

    if (Language.isView(group?.widgetType)) { 
      group.children.each { Widget widget -> 
		if (Language.isMenu(widget.widgetType)) {  
		  process(widget)
		} else if (Language.isGroup(widget.widgetType)) { 
		  processMenus(widget)
		}
      }
    }  
  }

  def getEntityHandlerForMenu(menu) { 
    //if (menuid) { 
    //def menu = getWidget(menuid)
    //info "[ListViewProcessor.Android] searching for menu $menuid found: ${menu != null}"
    if (menu){ 
      boolean staticText = menu.children.every { it.'text.src' == null }
      info "[ListViewProcessor.Android] staticText: ${staticText}"
      if (!staticText) {
		return vp.findEntityHandler(menu)
      }
    }
    return null
  }

  def contextMenuScrap(menu, eh = null) { 
    def code = ''
    if (menu) { 
      if (eh) { 
		code += eh.getDataScrap() + '\n'
      }
      code += "inflater.inflate(R.menu.${menu.id}, menu);"
	  
      if (eh) { 
		menu.children.each { m ->
		  code += "\nmenu.findItem(R.id.${m.id}).setTitle(${vp.generateExpressionCode(m, 'text', eh)});"
		}
      }
    }
    return code
  }


  def generateCreateMenuMethod() {
    boolean hasEntityCode = false

    vp.classModel.imports << 'android.view.ContextMenu.ContextMenuInfo' << 'android.widget.AdapterView.AdapterContextMenuInfo'
    vp.classModel.onCreateScrap += """registerForContextMenu(list);"""

    def menuCases = ''
    def menuCode = null
    if (vp.view.menu) { 
      def menu = vp.getWidget(vp.view.menu)
      def eh = getEntityHandlerForMenu(menu)
      if (eh) hasEntityCode = true
      menuCode = "${contextMenuScrap(menu, eh)}"
    } else { 
      def menus = []
      int pos = 0
      if (vp.numSections <= 1) { 
		// Single section 
		def section = vp.sectionItems[0]
		def nextSet = [] as Set
		boolean hasNull = false
		section.each { row -> 
		  def next = row.next
		  if (!next) next = row.menu
		  if (next) nextSet << next else hasNull = true 
		}
		if (nextSet && nextSet.size() == 1 && !hasNull) { 
		  def c = section[0]
		  if (c.menu) { 
			def menu = vp.getWidget(c.menu)
			def eh = getEntityHandlerForMenu(menu)
			def adapterCode = ''
			if (eh) { 
			  hasEntityCode = true
			  adapterCode = """AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
position = info.position;
""" + vp.contentHandler.getAdapterScrap() + '\n'
			}
			menuCode = "${adapterCode}${contextMenuScrap(menu, eh)}"
		  }
		} else { 
		  section.eachWithIndex { c, i -> 
			if (c.menu) { 
			  def menu = vp.getWidget(c.menu)
			  def eh = getEntityHandlerForMenu(menu)
			  if (eh) hasEntityCode = true
			  menus << "case ${i}: ${contextMenuScrap(menu, eh)} break;"	
			}    
		  }
		  menuCases = """switch (info.position) {
${menus.join('\n')}
}"""
		}
      } else { 
		// multiple section 
		vp.sectionItems.eachWithIndex { sec, i ->
		  //pos++;
		  if (vp.sections[i].menu) { 
			def menu = vp.getWidget(vp.sections[i].menu)
			def eh = getEntityHandlerForMenu(menu)
			if (eh) hasEntityCode = true
			//def caseLabels = ''
			//sec.each { c ->	caseLabels += "case ${pos++}: " }
			//menus << (caseLabels + "${contextMenuScrap(menu, eh)} break;")	 
			menus << """if (adapter.inSection(info.position) == ${i}) {
${indent(contextMenuScrap(menu, eh), 1, '    ')}
}"""
		  } else { 
			def menuitems = []
			sec.each { c ->
			  if (c.menu) { 
				def menu = vp.getWidget(c.menu)
				def eh = getEntityHandlerForMenu(menu)
				if (eh) hasEntityCode = true
				//menuitems << "case ${pos}: ${contextMenuScrap(menu, eh)} break;"	    
				menuitems << contextMenuScrap(menu, eh)
			  }	else { 
				menuitems << ''
			  }    
			  //pos++;
			}
			def itemsCode = ''
			if (menuitems.size() == 1) { 
			  itemsCode = menuitems[0]
			} else if (menuitems.size() > 1) {
			  def cases = []
			  menuitems.eachWithIndex{ code, j ->
				if (code)
				  cases << "case ${j}: ${code} break;" 
			  }
			  if (cases.size() > 0) { 
				itemsCode = """switch (info.position - adapter.firstInSection(${i})) {
${cases.join('\n')}
}"""
			  }
			}
			if (itemsCode) { 
			  menus << """if (adapter.inSection(info.position) == ${i}) {
${indent(itemsCode, 1, '    ')}
}"""
			}
		  }
		}
		if (menus)
		  menuCases = menus.join(' else ')
      }
    }

    def setPositionScrap = ''
    //if (hasEntityMenuAction) { 
    if (hasEntityCode) { 
      vp.classModel.declarationScrap += '''
int position = -1; 
'''
      setPositionScrap = 'position = info.position;\n'
    }

    if (!menuCode) { 
      def adapterCode = (hasEntityCode || vp.numSections > 1) ? (vp.contentHandler.getAdapterScrap() + '\n') : ''
      //def adapterCode = contentHandler.getAdapterScrap() + '\n'
      menuCode = """AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo; 
${setPositionScrap}${adapterCode}${menuCases}"""
    }


    vp.classModel.methodScrap += """
@Override
public void onCreateContextMenu(ContextMenu menu, View v,
                                ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
    MenuInflater inflater = getMenuInflater();
${indent(menuCode, 1, '    ')}
}    
"""
  } 

  def generateActionMethod() { 
    def actionCode = null

    if (menuActions.size() == 1 ||
		menuActions.every { m -> m.dataCode == null }) { 
      if ( menuItemActions) { 
		def actionCases = []
		menuItemActions.each { item -> 
		  def code = item.code + '\n' + 'return true;'
		  actionCases << """case R.id.${item.id}:
${indent(code, 1, '    ')}"""
		}	  
		actionCode = """switch (item.getItemId()) {
${actionCases.join('\n')}
}"""
      }
      if (menuActions[0].dataCode != null) { 
		def adapterCode = vp.contentHandler.getAdapterScrap()
		actionCode = adapterCode + '\n' + menuActions[0].dataCode + '\n' + actionCode
      }
    } else { 
      def menuCases = []
      menuActions.eachWithIndex { m, i -> 
		def idlist = m.items
		def actionCases = []
		menuItemActions.each { item -> 
		  if (item.id in idlist) { 
			def code = item.code + '\n' + 'return true;'
			actionCases << """case R.id.${item.id}:
${indent(code, 1, '    ')}"""
		  }
		}
	
		def preAction = m.dataCode ? (m.dataCode + '\n') : ''
		def menuCode = """${preAction}switch (item.getItemId()) {
${actionCases.join('\n')}
}"""
		//def cond = idlist.collect { id -> "item.getItemId() == R.id.${id}"}.join(' || ')
		def cond = "adapter.inSection(info.position) == ${i}"
		menuCases << """if (${cond}) {
${indent(menuCode, 1, '    ')}
}"""
      }
      def adapterCode = vp.contentHandler.getAdapterScrap()
      actionCode = adapterCode + '\n' + menuCases.join(' else ')
    }

    if (actionCode) { 
      vp.classModel.methodScrap += """
@Override
public boolean onContextItemSelected(MenuItem item) {
    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
${indent(actionCode, 1, '    ')}
    return super.onContextItemSelected(item);
}
"""
    }
  }

}