package appbuildersandbox;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

public class AppBuilderProjectPage extends WizardNewProjectCreationPage {

//	private Composite container;
	private String pageName = "New AppBuilder Project";

	public AppBuilderProjectPage() {
		super("AppBuilder New Project Page");
		
		System.out.println("AppBuilderProjectPage()");
		
		setTitle(pageName);
		setDescription("Enter a project name");
	}
	
	@Override
	public void createControl(Composite parent) {
		System.out.println("AppBuilderProjectPage createControl");
		
		// inherit default container and name specification widgets
		super.createControl(parent);
//		Composite composite = (Composite)getControl();
	}
	
	public boolean performFinish() {
		System.out.println("AppBuilderProjectPage performFinish()");
		System.out.println("Project name: " + getProjectName());
		
		doFinish(getProjectName(), getProjectHandle());
		
		return true;
	}
	
	private void doFinish(String projectName, IProject project) {
		System.out.println("AppBuilderProjectPage doFinish");
		
		IProgressMonitor iProgressMonitor = null; // OK to pass null to create and open project
												  // TODO, learn how to use it instead of passing null
		
		// Create and open an empty project
		try {
			project.create(iProgressMonitor);
			project.open(iProgressMonitor);
		} catch (CoreException e) {
			System.err.println("doFinish CoreException");
			e.printStackTrace();
		}
	}
}
