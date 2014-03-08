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
 * Templates can be added by putting them in the madlprojectwizard templates directory
 * and configuring the filename, order, display name, and description the templates.properties file
 *
 */
public class TemplateConfig {
	
	private static List<Template> templates;
	private static String[] templateDescriptions;
	
	public static String[] getTemplateDescriptions() {
		return templateDescriptions;
	}

	public static void setTemplateDescriptions(String[] templateDescriptions) {
		TemplateConfig.templateDescriptions = templateDescriptions;
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
		
		// Get a list of templates and sort them
		templates = sortedTemplates(templateProperties);
		
		// As a side-effect, create an array of descriptions for the SWT Label
		setTemplateDescriptions(templateDescriptionsArray(templates));
		
		// Return the template display names as an array as required by the SWT List widget
		return templateDisplayNamesArray(templates);
	}
	
	public static List<Template> getTemplates() {
		return templates;
	}

	private static List<Template> sortedTemplates(Properties templateProperties) {
		List<Template> templates = new ArrayList<Template>();
		
		for (String property : templateProperties.stringPropertyNames()) {
			templates.add(new Template(	property,
										getDisplayName(templateProperties.getProperty(property)), 
										getOrder(templateProperties.getProperty(property)),
										getDescription(templateProperties.getProperty(property))));
		}
		
		Collections.sort(templates);
		
		return templates;
	}

	private static String[] templateDisplayNamesArray(List<Template> templates) {	
		String[] displayNames = new String[templates.size()];
		
		for (int i = 0; i < templates.size(); i++) {
			displayNames[i] = templates.get(i).getDisplayName();
		}
		
		return displayNames;
	}
	
	private static String[] templateDescriptionsArray(List<Template> templates) {
		String[] descriptions = new String[templates.size()];
		
		for (int i = 0; i < descriptions.length; i++) {
			descriptions[i] = templates.get(i).getDescription();
		} 
		
		return descriptions;
	}

	private static String getDisplayName(String value) {
		int firstSemicolon = value.indexOf(';');
		int secondSemicolon = StringUtils.ordinalIndexOf(value, ";", 2);
		return value.substring(firstSemicolon + 1, secondSemicolon);
	}

	private static int getOrder(String value) {
		return Integer.parseInt(value.substring(0, value.indexOf(';')));
	}
	
	private static String getDescription(String value) {
		int secondSemicolon = StringUtils.ordinalIndexOf(value, ";", 2);
		return value.substring(secondSemicolon + 1);
	}
}
