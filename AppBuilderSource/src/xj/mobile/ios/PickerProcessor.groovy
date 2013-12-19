package xj.mobile.ios

import xj.mobile.*
import xj.mobile.common.*
import xj.mobile.model.ui.*
import xj.mobile.lang.*

import static xj.mobile.ios.IOSAppGenerator.*
import static xj.mobile.util.CommonUtil.*
import static xj.mobile.codegen.IOSUtil.*

import static xj.mobile.common.ViewProcessor.*

class PickerProcessor { 

  String dataVar

  void process(Widget widget, ViewProcessor vp) { 
    dataVar = "${widget.id}Data"

    int numComponents = 1
    if (widget.options) { 
      if (widget.options.every { it instanceof List }) { 
		numComponents = widget.options.size()
      }
    }
    
    def textData
    if (numComponents == 1) { 
      textData = toNSArrayWithStrings(widget.options, '\n\t')
    } else {  
      textData = toNSArrayWithObjects(widget.options.collect { sec -> 
		toNSArrayWithStrings(sec, '\n\t\t') 
	  }, '\n\t')
    }

	def binding = [
	  var: dataVar,
	  data: textData,
	  numComponents: numComponents,
	]
	vp.generator.injectCodeFromTemplateRef(vp.classModel, "Picker:picker1", binding)
	vp.generator.injectCodeFromTemplateRef(vp.classModel, "Picker:picker2", binding)

    def actionCode = null
    if (widget.selection instanceof Closure) { 	
	  actionCode = vp.generator.generateActionCode(vp, widget['selection.src'], widget)
      if (actionCode) { 
		binding += [
		  actionCode: actionCode
		]
		vp.generator.injectCodeFromTemplateRef(vp.classModel, "Picker:picker3", binding)
      }
    }

    def code
    if (numComponents == 1) { 
      code = "return ${widget.options.size()};"
    } else { 
      def cases = []
      widget.options.eachWithIndex { col, i ->  
		cases << "case ${i}: return ${widget.options[i].size()};" 
      }
      code = """switch (component) {
${cases.join('\n')}
}
return 0;"""
    }

	binding['code'] = code
	vp.generator.injectCodeFromTemplateRef(vp.classModel, "Picker:picker4", binding)
	vp.generator.injectCodeFromTemplateRef(vp.classModel, "Picker:picker5", binding)

    if (numComponents == 1) { 
      code = "return [${dataVar} objectAtIndex:row];"
    } else { 
      code = "return [[${dataVar} objectAtIndex:component] objectAtIndex:row];" 
    }
	binding['code'] = code
	vp.generator.injectCodeFromTemplateRef(vp.classModel, "Picker:picker6", binding)
	vp.generator.injectCodeFromTemplateRef(vp.classModel, "Picker:picker7", binding)
  }

}
