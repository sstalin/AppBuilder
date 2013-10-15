package com.example.e4.rcp.todo.handlers;

import org.eclipse.e4.core.di.annotations.Execute;

public class SaveAllHandler {
  @Execute
  public void execute() {
    System.out.println((this.getClass().getSimpleName() + " called"));
  }
} 