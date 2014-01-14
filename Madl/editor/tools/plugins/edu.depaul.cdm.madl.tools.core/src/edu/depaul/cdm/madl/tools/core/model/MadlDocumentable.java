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

import edu.depaul.cdm.madl.engine.utilities.source.SourceRange;

/**
 * A MadlElement that is documentable.
 * 
 * @coverage madl.tools.core.model
 */
public interface MadlDocumentable extends MadlElement {

  /**
   * Returns the madldoc range if this element is from source or if this element is a binary element
   * with an attached source, null otherwise.
   * <p>
   * If this element is from source, the madldoc range is extracted from the corresponding source.
   * </p>
   * <p>
   * If this element is from a binary, the madldoc is extracted from the attached source if present.
   * </p>
   * <p>
   * If this element's openable is not consistent, then null is returned.
   * </p>
   * 
   * @exception MadlModelException if this element does not exist or if an exception occurs while
   *              accessing its corresponding resource.
   * @return a source range corresponding to the madldoc source or <code>null</code> if no source is
   *         available, this element has no madldoc comment or this element's openable is not
   *         consistent
   * @see IOpenable#isConsistent()
   */
  public SourceRange getMadlDocRange() throws MadlModelException;

}
