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

//import edu.depaul.cdm.madl.compiler.util.StringInterner;
import edu.depaul.cdm.madl.engine.utilities.source.SourceRange;
import edu.depaul.cdm.madl.tools.core.MadlCore;
import edu.depaul.cdm.madl.tools.core.completion.CompletionRequestor;
import edu.depaul.cdm.madl.tools.core.internal.model.info.MadlElementInfo;

//import edu.depaul.cdm.madl.tools.core.internal.util.MementoTokenizer;
import edu.depaul.cdm.madl.tools.core.internal.workingcopy.DefaultWorkingCopyOwner;

import edu.depaul.cdm.madl.tools.core.model.MadlElement;

//import edu.depaul.cdm.madl.tools.core.model.MadlLibrary;

import edu.depaul.cdm.madl.tools.core.model.MadlModelException;

//import edu.depaul.cdm.madl.tools.core.model.MadlTypeParameter;
//import edu.depaul.cdm.madl.tools.core.model.Field;
//import edu.depaul.cdm.madl.tools.core.model.Method;

import edu.depaul.cdm.madl.tools.core.model.Type;
//import edu.depaul.cdm.madl.tools.core.model.TypeHierarchy;
import edu.depaul.cdm.madl.tools.core.model.TypeMember;
import edu.depaul.cdm.madl.tools.core.workingcopy.WorkingCopyOwner;

import org.eclipse.core.runtime.IProgressMonitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Instances of the class <code>MadlTypeImpl</code> implement the representation of types defined in
 * compilation units.
 * 
 * @coverage madl.tools.core.model
 */
public class MadlTypeImpl extends SourceReferenceImpl implements Type {
  /**
   * The name of the type.
   */
  private String name;

  /**
   * Initialize a newly created type to be defined within the given compilation unit.
   * 
   * @param parent the compilation unit containing the type
   * @param name the name of the type
   */
  public MadlTypeImpl(CompilationUnitImpl parent, String name) {
    super(parent);
    this.name = StringInterner.intern(name);
  }

  @Override
  public void codeComplete(char[] snippet, int insertion, int position,
      char[][] localVariableTypeNames, char[][] localVariableNames, int[] localVariableModifiers,
      boolean isStatic, CompletionRequestor requestor) throws MadlModelException {
    codeComplete(
        snippet,
        insertion,
        position,
        localVariableTypeNames,
        localVariableNames,
        localVariableModifiers,
        isStatic,
        requestor,
        DefaultWorkingCopyOwner.getInstance());
  }

  @Override
  public void codeComplete(char[] snippet, int insertion, int position,
      char[][] localVariableTypeNames, char[][] localVariableNames, int[] localVariableModifiers,
      boolean isStatic, CompletionRequestor requestor, IProgressMonitor monitor)
      throws MadlModelException {
    codeComplete(
        snippet,
        insertion,
        position,
        localVariableTypeNames,
        localVariableNames,
        localVariableModifiers,
        isStatic,
        requestor,
        DefaultWorkingCopyOwner.getInstance(),
        monitor);
  }

  @Override
  public void codeComplete(char[] snippet, int insertion, int position,
      char[][] localVariableTypeNames, char[][] localVariableNames, int[] localVariableModifiers,
      boolean isStatic, CompletionRequestor requestor, WorkingCopyOwner owner)
      throws MadlModelException {
    codeComplete(
        snippet,
        insertion,
        position,
        localVariableTypeNames,
        localVariableNames,
        localVariableModifiers,
        isStatic,
        requestor,
        owner,
        null);
  }

  @Override
  public void codeComplete(char[] snippet, int insertion, int position,
      char[][] localVariableTypeNames, char[][] localVariableNames, int[] localVariableModifiers,
      boolean isStatic, CompletionRequestor requestor, WorkingCopyOwner owner,
      IProgressMonitor monitor) throws MadlModelException {
    if (requestor == null) {
      throw new IllegalArgumentException("Completion requestor cannot be null"); //$NON-NLS-1$
    }
    MadlCore.notYetImplemented();
    // MadlProjectImpl project = (MadlProjectImpl) getMadlProject();
    // SearchableEnvironment environment =
    // project.newSearchableNameEnvironment(owner);
    // CompletionEngine engine = new CompletionEngine(environment, requestor,
    // project.getOptions(true), project, owner, monitor);
    //
    // String source = getCompilationUnit().getSource();
    // if (source != null && insertion > -1 && insertion < source.length()) {
    //
    // char[] prefix = CharOperation.concat(source.substring(0,
    // insertion).toCharArray(), new char[]{'{'});
    // char[] suffix = CharOperation.concat(new char[]{'}'},
    // source.substring(insertion).toCharArray());
    // char[] fakeSource = CharOperation.concat(prefix, snippet, suffix);
    //
    // BasicCompilationUnit cu =
    // new BasicCompilationUnit(
    // fakeSource,
    // null,
    // getElementName(),
    // getParent());
    //
    // engine.complete(cu, prefix.length + position, prefix.length,
    // null/*extended context isn't computed*/);
    // } else {
    // engine.complete(this, snippet, position, localVariableTypeNames,
    // localVariableNames, localVariableModifiers, isStatic);
    // }
    // if (NameLookup.VERBOSE) {
    //      System.out.println(Thread.currentThread() + " TIME SPENT in NameLoopkup#seekTypesInSourcePackage: " + environment.nameLookup.timeSpentInSeekTypesInSourcePackage + "ms");  //$NON-NLS-1$ //$NON-NLS-2$
    //      System.out.println(Thread.currentThread() + " TIME SPENT in NameLoopkup#seekTypesInBinaryPackage: " + environment.nameLookup.timeSpentInSeekTypesInBinaryPackage + "ms");  //$NON-NLS-1$ //$NON-NLS-2$
    // }
  }

