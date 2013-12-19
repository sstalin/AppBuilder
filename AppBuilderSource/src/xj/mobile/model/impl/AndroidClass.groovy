package xj.mobile.model.impl

import xj.mobile.common.AppGenerator

import xj.mobile.codegen.CodeGenerator.InjectionPoint

import static xj.mobile.codegen.CodeGenerator.InjectionPoint.*
import static xj.mobile.util.CommonUtil.*

//
// represent an activity class in Android   
//
class AndroidClass extends ClassModel { 

  // additional classes to be included, the classes are predefined templates 
  def auxiliaryClasses = [] as Set

  @Delegate
  AndroidResources resources

  AndroidClass(String name, String superClassName) { 
	this.name = name
	this.superClassName = superClassName

	resources = new AndroidResources()
  }

  String getImportScrap() {
    if (imports) { 
      return imports.unique().sort().collect { f -> "import ${f};" }.join('\n')
    }
    return ''
  }  

  boolean isNeedSupportLibrary() { 
	false
  }

  //
  //  Building components 
  //

  public void process() {}

  public void injectCode(InjectionPoint location, String code, Map binding = null) { 
	if (location && code) { 
	  switch (location) {
	  case Import:
		imports << code
		break; 
	  case Declaration:
		declarationScrap += "\n${code}"
		break;
	  case Method:
		methodScrap += "\n${code}"
		break;

	  case Draw:
		if (drawScrap != '')
		  drawScrap += "\n${code}"
		else 
		  drawScrap = code
		break; 

	  }
	}
  }

}