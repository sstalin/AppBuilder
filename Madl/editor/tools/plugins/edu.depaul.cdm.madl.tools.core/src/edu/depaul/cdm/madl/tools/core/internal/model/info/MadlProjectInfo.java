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
package edu.depaul.cdm.madl.tools.core.internal.model.info;

import edu.depaul.cdm.madl.tools.core.MadlCore;
//import edu.depaul.cdm.madl.tools.core.internal.model.MadlLibraryImpl;
//import edu.depaul.cdm.madl.tools.core.internal.model.MadlProjectImpl;
import edu.depaul.cdm.madl.tools.core.model.MadlElement;

import org.eclipse.core.resources.IResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Instances of the class <code>MadlProjectInfo</code> maintain the cached data shared by all equal
 * projects.
 * 
 * @coverage madl.tools.core.model
 */
public class MadlProjectInfo extends OpenableElementInfo {
  /**
   * A list containing the project-relative paths to all children in the project.
   */
  private List<String> childPaths = null;

  /**
   * Table to store html file to library mapping
   */
  private HashMap<String, List<String>> htmlMapping;

  /**
   * The name of the directory in packages that is the self reference to the "lib" folder in the
   * project
   */
  private String linkedPackageDirName;

  /**
   * A list containing the names of the dependencies as specified in the pubspec.yaml file
   */
  private List<String> dependencies;

  /**
   * Return a list containing the project-relative paths to all children in the project.
   * 
   * @return a list containing the project-relative paths to all children in the project
   */
  public List<String> getChildPaths() {
    return childPaths;
  }

  /**
   * Return the mapping for the html files contained in this project.
   * 
   * @return the table with the html files to library mapping
   */

  public HashMap<String, List<String>> getHtmlMapping() {
    return htmlMapping;
  }

  //ss
/*  public MadlLibraryImpl[] getLibraries() {
    ArrayList<MadlLibraryImpl> libraries = new ArrayList<MadlLibraryImpl>();
    for (MadlElement child : getChildren()) {
      if (child instanceof MadlLibraryImpl) {
        libraries.add((MadlLibraryImpl) child);
      }
    }
    return libraries.toArray(new MadlLibraryImpl[libraries.size()]);
  }*/

  public String getLinkedPackageDirName() {
    return linkedPackageDirName;
  }

  //ss
/*  public IResource[] getNonMadlResources(MadlProjectImpl project) {
    MadlCore.notYetImplemented();
    return new IResource[0];
  }*/

  /**
   * Return a list containing the names of all the dependencies specified in pubspec.yaml file
   * 
   * @return the dependencies
   */
  public List<String> getPackageDependencies() {
    return dependencies;
  }

  public void resetCaches() {
    MadlCore.notYetImplemented();
  }

  /**
   * Set the project-relative paths to all children in the project to the given list.
   * 
   * @param paths a list containing the project-relative paths to all children in the project
   */
  public void setChildPaths(List<String> paths) {
    childPaths = paths;
  }

  /**
   * Set the html file to library mapping table
   * 
   * @param mapping a table with the html file to library mapping
   */
  public void setHtmlMapping(HashMap<String, List<String>> mapping) {
    htmlMapping = mapping;
  }

  /**
   * set the name for the self linked package directory
   */
  public void setLinkedPackageDirName(String packageDirectoryName) {
    this.linkedPackageDirName = packageDirectoryName;
  }

  /**
   * Set the dependencies to the given list populated from information in pubspec.yaml
   * 
   * @param dependencies the dependencies to set
   */
  public void setPackageDependencies(List<String> dependencies) {
    this.dependencies = dependencies;
  }

  /**
   * Update the mapping table on changes to the project
   * 
   * @param htmlFileName the name of the html file that changed
   * @param libraries list of library references
   */
  public void updateHtmlMapping(String htmlFileName, List<String> libraries, boolean add) {
    if (htmlMapping == null) {
      return;
    }
    if (add) {
      htmlMapping.put(htmlFileName, libraries);
    } else {
      htmlMapping.remove(htmlFileName);
    }
  }
}
