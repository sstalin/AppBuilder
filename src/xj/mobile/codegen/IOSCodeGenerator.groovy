package xj.mobile.codegen

import xj.mobile.common.AppGenerator
import xj.mobile.codegen.templates.IOSTemplates
import xj.mobile.model.impl.ClassModel
import xj.mobile.model.properties.ModalTransitionStyle

import static xj.mobile.common.ViewHierarchyProcessor.toViewName
import static xj.mobile.model.impl.ViewControllerClass.getViewControllerName

class IOSCodeGenerator extends CodeGenerator { 

  IOSCodeGenerator() { 
	templates = IOSTemplates.getInstance()
	actionHandler = new xj.mobile.codegen.IOSActionHandler()
	attributeHandler = new xj.mobile.api.IOSAttributeHandler()

	AppGenerator appgen = AppGenerator.getAppGenerator('ios')
	unparser = appgen.translator.unparser
	engine = appgen.engine
  }


  //
  // generate transition code
  //

  String generatePushTransitionCode(String curView, 
									String nextView, 
									boolean isEmbedded = false, 
									data = null) { 
	String target = isEmbedded ? 'self.owner' : 'self'
    if (nextView != null && nextView != '' && nextView[0] != '#') { 
	  String nextViewControllerName = getViewControllerName(toViewName(nextView))
	  //classModel.addImport(nextViewControllerName)

	  String setData = ''
	  if (data) { 
		if (data instanceof String) { 
		  setData = "${nextView}.data = @\"${data}\";\n"
		}
	  }
	  return """if (${nextView} == nil) ${nextView} = [[${nextViewControllerName} alloc] init];
${setData}[${target}.navigationController pushViewController:${nextView} animated:YES];"""	
	} else if (nextView == '#Previous') {  
	  return "[${target}.navigationController popViewControllerAnimated:YES];"
	} else if (nextView == '#Top') {
	  return "[${target}.navigationController popToRootViewControllerAnimated:YES];"
	}
  }

  String generateModalTransitionCode(ClassModel classModel,
									 String curView, 
									 String nextView, 
									 boolean isEmbedded = false, 
									 boolean animated = true,
									 ModalTransitionStyle style = null,
									 data = null) { 
	String ani = (animated || style != null) ? 'YES' : 'NO'
	String target = isEmbedded ? 'self.owner' : 'self'
    if (nextView != null && nextView != '' && nextView[0] != '#') { 
	  String nextViewControllerName = getViewControllerName(toViewName(nextView))
	  classModel.addImport(nextViewControllerName)

	  String setStyle = ''
	  String setData = ''
	  if (style) { 
		setStyle = "\n${nextView}.modalTransitionStyle = ${style.toIOSString()};"
	  }
	  if (data) { 
		if (data instanceof String) { 
		  setData = "\n${nextView}.data = @\"${data}\";"
		}
	  }
	  return """if (${nextView} == nil) ${nextView} = [[${nextViewControllerName} alloc] init];${setStyle}${setData}
[${target} presentViewController:${nextView} animated:${ani} completion: NULL];"""
	} else if (nextView == '#Previous') {  
	  return "[${target} dismissViewControllerAnimated:${ani} completion:NULL];"
	} else if (nextView == '#Top') {
	  return """UIViewController* top = ${target};
while (top.presentingViewController != nil) top = top.presentingViewController;
[top dismissViewControllerAnimated:${ani} completion:NULL];"""
	}
  }


}