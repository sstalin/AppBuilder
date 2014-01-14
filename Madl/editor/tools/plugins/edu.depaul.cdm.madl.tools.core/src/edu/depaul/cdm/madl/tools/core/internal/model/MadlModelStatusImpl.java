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
package edu.depaul.cdm.madl.tools.core.internal.model;

import edu.depaul.cdm.madl.tools.core.MadlCore;
import edu.depaul.cdm.madl.tools.core.model.MadlElement;
import edu.depaul.cdm.madl.tools.core.model.MadlModelStatus;
import edu.depaul.cdm.madl.tools.core.model.MadlModelStatusConstants;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * Instances of the class <code>MadlModelStatusImpl</code> implement a Madl model status that can be
 * used when creating exceptions.
 * 
 * @coverage madl.tools.core.model
 */
public class MadlModelStatusImpl extends Status implements MadlModelStatus,
    MadlModelStatusConstants {
  /**
   * The Madl elements associated with the failure (see specification of the status code), or
   * <code>null</code> if no elements are related to this particular status code.
   */
  private MadlElement[] elements = MadlElement.EMPTY_ARRAY;

  /**
   * The path related to the failure, or <code>null</code> if no path is involved.
   */
  private IPath path;

  /**
   * An array containing the children of this status object.
   */
  private IStatus[] children = NO_CHILDREN;

  /**
   * An empty array of status objects used when a status has no children.
   */
  private final static IStatus[] NO_CHILDREN = new IStatus[0];

  /**
   * Singleton OK object
   */
  public static final MadlModelStatusImpl VERIFIED_OK = new MadlModelStatusImpl(OK, OK);

  /**
   * Create and return a new <code>MadlModelStatus</code> that is a a multi-status status.
   * 
   * @see IStatus#isMultiStatus()
   */
  public static MadlModelStatus newMultiStatus(MadlModelStatus[] children) {
    MadlModelStatusImpl status = new MadlModelStatusImpl();
    status.children = children;
    return status;
  }

  public MadlModelStatusImpl() {
    super(Status.ERROR, MadlCore.PLUGIN_ID, "MadlModelStatus", null); //$NON-NLS-1$
  }

  public MadlModelStatusImpl(int code) {
    super(Status.ERROR, MadlCore.PLUGIN_ID, code, "MadlModelStatus", null); //$NON-NLS-1$
  }

  public MadlModelStatusImpl(int code, MadlElement element) {
    this(code, new MadlElement[] {element});
  }

  public MadlModelStatusImpl(int code, MadlElement element, String message) {
    super(Status.ERROR, MadlCore.PLUGIN_ID, code, message, null);
    elements = new MadlElement[] {element};
  }

  public MadlModelStatusImpl(int code, MadlElement[] elements) {
    super(Status.ERROR, MadlCore.PLUGIN_ID, code, "MadlModelStatus", null); //$NON-NLS-1$
    this.elements = elements;
  }

  /**
   * Constructs a Madl model status with no corresponding elements.
   */
  public MadlModelStatusImpl(int severity, int code) {
    super(severity, MadlCore.PLUGIN_ID, code, "MadlModelStatus", null); //$NON-NLS-1$
  }

  /**
   * Constructs a Madl model status with no corresponding elements.
   */
  public MadlModelStatusImpl(int severity, int code, String message) {
    super(severity, MadlCore.PLUGIN_ID, code, message, null); //$NON-NLS-1$
  }

  public MadlModelStatusImpl(int code, String message) {
    super(Status.ERROR, MadlCore.PLUGIN_ID, code, message, null);
  }

  public MadlModelStatusImpl(int code, Throwable exception) {
    super(Status.ERROR, MadlCore.PLUGIN_ID, code, exception.getMessage(), exception);
  }

  public MadlModelStatusImpl(String message, Throwable exception) {
    super(Status.ERROR, MadlCore.PLUGIN_ID, message, exception);
  }

  public MadlModelStatusImpl(Throwable exception) {
    super(Status.ERROR, MadlCore.PLUGIN_ID, CORE_EXCEPTION, exception.getMessage(), exception);
  }

  // public MadlModelStatusImpl(int severity, String pluginId, int code,
  // String message, Throwable exception) {
  // super(severity, pluginId, code, message, exception);
  // }

  // public MadlModelStatusImpl(int severity, String pluginId, String message) {
  // super(severity, pluginId, message);
  // }

  // public MadlModelStatusImpl(int severity, String pluginId, String message,
  // Throwable exception) {
  // super(severity, pluginId, message, exception);
  // }

  @Override
  public IStatus[] getChildren() {
    return children;
  }

  @Override
  public MadlElement[] getElements() {
    return elements;
  }

  @Override
  public IPath getPath() {
    return path;
  }

  @Override
  public int getSeverity() {
    if (this.children == NO_CHILDREN) {
      return super.getSeverity();
    }
    int severity = -1;
    int count = children.length;
    for (int i = 0; i < count; i++) {
      int childrenSeverity = children[i].getSeverity();
      if (childrenSeverity > severity) {
        severity = childrenSeverity;
      }
    }
    return severity;
  }

  @Override
  public boolean isDoesNotExist() {
    return getCode() == ELEMENT_DOES_NOT_EXIST;
  }

  @Override
  public boolean isMultiStatus() {
    return this.children != NO_CHILDREN;
  }

  @Override
  public boolean isOK() {
    return getCode() == OK;
  }

  @Override
  public boolean matches(int mask) {
    if (!isMultiStatus()) {
      return matches(this, mask);
    } else {
      for (int i = 0, max = this.children.length; i < max; i++) {
        if (matches((MadlModelStatusImpl) this.children[i], mask)) {
          return true;
        }
      }
      return false;
    }
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Madl Model Status ["); //$NON-NLS-1$
    builder.append(getMessage());
    builder.append("]"); //$NON-NLS-1$
    return builder.toString();
  }

  private int getBits() {
    int severity = 1 << (getCode() % 100 / 33);
    int category = 1 << ((getCode() / 100) + 3);
    return severity | category;
  }

  /**
   * Helper for matches(int).
   */
  private boolean matches(MadlModelStatusImpl status, int mask) {
    int severityMask = mask & 0x7;
    int categoryMask = mask & ~0x7;
    int bits = status.getBits();
    return ((severityMask == 0) || (bits & severityMask) != 0)
        && ((categoryMask == 0) || (bits & categoryMask) != 0);
  }
}
