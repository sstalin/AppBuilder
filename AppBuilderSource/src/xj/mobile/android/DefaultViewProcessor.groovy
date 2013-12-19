package xj.mobile.android

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.control.*
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.*
import org.codehaus.groovy.ast.builder.AstBuilder

import xj.mobile.*
import xj.mobile.common.*
import xj.mobile.codegen.*
import xj.mobile.model.*
import xj.mobile.model.impl.*
import xj.mobile.model.ui.*
import xj.mobile.lang.*
import xj.mobile.util.PrettyMarkupBuilder
import xj.mobile.codegen.CodeGenerator
import xj.mobile.codegen.CodeGenerator.InjectionPoint
import xj.mobile.model.properties.ModalTransitionStyle
import xj.mobile.model.properties.Property

import static xj.mobile.codegen.CodeGenerator.InjectionPoint.*

import static xj.mobile.android.AndroidAppGenerator.*
import static xj.mobile.common.ViewProcessor.*
import static xj.mobile.util.CommonUtil.*
import static xj.translate.Logger.info 

// process a single top view 
class DefaultViewProcessor extends ViewProcessor { 

  static XMLNS_ANDROID = 'http://schemas.android.com/apk/res/android'

  // Android specific, deal with NavigationView
  // when the top is NavigationView, the main view is the first child 
  static boolean isAndroidMainView(ViewProcessor vp) { 
    if (vp.view.type == 'NavigationView') { 
      return false
    } else if (vp.view.parent?.widgetType != 'NavigationView') { 
      return vp.isMainView()
    } else { 
      return vp.view.parent.viewProcessor.topViews[0] == vp.view.id
    }
  }

  // no code generated for NavigationView
  static boolean needGenerateCode(ViewProcessor vp) { 
    return vp.view.widgetType != 'NavigationView'
  }

  MenuProcessor menuProcessor

  boolean hasCustomWidgets = false 

  AndroidClass classModel

  public DefaultViewProcessor(View view, String viewName = null) { 
    super(view, viewName)

	classModel = new ActivityClass(viewName)
	classModel.needGenerateCode = needGenerateCode(this)
	classModel.isMainView = isAndroidMainView(this) 
	//classModel.name = viewName
	classModel.imports << 'android.content.Intent' 

	generator = CodeGenerator.getCodeGenerator('android')

    widgetProcessor = new WidgetProcessor(this)
    popupProcessor = new PopupProcessor(this)
    menuProcessor = new MenuProcessor(this)

    config = androidConfig

  }

  void init(AppInfo appInfo) { 
	if (isAndroidMainView(this)) { 
	  viewName = appInfo.appid 
	  classModel.name = viewName
	}
	classModel.packageName = appInfo.packageName
  }

  public String getPlatform() { 
	'Android'
  }

  String getPropertyValueString(Property val) { 
	val.toAndroidJavaString()
  }

  protected static Orientations = [
	Portrait: 'portrait', 
	PortraitUpsideDown: 'reversePortrait', 
	LandscapeLeft: 'landscape', 
	LandscapeRight: 'reverseLandscape',
  ]

  protected void processRootView() {
	info '[Android.DefaultViewProcessor] processRootView()'
	if (view.initialOrientation) { 
	  String orientation = Orientations[view.initialOrientation.name]
	  if (!orientation) orientation = 'unspecified'	  
	  classModel.orientation = orientation
	} else if (view.supportedOrientations) { 
	  def orientations = view.supportedOrientations.collect { Orientations[it.name] }
	  if (!('portrait' in orientations) ||
		  !('landscape' in orientations) && !('reverseLandscape' in orientations)) { 
		classModel.orientation = orientations[0]
	  }
	}
  } 

  protected void handleLocalVariableDeclaration(decl) { 
	def declmap = generator.unparser.unparseDeclarationExpression(decl)
	String code = "${declmap.type} ${declmap.name}"
	if (declmap.init) code += " = ${declmap.init}"
	classModel.injectCode(Declaration, code + ';')
  }

