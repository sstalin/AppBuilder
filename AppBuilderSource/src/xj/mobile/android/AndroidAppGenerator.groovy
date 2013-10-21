
package xj.mobile.android

import xj.mobile.*
import xj.mobile.common.*
import xj.mobile.model.*
import xj.mobile.model.ui.*
import xj.mobile.codegen.java.UnparserAndroid
import xj.mobile.lang.WidgetMap 

import xj.translate.Language
import xj.translate.Translator
import xj.translate.java.JavaClassProcessor
import xj.translate.common.*

import static xj.mobile.android.DefaultViewProcessor.*
import static xj.translate.Logger.info 

class AndroidAppGenerator extends AppGenerator {  

  String getTarget() { 'Android' }

  void setUp() { 
    ViewProcessorFactory.setFactory('Android')

    def unparser = new UnparserAndroid() 
    translator = new Translator(Language.Java, unparser)
    translator.load(Main.confDir + '/View.groovy')

	def typeMap = [ 'String' : 'CharSequence' ]

	WidgetMap.widgets.each { name, pmap ->
	  def pname = pmap['android']
	  if (pname instanceof List) pname = pname[0]
	  //typeMap[name] = pname
	  typeMap['xj.mobile.lang.madl.' + name] = pname
	}
  	JavaClassProcessor.JavaTypeMap = typeMap
      
    ModuleProcessor.currentClassProcessor = ModuleProcessor.classMap.get('View')
    Unparser.tab = '    '
  }

  void cleanUp() { 
    translator = null
  }

}