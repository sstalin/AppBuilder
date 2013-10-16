package xj.mobile.codegen.templates

import static xj.mobile.codegen.CodeGenerator.InjectionPoint.*

class AndroidListViewTemplates { 

  static templates = [
	adapter: [
	  [ 
		import: 'android.content.Context' 
	  ],
	  [ 
		declaration: '''static class ListItemAdapter extends ArrayAdapter<${itemType}> {
    ${itemsType} items;
    private LayoutInflater mInflater;
		
    public ListItemAdapter(Context context, int itemViewResourceId, ${itemsType} items) {
        super(context, itemViewResourceId, items); 
        this.items = items;
        mInflater = LayoutInflater.from(context);
    }
	    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.${listItemLayout}, null);
            holder = new ViewHolder();
${indent(initViewHolder, 3, '    ')}
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position >= 0 && position < items.${length}) {
            ${itemType} item = items${getPosition};
${indent(setViewHolder, 3, '    ')}
        }
        return convertView;
    }
		
}
'''
	  ]
	],

	sectionAdapter: [
	  declaration: '''static class SectionAdapter extends SectionedListItemAdapter<${itemType}> {
		
    public SectionAdapter(Context context,
                          int headerViewResourceId,
                          int itemViewResourceId,
                          String[] titles,
                          ${sectionType} objects) { 
        this.titles = titles; 
        headers = new ArrayAdapter<String>(context, headerViewResourceId, titles);  
        int n = titles.length;
        if (n == objects.${length}) {
            for (int i = 0; i < n; i++) {
                sections.add(new ListItemAdapter(context, itemViewResourceId, objects${get_i}));
            }
        }
    } 

}
'''
	], 

	startActivity1: [
	  code: '''${itemType} item = (${itemType}) adapter.getItem(position);
if (item.next != null) {
    Intent intent = new Intent(${thisActivity}.this, item.next);
    intent.putExtra(item.next.getSimpleName().toUpperCase() + \"_DATA\", item.data); 
    startActivity(intent);
}'''
	],

	startActivity2: [
	  code: '''${itemType} item = (${itemType}) adapter.getItem(position);
if (item.next != null)
    startActivity(new Intent(${thisActivity}.this, item.next));'''
	],

	checkbox1: [
	  code: '''int prevPos = -1;
CheckedTextView prevView = null;
'''
	],

	checkbox2: [
	  code: '''CheckedTextView ctv = getCheckedTextView(view);
if (prevPos != position) {
    if (prevView != null) {
        prevView.setChecked(false);
        ((${itemType}) ${listVar}.getAdapter().getItem(prevPos)).${contentHandler.setItemAttributeScrap(\'checked\', \'false\')};
    }
    ctv.setChecked(true);
    ((${itemType}) ${listVar}.getAdapter().getItem(position)).${contentHandler.setItemAttributeScrap(\'checked\', \'true\')};
    prevView = ctv;
    prevPos = position;
}'''
	],

	checkbox3: [
	  code: '''CheckedTextView ctv = getCheckedTextView(view);
ctv.toggle();	      
((${itemType}) ${listVar}.getAdapter().getItem(position)).${contentHandler.setItemAttributeScrap(\'checked\', \'ctv.isChecked()\')};'''
	],

	checkbox4: [
	  declaration: '''public CheckedTextView getCheckedTextView(View itemView) { 
    return (CheckedTextView) ((ViewGroup) itemView).getChildAt(0).findViewById(R.id.text_${viewid});
}
'''
	],



	//
	// Expandable list
	//
	
	exp_adapter: [
	  [	
		import: 'android.content.Context' 
	  ],
	  [	
		declaration: '''static class ${name} extends ${superClass}<ListItem> {

    public ${name}(Context context, 
                          int headerViewResourceId,
                          int itemViewResourceId,
                          String[] titles,
                          ListItem[][] objects) {
        super(context, headerViewResourceId, itemViewResourceId, titles, objects);
    }
        
    @Override
    public View getChildView(int groupPosition,
                             int childPosition, 
                             boolean isLastChild,
                             View convertView,
                             ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(itemViewResourceId, null);
            holder = new ViewHolder();
${indent(initViewHolder, 3, '    ')}
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (groupPosition >= 0 && groupPosition < objects.length) {
            ListItem[] items = objects[groupPosition];      	
            if (childPosition >= 0 && childPosition < items.length) {
                ListItem item = items[childPosition];
${indent(setViewHolder, 4, '    ')}
            }
        }
        return convertView;
    }

}
''' 
	  ],
	],

	exp_select: [
	  code: '''ListItem item = (ListItem) adapter.getChild(groupPosition, childPosition);
if (item.next != null) {
    startActivity(new Intent(${viewName}.this, item.next));
    return true;
}
return false;'''
	], 

	exp_startActivity: [
	  code: '''startActivity(new Intent(${viewName}.this, ${act}.class));
return true;'''
	]

  ]

}