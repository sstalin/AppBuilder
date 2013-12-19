package xj.mobile.android

import xj.mobile.*
import xj.mobile.model.ui.*
import xj.mobile.lang.*

import xj.mobile.util.PrettyMarkupBuilder

class ImageLayoutProcessor { 

  void process(Widget widget, 
			   PrettyMarkupBuilder mainLayout,
			   String uiclass,
			   Map attributes) { 
	if (widget.scroll) { 
	  attributes['android:scaleType'] = 'center'
	  mainLayout.HorizontalScrollView('android:layout_width': 'wrap_content', 
									  'android:layout_height': 'wrap_content') { 
		ScrollView('android:layout_width': 'wrap_content', 
				   'android:layout_height': 'wrap_content') { 
		  "${uiclass}"(attributes)
		}
	  } 
	} else { 
	  mainLayout."${uiclass}"(attributes)
	}
  }

}