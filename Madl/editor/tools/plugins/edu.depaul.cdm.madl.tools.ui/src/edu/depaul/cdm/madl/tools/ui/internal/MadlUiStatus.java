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
package edu.depaul.cdm.madl.tools.ui.internal;

import edu.depaul.cdm.madl.tools.ui.MadlToolsPlugin;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * Convenience class for error exceptions thrown inside JavaScriptUI plugin.
 */
public class MadlUiStatus extends Status {

  public static IStatus createError(int code, String message, Throwable throwable) {
    return new MadlUiStatus(IStatus.ERROR, code, message, throwable);
  }

  public static IStatus createError(int code, Throwable throwable) {
    String message = throwable.getMessage();
    if (message == null) {
      message = throwable.getClass().getName();
    }
    return new MadlUiStatus(IStatus.ERROR, code, message, throwable);
  }

  public static IStatus createInfo(int code, String message, Throwable throwable) {
    return new MadlUiStatus(IStatus.INFO, code, message, throwable);
  }

  public static IStatus createWarning(int code, String message, Throwable throwable) {
    return new MadlUiStatus(IStatus.WARNING, code, message, throwable);
  }

  private MadlUiStatus(int severity, int code, String message, Throwable throwable) {
    super(severity, MadlToolsPlugin.getPluginId(), code, message, throwable);
  }
}
