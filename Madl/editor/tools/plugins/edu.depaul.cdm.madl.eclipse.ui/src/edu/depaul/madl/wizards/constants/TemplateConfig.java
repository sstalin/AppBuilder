package edu.depaul.madl.wizards.constants;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import org.eclipse.core.runtime.CoreException;

/**
 * Template names and descriptions. 
 * 
 * The index here should match the index in the template list on the TemplateSelectionPage
 * of the New Project Wizard in order for the correct display of the description
 * and correct addition of the template to the project.
 *
 */
public class TemplateConfig {

	private static String[] templateDescriptions = {
		"An app with one screen showing a label, two buttons, and a \n"
				+ "text input labeled with Name.",
		"An app with a yellow background, buttons, and name and address \n"
				+ "input fields.",
		"A shopping list app where the user can manage quantities and add, \n"
				+ "edit, and delete items."
	};
	
	private static String[] templateFilename = {
		"enter_name_app.madl",
		"name_address_input.madl",
		"shopping_list.madl"
	};
	
	public static String getDescription(int index) {
		return templateDescriptions[index];
	}
	
	public static String getFilename(int index) {
		return templateFilename[index];
	}
	
	public static void getTemplateDisplayNames() {
		Properties templateProperties = new Properties();		
		URL url;
		
		try {
		    url = new URL("platform:/plugin/edu.depaul.cdm.madl.eclipse.ui.madlprojectwizard/templates/templates.properties");
		    InputStream inputStream = url.openConnection().getInputStream();
		    templateProperties.load(inputStream);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		for ( Enumeration<?> e = templateProperties.propertyNames(); e.hasMoreElements(); ) {
			System.out.println(e.nextElement());
		}
	}
}
