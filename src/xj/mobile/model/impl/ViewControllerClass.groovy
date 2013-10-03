package xj.mobile.model.impl

import xj.mobile.common.AppGenerator
import xj.mobile.codegen.templates.IOSTemplates
import xj.mobile.codegen.Delegate
import xj.mobile.model.properties.ModalTransitionStyle

import xj.mobile.codegen.CodeGenerator
import xj.mobile.codegen.CodeGenerator.InjectionPoint
import static xj.mobile.codegen.CodeGenerator.InjectionPoint.*

import static xj.mobile.util.CommonUtil.*
import static xj.mobile.common.ViewProcessor.*
import static xj.mobile.codegen.templates.IOSDelegateTemplates.*
import static xj.translate.Logger.info 

//
// represent a view controller class in iOS   
//
class ViewControllerClass extends IOSClass { 

  boolean initViewWithSuper = false

  boolean canBecomeFirstResponder = false 

  String loadViewBodyScrap = ''
  String loadViewBodyTailScrap = ''
  String viewWillAppearScrap = ''

  String viewDidLoadBodyScrap = ''
  String deallocScrap = ''

  String popupActionScrap = ''

  ViewControllerClass(String viewName) { 
	super(getViewControllerName(viewName), 'UIViewController')

	templates = IOSTemplates.getInstance()
  }

  String getInitializeViewScrap() { 
    String code = getLoadViewScrap() 
    if (viewDidLoadBodyScrap)
      code += getViewDidLoadScrap()
    return code
  }
  
  String getLoadViewScrap() { 
	String body = loadViewBodyScrap + loadViewBodyTailScrap
    return """
- (void)loadView 
{
${indent(body)} 	
}
"""
  }
  
  String getViewDidLoadScrap() { 
    return """
- (void)viewDidLoad 
{
${indent(viewDidLoadBodyScrap)} 	
}
"""
  }

  static String getViewControllerName(String viewName) { 
	if (viewName) { 
	  if (viewName.startsWith('View')) { 
		String suffix = ''
		if (viewName.length() > 4)
		  suffix = viewName[4 .. -1]
		return 'ViewController' + suffix 
	  } else { 
		return viewName + 'ViewController'
	  }
	}
	return 'ViewController'
  }

  //
  //  Building components 
  //

  public void injectCode(InjectionPoint location, String code, Map binding = null) { 
	if (location && code) { 
	  switch (location) { 
	  case Creation:
	  case LoadView:
		if (loadViewBodyScrap != '')
		  loadViewBodyScrap += "\n${code}"
		else 
		  loadViewBodyScrap = code
		break; 
	  case PostCreation:
	  case LoadViewTail:
		loadViewBodyTailScrap += "\n${code}"
		break; 
	  case UpdateView:
		if (loadViewBodyScrap != '')
		  viewWillAppearScrap += "\n${code}"
		else 
		  viewWillAppearScrap = code
		break; 
	  default:
		super.injectCode(location, code, binding)
	  }
	}
  }


  public void process() { 
	super.process()
	if (viewWillAppearScrap) { 
	  methodScrap = """- (void) viewWillAppear:(BOOL)animated
{
    [super viewWillAppear: animated];
${indent(viewWillAppearScrap)}
}
""" + methodScrap
	}

	if (canBecomeFirstResponder) { 
	  methodScrap += '''
- (BOOL)canBecomeFirstResponder
{ 
	return YES; 
}
'''
	}

	methodScrap = initializeViewScrap + popupActionScrap + methodScrap + '''
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.

}
'''
  }

  void initTopView(boolean needScroll, int cwidth, int cheight) { 
    if (initViewWithSuper) { 
      loadViewBodyScrap += """[super loadView];
"""
    } else { 
      String viewClass = needScroll ? 'UIScrollView' : 'UIView'
      loadViewBodyScrap += """self.view = [[${viewClass} alloc] initWithFrame:[UIScreen mainScreen].applicationFrame];
"""
      if (needScroll) { 
		loadViewBodyScrap += """[(UIScrollView*) self.view setContentSize:CGSizeMake(${cwidth}, ${cheight})];
[(UIScrollView*) self.view setMaximumZoomScale:2.0];
"""
      }
    }
  }

  void setViewBackground(background) { 
	if (!initViewWithSuper) { 
	  loadViewBodyScrap += setViewBackgroundCode(background, 'self.view') + '\n'
	}
  }

  //
  // handle action
  //

  void injectActionCode(String widgetType, String widgetName, wtemp, actionCode) { 
    def delegateAction = getDelegateTemplateForWidget(wtemp, 'action')
    if (delegateAction) { 
      if (actionCode || widgetType == 'Text') { 
		Delegate d = delegateActions[wtemp.delegate]
		d.actions << [ name: widgetName, code: actionCode ]
      }
    } else { 
      if (actionCode) { 		
		def actionName = "${getActionName(widgetType)}_${widgetName}"
		def actionTemplate = getTemplate(wtemp, 'action')
		def targetTemplate = getTemplate(wtemp, 'target')
		def event = getTemplate(wtemp, 'actionEvent')
		CodeGenerator generator = CodeGenerator.getCodeGenerator('ios')
		generator.injectCode(this, widgetName, event, actionCode, actionName, 
							 actionTemplate, targetTemplate) 
      }
    }
  }  


}