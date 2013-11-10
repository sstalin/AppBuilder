
package com.example.e4.rcp.todo.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.IWorkbench;

//import statements cut out
//..

public class ExitHandler {
@Execute
public void execute(IWorkbench workbench) {
 workbench.close();
}

// NOT REQUIRED IN THIS EXAMPLE
// just to demonstrates the usage of
// the annotation
@CanExecute
public boolean canExecute() {
 return true;
}

} 


