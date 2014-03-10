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
	
	private String iosOutputDir;
	private String androidOutputDir;
	
	private boolean isDefaultIosOutputDir;
	private boolean isDefaultAndroidOutputDir;
	
	private final String ORG_PROPERTIES_FILENAME = "org.conf";
	
	public OrgPropertiesFile() {}
		
	public OrgPropertiesFile(String developerName, String developerOrg,
			String developerDomain, boolean platformIosEnabled,
			String platformIosVersion, boolean platformAndroidEnabled,
			String platformAndroidVersion, 
			String iosOutputDir, String androidOutputDir,
			boolean isDefaultIosOutputDir, boolean isDefaultAndroidOutputDir) {
		setDeveloperName(developerName);
		setDeveloperOrg(developerOrg);
		setDeveloperDomain(developerDomain);
		setPlatformIosEnabled(platformIosEnabled);
		setPlatformIosVersion(platformIosVersion);
		setPlatformAndroidEnabled(platformAndroidEnabled);
		setPlatformAndroidVersion(platformAndroidVersion);
		setIosOutputDir(iosOutputDir);
		setAndroidOutputDir(androidOutputDir);
		setDefaultIosOutputDir(isDefaultIosOutputDir);
		setDefaultAndroidOutputDir(isDefaultAndroidOutputDir);
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

	public String getIosOutputDir() {
		return iosOutputDir;
	}

	public void setIosOutputDir(String iosOutputDir) {
		this.iosOutputDir = iosOutputDir;
	}

	public String getAndroidOutputDir() {
		return androidOutputDir;
	}

	public void setAndroidOutputDir(String androidOutputDir) {
		this.androidOutputDir = androidOutputDir;
	}

	public boolean isDefaultIosOutputDir() {
		return isDefaultIosOutputDir;
	}

	public void setDefaultIosOutputDir(boolean isDefaultIosOutputDir) {
		this.isDefaultIosOutputDir = isDefaultIosOutputDir;
	}

	public boolean isDefaultAndroidOutputDir() {
		return isDefaultAndroidOutputDir;
	}

	public void setDefaultAndroidOutputDir(boolean isDefaultAndroidOutputDir) {
		this.isDefaultAndroidOutputDir = isDefaultAndroidOutputDir;
	}

	public void generateOrgPropertiesFile() {
		System.out.println("Generating org.properties file from configuration");
		
		File orgPropertiesFile = new File(
				AppBuilderConfiguration.getInstance().getProject().getLocation() + File.separator + ORG_PROPERTIES_FILENAME);
		FileWriter writer = null;
		
		try {
			orgPropertiesFile.createNewFile();
			writer = new FileWriter(orgPropertiesFile);
			writeDeveloperConfig(writer);
			writer.write("platform {\n");
			writeIosConfig(writer);
			writeAndroidConfig(writer);
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

	private void writeDeveloperConfig(FileWriter writer) throws IOException {
		writer.write("\ndeveloper {\n");
		writer.write("\tname = \"" + getDeveloperName() + "\"\n");
		writer.write("\torg = \"" + getDeveloperOrg() + "\"\n");
		writer.write("\tdomain = \"" + getDeveloperDomain() + "\"\n");
		writer.write("}\n\n");
	}
	
	private void writeIosConfig(FileWriter writer) throws IOException {
		if (isPlatformIosEnabled()) {
			writer.write("\tios {\n");
			writer.write("\t\tversion = " + getPlatformIosVersion() + "\n");
			writeIosOutputDir(writer);
			writer.write("\t}\n");
		}
	}

	private void writeIosOutputDir(FileWriter writer) throws IOException {
		if (!isDefaultIosOutputDir()) {
			writer.write("\t\toutput.dir = " + getIosOutputDir() + "\n");
		}
	}

	private void writeAndroidConfig(FileWriter writer) throws IOException {
		if (isPlatformAndroidEnabled()) {
			writer.write("\tandroid {\n");
			writer.write("\t\tversion = " + getPlatformAndroidVersion() + "\n");
			writeAndroidOutputDir(writer);
			writer.write("\t}\n");
		}
	}

	private void writeAndroidOutputDir(FileWriter writer) throws IOException {
		if (!isDefaultAndroidOutputDir()) {
			writer.write("\t\toutput.dir = " + getAndroidOutputDir() + "\n");
		}
	}
}
