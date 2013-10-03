package xj.mobile.model.impl

import xj.mobile.codegen.templates.IOSTemplates
import xj.mobile.codegen.CodeGenerator.InjectionPoint

import static xj.mobile.codegen.CodeGenerator.InjectionPoint.*
import static xj.mobile.util.CommonUtil.*

//
// represent a custom view class in iOS   
//
class IOSViewClass extends IOSClass { 

  String drawScrap = ''
  String initScrap = ''

  IOSViewClass(String viewName) { 
	super(viewName, 'UIView')

  }

  public void injectCode(InjectionPoint location, String code, Map binding = null) { 
	if (location && code) { 
	  switch (location) { 
	  case Draw:
		if (drawScrap != '')
		  drawScrap += "\n${code}"
		else 
		  drawScrap = code
		break; 

	  default:
		super.injectCode(location, code, binding)
	  }
	}
  }

  void setViewBackground(background) { 
	initScrap += setViewBackgroundCode(background, 'self')
  }

  public void process() { 
	super.process()
	 methodScrap = """- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
${indent(initScrap, 2)}
    }
    return self;
}
""" + methodScrap
	 if (drawScrap) { 
	   methodScrap += """
- (void)drawRect:(CGRect)rect
{
    // Drawing code
${indent(drawScrap)}  
}
"""
	 }
  }

}