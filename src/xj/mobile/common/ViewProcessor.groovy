package xj.mobile.common

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.*

import static org.codehaus.groovy.ast.ClassHelper.*

import xj.mobile.*
import xj.mobile.model.sm.*
import xj.mobile.model.ModelNode
import xj.mobile.model.Application
import xj.mobile.model.impl.Project
import xj.mobile.model.ui.*
import xj.mobile.lang.*
import xj.mobile.api.TypeInfo
import xj.mobile.api.FrameworkTypeInfo
import xj.mobile.codegen.CodeGenerator
import xj.mobile.model.properties.Property
import xj.mobile.model.properties.ModalTransitionStyle

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

import xj.translate.typeinf.TypeCategory

import static xj.translate.common.ClassProcessor.MethodInfo
import static xj.translate.typeinf.MethodHelper.addReturnsIfNeeded
import static xj.translate.typeinf.TypeInference.inferStatementTypes
import static xj.translate.typeinf.TypeInference.inferExpressionTypes

import static xj.translate.Logger.info 

abstract class ViewProcessor {

  View view
  String viewName

  def config

  Map viewInfo

  TypeInfo typeInfo

  WidgetProcessor widgetProcessor
  PopupProcessor popupProcessor

  ViewHierarchyProcessor vhp

  def widgetTable = [:]  // widget.id -> widget, include nested widgets in the same top view

  def closureDefs = [:]  // closure definitions in this view, name -> closure exp

  int contentWidth = 0, contentHeight = 0

  List topViews = []          // the id of sub views of this view that are top views  
  Set  nextViews = [] as Set  // the id of the views that can follow this view 

  Map nextViewMap = [:]

  CodeGenerator generator

  static ViewProcessor currentViewProcessor 

  public ViewProcessor(View view, String viewName = null) { 
    this.view = view
    this.viewName = viewName ?: (view?.id) 

    initViewInfo()
  }

  void init(AppInfo appInfo) { }

  public boolean isMainView() { 
	view && view.root 
	// !view.parent || (view.parent instanceof Application) && (view.parent.children[0] == view)
  }

  Project getProject() { 
	vhp?.project
  }

  def getWidget(id) { 
    widgetTable[id]
  }

  View findViewPredecessor(v) { 
    def pre = []
    nextViewMap.each { k, vset ->
      if (v.id in vset) { 
		pre << k
      }
    }
    if (pre) { 
      return view.getChild(pre[0])
    }
    return null
  }

  boolean hasClosureDef(String name) { 
	closureDefs[name] != null
  }

  def getChild(id, boolean nested = false) { 
    //view?.children?.find { it.id == id }
    view.getChild(id, nested)
  }

  void initViewInfo() { 
    if (view) { 
      def vars = null 
      viewInfo = view['#info']
      def declarations = viewInfo?.declarations
      if (declarations) { 
		vars = [:]
		declarations.each { name, inf ->
		  def decl = inf.src
		  if (decl instanceof DeclarationExpression) { 
			if (!(decl.rightExpression instanceof ClosureExpression)) { 
			  def var = decl.variableExpression
			  if (var instanceof VariableExpression) { 
				vars[name] = ClassHelper.getUnwrapper(var.type)
			  }
			}
		  }
		}

		if (!vars) { 
		  vars = null
		}
      }

	  info "[ViewProcessor] initViewInfo() typeInfo: ${vars}"
      typeInfo = new TypeInfo(FrameworkTypeInfo.getFrameworkTypeInfo(platform), vars)
	  xj.translate.Translator.setExternalTypeInfo(typeInfo)

      if (declarations) { 
		def methods = [:]
		declarations.each { name, inf ->
		  def decl = inf.src
		  if (decl instanceof DeclarationExpression) { 
			if (decl.rightExpression instanceof ClosureExpression) { 
			  def var = decl.variableExpression
			  def cloExp = decl.rightExpression
			  boolean isVoid = isStatementVoidType(cloExp.code)
			  if (!isVoid) { 
				def cbody = addReturnsIfNeeded(cloExp.code, true)
				cloExp.code = cbody
			  } else { 
				use (TypeCategory) { 
				  cloExp.setActualType(VOID_TYPE)
				}
			  }

			  inferExpressionTypes(cloExp, cloExp.variableScope) 

			  def type = VOID_TYPE
			  if (!isVoid) { 
				use (TypeCategory) { 
				  type = cloExp.getActualType()
				}
			  }
			  info "[ViewProcessor] handle closure declaration: ${name} actualType: ${type}"
			  closureDefs[name] = decl.rightExpression 

			  MethodInfo minfo = new MethodInfo()
			  // String name, int modifiers, ClassNode returnType, Parameter[] parameters, 
			  // ClassNode[] exceptions, Statement code
			  minfo.method = new MethodNode(name, 0, type, cloExp.parameters, null, null)
			  minfo.omitParamNameInMethodCall = true
			  if (methods[name] == null) { 
				methods[name] = [ minfo ]
			  } else { 
				def m = methods[name]
				if (m instanceof List) { 
				  m << minfo
				}
			  }
			}
		  }
		}

		typeInfo.methodMap = methods
	  }

    }
  }

