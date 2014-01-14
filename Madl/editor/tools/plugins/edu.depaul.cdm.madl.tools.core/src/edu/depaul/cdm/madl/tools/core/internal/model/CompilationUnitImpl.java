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

import com.google.common.collect.Lists;
import edu.depaul.cdm.madl.compiler.ast.MadlUnit;

import edu.depaul.cdm.madl.tools.core.MadlCore;
import edu.depaul.cdm.madl.tools.core.buffer.Buffer;
import edu.depaul.cdm.madl.tools.core.completion.CompletionRequestor;
import edu.depaul.cdm.madl.tools.core.internal.buffer.BufferManager;

import edu.depaul.cdm.madl.tools.core.internal.model.info.CompilationUnitInfo;
import edu.depaul.cdm.madl.tools.core.internal.model.info.MadlElementInfo;
import edu.depaul.cdm.madl.tools.core.internal.model.info.OpenableElementInfo;
import edu.depaul.cdm.madl.tools.core.internal.problem.CategorizedProblem;

import edu.depaul.cdm.madl.tools.core.internal.util.CharOperation;

import edu.depaul.cdm.madl.tools.core.internal.util.MementoTokenizer;
import edu.depaul.cdm.madl.tools.core.internal.util.Messages;
import edu.depaul.cdm.madl.tools.core.internal.util.ResourceUtil;

import edu.depaul.cdm.madl.tools.core.internal.util.Util;

import edu.depaul.cdm.madl.tools.core.internal.workingcopy.DefaultWorkingCopyOwner;

import edu.depaul.cdm.madl.tools.core.model.CompilationUnit;

import edu.depaul.cdm.madl.tools.core.model.MadlConventions;

import edu.depaul.cdm.madl.tools.core.model.MadlElement;

import edu.depaul.cdm.madl.tools.core.model.MadlFunctionTypeAlias;
import edu.depaul.cdm.madl.tools.core.model.MadlLibrary;

import edu.depaul.cdm.madl.tools.core.model.MadlModelException;
import edu.depaul.cdm.madl.tools.core.model.MadlModelStatusConstants;

import edu.depaul.cdm.madl.tools.core.model.MadlVariableDeclaration;

import edu.depaul.cdm.madl.tools.core.model.Type;
import edu.depaul.cdm.madl.tools.core.problem.ProblemRequestor;
import edu.depaul.cdm.madl.tools.core.workingcopy.WorkingCopyOwner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Instances of the class <code>CompilationUnitImpl</code> implement the representation of files
 * containing Madl source code that needs to be compiled.
 * 
 * @coverage madl.tools.core.model
 */
