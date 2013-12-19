package xj.mobile.model.impl

import xj.mobile.codegen.templates.AndroidTemplates
import xj.mobile.codegen.CodeGenerator.InjectionPoint

import static xj.mobile.codegen.CodeGenerator.InjectionPoint.*
import static xj.mobile.util.CommonUtil.*

class AndroidViewClass extends AndroidClass { 

  static final VIEW_IMPORTS = [
	'android.view.View',
	'android.content.Context',
	'android.util.AttributeSet',
	'android.graphics.*'
  ]

  String drawScrap = ''

  AndroidViewClass(String name) { 
	super(name, 'View')

	imports += VIEW_IMPORTS
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
	//initScrap += setViewBackgroundCode(background, 'self')
  }

  public void process() { 
	super.process()
	 methodScrap = """public ${name}(Context context){
    super(context);
}

public ${name}(Context context, AttributeSet attrs){
    super(context, attrs);
}
""" +  methodScrap

	 if (drawScrap) { 
	   methodScrap += """
@Override
protected void onDraw(Canvas canvas) {
    // Drawing code
${indent(drawScrap)}  
}
"""
	 }
  }

}