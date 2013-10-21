package xj.mobile.android

import xj.mobile.*
import xj.mobile.model.ui.*
import xj.mobile.lang.*

import xj.mobile.common.ViewProcessor
import xj.mobile.common.ViewHierarchyProcessor

import static xj.mobile.android.AndroidAppGenerator.*
import static xj.mobile.util.CommonUtil.*
import static xj.mobile.common.ViewProcessor.*

class SpinnerProcessor { 

  String dataVar
  String adapterVar

  void process(Widget widget, ViewProcessor vp) { 
    dataVar = "${widget.id}Data"
    adapterVar = "${widget.id}Adapter"

    def values = widget.options.collect{ "\"${it}\"" }.join(',\n')
    vp.classModel.declarationScrap += """
private static final String[] ${dataVar} = {
${indent(values, 1, '    ')}
};""" 

    vp.classModel.onCreateScrap += """
ArrayAdapter<String> ${adapterVar} = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ${dataVar});
${adapterVar}.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
${widget.id}.setAdapter(${adapterVar});
"""

    String actionCode = null
    if (widget['selection'] instanceof Closure) { 	
	  actionCode = vp.generator.generateActionCode(vp, widget['selection.src'], widget)
    } else if (widget['action'] instanceof Closure) { 	
	  actionCode = vp.generator.generateActionCode(vp, widget['action.src'], widget)
    }
    if (actionCode) { 
      vp.classModel.onCreateScrap += """
${widget.id}.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String item = parent.getItemAtPosition(pos).toString();
${indent(actionCode, 2, '    ')}
    }

    public void onNothingSelected(AdapterView parent) { }

});
"""
    }

  }

}