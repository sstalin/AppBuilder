package xj.mobile.android

import xj.mobile.*
import xj.mobile.model.ui.*
import xj.mobile.model.graphics.*
import xj.mobile.model.impl.AndroidViewClass
import xj.mobile.lang.*
import xj.mobile.model.properties.Color
import xj.mobile.common.ViewProcessor
import xj.mobile.common.ViewHierarchyProcessor

import static xj.mobile.android.AndroidAppGenerator.*
import static xj.mobile.util.CommonUtil.*
import static xj.mobile.lang.Language.*
import static xj.mobile.common.ViewProcessor.*
import static xj.mobile.model.impl.ClassModel.getCustomViewName
import static xj.mobile.codegen.CodeGenerator.InjectionPoint.*

class CanvasProcessor { 

  def drawCode = []
  //def initCode = []

  ViewProcessor vp

  void process(Widget widget, ViewProcessor vp) { 
	this.vp = vp
	if (widget && vp) { 
	  String className = getCustomViewName(widget.id)
	  AndroidViewClass viewClass = new AndroidViewClass(className) 
	  vp.project.classes << viewClass

	  drawCode = []		
	  //initCode = []
	  widget.children.eachWithIndex { s, i -> 
		if (s instanceof Shape) { 
		  String code = generateDrawCode(s)
		  if (code) {
			if (i > 0) { 
			  code = 'paint.reset();\n' + code 
			}
			drawCode << code
		  } 
		}
	  }

	  if (drawCode) { 
		viewClass.setViewBackground(widget.background)

		def preCode = []
		preCode << 'Paint paint = new Paint();'
		if (widget.antialias) { 
		  preCode <<'paint.setAntiAlias(true);'
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
		  path = "canvas.drawPoint(${s.position[0]}, ${s.position[1]}, paint);"
		break
	  case 'Line': 
		if (s.start && s.end)
		  path = "canvas.drawLine(${s.start[0]}, ${s.start[1]}, ${s.end[0]}, ${s.end[1]}, paint);"
		break
	  case 'Curve': 
		if (s.start && s.end && s.control)
		  path = """final Path ${s.id} = new Path();
${s.id}.moveTo(${s.start[0]}, ${s.start[1]});
${s.id}.cubicTo(${s.control[0][0]}, ${s.control[0][1]}, ${s.control[1][0]}, ${s.control[1][1]}, ${s.end[0]}, ${s.end[1]});
canvas.drawPath(${s.id}, paint);"""
		break
	  case 'Rect': 
		if (s.frame)
		  path = "canvas.drawRect(${s.frame[0]}f, ${s.frame[1]}f, ${s.frame[0] + s.frame[2]}f, ${s.frame[1] + s.frame[3]}f, paint);"
		break
	  case 'Circle':
		if (s.center && s.radius) 
		  path = "canvas.drawCircle(${s.center[0]}, ${s.center[1]}, ${s.radius}, paint);"
		break
	  case 'Ellipse':
		if (s.frame) 
		  path = "canvas.drawOval(new RectF(${s.frame[0]}f, ${s.frame[1]}f, ${s.frame[0] + s.frame[2]}f, ${s.frame[1] + s.frame[3]}f), paint);"
 		break
	  case 'Arc':
		if (s.center && s.radius && s.startAngle != null && s.endAngle != null) { 
		  float x = s.center[0] - s.radius
		  float y = s.center[1] - s.radius
		  float d = s.radius * 2
		  float sweepAngle = (s.clockwise ? 1 : -1) * (s.endAngle - s.startAngle)
		  path = "canvas.drawArc(new RectF(${x}f, ${y}f, ${x+d}f, ${y+d}f), ${s.startAngle}f, ${sweepAngle}f, false, paint);"
		} else if (s.start && s.end && s.control) { 
		  path = """final Path ${s.id} = new Path();
${s.id}.moveTo(${s.start[0]}, ${s.start[1]});
${s.id}.quadTo(${s.control[0]}, ${s.control[1]}, ${s.end[0]}, ${s.end[1]});
canvas.drawPath(${s.id}, paint);"""
		}
		  /*
		} else if (s.start && s.end && s.radius && s.control) { 
		  path = """CGContextMoveToPoint(context, ${s.start[0]}, ${s.start[1]});
CGContextAddArcToPoint(context, ${s.control[0]}, ${s.control[1]}, ${s.end[0]}, ${s.end[1]}, ${s.radius});"""
		}
		*/
		break
	  case 'Path':
		path = generatePathCode(s)
		break 
	  case 'ImageShape':
		if (s.frame && s.file) 

		  path = """Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.${getFileName(s.file)});
canvas.drawBitmap(myBitmap, null, new RectF(${s.frame[0]}f, ${s.frame[1]}f, ${s.frame[0] + s.frame[2]}f, ${s.frame[1] + s.frame[3]}f), paint);"""
		vp.classModel.addImageFile(s.file)		
		break 
	  case 'TextShape':
		if (s.position && s.font && s.text) { 
		  String setFont = "paint.setTypeface(${s.font.toAndroidTypeface()});"
		  if (s.font.size > 1) { 
			setFont += "\npaint.setTextSize(${s.font.size});"
			//viewClass.addImport('android.util.TypedValue')
		  }
		  path = setFont + "\ncanvas.drawText(\"${s.text}\", ${s.position[0]}, ${s.position[1]}, paint);"
		}
		break 
	  }

	  if (path) { 
		def code = [] 

		boolean paintStroke = true
		boolean paintFill = false
		if (s.fillColor) { 
		  paintFill = true
		  if (!s.color) 
			paintStroke = false
		}
	
		if (paintFill) { 
		  def fillColor = s.fillColor
		  if (isTextShape(s.nodeType) && s.color && !s.fillColor) { 
			fillColor = s.color
		  }
		  if (fillColor instanceof Color) {
			def rgb = fillColor.getRGB()
			code << "paint.setColor(Color.rgb(${rgb[0]}, ${rgb[1]}, ${rgb[2]}));"
		  }
		  code << 'paint.setStrokeWidth(0);'
		  code << 'paint.setStyle(Paint.Style.FILL);'
		  code << path 
		}

		if (paintStroke) { 
		  if (s.color instanceof Color) { 
			def rgb = s.color.getRGB()
			code << "paint.setColor(Color.rgb(${rgb[0]}, ${rgb[1]}, ${rgb[2]}));"
		  }
		  if (s.width) { 
			code << "paint.setStrokeWidth(${s.width});"
		  }
		  String join = getLineJoin(s.join)
		  if (join) { 
			code << "paint.setStrokeJoin(${join});"
			
		  }
		  String cap = getLineCap(s.cap)
		  if (cap) { 
			code << "paint.setStrokeCap(${cap});"
		  }
		  code << 'paint.setStyle(Paint.Style.STROKE);'
		  code << path
		}

		/*
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
		*/

		
		/*
		if (!isTextShape(s.nodeType) && !isImageShape(s.nodeType))
		  code << "CGContextDrawPath(context, ${drawMode});"
		*/
		return code.join('\n') + '\n'
	  }
	}
	return null 
  }

  String generatePathCode(Path path) { 
	if (path) { 
	  def code = []
	  code << "final Path ${path.id} = new Path();"
	  if (path.start) { 
		code << "${path.id}.moveTo(${path.start[0]}, ${path.start[1]});"
	  }
	  path.children.each { s -> 
		if (s instanceof PathElement) { 
		  switch (s.nodeType) { 
		  case 'moveTo':
			if (s.location)
			  code << "${path.id}.moveTo(${s.location[0]}, ${s.location[1]});"
			break
		  case 'lineTo':
			if (s.location)
			  code << "${path.id}.lineTo(${s.location[0]}, ${s.location[1]});"
			break
		  case 'curveTo':
			if (s.location && s.control)
			  code << "${path.id}.cubicTo(${s.control[0][0]}, ${s.control[0][1]}, ${s.control[1][0]}, ${s.control[1][1]}, ${s.location[0]}, ${s.location[1]});"
			break
		  case 'arcTo':
			if (s.center && s.radius && s.startAngle != null && s.endAngle != null) { 
			  float x = s.center[0] - s.radius
			  float y = s.center[1] - s.radius
			  float d = s.radius * 2
			  float sweepAngle = (s.clockwise ? 1 : -1) * (s.endAngle - s.startAngle)
			  code << "${path.id}.arcTo(new RectF(${x}f, ${y}f, ${x+d}f, ${y+d}f), ${s.startAngle}f, ${sweepAngle}f);"
			} else if (s.location && s.control) { 
			  code << "${path.id}.quadTo(${s.control[0]}, ${s.control[1]}, ${s.location[0]}, ${s.location[1]});"
			}
			/*
			} else if (s.location && s.radius && s.control) { 
			  code << "CGContextAddArcToPoint(context, ${s.control[0]}, ${s.control[1]}, ${s.location[0]}, ${s.location[1]}, ${s.radius});"
			}
			*/
			break
		  }
		}
	  }
	  if (code) { 		
		if (path.closed) { 
		  code << "${path.id}.close();"
		}
		code << "canvas.drawPath(${path.id}, paint);"
		return code.join('\n')
	  }
	}
	return null
  }


  static JOIN = [ 'Miter', 'Round', 'Bevel' ]

  static String getLineJoin(s) { 
	if (s) { 
	  s = s.capitalize() 
	  if (s in JOIN) { 
		return 'Paint.Join.' + s.toUpperCase()
	  }
	}
	return null
  }

  static CAP = [ 'Butt', 'Round', 'Square' ]

  static String getLineCap(s) { 
	if (s) { 
	  s = s.capitalize() 
	  if (s in CAP) { 
		return 'Paint.Cap.' + s.toUpperCase()
	  }
	}
	return null
  }

}