  static boolean isStatementVoidType(Statement stmt) { 
	if (stmt) { 
	  switch (stmt.class) { 
	  case ReturnStatement:
		return stmt.expression == null
	  case ExpressionStatement:
		Expression exp = stmt.expression 
		if (exp.operation.text == "=" &&
			exp.leftExpression.class == PropertyExpression) {
		  return true
		} else { 
		  return false
		}
	  case IfStatement: 
		return (isStatementVoidType(stmt.ifBlock) ||
				(stmt.elseBlock && isStatementVoidType(stmt.elseBlock)))
	  case BlockStatement:
		if (stmt.statements && stmt.statements.size() > 0) { 
		  return isStatementVoidType(stmt.statements[-1])
		} else { 
		  return true
		}
	  }
	}
	return false
  }

  public void process() { 
    info "[ViewProcessor] process() ${viewName}"

    if (view) { 
	  currentViewProcessor = this

      //(contentWidth, contentHeight) = Layout.layout(view, config, !view.scroll, true)
	  (contentWidth, contentHeight) = Layout.layout(view, config, true)
      processView()
    }
  }


  /// to-do: more general handling needed
  public static TransitionAttributes = [ 'taps', 'direction', 'touches' ]
  public static ActionAttributes = [ 'delay' ]

  public void processTransitions() { 
	view.children.each { t -> 
	  if (t instanceof Transition) { 
		String tempRef = "Default:${t.nodeType}"
		String code = null
		boolean hasActionParameters = false 
		def srcInfo = t['#action.src']
		if (srcInfo && srcInfo.code instanceof ClosureExpression) { 
		  hasActionParameters = srcInfo.code.parameters
		  if (hasActionParameters) { 
			def temp = generator.getTemplateByRef(tempRef)
			if (temp) { 
			  if (temp instanceof List) { 
				if (temp.size() > 0) { 
				  srcInfo['param'] = temp[0].parameters
				}
			  } else { 
				srcInfo['param'] = temp.parameters
			  }
			}
		  }
		  code = generator.generateActionCode(this, srcInfo, t) 
		}

		if (t.next) { 
		  boolean animated = true
		  ModalTransitionStyle transition = null 
		  if (t.animated != null) 
			animated = t.animated as Boolean
		  if (t.transition instanceof ModalTransitionStyle)
			transition = t.transition

		  String tcode = generateTransitionCode(t.next, isInsideNavigationView(view), 
												view.embedded as boolean, 
												animated, transition)
		  if (tcode) { 
			if (code) code += ('\n' + tcode) 
			else code = tcode
		  }
		}
		
		t.children.each { c -> 
		  if (c instanceof Action) { 
			if (c.nodeType == 'doAfter') { 
			  srcInfo = c['#action.src']
			  if (srcInfo) { 
				def after_code = generator.generateActionCode(this, srcInfo, c) 
				if (after_code) { 
				  def actionBinding = [ name: t.id,
										code: after_code]
				  ActionAttributes.each { attr -> 
					if (c[attr] != null) actionBinding[attr] = c[attr]
				  }
				  def code1 = generator.instantiateCodeFromTemplateRef("Default:afterAction1", actionBinding)
				  if (code1) { 
					if (code) code += ('\n' + code1) 
					else code = code1	
				  }
				  generator.injectCodeFromTemplateRef(classModel, "Default:afterAction2", actionBinding)
				}
			  }
			}
		  }
		}

		def binding = [ name: t.id ] 
		TransitionAttributes.each { attr -> 
		  def val = t[attr]
		  if (val instanceof Property) { 
			val = getPropertyValueString(val)
		  }
		  binding[attr] = val
		}
		if (code) { 
		  binding += [ code: code,
					   hasActionParameters: hasActionParameters ] 
		  generator.injectCodeFromTemplateRef(classModel, tempRef, binding)
		}
	  }
	}
  }

