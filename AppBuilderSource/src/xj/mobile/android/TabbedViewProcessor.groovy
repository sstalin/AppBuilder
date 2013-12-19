package xj.mobile.android

import groovy.xml.MarkupBuilder

import xj.mobile.*
import xj.mobile.model.ui.*
import xj.mobile.lang.*

import xj.mobile.common.ViewProcessor

import static xj.mobile.android.AndroidAppGenerator.*
import static xj.mobile.util.CommonUtil.*
import static xj.translate.Logger.info 

class TabbedViewProcessor extends DefaultViewProcessor { 

  public TabbedViewProcessor(View view, String viewName = null) { 
    super(view, viewName)
    classModel.superClassName = 'TabActivity'
  }

  public void process() { 
    info "xj.mobile.android.TabbedViewProcessor.process() ${viewName}"

    if (view) { 
	  currentViewProcessor = this

      classModel.onCreateScrap += '''
TabHost tabHost = getTabHost();  
TabHost.TabSpec spec;
'''

      view.children.each { Widget widget -> 
	if (Language.isTopView(widget.widgetType)) {  
	  String name = widget.id
	  //topViews << name

	  def indicator = "\"${widget.title ?: widget.id}\""
	  if (widget.tabImage) { 
	    indicator += ", getResources().getDrawable(R.drawable.${getFileName(widget.tabImage)})"
	    //classModel.imageFiles << widget.tabImage
		classModel.addImageFile(widget.tabImage)
	  }

	  classModel.onCreateScrap += """
spec = tabHost.newTabSpec(\"${name}\");
spec.setIndicator(${indicator});
spec.setContent(new Intent(this, ${widget.viewProcessor.viewName}.class));
tabHost.addTab(spec);
"""
	}	
      }
      
      generateViewLayout()
    }
  }

  void generateViewLayout() { 
    def layoutFilename = isAndroidMainView(this) ? 'main' : viewName.toLowerCase()

    classModel.onCreateScrap = """setContentView(R.layout.${layoutFilename});
""" + classModel.onCreateScrap 

    classModel.resources.viewLayouts[layoutFilename] = generateTabLayoutXML(view) 
  }

  String generateTabLayoutXML(view) { 
    def writer = new StringWriter()
    writer.println "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
    def xml = new MarkupBuilder(writer)    
    xml.setDoubleQuotes(true)

    xml.TabHost('xmlns:android': XMLNS_ANDROID,
		'android:id': '@android:id/tabhost',
		'android:layout_width': 'fill_parent',
		'android:layout_height': 'fill_parent') { 
      LinearLayout('android:orientation': 'vertical',
		   'android:layout_width': 'fill_parent',
		   'android:layout_height': 'fill_parent',
		   'android:padding': '5dp') { 
        TabWidget('android:id': '@android:id/tabs',
		  'android:layout_width': 'fill_parent',
		  'android:layout_height': 'wrap_content')
        FrameLayout('android:id': '@android:id/tabcontent',
		    'android:layout_width': 'fill_parent',
		    'android:layout_height': 'fill_parent',
		    'android:padding': '5dp')
      }
    }

    def text = writer.toString()
    info text
    return text
  }

}