  //ss
/*  @Override
  public Field createField(String contents, TypeMember sibling, boolean force,
      IProgressMonitor monitor) throws MadlModelException {
    // CreateFieldOperation op = new CreateFieldOperation(this, contents,
    // force);
    // if (sibling != null) {
    // op.createBefore(sibling);
    // }
    // op.runOperation(monitor);
    // return (Field) op.getResultElements()[0];
    MadlCore.notYetImplemented();
    return null;
  }*/

  //ss
 /* @Override
  public Method createMethod(String contents, TypeMember sibling, boolean force,
      IProgressMonitor monitor) throws MadlModelException {
    // CreateMethodOperation op = new CreateMethodOperation(this, contents,
    // force);
    // if (sibling != null) {
    // op.createBefore(sibling);
    // }
    // op.runOperation(monitor);
    // return (Method) op.getResultElements()[0];
    MadlCore.notYetImplemented();
    return null;
  }*/

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof MadlTypeImpl)) {
      return false;
    }
    return super.equals(o);
  }

  //ss
/*  @Override
  public Method[] findMethods(Method method) {
    // try {
    // return findMethods(method, getMethods());
    // } catch (MadlModelException e) {
    // // if type doesn't exist, no matching method can exist
    // return null;
    // }
    MadlCore.notYetImplemented();
    return null;
  }*/

  @Override
  public String getElementName() {
    return name;
  }

  @Override
  public int getElementType() {
    return MadlElement.TYPE;
  }

  @Override
  public TypeMember[] getExistingMembers(String memberName) throws MadlModelException {
    List<TypeMember> allMembers = getChildrenOfType(TypeMember.class);
    List<TypeMember> matchingMembers = new ArrayList<TypeMember>(allMembers.size());
    for (TypeMember member : allMembers) {
      if (member.getElementName().equals(memberName)) {
        matchingMembers.add(member);
      }
    }
    return matchingMembers.toArray(new TypeMember[matchingMembers.size()]);
  }
  
  //ss
/*
  @Override
  public Field getField(String fieldName) {
    return null;
  }*/

/*  @Override
  public Field[] getFields() throws MadlModelException {
    List<Field> list = getChildrenOfType(Field.class);
    return list.toArray(new Field[list.size()]);
  }*/

/*  @Override
  public MadlLibrary getLibrary() {
    return getAncestor(MadlLibrary.class);
  }*/

/*  @Override
  public Method getMethod(String methodName, String[] parameterTypeSignatures) {
    return null;
  }*/

/*  @Override
  public Method[] getMethods() throws MadlModelException {
    List<Method> list = getChildrenOfType(Method.class);
    return list.toArray(new Method[list.size()]);
  }*/

  @Override
  public SourceRange getNameRange() throws MadlModelException {
    return null;
  }

  @Override
  public String getSuperclassName() throws MadlModelException {

    return null;
  }

  @Override
  public String[] getSuperInterfaceNames() throws MadlModelException {
    return null;
  }

  @Override
  public String[] getSupertypeNames() throws MadlModelException {
    return null;
  }

  @Override
  public MadlTypeParameter[] getTypeParameters() throws MadlModelException {
    List<MadlTypeParameter> list = getChildrenOfType(MadlTypeParameter.class);
    return list.toArray(new MadlTypeParameter[list.size()]);
  }

  @Override
  @Deprecated
  public String getTypeQualifiedName(char separatorChar) {
    // TODO(devoncarew): remove this method
    return getElementName();
  }

  @Override
  public boolean isAbstract() throws MadlModelException {
    if (isInterface()) {
      return true;
    }
    for (Method m : getMethods()) {
      if (m.isAbstract()) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean isClass() throws MadlModelException {
    return false;
  }

  @Override
  public boolean isInterface() throws MadlModelException {
    return false;
  }

  @Override
  public TypeHierarchy newSupertypeHierarchy(IProgressMonitor progressMonitor)
      throws MadlModelException {
    // TODO(devoncarew): this method needs to be implemented
    MadlCore.notYetImplemented();
    return null;
  }

  @Override
  protected MadlElementInfo createElementInfo() {
    return null;
  }

  @Override
  protected MadlElement getHandleFromMemento(String token, MementoTokenizer tokenizer,
      WorkingCopyOwner owner) {

    return null;
  }

  @Override
  protected char getHandleMementoDelimiter() {
    return MEMENTO_DELIMITER_TYPE;
  }
}
