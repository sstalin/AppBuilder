package xj.mobile.codegen.templates

/*
 * Managing all templates for iOS
 *
 * Singleton
 */
class IOSTemplates extends Templates {

  //
  // Singleton Pattern
  //
 
  static public IOSTemplates getInstance() { 
	if (theInstance == null) { 
	  theInstance = new IOSTemplates()
	}
	return theInstance
  }

  private IOSTemplates() {
	widgetTemplates = WidgetTemplates.getWidgetTemplates('ios')
	popupTemplates = PopupTemplates.getPopupTemplates('ios')

	templatesMap = [
	  Default:    IOSDefaultViewTemplates.templates, 
	  PageView:   IOSPageViewTemplates.templates, 
	  ListView:   IOSListViewTemplates.templates, 
	  NavView:    IOSNavigationViewTemplates.templates, 
	  TabbedView: IOSTabbedViewTemplates.templates, 

	  Picker:     IOSPickerTemplates.templates, 
	  Web:        IOSWebTemplates.templates, 
	]
 }

  static private IOSTemplates theInstance = null

}