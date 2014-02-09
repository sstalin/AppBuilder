package edu.depaul.cdm.madl.eclipse.ui;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>,
 * we can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */
public class AppBuilderPreferencesPage extends FieldEditorPreferencePage
										implements IWorkbenchPreferencePage {

    public AppBuilderPreferencesPage() {
    	super(GRID);
    }
    
    public void createFieldEditors() {
    	addField(new DirectoryFieldEditor("APP_BUILDER_HOME", "&AppBuilder Home Directory:", getFieldEditorParent()));
    }

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("AppBuilder project configurations");
	}
}