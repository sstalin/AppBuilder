
package xj.mobile.common

import java.awt.Font
import java.awt.Label
import java.awt.FontMetrics

import xj.mobile.model.ui.*
import xj.mobile.lang.*
import xj.mobile.model.properties.Font

import static xj.mobile.util.CommonUtil.linesInString

import static java.lang.Math.max
import static xj.translate.Logger.info 

class Layout { 

  // return dimension of view [width, height] (the content size)
  static def layout(View view, 
					def config,
					boolean setWidgetFrame = true) { 
    info "[Layout] view id=${view.id} type=${view.widgetType}"
    int m = config.defaults.margin
    int vw = 0, vh = 0
	boolean bounded = !(view.scroll || 
						view.children.any { w -> (w instanceof Widget) && w.scroll })
	vw = config.defaults.Screen.width - 2 * m
	vh = config.defaults.Screen.height - 2 * m
    if (view.nodeType == 'TabbedView') { 
      vh -= config.defaults.TabBar.height
    }
    return layout(view, m, m, vw, vh, m, m, config, bounded, setWidgetFrame)
  }

  // return [width, heght] (the content size)
  static layout(View view, 
				int x, int y,     // current position
				int rw, int rh,   // remaining width and height
				int mx, int my,   // margin x and y 
				def config,
				boolean bounded = true, 
				boolean setWidgetFrame = true) { 
    info "[Layout] layout(): id=${view.id} cur=(${x},${y}) remaining=(${rw},${rh})"

    if (view.nodeType == 'Table') { 
      return layoutTable(view, x, y, rw, rh, mx, my, config, bounded, setWidgetFrame)
    } 

    int vw = 0, vh = 0;
    if (view && view.children) { 
      boolean horizontal = (view.nodeType in [ 'Box', 'Row' ])
      if (view.orientation) { 
		horizontal = ('horizontal' == view.orientation)
      }

      int cx = x, cy = y
      int w = 0, h = 0
      boolean wfill = false, hfill = false 
      boolean topmost = true, leftmost = true
      view.children.each { widget -> 
		info "[Layout] ${view.id} child ${widget.id} isUI=${Language.isUI(widget.nodeType)} cur=(${cx},${cy}) remaining=(${rw},${rh}) size=(${vw},${vh})"

		if (Language.isTopView(widget.nodeType) && !widget.embedded) { 
		  layout(widget, config, setWidgetFrame)
		} else if (Language.isUI(widget.nodeType) &&
				   !Language.isPopup(widget.nodeType)) { 
		  if (Language.isGroup(widget.nodeType)) { 
			int rw0 = rw - (cx - x)
			int rh0 = rh - (cy - y)
			(w, h) = layout(widget, cx, cy, rw0, rh0, mx, my, config, bounded, setWidgetFrame)	    
		  } else if (Language.isTopView(widget.nodeType) && widget.embedded) {  
			(w, h) = [ rw, rh ]  // a group view will take the remaining space
		  } else { 
			if (widget.frame) { 
			  (cx, cy, w, h) = widget.frame
			} else { 
			  (cx, cy, w, h, wfill, hfill) = determineWidgetFrame(widget, cx, cy, rw, rh, mx, my,
																  topmost, leftmost, config, bounded)
			}
		  }

		  if (setWidgetFrame) { 
			if (widget.nodeType != 'Image' || bounded) {  
			  widget._frame = [cx, cy, w, h, wfill, hfill]
			  info "[Layout] widget ${widget.nodeType} ${widget.id} layout: ${widget._frame}" 
			}
		  }

		  vw = max(vw, cx + w)
		  vh = max(vh, cy + h)
	  
		  if (horizontal) { 
			if (w > 0) { 
			  cx += (w + config.defaults.gap)
			  rw -= (cx - x)
			  leftmost = false
			}
		  } else { 
			if (h > 0) { 
			  cy += (h + config.defaults.gap)
			  rh -= (cy - y)
			}
		  }
		}
		topmost = false
      }
    }
    return [vw - x, vh - y]
  }

