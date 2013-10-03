package xj.mobile.common

import xj.mobile.*
import xj.mobile.model.ui.*
import xj.mobile.model.properties.*
import xj.mobile.lang.*

import static xj.translate.Logger.info 

@Category(ViewProcessor)
class ListViewCategory { 

  // process table items
  // assume either a) sections/items b) items
  //    no mix of section/item allowed

  // Assume the following declaration
  // def sections          = []  // list of widgets (Section) 
  // def sectionTitles     = []
  // def sectionItems      = [] // list of setions -> list of items, each item is an Item
  // boolean hasNextView   = false
  // boolean hasMenu       = false
  // boolean hasSelection  = false
  // boolean hasDetailText = false
  // boolean hasImage      = false
  // boolean hasAccessory  = false
  // boolean hasCheckBox   = false
  // boolean hasEntity     = false 
  // boolean hasEntityMenuAction = false
  // int numSections = 0
  //
  // Set  nextViews

  void handleNextView(next) {
	if (next) { 
	  if (next instanceof Map) { 
		nextViews << next.to?.toString()
		if (next.data != null) { 
		  hasData = true
		}
	  } else {  
		nextViews << next?.toString()
	  }
	}
  }

  void handleWidget(widget) {
	if (widget) { 
	  if (widget.next) { 
		hasNextView = true
		handleNextView(widget.next)
	  }
	  if (widget.menu) { 
		hasMenu = true
	  }
	  if (widget.selection instanceof Closure) { 
		hasSelection = true
	  }
	}
  }

  void processListView() { 
    if (view) { 
	  handleNextView(view.next)

      def items = []
      view.children.each { Widget widget -> 
		if (widget.widgetType == 'Section') {  
		  // a multi-section lists
		  String name = widget.id
		  sectionTitles << widget.title
		  sections << widget
		  handleWidget(widget)
		 
		  items = []
		  widget.children.each { Widget item -> 
			if (item.widgetType == 'Item') { 
			  items << item
			  handleWidget(item)
			  if (item.type == 'CheckBox') { 
				view.choiceMode = 'Multiple'
			  }
			}
		  }
		  sectionItems << items
		} else if (widget.widgetType == 'Item') {
		  // items in a list without section
		  items << widget
		  handleWidget(widget)
		  if (widget.type == 'CheckBox') { 
			view.choiceMode = 'Multiple'
		  }
		}

		if (!sectionItems) { 
		  // handle a single section list
		  sectionTitles << null
		  sectionItems << items
		}
	  } 		
		
      numSections = sectionTitles.size()
	  singleSection = sections.size() <= 1

	  sectionEntities = sectionItems.collect { it[0]['#entity'] }
	  hasEntity = sectionEntities.any { it != null } 

      hasDetailText = sectionItems.any { sec -> sec.any { it.detailText } }
      hasImage      = sectionItems.any { sec -> sec.any { it.image } }
      hasAccessory  = sectionItems.any { sec -> sec.any { it.accessory } }
      hasCheckBox   = (view.choiceMode == 'Single' || view.choiceMode == 'Multiple')

	  processPopupsInList()

	  handleReadWrite = hasEntity //&& hasEntityMenuAction

	  info "[ListViewCategory] sectionTitles: ${sectionTitles}"
	  info "[ListViewCategory] sectionItems: ${sectionItems}"
	  info "[ListViewCategory] nextViews: ${nextViews}"
    }
  }

  // list of item types 
  def findAllItemTypes(boolean styleOnly = true) { 
    def types = [] as Set
    if (sectionItems) { 
      sectionItems.each { section ->
		section.each { item -> 
		  types << determineItemType(item, styleOnly)
		}
      }
    }
    return types
  }

  // list of list of item types 
  def findAllItemTypesEachSection(boolean styleOnly = true) { 
    def types = [] 
    if (sectionItems) {
      sectionItems.each { section ->
		def secTypes = [] as Set
		section.each { item -> 
		  secTypes << determineItemType(item, styleOnly)
		}
		types << secTypes
      }
    }
    return types
  }

  def determineItemType(item, boolean styleOnly = true) { 
    if (item) { 
      def style = item.style
      def accessory = item.accessory
      if (!style) { 
		style = CellStyle.Default
      }
      if (item.detailText || item.image) { 
		if (style == CellStyle.Default) { 
		  style = CellStyle.Subtitle
		}
      }
      if (!accessory) { 
		accessory = AccessoryType.None
      }

      if (styleOnly)
		return style
      else 
		return [style, accessory]
    }
    return null
  }

  def itemTypeToID(itemType, boolean styleOnly = true) { 
    if (itemType) { 
      if (itemType instanceof List) { 
		return itemType.collect{ it.toShortString() }.join('-')
      } else { 
		return itemType.toShortString()
      }
    }
    return null
  }

  def processPopupsInList() { 
	processPopupsInList(view) 
  }

  def processPopupsInList(group) { 
	if (Language.isView(group?.widgetType)) { 
	  group.children.each { Widget widget -> 
		if (Language.isPopup(widget.widgetType)) {  
		  boolean hasRead = widget.children.any { item -> item['action.src']?.readEntity }
		  boolean hasWrite = widget.children.any { item -> item['action.src']?.writeEntity }
		  widget.'#readEntity' = hasRead
		  widget.'#writeEntity' = hasWrite
		  hasEntityMenuAction |= (hasRead || hasWrite)
		} else if (Language.isGroup(widget.widgetType)) { 
		  processPopupsInList(widget)
		}
	  }
    }
  }

  ///////
  //
  //  utilities 
  //
  ///////

  int findSectionForWidget(widget) { 
    if (widget) { 
      def sec = null 
      def w = widget
      while (w && !sec) { 
		if (w.widgetType == 'Section')
		  sec = w
		else 
		  w = w.parent
      }
      if (sec) { 
		return sections.indexOf(sec)
      }
      return 0
    }
    return -1
  }

  def findEntityHandler(widget) { 
    if (contentHandler.hasEntity) {
      int i = findSectionForWidget(widget)
      if (i >= 0 && sectionEntities[i])
		return contentHandler.getSectionHandler(i)
    }
    return null
  }

  boolean isDummy(varname) { 
    def entities = view['#entities']
    if (entities) { 
      for (e in entities) { 
        if (e._dummy_ == varname) return true
      }
    }
    return false
  }


}
