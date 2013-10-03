package xj.mobile.ios

import xj.mobile.*
import xj.mobile.common.*
import xj.mobile.model.ui.*
import xj.mobile.lang.*

import static xj.mobile.ios.IOSAppGenerator.*
import static xj.mobile.util.CommonUtil.*
import static xj.mobile.common.ViewProcessor.*

class RadioButtonProcessor { 

  void process(Widget widget, ViewProcessor vp) { 
    def wp = vp.widgetProcessor
    def wtemp = wp.getWidgetTemplate(widget)
    String actionCode = ''
    def codeTemplate = null
    String name = getWidgetName(widget)
    def binding = [ name :  name ]
    def rgroup = widget.parent
    if (rgroup?.widgetType == 'RadioGroup') { 
      binding['group'] = getWidgetName(rgroup)
      def members = vp.radioGroups[rgroup.id]
      if (members.size() >= 3) { 
		codeTemplate = wp.getTemplate(wtemp, 'actionCode3') 
      } else if (members.size() == 2) {
		def other = (members[0] == name ? members[1] : members[0])
		binding['other'] = other
		codeTemplate = wp.getTemplate(wtemp, 'actionCode2') 
      } 
    }
    if (!codeTemplate)  { 
      codeTemplate = wp.getTemplate(wtemp, 'actionCode') 
    }

    def template = wp.engine.createTemplate(codeTemplate).make(binding)
    actionCode = template.toString()    

    String userCode = wp.genActionCode(widget)
    if (userCode) {
      if (actionCode) { 
		actionCode += ('\n' + userCode) 
      } else { 
		actionCode = userCode
      }
    }
	vp.classModel.injectActionCode(widget.widgetType, getWidgetName(widget), wtemp, actionCode) 

  }

}