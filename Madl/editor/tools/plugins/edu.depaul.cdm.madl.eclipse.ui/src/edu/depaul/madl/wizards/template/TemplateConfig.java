package edu.depaul.madl.wizards.template;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

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
	
	public static String[] getTemplateDisplayNames() {
		Properties templateProperties = new Properties();		
		URL url;
		
		// Open the properties file
		try {
		    url = new URL("platform:/plugin/edu.depaul.cdm.madl.eclipse.ui.madlprojectwizard/templates/templates.properties");
		    InputStream inputStream = url.openConnection().getInputStream();
		    templateProperties.load(inputStream);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		// Get a list of template display names and sort them
		List<TemplateDisplayName> templateDisplayNames = sortedTemplateDisplayNames(templateProperties);
		
		// Return the template display names as an array as required by the SWT List widget
		return templateDisplayNamesArray(templateDisplayNames);
	}

	private static List<TemplateDisplayName> sortedTemplateDisplayNames(Properties templateProperties) {
		List<TemplateDisplayName> templateDisplayNames = new ArrayList<TemplateDisplayName>();
		
		for (String property : templateProperties.stringPropertyNames()) {
			templateDisplayNames.add(new TemplateDisplayName(
										getDisplayName(templateProperties.getProperty(property)), 
										getOrder(templateProperties.getProperty(property))));
		}
		
		Collections.sort(templateDisplayNames);
		
		return templateDisplayNames;
	}
	
	private static String[] templateDisplayNamesArray(
			List<TemplateDisplayName> templateDisplayNames) {
		
		String[] displayNames = new String[templateDisplayNames.size()];
		
		for (int i = 0; i < templateDisplayNames.size(); i++) {
			displayNames[i] = templateDisplayNames.get(i).getDisplayName();
		}
		
		return displayNames;
	}

	private static String getDisplayName(String value) {
		int firstSemicolon = value.indexOf(';');
		int secondSemicolon = StringUtils.ordinalIndexOf(value, ";", 2);
		return value.substring(firstSemicolon + 1, secondSemicolon);
	}

	private static int getOrder(String value) {
		return Integer.parseInt(value.substring(0, value.indexOf(';')));
	}
}
