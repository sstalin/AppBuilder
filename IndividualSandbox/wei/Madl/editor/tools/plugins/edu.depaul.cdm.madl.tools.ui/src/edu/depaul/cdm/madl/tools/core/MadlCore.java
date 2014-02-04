package edu.depaul.cdm.madl.tools.core;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Plugin;

/**
 * @author sstalin
 *
 */
public class MadlCore extends Plugin{
	  /**
	   * The unique instance of this class.
	   */
	  private static MadlCore PLUG_IN;
	  /**
	   * The log used by {@link #logError(String)} and other local methods for logging errors, warnings,
	   * and information or <code>null</code> to use the default system log.
	   */
	  private static ILog PLUGIN_LOG;

	  /**
	   * Flag indicating whether instrumentation logging of errors is enabled
	   */
	  private static boolean instrumentationLogErrorEnabled = true;

	  /**
	   * The id of the plug-in that defines the Dart model.
	   */
	  public static final String PLUGIN_ID = MadlCore.class.getPackage().getName();
}
