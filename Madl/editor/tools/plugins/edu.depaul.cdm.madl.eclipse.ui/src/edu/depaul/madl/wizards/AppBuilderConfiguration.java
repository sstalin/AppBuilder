package edu.depaul.madl.wizards;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.IProject;

import edu.depaul.madl.wizards.constants.AppBuilderDTO;

public class AppBuilderConfiguration {

	private static IProject project;
	private static AppBuilderConfiguration config;
	
	private AppBuilderConfiguration(){}
	
	public static AppBuilderConfiguration getInstance() {
		if (config == null) {
			config = new AppBuilderConfiguration();
		}
		return config;
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		AppBuilderConfiguration.project = project;
	}
	
	public void copyAppBuilderFiles() {
		// Files and directories in the project
		File projectConfDir = new File(getProject().getLocation() + File.separator + "conf");
		File projectLibApiDir = new File(getProject().getLocation() + File.separator + "lib/api");
		File projectTemplatesDir = new File(getProject().getLocation() + File.separator + "templates");
		File projectOrgPropertiesFile = new File(getProject().getLocation() + File.separator + "org.properties");
		
		try {
			// Local AppBuilder files and directories
			File localConfDir = new File(AppBuilderDTO.APP_BUILDER_SOURCE_ROOT_DIRECTORY + "/conf");
			File localLibApiDir = new File(AppBuilderDTO.APP_BUILDER_SOURCE_ROOT_DIRECTORY + "/lib/api");
			File localTemplateDir = new File(AppBuilderDTO.APP_BUILDER_SOURCE_ROOT_DIRECTORY + "/templates");
			File localOrgPropertiesFile = new File(AppBuilderDTO.APP_BUILDER_SOURCE_ROOT_DIRECTORY + "/test/org.properties");
			
			// Copy AppBuilder from local location to the Eclipse project
			FileUtils.copyDirectory(localConfDir, projectConfDir);
			FileUtils.copyDirectory(localLibApiDir, projectLibApiDir);
			FileUtils.copyDirectory(localTemplateDir, projectTemplatesDir);
			FileUtils.copyFile(localOrgPropertiesFile, projectOrgPropertiesFile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