  public void processWidgetTable(Widget widget = null) { 
    if (widget == null || 
		Language.isGroup(widget.nodeType) || 
		Language.isPopup(widget.nodeType)) {
      if (widget == null) { 
		widget = view
      }
      //if (Language.hasProperties(widget.widgetType)) { 
      widgetTable[widget.id] = widget
      //}
      widget.children.each { w -> 
		if (w instanceof Widget) { 
		  processWidgetTable(w)
		}
      }
    } else if (Language.isWidget(widget.nodeType)) { 
      widgetTable[widget.id] = widget   
	  preprocessWidget(widget)   
    }
  }

  protected void preprocessWidget(Widget widget) { }
  
  // additional processing if this view is the root view  
  protected void processRootView() {
	info '[ViewProcessor] processRootView()'
  } 

  public void processView() { 
    initializeTopView()

    handleLocalDeclarations()
    processSubviews(view)
	processTransitions()
    postProcessTopView()
  }

  protected void initializeTopView() { }

  protected void processSubviews(View view) { }

  protected void handleLocalDeclarations() { 
    def viewInfo = view['#info']
    if (viewInfo) { 
      def declarations = viewInfo.declarations
      if (declarations) {  
		declarations.each { name, inf -> 
		  if (inf.src.rightExpression instanceof ClosureExpression) { 
			handleClosureDeclaration(inf.src)
		  } else { 
			if (inf.src && !isEntityDeclaration(inf.src)) { 
			  handleLocalVariableDeclaration(inf.src)
			}
		  }
		} 
      }
	  handleSpecialLocalDeclarations()
    }
  }

  protected void handleSpecialLocalDeclarations() { }

  protected void postProcessTopView() { }

  protected void handleLocalVariableDeclaration(decl) { }

  //protected void handleClosureDeclaration(decl) { }

  protected void handleClosureDeclaration(decl) { 
	def declmap = generator.unparser.unparseDeclarationExpression(decl)
	def params = declmap.params
	def paramMap = null
	String paramStr = ''
	if (params) { 
	  paramMap = [:]	 
	  params.each { p -> paramMap[p.name] = p.type }
	  //paramStr = params.collect{ p -> "${p.typeName} ${p.name}" }.join(', ')
	  paramStr = generator.instantiateCodeFromTemplateRef("Default:closureParam", [params: params])
	}

	def srcInfo = [
	  code: decl.rightExpression,
	  param: paramMap,
	]
	String body = generator.generateActionCode(this, srcInfo, null)

	def binding = [ type: declmap.rtype,
					name: declmap.varname,
					params: paramStr,
					body: body ]
	generator.injectCodeFromTemplateRef(classModel, "Default:closureDecl", binding)
  }

  boolean isEntityDeclaration(decl) { 
    //info "[ViewProcessor] isEntityDeclaration() ${decl}"
    def exp = decl?.rightExpression
    if (exp instanceof MethodCallExpression &&
		exp.methodAsString == 'ListEntity') { 
      return true
    } else { 
      return false
    }
  }

  static ImageAttr = [
	Image: 'file',
	ImageShape: 'file',
	Button: 'image',
	ImageButton: 'image',
  ]

  static boolean allowImage(widget) { 
	widget && widget.widgetType in ImageAttr.keySet()
  }

  static getImageSizeForWidget(widget) { 
	if (widget && ImageAttr[widget.widgetType]) { 
	  return getImageSize(widget[ImageAttr[widget.widgetType]])
	} else { 
	  null
	}
  }

  public void handleImageFiles(Widget widget) { 
	String iattr = ImageAttr[widget.widgetType]
	if (iattr && widget[iattr]) { 
	  classModel.addImageFile(widget[iattr])
	}
  }

  def processPopups() { 
    processPopups(view) 
  }

  def processPopups(group) { 
    if (Language.isView(group?.nodeType)) { 
      group.children.each { Widget widget -> 
		if (Language.isPopup(widget.nodeType)) {  
		  popupProcessor.process(widget)
		} else if (Language.isGroup(widget.nodeType)) { 
		  processPopups(widget)
		}
      }
    }
  }

  def findEntityHandler(widget) { null }

  def getWidgetAttributeType(ModelNode widget, String attrName) { 
	if (widget && attrName) { 
	  return getWidgetAttributeType(widget['#type'], widget['#subtype'], attrName)
	}
	return null 
  }

