//
//  File:    SectionedListItemAdapter.java
//  Project: ___PROJECTNAME___
//
//  Created by ___FULLUSERNAME___ on ___DATE___.
//  Copyright ___YEAR___ ___ORGANIZATIONNAME___. All rights reserved.
//

package ___PACKAGE___;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ArrayAdapter;
import java.util.List;
import java.util.LinkedList;

public class SectionedListItemAdapter<T> extends BaseAdapter {
	
  public final static int TYPE_SECTION_HEADER = 0;  
	
  List<Adapter> sections = new LinkedList<Adapter>();  
  ArrayAdapter<String> headers; 
  String[] titles;
	
  public SectionedListItemAdapter(Context context,
								  int headerViewResourceId,
								  int itemViewResourceId,
								  String[] titles,
								  T[][] objects) {  
    this.titles = titles;
    headers = new ArrayAdapter<String>(context, headerViewResourceId, titles);  
    int n = titles.length;
    if (n == objects.length) {
      for (int i = 0; i < n; i++) {
		sections.add(new ArrayAdapter<T>(context, itemViewResourceId, objects[i]));
      }
    }
  } 

  public SectionedListItemAdapter(Context context,
								  int headerViewResourceId,
								  int itemViewResourceId,
								  String[] titles,
								  List<List<T>> objects) {  
    this.titles = titles;
    headers = new ArrayAdapter<String>(context, headerViewResourceId, titles);  
    int n = titles.length;
    if (n == objects.size()) {
      for (int i = 0; i < n; i++) {
		sections.add(new ArrayAdapter<T>(context, itemViewResourceId, objects.get(i)));
      }
    }
  } 

  protected SectionedListItemAdapter() { }

  @Override
  public int getCount() {
    int n = sections.size();
    int count = 0;
    for (int i = 0; i < n; i++) {
      count += sections.get(i).getCount();
    }
    for (int i = 0; i < n; i++) {
      if (titles[i] != null) count++;
    }
    return count;
  }

  @Override
  public Object getItem(int position) {
    int n = sections.size();
    int count = 0;
    for (int i = 0; i < n; i++) {
      int secCount = sections.get(i).getCount() + (titles[i] != null ? 1 : 0);
      if (position < count + secCount) {
		if (titles[i] != null) {
		  if (position == count) {
			return headers.getItem(i);
		  } else {
			return sections.get(i).getItem(position - count - 1);
		  }
		} else {
		  return sections.get(i).getItem(position - count);
		}
      }
      count += secCount;
    }
    return null;
  }
	
  public int getViewTypeCount() {  
    int n = sections.size();
    return n + 1;  
  }  
  
  public int getItemViewType(int position) {  
    int n = sections.size();
    int count = 0;
    for (int i = 0; i < n; i++) {
      int secCount = sections.get(i).getCount() + (titles[i] != null ? 1 : 0);
      if (position < count + secCount) {
		if (titles[i] != null && position == count) {
		  return TYPE_SECTION_HEADER;
		} else {
		  return i + 1;
		}
      }
      count += secCount;
    }
    return -1;  
  }  
  
  public boolean areAllItemsSelectable() {  
    return false;  
  }  
  
  public boolean isEnabled(int position) {  
    return (getItemViewType(position) != TYPE_SECTION_HEADER);  
  }  
	
  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    int n = sections.size();
    int count = 0;
    for (int i = 0; i < n; i++) {
      int secCount = sections.get(i).getCount() + (titles[i] != null ? 1 : 0);
      if (position < count + secCount) {
		if (titles[i] != null) {
		  if (position == count) {
			return headers.getView(i, convertView, parent);
		  } else {
			return sections.get(i).getView(position - count - 1, convertView, parent);
		  }
		} else { 
		  return sections.get(i).getView(position - count, convertView, parent);
		}
      }
      count += secCount;
    }
    return null;
  }

  public int getSectionCount() {
	return sections.size();
  }

  public int inSection(int position) {
	int n = sections.size();
	if (position >= 0) {
	  int count = 0;
	  for (int i = 0; i < n; i++) {
		int secCount = sections.get(i).getCount() + (titles[i] != null ? 1 : 0);
		if (position < count + secCount) {
		  return i;
		}
		count += secCount;
	  }
	}
    return -1;
  }

  public int firstInSection(int section) {
	int n = sections.size();
	if (section >= 0 && section < n) {
	  int count = 0;
	  for (int i = 0; i < section; i++) {		
		int secCount = sections.get(i).getCount() + (titles[i] != null ? 1 : 0);
		count += secCount;
	  }
	  if (titles[section] != null) {
		return count + 1;
	  } else {
		return count; 
	  }
	}
    return -1;
  }

  public int lastInSection(int section) {
	int n = sections.size();
	if (section >= 0 && section < n) {
	  int count = 0;
	  for (int i = 0; i <= section; i++) {
		int secCount = sections.get(i).getCount() + (titles[i] != null ? 1 : 0);
		count += secCount;
	  }
	  return count - 1; 
	}
    return -1;
  }

  public ArrayAdapter getAdapter(int section) {
	int n = sections.size();
	if (section >= 0 && section < n) {
	  return (ArrayAdapter<T>) sections.get(section);
	} 
	return null;
  }

}

