package xj.mobile.ios

import xj.mobile.*
import xj.mobile.model.ui.*
import xj.mobile.lang.*
import xj.mobile.common.ListSectionDataMode
import xj.mobile.common.ListViewData
import xj.mobile.common.ListViewCategory

import xj.translate.common.ModuleProcessor

import static xj.mobile.ios.IOSAppGenerator.*
import static xj.mobile.util.CommonUtil.*

import static xj.translate.Logger.info 

@Mixin(ListViewCategory)
class ListViewProcessor extends DefaultViewProcessor { 

  public ListViewProcessor(View view, String viewName = null) { 
    super(view, viewName)
    listData = new ListViewData()

    classModel.superClassName = 'UITableViewController'
    classModel.initViewWithSuper = true
  }
  
  @groovy.lang.Delegate
  ListViewData listData

  int colText       = 0
  int colDetailText = 1
  int colImage      = 2
  int colAccessory  = 3

  ListViewContentHandler contentHandler

  void process() { 
    if (view) { 
	  currentViewProcessor = this

      // common processing 
      processListView()   // ListViewCategory
      initializeTopView() // DefaultViewProcessor
             
      if (hasDetailText || hasImage || hasAccessory) { 
		dataMode = ListSectionDataMode.Tuple 
      }

      contentHandler = new ListViewContentHandler(this)

      // iOS specific processing 
      initializeData()
      handleSectionTitles()
      handleDataItem()
      processPopups() 
      handleChoiceMode()
      handleSelection()
      handleReadWrite()

      // common post-processing 
      postProcessTopView()
    }
  }

  // var name in genenrated code 
  String dataVarName
  String dataVarType 

  def initializeData() {
    contentHandler.init()

    if (sectionItems) { 
      def initBodyScrap = contentHandler.getInitBody()
    
      def superInitCall = 'init'
      if (view.style) { 
		superInitCall = "initWithStyle: ${view.style.toIOSString()}"
      }
	  def binding = [
		superCall: superInitCall,
		body: initBodyScrap,
	  ]
	  generator.injectCodeFromTemplateRef(classModel, "ListView:init", binding)
    }    
  } 
  
  def handleSectionTitles() { 
    def title = contentHandler.getSectionTitleScrap()
	def rows = contentHandler.getRowsInSectionScrap()
    def count = sectionItems ? "${dataVarName}.count" : '0'

	def binding = [
	  title: title,
	  count: count,
	  rows: rows,
	]
	generator.injectCodeFromTemplateRef(classModel, "ListView:section1", binding)
	generator.injectCodeFromTemplateRef(classModel, "ListView:section2", binding)
	generator.injectCodeFromTemplateRef(classModel, "ListView:section3", binding)

  }

