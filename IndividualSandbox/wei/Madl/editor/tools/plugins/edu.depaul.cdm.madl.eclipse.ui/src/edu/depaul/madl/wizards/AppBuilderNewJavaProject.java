package edu.depaul.madl.wizards;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.core.filesystem.*;

import edu.depaul.madl.wizards.constants.ProjectFilenames;


public class AppBuilderNewJavaProject extends WizardNewProjectCreationPage{
	
	private String pageName = "New AppBuilder Madl Project";
	
	public AppBuilderNewJavaProject(){
		super("AppBuilder New Project Page");
		System.out.println("Java Project Creation Page");		
		setTitle(pageName);
		setDescription("Enter a project name");
	}
	
	
	public void createJavaProject() throws CoreException{		
		//create a java project
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(getProjectName());
		project.create(null);
		project.open(null);
				 
		//set the Java nature
		IProjectDescription description = project.getDescription();
		description.setNatureIds(new String[] { JavaCore.NATURE_ID });
		 
		//create the project
		project.setDescription(description, null);
		IJavaProject javaProject = JavaCore.create(project);
		 
		//set the build path
		IClasspathEntry[] buildPath = {
				JavaCore.newSourceEntry(project.getFullPath().append("src")),
						JavaRuntime.getDefaultJREContainerEntry() };
		 
		javaProject.setRawClasspath(buildPath, project.getFullPath().append(
						"bin"), null);
		 
		//create folder by using resources package
		IFolder folder = project.getFolder("src");
		folder.create(true, true, null);
		
		IFile file = folder.getFile(ProjectFilenames.MADL_FILE);		
		insertSampleApp(file);
		
		//create the rest of folder
//		IFolder f_conf = project.getFolder("conf");
//		f_conf.create(true, true, null);
//		
//		IFolder f_templete = project.getFolder("templete");
//		f_templete.create(true, true, null);
//		
//		IFolder f_lib = project.getFolder("lib");
//		f_lib.create(true, true, null);
//		
//		IFolder f_sound = project.getFolder("sound_video");
//		f_sound.create(true, true, null);
		
//		//refer org.properties file
//		IPath properties_loc = new Path("/Users/WeiLoveLisha/Projects/AppBuilder_dev/test/org.properties");
//		IFile f_org = project.getFile(properties_loc.lastSegment());
//		f_org.createLink(properties_loc, NONE, null);
//		
//		//refer conf folder
//		IPath conf_loc = new Path("/Users/WeiLoveLisha/Projects/AppBuilder_dev/conf");
//		IFolder f_conf = project.getFolder(conf_loc.lastSegment());
//		f_conf.createLink(conf_loc, NONE, null);
//		
//		//refer lib folder
//		IPath lib_loc = new Path("/Users/WeiLoveLisha/Projects/AppBuilder_dev/lib");
//		IFolder f_lib = project.getFolder(lib_loc.lastSegment());
//		f_lib.createLink(lib_loc, NONE, null);
//		
//		//refer template foler
//		IPath tem_loc = new Path("/Users/WeiLoveLisha/Projects/AppBuilder_dev/templates");
//		IFolder f_tem = project.getFolder(tem_loc.lastSegment());
//		f_tem.createLink(tem_loc, NONE, null);
		IFileSystem fileSystem = EFS.getLocalFileSystem();
		try {
			IFileStore homeDir = fileSystem.getStore(new URI("/Users/WeiLoveLisha/Projects/AppBuilder_dev"));
			IFileStore backupDir = fileSystem.getStore(getLocationURI());
			homeDir.copy(backupDir, EFS.NONE, null);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void insertSampleApp(IFile file) {
		URL url;
		try {
			// Get the input from the template file
		    url = new URL("platform:/plugin/edu.depaul.cdm.madl.eclipse.ui.madlprojectwizard/templates/enter_name_app");
		    InputStream inputStream = url.openConnection().getInputStream();
		    
		    System.out.println("Inserting sample app...");		    		    
		    file.create(inputStream, true, null);
		 
		} catch (IOException e) {
	        System.err.println("Error in insertSampleApp method while opening template");
		    e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
}
