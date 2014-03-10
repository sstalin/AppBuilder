package edu.depaul.cdm.madl.groovy.model;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.jdt.groovy.model.GroovyCompilationUnit;
import org.eclipse.jdt.groovy.search.AbstractSimplifiedTypeLookup;
import org.eclipse.jdt.groovy.search.ITypeLookup;
import org.eclipse.jdt.groovy.search.VariableScope;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MadlTypeLookup extends AbstractSimplifiedTypeLookup implements ITypeLookup {

  private ClassNode MADL_APP_NODE = null;
  private ClassNode MADL_VIEW_NODE = null;
  private ClassNode MADL_WIDGET_NODE = null;
  private List<String> topViews = new ArrayList<String>(Arrays.asList("View"
      ,"ListView"
      ,"TabbedView"
      ,"NavigationView"
      ,"Row"));
  private List<String> widgets = new ArrayList<String>(Arrays.asList("Button"
      ,"Label"
      ,"Text"
      ,"Image"
      ,"Box"
      ,"CheckBox"
      ,"Switch"
      ,"Slider"
      ,"Selection"
      ));

  @Override
  public void initialize(GroovyCompilationUnit unit, VariableScope scope) {
    // TODO Auto-generated method stub
    try {
      MADL_APP_NODE = new ClassNode(Class.forName("xj.mobile.model.Application"));
      MADL_VIEW_NODE = new ClassNode(Class.forName("xj.mobile.model.ui.View"));
      MADL_WIDGET_NODE = new ClassNode(Class.forName("xj.mobile.model.ui.Widget"));
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    scope.addVariable("MADL_APP_NODE", MADL_APP_NODE, MADL_APP_NODE);
    scope.addVariable("MADL_VIEW_NODE", MADL_VIEW_NODE, MADL_VIEW_NODE);
    scope.addVariable("MADL_WIDGET", MADL_WIDGET_NODE, MADL_WIDGET_NODE);
  }

  @Override
  protected TypeAndDeclaration lookupTypeAndDeclaration(ClassNode declaringType, String name,
      VariableScope scope) {
    // TODO Auto-generated method stub

    if (name.equals("app")) {
     return new TypeAndDeclaration(MADL_APP_NODE, MADL_APP_NODE);
    }
    if (topViews.contains(name)) {
      return  new TypeAndDeclaration(MADL_VIEW_NODE, MADL_VIEW_NODE);
    }
    if(widgets.contains(name)){
      return  new TypeAndDeclaration(MADL_WIDGET_NODE, MADL_WIDGET_NODE);
    }

    return null;
  }

}
