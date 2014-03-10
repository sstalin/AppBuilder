package edu.depaul.madl.wizards.constants;

public class AppPlatform {
	
	/**
	 * A single accessor to hold iOS versions that are automatically populated
	 * in the New Project Wizard. These should be in ascending order for the
	 * dropdown to be populated accordingly and for the latest to be selected.
	 * 
	 * @return String array of iOS versions
	 */
	public static String[] getIOsVersions() {
		return new String[] {
				"5", "6", "7"
		};
	}
	
	/**
	 * A single accessor to hold Android versions that are automatically populated
	 * in the New Project Wizard. These should be in ascending order for the
	 * dropdown to be populated accordingly and for the latest to be selected.
	 * 
	 * @return String array of Android versions
	 */
	public static String[] getAndroidVersions() {
		return new String[] {
				"2.3", "4.0", "4.0.3", "4.1.2", "4.2.2", "4.3", "4.4"
		};
	}
}
