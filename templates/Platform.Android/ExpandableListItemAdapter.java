//
//  File:    ExpandableListItemAdapter.java
//  Project: ___PROJECTNAME___
//
//  Created by ___FULLUSERNAME___ on ___DATE___.
//  Copyright ___YEAR___ ___ORGANIZATIONNAME___. All rights reserved.
//

package ___PACKAGE___;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import ___PACKAGE___.R;

public class ExpandableListItemAdapter<T> extends BaseExpandableListAdapter {

  LayoutInflater mInflater;
  int headerViewResourceId;
  int itemViewResourceId;	
  String[] titles;
  T[][] objects;
        	

  public ExpandableListItemAdapter(Context context,
				   int headerViewResourceId,
				   int itemViewResourceId, 
				   String[] titles,
				   T[][] objects) {
    this.titles = titles;
    this.objects = objects;
    this.headerViewResourceId = headerViewResourceId;
    this.itemViewResourceId = itemViewResourceId;
    mInflater = LayoutInflater.from(context);
  }

  @Override
  public Object getChild(int groupPosition, int childPosition) {
    return objects[groupPosition][childPosition];
  }

  @Override
  public long getChildId(int groupPosition, int childPosition) {
    return childPosition;
  }
    
  @Override
  public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
			   View convertView, ViewGroup parent) {
    T[] items = objects[groupPosition];      	
    if (childPosition >= 0 && childPosition < items.length) {
      T item = items[childPosition];
      if (convertView == null) {
	convertView = mInflater.inflate(itemViewResourceId, null);
      }
      TextView tv = (TextView) convertView;
      tv.setText(item.toString());
    }
    return convertView;
  }

  @Override
  public int getChildrenCount(int groupPosition) {
    return objects[groupPosition].length;
  }

  @Override
  public Object getGroup(int groupPosition) {
    return objects[groupPosition];
  }

  @Override
  public int getGroupCount() {
    return objects.length; 
  }

  @Override
  public long getGroupId(int groupPosition) {
    return groupPosition;
  }

  @Override
  public View getGroupView(int groupPosition, boolean isExpanded,
			   View convertView, ViewGroup parent) {    	
    String title = titles[groupPosition];
    if (convertView == null) {
      convertView = mInflater.inflate(headerViewResourceId, null);
    }
    TextView tv = (TextView) convertView;
    tv.setText(title);
    return convertView;
  }
  
  @Override
  public boolean hasStableIds() {
    return true;
  }

  @Override
  public boolean isChildSelectable(int arg0, int arg1) {
    return true;
  }
    
  @Override
  public boolean areAllItemsEnabled() {
    return true;
  }

}