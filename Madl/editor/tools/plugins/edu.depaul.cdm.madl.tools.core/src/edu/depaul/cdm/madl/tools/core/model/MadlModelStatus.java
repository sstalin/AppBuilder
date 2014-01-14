/*
 * Copyright (c) 2011, the Madl project authors.
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

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;

/**
 * Instances of the class <code>MadlModelStatus</code> represent the outcome of a Madl model
 * operation. Status objects are used inside <code>MadlModelException</code> objects to indicate
 * what went wrong.
 * <p>
 * Madl model status object are distinguished by their plug-in id: <code>getPlugin</code> returns
 * <code>"com.google.madl.tools.core"</code>. <code>getCode</code> returns one of the status codes
 * declared in <code>MadlModelStatusConstants</code>.
 * </p>
 * <p>
 * A Madl model status may also carry additional information (that is, in addition to the
 * information defined in <code>IStatus</code>):
 * <ul>
 * <li>elements - optional handles to Madl elements associated with the failure</li>
 * <li>string - optional string associated with the failure</li>
 * </ul>
 * @coverage madl.tools.core.model
 */
public interface MadlModelStatus extends IStatus {
  /**
   * Return an array containing any Madl elements associated with the failure (see specification of
   * the status code), or an empty array if no elements are related to this particular status code.
   * 
   * @return the list of Madl element culprits
   * @see MadlModelStatusConstants
   */
  public MadlElement[] getElements();

  /**
   * Return the path associated with the failure (see specification of the status code), or
   * <code>null</code> if the failure does not include path information.
   * 
   * @return the path that caused the failure, or <code>null</code> if none
   */
  public IPath getPath();

  /**
   * Return <code>true</code> if this status indicates that a Madl model element does not exist.
   * This convenience method is equivalent to
   * <code>getCode() == MadlModelStatusConstants.ELEMENT_DOES_NOT_EXIST</code>.
   * 
   * @return <code>true</code> if the status code indicates that a Madl model element does not exist
   * @see MadlModelStatusConstants#ELEMENT_DOES_NOT_EXIST
   */
  public boolean isDoesNotExist();
}
