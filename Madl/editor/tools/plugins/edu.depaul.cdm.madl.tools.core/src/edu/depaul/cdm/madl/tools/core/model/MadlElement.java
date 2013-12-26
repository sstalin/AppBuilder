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
package edu.depaul.cdm.madl.tools.core.model;

import edu.depaul.cdm.madl.tools.core.MadlCore;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.jobs.ISchedulingRule;

import java.util.List;

/**
 * The interface <code>MadlElement</code> defines the behavior of objects representing some element
 * within the Madl model. Madl model elements are exposed to clients as handles to the actual
 * underlying element. The Madl model may hand out any number of handles for each element. Handles
 * that refer to the same element are guaranteed to be equal, but not necessarily identical.
 * <p>
 * Methods annotated as "handle-only" do not require underlying elements to exist. Methods that
 * require underlying elements to exist throw a {@link MadlModelException} when an underlying
 * element is missing. The method {@link MadlModelException#isDoesNotExist()} can be used to
 * recognize this common special case.
 * <p>
 * This interface is not intended to be implemented by clients.
 * 
 * @coverage madl.tools.core.model
 */
public interface MadlElement extends IAdaptable {
  /**
   * A constant representing a Madl model (workspace level object). A Madl element with this type
   * can be safely cast to {@link MadlModel}.
   */
  public static int MADL_MODEL = 1;

  /**
   * A constant representing a Madl project. A Madl element with this type can be safely cast to
   * {@link MadlProject}.
   */
  public static int MADL_PROJECT = 2;

  /**
   * A constant representing an HTML file. A Madl element with this type can be safely cast to
   * {@link HTMLFile}.
   */
  public static int HTML_FILE = 3;

  /**
   * A constant representing a Madl library. A Madl element with this type can be safely cast to
   * {@link MadlLibrary}.
   */
  public static int LIBRARY = 4;

  /**
   * A constant representing a library folder. A Madl element with this type can be safely cast to
   * {@link MadlLibraryFolder}.
   */
  public static int MADL_LIBRARY_FOLDER = 16;

  /**
   * A constant representing a Madl compilation unit. A Madl element with this type can be safely
   * cast to {@link CompilationUnit}.
   */
  public static int COMPILATION_UNIT = 7;

  /**
   * A constant representing a type (a class or interface). A Madl element with this type can be
   * safely cast to {@link Type}.
   */
  public static int TYPE = 8;

  /**
   * A constant representing a field. A Madl element with this type can be safely cast to
   * {@link Field}.
   */
  public static int FIELD = 10;

  /**
   * A constant representing a method or constructor. A Madl element with this type can be safely
   * cast to {@link Method}.
   */
  public static int METHOD = 11;

  /**
   * A constant representing an import container. A Madl element with this type can be safely cast
   * to {@link MadlImportContainer}.
   */
  public static int IMPORT_CONTAINER = 12;

  /**
   * A constant representing an import. A Madl element with this type can be safely cast to
   * {@link MadlImport}.
   */
  public static int IMPORT = 13;

  /**
   * A constant representing a function. A Madl element with this type can be safely cast to
   * {@link MadlFunction}.
   */
  public static int FUNCTION = 14;

  /**
   * A constant representing a function type alias. A Madl element with this type can be safely cast
   * to {@link MadlFunctionTypeAlias}.
   */
  public static int FUNCTION_TYPE_ALIAS = 15;

  /**
   * A constant representing a class type alias. A Madl element with this type can be safely cast to
   * {@link MadlClassTypeAlias}.
   */
  public static int CLASS_TYPE_ALIAS = 18;

  /**
   * A constant representing a local variable or parameter. A Madl element with this type can be
   * safely cast to {@link MadlVariableDeclaration}.
   */
  public static int VARIABLE = 17;

  /**
   * A constant representing a type parameter. A Madl element with this type can be safely cast to
   * {@link MadlTypeParameter}.
   */
  public static int TYPE_PARAMETER = 19;

  /**
   * An empty array of elements.
   */
  public static final MadlElement[] EMPTY_ARRAY = new MadlElement[0];

  /**
   * Return <code>true</code> if this Madl element exists in the model.
   * <p>
   * Madl elements are handle objects that may or may not be backed by an actual element. Madl
   * elements that are backed by an actual element are said to "exist", and this method returns
   * <code>true</code>. For Madl elements that are not working copies, it is always the case that if
   * the element exists, then its parent also exists (provided it has one) and includes the element
   * as one of its children. It is therefore possible to navigated to any existing Madl element from
   * the root of the Madl model along a chain of existing Madl elements. On the other hand, working
   * copies are said to exist until they are destroyed (with <code>WorkingCopy.destroy</code>).
   * Unlike regular Madl elements, a working copy never shows up among the children of its parent
   * element (which may or may not exist).
   * 
   * @return <code>true</code> if this element exists in the Madl model
   */
  public boolean exists();

  /**
   * Return the first ancestor of this element that has the given type, or <code>null</code> if no
   * such ancestor can be found.
   * 
   * @param ancestorClass the class of element to be returned
   * @return the first ancestor of this element that has the given type
   */
  public <E extends MadlElement> E getAncestor(Class<E> ancestorClass);

  /**
   * Return the children of this element, or an empty array if there are no children.
   * 
   * @return the children of this element
   * @throws MadlModelException if the children of the element cannot be determined
   */
  public MadlElement[] getChildren() throws MadlModelException;

