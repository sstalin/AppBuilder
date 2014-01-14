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
package edu.depaul.cdm.madl.tools.core.internal.model;

import edu.depaul.cdm.madl.compiler.ast.MadlNode;
import edu.depaul.cdm.madl.compiler.ast.MadlUnit;

import edu.depaul.cdm.madl.engine.utilities.source.SourceRange;
import edu.depaul.cdm.madl.tools.core.MadlCore;

import edu.depaul.cdm.madl.tools.core.internal.model.info.MadlElementInfo;
import edu.depaul.cdm.madl.tools.core.internal.util.MementoTokenizer;

import edu.depaul.cdm.madl.tools.core.model.CompilationUnit;
import edu.depaul.cdm.madl.tools.core.model.MadlElement;

import edu.depaul.cdm.madl.tools.core.model.MadlElementVisitor;

import edu.depaul.cdm.madl.tools.core.model.MadlModel;
import edu.depaul.cdm.madl.tools.core.model.MadlModelException;

import edu.depaul.cdm.madl.tools.core.model.MadlModelStatus;

import edu.depaul.cdm.madl.tools.core.model.MadlModelStatusConstants;

import edu.depaul.cdm.madl.tools.core.model.MadlProject;
//import edu.depaul.cdm.madl.tools.core.model.MadlSdkManager;
//import edu.depaul.cdm.madl.tools.core.model.Field;

import edu.depaul.cdm.madl.tools.core.model.OpenableElement;
import edu.depaul.cdm.madl.tools.core.model.ParentElement;
import edu.depaul.cdm.madl.tools.core.model.SourceReference;
import edu.depaul.cdm.madl.tools.core.workingcopy.WorkingCopyOwner;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.core.runtime.jobs.ISchedulingRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Instances of the class <code>MadlElementImpl</code> implement the behavior common to elements
 * within a project that has a Madl nature.
 * 
 * @coverage madl.tools.core.model
 */
public abstract class MadlElementImpl extends PlatformObject implements MadlElement {
  /**
   * The parent of this element.
   */
  private final MadlElement parent;

  /**
   * The character used before the name of a compilation unit.
   */
  public static final char MEMENTO_DELIMITER_COMPILATION_UNIT = '!';

  /*
   * CRITICAL! Do not use colon (:) as a delimiter. It is used in the indexer as a delimiter between
   * the memento and other information. Using it as a delimiter here would break the indexer.
   * 
   * Suggested characters for additional delimiters: ')', '}', '?'
   */

  /**
   * The character used before an integer for elements that have no name.
   */
  public static final char MEMENTO_DELIMITER_COUNT = '#';

  /**
   * The character used to escape the following character from special handling.
   */
  public static final char MEMENTO_DELIMITER_ESCAPE = '\\';

  /**
   * The character used before the name of a field.
   */
  public static final char MEMENTO_DELIMITER_FIELD = '^';

  /**
   * The character used before the name of a function.
   */
  public static final char MEMENTO_DELIMITER_FUNCTION = '(';

  /**
   * The character used before the name of a function type alias.
   */
  public static final char MEMENTO_DELIMITER_FUNCTION_TYPE_ALIAS = '$';

  /**
   * The character used before the name of an HTML file.
   */
  public static final char MEMENTO_DELIMITER_HTML_FILE = '@';

  /**
   * The character used before the name of an import.
   */
  public static final char MEMENTO_DELIMITER_IMPORT = ';';

  /**
   * The character used before the name of an import container.
   */
  public static final char MEMENTO_DELIMITER_IMPORT_CONTAINER = '<';

  /**
   * The character used before the name of a library.
   */
  public static final char MEMENTO_DELIMITER_LIBRARY = '{';

  /**
   * The character used before the name of a library folder.
   */
  public static final char MEMENTO_DELIMITER_LIBRARY_FOLDER = '>';

  /**
   * The character used before the name of an HTML file.
   */
  public static final char MEMENTO_DELIMITER_RESOURCE = '&';

