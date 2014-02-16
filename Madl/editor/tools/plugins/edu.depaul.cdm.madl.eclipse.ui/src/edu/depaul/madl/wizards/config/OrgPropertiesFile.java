package edu.depaul.madl.wizards.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import edu.depaul.madl.wizards.AppBuilderConfiguration;

/**
 * Represents the org.properties configuration file. The data members represent information
 * gathered on Page 2 of the New Project Wizard. The generateOrgPropertiesFile method creates
 * the org.properties file with the user's input.
 *
 */
public class OrgPropertiesFile {
	
	private String developerName;
	private String developerOrg;
	private String developerDomain;
	
	private boolean platformIosEnabled;
	private String platformIosVersion;
	
	private boolean platformAndroidEnabled;
	private String platformAndroidVersion;
	
	private final String ORG_PROPERTIES_FILENAME = "org.properties";
		
	public OrgPropertiesFile(String developerName, String developerOrg,
			String developerDomain, boolean platformIosEnabled,
			String platformIosVersion, boolean platformAndroidEnabled,
			String platformAndroidVersion) {
		setDeveloperName(developerName);
		setDeveloperOrg(developerOrg);
		setDeveloperDomain(developerDomain);
		setPlatformIosEnabled(platformIosEnabled);
		setPlatformIosVersion(platformIosVersion);
		setPlatformAndroidEnabled(platformAndroidEnabled);
		setPlatformAndroidVersion(platformAndroidVersion);
	}
	
	public String getDeveloperName() {
		return developerName;
	}
	
	public void setDeveloperName(String developerName) {
		this.developerName = developerName;
	}
	
	public String getDeveloperOrg() {
		return developerOrg;
	}
	
	public void setDeveloperOrg(String developerOrg) {
		this.developerOrg = developerOrg;
	}
	
	public String getDeveloperDomain() {
		return developerDomain;
	}
	
	public void setDeveloperDomain(String developerDomain) {
		this.developerDomain = developerDomain;
	}
	
	public boolean isPlatformIosEnabled() {
		return platformIosEnabled;
	}
	
	public void setPlatformIosEnabled(boolean platformIosEnabled) {
		this.platformIosEnabled = platformIosEnabled;
	}
	
	public String getPlatformIosVersion() {
		return platformIosVersion;
	}
	
	public void setPlatformIosVersion(String platformIosVersion) {
		this.platformIosVersion = platformIosVersion;
	}
	
	public boolean isPlatformAndroidEnabled() {
		return platformAndroidEnabled;
	}
	
	public void setPlatformAndroidEnabled(boolean platformAndroidEnabled) {
		this.platformAndroidEnabled = platformAndroidEnabled;
	}
	
	public String getPlatformAndroidVersion() {
		return platformAndroidVersion;
	}
	
	public void setPlatformAndroidVersion(String platformAndroidVersion) {
		this.platformAndroidVersion = platformAndroidVersion;
	}

	public void generateOrgPropertiesFile() {
		System.out.println("Generating org.properties file from configuration");
		
		File orgPropertiesFile = new File(
				AppBuilderConfiguration.getInstance().getProject().getLocation() + File.separator + ORG_PROPERTIES_FILENAME);
		FileWriter writer = null;
		
		try {
			orgPropertiesFile.createNewFile();
			writer = new FileWriter(orgPropertiesFile);
			writer.write("\ndeveloper {\n");
			writer.write("\tname = \"" + getDeveloperName() + "\"\n");
			writer.write("\torg = \"" + getDeveloperOrg() + "\"\n");
			writer.write("\tdomain = \"" + getDeveloperDomain() + "\"\n");
			writer.write("}\n\n");
			writer.write("platform {\n");
			
			if (isPlatformIosEnabled()) {
				writer.write("\tios {\n");
				writer.write("\t\tversion = " + getPlatformIosVersion() + "\n");
				writer.write("\t}\n");
			}
			
			if (isPlatformAndroidEnabled()) {
				writer.write("\tandroid {\n");
				writer.write("\t\tversion = " + getPlatformAndroidVersion() + "\n");
				writer.write("\t}\n");
			}
			
			writer.write("}");
		} catch (Exception ex) {
			System.err.println("Error writing to org.properties file!");
			ex.printStackTrace();
		} finally {
			try { 
				writer.close(); 
			} catch (IOException ex) {
				System.err.println("Error closing FileWriter!");
				ex.printStackTrace();
			}
		}
	}
}
