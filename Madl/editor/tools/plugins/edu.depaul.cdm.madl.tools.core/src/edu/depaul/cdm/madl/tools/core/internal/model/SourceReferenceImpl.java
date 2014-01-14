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

import edu.depaul.cdm.madl.compiler.ast.MadlNode;
import edu.depaul.cdm.madl.compiler.ast.MadlUnit;

import edu.depaul.cdm.madl.engine.utilities.source.SourceRange;
import edu.depaul.cdm.madl.tools.core.buffer.Buffer;

import edu.depaul.cdm.madl.tools.core.internal.model.info.MadlElementInfo;
//import edu.depaul.cdm.madl.tools.core.internal.util.DOMFinder;
import edu.depaul.cdm.madl.tools.core.internal.util.Messages;

import edu.depaul.cdm.madl.tools.core.model.CompilationUnit;
import edu.depaul.cdm.madl.tools.core.model.MadlElement;
import edu.depaul.cdm.madl.tools.core.model.MadlModelException;
import edu.depaul.cdm.madl.tools.core.model.OpenableElement;
import edu.depaul.cdm.madl.tools.core.model.SourceReference;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

import java.util.HashMap;

/**
 * Instances of the class <code>SourceReferenceImpl</code>.
 * 
 * @coverage madl.tools.core.model
 */
public abstract class SourceReferenceImpl extends MadlElementImpl implements SourceReference {
  /*
   * A count to uniquely identify this element in the case that a duplicate named element exists.
   * For example, if there are two fields in a compilation unit with the same name, the occurrence
   * count is used to distinguish them. The occurrence count starts at 1 (thus the first occurrence
   * is occurrence 1, not occurrence 0).
   */
  public int occurrenceCount = 1;

  protected SourceReferenceImpl(MadlElementImpl parent) {
    super(parent);
  }

  /**
   * @see ISourceManipulation
   */
  public void copy(MadlElement container, MadlElement sibling, String rename, boolean force,
      IProgressMonitor monitor) throws MadlModelException {
    if (container == null) {
      throw new IllegalArgumentException(Messages.operation_nullContainer);
    }
    MadlElement[] elements = new MadlElement[] {this};
    MadlElement[] containers = new MadlElement[] {container};
    MadlElement[] siblings = null;
    if (sibling != null) {
      siblings = new MadlElement[] {sibling};
    }
    String[] renamings = null;
    if (rename != null) {
      renamings = new String[] {rename};
    }
    getMadlModel().copy(elements, containers, siblings, renamings, force, monitor);
  }