  def handleDataItem() { 
    def itemTypes = findAllItemTypes()
    def itemTypesEachSection = findAllItemTypesEachSection()
    info "itemTypes: ${itemTypes}"
    info "itemTypesEachSection: ${itemTypesEachSection}"

    //boolean sameItemStyle = true
    //boolean sameAccessory = true

    def cellScrap = 'return nil;'
    if (sectionItems) { 
      def cellIDScrap = 'static NSString *cellId = @\"Cell-Default\";'
      if (itemTypes.size() > 1) { 
		cellIDScrap = 'NSString *cellId = @\"Cell\";'
		def cases = []
		itemTypesEachSection.eachWithIndex { sec, i ->
		  if (sec.size() == 1) { 
			def ctype = itemTypeToID((sec as List)[0])
			cases << "case ${i}: cellId = @\"Cell-${ctype}\"; break;"
		  } else { 	    
			def items = []
			sectionItems[i].eachWithIndex { c, j -> 
			  def ctype = itemTypeToID(determineItemType(c)) 
			  items << "case ${j}: cellId = @\"Cell-${ctype}\"; break;"	    
			}
			cases << """case ${i}:
\tswitch (indexPath.row) {
\t${items.join('\n\t')}
\t}
\tbreak;"""
		  }
		}
		cellIDScrap += "\nswitch (indexPath.section) {\n${cases.join('\n')}\n}"
      }

      def cellStyleScrap = ''
      def cellInitScrap = ''
      if (itemTypes.size() > 1) { 
		cellStyleScrap = 'UITableViewCellStyle cellStyle = UITableViewCellStyleDefault;'
		def cases = []
		itemTypesEachSection.eachWithIndex { sec, i ->
		  if (sec.size() == 1) { 
			def ctype = (sec as List)[0].toIOSString()
			cases << "case ${i}: cellStyle = ${ctype}; break;"
		  } else { 	    
			def items = []
			sectionItems[i].eachWithIndex { c, j -> 
			  def ctype = determineItemType(c).toIOSString()
			  items << "case ${j}: cellStyle = ${ctype}; break;"	    
			}
			cases <<  """case ${i}:
\tswitch (indexPath.row) {
\t${items.join('\n\t')}
\t}
\tbreak;"""
		  }
		}
		cellStyleScrap += "\nswitch (indexPath.section) {\n${cases.join('\n')}\n}"
		cellInitScrap = """${cellStyleScrap}
cell = [[UITableViewCell alloc] initWithStyle:cellStyle reuseIdentifier:cellId];"""
      } else { 
		def cellStyle = (itemTypes as List)[0].toIOSString()
		cellInitScrap = "cell = [[UITableViewCell alloc] initWithStyle:${cellStyle} reuseIdentifier:cellId];"
      }

      def getData = contentHandler.getDataScrap()
      def textCode = contentHandler.getCellTextScrap()

      def detailTextCode = ''
      def cellImageCode = ''
      def cellAccessoryCode = ''
      if (dataMode == ListSectionDataMode.Tuple) { 
		if (hasDetailText) { 
		  detailTextCode = contentHandler.getCellDetailTextScrap() + generator.instantiateCodeFromTemplateRef("ListView:cell1")
		}
		if (hasImage) { 
		  cellImageCode = contentHandler.getCellImageScrap() + generator.instantiateCodeFromTemplateRef("ListView:cell2")
        }
      }
	  
      def accessory = contentHandler.getCellAccessoryScrap()
      if (accessory) { 
		cellAccessoryCode = """${accessory}
"""
      }

	  def binding = [ 
		cellID: cellIDScrap,
		cellInit: cellInitScrap,
		getData: getData,
		text: textCode,
		detailText: detailTextCode,
		cellImage: cellImageCode,
		cellAccessory: cellAccessoryCode,
	  ]
	  cellScrap = generator.instantiateCodeFromTemplateRef("ListView:cell3", binding)
    } 

	def binding = [ 
	  cellScrap: cellScrap,
	]
	generator.injectCodeFromTemplateRef(classModel, "ListView:cell4", binding)
  }

  void handleChoiceMode() { 
	if (view.choiceMode) { 
      if (view.choiceMode == 'Single') { 
		classModel.propertyScrap += '\n@property(strong, nonatomic) NSIndexPath* lastSelected;'
		classModel.propertyNames += 'lastSelected'
      }
    }
  }

  String generateNextViewCode(next) { 
	boolean inNavView = isInsideNavigationView(view)
	def (String nextState, data) = getTransitionInfo(next)	
	if (nextState) { 
	  return generateTransitionCode(nextState, inNavView, 
									view.embedded as boolean,
									true, null, data)
	}
	return ''
  }

