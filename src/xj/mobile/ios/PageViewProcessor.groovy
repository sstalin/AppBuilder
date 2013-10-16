package xj.mobile.ios

import xj.mobile.*
import xj.mobile.common.*
import xj.mobile.model.ui.*
import xj.mobile.lang.*

import static xj.mobile.ios.IOSAppGenerator.*
import static xj.mobile.util.CommonUtil.*

import static xj.mobile.model.impl.ViewControllerClass.getViewControllerName

@Mixin(xj.mobile.common.PageViewCategory)
class PageViewProcessor extends DefaultViewProcessor { 

  def pages = []

  public PageViewProcessor(View view, String viewName = null) { 
    super(view, viewName)
    processPageView()
  }

  void initializeTopView() { 
    def cases = []
    pages.eachWithIndex { view, i ->  
	  String uiclass = getViewControllerName(view.viewProcessor.viewName)

	  classModel.addImport(uiclass)
      cases << "case ${i}: controller = [[${uiclass} alloc] init]; break;" 
    }
    String pageAllocCode = """switch (page) {
${cases.join('\n')}
}"""

	def binding = [
	  numberOfPages: pages.size(),
	  pageAllocCode: pageAllocCode, 
	]

	generator.injectCodeFromTemplateRef(classModel, "PageView:pageview", binding)
  }

}
