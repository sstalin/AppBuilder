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

import edu.depaul.cdm.madl.tools.core.model.MadlElement;
import edu.depaul.cdm.madl.tools.core.model.MadlModelException;

import org.eclipse.core.resources.IResource;

/**
 * This class locates different resources which are related to an object
 */
public class ResourceLocator implements IResourceLocator {

  @Override
  public IResource getContainingResource(Object element) throws MadlModelException {
    IResource resource = null;
    if (element instanceof IResource) {
      resource = (IResource) element;
    }
    if (element instanceof MadlElement) {
      resource = ((MadlElement) element).getResource();
      if (resource == null) {
        resource = ((MadlElement) element).getMadlProject().getProject();
      }
    }
    return resource;
  }

  @Override
  public IResource getCorrespondingResource(Object element) throws MadlModelException {
    if (element instanceof MadlElement) {
      return ((MadlElement) element).getCorrespondingResource();
    } else {
      return null;
    }
  }

  @Override
  public IResource getUnderlyingResource(Object element) throws MadlModelException {
    if (element instanceof MadlElement) {
      return ((MadlElement) element).getUnderlyingResource();
    } else {
      return null;
    }
  }
}
