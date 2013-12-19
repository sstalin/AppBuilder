package edu.depaul.cdm.madl.tabGroup;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaArgumentsTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaClasspathTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaJRETab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaMainTab;

public class TabGroup extends AbstractLaunchConfigurationTabGroup {

	ILaunchConfigurationTab[] tabs;

	public TabGroup() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		// TODO Auto-generated method stub

		tabs = new ILaunchConfigurationTab[]{
				new JavaMainTab(),
				new JavaArgumentsTab(),
				new JavaJRETab(),
				new JavaClasspathTab(),
				new CommonTab(),
//				new JavaSourceLookupTab(),
		};
	}
	
	@Override
	public void dispose(){
		
	}
	
	@Override
	public void launched(ILaunch launch){
		
	}
	
	@Override
	public ILaunchConfigurationTab[] getTabs() {
		return tabs;
	}
	
	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		tabs[0].initializeFrom(configuration);
		tabs[1].initializeFrom(configuration);
		tabs[2].initializeFrom(configuration);
		tabs[3].initializeFrom(configuration);
		tabs[4].initializeFrom(configuration);
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		tabs[0].performApply(configuration);
		tabs[1].performApply(configuration);
		tabs[2].performApply(configuration);
		tabs[3].performApply(configuration);
		tabs[4].performApply(configuration);
	}
	
	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		tabs[0].setDefaults(configuration);
		tabs[1].setDefaults(configuration);
		tabs[2].setDefaults(configuration);
		tabs[3].setDefaults(configuration);
		tabs[4].setDefaults(configuration);
	}
}
