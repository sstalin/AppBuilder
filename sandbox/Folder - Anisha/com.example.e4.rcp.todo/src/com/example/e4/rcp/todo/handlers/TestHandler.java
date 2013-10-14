
package com.example.e4.rcp.todo.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;

public class TestHandler {
/*  @Execute
  public void execute() {
    System.out.println((this.getClass().getSimpleName() + " called"));
  }
  */
	
  @Execute
  public void execute(@Named("com.example.e4.rcp.todo.commandparameter.input") String param) {
    System.out.println(param);
  }
} 