  static layoutTable(View view, 
					 int x, int y,     // current position
					 int rw, int rh,   // remaining width and height
					 int mx, int my,   // margin x and y 
					 def config,
					 boolean bounded = true, 
					 boolean setWidgetFrame = true) { 
    info "[LayoutTable] id=${view.id}"
    int vw = 0, vh = 0;
    if (view && view.children) { 
      //boolean horizontal = false // alwaws vertical 

      def colWidth = [:]  // the maximum width of each column 
      def colStretch = [:] // whether each column is stretchable 
      int cx = x, cy = y;
      int w = 0, h = 0;
      boolean wfill = false, hfill = false 
      boolean topmost = true, leftmost = true
      // first pass 
      view.children.each { widget -> 
		info "[LayoutTable] row ${view.id} child ${widget.id} isUI=${Language.isUI(widget.nodeType)}"

		if (Language.isUI(widget.nodeType) &&
			!Language.isPopup(widget.nodeType)) { 
		  if (Language.isGroup(widget.nodeType)) {  
			int rw0 = rw - (cx - x)
			int rh0 = rh - (cy - y)
			(w, h) = layout(widget, cx, cy, rw0, rh0, mx, my, config, bounded, setWidgetFrame) // true 

			if (widget.nodeType == 'Row') { 
			  widget.children.eachWithIndex { w1, i ->
				colStretch[i] = w1._frame[4]  // wfill
				int width = w1._frame[2]
				int cwidth = colWidth[i] ?: 0
				colWidth[i] = max(width, cwidth)
			  }
			}
		  } else { 
			(cx, cy, w, h, wfill, hfill) = determineWidgetFrame(widget, cx, cy, rw, rh, mx, my,
																topmost, leftmost, config, bounded)
		  }

		  vw = max(vw, cx + w)
		  vh = max(vh, cy + h)
	  
		  // vertical 
		  cy += (h + config.defaults.gap)
		  rh -= (cy - y)
		}
		topmost = false
      }

      info "[LayoutTable] colWidth: ${colWidth}  colStretch: ${colStretch}"
      view._colWidth = colWidth
      view._colStretch = colStretch

      // second pass 
      cx = x; cy = y;
      w = 0; h = 0;
      wfill = false; hfill = false; 
      topmost = true; leftmost = true;
      view.children.each { widget -> 
		info "[LayoutTable] row ${view.id} child ${widget.id} isUI=${Language.isUI(widget.nodeType)}"

		if (Language.isUI(widget.nodeType) &&
			!Language.isPopup(widget.nodeType)) { 
		  if (Language.isGroup(widget.nodeType)) {  
			int rw0 = rw - (cx - x)
			int rh0 = rh - (cy - y)
			if (widget.nodeType == 'Row') { 
			  (w, h) = layoutTableRow(widget, cx, cy, rw0, rh0, mx, my, 
									  colWidth, colStretch,
									  config, bounded, setWidgetFrame)
			} else { 
			  (w, h) = layout(widget, cx, cy, rw0, rh0, mx, my, config, setWidgetFrame)
			}
		  } else { 
			(cx, cy, w, h, wfill, hfill) = determineWidgetFrame(widget, cx, cy, rw, rh, mx, my,
																topmost, leftmost, config, bounded)
		  }

		  if (setWidgetFrame) { 
			if (widget.nodeType != 'Image' || bounded) {  
			  widget._frame = [cx, cy, w, h, wfill, hfill]
			  info "[LayoutTable] widget ${widget.nodeType} ${widget.id} layout: ${widget._frame}" 
			}
		  }

		  vw = max(vw, cx + w)
		  vh = max(vh, cy + h)
	  
		  // vertical 
		  cy += (h + config.defaults.gap)
		  rh -= (cy - y)
		}
		topmost = false
      }

    }
    return [vw - x, vh - y]
  }

  // return [width, heght]
  static layoutTableRow(View view, 
						int x, int y,     // current position
						int rw, int rh,   // remaining width and height
						int mx, int my,   // margin x and y 
						def colWidth,     // col width, Map: i -> int
						def colStretch,   // col stretchable, Map: i -> boolean
						def config,
						boolean bounded = true, 
						boolean setWidgetFrame = true) { 
    info "[LayoutTableRow] id=${view.id}"

    int vw = 0, vh = 0;
    if (view && view.children) { 
      // boolean horizontal = true 

      int cx = x, cy = y
      int w = 0, h = 0
      boolean wfill = false, hfill = false 
      boolean topmost = true, leftmost = true
      view.children.eachWithIndex { widget, i -> 
		info "[LayoutTableRow] ${view.id} child ${widget.id} isUI=${Language.isUI(widget.nodeType)}"

		if (Language.isUI(widget.nodeType) &&
			!Language.isPopup(widget.nodeType)) { 
		  if (Language.isGroup(widget.nodeType)) {  
			int rw0 = rw - (cx - x)
			int rh0 = rh - (cy - y)
			(w, h) = layout(widget, cx, cy, rw0, rh0, mx, my, config, bounded, setWidgetFrame)	    
		  } else { 
			(cx, cy, w, h, wfill, hfill) = determineWidgetFrame(widget, cx, cy, rw, rh, mx, my,
																topmost, leftmost, config, bounded)
		  }
		  if (w < colWidth[i]) { // handle column width 
			w = colWidth[i]
			if (w > rw)  
			  w = rw
		  }

		  if (setWidgetFrame) { 
			if (widget.nodeType != 'Image' ||  
				rw > 0 || rh > 0) { 
			  widget._frame = [cx, cy, w, h, wfill, hfill]
			  info "[LayoutTableRow] widget ${widget.nodeType} ${widget.id} layout: ${widget._frame}" 
			}
		  }

		  vw = max(vw, cx + w)
		  vh = max(vh, cy + h)
	  
		  // horizontal 
		  cx += (w + config.defaults.gap)
		  rw -= (cx - x)
		  leftmost = false

		}
		topmost = false
      }
    }
    return [vw - x, vh - y]
  }

