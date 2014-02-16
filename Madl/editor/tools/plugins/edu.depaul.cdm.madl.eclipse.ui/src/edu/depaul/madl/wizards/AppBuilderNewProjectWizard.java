package edu.depaul.madl.wizards;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

import edu.depaul.madl.wizards.config.OrgPropertiesFile;
import edu.depaul.madl.wizards.pages.AppBuilderNewJavaProject;
import edu.depaul.madl.wizards.pages.NewProjectWizardPageTwo;
import edu.depaul.madl.wizards.pages.PageTwo;

public class AppBuilderNewProjectWizard extends Wizard implements IWorkbenchWizard {
	
//	protected AppBuilderProjectPage firstPage;
	protected AppBuilderNewJavaProject firstPage;
	protected PageTwo pageTwo;
	protected NewProjectWizardPageTwo page2;
	
	protected IWorkbench workbench;
	protected IStructuredSelection selection;
	private IProgressMonitor progressMonitor = null;
		
	public AppBuilderNewProjectWizard() {
		super();
		
		System.out.println("AppBuilderNewProjectWizard()");
		
		setWindowTitle("New AppBuilder Project");
	}

	public void addPages() {
		System.out.println("AppBuilderNewProjectWizard addPages()");
		
//		firstPage = new AppBuilderProjectPage();
		firstPage = new AppBuilderNewJavaProject();
//		pageTwo = new PageTwo();
		page2 = new NewProjectWizardPageTwo();
		
		addPage(firstPage);
//		addPage(pageTwo);
		addPage(page2);
		
	}

	@Override
	public boolean performFinish() {
		System.out.println("AppBuilderNewProjectWizard performFinish()");
//		return firstPage.performFinish();
		try {
//			new AppBuilderNewJavaProject().createJavaProject();
			firstPage.createJavaProject();
			AppBuilderConfiguration.getInstance().copyAppBuilderFiles();
			
			OrgPropertiesFile orgPropertiesFile = new OrgPropertiesFile(
					page2.getDeveloperName(), page2.getDeveloperOrg(), 
					page2.getDeveloperDomain(), page2.isIosEnabled(), page2.getIosVersion(), 
					page2.isAndroidEnabled(), page2.getAndroidVersion());
			orgPropertiesFile.generateOrgPropertiesFile();
			
			AppBuilderConfiguration.getInstance().getProject().refreshLocal(IResource.DEPTH_INFINITE, progressMonitor);
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