  def handleSelection() { 
    if (sectionItems) { 
      String selectionCode = contentHandler.getChoiceScrap()
	  boolean inNavView = isInsideNavigationView(view)

	  String listActionCode = ''
	  String listMenuCode = ''
	  String listTransitionCode = ''
      if (view.selection instanceof Closure) {
		listActionCode = generator.generateActionCode(this, view['selection.src'], null)
      } 
	  if (view.next) {
		listTransitionCode = generateTransitionCode(view.next, inNavView, view.embedded as boolean)
      } 
	  if (view.menu) {
		listMenuCode = generateTransitionCode(view.menu, inNavView, view.embedded as boolean) 
      } 

      String actionCode = ''
      String menuActionCode = ''
	  if (hasNextView || hasMenu || hasSelection) { 
		def sectionTransCases = []
		def sectionMenuCases = []
		sectionItems.eachWithIndex { section, i ->  
		  def nextSet = [] as Set
		  boolean hasNull = false
		  boolean hasRowSel = false
		  boolean hasMenuInSec = false
		  section.each { row -> 
			def next = row.next
			if (!next) next = row.menu
			if (next) nextSet << next else hasNull = true 
			if (row.menu) hasMenuInSec = true
			if (row.selection instanceof Closure) hasRowSel = true
		  }
		  def sec = sections[i]

		  String sectionActionCode = ''
		  String sectionMenuCode = ''
		  String sectionTransitionCode = ''
		  if (sec) { 
			def snext = sec.next
			if (!snext) snext = sec.menu
			info "[ListViewProcessor] handleSelection(): sec.menu: ${sec.id} ${snext}"
			if (sec.selection instanceof Closure) { 
			  sectionActionCode = generator.generateActionCode(this, sec['selection.src'], null)
			}
			if (sec.next) { 
			  sectionTransitionCode = generateNextViewCode(sec.next) 
			}
			if (sec.menu) { 
			  sectionMenuCode = generateNextViewCode(sec.menu) 
			}
		  }
	  
		  def secTransCode = ''
		  def secMenuCode = ''
		  if (nextSet || hasRowSel) { 
			// individual row response 
			if (nextSet.size() == 1 && !hasNull) { 
			  def next = nextSet.iterator().next()
			  def (String nextState, data) = getTransitionInfo(next)
			  if (nextState) { 
				secTransCode = generateTransitionCode(nextState, inNavView, view.embedded as boolean, 
											   true, null, data) 
				if (secTransCode) secTransCode += ' ' else secTransCode = ''

				if(hasMenuInSec)
				  secMenuCode = secTransCode
			  }
			} else { 
			  def itemTransCases = []
			  def itemMenuCases = []
			  section.eachWithIndex { row, j -> 
				def transCode = ''
				def menuCode = ''
				if (row) { 
				  if (row.selection instanceof Closure) { 
					transCode = generator.generateActionCode(this, row['selection.src'], null)
				  }
				  def next = row.next
				  if (!next) next = row.menu
				  if (next) { 
					def (String nextState, data) = getTransitionInfo(next)
					if (nextState) { 				  
					  if (transCode) transCode += '\n'
					  transCode += generateTransitionCode(nextState, inNavView, view.embedded as boolean, 
														  true, null, data) 
					}
				  } else { 
					if (sectionTransitionCode) { 
					  if (transCode) transCode += '\n'
					  transCode += sectionTransitionCode
					} else if (listTransitionCode) { 
					  if (transCode) transCode += '\n'
					  transCode += listTransitionCode
					}
				  }
				  if (transCode) { 
					transCode = '\n' + indent(transCode, 1) + '\n\t'
				  } else transCode = ''	

				  if (row.menu) { 
					def (String nextState, data) = getTransitionInfo(row.menu)
					if (nextState) { 				  
					  if (menuCode) menuCode += '\n'
					  menuCode += generateTransitionCode(nextState, inNavView, view.embedded as boolean, 
														 true, null, data) 
					}
				  } else { 
					if (sectionMenuCode) { 
					  if (menuCode) menuCode += '\n'
					  menuCode += sectionMenuCode
					} else if (listMenuCode) { 
					  if (menuCode) menuCode += '\n'
					  menuCode += listMenuCode
					}
				  }
				}
				itemTransCases << "case ${j}: ${transCode}break;"
				if (menuCode) { 
				  menuCode = '\n' + indent(menuCode, 1) + '\n\t'
				  itemMenuCases << "case ${j}: ${menuCode}break;"
				}				
			  }
			  secTransCode = "switch (indexPath.row) {\n" + itemTransCases.join('\n') + "\n}"
			  if (itemMenuCases) 
				secMenuCode = "switch (indexPath.row) {\n" + itemMenuCases.join('\n') + "\n}"
			}
		  } else { 
			// section response 
			if (sectionTransitionCode) { 
			  if (secTransCode) secTransCode += '\n'
			  secTransCode += sectionTransitionCode
			} else if (listTransitionCode) { 
			  if (secTransCode) secTransCode += '\n'
			  secTransCode += listTransitionCode
			}

			if (sectionMenuCode) { 
			  if (secMenuCode) secMenuCode += '\n'
			  secMenuCode += sectionMenuCode
			} else if (listMenuCode) { 
			  if (secMenuCode) secMenuCode += '\n'
			  secMenuCode += listMenuCode
			}			
		  } 		  
		  if (sectionActionCode) { 
			secTransCode = sectionActionCode + '\n' + secTransCode
		  }

		  if (secTransCode) { 
			secTransCode = '\n' + indent(secTransCode, 1) + '\n\t'
		  } else secTransCode = ''	
		  sectionTransCases << "case ${i}: ${secTransCode}break;" 

		  if (secMenuCode) { 
			secMenuCode = '\n' + indent(secMenuCode, 1) + '\n\t'
			sectionMenuCases << "case ${i}: ${secMenuCode}break;" 
		  } 
		}

		actionCode = "switch (indexPath.section) {\n" + sectionTransCases.join('\n') + "\n}\n"
		if (sectionMenuCases)
		  menuActionCode = "switch (indexPath.section) {\n" + sectionMenuCases.join('\n') + "\n}\n"
      }

      if (menuActionCode || listMenuCode) { 
		if (!menuActionCode) 
		  menuActionCode = listMenuCode
		handleLongPressMenu(menuActionCode)
      }

	  if (actionCode) { 
		if (selectionCode) { 
		  if (!hasMenu && !view.menu) { 
			selectionCode += ('\n' + actionCode)
		  }
		} else { 
		  selectionCode = actionCode
		}
      }
	  if (selectionCode || listActionCode || listTransitionCode) { 
		String cellAccessory = """
cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;"""
	
		def code = []
		if (listActionCode) code << listActionCode
		if (selectionCode) code << selectionCode
		if (!selectionCode && listTransitionCode) code << listTransitionCode
		def binding = [ 
		  selectionCode: code.join('\n'),
		]
		generator.injectCodeFromTemplateRef(classModel, "ListView:selection", binding)
	  }
    }
  }

  def handleReadWrite() { 
    contentHandler.generateReadWrite()
  }

  String longPressIndexPathVar = 'menuIndexPath'

  def handleLongPressMenu(actionCode) { 
    if (actionCode) { 
      classModel.delegates << 'UIGestureRecognizerDelegate'

	  def binding = [ 
		var: longPressIndexPathVar,
	  ]

      def indexPath = ''
      if (hasEntityMenuAction) { 
		generator.injectCodeFromTemplateRef(classModel, "ListView:longpress1", binding)
		indexPath = generator.instantiateCodeFromTemplateRef("ListView:longpress2", binding)
      }

	  binding += [ indexPath: indexPath ]
	  generator.injectCodeFromTemplateRef(classModel, "ListView:longpress3", binding)
	  actionCode = generator.instantiateCodeFromTemplateRef("ListView:longpress4", binding) + actionCode

	  binding += [ actionCode: actionCode ]
	  generator.injectCodeFromTemplateRef(classModel, "ListView:longpress5", binding)
    }
  }
   
}



