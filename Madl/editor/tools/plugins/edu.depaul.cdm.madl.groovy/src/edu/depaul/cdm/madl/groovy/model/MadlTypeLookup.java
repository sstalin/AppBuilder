package edu.depaul.cdm.madl.groovy.model;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.jdt.groovy.model.GroovyCompilationUnit;
import org.eclipse.jdt.groovy.search.AbstractSimplifiedTypeLookup;
import org.eclipse.jdt.groovy.search.ITypeLookup;
import org.eclipse.jdt.groovy.search.VariableScope;

public class MadlTypeLookup extends AbstractSimplifiedTypeLookup implements ITypeLookup {

  @Override
  public void initialize(GroovyCompilationUnit arg0, VariableScope arg1) {
    // TODO Auto-generated method stub

  }

  @Override
  protected TypeAndDeclaration lookupTypeAndDeclaration(ClassNode arg0, String arg1,
      VariableScope arg2) {
    // TODO Auto-generated method stub
    return null;
  }

}
