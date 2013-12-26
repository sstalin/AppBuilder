 /*
 * Copyright (c) 2012, the Madl project authors.
 * 
 * Licensed under the Eclipse Public License v1.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package edu.depaul.cdm.madl.tools.core;

/*import edu.depaul.cdm.madl.engine.AnalysisEngine;
import edu.depaul.cdm.madl.engine.error.ErrorCode;
import edu.depaul.cdm.madl.engine.utilities.instrumentation.Instrumentation;
import edu.depaul.cdm.madl.engine.utilities.instrumentation.InstrumentationBuilder;
import edu.depaul.cdm.madl.engine.utilities.logging.Logger;*/

import edu.depaul.cdm.madl.tools.core.analysis.model.ProjectManager;
import edu.depaul.cdm.madl.tools.core.analysis.model.PubFolder;

import edu.depaul.cdm.madl.tools.core.internal.MessageConsoleImpl;

/*import edu.depaul.cdm.madl.tools.core.internal.OptionManager;
import edu.depaul.cdm.madl.tools.core.internal.analysis.model.ProjectManagerImpl;
import edu.depaul.cdm.madl.tools.core.internal.builder.AnalysisMarkerManager;
import edu.depaul.cdm.madl.tools.core.internal.model.MadlIgnoreManager;
import edu.depaul.cdm.madl.tools.core.internal.util.Extensions;
import edu.depaul.cdm.madl.tools.core.internal.util.ResourceUtil;
import edu.depaul.cdm.madl.tools.core.internal.util.Util;
import edu.depaul.cdm.madl.tools.core.jobs.CleanLibrariesJob;
import edu.depaul.cdm.madl.tools.core.model.CompilationUnit;
import edu.depaul.cdm.madl.tools.core.model.MadlElement;
import edu.depaul.cdm.madl.tools.core.model.MadlIgnoreListener;
import edu.depaul.cdm.madl.tools.core.model.MadlLibrary;
import edu.depaul.cdm.madl.tools.core.model.MadlModel;
import edu.depaul.cdm.madl.tools.core.model.MadlModelException;
import edu.depaul.cdm.madl.tools.core.model.MadlProject;
import edu.depaul.cdm.madl.tools.core.model.MadlSdk;
import edu.depaul.cdm.madl.tools.core.model.MadlSdkListener;
import edu.depaul.cdm.madl.tools.core.model.MadlSdkManager;
import edu.depaul.cdm.madl.tools.core.model.ElementChangedListener;
import edu.depaul.cdm.madl.tools.core.utilities.general.StringUtilities;
import edu.depaul.cdm.madl.tools.core.utilities.performance.PerformanceManager;*/

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Version;
import org.osgi.service.prefs.BackingStoreException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Properties;

/**
 * The class <code>MadlCore</code> is used to access the elements modeling projects that have a Madl
 * nature.
 * 
 * @coverage madl.tools.core
 */
public class MadlCore extends Plugin {
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
   * The id of the plug-in that defines the Madl model.
   */
  public static final String PLUGIN_ID = MadlCore.class.getPackage().getName();

  /**
   * The id of the project nature used for Madl projects.
   */
  public static final String MADL_PROJECT_NATURE = PLUGIN_ID + ".madlNature";

  /**
   * The id of the builder used for Madl projects.
   */
  public static final String MADL_BUILDER_ID = PLUGIN_ID + ".madlBuilder";

  /**
   * The id of the content type used to represent Madl compilation units.
   */
  public static final String MADL_SOURCE_CONTENT_TYPE = PLUGIN_ID + ".madlSourceFile";

  /**
   * Eclipse problem marker type used to display Madl compilation errors.
   */
  public static final String MADL_PROBLEM_MARKER_TYPE = PLUGIN_ID + ".problem";

  /**
   * Eclipse problem marker type used to display Madl hints.
   */
  public static final String MADL_HINT_MARKER_TYPE = PLUGIN_ID + ".hint";

  /**
   * Eclipse problem marker type used to display todo markers.
   */
  public static final String MADL_TASK_MARKER_TYPE = PLUGIN_ID + ".task";

  /**
   * Extension for single unit compiled into JavaScript.
   */
  public static final String EXTENSION_JS = "js";

  /**
   * Preference for the automatically running pub.
   */
  public static final String PUB_AUTO_RUN_PREFERENCE = "pubAutoRun";

  /**
   * Preference for enabling hints.
   */
  public static final String ENABLE_HINTS_PREFERENCE = "enableHints";

  /**
   * Preference for enabling madl2js related hints.
   */
  public static final String ENABLE_HINTS_MADL2JS_PREFERENCE = "enableHints_madl2js";

  public static final String PROJECT_PREF_PACKAGE_ROOT = "projectPackageRoot";

  /**
   * Preference to control if "not a member" warnings should be reported for inferred types.
   */
  public static final String TYPE_CHECKS_FOR_INFERRED_TYPES = "typeChecksForInferredTypes";

  public static final String PROJECT_PREF_DISABLE_MADL_BASED_BUILDER = "disableMadlBasedBuilder";

  /**
   * Cached extensions for CSS files.
   */
  private static final String[] CSS_FILE_EXTENSIONS = {"css"};

  /**
   * Cached extensions for HTML files.
   */
  private static final String[] HTML_FILE_EXTENSIONS = {"html", "htm"};

  /**
   * Cached extensions for JS files.
   */
  private static final String[] JS_FILE_EXTENSIONS = {"js"};

  /**
   * Cached extensions for TXT files.
   */
  private static final String[] TXT_FILE_EXTENSIONS = {"txt"};

  /**
   * Cached extensions for files that are generated by the madlc compiler.
   */
  private static final String[] MADL_GENERATED_FILE_EXTENSIONS = {"api", "deps", "js", "map"};

