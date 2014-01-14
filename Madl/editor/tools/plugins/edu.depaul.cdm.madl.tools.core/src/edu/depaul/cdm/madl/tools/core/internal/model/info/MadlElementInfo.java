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
package edu.depaul.cdm.madl.tools.core.internal.model.info;

import edu.depaul.cdm.madl.tools.core.internal.model.MadlElementImpl;
import edu.depaul.cdm.madl.tools.core.model.MadlElement;

/**
 * Instances of the class <code>MadlElementInfo</code> maintain the cached data that is shared by
 * all equal elements.
 * 
 * @coverage madl.tools.core.model
 */
public class MadlElementInfo {
  /**
   * Initialize a newly created information holder.
   */
  public MadlElementInfo() {
    super();
  }

  public MadlElement[] getChildren() {
    return MadlElementImpl.EMPTY_ARRAY;
  }
}
