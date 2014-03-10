package edu.depaul.madl.wizards.constants;

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
}
