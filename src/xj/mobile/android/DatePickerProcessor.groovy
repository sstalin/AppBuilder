package xj.mobile.android

import xj.mobile.*
import xj.mobile.model.ui.*
import xj.mobile.lang.*

import xj.mobile.common.ViewProcessor
import xj.mobile.common.ViewHierarchyProcessor

import static xj.mobile.android.AndroidAppGenerator.*
import static xj.mobile.util.CommonUtil.*
import static xj.mobile.common.ViewProcessor.*

class DatePickerProcessor { 

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
${widget.id}.init(year, month, day, new DatePicker.OnDateChangedListener() {

    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date(year, monthOfYear, dayOfMonth));
${indent(actionCode, 2, '    ')}
  }

});
"""
    }

  }

}