  /**
   * Cached extensions for image files.
   */
  public static final String[] IMAGE_FILE_EXTENSIONS = {
      "bmp", "gif", "jpeg", "jpg", "png", "raw", "thm", "tif", "tiff"};

  /**
   * Name of directory for packages installed by pub
   */
  public static final String PACKAGES_DIRECTORY_NAME = "packages";

  /**
   * Name of "lib" directory in package
   */
  public static final String LIB_DIRECTORY_NAME = "lib";

  /**
   * Path string for packages directory, uses "/" as path separator, equals "/packages/".
   */
  public static final String PACKAGES_DIRECTORY_PATH = "/" + PACKAGES_DIRECTORY_NAME + "/";

  /**
   * Path string for packages directory in a url.
   */
  public static final String PACKAGES_DIRECTORY_URL = "/" + PACKAGES_DIRECTORY_NAME + "/";

  public static final String PACKAGE_SCHEME_SPEC = "package:";

  /**
   * Path string for lib directory in a url.
   */
  public static final String LIB_URL_PATH = "/lib/";

  /**
   * Name of pubspec file
   */
  public static final String PUBSPEC_FILE_NAME = "pubspec.yaml";

  /**
   * Name of special pubspec lock file
   */
  public static final String PUBSPEC_LOCK_FILE_NAME = "pubspec.lock";

  /**
   * The name of the build.madl special file.
   */
  public static final String BUILD_MADL_FILE_NAME = "build.madl";

  /**
   * The shared message console instance.
   */
  private static final MessageConsole CONSOLE = new MessageConsoleImpl();

  /**
   * The QualifiedName for a resource remapping (a.foo ==> a.bar).
   */
  private static final QualifiedName RESOURCE_REMAP_NAME = new QualifiedName(
      PLUGIN_ID,
      "resourceRemap");

  /**
   * The QualifiedName for the package version
   */
  public static final QualifiedName PUB_PACKAGE_VERSION = new QualifiedName(
      MadlCore.PLUGIN_ID,
      "pub.package.version");

  /**
   * The unique project manager used for analysis of anything in the workspace
   */
  //ss
  //private static ProjectManager projectManager;

  /**
   * Used to synchronize access to {@link #projectManager}.
   */
  private static final Object projectManagerLock = new Object();

  /**
   * Add the given listener to the list of objects that are listening for changes to Madl elements.
   * Has no effect if an identical listener is already registered.
   * <p>
   * This listener will only be notified during the POST_CHANGE resource change notification and any
   * reconcile operation (POST_RECONCILE). For finer control of the notification, use
   * {@link #addElementChangedListener(IElementChangedListener,int)}, which allows to specify a
   * different eventMask.
   * 
   * @param listener the listener being added
   */
  //ss
/*  public static void addElementChangedListener(ElementChangedListener listener) {
    //TODO (pquitslund): remove method
  }*/

  /**
   * Add the given listener for madl ignore changes to the Madl Model. Has no effect if an identical
   * listener is already registered.
   * 
   * @param listener the listener to add
   */
  //ss
  /*public static void addIgnoreListener(MadlIgnoreListener listener) {
    getProjectManager().getIgnoreManager().addListener(listener);
  }*/

  /**
   * Add the given resource to the set of ignored resources.
   * 
   * @param resource the resource to ignore
   * @throws IOException if there was an error accessing the ignore file
   * @throws CoreException if there was an error deleting markers
   */
  //ss
  /*public static void addToIgnores(IResource resource) throws IOException, CoreException {
    getProjectManager().getIgnoreManager().addToIgnores(resource);
  }*/

  /**
   * Remove any resource mapping for the given container.
   * 
   * @param resource
   */
  public static void clearResourceRemapping(IContainer container) {
    try {
      container.accept(new IResourceVisitor() {
        @Override
        public boolean visit(IResource resource) throws CoreException {
          if (resource instanceof IFile) {
            clearResourceRemapping((IFile) resource);

            return true;
          } else if (resource instanceof IContainer) {
            IContainer childContainer = (IContainer) resource;

            if (childContainer.getName().startsWith(".")) {
              return false;
            } else {
              return true;
            }
          } else {
            return false;
          }
        }
      });
    } catch (CoreException e) {
      //MadlCore.logError(e);
    }
  }

  /**
   * Remove any resource mapping for the given file.
   * 
   * @param resource
   */
  public static void clearResourceRemapping(IFile resource) {
    if (getResourceRemapping(resource) != null) {
      setResourceRemapping(resource, null);
    }
  }

  /**
   * Return true if directory contains a "packages" directory that is installed by pub
   */
  public static boolean containsPackagesDirectory(File file) {
    return new File(file, PACKAGES_DIRECTORY_NAME).isDirectory();
  }

  /**
   * Return <code>true</code> if the directory contains a "pubspec.yaml" file
   */
  public static boolean containsPubspecFile(File directory) {
    return new File(directory, PUBSPEC_FILE_NAME).isFile();
  }

  /**
   * Return the Madl project corresponding to the given project. Note that no check is made to
   * ensure that the project has the Madl nature.
   * 
   * @param project the resource corresponding to the Madl project
   * @return the Madl project corresponding to the given project
   */
  //ss
/*  public static MadlProject create(IProject project) {
    return null;
  }*/

  /**
   * Return the Madl element corresponding to the given resource, or <code>null</code> if the given
   * resource is not associated with any Madl element.
   * 
   * @param resource the resource corresponding to the Madl element
   * @return the Madl element corresponding to the given resource
   */
  //ss
/*  public static MadlElement create(IResource resource) {
    return null;
  }*/

  /**
   * Return the Madl model corresponding to the given workspace root.
   * 
   * @param project the workspace root corresponding to the model
   * @return the Madl model corresponding to the given workspace root
   */
  //ss
 /* public static MadlModel create(IWorkspaceRoot workspaceRoot) {
    return null;
  }*/

