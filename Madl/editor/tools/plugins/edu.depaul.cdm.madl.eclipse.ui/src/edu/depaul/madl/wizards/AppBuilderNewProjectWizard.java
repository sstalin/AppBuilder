package edu.depaul.madl.wizards;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

public class AppBuilderNewProjectWizard extends Wizard implements IWorkbenchWizard {
	
//	protected AppBuilderProjectPage firstPage;
	protected AppBuilderNewJavaProject firstPage;
	
	protected IWorkbench workbench;
	protected IStructuredSelection selection;
	
	public AppBuilderNewProjectWizard() {
		super();
		
		System.out.println("AppBuilderNewProjectWizard()");
		
		setWindowTitle("New AppBuilder Project");
	}

	public void addPages() {
		System.out.println("AppBuilderNewProjectWizard addPages()");
		
//		firstPage = new AppBuilderProjectPage();
		firstPage = new AppBuilderNewJavaProject();
		addPage(firstPage);
	}

	@Override
	public boolean performFinish() {
		System.out.println("AppBuilderNewProjectWizard performFinish()");
//		return firstPage.performFinish();
		try {
//			new AppBuilderNewJavaProject().createJavaProject();
			firstPage.createJavaProject();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		System.out.println("AppBuilderNewProjectWizard init()");
		
		this.workbench = workbench;
	    this.selection = selection;
	}

}
