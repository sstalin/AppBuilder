package edu.depaul.cdm.madl.groovy.model;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.eclipse.codeassist.processors.IProposalProvider;
import org.codehaus.groovy.eclipse.codeassist.proposals.IGroovyProposal;
import org.codehaus.groovy.eclipse.codeassist.requestor.ContentAssistContext;

import java.util.List;
import java.util.Set;

public class MadlProposalProvider implements IProposalProvider {

  @Override
  public List<IGroovyProposal> getStatementAndExpressionProposals(ContentAssistContext context,
      ClassNode completionType, boolean isStatic, Set<ClassNode> categories) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<MethodNode> getNewMethodProposals(ContentAssistContext context) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<String> getNewFieldProposals(ContentAssistContext context) {
    // TODO Auto-generated method stub
    return null;
  }

}