  /**
   * Answer the application directory (a directory contains a "packages" directory and a
   * "pubspec.yaml" file) directly or indirectly containing the specified file or the file itself if
   * it is an application directory.
   * 
   * @param libFileOrDir the library file or directory
   * @return the context in which the specified library should be analyzed (not <code>null</code>)
   */
  public static File getApplicationDirectory(File file) {
    while (file != null) {
      if (isApplicationDirectory(file)) {
        return file;
      }
      file = file.getParentFile();
    }
    return null;
  }

  /**
   * Returns the day (yyyy-MM-dd) the product was built.
   */
  public static String getBuildDate() {
    return "@BUILDDATE@";
  }

  /**
   * Returns the SVN revision number as a String.
   */
  public static String getBuildId() {
    return "@REVISION@";
  }

  /**
   * Return a unique token that can be used to determine whether cached data that changes only when
   * the version of the editor changes is still valid.
   * 
   * @return a token used to determine the validity of cached data
   */
  public static String getBuildIdOrDate() {
    String buildIdOrDate = getBuildId();
    if (buildIdOrDate.startsWith("@")) {
      buildIdOrDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }
    return buildIdOrDate;
  }

  /**
   * Returns the shared message console. Unlike the log ({@link MadlCore#getLog()}), the console is
   * intended for communication with the end-user.
   * 
   * @return the message console
   */
  public static MessageConsole getConsole() {
    return CONSOLE;
  }

  /**
   * Return the list of known Madl-like file extensions. Madl-like extensions are defined in the
   * {@link Platform.getContentManager() content type manager} for the
   * {@link #MADL_SOURCE_CONTENT_TYPE}. Note that a Madl-like extension does not include the leading
   * dot, and that the "madl" extension is always defined as a Madl-like extension.
   * 
   * @return the list of known Madl-like file extensions
   */
  //ss
 /* public static String[] getMadlLikeExtensions() {
    IContentType madlContentType = Platform.getContentTypeManager().getContentType(
        MADL_SOURCE_CONTENT_TYPE);
    HashSet<String> extensionSet = new HashSet<String>();
    for (IContentType contentType : Platform.getContentTypeManager().getAllContentTypes()) {
      if (contentType.isKindOf(madlContentType)) {
        for (String extension : contentType.getFileSpecs(IContentType.FILE_EXTENSION_SPEC)) {
          extensionSet.add(extension);
        }
      }
    }
    extensionSet.remove(Extensions.MADL);
    ArrayList<String> extensionList = new ArrayList<String>(extensionSet);
    extensionList.add(0, Extensions.MADL);
    return extensionList.toArray(new String[extensionList.size()]);
  }*/

  /**
   * Return a table of all known configurable options with their default values. These options allow
   * to configure the behavior of the underlying components. The client may safely use the result as
   * a template that they can modify and then pass to {@link #setOptions()}</code>.
   * <p>
   * Helper constants have been defined on MadlPreferenceConstants for each of the option IDs
   * (categorized in Code assist option ID, Compiler option ID and Core option ID) and some of their
   * acceptable values (categorized in Option value). Some options accept open value sets beyond the
   * documented constant values.
   * <p>
   * Note: each release may add new options.
   * 
   * @return a table of all known configurable options with their default values
   */
  public static Hashtable<String, String> getDefaultOptions() {
    return null;
  }

  public static File getEclipseInstallationDirectory() {
    return new File(Platform.getInstallLocation().getURL().getFile());
  }

  /**
   * Return the workspace root default charset encoding.
   * 
   * @return the name of the default charset encoding for the workspace root
   */
  public static String getEncoding() {
    try {
      return ResourcesPlugin.getWorkspace().getRoot().getDefaultCharset();
    } catch (IllegalStateException exception) {
      // happen when there's no workspace (see bug
      // https://bugs.eclipse.org/bugs/show_bug.cgi?id=216817)
      // or when it is shutting down (see bug
      // https://bugs.eclipse.org/bugs/show_bug.cgi?id=60687)
      return System.getProperty("file.encoding"); //$NON-NLS-1$
    } catch (CoreException exception) {
      // fails silently and return plugin global encoding if core exception
      // occurs
    }
    return ResourcesPlugin.getEncoding();
  }

  /**
   * Extract {@link ErrorCode} form the given {@link IMarker}.
   * 
   * @return the {@link ErrorCode}, may be {@code null}.
   */
  //ss
/*  public static ErrorCode getErrorCode(IMarker marker) {
    return AnalysisMarkerManager.getErrorCode(marker);
  }*/

  /**
   * Utility method for returning one option value only. Equivalent to
   * <code>MadlCore.getOptions().get(optionName)</code> Note that it may answer <code>null</code> if
   * this option does not exist.
   * <p>
   * Helper constants have been defined on MadlPreferenceConstants for each of the option IDs
   * (categorized in Code assist option ID, Compiler option ID and Core option ID) and some of their
   * acceptable values (categorized in Option value). Some options accept open value sets beyond the
   * documented constant values.
   * <p>
   * Note: each release may add new options.
   * 
   * @param optionName the name of the option whose value is to be returned
   * @return the value of a given option
   */
  //ss
 /* public static String getOption(String optionName) {
    return OptionManager.getInstance().getOption(optionName);
  }*/

  /**
   * Return the table of the current options. Initially, all options have their default values, and
   * this method returns a table that includes all known options.
   * <p>
   * Helper constants have been defined on MadlPreferenceConstants for each of the option IDs
   * (categorized in Code assist option ID, Compiler option ID and Core option ID) and some of their
   * acceptable values (categorized in Option value). Some options accept open value sets beyond the
   * documented constant values.
   * <p>
   * Note: each release may add new options.
   * <p>
   * Returns a default set of options even if the platform is not running.
   * </p>
   * 
   * @return table of current settings of all options (key type: <code>String</code>; value type:
   *         <code>String</code>)
   */
  //ss
/*  public static HashMap<String, String> getOptions() {
    return OptionManager.getInstance().getOptions();
  }*/

