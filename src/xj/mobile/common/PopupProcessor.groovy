package xj.mobile.common

import xj.mobile.lang.WidgetMap

import xj.mobile.codegen.templates.PopupTemplates

import static xj.translate.Logger.info 

class PopupProcessor { 

  ViewProcessor vp
  def engine 

  @Delegate
  PopupTemplates popupTemplates

  PopupProcessor(ViewProcessor vp) { 
    this.vp = vp;
    engine = vp.generator.engine
  }

  String getTemplateName(String wtype) { 
	String tname = null 
	if (wtype) { 
      if (popupTemplates.getTemplate(wtype)) { 
		tname = wtype
	  } else { 
		def wmap = WidgetMap.widgets[wtype]
		def pname = wmap ? wmap[vp.widgetProcessor.target] : null  
		if (pname && pname instanceof List) { 
		  tname = pname[0]
		} else { 
		  tname = pname
		}
	  }
	}
	return tname
  }

  def getPopupTemplate(widget) { 
    def temp = null
    if (widget) { 
      def name = widget.widgetType 
      info "[PopupProcessor] getPopupTemplate() name: ${name}"
	  def pname = getTemplateName(name)
      if (pname) { 
		temp = popupTemplates.getTemplate(pname)
      } else { 
		temp = popupTemplates.getTemplate(name)
      }
    }
    return temp
  }

  def getActionTemplate(wtype) { 
    popupTemplates.getTemplate(getTemplateName(wtype))?."${getActionTemplateName(wtype)}"
  }

  def getActionTemplateName(wtype) { 
	'action'
  }

  boolean isMenu(wtype) { 
	false
  }

}