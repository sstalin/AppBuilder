/*
 * Copyright (c) 2013, the Dart project authors.
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

package edu.depaul.cdm.madl.tools.ui.internal.text.editor;

import edu.depaul.cdm.madl.engine.services.assist.AssistContext;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;

/**
 * {@link TextSelection} with information about selection in {@link MadlEditor}.
 */
public class MadlSelection extends TextSelection {
  private final MadlEditor editor;

  public MadlSelection(MadlEditor editor, IDocument document, int offset, int length) {
    super(document, offset, length);
    this.editor = editor;
  }

  /**
   * @return the {@link AssistContext} for selection, may be <code>null</code>, for example if unit
   *         is not resolved.
   */
  public AssistContext getContext() {
    return editor.getAssistContext();
  }

  /**
   * @return the {@link MadlEditor} from which this {@link DartSelection} is coming.
   */
  public MadlEditor getEditor() {
    return editor;
  }
}
