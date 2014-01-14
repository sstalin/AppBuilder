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
package edu.depaul.cdm.madl.tools.core.model;

import edu.depaul.cdm.madl.tools.core.MadlCore;
import edu.depaul.cdm.madl.tools.core.workingcopy.WorkingCopyOwner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * The interface <code>MadlProject</code> defines the behavior of objects representing a project
 * that has a Madl nature.
 * 
 * @coverage madl.tools.core.model
 */
public interface MadlProject extends ParentElement, OpenableElement {

  /**
   * Find the type defined in this project with the given qualified name.
   * 
   * @param typeName the name of the type to be returned
   * @return the type defined in this project with the given qualified name
   * @throws MadlModelException if the types defined in this project cannot be determined for some
   *           reason
   * @deprecated Madl projects don't have a notion of a qualified type name; this method should be
   *             removed
   */
  @Deprecated
  public Type findType(String qualifiedTypeName) throws MadlModelException;

  /**
   * Find the type defined in this project with the given qualified name.
   * 
   * @param qualifiedTypeName the name of the type to be returned
   * @param owner
   * @return the type defined in this project with the given qualified name
   * @throws MadlModelException if the types defined in this project cannot be determined for some
   *           reason
   * @deprecated Madl projects don't have a notion of a qualified type name; this method should be
   *             removed
   */
  @Deprecated
  public Type findType(String qualifiedTypeName, WorkingCopyOwner owner) throws MadlModelException;

  /**
   * Find all of the types defined in this project with the given name.
   * 
   * @param typeName the name of the types to be returned
   * @return all of the types defined in this project with the given name
   * @throws MadlModelException if the types defined in this project cannot be determined for some
   *           reason
   */
  public Type[] findTypes(String typeName) throws MadlModelException;

  /**
   * Return the output location for this project's artifacts.
   * <p>
   * The artifact output location is where derived resources are ordinarily generated. Examples of
   * derived resources include source mapping files (.map) and secondary JavaScript (.js).
   * 
   * @return the workspace-relative absolute path of the default output folder
   * @see #getOutputLocation()
   */
  public IPath getArtifactLocation();

  /**
   * Return an array containing all of the libraries defined in this project.
   * 
   * @return an array containing all of the libraries defined in this project
   * @throws MadlModelException if the libraries defined in this project cannot be determined for
   *           some reason
   */
  //public MadlLibrary[] getMadlLibraries() throws MadlModelException;

  /**
   * Return the library associated with the given resource.
   * 
   * @param resource the resource associated with the library to be returned
   * @return the library associated with the given resource
   * @throws MadlModelException if the children of this project cannot be accessed for some reason
   */
 // public MadlLibrary getMadlLibrary(IResource resource) throws MadlModelException;

  /**
   * Return the library associated with the given resource path.
   * 
   * @param resourcePath the resource path associated with the library to be returned
   * @return the library associated with the given resource path
   * @throws MadlModelException if the children of this project cannot be accessed for some reason
   */
  //public MadlLibrary getMadlLibrary(String resourcePath) throws MadlModelException;

  /**
   * Return the default output location. This is used when resetting the project's output location
   * to the default.
   * 
   * @return the default output location
   */
  public IPath getDefaultOutputFullPath();

  /**
   * Return the table containing the mapping between the Html files in the project which have a madl
   * related script tag and the corresponding library definition file.
   * 
   * @return table of html file to library file mapping
   * @throws CoreException
   */
  public HashMap<String, List<String>> getHtmlMapping() throws CoreException;

  /**
   * Return an array of non-Madl resources directly contained in this project. It does not
   * transitively answer non-Madl resources contained in folders; these would have to be explicitly
   * iterated over.
   * <p>
   * Non-Madl resources includes other files and folders located in the project not accounted for by
   * any of its libraries.
   * 
   * @return an array of non-Madl resources (<code>IFile</code>s and/or <code>IFolder</code>s)
   *         directly contained in this project
   * @throws MadlModelException if this element does not exist or if an exception occurs while
   *           accessing its corresponding resource
   */
  public IResource[] getNonMadlResources() throws MadlModelException;

  /**
   * Utility method for returning one option value only. Equivalent to
   * <code>this.getOptions(inheritMadlCoreOptions).get(optionName)</code> Note that it may answer
   * <code>null</code> if this option does not exist, or if there is no custom value for it.
   * <p>
   * For a complete description of the configurable options, see
   * {@link MadlCore#getDefaultOptions()}.
   * </p>
   * 
   * @param optionName the name of the option whose value is to be returned
   * @param inheritMadlCoreOptions <code>true</code> if MadlCore options should be inherited as well
   * @return the value of a given option
   */
  public String getOption(String optionName, boolean inheritMadlCoreOptions);

