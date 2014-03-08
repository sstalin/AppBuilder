package edu.depaul.cdm.madl.groovy.model;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.jdt.groovy.model.GroovyCompilationUnit;
import org.eclipse.jdt.groovy.search.AbstractSimplifiedTypeLookup;
import org.eclipse.jdt.groovy.search.ITypeLookup;
import org.eclipse.jdt.groovy.search.VariableScope;


public class MadlTypeLookup extends AbstractSimplifiedTypeLookup implements ITypeLookup {

  @Override
  public void initialize(GroovyCompilationUnit unit, VariableScope scope) {
    // TODO Auto-generated method stub

  }

  @Override
  protected TypeAndDeclaration lookupTypeAndDeclaration(ClassNode declaringType, String name,
      VariableScope scope) {
    // TODO Auto-generated method stub
    if (name.equals("app")) {
      TypeAndDeclaration td = new TypeAndDeclaration(VariableScope.CLOSURE_CLASS, declaringType);
      return td;
    }
    if (name.equals("ListView")) {
      TypeAndDeclaration td = new TypeAndDeclaration(VariableScope.CLOSURE_CLASS, declaringType);
      return td;
    }

    return null;
  }

}
