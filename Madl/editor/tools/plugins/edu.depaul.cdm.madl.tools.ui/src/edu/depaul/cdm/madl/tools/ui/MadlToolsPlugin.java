package edu.depaul.cdm.madl.tools.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;

import edu.depaul.cdm.madl.tools.ui.text.MadlTextTools;

public class MadlToolsPlugin extends AbstractUIPlugin {
	
	// The plug-in ID
	  public static final String PLUGIN_ID = "edu.depaul.cdm.madl.tools.ui"; //$NON-NLS-1$

	  // The shared instance
	  private static MadlToolsPlugin plugin;
	  private MadlTextTools madlTextTools;
	  
	  /**
	   * Returns the shared instance
	   * 
	   * @return the shared instance
	   */
	  public static MadlToolsPlugin getDefault() {
	    return plugin;
	  }
	  
	  /**
	   * The constructor
	   */
	  public MadlToolsPlugin() {
	    plugin = this;
	  }
	 
	  
	  public synchronized MadlTextTools getMadlTextTools() {
	    if (madlTextTools == null) {
	    	//SS setting preferences to null explicitly
	      madlTextTools = new MadlTextTools(
	          getPreferenceStore()
	         );
	    }
	    return madlTextTools;
	  }
}