  /**
   * Return the unique instance of this class.
   * 
   * @return the unique instance of this class
   */
  public static MadlCore getPlugin() {
    return PLUG_IN;
  }

  /**
   * Answer the log used by {@link #logError(String)} and other local methods for logging errors,
   * warnings, and information.
   * 
   * @return the log (not <code>null</code>)
   */
  public static ILog getPluginLog() {
    return PLUGIN_LOG != null ? PLUGIN_LOG : getPlugin().getLog();
  }

  /**
   * Answer the unique project manager used for analysis of anything in the workspace.
   * 
   * @return the manager (not {@code null})
   */
  
 public static ProjectManager getProjectManager() {
	//TODO
	 //ss
	/* 
    synchronized (projectManagerLock) {
      if (projectManager == null) {
        projectManager = new ProjectManagerImpl(
            ResourcesPlugin.getWorkspace().getRoot(),
            MadlSdkManager.getManager().getNewSdk(),
            MadlIgnoreManager.getInstance());
      }
    }
    return projectManager;*/
	 return null;
  }

  /**
   * @return the mapping for the given resource, if any
   */
  public static String getResourceRemapping(IFile originalResource) {
    try {
      return originalResource.getPersistentProperty(RESOURCE_REMAP_NAME);
    } catch (CoreException e) {
      return null;
    }
  }

  /**
   * Returns the name of the directory in packages that is linked to the "lib" folder in the project
   * 
   * @param project
   * @return the name of the directory, <code>null</code> if there is no self linked packages folder
   */
  //ss
/*  public static String getSelfLinkedPackageName(IResource resource) {
    String packageName = null;

    try {
      PubFolder folder = MadlCore.getProjectManager().getPubFolder(resource);
      packageName = folder.getPubspec().getName();
    } catch (Exception e) {
      MadlCore.logError(e);
    }

    return packageName;
  }*/

  /**
   * Returns the path string for the default madl directory - user.home/madl
   * 
   * @return the name of the user.home/madl directory
   */
  public static String getUserDefaultMadlFolder() {
    String defaultLocation = System.getProperty("user.home"); //$NON-NLS-1$
    return defaultLocation + File.separator + "madl" + File.separator; //$NON-NLS-1$
  }

  /**
   * Returns the current value of the string-valued user-defined property with the given name.
   * Returns <code>null</code> if there is no user-defined property with the given name.
   * <p>
   * User-defined properties are defined in the <code>editor.properties</code> file located in the
   * eclipse installation directory.
   * 
   * @see MadlCore#getEclipseInstallationDirectory()
   * @param name the name of the property
   * @return the string-valued property
   */
  public static String getUserDefinedProperty(String key) {

    Properties properties = new Properties();

    File installDirectory = getEclipseInstallationDirectory();
    File file = new File(installDirectory, "editor.properties");

    if (file.exists()) {
      try {
        properties.load(new FileReader(file));
      } catch (FileNotFoundException e) {
       // logError(e);
      } catch (IOException e) {
        //logError(e);
      }
    }

    return properties.getProperty(key);
  }

  /**
   * @return the version text for this plugin (i.e. 1.1.0)
   */
  public static String getVersion() {
    Version version = getPlugin().getBundle().getVersion();

    return version.getMajor() + "." + version.getMinor() + "." + version.getMicro();
  }

  public static boolean is32Bit() {
    return Platform.getOSArch().indexOf("64") == -1;
  }

  /**
   * Return <code>true</code> if the given resource should be analyzed. All resources are to be
   * analyzed unless they have been excluded.
   * 
   * @param resource the resource being tested
   * @return <code>true</code> if the given resource should be analyzed
   */
  //ss
 /* public static boolean isAnalyzed(IResource resource) {
    return getProjectManager().getIgnoreManager().isAnalyzed(resource);
  }*/

  /**
   * Return true if directory contains a "packages" directory and a "pubspec.yaml" file
   */
  public static boolean isApplicationDirectory(File directory) {
    return containsPubspecFile(directory) && containsPackagesDirectory(directory);
  }

  /**
   * Return true if directory contains a "packages" directory and a "pubspec.yaml" file
   */
  public static boolean isApplicationDirectory(IContainer container) {
    if (container.getLocation() == null) {
      return false;
    }

    File directory = container.getLocation().toFile();

    return isApplicationDirectory(directory);
  }

  /**
   * Answer {@code true} if the specified resource is a build.madl file and exists either in a
   * project or in a folder containing a pubspec file.
   * 
   * @param file the file
   * @return {@code true} if the file is a build.madl file that will be run by the builder
   */
  public static boolean isBuildMadl(IFile file) {
    if (file == null || !file.getName().equals(BUILD_MADL_FILE_NAME) || !file.exists()) {
      return false;
    }

    IContainer container = file.getParent();

    // Always run build.madl in a project's root.
    if (container.getType() == IResource.PROJECT) {
      return true;
    }

    return container.getFile(new Path(PUBSPEC_FILE_NAME)).exists();
  }

  /**
   * Return <code>true</code> if the given file is contained in the packages directory created by
   * pub
   * 
   * @param file the file that is to be checked
   * @return <code>true</code> if the given file is in packages
   */
  public static boolean isContainedInPackages(File file) {
    if (file.getAbsolutePath().contains(PACKAGES_DIRECTORY_NAME)) {
      return true;
    }
    return false;
  }

  /**
   * Return <code>true</code> if the given file is contained in the packages directory created by
   * pub
   * 
   * @param file the file that is to be checked
   * @return <code>true</code> if the given file is in packages
   */
  public static boolean isContainedInPackages(IFile file) {
    return file.getFullPath().toString().contains(MadlCore.PACKAGES_DIRECTORY_PATH);
  }

