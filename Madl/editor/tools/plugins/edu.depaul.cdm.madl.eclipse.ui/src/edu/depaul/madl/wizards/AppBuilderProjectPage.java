package edu.depaul.madl.wizards;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

import edu.depaul.madl.wizards.constants.ProjectFilenames;

public class AppBuilderProjectPage extends WizardNewProjectCreationPage {

	private String pageName = "New AppBuilder Project";
	private IProgressMonitor iProgressMonitor = null; 	// OK to pass null to create and open project
														// TODO, learn how to use it instead of passing null

	public AppBuilderProjectPage() {
		super("AppBuilder New Project Page");
		System.out.println("AppBuilderProjectPage()");		
		setTitle(pageName);
		setDescription("Enter a project name");
	}
	
	@Override
	public void createControl(Composite parent) {
		System.out.println("AppBuilderProjectPage createControl");
		super.createControl(parent);
	}
	
	public boolean performFinish() {
		System.out.println("AppBuilderProjectPage performFinish()");
		System.out.println("Project name: " + getProjectName());
		
		createProject();
		setupProjectStructure();
		refreshProject();
		insertSampleApp();
		
		return true;
	}

	private void setupProjectStructure() {
		createMadlFile();
		createPropertiesFile();
		createConfDirectory();
		createSystemConfFile();
		createImageDirectory();
		createSoundVideoDirectory();
		createLibDirectory();
		createGenDirectory();
		createIosDirectory();
		createAndroidDirectory();
	}

	private void createProject() {
		System.out.println("Creating project location: " + getProjectHandle().getLocation());
		try {
			getProjectHandle().create(iProgressMonitor);
			getProjectHandle().open(iProgressMonitor);
		} catch (CoreException ex) {
			System.err.println("Error creating project: " + getProjectHandle().toString());
		}
	}

	private void createMadlFile() {
		File madlFile = new File(getProjectHandle().getLocation() + 
				File.separator + "src" + File.separator + ProjectFilenames.MADL_FILE);
		try {
			madlFile.getParentFile().mkdirs();
			madlFile.createNewFile();
		} catch (IOException ex) {
			System.err.println("Error creating the .madl project file: " + madlFile.toString());
			ex.printStackTrace();
		}
	}
	
	private void createPropertiesFile() {
		File propertiesFile = new File(getProjectHandle().getLocation() + 
				File.separator + ProjectFilenames.PROPERTIES_FILE);
		try {
			propertiesFile.createNewFile();
		} catch (IOException ex) {
			System.err.println("Error creating the .properties file: " + propertiesFile.toString());
			ex.printStackTrace();
		}
	}
	
	private void createConfDirectory() {
		File confDirectory = new File(getProjectHandle().getLocation() + 
				File.separator + ProjectFilenames.CONF_DIRECTORY);
		confDirectory.mkdir();
	}
	
	private void createSystemConfFile() {
		File systemConfFile = new File(getProjectHandle().getLocation() + 
				File.separator + ProjectFilenames.CONF_DIRECTORY + File.separator + 
				ProjectFilenames.SYSTEM_CONF_FILE);
		try {
			systemConfFile.createNewFile();
		} catch (IOException ex) {
			System.err.println("Error creating the system config file: " + systemConfFile.toString());
			ex.printStackTrace();
		}
	}
	
	private void createImageDirectory() {
		File imageDirectory = new File(getProjectHandle().getLocation() + 
				File.separator + ProjectFilenames.IMAGE_DIRECTORY);
		imageDirectory.mkdir();
	}
	
	private void createSoundVideoDirectory() {
		File soundVideoDirectory = new File(getProjectHandle().getLocation() + 
				File.separator + ProjectFilenames.SOUND_VIDEO_DIRECTORY);
		soundVideoDirectory.mkdir();
	}
	
	private void createLibDirectory() {
		File libDirectory = new File(getProjectHandle().getLocation() + 
				File.separator + ProjectFilenames.LIB_DIRECTORY);
		libDirectory.mkdir();
	}
	
	private void createGenDirectory() {
		File genDirectory = new File(getProjectHandle().getLocation() + 
				File.separator + ProjectFilenames.GEN_DIRECTORY);
		genDirectory.mkdir();
	}
	
	private void createIosDirectory() {
		File iosDirectory = new File(getProjectHandle().getLocation() + 
				File.separator + ProjectFilenames.GEN_DIRECTORY + File.separator + 
				ProjectFilenames.IOS_DIRECTORY);
		iosDirectory.mkdir();
	}
	
	private void createAndroidDirectory() {
		File androidDirectory = new File(getProjectHandle().getLocation() + 
				File.separator + ProjectFilenames.GEN_DIRECTORY + File.separator + 
				ProjectFilenames.ANDROID_DIRECTORY);
		androidDirectory.mkdir();
	}
	
	private void refreshProject() {
		try {
			getProjectHandle().refreshLocal(IProject.DEPTH_INFINITE, iProgressMonitor);
		} catch (CoreException er) {
			System.err.println("Error refreshing the project: " + getProjectName());
		}
	}
	
	private void insertSampleApp() {
		URL url;
		try {
			// Get the input from the template file
		    url = new URL("platform:/plugin/edu.depaul.cdm.madl.eclipse.ui.madlprojectwizard/templates/enter_name_app");
		    InputStream inputStream = url.openConnection().getInputStream();
		    BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
		    
		    System.out.println("Inserting sample app...");		    
		    writeSampleToMadlFile(input);
		 
		} catch (IOException e) {
	        System.err.println("Error in insertSampleApp method while opening template");
		    e.printStackTrace();
		}
	}

	private void writeSampleToMadlFile(BufferedReader input) throws IOException {
		String inputLine;
		
		// Get the file
		File madlFile = new File(getProjectHandle().getLocation() + 
				File.separator + "src" + File.separator + ProjectFilenames.MADL_FILE);
		
		Writer writer = null;
		
		try {
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(madlFile), "utf-8"));
		    
		    // Write each line of the template to the madl file
		    while ((inputLine = input.readLine()) != null) {
		    	writer.write(inputLine);
		    	writer.write("\n");
		    }
		} catch (IOException ex) {
			  System.err.println("Error in method writeSampleToMadlFile while writing template file...");
			  ex.printStackTrace();
		} finally {
			   try {writer.close();} catch (Exception ex) {}
		}
 
		input.close();
	}
}
