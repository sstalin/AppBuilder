package com.myplugin.rmp;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import com.myplugin.rmp.wizards.CaptureEmployeeInfomrationWizard;

public class ResourceManagerViewActionDelegate implements IViewActionDelegate {
     private IViewPart view;

     public void init(IViewPart view) {
              this.view = view; // cache the view part, this will be used in run action
              					// to fetch the parent shell for dialog
     }


  /*   public void run(IAction action) {
              InputDialog dialog = new InputDialog(view.getSite().getShell(),"Lets try!",
              			"Please enter your name","",null); // new input dialog
              if( dialog.open()== IStatus.OK){ // open dialog and wait for return status code.
              					// If user clicks ok display message box
                  String value = dialog.getValue(); // fetch the value entered by the user.
                  MessageBox box = new MessageBox(view.getSite().getShell(),SWT.ICON_INFORMATION);
                  box.setMessage("Hey there! You entered : " + value);
                  box.open();
              }else{
                  MessageBox box = new MessageBox(view.getSite().getShell(),SWT.ICON_INFORMATION);
                  box.setMessage("Bye!");
                  box.open();
              }
     }
*/
public void run(IAction action) {
         CaptureEmployeeInfomrationWizard wizard = new CaptureEmployeeInfomrationWizard();
         WizardDialog dialog = new WizardDialog(view.getSite().getShell(), wizard);
         dialog.create();
         dialog.open();
}

     public void selectionChanged(IAction action, ISelection selection) {}
}

/*package com.myplugin.rmp;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;


public class ResourceManagerViewActionDelegate implements IViewActionDelegate {
    private IViewPart view;

    public void init(IViewPart view) {
             this.view = view;
    }

    public void run(IAction action) {
             MessageBox box = new MessageBox(view.getSite().getShell(),SWT.ICON_INFORMATION);
             box.setMessage("Hello! You clicked view action!");
             box.open();
    }
    public void selectionChanged(IAction action, ISelection selection) {}
}
*/