  /**
   * Return the children of this element which are of type <code>elementClass</code>.
   * 
   * @param elementClass the class of element to be returned
   * @return this element's children of type elementClass
   * @throws MadlModelException if the children of the element cannot be determined
   */
  public <E extends MadlElement> List<E> getChildrenOfType(Class<E> elementClass)
      throws MadlModelException;

  /**
   * Return the resource that corresponds directly to this element, or <code>null</code> if there is
   * no resource that corresponds directly to this element.
   * <p>
   * For example, the corresponding resource for a <code>CompilationUnit</code> is its underlying
   * <code>IFile</code>. There is no corresponding resource for <code>Type</code>s,
   * <code>Method</code>s, <code>Field</code>s, etc.
   * 
   * @return the resource that corresponds directly to this element
   * @throws MadlModelException if this element does not exist or if an exception occurs while
   *           accessing its corresponding resource
   */
  public IResource getCorrespondingResource() throws MadlModelException;

  /**
   * Return the Madl model that contains this element, or <code>null</code> if this element is not
   * contained in the model. This is a handle-only method.
   * 
   * @return the Madl model that contains this element
   */
  public MadlModel getMadlModel();

  /**
   * Return the Madl project that contains this element, or <code>null</code> if this element is not
   * contained in any project. This is a handle-only method.
   * 
   * @return the Madl project that contains this element
   */
 // public MadlProject getMadlProject();

  /**
   * Return the name of this element as it should appear in the user interface. This is a
   * handle-only method.
   * 
   * @return the name of this element
   */
  public String getElementName();

  /**
   * Return the type of this element, encoded as an integer. The returned value will be one of the
   * constants declared in {@link MadlElement}. This is a handle-only method.
   * 
   * @return the type of this element
   */
  public int getElementType();

  /**
   * Return an identifier that can be used to identify this element. The format of the identifier is
   * not specified, but the identifier is stable across workspace sessions and can be used to
   * recreate this handle via the {@link MadlCore#create(String)} method.
   * 
   * @return an identifier that can be used to identify this element
   */
  public String getHandleIdentifier();

  /**
   * Return the first openable parent. If this element is openable, the element itself is returned.
   * Return <code>null</code> if this element doesn't have an openable parent. This is a handle-only
   * method.
   * 
   * @return the first openable parent
   */
  //public OpenableElement getOpenable();

  /**
   * Return the parent of this element, or <code>null</code> if this element does not have a parent.
   * This is a handle-only method.
   * 
   * @return the parent of this element
   */
  public MadlElement getParent();

  /**
   * Return the path to the innermost resource enclosing this element. If this element is not
   * included in an external library, the path returned is the full, absolute path to the underlying
   * resource, relative to the workbench. If this element is included in an external library, the
   * path returned is the absolute path to the archive or to the folder in the file system. This is
   * a handle-only method.
   * 
   * @return the path to the innermost resource enclosing this element
   */
  public IPath getPath();

  /**
   * Return the primary element (whose compilation unit is the primary compilation unit) this
   * working copy element was created from, or this element if it is a descendant of a primary
   * compilation unit or if it is not a descendant of a working copy (e.g. it is a binary member).
   * The returned element may or may not exist.
   * 
   * @return the primary element this working copy element was created from, or this element
   */
  public MadlElement getPrimaryElement();

  /**
   * Return the innermost resource enclosing this element, or <code>null</code> if this element is
   * not enclosed in a resource. This is a handle-only method.
   * 
   * @return the innermost resource enclosing this element
   */
  public IResource getResource();

  /**
   * Return the scheduling rule associated with this Madl element. This is a handle-only method.
   * 
   * @return the scheduling rule associated with this Madl element
   */
  public ISchedulingRule getSchedulingRule();

  /**
   * Return the smallest underlying resource that contains this element, or <code>null</code> if
   * this element is not contained in a resource.
   * 
   * @return the underlying resource, or <code>null</code> if none
   * @throws MadlModelException if this element does not exist or if an exception occurs while
   *           accessing its underlying resource
   */
  public IResource getUnderlyingResource() throws MadlModelException;

  /**
   * Return <code>true</code> if this element is a part of the SDK.
   * 
   * @return <code>true</code> if this element is a part of the SDK
   */
  public boolean isInSdk();

  /**
   * Return <code>true</code> if this Madl element is read-only. An element is read-only if its
   * structure cannot be modified by the Madl model.
   * <p>
   * Note this is different from IResource.isReadOnly(). For example, .jar files are read-only as
   * the Madl model doesn't know how to add/remove elements in this file, but the underlying IFile
   * can be writable.
   * <p>
   * This is a handle-only method.
   * 
   * @return <code>true</code> if this element is read-only
   */
  public boolean isReadOnly();

  /**
   * Return <code>true</code> if the structure of this element is known. For example, for a
   * compilation unit that has syntax errors, <code>false</code> is returned. If the structure of an
   * element is unknown, navigations will return reasonable defaults. For example,
   * <code>getChildren</code> for a compilation unit with syntax errors will return a collection of
   * the children that could be parsed.
   * <p>
   * Note: This does not imply anything about consistency with the underlying resource/buffer
   * contents.
   * </p>
   * 
   * @return <code>true</code> if the structure of this element is known
   * @throws MadlModelException if this element does not exist or if an exception occurs while
   *           accessing its corresponding resource
   */
  public boolean isStructureKnown() throws MadlModelException;

  /**
   * Visit hierarchy of {@link MadlElement}s starting form this one.
   */
 // void accept(MadlElementVisitor visitor) throws MadlModelException;
}