  /**
   * Return <code>true</code> if the given file name's extension is an CSS-like extension.
   * 
   * @param fileName the file name being tested
   * @return <code>true</code> if the given file name's extension is an CSS-like extension
   */
  //ss
/*  public static boolean isCssLikeFileName(String fileName) {
    return isLikeFileName(fileName, CSS_FILE_EXTENSIONS);
  }*/

  /**
   * Return <code>true</code> if the given file name's extension is a generated Madl like extension.
   * 
   * @return <code>true</code> if the given file name's extension is a generated Madl like extension
   */
  //ss
 /* public static boolean isMadlGeneratedFile(String fileName) {
    return isLikeFileName(fileName, MADL_GENERATED_FILE_EXTENSIONS);
  }*/

  /**
   * Return <code>true</code> if the given file name's extension is a Madl-like extension.
   * 
   * @param fileName the file name being tested
   * @return <code>true</code> if the given file name's extension is a Madl-like extension
   * @see #getMadlLikeExtensions()
   */
  //ss
/*  public static boolean isMadlLikeFileName(String fileName) {
    return isLikeFileName(fileName, getMadlLikeExtensions());
  }*/

  /**
   * Return <code>true</code> if file is in the madl sdk lib directory
   * 
   * @param file
   * @return <code>true</code> if file is in madl-sdk/lib
   */
  //ss
/*  public static boolean isMadlSdkLibraryFile(File file) {
    File sdkLibrary = MadlSdkManager.getManager().getSdk().getLibraryDirectory();
    File parentFile = file.getParentFile();
    while (parentFile != null) {
      if (parentFile.equals(sdkLibrary)) {
        return true;
      }
      parentFile = parentFile.getParentFile();
    }
    return false;
  }*/

  /**
   * Return <code>true</code> if the given file name's extension is an HTML-like extension.
   * 
   * @param fileName the file name being tested
   * @return <code>true</code> if the given file name's extension is an HTML-like extension
   */
  //ss
 /* public static boolean isHtmlLikeFileName(String fileName) {
    return isLikeFileName(fileName, HTML_FILE_EXTENSIONS);
  }
*/
  /**
   * Return <code>true</code> if the given file name's extension is an image-like extension.
   * 
   * @param fileName the file name being tested
   * @return <code>true</code> if the given file name's extension is an image-like extension
   */
  //ss
 /* public static boolean isImageLikeFileName(String fileName) {
    return isLikeFileName(fileName, IMAGE_FILE_EXTENSIONS);
  }*/

  /**
   * @return {@code true} if the given resource is located in the top-level "packages" folder of its
   *         enclosing package.
   */
  //ss
 /* public static boolean isInDuplicatePackageFolder(IResource resource) {
    if (resource == null) {
      return false;
    }
    // prepare "pub" folder
    PubFolder pubFolder = MadlCore.getProjectManager().getPubFolder(resource);
    if (pubFolder == null) {
      return false;
    }
    IPath pubPath = pubFolder.getResource().getFullPath();
    // check if resource is in "packages"
    IPath resourcePath = resource.getFullPath();
    String[] segments = resourcePath.segments();
    for (int i = 1; i < segments.length - 1; i++) {
      String segment = segments[i];
      if (segment.equals("packages")) {
        return !resourcePath.uptoSegment(i).equals(pubPath);
      }
    }
    // not in "packages"
    return false;
  }*/

  /**
   * @return {@code true} if the given resource is located in the "packages" sub-folder that
   *         corresponds to the enclosing package.
   */
  //ss
/*  public static boolean isInSelfLinkedPackageFolder(IResource resource) {
    if (resource == null) {
      return false;
    }
    try {
      // prepare "pub" folder
      PubFolder pubFolder = MadlCore.getProjectManager().getPubFolder(resource);
      if (pubFolder == null) {
        return false;
      }
      // the name of the enclosing package
      String packageName = pubFolder.getPubspec().getName();
      // check if resource is in "packages" and references the enclosing package
      String[] segments = resource.getFullPath().segments();
      for (int i = 0; i < segments.length - 1; i++) {
        String segment = segments[i];
        if (segment.equals("packages") && segments[i + 1].equals(packageName)) {
          return true;
        }
      }
    } catch (Throwable e) {
      // pubFolder.getPubspec() may fail
      return false;
    }
    // not a self-reference
    return false;
  }*/

  /**
   * Return <code>true</code> if the given file name's extension is an HTML-like extension.
   * 
   * @param fileName the file name being tested
   * @return <code>true</code> if the given file name's extension is an HTML-like extension
   */
/*  public static boolean isJSLikeFileName(String fileName) {
    return isLikeFileName(fileName, JS_FILE_EXTENSIONS);
  }*/

  public static boolean isLinux() {
    return !isMac() && !isWindows();
  }

  public static boolean isMac() {
    // Look for the "Mac" OS name.
    return System.getProperty("os.name").toLowerCase().startsWith("mac");
  }

  /**
   * Return true if directory is one that is installed by pub
   * 
   * @param file the file to be checked
   * @return <code>true</code> if file name matches and is sibling of pubspec.yaml
   */
  public static boolean isPackagesDirectory(File file) {
    if (file.getName().equals(PACKAGES_DIRECTORY_NAME)) {
      return true;
    }
    return false;
  }

  /**
   * Return true if directory is one that is installed by pub
   * 
   * @param folder the folder to be checked
   * @return <code>true</code> if folder name matches and is sibling of pubspec.yaml
   */
  public static boolean isPackagesDirectory(IFolder folder) {
    IPath location = folder.getLocation();
    return location != null && isPackagesDirectory(location.toFile());
  }

  /**
   * Answer <code>true</code> if the string is a package spec
   */
  public static boolean isPackageSpec(String spec) {
    return spec != null && spec.startsWith(PACKAGE_SCHEME_SPEC);
  }