  protected void handleSpecialLocalDeclarations() { 
	def uset = view['#info'].useSet
	if (uset && 'data' in uset) { 
	  String code = 'String data;'
	  classModel.injectCode(Declaration, code)

	  classModel.onCreateScrap += "data = getIntent().getExtras().getString(\"${view.id.toUpperCase()}_DATA\");\n"
	}
  }

  protected void preprocessWidget(Widget widget) { 
	if (widget) { 
	  def wtemp = widgetProcessor.getWidgetTemplate(widget)
	  if (wtemp?.custom) { 
		hasCustomWidgets = true
	  }
	}
  }

  protected void initializeTopView() { 
	if (view.title) { 
	  classModel.title = view.title
	}
  }

  protected void processSubviews(View view) { 
	generateViewLayout()
  }

  void generateViewLayout() { 
    def layoutFilename = isAndroidMainView(this) ? 'main' : viewName.toLowerCase()
    classModel.onCreateScrap += "setContentView(R.layout.${layoutFilename});"
    classModel.resources.viewLayouts[layoutFilename] = generateLayoutXML() 
  }

  String generateLayoutXML() { 
    def writer = new StringWriter()
    writer.println "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
    def mainLayout = new PrettyMarkupBuilder(writer)    
    mainLayout.setDoubleQuotes(true)

    def viewAttrs = [ 'xmlns:android': XMLNS_ANDROID ]
	if (hasCustomWidgets) { 
	  viewAttrs['xmlns:custom'] = "http://schemas.android.com/apk/res/${classModel.packageName}"
	}
	viewAttrs.putAll([ 'android:layout_width': 'match_parent',
					   'android:layout_height': 'match_parent', 
					   'android:padding': "${config.defaults.margin}dp",
					   'android:orientation': 'vertical' ])
    if (view.background &&
		!(view.background instanceof xj.mobile.model.properties.Color)) { 
      String imgFile = view.background
      classModel.addImageFile(imgFile)

      int i = imgFile.lastIndexOf('.')
      if (i > 0) { 
		imgFile = imgFile[0 .. i-1]
      }
      def writer2 = new StringWriter()
      writer2.println "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
      def drawable = new PrettyMarkupBuilder(writer2)    
      def bitmapAttrs = [ 'xmlns:android': XMLNS_ANDROID,
						  'android:src': "@drawable/${imgFile}", 
						  'android:tileMode': 'repeat' ]
      drawable.bitmap(bitmapAttrs)
      classModel.resources.drawableResources["${imgFile}repeat"] = writer2.toString() 

      viewAttrs['android:background'] = "@drawable/${imgFile}repeat"
    } else if (view.background) { 
      viewAttrs['android:background'] = view.background.toAndroidXMLString()    
    }

	//boolean needScroll = (view.scroll || view.children.any { w -> (w instanceof Widget) && w.scroll })

    // top level layout 
	if (view.scroll) { 
	  viewAttrs.remove('android:padding')
	  mainLayout.ScrollView(viewAttrs) { 
		viewAttrs.remove('xmlns:android')
		viewAttrs['android:padding'] = "${config.defaults.margin}dp"
		LinearLayout(viewAttrs) { 
		  view.children.each { widget -> 
			if (widget instanceof Widget) { 
			  handleWidget(widget, mainLayout)
			}
		  }
		}
      }
	} else { 
	  mainLayout.LinearLayout(viewAttrs) { 
		view.children.each { widget -> 
		  if (widget instanceof Widget) { 
			handleWidget(widget, mainLayout)
		  }
		}
      }
    }
    
    def text = writer.toString()
    info '[Android.DefaultViewProcessor] layout:\n' + text
    return text
  }

