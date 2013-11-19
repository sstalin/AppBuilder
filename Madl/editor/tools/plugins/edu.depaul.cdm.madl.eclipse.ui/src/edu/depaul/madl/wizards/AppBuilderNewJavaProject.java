package edu.depaul.madl.wizards;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;


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
		IFile file = folder.getFile("app01.madl");
		
		//change string to input string
				InputStream stream = null;
				try {
					stream = new ByteArrayInputStream(madl01().getBytes("UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    file.create(stream, true, null);
		
		//create the rest of folder
		IFolder f_conf = project.getFolder("conf");
		f_conf.create(true, true, null);
		
		IFolder f_templete = project.getFolder("templete");
		f_templete.create(true, true, null);
		
		IFolder f_lib = project.getFolder("lib");
		f_lib.create(true, true, null);
		
		IFolder f_sound = project.getFolder("sound_video");
		f_sound.create(true, true, null);
	}
	
	private String madl01(){
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("/* Android Specific */\n");
		strBuilder.append("app(name : \'Android App\') { \n");
		strBuilder.append("	View(id: top) { \n");
		strBuilder.append("    Label(id: l1, text: \'Hello\')\n");
		strBuilder.append("    CheckBox(id: c1, text: 'Item 1')\n");
		strBuilder.append("    CheckBox(id: c2, text: 'Item 2')\n");
		strBuilder.append("    RadioButton(id: r1, text: 'Radio 1')\n");
		strBuilder.append("    RadioButton(id: r2, text: 'Radio 2')\n");
		strBuilder.append("    RadioGroup () { \n");
		strBuilder.append("      RadioButton(id: r11, text: 'Choice 1')\n");
		strBuilder.append("      RadioButton(id: r12, text: 'Choice 2')\n");
		strBuilder.append("    }\n");
		strBuilder.append("  }\n");
		strBuilder.append("}\n");
		return strBuilder.toString();
	}
}