  /**
   * Return the table containing the current custom options for this project. Projects remember
   * their custom options, in other words, only the options different from the the {@link MadlCore}
   * global options for the workspace. A boolean argument allows to directly merge the project
   * options with global ones from {@link MadlCore}.
   * <p>
   * For a complete description of the configurable options, see
   * {@link MadlCore#getDefaultOptions()}.
   * </p>
   * 
   * @param inheritMadlCoreOptions <code>true</code> if {@link MadlCore} options should be inherited
   *          as well
   * @return table of current settings of all options
   */
  public Hashtable<String, String> getOptions(boolean inheritMadlCoreOptions);

  /**
   * Return the output location for this project as a workspace- relative absolute path.
   * <p>
   * The output location is where the main derived resources are generated. Examples of these
   * resources are .app.js files.
   * 
   * @return the workspace-relative absolute path of the default output folder
   * @throws MadlModelException if this element does not exist
   * @see #setOutputLocation(org.eclipse.core.runtime.IPath, IProgressMonitor)
   * @see IClasspathEntry#getOutputLocation()
   */
  public IPath getOutputLocation() throws MadlModelException;

  /**
   * Return the project corresponding to this Madl project.
   * 
   * @return the project corresponding to this Madl project
   */
  public IProject getProject();

  // /**
  // * Create and return a new evaluation context.
  // *
  // * @return the evaluation context that was created
  // */
  // public EvaluationContext newEvaluationContext();

  /**
   * Return <code>true</code> if this project has been built at least once and thus has a build
   * state.
   * 
   * @return <code>true</code> if this project has been built at least once
   */
  public boolean hasBuildState();

  /**
   * Removes the new {@link IFile} as a top-level library in this project.
   * 
   * @param file the new library file
   */
  public boolean removeLibraryFile(IFile file);

  /**
   * Helper method for setting one option value only.
   * <p>
   * Equivalent to:
   * 
   * <pre>
   *  Map options = this.getOptions(false);
   *  map.put(optionName, optionValue);
   *  this.setOptions(map);
   * </pre>
   * <p>
   * For a complete description of the configurable options, see
   * {@link MadlCore#getDefaultOptions()}.
   * 
   * @param optionName the name of an option
   * @param optionValue the value of the option to set. If <code>null</code>, then the option is
   *          removed from the project preferences.
   * @throws NullPointerException if <code>optionName</code> is <code>null</code> (see
   *           {@link org.osgi.service.prefs.Preferences#put(String, String)})
   */
  public void setOption(String optionName, String optionValue);

  /**
   * Sets the project custom options. All and only the options explicitly included in the given
   * table are remembered; all previous option settings are forgotten, including ones not explicitly
   * mentioned.
   * <p>
   * For a complete description of the configurable options, see
   * {@link MadlCore#getDefaultOptions()}.
   * 
   * @param newOptions the new options, or <code>null</code> to flush all custom options (clients
   *          will automatically get the global MadlCore options).
   */
  public void setOptions(Map<String, String> newOptions);

  /**
   * Sets the default output location of this project to the location described by the given
   * workspace-relative absolute path.
   * <p>
   * The default output location is where derived resources are ordinarily generated. Examples of
   * derived resources include JavaScript (.js) files and metadata (.meta) files.
   * 
   * @param path the workspace-relative absolute path of the default output folder
   * @param monitor the progress monitor
   * @throws MadlModelException if the classpath could not be set. Reasons include:
   *           <ul>
   *           <li>This Madl element does not exist (ELEMENT_DOES_NOT_EXIST) </li> <li>The path
   *           refers to a location not contained in this project (<code>PATH_OUTSIDE_PROJECT</code>
   *           ) <li>The path is not an absolute path ( <code>RELATIVE_PATH</code>) <li>The output
   *           location is being modified during resource change event notification (CORE_EXCEPTION)
   *           </ul>
   * @see #getOutputLocation()
   */
  public void setOutputLocation(IPath path, IProgressMonitor monitor) throws MadlModelException;

  /**
   * Updates the html file to library mapping table
   * 
   * @param htmlFileName the name of the html file
   * @param libraries the list of libraries referenced in the html file
   * @param add true adds the entry if true, false removes the entry
   * @throws MadlModelException
   */
  public void updateHtmlMapping(String htmlFileName, List<String> libraries, boolean add)
      throws MadlModelException;

}
