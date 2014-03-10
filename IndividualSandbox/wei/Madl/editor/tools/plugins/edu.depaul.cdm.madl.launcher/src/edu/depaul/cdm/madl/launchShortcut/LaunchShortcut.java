package edu.depaul.cdm.madl.launchShortcut;

import java.io.File;

import org.eclipse.core.internal.resources.ICoreConstants;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.Launch;
import org.eclipse.debug.core.model.ISourceLocator;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.debug.ui.JavaUISourceLocator;
import org.eclipse.jdt.launching.AbstractJavaLaunchConfigurationDelegate;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.JavaLaunchDelegate;
import org.eclipse.jdt.launching.sourcelookup.JavaSourceLocator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.jdt.internal.launching.JavaSourceLookupDirector;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

import edu.depaul.cdm.madl.launch.MadlLaunch;

public class LaunchShortcut extends AbstractJavaLaunchConfigurationDelegate implements ILaunchShortcut {

	@Override
	public void launch(ISelection selection, String mode) {
		// TODO Auto-generated method stub
		/*
		System.out.println("444444");
		
		ILaunchManager mgr = DebugPlugin.getDefault().getLaunchManager();
//		ILaunchConfigurationType lct = mgr.getLaunchConfigurationType();
//		ILaunchConfiguration[] lcs = mgr.getLaunchConfigurations(lct);
		try {
			ILaunchConfiguration[] conf = mgr.getLaunchConfigurations();
			System.out.println("@@@" + conf[0].toString());
			
			ILaunch[] confLaunch = mgr.getLaunches();
//			System.out.println("!!!" + confLaunch[0].toString());
			
			//get IRecource:
		      IStructuredSelection ss = (IStructuredSelection) selection;
		      Object element = ss.getFirstElement();
		      IAdaptable adaptable = (IAdaptable)element;
		      Object adapter = adaptable.getAdapter(IResource.class);
		      IResource resource = (IResource) adapter;
		      IProject project = resource.getProject();
			
		      //set ISourceLocator
		      JavaSourceLookupDirector sourceLocator = new JavaSourceLookupDirector();
		      sourceLocator.initializeDefaults(conf[0]);
		      sourceLocator.initializeParticipants();
			
			ILaunch confL = new Launch(conf[0], mode,sourceLocator );
			
			MadlLaunch launcher = new MadlLaunch();
			launcher.launch(conf[0], mode, confL, null);
			
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ERRRRROOOOOORRRRR");
		}
		*/
		
//		setUpConf();
		oneClickUpConf(selection, mode);
	}
	
	private void oneClickUpConf(ISelection selection, String mode){		
		IStructuredSelection ss = (IStructuredSelection) selection;
	    Object element = ss.getFirstElement();
	    IAdaptable adaptable = (IAdaptable)element;
	    Object adapter = adaptable.getAdapter(IResource.class);
	    IResource resource = (IResource) adapter;
	    IProject project = resource.getProject();
	    String projectName = project.getName();
	    String filePath = project.getProject().getLocation().toString();
	    File folder = new File(filePath+"/src");
	    File[] listOfFiles = folder.listFiles();
	    String madlFileName = listOfFiles[0].getName();
	    
		try {
			ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
			
			ILaunchConfigurationType type = manager.getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
			ILaunchConfiguration[] configurations = manager.getLaunchConfigurations(type);
			for(int i=0; i<configurations.length; i++){
				ILaunchConfiguration configuration = configurations[i];
				if(configuration.getName().equals(projectName)){
					configuration.delete();
					break;
				}
			}
			ILaunchConfigurationWorkingCopy workingCopy = type.newInstance(null, projectName);
			workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, projectName);
			workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, "xj.mobile.Main");
			workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, "src/"+madlFileName+" org.conf");
			ILaunchConfiguration configuration = workingCopy.doSave();
			DebugUITools.launch(configuration, mode);
			
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	private void setUpConf(){
		String mode = "run";
		System.out.println("111111");
		try {
			ILaunchConfiguration configuration = this.getLaunchManager().getLaunchConfigurations()[0];
//			ILaunch[] launcher = this.getLaunchManager().getLaunches();
//			JavaSourceLookupDirector sourceLocator = new JavaSourceLookupDirector();
			IJavaProject project = this.getJavaProject(configuration);
			JavaUISourceLocator sourceLocator = new JavaUISourceLocator(project);
//		    sourceLocator.initializeDefaults(configuration);
			ILaunch launch = new Launch(configuration, mode, sourceLocator);
//			ILaunch launch = this.getLaunchManager().getLaunches()[0];
			launch(configuration, mode, launch, null);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void createConsole(){
		
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		// TODO Auto-generated method stub
		System.out.println("Wei,Wei,NanChang");

	}

	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		// TODO Auto-generated method stub
		System.out.println("222222");
//		ISourceLocator locator = launch.getSourceLocator();
		JavaLaunchDelegate javaLauncher = new JavaLaunchDelegate();
		javaLauncher.launch(configuration, mode, launch, monitor);
	}
}
