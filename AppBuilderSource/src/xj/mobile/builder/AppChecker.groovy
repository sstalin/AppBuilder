package xj.mobile.builder

import xj.mobile.lang.*
import xj.mobile.model.*
import xj.mobile.model.ui.*

import static xj.translate.Logger.info 

class AppChecker {
  
  static verbose = false

  @Delegate
  ErrorMessages errors = new ErrorMessages()

  Application app = null
  
  AppChecker(app) { 
    this.app = app
    info "[AppChecker] verbose=${verbose}"
    if (app?.mainView)
      check(app.mainView)
  }

  def check(ModelNode node) { 
    if (verbose) info "[AppChecker] check ${node.nodeType}"
	
	String nodeType = node.nodeType
	if (Language.isTransition(nodeType)) { 

	} else if (Language.isState(nodeType)) { 

	} else if (Language.isAction(nodeType)) { 

	} else if (Language.isUI(nodeType)) { 
	  //} else if (Language.isDefined(nodeType)) { 

	} else { 
      errorMessages << "Line ${node.'#line'}: Unknown element '${nodeType}'"
    }

    if (node instanceof Composite) { 
      node.children.each { w -> check(w) }
    }
  }

} 
