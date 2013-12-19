package xj.mobile.android

import xj.mobile.*
import xj.mobile.model.ui.*
import xj.mobile.lang.*

import xj.mobile.common.ViewProcessor
import xj.mobile.common.ViewHierarchyProcessor

import static xj.mobile.android.AndroidAppGenerator.*
import static xj.mobile.util.CommonUtil.*
import static xj.mobile.common.ViewProcessor.*

class TimePickerProcessor { 

  void process(Widget widget, ViewProcessor vp) { 
    String actionCode = null
    if (widget['selection'] instanceof Closure) { 	
	  actionCode = vp.generator.generateActionCode(vp, widget['selection.src'], widget)
    }
    if (actionCode) { 
      vp.classModel.imports << 'java.text.DateFormat' << 'java.util.Calendar' << 'java.util.Date'

      vp.classModel.onCreateScrap += """
final Calendar c = Calendar.getInstance();
final int year = c.get(Calendar.YEAR);
final int month = c.get(Calendar.MONTH);
final int day = c.get(Calendar.DAY_OF_MONTH);
${widget.id}.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

    public void onTimeChanged(TimePicker view, int hour, int minute) {
        String time = DateFormat.getTimeInstance(DateFormat.MEDIUM).format(new Date(year, month, day, hour, minute));
${indent(actionCode, 2, '    ')}
  }

});
"""
    }

  }

}