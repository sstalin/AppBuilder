package com.example.e4.rcp.todo;
import java.awt.Composite;

import javax.annotation.PostConstruct;

	// more code
public class TodoDetailsPart {

	

	@PostConstruct
	public void createControls(Composite parent) {
	  System.out.println(this.getClass().getSimpleName() 
	  + " @PostConstruct method called.");
	} 
	
}