  void handleWidget(Widget widget, mainLayout) { 
    if (Language.isGroup(widget.nodeType)) {  
      def layoutName = 'LinearLayout'
      boolean horizontal = (widget.nodeType in [ 'Box', 'Row' ])
      if (widget.orientation) { 
		horizontal = ('horizontal' == widget.orientation)
      }
      // ('horizontal' == widget.orientation)

      def attributes = [:]
      if (widget.id) { 
		attributes['android:id'] = "@+id/${widget.id}"
      }

      attributes['android:layout_height'] = 'wrap_content'
      attributes['android:layout_width'] = 'match_parent'

      if (widget.widgetType == 'RadioGroup') { 
		layoutName = 'RadioGroup'
		handleRadioGroup(widget)
      } else if (widget.widgetType == 'Table') { 
		layoutName = 'TableLayout'
		def colStretch = []
		widget._colStretch.each{ i, st -> if (st) colStretch << i }
		if (colStretch)
		  attributes['android:stretchColumns'] = colStretch.join(',')
      } else if (widget.widgetType == 'Row') { 
		layoutName = 'TableRow'
      } 

      if (layoutName in ['RadioGroup', 'LinearLayout']) { 
		// LinearLayout
		if (!horizontal) { 
		  attributes['android:orientation'] = 'vertical'
		}
      }

      mainLayout."${layoutName}"(attributes) { 
		widget.children.each { w -> 
		  handleWidget(w, mainLayout)
		}
      }

      if (widget.widgetType == 'SpinnerGroup') { 
		handleSpinnerGroup(widget)
      }
	} else if (Language.isTopView(widget.nodeType) && widget.embedded) {  
	  widgetProcessor.process(widget, mainLayout)
    } else if (Language.isPopup(widget.nodeType)) {  
      popupProcessor.process(widget)
    } else if (Language.isWidget(widget.nodeType)) {  
      widgetProcessor.process(widget, mainLayout)
    }
  }

  void handleRadioGroup(View rgroup) { 
    if (rgroup?.selection) { 
      widgetProcessor.handleAction(rgroup, null, null)

      classModel.methodScrap += "\npublic RadioButton selectedRadioButton_${rgroup.id}() {\n\t" + 
      rgroup.children?.collect { w ->
		(w.widgetType == 'RadioButton') ? "if (${w.id}.isChecked()) return ${w.id};\n\t" : ''
      }.join('') + "return null;\n}"

      def actionName = "${getActionName(rgroup)}_${rgroup.id}"
      rgroup.children?.each { w -> 
		if (w.widgetType == 'RadioButton') { 
		  w.actionName = actionName
		}
      }
    }
  }

  void handleSpinnerGroup(View sgroup) { 
    if (sgroup?.selection) { 
      String actionCode = null
      if (sgroup['selection'] instanceof Closure) { 	
		actionCode = generator.generateActionCode(this, sgroup['selection.src'], sgroup)
      }
      if (actionCode) { 
		def items = sgroup.children?.collect { w ->
		  (w.widgetType == 'Spinner') ? "${w.id}Data[0], " : ''
		}.join('')

		int i = 0
		def selectCode = sgroup.children?.collect { w ->
		  (w.widgetType == 'Spinner') ? "if (parent == ${w.id}) items[${i++}] = parent.getItemAtPosition(pos).toString();\n" : ''
		}.join('')
	
		def binding = [
		  items: items,
		  selectCode: selectCode,
		  actionCode: actionCode
		]
		generator.injectCodeFromTemplateRef(classModel, "Default:spinner1", binding)
		sgroup.children?.each { w -> 
		  if (w.widgetType == 'Spinner') { 
			def binding1 = [ name : w.id ]
			generator.injectCodeFromTemplateRef(classModel, "Default:spinner2", binding1)
		  }
		}
      }
    }
  }

  def processPopups(group) { 
	if (Language.isView(group?.nodeType)) { 
	  group.children.each { Widget widget -> 
		if (Language.isPopup(widget.nodeType) &&
			!Language.isMenu(widget.nodeType)) {  
		  popupProcessor.process(widget)
		} else if (Language.isGroup(widget.nodeType)) { 
		  processPopups(widget)
		}
	  }
    }
  }

}