  /**
   * @return <code>true</code> if given {@link IResource} was installed by pub.
   */
  public static boolean isPackagesResource(IResource resource) {
    if (resource instanceof IFolder) {
      if (isPackagesDirectory((IFolder) resource)) {
        return true;
      }
    }
    if (resource != null && resource.getParent() instanceof IFolder) {
      IFolder parentFolder = (IFolder) resource.getParent();
      return isPackagesResource(parentFolder);
    }
    return false;
  }

  /**
   * @return <code>true</code> if given {@link CompilationUnit} is declared in "packages".
   */
  //ss
/*  public static boolean isPackagesUnit(CompilationUnit unit) {
    IResource resource = unit.getResource();
    return MadlCore.isPackagesResource(resource);
  }*/

  /**
   * Check if this URI denotes a patch file.
   */
  public static boolean isPatchfile(File file) {
    return file != null && file.getName().endsWith("_patch.madl");
  }

  /**
   * @return whether we're running in the context of the eclipse plugins build
   */
  public static boolean isPluginsBuild() {
    return Platform.getBundle("edu.depaul.cdm.madl.eclipse.core") != null;
  }

  /**
   * Checks if the given linkedResource points to a file in the container. This is used to check for
   * the self link in the packages directory
   * 
   * @param resource
   * @param linkedResource
   * @return <code>true</code> if the linked resource points to a file/folder in the project
   */
  public static boolean isSelfLinkedResource(IContainer resource, IResource linkedResource) {

    try {
      //     if (linkedResource.getParent().getParent() == resource) {
      String resourcePath = resource.getLocation().toFile().getCanonicalPath();
      String linkPath = linkedResource.getLocation().toFile().getCanonicalPath();
      if (linkPath.startsWith(resourcePath)) {
        return true;
      }
      //     }
    } catch (IOException e) {
      return false;
    }
    return false;
  }

  /**
   * Return <code>true</code> if the given file name's extension is an HTML-like extension.
   * 
   * @param fileName the file name being tested
   * @return <code>true</code> if the given file name's extension is an HTML-like extension
   */
/*  public static boolean isTxtLikeFileName(String fileName) {
    return isLikeFileName(fileName, TXT_FILE_EXTENSIONS);
  }*/

  public static boolean isWindows() {
    // Look for the "Windows" OS name.
    return System.getProperty("os.name").toLowerCase().startsWith("win");
  }

  public static boolean isWindowsXp() {
    // Look for the "Windows XP" OS name.
    return System.getProperty("os.name").toLowerCase().equals("windows xp");
  }

  /**
   * Log the given message as an error.
   * 
   * @param message an explanation of why the error occurred or what it means
   */
  //ss
  public static void logError(String message) {
  /*  logErrorImpl(message, null);
    instrumentationLogErrorImpl(message, null);*/
  }

  /**
   * Log the given exception as one representing an error.
   * 
   * @param message an explanation of why the error occurred or what it means
   * @param exception the exception being logged
   */
  //ss
  public static void logError(String message, Throwable exception) {
/*    logErrorImpl(message, exception);
    instrumentationLogErrorImpl(message, exception);*/
  }

  /**
   * Log the given exception as one representing an error.
   * 
   * @param exception the exception being logged
   */
  //ss
  public static void logError(Throwable exception) {
   /* logErrorImpl(exception.getMessage(), exception);
    instrumentationLogErrorImpl(null, exception);*/
  }

  /**
   * Log the given informational message.
   * 
   * @param message an explanation of why the error occurred or what it means
   * @param exception the exception being logged
   */
  public static void logInformation(String message) {
    logInformation(message, null);
  }

  /**
   * Log the given exception as one representing an informational message.
   * 
   * @param message an explanation of why the error occurred or what it means
   * @param exception the exception being logged
   */
  public static void logInformation(String message, Throwable exception) {
//ss
 /*   if (MadlCoreDebug.VERBOSE) {
      getPluginLog().log(new Status(Status.INFO, PLUGIN_ID, "INFO: " + message, exception));
    }

    instrumentationLogErrorImpl(message, exception);*/
  }

  /**
   * This method exists as a convenient marker for methods that have not yet been fully implemented.
   * It should be deleted before this product ships.
   */
  public static void notYetImplemented() {
  }

  /**
   * Check for accesses to the old model when the new analysis engine is enabled. This will fail by
   * throwing a runtime exception.
   */
  //ss
/*  public static void oldModelCheck() {
    if (MadlCoreDebug.ENABLE_NEW_ANALYSIS) {
      IllegalStateException exception = new IllegalStateException(
          "Wildly inappropriate access to the old model");

      MadlCore.logError(exception);

      throw exception;
    }
  }*/

  /**
   * If the given file defines a library, open the library and return it. If the library was already
   * open, then this method has no effect but returns the existing library. If the file does not
   * define a library, then look for a library in the same directory as the file or in a parent of
   * that directory that references the file. If such a library can be found, then open that library
   * and return it. Otherwise return <code>null</code>.
   * 
   * @param libraryFile the file defining the library to be opened
   * @param monitor the progress monitor used to provide feedback to the user, or <code>null</code>
   *          if no feedback is desired
   * @return the library defined by the given file
   * @throws MadlModelException if the library exists but could not be opened for some reason
   */
  //ss
 /* public static MadlLibrary openLibrary(File libraryFile, IProgressMonitor monitor)
      throws MadlModelException {
    return null;
  }*/

  /**
   * Removes the file extension from the given file name, if it has a Madl-like file extension.
   * Otherwise the file name itself is returned. Note this removes the dot ('.') before the
   * extension as well.
   * 
   * @param fileName the name of a file
   * @return the fileName without the Madl-like extension
   */
  //ss
 /* public static String removeMadlLikeExtension(String fileName) {
    return Util.getNameWithoutMadlLikeExtension(fileName);
  }*/

