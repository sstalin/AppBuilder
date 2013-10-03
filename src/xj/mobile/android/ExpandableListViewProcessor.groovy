package xj.mobile.android

import xj.mobile.*
import xj.mobile.model.ui.*
import xj.mobile.lang.*

import xj.mobile.common.ViewProcessor
import xj.mobile.common.ViewHierarchyProcessor

import static xj.mobile.android.AndroidAppGenerator.*
import static xj.mobile.util.CommonUtil.*

@Mixin(xj.mobile.common.ListViewCategory)
class ExpandableListViewProcessor extends ListViewProcessor { 

  String expListAdapterClass

  public ExpandableListViewProcessor(View view, String viewName = null) { 
    super(view, viewName)

	if (!view.embedded || view.parent == null) { 
	  classModel.superClassName = 'ExpandableListActivity'
	}

    viewClass = 'ExpandableListView'
    adapterClass = 'ExpandableListItemAdapter'
	expListAdapterClass = 'ExpListAdapter'

    actionListenerImport = 'android.widget.ExpandableListView.OnChildClickListener'
    actionListener = 'OnChildClickListener'
    actionMethodSignature = 'boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id)'

    listHeaderAttributes['android:paddingLeft'] = '40dip' 
  }

  void handleListItemAdapter() { 
	def binding = [
	  name: expListAdapterClass,
	  superClass: adapterClass, 
	  initViewHolder: viewHolderMembers.collect{ 
          "holder.${it} = (${getViewHolderType(it)}) convertView.findViewById(R.id.${it}_${viewid});" 
	    }.join('\n'),
	  setViewHolder: viewHolderMembers.collect{ 
		  "holder.${it}.${viewHolderSetter[it]}(item.${it});"
	    }.join('\n')
	]
	generator.injectCodeFromTemplateRef(classModel, "ListView:exp_adapter", binding)
  }

  String selectListItemCode() { 
	def binding = [ viewName: viewName ]
	generator.instantiateCodeFromTemplateRef("ListView:exp_select", binding)
  }

  String selectNextCode(next, data = null) { 
    def act = next ? vhp.findViewProcessor(next)?.viewName : null
    if (act) { 
	  def binding = [ viewName: viewName, act: act ]
	  return generator.instantiateCodeFromTemplateRef("ListView:exp_startActivity", binding)
    } else 
      return null
  }

  String handleSectionAdapter() {
    expListAdapterClass
  }

} 