package xj.mobile.ios

import xj.mobile.common.*
import xj.mobile.model.*
import xj.mobile.model.impl.*
import xj.mobile.model.ui.*
import xj.mobile.model.sm.*
import xj.mobile.lang.*

import xj.mobile.codegen.CodeGenerator
import xj.mobile.model.properties.ModalTransitionStyle
import xj.mobile.model.properties.Property

import static xj.mobile.ios.IOSAppGenerator.iosConfig
import static xj.mobile.common.ViewHierarchyProcessor.toViewName
import static xj.mobile.model.impl.ViewControllerClass.getViewControllerName
import static xj.mobile.codegen.CodeGenerator.InjectionPoint.*

import static xj.mobile.util.CommonUtil.*

import static xj.translate.Logger.info 

// process a single top view 
class DefaultViewProcessor extends ViewProcessor { 

  IOSClass classModel

  boolean needKeyboardScrolling = false 
  Map radioGroups = [:]

  public DefaultViewProcessor(View view, String viewName = null) { 
    super(view, viewName)
	classModel = new ViewControllerClass(viewName)
	generator = CodeGenerator.getCodeGenerator('ios')

    widgetProcessor = new WidgetProcessor(this)
    popupProcessor = new PopupProcessor(this)

    config = iosConfig
  }

  public String getPlatform() { 
	'iOS'
  }

  String getPropertyValueString(Property val) { 
	val.toIOSString()
  }

  protected void handleLocalVariableDeclaration(decl) { 
	def declmap = generator.unparser.unparseDeclarationExpression(decl)

	String code = "${declmap.type} ${declmap.name};"
	classModel.injectCode(VariableDeclaration, code)
	if (declmap.init) { 
	  code = "${declmap.name} = ${declmap.init};"
	  classModel.injectCode(LoadViewTail, code)
	}
  }

  protected void handleSpecialLocalDeclarations() { 
	def uset = view['#info'].useSet
	if (uset && 'data' in uset) { 
	  classModel.declareProperty('NSString', 'data')
	  //String code = "data = nil;"
	  //classModel.injectCode(LoadViewTail, code)
	}
  }

  public void processRootView() { 
	info '[iOS.DefaultViewProcessor] processRootView()'
	def binding = [ orientations: view.supportedOrientations,
					supportedOrientationsMethod: view.supportedOrientationsMethod
				  ]
	generator.injectCodeFromTemplateRef(classModel, 'Default:orientations', binding)
  } 

  protected void initializeTopView() { 
	needKeyboardScrolling = hasWidgetTypes(view, 'Text') && !hasWidgetTypes(view, [ 'Web', 'Map' ]) 
	info "[DefaultViewProcessor] needKeyboardScrolling: ${needKeyboardScrolling}"

	boolean needScroll = (view.scroll || needKeyboardScrolling || 
						  view.children.any { w -> (w instanceof Widget) && w.scroll })
	int cwidth = needScroll ? Math.max(contentWidth, config.defaults.Screen.width) : contentWidth
	int cheight = needScroll ? Math.max(contentHeight, config.defaults.Screen.height) : contentHeight

	classModel.initTopView(needScroll, cwidth, cheight)
	classModel.setViewBackground(view.background)
  }

  protected void processNextViews() { 
	if (nextViews) { 
	  nextViews.each { vid -> 
		String name = vid
		def vp = vhp.getViewProcessor(vid)
		if (vp) { 
		  String uiclass = getViewControllerName(vp.viewName)
		  classModel.addImport(uiclass) 
		  classModel.declareProperty(uiclass, name)
		}
	  }
	}
  }

  protected void processSubviews(View view) { 
    // process subviews	
    view.children.each { widget -> 
	  if (widget instanceof Widget) { 
		if (Language.isTopView(widget.nodeType) && widget.embedded) {  
		  processEmbeddedView(widget)
		} else if (Language.isGroup(widget.nodeType)) {  
		  if (widget.widgetType == 'RadioGroup') { 
			preProcessRadioGroup(widget)
			processSubviews(widget)
			postProcessRadioGroup(widget)
		  } else { 
			processSubviews(widget)
		  }       
		} else if (Language.isPopup(widget.nodeType)) {  
		  popupProcessor.process(widget)
		} else if (Language.isWidget(widget.nodeType)) {  
		  widgetProcessor.process(widget)
		}
	  }
    }
  }

  protected void postProcessTopView() { 
	processNextViews()
	if (view.embedded) { 
	  String ownerViewClass = getViewControllerName(toViewName(view.parent.id))
	  classModel.declareProperty(ownerViewClass, 'owner', false)
	  classModel.addImport(ownerViewClass) 
	}

    classModel.handleDelegates()
    if (needKeyboardScrolling)
      handleKeyboard() 
  }

  void processEmbeddedView(widget) { 
	//nextViews << widget.id

	String name = widget.id
	String viewClass = getViewControllerName(toViewName(name))
	classModel.addImport(viewClass) 
	classModel.declareProperty(viewClass, name)

	def binding = [ name: name, 
					viewClass: viewClass, 
					frame: widget._frame ? widget._frame[0..3].join(', ') : '0, 0, 0, 0' ]
	generator.injectCodeFromTemplateRef(classModel, "Default:embeddedView", binding)
  }

  void preProcessRadioGroup(rgroup) { 
    if (rgroup) { 
      def members = []
      rgroup.children.each { Widget widget -> 
		if (widget.widgetType == 'RadioButton') { 
		  members << widget.id
		}
      }
      radioGroups[rgroup.id] = members
    }
  }

  void postProcessRadioGroup(rgroup) { 
    if (rgroup) { 
      def members = radioGroups[rgroup.id]
      if (members.size() >= 3) { 
		def binding = [
		  name: rgroup.id,
		  members: members
		]
		generator.injectCodeFromTemplateRef(classModel, "Default:radioGroup", binding)
      }
    }
  }

  void handleKeyboard() { 
	// scroll the contents to uncover the textfield when the kayboard is displayde  
	generator.injectCodeFromTemplateRef(classModel, "Default:keyboard")
  }

}



