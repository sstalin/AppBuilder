package com.example.e4.rcp.todo;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.workbench.swt.modeling.EMenuService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.viewers.TableViewer;

public class TodoOverviewPart {
  
  @Inject 
  public TodoOverviewPart(Composite parent) {
    
  // assuming that dependency injection works 
  // parent will never be null
  System.out.println("Woh! Got a Composite via DI.");
  
  // does it have a layout manager?
  System.out.println("Layout: " + parent.getLayout().getClass());
  }

  @PostConstruct
  public void createControls(Composite parent, EMenuService menuService) {
    // more code...
    TableViewer viewer = new TableViewer(parent, SWT.FULL_SELECTION | SWT.MULTI);
    
    // more code
    
    // register context menu on the table
    menuService.registerContextMenu(viewer.getControl(), 
        "com.example.e4.rcp.todo.popupmenu.table");
  }

@Focus
private void setFocus() {
 System.out.println(this.getClass().getSimpleName() 
     + " @Focus method called");
} 

  	

  	@PostConstruct
  	public void createControls(Composite parent) {
  	  System.out.println(this.getClass().getSimpleName() 
  	  + " @PostConstruct method called.");
  	} 
  	
  

} 