  /**
   * Remove the given listener from the list of objects that are listening for changes to Madl
   * elements. Has no affect if an identical listener is not registered.
   * 
   * @param listener the listener to be removed
   */
  //ss
 /* public static void removeElementChangedListener(ElementChangedListener listener) {
    //TODO (pquitslund): remove method
  }*/

  /**
   * Remove the given resource (as a path) from the set of ignored resources.
   * 
   * @param resource the resource path to (un)ignore
   * @throws IOException if there was an error accessing the ignore file
   */
  //ss
/*  public static void removeFromIgnores(IPath resource) throws IOException {
    getProjectManager().getIgnoreManager().removeFromIgnores(resource);
  }*/

  /**
   * Remove the given resource from the set of ignored resources.
   * 
   * @param resource the resource to (un)ignore
   * @throws IOException if there was an error accessing the ignore file
   */
  //ss
 /* public static void removeFromIgnores(IResource resource) throws IOException {
    getProjectManager().getIgnoreManager().removeFromIgnores(resource);
  }*/

  /**
   * Remove the given listener for madl ignore changes from the Madl Model. Has no effect if an
   * identical listener is not registered.
   * 
   * @param listener the non-<code>null</code> listener to remove
   */
  //ss
/*  public static void removeIgnoreListener(MadlIgnoreListener listener) {
    getProjectManager().getIgnoreManager().addListener(listener);
  }*/

  public static void setOptions(HashMap<String, String> newOptions) {

  }

  /**
   * Sets the current table of options. All and only the options explicitly included in the given
   * table are remembered; all previous option settings are forgotten, including ones not explicitly
   * mentioned.
   * <p>
   * Helper constants have been defined on MadlPreferenceConstants for each of the option IDs
   * (categorized in Code assist option ID, Compiler option ID and Core option ID) and some of their
   * acceptable values (categorized in Option value). Some options accept open value sets beyond the
   * documented constant values.
   * <p>
   * Note: each release may add new options.
   * 
   * @param newOptions the new options, or <code>null</code> to reset all options to their default
   *          values
   */
  public static void setOptions(Hashtable<String, String> newOptions) {

  }

  /**
   * TESTING ONLY: Set the log used by {@link #logError(String)} and other local methods for logging
   * errors, warnings, and information.
   * 
   * @param log the log or <code>null</code> to use the default system log
   * @return the log prior to calling this method or <code>null</code> for the default system log
   */
  public static ILog setPluginLog(ILog log) {
    ILog oldLog = PLUGIN_LOG;
    PLUGIN_LOG = log;
    return oldLog;
  }

  /**
   * Set a symbolic resource mapping from one resource to another. For some uses of the original
   * resource, like serving web content, the mapped resource should be substituted.
   * 
   * @param originalResource
   * @param newResource
   */
  public static void setResourceRemapping(IFile originalResource, IFile newResource) {
    try {
      String resourcePath = (newResource == null ? null
          : newResource.getFullPath().toPortableString());
      originalResource.setPersistentProperty(RESOURCE_REMAP_NAME, resourcePath);
    } catch (CoreException e) {
     // MadlCore.logError(e);
    }
  }

  /**
   * Sets the value of the string-valued user-defined property for the given key.
   * <p>
   * User-defined properties are defined in the <code>editor.properties</code> file located in the
   * eclipse installation directory.
   * 
   * @see MadlCore#getEclipseInstallationDirectory()
   * @param key the name of the property
   * @param value the string-valued property
   */
  public static void setUserDefinedProperty(String key, String value) {

    Properties properties = new Properties();

    File installDirectory = getEclipseInstallationDirectory();
    File file = new File(installDirectory, "editor.properties");

    try {
      if (!file.exists()) {
        file.createNewFile();
      }
      properties.load(new FileReader(file));
      properties.setProperty(key, value);
      properties.store(new FileWriter(file), null);
    } catch (FileNotFoundException e) {
     // logError(e);
    } catch (IOException e) {
     // logError(e);
    }

  }

  //ss
/*  private static void instrumentationLogErrorImpl(String message, Throwable exception) {
    if (instrumentationLogErrorEnabled) {

      InstrumentationBuilder instrumentation = Instrumentation.builder("MadlCore.LogError");
      try {
        instrumentation.data("Log_Message", message != null ? message : "null");
        instrumentation.data("Log_Exception", exception != null ? exception.toString() : "null");

        if (exception != null) {
          instrumentation.record(exception);
        }

      } catch (Exception e) {
        instrumentationLogErrorEnabled = false;
        logErrorImpl("Instrumentation failed to log error", exception);
      } finally {
        instrumentation.log();
      }
    }
  }*/

  /**
   * Return <code>true</code> if the given file name's extension matches one of the passed
   * extensions.
   * 
   * @param fileName the file name being tested
   * @param extensions an array of file extensions to test against
   * @return <code>true</code> if the given file name's extension matches one of the passed
   *         extensions
   */
  //ss
/*  private static boolean isLikeFileName(String fileName, String[] extensions) {
    if (fileName == null || fileName.length() == 0) {
      return false;
    }
    for (String extension : extensions) {
      if (StringUtilities.endsWithIgnoreCase(fileName, '.' + extension)) {
        return true;
      }
    }
    return false;
  }*/

  private static void logErrorImpl(String message, Throwable exception) {
    getPluginLog().log(new Status(Status.ERROR, PLUGIN_ID, message, exception));
  }

  private IEclipsePreferences prefs;

  /**
   * Initialize a newly created instance of this class.
   */
  public MadlCore() {
    PLUG_IN = this;
  }

  /**
   * Use madl2js if the SDK is present.
   */
  //ss
/*  public boolean getCompileWithMadl2JS() {
    return MadlSdkManager.getManager().hasSdk();
  }*/

