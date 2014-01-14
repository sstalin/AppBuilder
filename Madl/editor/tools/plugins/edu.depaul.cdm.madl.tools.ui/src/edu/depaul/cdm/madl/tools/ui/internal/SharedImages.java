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

import edu.depaul.cdm.madl.tools.ui.MadlPluginImages;
import edu.depaul.cdm.madl.tools.ui.ISharedImages;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * Default implementation of ISharedImages
 */
public class SharedImages implements ISharedImages {

  public SharedImages() {
  }

  /*
   * (Non-Javadoc) Method declared in ISharedImages
   */
  @Override
  public Image getImage(String key) {
    return MadlPluginImages.get(key);
  }

  /*
   * (Non-Javadoc) Method declared in ISharedImages
   */
  @Override
  public ImageDescriptor getImageDescriptor(String key) {
    return MadlPluginImages.getDescriptor(key);
  }

}
