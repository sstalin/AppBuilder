package xj.mobile.ios

import xj.mobile.*
import xj.mobile.common.*
import xj.mobile.model.ui.*
import xj.mobile.model.graphics.*
import xj.mobile.model.impl.IOSViewClass
import xj.mobile.model.properties.Color
import xj.mobile.lang.*

import static xj.mobile.ios.IOSAppGenerator.*
import static xj.mobile.util.CommonUtil.*
import static xj.mobile.lang.Language.*
import static xj.mobile.model.impl.ClassModel.getCustomViewName
import static xj.mobile.codegen.CodeGenerator.InjectionPoint.*

import static xj.mobile.model.impl.ViewControllerClass.getViewControllerName

class CanvasProcessor {  

  def drawCode = []
  def initCode = []

  boolean defaultColor = true
  boolean defaultWidth = true
  boolean defaultJoin = true
  boolean defaultCap = true

  ViewProcessor vp

  void process(Widget widget, ViewProcessor vp) { 
	this.vp = vp
	if (widget && vp) {
	  String className = getCustomViewName(widget.id)
	  IOSViewClass viewClass = new IOSViewClass(className) 
	  vp.project.classes << viewClass
	  vp.classModel.injectCode(Import, className)
	  
	  defaultColor = true
	  defaultWidth = true
	  defaultJoin = true
	  defaultCap = true
	  drawCode = []		
	  initCode = []
	  widget.children.each { s -> 
		if (s instanceof Shape) { 
		  String code = generateDrawCode(s)
		  if (code)
			drawCode << code 
		}
	  }

	  if (drawCode) { 
		viewClass.setViewBackground(widget.background)

		def preCode = []
		preCode << 'CGContextRef context = UIGraphicsGetCurrentContext();'
		if (widget.antialias) { 
		  preCode <<'CGContextSetShouldAntialias(context, YES);'
		}
		viewClass.injectCode(Draw, (preCode + drawCode).join('\n'))
	  }
	}
  }

  String generateDrawCode(Shape s) { 
	if (s) { 
	  String path = ''
	  switch (s.nodeType) { 
	  case 'Point':
		if (s.position)
		  path = """CGContextMoveToPoint(context, ${s.position[0]}, ${s.position[1]});
CGContextAddLineToPoint(context, ${s.position[0]}, ${s.position[1]});"""
		break
	  case 'Line': 
		if (s.start && s.end)
		  path = """CGContextMoveToPoint(context, ${s.start[0]}, ${s.start[1]});
CGContextAddLineToPoint(context, ${s.end[0]}, ${s.end[1]});"""
		break
	  case 'Curve': 
		if (s.start && s.end && s.control)
		  path = """CGContextMoveToPoint(context, ${s.start[0]}, ${s.start[1]});
CGContextAddCurveToPoint(context, ${s.control[0][0]}, ${s.control[0][1]}, ${s.control[1][0]}, ${s.control[1][1]}, ${s.end[0]}, ${s.end[1]});"""
		break
	  case 'Rect': 
		if (s.frame)
		  path = "CGContextAddRect(context, CGRectMake(${s.frame[0]}, ${s.frame[1]}, ${s.frame[2]}, ${s.frame[3]}));"
		break
	  case 'Ellipse':
	  case 'Circle':
		if (s.frame) 
		  path = "CGContextAddEllipseInRect(context, CGRectMake(${s.frame[0]}, ${s.frame[1]}, ${s.frame[2]}, ${s.frame[3]}));"
 		break
	  case 'Arc':
		if (s.center && s.radius && s.startAngle != null && s.endAngle != null) { 
		  path = "CGContextAddArc(context, ${s.center[0]}, ${s.center[1]}, ${s.radius}, ${(float)s.startAngle}, ${(float)s.endAngle}, ${s.clockwise ? 1 : 0});"
		} else if (s.start && s.end && s.radius && s.control) { 
		  path = """CGContextMoveToPoint(context, ${s.start[0]}, ${s.start[1]});
CGContextAddArcToPoint(context, ${s.control[0]}, ${s.control[1]}, ${s.end[0]}, ${s.end[1]}, ${s.radius});"""
		}
		break
	  case 'Path':
		path = generatePathCode(s)
		break 
	  case 'ImageShape':
		if (s.frame && s.file) 
		  path = """UIImage *myImage = [UIImage imageNamed:@\"${s.file}\"];
[myImage drawInRect:CGRectMake(${s.frame[0]}, ${s.frame[1]}, ${s.frame[2]}, ${s.frame[3]})];"""
		vp.classModel.addImageFile(s.file)
		break 
	  case 'TextShape':
		if (s.position && s.font && s.text) 
		  path = "[@\"${s.text}\" drawAtPoint:CGPointMake(${s.position[0]}, ${s.position[1]}) withFont:${s.font.toIOSString()}];"
		break 
	  }

	  if (path) { 
		String drawMode = 'kCGPathStroke'
		if (s.fillColor && s.color) 
		  drawMode = 'kCGPathFillStroke'
		else if (s.fillColor && !s.color) 
	      drawMode = 'kCGPathFill'

		def code = [] 
		if (s.color instanceof Color) { 
		  def rgba = s.color.toIOSRGBA()
		  code << "CGContextSetRGBStrokeColor(context, ${rgba[0]}, ${rgba[1]}, ${rgba[2]}, ${rgba[3]});"
		  defaultColor = (rgba[0] == 0.0 && rgba[1] == 0.0 && rgba[2] == 0.0 && rgba[3] == 1.0 )
		} else if (!defaultColor) { 
		  code << 'CGContextSetRGBStrokeColor(context, 0.0, 0.0, 0.0, 1.0);'
		  defaultColor = true
		}
		def fillColor = s.fillColor
		if (isTextShape(s.nodeType) && s.color && !s.fillColor) { 
		  fillColor = s.color
		}
		if (fillColor instanceof Color) { 
		  def rgba = fillColor.toIOSRGBA()
		  code << "CGContextSetRGBFillColor(context, ${rgba[0]}, ${rgba[1]}, ${rgba[2]}, ${rgba[3]});"
		}
		if (s.width) { 
		  code << "CGContextSetLineWidth(context, ${s.width});"
		  defaultWidth = (s.width as float == 1)
		} else if (!defaultWidth) { 
		  code << 'CGContextSetLineWidth(context, 1);'
		  defaultWidth = true
		}

		String join = getLineJoin(s.join)
		if (join) { 
		  code << "CGContextSetLineJoin(context, ${join});"
		  defaultJoin = (join == 'kCGLineJoinMiter') 
		} else if (!defaultJoin) { 
		  code << 'CGContextSetLineJoin(context, kCGLineJoinMiter);'
		  defaultJoin = true 
		}

		String cap = getLineCap(s.cap)
		if (cap) { 
		  code << "CGContextSetLineCap(context, ${cap});"
		  defaultCap = (cap == 'kCGLineCapButt') 
		} else if (!defaultCap) { 
		  code << 'CGContextSetLineCap(context, kCGLineCapButt);'
		  defaultCap = true
		}

		code << path
		if (!isTextShape(s.nodeType) && !isImageShape(s.nodeType))
		  code << "CGContextDrawPath(context, ${drawMode});"

		return code.join('\n') + '\n'
	  }
	}
	return null 
  }