  public boolean getDisableMadlBasedBuilder(IProject project) {
    return getProjectPreferences(project).getBoolean(PROJECT_PREF_DISABLE_MADL_BASED_BUILDER, false);
  }

  /**
   * Given an File, return the appropriate java.io.File representing a package root. This can be
   * null if a package root should not be used.
   * 
   * @param file the File
   * @return the File for the package root, or null if none
   */
  //ss
 /* public File getPackageRoot(File file) {
    IResource resource = ResourceUtil.getResource(file);
    if (resource != null) {
      return getPackageRoot(resource.getProject());
    }
    return null;
  }*/

  /**
   * Given an IProject, return the appropriate java.io.File representing a package root. This can be
   * null if a package root should not be used.
   * 
   * @param project the IProject
   * @return the File for the package root, or null if none
   */
  //ss
 /* public File getPackageRoot(IProject project) {
    if (project != null) {
      String setting = getProjectPreferences(project).get(PROJECT_PREF_PACKAGE_ROOT, "");

      if (setting != null && setting.length() > 0) {
        String[] paths = setting.split(File.pathSeparator);
        return new File(paths[0]);
      }
    }

    File[] roots = CmdLineOptions.getOptions().getPackageRoots();

    if (roots.length > 0) {
      return roots[0];
    }

    return null;
  }*/

  /**
   * Get the core plugin preferences. Use savePrefs() to save the preferences.
   */
  public IEclipsePreferences getPrefs() {
    if (prefs == null) {
      prefs = InstanceScope.INSTANCE.getNode(PLUGIN_ID);
    }

    return prefs;
  }

  public IEclipsePreferences getProjectPreferences(IProject project) {
    ProjectScope projectScope = new ProjectScope(project);

    return projectScope.getNode(PLUGIN_ID);
  }

  public boolean isAutoRunPubEnabled() {
    return MadlCore.getPlugin().getPrefs().getBoolean(PUB_AUTO_RUN_PREFERENCE, true);
  }

  public boolean isHintsMadl2JSEnabled() {
    return MadlCore.getPlugin().getPrefs().getBoolean(ENABLE_HINTS_MADL2JS_PREFERENCE, true);
  }

  public boolean isHintsEnabled() {
    return MadlCore.getPlugin().getPrefs().getBoolean(ENABLE_HINTS_PREFERENCE, true);
  }

  /**
   * Save the core plugin preferences
   * 
   * @throws CoreException
   */
  public void savePrefs() throws CoreException {
    try {
      getPrefs().flush();
    } catch (BackingStoreException e) {
      throw new CoreException(new Status(IStatus.ERROR, PLUGIN_ID, e.toString(), e));
    }
  }

  //ss
/*  @Override
  public void sdkUpdated(MadlSdk sdk) {
    Job job = new CleanLibrariesJob();

    job.schedule();
  }*/

  public void setDisableMadlBasedBuilder(IProject project, boolean value) throws CoreException {
    try {
      IEclipsePreferences prefs = getProjectPreferences(project);
      prefs.putBoolean(PROJECT_PREF_DISABLE_MADL_BASED_BUILDER, value);
      prefs.flush();
    } catch (BackingStoreException ex) {
      throw new CoreException(new Status(IStatus.ERROR, PLUGIN_ID, ex.toString(), ex));
    }
  }

  public void setPackageRoot(IProject project, String packageRootPath) throws CoreException {
    try {
      IEclipsePreferences prefs = getProjectPreferences(project);
      prefs.put(PROJECT_PREF_PACKAGE_ROOT, packageRootPath);
      prefs.flush();
    } catch (BackingStoreException ex) {
      throw new CoreException(new Status(IStatus.ERROR, PLUGIN_ID, ex.toString(), ex));
    }
  }

  @Override
  public void start(BundleContext context) throws Exception {
    super.start(context);

/*    CmdLineOptions.setOptions(CmdLineOptions.parseCmdLine(Platform.getApplicationArgs()));
    CmdLineOptions.getOptions().printWarnings();

    if (MadlCoreDebug.PERF_THREAD_CONTENTION_MONIOR) {
      try {
        java.lang.management.ThreadMXBean th = ManagementFactory.getThreadMXBean();
        th.setThreadContentionMonitoringEnabled(true);
      } catch (UnsupportedOperationException e) {
      }
    }

    AnalysisEngine analysisEngine = AnalysisEngine.getInstance();
    analysisEngine.setLogger(new Logger() {
      @Override
      public void logError(String message) {
        MadlCore.logError(message);
      }

      @Override
      public void logError(String message, Throwable exception) {
        MadlCore.logError(message, exception);
      }

      @Override
      public void logError(Throwable exception) {
        MadlCore.logError(exception);
      }

      @Override
      public void logInformation(String message) {
        MadlCore.logInformation(message);
      }

      @Override
      public void logInformation(String message, Throwable exception) {
        MadlCore.logInformation(message, exception);
      }
    });

    MadlSdkManager.getManager().addSdkListener(this);

    // Perform the project manager initialization in a job.
    Job job = new Job("Initialize ProjectManager") {
      @Override
      protected IStatus run(IProgressMonitor monitor) {
        getProjectManager().start();
        return Status.OK_STATUS;
      }
    };
    job.setSystem(true);
    job.schedule();*/
  }

  @Override
  public void stop(BundleContext context) throws Exception {
	  
	  super.stop(context);
    /*MadlSdkManager.getManager().removeSdkListener(this);

    try {
      getProjectManager().stop();

      if (MadlCoreDebug.METRICS) {
        StringWriter writer = new StringWriter();
        PerformanceManager.getInstance().printMetrics(new PrintWriter(writer));
        String metricsInfo = writer.toString();
        if (metricsInfo.length() > 0) {
          getLog().log(new Status(Status.INFO, PLUGIN_ID, metricsInfo, null));
        }
      }
    } finally {
      super.stop(context);
    }*/
  }
}
