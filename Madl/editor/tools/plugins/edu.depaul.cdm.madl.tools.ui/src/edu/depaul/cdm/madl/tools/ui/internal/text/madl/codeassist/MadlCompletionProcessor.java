package edu.depaul.cdm.madl.tools.ui.internal.text.madl.codeassist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

import edu.depaul.cdm.madl.tools.core.Dummy_Parser;



public class MadlCompletionProcessor implements IContentAssistProcessor {

	private static final char[] autoActivationChars = new char[] {'.',' '}; 

	Dummy_Parser dp = new Dummy_Parser();
	
	private static String[] commands = new String[] {} ;


	
	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,
			int offset) {
		
		commands = dp.getKeyWords();
		
		CodeAssistContext ctx = new CodeAssistContext();
		ctx.docOffset = offset;
		ctx.docText = viewer.getDocument().get();

		ArrayList<ICompletionProposal> proposalList = null;

		proposalList = getProposalsForCommandAndFunctions("", ctx);

		if (proposalList != null)
		{
			Collections.sort((List)proposalList);
			ICompletionProposal[] result = new ICompletionProposal[proposalList.size()];
			for (int i = 0; i < proposalList.size(); i++)
				result[i] = proposalList.get(i);
			return result;
		}
		
		return null;
	}

	private ArrayList<ICompletionProposal> getProposalsForCommandAndFunctions (String prefix, CodeAssistContext ctx)
	{
		ArrayList<ICompletionProposal> result = new ArrayList<ICompletionProposal>();
		ArrayList<ICompletionProposal> commands = getProposalsForCommand(prefix, ctx);
		if (commands != null)
			result.addAll(commands);
		
		
		return result;
	}
	
	private ArrayList<ICompletionProposal> getProposalsForCommand (String prefix, CodeAssistContext ctx)
	{
		if (prefix == null)
			prefix = "";
		prefix = prefix.toLowerCase();
		ArrayList<String> proposalStrs = new ArrayList<String>();
		for (int i = 0; i < commands.length; i++)
		{
			if (commands[i].toLowerCase().startsWith(prefix))
				proposalStrs.add(commands[i]);
		}
		return createProposals(proposalStrs, MadlCompletionProposal.COMMAND_TYPE);
	}
	
	
	private ArrayList<ICompletionProposal> createProposals(ArrayList<String> proposals, int type)
	{
		if (proposals == null)
			return null;
		
		ArrayList<ICompletionProposal> result = new ArrayList<ICompletionProposal>();
		for (int i = 0; i < proposals.size(); i++)
		{
			result.add( new MadlCompletionProposal(proposals.get(i), type));
		}
		
		return result;
	}
	


	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer,
			int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return autoActivationChars;
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		// TODO Auto-generated method stub
		return null;
	}
	
	ArrayList<ICompletionProposal> createCompletionProposals (CodeAssistContext ctx)
	{

		ArrayList<ICompletionProposal> proposalList = null;


		return proposalList;
		
	}


}