  def getWidgetAttributeType(String widgetType, String widgetSubtype, String attrName) { 
	if (widgetType && attrName) { 
	  def t = typeInfo.getPropertyType(widgetType, attrName)

	  if (widgetType == 'NumberStepper') { 
		if (attrName == 'value' &&
		   widgetSubtype == 'Int') { 
		  // handle special case, customize using #subtype
		  t = int_TYPE
		}
	  }

	  return t
	}
	return null 
  }

  // generate code to handle the transition to the next state 
  // handle widget.next
  String generateTransitionCode(String next, 
								boolean inNavView, 
								boolean isEmbedded = false, 
								boolean animated = true,
								ModalTransitionStyle style = null,
								data = null) { 
    String actionCode = null
    if (next) {      
      def nextView = getChild(next, true)
	  def parent = view.parent
	  while (nextView == null && parent != null) { 
		nextView = parent.getChild(next, true)
		parent = parent.parent
	  }
      if (nextView && Language.isPopup(nextView.nodeType)) { 
		info "[DefaultViewProcessor] process popup ${nextView.id}"
		actionCode = generator.generatePopupTransitionCode(getWidgetName(nextView), 
														   popupProcessor.getTemplateName(nextView.widgetType), 
														   popupProcessor.isMenu(nextView.widgetType))
      } else { 
		//nextView = view.parent.getChild(next, true)
		if (nextView){ 
		  nextViews << next	  
		} else if (next in [ 'Previous', 'Top', 'previous', 'top' ]) {  
		  next = "#${next.capitalize()}"
		} else { 
		  next = null
		}
		if (next != null) { 
		  if (inNavView)
			actionCode = generator.generatePushTransitionCode(viewName, next, isEmbedded, data)
		  else 
			actionCode = generator.generateModalTransitionCode(classModel, viewName, next, 
															   isEmbedded, animated, style, data)
		}
	  }
    }
    return actionCode
  }

  def setAttributes(widget, attrs, classModel) { 
	def attrCode = []
	if (attrs) { 
	  String name = getWidgetName(widget)
	  attrs.each { a ->
		info "[ViewProcessor] process attribute ${a} ${widget.widgetType}"
		//CodeGenerator generator = CodeGenerator.getCodeGenerator('ios')
		def src = widget["${a}.src"]
		if (src) { 
		  def ucode = generator.generateUpdateCode(this, src.code, widget, name, a);
		  generator.injectCodeFromTemplate(classModel, CodeGenerator.InjectionPoint.LoadViewTail, ucode)
		  //generator.injectCodeFromTemplate(classModel, UpdateLoadView, ucode)
		} else { 
		  def code = generator.generateSetAttributeCode(widget.widgetType, name, a, widget[a])
		  if (code != null) { 
			attrCode << [ code[0], "${code[1]};" ]
		  } else { 
				
		  }			  
		}

	  }
	}
	return attrCode
  }

  // static features 

  static boolean imageFileExists(imageFile) { 
    File img = new File("${Main.imageDir}/${imageFile}")
    return img.exists() 
  }

  static getImageSize(String imageFile) { 
    File img = new File("${Main.imageDir}/${imageFile}")
    if (img.exists()) { 
      BufferedImage bi = ImageIO.read(img)
      if (bi) { 
		return [ bi.width, bi.height ]
      }
    }
    return [0, 0]
  }

  static String getWidgetName(Widget widget) { 
    widget.id
  }

  static String getActionName(Widget widget) { 
	getActionName(widget.nodeType)
  }

  static String getActionName(String widgetType) { 
    if (widgetType) { 
      return widgetType[0].toLowerCase() + widgetType[1 .. -1] + 'Action'
    }
    return null
  }

  static boolean hasWidgetTypes(ModelNode widget, wtypes) { 
    if (widget instanceof Widget) { 
      if (widget instanceof View) { 
		return widget.children.any { hasWidgetTypes(it, wtypes) }
      } else { 
		if (wtypes instanceof String) { 
		  return widget.widgetType == wtypes
		} else if (wtypes instanceof Collection) { 
		  return  widget.widgetType in wtypes
		} 
      }
    }
    return false 
  }
  
  static boolean isInsideNavigationView(widget) { 
    if (widget) { 
      def w = widget?.parent
      while (w instanceof Widget) { 
		if (w.widgetType == 'NavigationView') return true
		w = w.parent
      }
    }
    return false
  }

  static getTransitionInfo(next) { 
	String nextState = null
	def data = null 
	if (next instanceof Map) {
	  nextState = next.to?.toString()
	  data = next.data
	} else {  
	  nextState = next.toString()
	}
	return [ nextState, data ]
  } 

}