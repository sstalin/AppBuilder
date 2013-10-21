package com.myplugin.rmp.actions;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;


public class OpenResourceManagerViewActionDelegate implements IWorkbenchWindowActionDelegate {

        private IWorkbenchWindow window;
        public static final String ID = "com.myplugin.rmp.views.ResourceManagerView";


        public void init(IWorkbenchWindow window) {
           this.window = window; // cache the window object in which action delegate is operating
        }
        public void dispose() {}

        public void run(IAction action) {

                    IWorkbenchPage page = window.getActivePage();

                    try {

                       page.showView(ID); // use the Resource Manager View id to open up view.

                    } catch (PartInitException e) {
         
                    }
        }
        public void selectionChanged(IAction action, ISelection selection) {}
}

