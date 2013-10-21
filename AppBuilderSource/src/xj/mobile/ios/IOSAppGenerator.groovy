
package xj.mobile.ios

import xj.mobile.*
import xj.mobile.common.*
import xj.mobile.model.*
import xj.mobile.model.ui.*
import xj.mobile.codegen.objc.UnparserIOS
import xj.mobile.lang.WidgetMap 

import xj.translate.Language
import xj.translate.Translator
import xj.translate.objc.ObjectiveCClassProcessor
import xj.translate.common.*

import static xj.translate.Logger.info 

class IOSAppGenerator extends AppGenerator {  
  
  String getTarget() { 'iOS' }

  void setUp() { 
    ViewProcessorFactory.setFactory('iOS')

    def unparser = new UnparserIOS()
    translator = new Translator(Language.ObjectiveC, unparser)
    translator.load('conf/View.groovy')

    unparser.setUnparseOptions([ UseNSInteger: true ])

	def typeMap = [:]
	WidgetMap.widgets.each { name, pmap ->
	  def pname = pmap['ios']
	  if (pname instanceof List) pname = pname[0]
	  typeMap[name] = 'UI' + pname
	  typeMap['xj.mobile.lang.madl.' + name] = 'UI' + pname
	}
  	ObjectiveCClassProcessor.ObjectiveCTypeMap = typeMap

    ModuleProcessor.currentClassProcessor = ModuleProcessor.classMap.get('View')
    Unparser.tab = '\t'
  }

  void cleanUp() { 
    translator = null
  }

}