  /**
   * @see ISourceManipulation
   */
  public void delete(boolean force, IProgressMonitor monitor) throws MadlModelException {
    MadlElement[] elements = new MadlElement[] {this};
    getMadlModel().delete(elements, force, monitor);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof SourceReferenceImpl)) {
      return false;
    }
    return occurrenceCount == ((SourceReferenceImpl) o).occurrenceCount && super.equals(o);
  }

  /**
   * Return the {@link MadlNode AST node} that corresponds to this element, or <code>null</code> if
   * there is no corresponding node.
   * 
   * @return the {@link MadlNode AST node} that corresponds to this element
   */
  @Override
  public MadlNode findNode(MadlUnit ast) {
    DOMFinder finder = new DOMFinder(ast, this, false);
    try {
      return finder.search();
    } catch (MadlModelException exception) {
      // receiver doesn't exist
      return null;
    }
  }

  @Override
  public CompilationUnit getCompilationUnit() {
    return getAncestor(CompilationUnit.class);
  }

  /**
   * Elements within compilation units have no corresponding resource.
   */
  @Override
  public IResource getCorrespondingResource() throws MadlModelException {
    if (!exists()) {
      throw newNotPresentException();
    }
    return null;
  }

  public final SourceRange getMadlDocRange() {

    return null;
  }

  // /*
  // * @see JavaElement
  // */
  // public MadlElement getHandleFromMemento(String token, MementoTokenizer
  // memento, WorkingCopyOwner workingCopyOwner) {
  // switch (token.charAt(0)) {
  // case JEM_COUNT:
  // return getHandleUpdatingCountFromMemento(memento, workingCopyOwner);
  // }
  // return this;
  // }
  // protected void getHandleMemento(StringBuffer buff) {
  // super.getHandleMemento(buff);
  // if (this.occurrenceCount > 1) {
  // buff.append(JEM_COUNT);
  // buff.append(this.occurrenceCount);
  // }
  // }

  // /*
  // * Update the occurence count of the receiver and creates a Java element
  // handle from the given memento.
  // * The given working copy owner is used only for compilation unit handles.
  // */
  // public MadlElement getHandleUpdatingCountFromMemento(MementoTokenizer
  // memento, WorkingCopyOwner owner) {
  // if (!memento.hasMoreTokens()) return this;
  // this.occurrenceCount = Integer.parseInt(memento.nextToken());
  // if (!memento.hasMoreTokens()) return this;
  // String token = memento.nextToken();
  // return getHandleFromMemento(token, memento, owner);
  // }

  /**
   * Return the occurrence count of this element. The occurrence count is used to distinguish two
   * elements that would otherwise be indistinguishable, such as two fields with the same name.
   * 
   * @return the occurrence count of this element
   */
  public int getOccurrenceCount() {
    return occurrenceCount;
  }

  @Override
  public OpenableElement getOpenableParent() {
    MadlElement current = getParent();
    while (current != null) {
      if (current instanceof OpenableElement) {
        return (OpenableElement) current;
      }
      current = current.getParent();
    }
    return null;
  }

  @Override
  public IPath getPath() {
    return getParent().getPath();
  }

  @Override
  public String getSource() throws MadlModelException {
    OpenableElement openable = getOpenableParent();
    Buffer buffer = openable.getBuffer();
    if (buffer == null) {
      return null;
    }
    SourceRange range = getSourceRange();
    int offset = range.getOffset();
    int length = range.getLength();
    if (offset == -1 || length == 0) {
      return null;
    }
    try {
      return buffer.getText(offset, length);
    } catch (RuntimeException e) {
      return null;
    }
  }

  @Override
  public SourceRange getSourceRange() throws MadlModelException {
    return null;
  }

  @Override
  public IResource getUnderlyingResource() throws MadlModelException {
    if (!exists()) {
      throw newNotPresentException();
    }
    return getParent().getUnderlyingResource();
  }

  @Override
  public boolean hasChildren() throws MadlModelException {
    return getChildren().length > 0;
  }

  /**
   * Return <code>true</code> if the element is private to the library in which it is defined.
   * 
   * @return <code>true</code> if the element is private to the library in which it is defined
   */
  public boolean isPrivate() {
    //
    // This method implements the isPrivate method defined in both CompilationUnitElement and
    // TypeMember, but cannot be marked as overriding either.
    //
    String name = getElementName();
    return name != null && name.length() > 0 && name.charAt(0) == '_';
  }

  @Override
  public boolean isStructureKnown() throws MadlModelException {
    // structure is always known inside an openable
    return true;
  }

  /**
   * @see ISourceManipulation
   */
  public void move(MadlElement container, MadlElement sibling, String rename, boolean force,
      IProgressMonitor monitor) throws MadlModelException {
    if (container == null) {
      throw new IllegalArgumentException(Messages.operation_nullContainer);
    }
    MadlElement[] elements = new MadlElement[] {this};
    MadlElement[] containers = new MadlElement[] {container};
    MadlElement[] siblings = null;
    if (sibling != null) {
      siblings = new MadlElement[] {sibling};
    }
    String[] renamings = null;
    if (rename != null) {
      renamings = new String[] {rename};
    }
    getMadlModel().move(elements, containers, siblings, renamings, force, monitor);
  }

  /**
   * @see ISourceManipulation
   */
  public void rename(String newName, boolean force, IProgressMonitor monitor)
      throws MadlModelException {
    if (newName == null) {
      throw new IllegalArgumentException(Messages.element_nullName);
    }
    MadlElement[] elements = new MadlElement[] {this};
    MadlElement[] dests = new MadlElement[] {getParent()};
    String[] renamings = new String[] {newName};
    getMadlModel().rename(elements, dests, renamings, force, monitor);
  }

  @Override
  public IResource resource() {
    return ((MadlElementImpl) getParent()).resource();
  }

  /**
   * Set the occurrence count of this element to the given value. The occurrence count is used to
   * distinguish two elements that would otherwise be indistinguishable, such as two fields with the
   * same name.
   * 
   * @param count the new occurrence count of this element
   */
  public void setOccurrenceCount(int count) {
    occurrenceCount = count;
  }

  @Override
  protected void closing(MadlElementInfo info) throws MadlModelException {
    // Do any necessary cleanup
  }

  @Override
  protected MadlElementInfo createElementInfo() {
    return null; // not used for source ref elements
  }

  // /**
  // * Returns the <code>ASTNode</code> that corresponds to this
  // <code>JavaElement</code>
  // * or <code>null</code> if there is no corresponding node.
  // */
  // public ASTNode findNode(CompilationUnit ast) {
  // DOMFinder finder = new DOMFinder(ast, this, false);
  // try {
  // return finder.search();
  // } catch (MadlModelException e) {
  // // receiver doesn't exist
  // return null;
  // }
  // }

  @Override
  protected void generateInfos(MadlElementInfo info,
      HashMap<MadlElement, MadlElementInfo> newElements, IProgressMonitor pm)
      throws MadlModelException {
    OpenableElementImpl openableParent = (OpenableElementImpl) getOpenableParent();
    if (openableParent == null) {
      return;
    }

  }

  @Override
  protected void toStringName(StringBuilder builder) {
    super.toStringName(builder);
    if (occurrenceCount > 1) {
      builder.append("#"); //$NON-NLS-1$
      builder.append(occurrenceCount);
    }
  }
}