  /**
   * The character used before the name of a method.
   */
  public static final char MEMENTO_DELIMITER_METHOD = '~';

  /**
   * The character used before the name of a project.
   */
  public static final char MEMENTO_DELIMITER_PROJECT = '=';

  /**
   * The character used before the name of a type.
   */
  public static final char MEMENTO_DELIMITER_TYPE = '%';

  /**
   * The character used before the name of a class type alias.
   */
  public static final char MEMENTO_DELIMITER_CLASS_TYPE_ALIAS = '*';

  /**
   * The character used before the name of a variable.
   */
  public static final char MEMENTO_DELIMITER_VARIABLE = '[';

  /**
   * The character used before the name of a type parameter.
   */
  public static final char MEMENTO_DELIMITER_TYPE_PARAMETER = ']';

  /**
   * An empty array of resources.
   */
  // TODO Move this to a utility class.
  protected static final IResource[] NO_RESOURCES = new IResource[0];

  protected static final MadlElementInfo NO_INFO = new MadlElementInfo();

  /**
   * Convert the given list of Madl elements into an array of elements.
   * 
   * @param elements the list of elements to be converted
   * @return an array containing the same elements as the list in the same order
   */
  public static MadlElement[] toArray(List<? extends MadlElement> elements) {
    if (elements.isEmpty()) {
      return EMPTY_ARRAY;
    }
    return elements.toArray(new MadlElement[elements.size()]);
  }

  /**
   * Initialize a newly created element to have the given parent.
   * 
   * @param parent the parent of the element
   */
  protected MadlElementImpl(MadlElement parent) {
    this.parent = parent;
  }

  @Override
  public void accept(MadlElementVisitor visitor) throws MadlModelException {
    if (visitor.visit(this)) {
      MadlElement[] children = getChildren();
      for (MadlElement child : children) {
        child.accept(visitor);
      }
    }
  }

  /**
   * Return <code>true</code> if this element can be removed from the Madl model cache to make
   * space.
   * 
   * @return <code>true</code> if this element can be removed from the Madl model cache
   */
  @Deprecated
  public boolean canBeRemovedFromCache() {
    try {
      return !hasUnsavedChanges();
    } catch (MadlModelException exception) {
      return false;
    }
  }

  public void close() throws MadlModelException {

  }

  /**
   * Return <code>true</code> if this handle represents the same Madl element as the given handle.
   * By default, two handles represent the same element if they are identical or if they represent
   * the same type of element, have equal names, parents, and occurrence counts.
   * <p>
   * If a subclass has other requirements for equality, this method must be overridden.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    // Madl model parent is null
    if (parent == null) {
      return super.equals(o);
    }
    return o instanceof MadlElement && getElementName().equals(((MadlElement) o).getElementName())
        && parent.equals(((MadlElement) o).getParent());
  }

  @Override
  public boolean exists() {
    try {
      getElementInfo();
      return true;
    } catch (MadlModelException exception) {
      // element doesn't exist: return false
    }
    return false;
  }

  /**
   * Return the AST node that corresponds to this element or <code>null</code> if there is no
   * corresponding node.
   * 
   * @param ast the AST node in which to search for the node corresponding to this element
   * @return the AST node that corresponds to this element
   */
  public MadlNode findNode(MadlUnit ast) {
    return null; // works only inside a compilation unit
  }

  @Override
  @SuppressWarnings("unchecked")
  public <E extends MadlElement> E getAncestor(Class<E> ancestorClass) {
    MadlElement current = this;
    do {
      if (ancestorClass.isInstance(current)) {
        return (E) current;
      }
      current = current.getParent();
    } while (current != null);
    return null;
  }

