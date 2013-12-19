package xj.mobile.codegen.templates

import xj.mobile.lang.WidgetMap

import static xj.translate.Logger.info 

class PopupTemplates {

  static templates = [
    ios :     new IOSPopupTemplates('ios'),
    android : new AndroidPopupTemplates('android')
  ]

  static getPopupTemplates(target) { 
    return templates[target]
  }

  String target

  PopupTemplates(String target) { 
    this.target = target
  }

  def getTemplate(String name) { 
	popupTemplates[name]
  }

  def getPopupTemplateByName(name) { 
    def temp = null
    if (name) { 
      def wmap = WidgetMap.widgets[name]
      def pname = wmap ? wmap[target] : null  
      if (pname && pname instanceof List) { 
		pname = pname[0]
      }
      if (pname) { 
		temp = popupTemplates[pname]
      } else { 
		temp = popupTemplates[name]
      }
    }
    return temp
  }

  static String actionTemplateName(boolean isMenu) { 
	isMenu ? 'actionMenu' : 'action'
  }

  def getActionTemplate(String popupType, boolean isMenu) { 
	def temp = popupTemplates[popupType]
	if (temp)
	  return temp[actionTemplateName(isMenu)]
	else 
	  return null
  }

} 