public class CompilationUnitImpl extends SourceFileElementImpl<CompilationUnit> implements
    CompilationUnit {
  /**
   * An empty array of compilation units.
   */
  public static final CompilationUnitImpl[] EMPTY_ARRAY = new CompilationUnitImpl[0];

  /**
   * Return the file associated with the given URI.
   * 
   * @param uri the uri of the file represented by the compilation unit
   * @return the file associated with the given URI
   */
  private static IFile getFile(URI uri) {
    IFile file = ResourceUtil.getFile(uri);
    if (file == null) {
      throw new IllegalArgumentException("The URI \"" + uri
          + "\" does not map to an existing resource.");
    }
    return file;
  }

  /**
   * Initialize a newly created compilation unit to be an element in the given container.
   * 
   * @param container the library or library folder containing the compilation unit
   * @param file the file represented by the compilation unit
   * @param owner the working copy owner
   */
  public CompilationUnitImpl(CompilationUnitContainer container, IFile file, WorkingCopyOwner owner) {
    super(container, file, owner);
  }

  /**
   * Initialize a newly created compilation unit to be an element in the given container.
   * 
   * @param container the library or library folder containing the compilation unit
   * @param uri the uri of the file represented by the compilation unit
   * @param owner the working copy owner
   */
  public CompilationUnitImpl(CompilationUnitContainer container, URI uri, WorkingCopyOwner owner) {
    super(container, getFile(uri), owner);
  }

  @Override
  public void becomeWorkingCopy(ProblemRequestor problemRequestor, IProgressMonitor monitor)
      throws MadlModelException {

  }

  @Override
  public boolean canBeRemovedFromCache() {

    return super.canBeRemovedFromCache();
  }

  @Override
  public boolean canBufferBeRemovedFromCache(Buffer buffer) {

    return super.canBufferBeRemovedFromCache(buffer);
  }

  /**
   * Clone this handle so that it caches its contents in memory.
   * <p>
   * DO NOT PASS TO CLIENTS
   */
  public CompilationUnitImpl cloneCachingContents() {
    return new CompilationUnitImpl((MadlLibraryImpl) getParent(), getFile(), this.owner) {
      private char[] cachedContents;

      @Override
      public char[] getContents() {
        if (this.cachedContents == null) {
          this.cachedContents = CompilationUnitImpl.this.getContents();
        }
        return this.cachedContents;
      }

//      public CompilationUnit originalFromClone() {
//        return CompilationUnitImpl.this;
//      }
    };
  }

  @Override
  public void close() throws MadlModelException {

    super.close();
  }

  @Override
  public void codeComplete(int offset, CompletionRequestor requestor) throws MadlModelException {
    codeComplete(offset, requestor, DefaultWorkingCopyOwner.getInstance());
  }

  @Override
  public void codeComplete(int offset, CompletionRequestor requestor, IProgressMonitor monitor)
      throws MadlModelException {
    codeComplete(offset, requestor, DefaultWorkingCopyOwner.getInstance(), monitor);
  }

  @Override
  public void codeComplete(int offset, CompletionRequestor requestor,
      WorkingCopyOwner workingCopyOwner) throws MadlModelException {
    codeComplete(offset, requestor, workingCopyOwner, null);
  }

  @Override
  public void codeComplete(int offset, CompletionRequestor requestor,
      WorkingCopyOwner workingCopyOwner, IProgressMonitor monitor) throws MadlModelException {
    CompilationUnit orig = isWorkingCopy() ? (CompilationUnit) getOriginalElement() : this;
    codeComplete(this, orig, offset, requestor, workingCopyOwner, monitor);
  }

  @Override
  public MadlElement[] codeSelect(MadlUnit ast, int offset, int length,
      WorkingCopyOwner workingCopyOwner) throws MadlModelException {
    //TODO (pquitslund): remove
    return null;
  }

  @Override
  public MadlElement[] codeSelect(int offset, int length) throws MadlModelException {
    return codeSelect(offset, length, DefaultWorkingCopyOwner.getInstance());
  }

  @Override
  public MadlElement[] codeSelect(int offset, int length, WorkingCopyOwner workingCopyOwner)
      throws MadlModelException {
    return codeSelect(null, offset, length, workingCopyOwner);
  }

  @Override
  public void commitWorkingCopy(boolean force, IProgressMonitor monitor) throws MadlModelException {

  }

  @Override
  public void copy(MadlElement container, MadlElement sibling, String rename, boolean force,
      IProgressMonitor monitor) throws MadlModelException {
    if (container == null) {
      throw new IllegalArgumentException(Messages.operation_nullContainer);
    }
    MadlElement[] elements = new MadlElement[] {this};
    MadlElement[] containers = new MadlElement[] {container};
    String[] renamings = null;
    if (rename != null) {
      renamings = new String[] {rename};
    }
    getMadlModel().copy(elements, containers, null, renamings, force, monitor);
  }

  @Override
  public MadlElementInfo createElementInfo() {
    return new CompilationUnitInfo();
  }

  @Override
  public Type createType(String content, MadlElement sibling, boolean force,
      IProgressMonitor monitor) throws MadlModelException {
    MadlCore.notYetImplemented();
    return null;
    // if (!exists()) {
    // // autogenerate this compilation unit
    // IPackageFragment pkg = (IPackageFragment) getParent();
    //      String source = ""; //$NON-NLS-1$
    // if (!pkg.isDefaultPackage()) {
    // // not the default package...add the package declaration
    // String lineSeparator = Util.getLineSeparator(
    // null/* no existing source */, getMadlProject());
    //        source = "package " + pkg.getElementName() + ";" + lineSeparator + lineSeparator; //$NON-NLS-1$ //$NON-NLS-2$
    // }
    // CreateCompilationUnitOperation op = new CreateCompilationUnitOperation(
    // pkg, name, source, force);
    // op.runOperation(monitor);
    // }
    // CreateTypeOperation op = new CreateTypeOperation(this, content, force);
    // if (sibling != null) {
    // op.createBefore(sibling);
    // }
    // op.runOperation(monitor);
    // return (Type) op.getResultElements()[0];
  }

  @Override
  public boolean definesLibrary() {
    try {
      return ((CompilationUnitInfo) getElementInfo()).getDefinesLibrary();
    } catch (MadlModelException exception) {
      return false;
    }
  }

  @Override
  public void delete(boolean force, IProgressMonitor monitor) throws MadlModelException {
    MadlElement[] elements = new MadlElement[] {this};
    getMadlModel().delete(elements, force, monitor);
  }

  @Override
  public void discardWorkingCopy() throws MadlModelException {

  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof CompilationUnitImpl)) {
      return false;
    }

    return super.equals(obj) && getPath().equals(((CompilationUnitImpl) obj).getPath());

  }

  /**
   * Finds the elements in this Madl file that correspond to the given element. An element A
   * corresponds to an element B if:
   * <ul>
   * <li>A has the same element name as B.
   * <li>If A is a method, A must have the same number of arguments as B and the simple names of the
   * argument types must be equal, if known.
   * <li>The parent of A corresponds to the parent of B recursively up to their respective Madl
   * files.
   * <li>A exists.
   * </ul>
   * Returns <code>null</code> if no such Madl elements can be found or if the given element is not
   * included in a Madl file.
   * 
   * @param element the given element
   * @return the found elements in this Madl file that correspond to the given element
   */
  @Override
  public MadlElement[] findElements(MadlElement element) {
    List<MadlElement> children = Lists.newArrayList();
    while (element != null && element.getElementType() != MadlElement.COMPILATION_UNIT) {
      children.add(element);
      element = element.getParent();
    }
    if (element == null) {
      return null;
    }
    MadlElement currentElement = this;
    for (int i = children.size() - 1; i >= 0; i--) {
      MadlCore.notYetImplemented();
      // SourceRefElement child = (SourceRefElement) children.get(i);
      // switch (child.getElementType()) {
      // case MadlElement.PACKAGE_DECLARATION:
      // currentElement = ((CompilationUnit)
      // currentElement).getPackageDeclaration(child.getElementName());
      // break;
      // case MadlElement.IMPORT_CONTAINER:
      // currentElement = ((CompilationUnit)
      // currentElement).getImportContainer();
      // break;
      // case MadlElement.IMPORT_DECLARATION:
      // currentElement = ((ImportContainer)
      // currentElement).getImport(child.getElementName());
      // break;
      // case MadlElement.TYPE:
      // switch (currentElement.getElementType()) {
      // case MadlElement.COMPILATION_UNIT:
      // currentElement = ((CompilationUnit)
      // currentElement).getType(child.getElementName());
      // break;
      // case MadlElement.TYPE:
      // currentElement = ((Type)
      // currentElement).getType(child.getElementName());
      // break;
      // case MadlElement.FIELD:
      // case MadlElement.INITIALIZER:
      // case MadlElement.METHOD:
      // currentElement = ((Member) currentElement).getType(
      // child.getElementName(), child.occurrenceCount);
      // break;
      // }
      // break;
      // case MadlElement.INITIALIZER:
      // currentElement = ((Type)
      // currentElement).getInitializer(child.occurrenceCount);
      // break;
      // case MadlElement.FIELD:
      // currentElement = ((Type)
      // currentElement).getField(child.getElementName());
      // break;
      // case MadlElement.METHOD:
      // currentElement = ((Type) currentElement).getMethod(
      // child.getElementName(), ((Method) child).getParameterTypes());
      // break;
      // }
    }
    if (currentElement != null && currentElement.exists()) {
      return new MadlElement[] {currentElement};
    } else {
      return null;
    }
  }

  @Override
  public CompilationUnit findWorkingCopy(WorkingCopyOwner workingCopyOwner) {
    CompilationUnitImpl cu = new CompilationUnitImpl(
        (MadlLibraryImpl) getParent(),
        getFile(),
        workingCopyOwner);
    return cu;
  }

  @Override
  public edu.depaul.cdm.madl.tools.core.model.MadlClassTypeAlias[] getClassTypeAliases()
      throws MadlModelException {
    List<edu.depaul.cdm.madl.tools.core.model.MadlClassTypeAlias> typeList = getChildrenOfType(edu.depaul.cdm.madl.tools.core.model.MadlClassTypeAlias.class);
    return typeList.toArray(new edu.depaul.cdm.madl.tools.core.model.MadlClassTypeAlias[typeList.size()]);
  }

  @Override
  public CompilationUnit getCompilationUnit() {
    return this;
  }

  public char[] getContents() {
    Buffer buffer = getBufferManager().getBuffer(this);
    if (buffer == null) {
      // no need to force opening of CU to get the content
      // also this cannot be a working copy, as its buffer is never closed while
      // the working copy is alive
      IFile file = (IFile) getResource();
      // Get encoding from file
      String encoding;
      try {
        encoding = file.getCharset();
      } catch (CoreException ce) {
        // do not use any encoding
        encoding = null;
      }
      try {
        return Util.getResourceContentsAsCharArray(file, encoding);
      } catch (MadlModelException e) {
        // if (MadlModelManager.getInstance().abortOnMissingSource.get() ==
        // Boolean.TRUE) {
        // IOException ioException = e.getMadlModelStatus().getCode() ==
        // MadlModelStatusConstants.IO_EXCEPTION
        // ? (IOException) e.getException()
        // : new IOException(e.getMessage());
        // throw new AbortCompilationUnit(null, ioException, encoding);
        // } else {
        // Util.log(e, Messages.bind(Messages.file_notFound,
        // file.getFullPath().toString()));
        // }
        MadlCore.notYetImplemented();
        return CharOperation.NO_CHAR;
      }
    }
    char[] contents = buffer.getCharacters();
    if (contents == null) {
      // see https://bugs.eclipse.org/bugs/show_bug.cgi?id=129814
      // if (MadlModelManager.getInstance().abortOnMissingSource.get() ==
      // Boolean.TRUE) {
      // IOException ioException = new IOException(Messages.buffer_closed);
      // IFile file = (IFile) getResource();
      // // Get encoding from file
      // String encoding;
      // try {
      // encoding = file.getCharset();
      // } catch (CoreException ce) {
      // // do not use any encoding
      // encoding = null;
      // }
      // throw new AbortCompilationUnit(null, ioException, encoding);
      // }
      MadlCore.notYetImplemented();
      return CharOperation.NO_CHAR;
    }
    return contents;
  }

  @Override
  public MadlElement getElementAt(int position) throws MadlModelException {
    MadlElement element = getSourceElementAt(position);
    if (element == this) {
      return null;
    } else {
      return element;
    }
  }

  @Override
  public int getElementType() {
    return MadlElement.COMPILATION_UNIT;
  }

  public char[] getFileName() {
    return getPath().toString().toCharArray();
  }

  @Override
  public MadlFunctionTypeAlias[] getFunctionTypeAliases() throws MadlModelException {
    List<MadlFunctionTypeAlias> typeList = getChildrenOfType(MadlFunctionTypeAlias.class);
    return typeList.toArray(new MadlFunctionTypeAlias[typeList.size()]);
  }

  @Override
  public MadlVariableDeclaration[] getGlobalVariables() throws MadlModelException {
    List<MadlVariableDeclaration> variableList = getChildrenOfType(MadlVariableDeclaration.class);
    return variableList.toArray(new MadlVariableDeclaration[variableList.size()]);
  }

  @Override
  public MadlLibrary getLibrary() {
    return getAncestor(MadlLibrary.class);
  }

  @Override
  public MadlElement getPrimaryElement(boolean checkOwner) {
    if (checkOwner && isPrimary()) {
      return this;
    }
    return new CompilationUnitImpl(
        (MadlLibraryImpl) getParent(),
        getFile(),
        DefaultWorkingCopyOwner.getInstance());
  }

  @Override
  public Type getType(String typeName) {
    return new MadlTypeImpl(this, typeName);
  }

  @Override
  public Type[] getTypes() throws MadlModelException {
    List<Type> typeList = getChildrenOfType(Type.class);
    return typeList.toArray(new Type[typeList.size()]);
  }

  @Override
  public IResource getUnderlyingResource() throws MadlModelException {
    if (isWorkingCopy() && !isPrimary()) {
      return null;
    }
    return super.getUnderlyingResource();
  }

  @Override
  public int hashCode() {
    if (getPath() != null) {
      return Util.combineHashCodes(getPath().hashCode(), super.hashCode());
    }
    return super.hashCode();
  }

  @Override
  public boolean hasMain() throws MadlModelException {
    List<edu.depaul.cdm.madl.tools.core.model.MadlFunction> functions = getChildrenOfType(edu.depaul.cdm.madl.tools.core.model.MadlFunction.class);

    for (edu.depaul.cdm.madl.tools.core.model.MadlFunction function : functions) {
      if (function.isMain()) {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean hasResourceChanged() {

    return false;
  }

  /**
   * Return <code>true</code> if the element is consistent with its underlying resource or buffer.
   * The element is consistent when opened, and is consistent if the underlying resource or buffer
   * has not been modified since it was last consistent.
   * <p>
   * NOTE: Child consistency is not considered. For example, a package fragment responds
   * <code>true</code> when it knows about all of its compilation units present in its underlying
   * folder. However, one or more of the compilation units could be inconsistent.
   * 
   * @return <code>true</code> if the element is consistent with its underlying resource or buffer
   */
  @Override
  public boolean isConsistent() {
    return false;
  }

  public MadlUnit makeConsistent(boolean resolveBindings, boolean forceProblemDetection,
      Map<String, CategorizedProblem[]> problems, IProgressMonitor monitor)
      throws MadlModelException {

    return null;

  }

  @Override
  public void makeConsistent(IProgressMonitor monitor) throws MadlModelException {
    makeConsistent(false, false, null, monitor);
  }

  @Override
  public void move(MadlElement container, MadlElement sibling, String rename, boolean force,
      IProgressMonitor monitor) throws MadlModelException {
    if (container == null) {
      throw new IllegalArgumentException(Messages.operation_nullContainer);
    }
    MadlElement[] elements = new MadlElement[] {this};
    MadlElement[] containers = new MadlElement[] {container};

    String[] renamings = null;
    if (rename != null) {
      renamings = new String[] {rename};
    }
    getMadlModel().move(elements, containers, null, renamings, force, monitor);
  }

  public void reconcile(boolean forceProblemDetection, IProgressMonitor monitor)
      throws MadlModelException {
    reconcile(forceProblemDetection, null, monitor);
  }

  public MadlUnit reconcile(boolean forceProblemDetection, WorkingCopyOwner workingCopyOwner,
      IProgressMonitor monitor) throws MadlModelException {

    return null;

  }

  @Override
  public void rename(String newName, boolean force, IProgressMonitor monitor)
      throws MadlModelException {
    if (newName == null) {
      throw new IllegalArgumentException(Messages.operation_nullName);
    }
    MadlElement[] elements = new MadlElement[] {this};
    MadlElement[] dests = new MadlElement[] {getParent()};
    String[] renamings = new String[] {newName};
    getMadlModel().rename(elements, dests, renamings, force, monitor);
  }

  @Override
  public void restore() throws MadlModelException {
    if (!isWorkingCopy()) {
      return;
    }
    CompilationUnitImpl original = (CompilationUnitImpl) getOriginalElement();
    Buffer buffer = getBuffer();
    if (buffer == null) {
      return;
    }
    buffer.setContents(original.getContents());
    updateTimeStamp(original);
    makeConsistent(null);
  }

  @Override
  public void save(IProgressMonitor monitor, boolean force) throws MadlModelException {
    if (isWorkingCopy()) {
      // No need to save the buffer for a working copy (this is a noop). Not
      // simply makeConsistent, also computes fine-grain deltas in case the
      // working copy is being reconciled already (if not it would miss one
      // iteration of deltas).
      reconcile(false, null, null);
    } else {
      super.save(monitor, force);
    }
  }

  public void updateTimeStamp(CompilationUnitImpl original) throws MadlModelException {
    if (original == null) {
      return;
    }
    IFile originalResource = (IFile) original.getResource();
    if (originalResource == null) {
      return;
    }
    long timeStamp = originalResource.getModificationStamp();
    if (timeStamp == IResource.NULL_STAMP) {
      throw new MadlModelException(new MadlModelStatusImpl(
          MadlModelStatusConstants.INVALID_RESOURCE));
    }
    ((CompilationUnitInfo) getElementInfo()).setTimestamp(timeStamp);
  }

  @Override
  protected boolean buildStructure(OpenableElementInfo info, IProgressMonitor pm,
      Map<MadlElement, MadlElementInfo> newElements, IResource underlyingResource)
      throws MadlModelException {
    CompilationUnitInfo unitInfo = (CompilationUnitInfo) info;
    //
    // Ensure that the buffer is opened so that it can be accessed indirectly
    // later.
    //
    Buffer buffer = getBufferManager().getBuffer(this);
    if (buffer == null) {
      // Open the buffer independently from the info, since we are building the
      // info
      openBuffer(pm, unitInfo);
    }
    return false;
  }

  @Override
  protected void closing(MadlElementInfo info) throws MadlModelException {

    // else the buffer of a working copy must remain open for the lifetime of
    // the working copy
  }

  @Override
  protected MadlElement getHandleFromMemento(String token, MementoTokenizer tokenizer,
      WorkingCopyOwner owner) {

    return null;
  }

  @Override
  protected char getHandleMementoDelimiter() {
    return MEMENTO_DELIMITER_COMPILATION_UNIT;
  }

  @Override
  protected String getHandleMementoName() {
    // Because the compilation unit can be anywhere relative to the library or
    // application that contains it we need to specify the full path.
    return getFile().getProjectRelativePath().toPortableString();
  }

  @Override
  protected CompilationUnit getWorkingCopy(WorkingCopyOwner workingCopyOwner,
      ProblemRequestor problemRequestor, IProgressMonitor monitor) throws MadlModelException {

    return this;

  }

  @Override
  protected boolean hasBuffer() {
    return true;
  }

  @Override
  protected boolean isSourceElement() {
    return true;
  }

  @Override
  protected void openAncestors(HashMap<MadlElement, MadlElementInfo> newElements,
      IProgressMonitor monitor) throws MadlModelException {
    if (!isWorkingCopy()) {
      super.openAncestors(newElements, monitor);
    }
    // else don't open ancestors for a working copy to speed up the first
    // becomeWorkingCopy
    // (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=89411)
  }

  @Override
  protected Buffer openBuffer(IProgressMonitor pm, MadlElementInfo info) throws MadlModelException {
    // create buffer
    BufferManager bufManager = getBufferManager();
    boolean isWorkingCopy = isWorkingCopy();
    Buffer buffer = isWorkingCopy ? owner.createBuffer(this) : BufferManager.createBuffer(this);
    if (buffer == null) {
      return null;
    }
    CompilationUnitImpl original = null;
    boolean mustSetToOriginalContent = false;
    if (isWorkingCopy) {
      // ensure that isOpen() is called outside the bufManager synchronized
      // block see https://bugs.eclipse.org/bugs/show_bug.cgi?id=237772
      mustSetToOriginalContent = !isPrimary()
          && (original = new CompilationUnitImpl(
              (MadlLibraryImpl) getParent(),
              getFile(),
              DefaultWorkingCopyOwner.getInstance())).isOpen();
    }
    // synchronize to ensure that 2 threads are not putting 2 different buffers
    // at the same time see https://bugs.eclipse.org/bugs/show_bug.cgi?id=146331
    synchronized (bufManager) {
      Buffer existingBuffer = bufManager.getBuffer(this);
      if (existingBuffer != null) {
        return existingBuffer;
      }
      // set the buffer source
      if (buffer.getCharacters() == null) {
        if (mustSetToOriginalContent) {
          buffer.setContents(original.getSource());
        } else {
          readBuffer(buffer, isWorkingCopy);
        }
      }

      // add buffer to buffer cache
      // note this may cause existing buffers to be removed from the buffer
      // cache, but only primary compilation unit's buffer
      // can be closed, thus no call to a client's IBuffer#close() can be done
      // in this synchronized block.
      bufManager.addBuffer(buffer);

      // listen to buffer changes
      buffer.addBufferChangedListener(this);
    }
    return buffer;
  }

  /**
   * Debugging purposes
   */
  @Override
  protected void toStringInfo(int tab, StringBuilder builder, MadlElementInfo info,
      boolean showResolvedInfo) {
    if (!isPrimary()) {
      builder.append(tabString(tab));
      builder.append("[Working copy] "); //$NON-NLS-1$
      toStringName(builder);
    } else {
      if (isWorkingCopy()) {
        builder.append(tabString(tab));
        builder.append("[Working copy] "); //$NON-NLS-1$
        toStringName(builder);
        if (info == null) {
          builder.append(" (not open)"); //$NON-NLS-1$
        }
      } else {
        super.toStringInfo(tab, builder, info, showResolvedInfo);
      }
    }
  }

  protected IStatus validateCompilationUnit(IResource resource) {
    MadlCore.notYetImplemented();
    // IPackageFragmentRoot root = getPackageFragmentRoot();
    // // root never null as validation is not done for working copies
    // try {
    // if (root.getKind() != IPackageFragmentRoot.K_SOURCE)
    // return new
    // MadlModelStatusImpl(MadlModelStatusConstants.INVALID_ELEMENT_TYPES,
    // root);
    // } catch (MadlModelException e) {
    // return e.getMadlModelStatus();
    // }
    if (resource != null) {
      // char[][] inclusionPatterns =
      // ((PackageFragmentRoot)root).fullInclusionPatternChars();
      // char[][] exclusionPatterns =
      // ((PackageFragmentRoot)root).fullExclusionPatternChars();
      // if (Util.isExcluded(resource, inclusionPatterns, exclusionPatterns))
      // return new
      // MadlModelStatusImpl(MadlModelStatusConstants.ELEMENT_NOT_ON_CLASSPATH,
      // this);
      if (!resource.isAccessible()) {
        return new MadlModelStatusImpl(MadlModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this);
      }
    }
    return MadlConventions.validateCompilationUnitName(getElementName());
  }

  @Override
  protected IStatus validateExistence(IResource underlyingResource) {
    // check if this compilation unit can be opened
    if (!isWorkingCopy()) { // no check is done on root kind or exclusion
                            // pattern for working copies
      IStatus status = validateCompilationUnit(underlyingResource);
      if (!status.isOK()) {
        return status;
      }
    }
    // prevents reopening of non-primary working copies (they are closed when
    // they are discarded and should not be reopened)
    if (!isPrimary()) {
      return newDoesNotExistStatus();
    }
    return MadlModelStatusImpl.VERIFIED_OK;
  }

  private MadlElement getOriginalElement() {

    return getPrimaryElement();
  }
}