  @Override
  public MadlElement[] getChildren() throws MadlModelException {
    MadlElementInfo elementInfo = getElementInfo();
    if (elementInfo != null) {
      return elementInfo.getChildren();
    } else {
      return EMPTY_ARRAY;
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public <E extends MadlElement> List<E> getChildrenOfType(Class<E> elementClass)
      throws MadlModelException {
    List<E> children = new ArrayList<E>();
    if (MadlSdkManager.getManager().hasSdk()) {
      for (MadlElement child : getChildren()) {
        if (elementClass.isInstance(child)) {
          children.add((E) child);
        }
      }
    }
    return children;
  }

  public CompilationUnit getCompilationUnit() {
    return null;
  }

  @Override
  @Deprecated
  public IResource getCorrespondingResource() throws MadlModelException {
    return null;
  }

  @Override
  public MadlModel getMadlModel() {
    return getAncestor(MadlModel.class);
  }

  @Override
  public MadlProject getMadlProject() {
    return getAncestor(MadlProject.class);
  }

  /**
   * Return the element info associated with this element.
   * 
   * @return the element info associated with this element
   */
  public MadlElementInfo getElementInfo() throws MadlModelException {
    return getElementInfo(null);
  }

  /**
   * Return the element info associated with this element. If this element is not already open, it
   * and all of its parents are opened.
   * 
   * @return the element info associated with this element
   * @throws MadlModelException if the element is not present or not accessible
   */
  public MadlElementInfo getElementInfo(IProgressMonitor monitor) throws MadlModelException {
    return null;
  }

  @Override
  public String getElementName() {
    return ""; //$NON-NLS-1$
  }

  /**
   * Create a Madl element handle from the given tokenizer. The element might not exist. The working
   * copy owner is used only for compilation unit handles.
   * 
   * @param tokenizer the tokenizer being used to parse the memento
   * @param owner the working copy owner to be used if a compilation unit needs to be created
   * @return the element that was created
   */
  public MadlElement getHandleFromMemento(MementoTokenizer tokenizer, WorkingCopyOwner owner) {
    if (!tokenizer.hasMoreTokens()) {
      return this;
    }
    return getHandleFromMemento(tokenizer.nextToken(), tokenizer, owner);
  }

  @Override
  public String getHandleIdentifier() {
    return getHandleMemento();
  }

  public String getHandleMemento() {
    StringBuilder builder = new StringBuilder();
    getHandleMemento(builder);
    return builder.toString();
  }

  @Override
  public OpenableElement getOpenable() {
    return getAncestor(OpenableElement.class);
  }

  public OpenableElement getOpenableParent() {
    return (OpenableElement) getParent(); // getAncestor(OpenableElement.class);
  }

  @Override
  public final MadlElement getParent() {
    return parent;
  }

  @Override
  @Deprecated
  public IPath getPath() {
    MadlCore.notYetImplemented();
    return null;
  }

  @Override
  public MadlElement getPrimaryElement() {
    return getPrimaryElement(true);
  }

  /**
   * Return the primary element. If checkOwner is <code>true</code>, and the compilation unit owner
   * is primary, return this element.
   * 
   * @param checkOwner <code>true</code> if children of a primary compilation unit should be
   *          returned directly
   */
  public MadlElement getPrimaryElement(boolean checkOwner) {
    return this;
  }

  @Override
  public IResource getResource() {
    return resource();
  }

  @Override
  public ISchedulingRule getSchedulingRule() {
    IResource resource = resource();
    if (resource == null) {
      class NoResourceSchedulingRule implements ISchedulingRule {
        public IPath path;

        public NoResourceSchedulingRule(IPath path) {
          this.path = path;
        }

        @Override
        public boolean contains(ISchedulingRule rule) {
          if (rule instanceof NoResourceSchedulingRule) {
            return path.isPrefixOf(((NoResourceSchedulingRule) rule).path);
          } else {
            return false;
          }
        }

        @Override
        public boolean isConflicting(ISchedulingRule rule) {
          if (rule instanceof NoResourceSchedulingRule) {
            IPath otherPath = ((NoResourceSchedulingRule) rule).path;
            return path.isPrefixOf(otherPath) || otherPath.isPrefixOf(path);
          } else {
            return false;
          }
        }
      }
      return new NoResourceSchedulingRule(getPath());
    } else {
      return resource;
    }
  }

  public boolean hasChildren() throws MadlModelException {
    // If this element is not open, return true to avoid opening (case of a Madl
    // project, a compilation unit or a class file).
    // also see https://bugs.eclipse.org/bugs/show_bug.cgi?id=52474

    return false;
  }

  // This hashCode method is called _a lot_.
  @Override
  public int hashCode() {
    if (parent == null) {
      return super.hashCode();
    } else {
      // Inlined from Util.combineHashCodes()
      return getElementName().hashCode() * 17 + parent.hashCode();
    }
  }

  @Deprecated
  public boolean hasUnsavedChanges() throws MadlModelException {
    MadlCore.notYetImplemented();
    // if (isReadOnly() || !isOpen()) {
    // return false;
    // }
    // Buffer buf = getBuffer();
    // if (buf != null && buf.hasUnsavedChanges()) {
    // return true;
    // }
    // // for package fragments, package fragment roots, and projects must check
    // open buffers
    // // to see if they have an child with unsaved changes
    // int elementType = getElementType();
    // if (elementType == PACKAGE_FRAGMENT ||
    // elementType == PACKAGE_FRAGMENT_ROOT ||
    // elementType == MADL_PROJECT ||
    // elementType == MADL_MODEL) { // fix for 1FWNMHH
    // Enumeration openBuffers= getBufferManager().getOpenBuffers();
    // while (openBuffers.hasMoreElements()) {
    // Buffer buffer= (Buffer)openBuffers.nextElement();
    // if (buffer.hasUnsavedChanges()) {
    // MadlElement owner= (MadlElement)buffer.getOwner();
    // if (isAncestorOf(owner)) {
    // return true;
    // }
    // }
    // }
    // }
    return false;
  }

  /**
   * Return <code>true</code> if this element is an ancestor of the given element.
   * 
   * @param element the element being tested
   * @return <code>true</code> if this element is an ancestor of the given element
   */
  public boolean isAncestorOf(MadlElement element) {
    MadlElement parentElement = element.getParent();
    while (parentElement != null) {
      if (parentElement.equals(this)) {
        return true;
      }
      parentElement = parentElement.getParent();
    }
    return false;
  }

  @Override
  public boolean isInSdk() {
    return false;
  }

  @Deprecated
  public boolean isOpen() {
    return false;
  }

  @Override
  public boolean isReadOnly() {
    return false;
  }

  @Override
  @Deprecated
  public boolean isStructureKnown() throws MadlModelException {
    MadlCore.notYetImplemented();
    return true;
  }

  /**
   * Create and return a new Madl model exception for this element with the given status.
   * 
   * @param status the status that explains the cause of the exception
   * @return the newly created exception
   */
  public MadlModelException newMadlModelException(IStatus status) {
    if (status instanceof MadlModelStatus) {
      return new MadlModelException((MadlModelStatus) status);
    } else {
      return new MadlModelException(new MadlModelStatusImpl(
          status.getSeverity(),
          status.getCode(),
          status.getMessage()));
    }
  }

  /**
   * Opens an <code>Openable</code> that is known to be closed (no check for <code>isOpen()</code>).
   * Returns the created element info.
   */
  public MadlElementInfo openWhenClosed(MadlElementInfo info, IProgressMonitor monitor)
      throws MadlModelException {
    return null;
  }

  public abstract IResource resource();

  /**
   * Debugging purposes
   */
  public String toDebugString() {
    StringBuilder builder = new StringBuilder();
    toStringInfo(0, builder, NO_INFO, true);
    return builder.toString();
  }

  /**
   * Debugging purposes
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    toString(0, builder);
    return builder.toString();
  }

  /**
   * Debugging purposes
   */
  public MadlElementInfo toStringInfo(int tab, StringBuilder builder) {
    return null;
  }

  /**
   * Debugging purposes
   */
  public String toStringWithAncestors() {
    return toStringWithAncestors(true);
  }

  /**
   * Debugging purposes
   */
  public String toStringWithAncestors(boolean showResolvedInfo) {
    StringBuilder builder = new StringBuilder();
    toStringInfo(0, builder, NO_INFO, showResolvedInfo);
    toStringAncestors(builder);
    return builder.toString();
  }

  /**
   * This element is being closed. Do any necessary cleanup.
   * 
   * @param info the information associates with this element
   * @throws MadlModelException if the element cannot be closed
   */
  protected void closing(MadlElementInfo info) throws MadlModelException {
    // Place holder so that implementations in subclasses can invoke the
    // overridden method.
  }

  /**
   * Create a new element info for this element.
   * 
   * @return the element that was created
   */
  protected abstract MadlElementInfo createElementInfo();

  /**
   * Generate the element info objects for this element, its ancestors (if they are not opened) and
   * its children (if it is an Openable). Puts the newly created element info in the given map.
   */
  protected abstract void generateInfos(MadlElementInfo info,
      HashMap<MadlElement, MadlElementInfo> newElements, IProgressMonitor pm)
      throws MadlModelException;

  /**
   * Create a Madl element handle from the given tokenizer. The element might not exist. The working
   * copy owner is used only for compilation unit handles.
   * 
   * @param token the current delimiter indicating the type of the next token(s)
   * @param tokenizer the tokenizer being used to parse the memento
   * @param owner the working copy owner to be used if a compilation unit needs to be created
   * @return the element that was created
   */
  protected abstract MadlElement getHandleFromMemento(String token, MementoTokenizer tokenizer,
      WorkingCopyOwner owner);

  /**
   * Use the given string builder to create the identifier for this element.
   * 
   * @param builder the builder to which this element's
   */
  protected void getHandleMemento(StringBuilder builder) {
    if (parent instanceof MadlElementImpl) {
      ((MadlElementImpl) parent).getHandleMemento(builder);
    }
    builder.append(getHandleMementoDelimiter());
    escapeMementoName(builder, getHandleMementoName());
  }

  /**
   * Return the character that marks the start of this handle's contribution to a memento.
   * 
   * @return the character that marks the start of this handle's contribution to a memento
   */
  protected abstract char getHandleMementoDelimiter();

  /**
   * Return the name that should be used when constructing a memento.
   * 
   * @return the name that should be used when constructing a memento
   */
  protected String getHandleMementoName() {
    return getElementName();
  }

  /**
   * Return the element that is located at the given source position in this element. This is a
   * helper method for <code>CompilationUnitImpl#getElementAt</code>, and only works on compilation
   * units and types. The position given is known to be within this element's source range already,
   * and if no finer grained element is found at the position, this element is returned.
   */
  protected MadlElement getSourceElementAt(int position) throws MadlModelException {
    if (this instanceof SourceReference) {
      MadlElement[] children = getChildren();
      for (int i = children.length - 1; i >= 0; i--) {
        MadlElement aChild = children[i];
        if (aChild instanceof SourceReferenceImpl) {
          SourceReferenceImpl child = (SourceReferenceImpl) children[i];
          SourceRange range = child.getSourceRange();
          int start = range.getOffset();
          int end = start + range.getLength();
          if (start <= position && position <= end) {
            if (child instanceof Field) {
              // check muti-declaration case (see
              // https://bugs.eclipse.org/bugs/show_bug.cgi?id=39943)
              int declarationStart = start;
              SourceReferenceImpl candidate = null;
              do {
                // check name range
                range = ((Field) child).getNameRange();
                if (position <= range.getOffset() + range.getLength()) {
                  candidate = child;
                } else {
                  return candidate == null ? child.getSourceElementAt(position)
                      : candidate.getSourceElementAt(position);
                }
                child = --i >= 0 ? (SourceReferenceImpl) children[i] : null;
              } while (child != null && child.getSourceRange().getOffset() == declarationStart);
              // position in field's type: use first field
              return candidate.getSourceElementAt(position);
            } else if (child instanceof ParentElement) {
              return child.getSourceElementAt(position);
            } else {
              return child;
            }
          }
        }
      }
    } else {
      // should not happen
      Assert.isTrue(false);
    }
    return this;
  }

  protected MadlModelStatus newDoesNotExistStatus() {
    return new MadlModelStatusImpl(MadlModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this);
  }

  protected MadlModelException newNotPresentException() {
    return new MadlModelException(newDoesNotExistStatus());
  }

  /**
   * Debugging purposes
   */
  protected String tabString(int tab) {
    StringBuilder builder = new StringBuilder();
    for (int i = tab; i > 0; i--) {
      builder.append("  "); //$NON-NLS-1$
    }
    return builder.toString();
  }

  /**
   * Debugging purposes
   * 
   * @param showResolvedInfo unused
   */
  protected void toStringInfo(int tab, StringBuilder builder, MadlElementInfo info,
      boolean showResolvedInfo) {
    builder.append(tabString(tab));
    toStringName(builder);
    if (info == null) {
      builder.append(" (not open)"); //$NON-NLS-1$
    }
    toStringType(builder);
  }

  /**
   * Debugging purposes
   */
  protected void toStringName(StringBuilder builder) {
    builder.append(getElementName());
  }

  /**
   * Debugging purposes
   */
  protected void toStringType(StringBuilder builder) {
    builder.append(" [" + getClass().getSimpleName() + "]");
  }

  /**
   * Append the characters in the given memento name to the given builder, escaping any characters
   * that would otherwise be interpreted as either a delimiter or as the escape character.
   * 
   * @param builder the builder to which the memento name is to be appended
   * @param mementoName the name to be escaped and appended to the builder
   */
  private void escapeMementoName(StringBuilder builder, String mementoName) {
    int length = mementoName.length();
    for (int i = 0; i < length; i++) {
      char character = mementoName.charAt(i);
      switch (character) {
        case MEMENTO_DELIMITER_CLASS_TYPE_ALIAS:
        case MEMENTO_DELIMITER_COMPILATION_UNIT:
        case MEMENTO_DELIMITER_COUNT:
        case MEMENTO_DELIMITER_ESCAPE:
        case MEMENTO_DELIMITER_FIELD:
        case MEMENTO_DELIMITER_FUNCTION:
        case MEMENTO_DELIMITER_FUNCTION_TYPE_ALIAS:
        case MEMENTO_DELIMITER_HTML_FILE:
        case MEMENTO_DELIMITER_IMPORT:
        case MEMENTO_DELIMITER_IMPORT_CONTAINER:
        case MEMENTO_DELIMITER_LIBRARY:
        case MEMENTO_DELIMITER_LIBRARY_FOLDER:
        case MEMENTO_DELIMITER_VARIABLE:
        case MEMENTO_DELIMITER_METHOD:
        case MEMENTO_DELIMITER_PROJECT:
        case MEMENTO_DELIMITER_TYPE:
        case MEMENTO_DELIMITER_TYPE_PARAMETER:
          builder.append(MEMENTO_DELIMITER_ESCAPE);
      }
      builder.append(character);
    }
  }

  /**
   * Debugging purposes
   */
  private void toString(int tab, StringBuilder builder) {
    MadlElementInfo info = toStringInfo(tab, builder);
    if (tab == 0) {
      toStringAncestors(builder);
    }
    toStringChildren(tab, builder, info);
  }

  /**
   * Debugging purposes
   */
  private void toStringAncestors(StringBuilder builder) {
    MadlElementImpl parentElement = (MadlElementImpl) getParent();
    if (parentElement != null && parentElement.getParent() != null) {
      builder.append(" [in "); //$NON-NLS-1$
      parentElement.toStringInfo(0, builder, NO_INFO, false);
      parentElement.toStringAncestors(builder);
      builder.append("]"); //$NON-NLS-1$
    }
  }

  /**
   * Debugging purposes
   */
  private void toStringChildren(int tab, StringBuilder builder, MadlElementInfo info) {
    if (info == null) {
      return;
    }
    MadlElement[] children = info.getChildren();
    for (int i = 0; i < children.length; i++) {
      builder.append("\n"); //$NON-NLS-1$
      ((MadlElementImpl) children[i]).toString(tab + 1, builder);
    }
  }
}
