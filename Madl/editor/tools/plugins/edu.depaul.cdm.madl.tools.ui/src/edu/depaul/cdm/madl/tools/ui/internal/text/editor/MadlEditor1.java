package edu.depaul.cdm.madl.tools.ui.internal.text.editor;

import java.util.ArrayList;
import java.util.List;


import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;

import edu.depaul.cdm.madl.tools.ui.MadlToolsPlugin;
import edu.depaul.cdm.madl.tools.ui.text.MadlPartitions;
import edu.depaul.cdm.madl.tools.ui.text.MadlSourceViewerConfiguration;
import edu.depaul.cdm.madl.tools.ui.text.MadlTextTools;


/**
 * @author sstalin
 *
 */
public class MadlEditor1 extends AbstractDecoratedTextEditor {

	
	  /**
	   * Default constructor.
	   */
	  public MadlEditor1() {
	    super();
	  }

	@Override
	protected void initializeEditor() {
		IPreferenceStore store = createCombinedPreferenceStore(null);
		//IPreferenceStore store= createPreferenceStore();
		setPreferenceStore(store);
		setSourceViewerConfiguration(createMadlSourceViewerConfiguration());
}
	  
	  /**
	   * Returns a new Dart source viewer configuration.
	   * 
	   * @return a new <code>DartSourceViewerConfiguration</code>
	   */
	  protected MadlSourceViewerConfiguration createMadlSourceViewerConfiguration() {
		MadlTextTools textTools = MadlToolsPlugin.getDefault().getMadlTextTools();
	    return new MadlSourceViewerConfiguration(
	        textTools.getColorManager(),
	        getPreferenceStore(),
	        this,
	        MadlPartitions.MADL_PARTITIONING);
	  }
	  
	  /**
	   * Creates and returns the preference store for this Dart editor with the given input.
	   * 
	   * @param input The editor input for which to create the preference store
	   * @return the preference store for this editor
	   */
	  private IPreferenceStore createCombinedPreferenceStore(IEditorInput input) {
	    List<IPreferenceStore> stores = new ArrayList<IPreferenceStore>(3);

	  /*  IProject project = EditorUtility.getProject(input);
	    if (project != null) {
	      stores.add(new EclipsePreferencesAdapter(new ProjectScope(project), DartCore.PLUGIN_ID));
	    }

	    stores.add(new PreferencesAdapter(DartCore.getPlugin().getPluginPreferences()));*/
	    stores.add((new MadlToolsPlugin()).getDefault().getPreferenceStore());
	    stores.add(EditorsUI.getPreferenceStore());
	   
	    return new ChainedPreferenceStore(stores.toArray(new IPreferenceStore[stores.size()]));
	  }
	
}
