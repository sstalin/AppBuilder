package edu.depaul.madl.wizards;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

import edu.depaul.madl.wizards.config.OrgPropertiesFile;
import edu.depaul.madl.wizards.constants.ProjectFilenames;
import edu.depaul.madl.wizards.pages.AppBuilderNewJavaProject;
import edu.depaul.madl.wizards.pages.PageTwo;
import edu.depaul.madl.wizards.pages.TemplateSelectionPage;
import edu.depaul.madl.wizards.template.TemplateConfig;

public class AppBuilderNewProjectWizard extends Wizard implements IWorkbenchWizard {
	
//	protected AppBuilderProjectPage firstPage;
	protected AppBuilderNewJavaProject firstPage;
	protected PageTwo pageTwo;
	protected TemplateSelectionPage templateSelectionPage;
	
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
		pageTwo = new PageTwo();
		templateSelectionPage = new TemplateSelectionPage();
		
		addPage(firstPage);
		addPage(pageTwo);
		addPage(templateSelectionPage);
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
					pageTwo.getDeveloperName(), pageTwo.getDeveloperOrg(), 
					pageTwo.getDeveloperDomain(), pageTwo.isIosEnabled(), pageTwo.getIosVersion(), 
					pageTwo.isAndroidEnabled(), pageTwo.getAndroidVersion(),
					pageTwo.getIosDestinationDir(), pageTwo.getAndroidDestinationDir(),
					pageTwo.isDefaultIosOutputDir(), pageTwo.isDefaultAndroidOutputDir());
			orgPropertiesFile.generateOrgPropertiesFile();
			
			addTemplate();
			
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
	
	private void addTemplate() {
		IFolder folder = AppBuilderConfiguration.getInstance().getProject().getFolder("src");
		
		// If a template was selected, put it in the src directory
		if (templateSelectionPage.isTemplateSelected()) {
			URL url;
//			IFile file = folder.getFile(TemplateConfig.getFilename(templateSelectionPage.getSelectedTemplateIndex()));
			IFile file = folder.getFile(TemplateConfig.getTemplates().get(templateSelectionPage.getSelectedTemplateIndex()).getFilename());
			try {
				// Get the input from the template file
			    url = new URL("platform:/plugin/edu.depaul.cdm.madl.eclipse.ui.madlprojectwizard/templates/" + 
			    		TemplateConfig.getTemplates().get(templateSelectionPage.getSelectedTemplateIndex()).getFilename());
			    InputStream inputStream = url.openConnection().getInputStream();
			    System.out.println("Adding template app...");		    		    
			    file.create(inputStream, true, null);
			} catch (IOException e) {
		        System.err.println("Error in addTemplate method while opening template");
			    e.printStackTrace();
			} catch (CoreException e) {
				e.printStackTrace();
			}
		} else {	// Otherwise, create an empty app.madl source file
			try {
				folder.getFile(ProjectFilenames.MADL_FILE).create(new ByteArrayInputStream(new byte[0]), true, null);
			} catch (CoreException e) {
				System.err.println("Error creating an empty app.madl file");
				e.printStackTrace();
			}
		}
	}

}
