package xj.mobile.ios

import xj.mobile.*
import xj.mobile.model.ui.*
import xj.mobile.lang.*
import xj.mobile.common.ListSectionDataMode
import xj.mobile.common.ListViewData
import xj.mobile.common.ListViewCategory
import xj.mobile.common.ViewProcessor
import xj.mobile.builder.ListEntity

import static xj.mobile.ios.IOSAppGenerator.*
import static xj.mobile.util.CommonUtil.*
import static xj.mobile.codegen.IOSUtil.*

import static xj.translate.Logger.info 

import xj.translate.common.ModuleProcessor

class ListViewContentHandler extends xj.mobile.common.ListViewContentHandler { 

  @groovy.lang.Delegate
  ListViewProcessor lvp

  @groovy.lang.Delegate
  DefaultViewProcessor dvp

  @groovy.lang.Delegate
  ViewProcessor vp

  @groovy.lang.Delegate
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

  } 

  void init() { 
	initVar()	

	def decl = dataSectionHandler.declarations()
	sectionHandlers.each { eh -> 
	  def dlist = eh.declarations()
	  dlist.each { d -> 
		if (!(d[0] in decl.collect { it[0] })) { 
		  decl << d
		}
	  }
	}

	classModel.declarationScrap += decl.collect { d -> d[1] + ';'}.join('\n') + '\n'
  }

  void initVar() { 
	dataVarName = "${lvp.view.id}Data"
	dataVarType = hasEntity ? 'NSMutableArray' : 'NSArray'
  }

  def getInitBody() { 
	def initCode = "${dataVarName} = ${getInitDataScrap()};"
	if (handleReadWrite) { 
	  """${dataVarName} = [self readData];
if (${dataVarName} == nil) {
${indent(initCode)}
}
""" 
	} else { 
	  initCode + '\n'
	}	
  }

  def getInitDataScrap() { 
	def list = (0 .. numSections - 1).collect { i -> 
	  getSectionHandler(i).getInitDataSectionScrap()
	}
	toNSArrayWithObjects(list, '\n\t', hasEntity) 
  }


  def getSectionTitleScrap() { 
    boolean sectionTitleAllNotNull = sectionTitles && sectionTitles.every { it != null }
    boolean sectionTitleAllNull = sectionTitles && sectionTitles.every { it == null }
    info "sectionTitles: ${sectionTitles}\n\t#=${numSections} allNotNull=${sectionTitleAllNotNull} allNull=${sectionTitleAllNull}"

    if (sectionItems && !sectionTitleAllNull) { 
      def cases = []
      sectionTitles.eachWithIndex { title, i ->  
		cases << "case ${i}: return ${sectionTitles[i] ? ('@\"' + sectionTitles[i] + '\"') : 'nil'};" 
      }
      """if (section >= 0 && section < ${numSections}) {
\tswitch (section) {\n\t${cases.join('\n\t')}
\t}
}
return nil;"""
    } else { 
	  'return nil;'
	}
  }

  def getRowsInSectionScrap() { 
    if (sectionItems) { 
       """if (section >= 0 && section < ${dataVarName}.count) {
\tNSArray *data = (NSArray *) [${dataVarName} objectAtIndex:section];
\treturn [data count];
}
return 0;"""
    } else { 
	  'return 0;'
	}
  }

  def getDataScrap() { 
	dataSectionHandler.getDataScrap() 
  }

  def getCellTextScrap() { 
	def head = 'cell.textLabel.text = (NSString *) '
	if (hasEntity) { 
	  if (singleSection) { 
		"${head}${getSectionHandler(0).getCellTextScrap()};"
	  } else { 		
		def stmts = sectionHandlers.collect { sh ->
		  "${head}${sh.getCellTextScrap()};"
		}
		if (stmts.every { it == stmts[0] }) { 
		  stmts[0] 
		} else { 
		  def cases = []
		  stmts.eachWithIndex { st, i ->
			cases << "case ${i}: ${st} break;"
		  }
		  """switch (indexPath.section) {\n${cases.join('\n')}
}
"""
		}
	  }
	} else { 
	  "${head}${dataSectionHandler.getCellTextScrap()};"
	}
  }

  def getCellDetailTextScrap() { 
	def head1 = 'NSString *detailText = (NSString *) '
	def head2 = 'detailText = (NSString *) '
	if (hasEntity) { 
	  if (singleSection) { 
		"${head1}${getSectionHandler(0).getCellDetailTextScrap()};"
	  } else { 		
		def stmts = sectionHandlers.collect { sh ->
		  "${head2}${sh.getCellDetailTextScrap()};"
		}
		if (stmts.every { it == stmts[0] }) { 
		  "NSString *${stmts[0]}"
		} else { 
		  def cases = []
		  stmts.eachWithIndex { st, i ->
			cases << "case ${i}: ${st} break;"
		  }
		  """NSString *detailText;
switch (indexPath.section) {\n${cases.join('\n')}
}
"""
		}
	  }
	} else { 
	  "${head1}${dataSectionHandler.getCellDetailTextScrap()};"	  
	}
  }

  def getCellImageScrap() { 
	def head1 = 'NSString *imageFile = (NSString *) '
	def head2 = 'imageFile = (NSString *) '
	if (hasEntity) { 
	  if (singleSection) { 
		"${head1}${getSectionHandler(0).getCellImageScrap()};"
	  } else { 		
		def stmts = sectionHandlers.collect { sh ->
		  "${head2}${sh.getCellImageScrap()};"
		}
		if (stmts.every { it == stmts[0] }) { 
		  "NSString *${stmts[0]}"
		} else { 
		  def cases = []
		  stmts.eachWithIndex { st, i ->
			cases << "case ${i}: ${st} break;"
		  }
		  """NSString *imageFile;
switch (indexPath.section) {\n${cases.join('\n')}
}
"""
		}
	  }
	} else { 
	  "${head1}${dataSectionHandler.getCellImageScrap()};"	  
	}

  }

  def getCellState() { 
	if (hasEntity) { 
	  getSectionHandler(0).getCellState()
	} else { 
	  dataSectionHandler.getCellState()
	}
  }

  def getCellAccessoryScrap() { 
	def head = 'cell.accessoryType = '
	if (hasEntity) { 
	  if (singleSection) { 
		def code = getSectionHandler(0).getCellAccessoryScrap()
		code ? "${head}${code};" : null		
	  } else { 		
		def stmts = sectionHandlers.collect { sh ->
		  "${head}${sh.getCellAccessoryScrap() ?: 'nil'};"
		}
		if (stmts.every { it == stmts[0] }) { 
		  stmts[0] 
		} else { 
		  def cases = []
		  stmts.eachWithIndex { st, i ->
			cases << "case ${i}: ${st} break;"
		  }
		  """switch (indexPath.section) {\n${cases.join('\n')}
}
"""
		}
	  }
	} else { 
	  def code = dataSectionHandler.getCellAccessoryScrap()
	  code ? "${head}${code};" : null
	}
  }


  /// handle selection 

  def getChoiceScrap() { 
	if (hasEntity) { 
	  getSectionHandler(0).getChoiceScrap()
	} else { 
	  dataSectionHandler.getChoiceScrap()
	}
  }


  ///////
  //
  //  read/write data 
  //
  ///////

  def fileType = 'PList'

  def generateReadWrite() { 
	if (handleReadWrite) { 
	  classModel.methodScrap += dataFilePathCode

	  def writeBodyTemp = writeDataCode[fileType] 
	  def binding = [ DataVar : dataVarName ]
	  def template = engine.createTemplate(writeBodyTemp).make(binding)
	  def writeBody = template.toString()

	  def readBody = readDataCode[fileType] 

	  classModel.methodScrap += """
- (void)writeData 
{
${indent(writeBody)}
}
"""

	  classModel.methodScrap += """
- (NSMutableArray*) readData
{
${indent(readBody)}
}
"""
	}
  }
  
  static dataFilePathCode = '''
- (NSString*) dataFilePath
{
    NSArray *paths = NSSearchPathForDirectoriesInDomains (NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsPath = [paths objectAtIndex:0];
    NSString *path = [documentsPath stringByAppendingPathComponent:@\"ListData.xml\"];    
    return path;
}
'''

  static writeDataCode = [
	PList:
	'''NSString *error;
id plist = [NSPropertyListSerialization dataFromPropertyList:${DataVar}
                                        format:NSPropertyListXMLFormat_v1_0 
                                        errorDescription:&error];
if (plist) {
    [plist writeToFile:[self dataFilePath] atomically:YES];
} else {
    NSLog(@"%@", error);
}
''', 

	Archive:
	'''if (![NSKeyedArchiver archiveRootObject:${DataVar} toFile:[self dataFilePath]]) { 
    NSLog(@"Write data failed.");         
}
''', 

	JSON:
	'''
''', 
  ]

  static readDataCode = [
	PList:
	'''NSString *errorDesc = nil;
NSPropertyListFormat format;
NSData *plistXML = [[NSFileManager defaultManager] contentsAtPath: [self dataFilePath]];
NSMutableArray *temp = (NSMutableArray *)[NSPropertyListSerialization
                                      propertyListFromData:plistXML
                                      mutabilityOption:NSPropertyListMutableContainersAndLeaves
                                      format:&format
                                      errorDescription:&errorDesc];
if (!temp) {
    NSLog(@"Error reading plist: %@, format: %d", errorDesc, format);
}
return temp;
''', 

	Archive:
	'''return (NSMutableArray*) [NSKeyedUnarchiver unarchiveObjectWithFile:[self dataFilePath]];
''', 

	JSON:
	'''
''', 
  ]


}
