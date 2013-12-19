package xj.mobile.codegen.templates

/*
 * Managing all templates for android
 *
 * Singleton
 */
class AndroidTemplates extends Templates {

  //
  // Singleton Pattern
  //
 
  static public AndroidTemplates getInstance() { 
	if (theInstance == null) { 
	  theInstance = new AndroidTemplates()
	}
	return theInstance
  }

  private AndroidTemplates() {
	widgetTemplates = WidgetTemplates.getWidgetTemplates('android')
	popupTemplates = PopupTemplates.getPopupTemplates('android')

	templatesMap = [
	  Default:    AndroidDefaultViewTemplates.templates, 
	  ListView:   AndroidListViewTemplates.templates, 

	  Web:        AndroidWebTemplates.templates, 
	  Map:        AndroidMapTemplates.templates, 
	]
 }

  static private AndroidTemplates theInstance = null

}