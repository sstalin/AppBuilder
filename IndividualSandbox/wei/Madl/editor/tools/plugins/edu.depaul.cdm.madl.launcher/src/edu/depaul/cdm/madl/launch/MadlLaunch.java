package edu.depaul.cdm.madl.launch;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.debug.core.model.ISourceLocator;
import org.eclipse.jdt.launching.AbstractJavaLaunchConfigurationDelegate;
import org.eclipse.jdt.launching.JavaLaunchDelegate;

import edu.depaul.madl.wizards.AppBuilderConfiguration;


public class MadlLaunch extends AbstractJavaLaunchConfigurationDelegate implements ILaunchConfigurationDelegate {

//	AppBuilderConfiguration config;
	
	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		// TODO Auto-generated method stub
		ISourceLocator locator = launch.getSourceLocator();
		JavaLaunchDelegate javaLauncher = new JavaLaunchDelegate();
		
		//launch
		String attr = launch.getAttribute(configuration.toString());
		
		javaLauncher.launch(configuration, mode, launch, monitor);
//		javaLauncher.launch(configuration, mode, launch, null);
		
//		AppBuilderConfiguration.getInstance().getProject().getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
	}

}