  // return [x, y, w, h, wfill, hfill]
  static determineWidgetFrame(Widget widget,
							  int curX, int curY,           // current top-right position   
							  int maxWidth, int maxHeight,  // max width and height of the container 
							  int marginX, int marginY,     // margin x and y 
							  boolean topmost,
							  boolean leftmost,
							  def config,
							  boolean bounded = true) { 
	info "[Layout] determineWidgetFrame(): widget=[${widget.widgetType}, ${widget.id}] cur=(${curX},${curY}) max=(${maxWidth},${maxHeight})"

    int w = 0
    int h = 0
    boolean wfill = false, hfill = false  // stretchable width | height
    if (widget.size) { 
      (w, h) = widget.size 
    } else { 
      if (widget.width) { 
		if (widget.width == '*') { 
		  w = maxWidth
		  wfill = true
		} else {  
		  w = widget.width
		}
      }
      if (widget.height) { 
		if (widget.height == '*') { 
		  h = maxHeight
		  hfill = true
		} else {  
		  h = widget.height
		}
      }
    }

    boolean useTextWidth = config.defaults[widget.nodeType].useTextWidth 
    int widthOffset = config.defaults[widget.nodeType].widthOffset ?: 0
    if (useTextWidth && (widget.text != null || widget.image == null)) { 
	  int numLines = widget.lines ?: linesInString(widget.text as String)
      h = config.defaults[widget.nodeType].height
	  String fname = config.defaults[widget.nodeType].font.name
	  int fsize = config.defaults[widget.nodeType].font.size
	  String fstyle = config.defaults[widget.nodeType].font.style
	  def font = widget.font
	  if (font == null) font = widget.titleFont 
	  if (font instanceof Font) { 
		if (font.family) fname = font.family
		if (font.size > 0) fsize = font.size
		if (font.bold) { 
		  fstyle = 'bold'
		} else if (font.italic) { 
		  fstyle = 'italic'
		}
		h = max(h, getFontHeight(fname, fsize) * numLines + 6)
	  } else { 
		if (numLines > 1) { 
		  h = max(h, getFontHeight(fname, fsize) * numLines + 6)
		}
	  }
      if (w <= 0) { 
		if (widget.text) { 
		  w = getStringWidth(widget.text, fname, fsize, fstyle) + 20 + widthOffset
		} else { 
		  w = maxWidth
		  wfill = true
		}
	  }

    } 

	if (ViewProcessor.allowImage(widget)) { 
	  if (!bounded) { 
		def (iw, ih) = ViewProcessor.getImageSizeForWidget(widget)
		if (w <= 0) w = iw
		if (h <= 0) h = ih
      }
    } 

    if (h <= 0) { 
      def v = config.defaults[widget.nodeType].height
      info "[Layout] config.defaults[${widget.nodeType}].height: ${v}"
      if (v instanceof Integer) { 
		if (v < 0) { 
		  h = maxHeight
		  hfill = true
		} else {  
		  h = v
		}
      } else { 
		if (v == '*') { 
		  h = maxHeight
		  hfill = true
		} else if (v == '=') { 
		  if (topmost) { 
			curY -= marginY
			h = maxHeight + 2 * marginY
		  } else { 
			h = maxHeight + marginY
		  }
		  hfill = true
		}
      }
    }
    if (w <= 0) { 
      def v = config.defaults[widget.nodeType].width
      info "[Layout] config.defaults[${widget.nodeType}].width: ${v}"
      if (v instanceof Integer) { 
		if (v < 0) { 
		  w = maxWidth
		  wfill = true
		} else {  
		  w = v
		}
      } else { 
		if (v == '*' ||
			v instanceof Integer && v < 0) { 
		  w = maxWidth
		  wfill = true
		} else if (v == '=') { 
		  if (leftmost) { 
			curX -= marginX
			w = maxWidth + 2 * marginX
		  } else { 
			w = maxWidth + marginX
		  }
		  wfill = true
		}
      }      
    }

    return [curX, curY, w, h, wfill, hfill]
  }


  static int getStringWidth(String text, String fname, int size, String style = null) { 
	(int) xj.mobile.util.FontMetrics.getStringWidth(text, fname, size, style)
  }

  static int getFontHeight(String fname, int size) { 
	(int) xj.mobile.util.FontMetrics.getFontHeight(fname, size)
  }

}