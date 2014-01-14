/*
 * Copyright (c) 2013, the Madl project authors.
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

import edu.depaul.cdm.madl.engine.element.Element;
import edu.depaul.cdm.madl.tools.ui.MadlElementLabels;
import edu.depaul.cdm.madl.tools.ui.NewMadlElementLabels;
import edu.depaul.cdm.madl.tools.ui.StandardMadlElementContentProvider;
import edu.depaul.cdm.madl.tools.ui.internal.viewsupport.MadlElementImageProvider;
import edu.depaul.cdm.madl.tools.ui.internal.viewsupport.NewMadlElementImageProvider;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;

/**
 * A {@link IWorkbenchAdapter} for Madl {@link Element}s.
 */
public class NewMadlWorkbenchAdapter implements IWorkbenchAdapter {

  private final NewMadlElementImageProvider imageProvider;
  private final StandardMadlElementContentProvider contentProvider;

  public NewMadlWorkbenchAdapter() {
    imageProvider = new NewMadlElementImageProvider();
    contentProvider = new StandardMadlElementContentProvider(true);
  }

  @Override
  public Object[] getChildren(Object element) {
    return contentProvider.getChildren(element);
  }

  @Override
  public ImageDescriptor getImageDescriptor(Object element) {
    Element de = getMadlElement(element);
    if (de != null) {
      return imageProvider.getMadlImageDescriptor(de, MadlElementImageProvider.OVERLAY_ICONS
          | MadlElementImageProvider.SMALL_ICONS);
    }
    return null;
  }

  @Override
  public String getLabel(Object element) {
    return NewMadlElementLabels.getTextLabel(getMadlElement(element), MadlElementLabels.ALL_DEFAULT);
  }

  @Override
  public Object getParent(Object element) {
    return contentProvider.getParent(element);
  }

  private Element getMadlElement(Object element) {
    if (element instanceof Element) {
      return (Element) element;
    }
    return null;
  }
}
