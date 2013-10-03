package xj.mobile.ios

import xj.mobile.*
import xj.mobile.model.ui.*
import xj.mobile.lang.*
import xj.mobile.common.ListSectionDataMode
import xj.mobile.common.ListViewData
import xj.mobile.common.ListViewCategory
import xj.mobile.common.ViewProcessor

import static xj.mobile.ios.IOSAppGenerator.*
import static xj.mobile.util.CommonUtil.*
import static xj.mobile.codegen.IOSUtil.*

import static xj.translate.Logger.info 

import xj.translate.common.ModuleProcessor

class ListViewDataSectionHandler 
extends xj.mobile.common.ListViewDataSectionHandler { 

  @groovy.lang.Delegate
  ListViewProcessor lvp

  @groovy.lang.Delegate
  DefaultViewProcessor dvp

  @groovy.lang.Delegate
  ViewProcessor vp

  @groovy.lang.Delegate
  ListViewData listData

  ListViewDataSectionHandler(ListViewProcessor lvp, pos) { 
	this.lvp = lvp
	vp = dvp = lvp
	this.listData = lvp?.listData
	this.pos = pos 
  } 

  def declarations() { 
	def decl = []
    if (sectionItems) { 
	  decl << [ "${dataVarName}", "${dataVarType} *${dataVarName}" ]
	
	  if (view.choiceMode == 'Multiple') { 
		decl << [ 'kTextKey', 'static NSString *kTextKey = @"textKey"' ]
		decl << [ 'kStateKey', 'static NSString *kStateKey = @"stateKey"' ]
		
		if (dataMode == ListSectionDataMode.Tuple) { 
		  if (hasDetailText) 
			decl << [ 'kDetailKey', 'static NSString *kDetailKey = @"detailKey"' ]
		  if (hasImage) 
			decl << [ 'kImagetKey', 'static NSString *kImagetKey = @"imageKey"' ]
		}
	  }
	}
	return decl
  }

  def getInitDataSectionScrap() { 
	if (dataMode == ListSectionDataMode.Text) { 
	  initDataSectionTextModel()
	} else if (dataMode == ListSectionDataMode.Tuple) {  
	  initDataSectionTupleModel()
	} else { 
	  'nil'
	}
  }

  def initDataSectionTextModel() {
	def sec = sectionItems[pos]
	if (view.choiceMode == 'Multiple') {
	  /*
	  def seclist = sec.collect {
		"""[NSMutableDictionary dictionaryWithObjectsAndKeys:
\t\t\t@\"${it.text}\", kTextKey, 
\t\t\t[NSNumber numberWithInteger:FALSE], kStateKey, nil]""" }
	  */

	  def seclist = sec.collect {
		"[@{ kTextKey: @\"${it.text}\", kStateKey: [NSNumber numberWithInteger:FALSE] } mutableCopy]" }
	  toNSArrayWithObjects(seclist, '\n\t\t')
	} else { 
	  toNSArrayWithStrings(sec.collect { it.text }, '\n\t\t') 
	}
  }

  def initDataSectionTupleModel() { 
	def numColumns = 1
	if (listData.hasDetailText) 
	  colDetailText = numColumns++;			
	if (listData.hasImage) 
	  colImage = numColumns++;
	if (listData.hasAccessory) 
	  colAccessory = numColumns++;

	def sec = sectionItems[pos]
	if (view.choiceMode == 'Multiple') {
	  def seclist = sec.collect {
		def row = [ "kTextKey: @\"${it.text}\",",
					"kStateKey: [NSNumber numberWithInteger:FALSE]," ]
		if (listData.hasDetailText)
		  row << "kDetailKey: @\"${it.detailText}\","	      
		if (listData.hasImage) 
		  row << "kImageKey: @\"${it.image}\","
		def dictionary = row.collect { r -> "${r}" }.join('\n\t\t\t')
		"[@{ ${dictionary} } mutableCopy]"

		/*
		def row = [ "@\"${it.text}\", kTextKey,",
					"[NSNumber numberWithInteger:FALSE], kStateKey," ]
		if (listData.hasDetailText)
		  row << "@\"${it.detailText}\", kDetailKey,"	      
		if (listData.hasImage) 
		  row << "@\"${it.image}\", kImageKey,"
		def dictionary = row.collect { r -> "\t\t\t${r}" }.join('\n')
		"""[NSMutableDictionary dictionaryWithObjectsAndKeys:
${dictionary} nil]"""
		*/ 
	  } 
	  toNSArrayWithObjects(seclist,  '\n\t\t')
	} else { 
	  def seclist = sec.collect { 
		def row = [ "@\"${it.text}\"" ]
		if (listData.hasDetailText) { 
		  def detail = it.detailText ? "@\"${it.detailText}\"" : '[NSNull null]'
		  row << detail
		}
		if (listData.hasImage) { 
		  def image = it.image ? "@\"${it.image}\"" : '[NSNull null]'
		  row << image
		  if (it.image) 
			classModel.addImageFile(it.image)
		}
		if (listData.hasAccessory) { 
		  def num = it.accessory ? it.accessory.toIOSString() : 'UITableViewCellAccessoryNone'
		  row << "[NSNumber numberWithInt:${num}]"
		}
		toNSArrayWithObjects(row, '\n\t\t\t')
	  }
	  toNSArrayWithObjects(seclist,  '\n\t\t' ) 
	}
  }

  def getDataScrap() { 
	if (view.choiceMode == 'Multiple') { 
	  """NSArray *sec = (NSArray *) [${dataVarName} objectAtIndex:indexPath.section];    
NSMutableDictionary* data = (NSMutableDictionary *) [sec objectAtIndex:indexPath.row];"""
	} else {
	  "NSArray *data = (NSArray *) [${dataVarName} objectAtIndex:indexPath.section];"
	}
  }

  def getCellTextScrap() { 
	if (view.choiceMode == 'Multiple') { 
	  '[data objectForKey:kTextKey]'
	} else { 
	  if (dataMode == ListSectionDataMode.Text) { 
		'[data objectAtIndex:indexPath.row]'
	  } else {  
		'[[data objectAtIndex:indexPath.row] objectAtIndex:0]'
	  }
	}
  }

  def getCellDetailTextScrap() { 
	if (dataMode == ListSectionDataMode.Tuple) { 
	  if (view.choiceMode == 'Multiple') { 
		'[data objectForKey:kDetailKey]'
	  } else { 
		"[[data objectAtIndex:indexPath.row] objectAtIndex:${colDetailText}]"
	  }
	} else { 
	  ''
	}
  }

  def getCellImageScrap() { 
	if (dataMode == ListSectionDataMode.Tuple && hasImage) { 
	  if (view.choiceMode == 'Multiple') { 
		'[data objectForKey:kImageKey]'
	  } else { 
		"[[data objectAtIndex:indexPath.row] objectAtIndex:${colImage}]"
	  }
	} else { 
	  ''
	}
  }

  def getCellState() { 
	'[data objectForKey:kStateKey]'
  }

  def getCellAccessoryScrap() { 
	if (view.choiceMode == 'Multiple') { 
	  "([${cellState} boolValue] ? UITableViewCellAccessoryCheckmark : UITableViewCellAccessoryNone)"
	} else if (dataMode == ListSectionDataMode.Tuple &&
			   hasAccessory) { 
	  "[(NSNumber *) [[data objectAtIndex:indexPath.row] objectAtIndex:${colAccessory}] intValue]"
	} else { 
	  null
	}
  }


  /// handle selection 

  def getChoiceScrap() { 
	if (view.choiceMode) { 
	  if (view.choiceMode == 'Single') { 
		singleChoiceCode
	  } else if (view.choiceMode == 'Multiple') { 
		def binding = [ DataVar : dataVarName, 
						StateKey : 'kStateKey',
						action : '' ]
		def template = engine.createTemplate(choiceTemplate).make(binding)
		template.toString()
	  }
	} else { 
	  null
	}
  }

  static singleChoiceCode =  '''if ([indexPath isEqual:self.lastSelected]) return; 
UITableViewCell *old = [self.tableView cellForRowAtIndexPath:self.lastSelected];
if (old != nil) {
    old.accessoryType = UITableViewCellAccessoryNone;
    [old setSelected:FALSE animated:TRUE];
}
UITableViewCell *cell = [self.tableView cellForRowAtIndexPath:indexPath];
cell.accessoryType = UITableViewCellAccessoryCheckmark;
[cell setSelected:TRUE animated:TRUE];
self.lastSelected = indexPath;'''

  static choiceTemplate = '''NSArray *sec = (NSArray *) [${DataVar} objectAtIndex:indexPath.section];
NSMutableDictionary* data = (NSMutableDictionary *) [sec objectAtIndex:indexPath.row];
Boolean checked = [[data objectForKey:${StateKey}] boolValue];

UITableViewCell *cell = [self.tableView cellForRowAtIndexPath:indexPath];
[data setObject:[NSNumber numberWithInteger: !checked] forKey:${StateKey}];
if (checked) {
    cell.accessoryType = UITableViewCellAccessoryNone;
    [cell setSelected:FALSE animated:TRUE];
} else {
    cell.accessoryType = UITableViewCellAccessoryCheckmark;
    [cell setSelected:TRUE animated:TRUE];
}${action}'''

}
