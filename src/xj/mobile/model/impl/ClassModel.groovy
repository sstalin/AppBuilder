package xj.mobile.model.impl

import xj.mobile.api.AttributeHandler
import xj.mobile.api.APIResolver
import xj.mobile.codegen.ActionHandler
import xj.mobile.codegen.templates.Templates
import xj.mobile.codegen.UnparserUtil
import xj.mobile.codegen.CodeGenerator.InjectionPoint

import xj.translate.common.Unparser
import static xj.translate.Logger.info 

//import static xj.mobile.lang.AttributeMap.findCommonAttributeDef

//
// represent a class in the output model -- the elements of a class   
//
class ClassModel { 

  // owner 
  Project project 

  String name
  String superClassName
  String packageName 

  boolean isMainView = false

  List<String> systemImports = []
  List<String> imports = []

  List<String> systemImageFiles = []
  List<String> imageFiles = []

  String declarationScrap = ''
  String methodScrap = ''

  boolean needGenerateCode = true

  @Delegate
  Templates templates

  ////

  public void addImageFile(imageFile) { 
    if (imageFile)
      imageFiles << imageFile
  }

  public void process() { }

  public void injectCode(InjectionPoint location, String code, Map binding = null) { }

  public void injectCode(Closure code, Map binding = null) { 
    info '[ClassMode] injectCode(Closure)'
    if (code) { 
	  code.delegate = this 
	  code(binding)
    }
  }

  public void addImport(String header) { 
    if (header) {
	  imports << header
    }
  }

  //
  // static methods 
  //

  static String getCustomViewName(String name) { 
	if (name) { 
	  int i = indexOfNumSiffix(name)
	  String suffix = ''		
	  if (i >= 0) { 
		suffix = name[i .. -1]
		name = name[0 .. (i-1)]
	  }
	  return name[0].toUpperCase() + name[1 .. -1] + 'View' + suffix 
	}
	return 'CustomView'
  }

  static int indexOfNumSiffix(String s) { 
	if (s) { 
	  int i = s.length()
	  while (i > 0 && s.charAt(i - 1) >= '0' && s.charAt(i - 1) <= '9') i--; 
	  if (i < s.length()) return i
	}
	return -1 
  }

}