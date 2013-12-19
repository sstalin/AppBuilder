package com.example.e4.rcp.todo.parts;

import javax.inject.Inject;

import org.eclipse.swt.widgets.Composite;

public class TodoOverviewPart {
  
  @Inject 
  public TodoOverviewPart(Composite parent) {
    
  // assuming that dependency injection works 
  // parent will never be null
  System.out.println("Woh! Got a Composite via DI.");
  
  // does it have a layout manager?
  System.out.println("Layout: " + parent.getLayout().getClass());
  }
} 