  String generatePathCode(Path path) { 
	if (path) { 
	  def code = []
	  if (path.start) { 
		code << "CGContextMoveToPoint(context, ${path.start[0]}, ${path.start[1]});"
	  }
	  path.children.each { s -> 
		if (s instanceof PathElement) { 
		  switch (s.nodeType) { 
		  case 'moveTo':
			if (s.location)
			  code << "CGContextMoveToPoint(context, ${s.location[0]}, ${s.location[1]});"
			break
		  case 'lineTo':
			if (s.location)
			  code << "CGContextAddLineToPoint(context, ${s.location[0]}, ${s.location[1]});"
			break
		  case 'curveTo':
			if (s.location && s.control)
			  code << "CGContextAddCurveToPoint(context, ${s.control[0][0]}, ${s.control[0][1]}, ${s.control[1][0]}, ${s.control[1][1]}, ${s.location[0]}, ${s.location[1]});"
			break
		  case 'arcTo':
			if (s.center && s.radius && s.startAngle != null && s.endAngle != null) { 
			  code << "CGContextAddArc(context, ${s.center[0]}, ${s.center[1]}, ${s.radius}, ${(float)s.startAngle}, ${(float)s.endAngle}, ${s.clockwise ? 1 : 0});"
			} else if (s.location && s.radius && s.control) { 
			  code << "CGContextAddArcToPoint(context, ${s.control[0]}, ${s.control[1]}, ${s.location[0]}, ${s.location[1]}, ${s.radius});"
			}
			break
		  }
		}
	  }
	  if (code) { 		
		if (path.closed) { 
		  code << 'CGContextClosePath(context);'
		}
		return 'CGContextBeginPath(context);\n' + code.join('\n')
	  }
	}
	return null
  }

  static JOIN = [ 'Miter', 'Round', 'Bevel' ]

  static String getLineJoin(s) { 
	if (s) { 
	  s = s.capitalize() 
	  if (s in JOIN) { 
		return 'kCGLineJoin' + s
	  }
	}
	return null
  }

  static CAP = [ 'Butt', 'Round', 'Square' ]

  static String getLineCap(s) { 
	if (s) { 
	  s = s.capitalize() 
	  if (s in CAP) { 
		return 'kCGLineCap' + s
	  }
	}
	return null
  }

}
