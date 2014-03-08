package edu.depaul.cdm.madl.groovy.editor;

import org.codehaus.groovy.eclipse.editor.highlighting.IHighlightingExtender;
import org.eclipse.jface.text.rules.IRule;

import java.util.Arrays;
import java.util.List;

public class MadlSyntaxHighlighting implements IHighlightingExtender{

  @Override
  public List<String> getAdditionalGJDKKeywords() {
    return Arrays.asList("app", "View", "Label", "Button");
  }

  @Override
  public List<String> getAdditionalGroovyKeywords() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<IRule> getAdditionalRules() {
    // TODO Auto-generated method stub
    return null;
  }

}
