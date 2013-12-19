package xj.mobile.common

import xj.mobile.api.AttributeHandler
import xj.mobile.codegen.templates.WidgetTemplates
import xj.mobile.codegen.CodeGenerator

import xj.mobile.model.properties.ModalTransitionStyle

import static xj.mobile.common.ViewProcessor.*

class WidgetProcessor { 

  CodeGenerator generator

  ViewProcessor vp
  def engine

  @Delegate
  WidgetTemplates widgetTemplates

  AttributeHandler attributeHandler

  WidgetProcessor(ViewProcessor vp) { 
    this.vp = vp
    engine = vp.generator.engine							
	attributeHandler = vp.generator.attributeHandler
  }

  static getActionInfo(widget) { 
    if (widget.action instanceof Closure) { 	
      return widget['action.src']
    } else if (widget.selection instanceof Closure) { 	
      return widget['selection.src']
    } else { 
	  return widget['action.src']
	}
    return null
  }

  static getWidgetAttributes(widget, exclude = null) { 
    if (widget) { 
      def attrs = []
      widget.properties.each { k, v -> 
		if (k[0] != '#' && 
			!k.endsWith('.src') &&
			!(k in ['class', 'id', 'parent', 
					'_frame', 'frame',
					'next',
					'action', 'selection' ]) &&
			(exclude == null || !(k in exclude))) { 
		  attrs << k
		}
      }
      return attrs
    }
    return null
  }

  def genActionCode(widget) { 
    String actionCode = null
    def srcInfo = getActionInfo(widget)
    if (srcInfo) { 
	  actionCode = generator.generateActionCode(vp, srcInfo, widget)
    } else if (widget.next || widget.menu) {
      def next = widget.next
      if (!next) next = widget.menu
	  boolean animated = true
	  ModalTransitionStyle transition = null
	  if (widget.animated != null) 
		animated = widget.animated as Boolean
	  if (widget.transition instanceof ModalTransitionStyle)
		transition = widget.transition

	  String nextState = null
	  def data = null
	  if (next instanceof Map) {
		nextState = next.to?.toString()
		data = next.data
	  } else { 
		nextState = next?.toString()
	  }
	  if (nextState) { 
		actionCode = vp.generateTransitionCode(nextState, isInsideNavigationView(widget), 
											   vp.view?.embedded as boolean, 
											   animated, transition, data)
	  }
    }
    return actionCode
  }
}