package edu.depaul.cdm.cpdt.editor.codeassist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformationValidator;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Point;

public class MadlCompletionProcessor implements IContentAssistProcessor {

	private static final char[] autoActivationChars = new char[] { '.', ' ' };
	private static final String[] commands = new String[] { "app", "view", "vfill", "Row", "Button" };

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,
			int offset) {
		ArrayList<ICompletionProposal> proposalList = new ArrayList<ICompletionProposal>();
		for (int i = 0; i < commands.length; i++) 
				proposalList.add(new CompletionProposal(commands[i], offset, 0,	commands[i].length()));
		
		
		return proposalList.toArray(new ICompletionProposal[proposalList.size()]);
	}

	/*
	 * List<ICompletionProposal> result = new ArrayList<ICompletionProposal>();
	 * for (int i = 0; i < methods.length; i++) { result.add(new
	 * CompletionProposal(methods[i], offset, 0, methods[i].length())); } return
	 * result.toArray(new ICompletionProposal[result.size()]);
	 */

	/*
	 * private static final char[] autoActivationChars = new char[] { '.', ' '
	 * }; private static final String[] reserveWords = { "abstract", "as",
	 * "assert", "boolean", "break", "byte", "case", "catch", "char", "class",
	 * "const", "continue", "def", "default", "do", "double", "else", "enum",
	 * "extends", "false", "final", "finally", "float", "for", "goto", "if",
	 * "implements", "import", "in", "instanceof", "int", "interface", "long",
	 * "native", "new", "null", "package", "private", "protected", "public",
	 * "return", "short", "static", "strictfp", "super", "switch",
	 * "synchronized", "this", "threadsafe", "throw", "throws", "transient",
	 * "true", "try", "void", "volatile", "while" }; private static final
	 * String[] methods = { "app", "view", "vfill", "Row", "Button" }; private
	 * static final String[] parameterType = { "id", "title", "action" };
	 */
	/*	*//**
	 * Return completion hints for the given offset. Here we always return
	 * all supported markup symbols.
	 */
	/*
	 * @Override public ICompletionProposal[]
	 * computeCompletionProposals(ITextViewer viewer, int documentOffset) {
	 * 
	 * 
	 * List<ICompletionProposal> result = new ArrayList<ICompletionProposal>();
	 * for (int i = 0; i < methods.length; i++) { result.add(new
	 * CompletionProposal(methods[i], documentOffset, 0, methods[i].length()));
	 * } return result.toArray(new ICompletionProposal[result.size()]);
	 * 
	 * 
	 * IDocument doc = viewer.getDocument();// Retrieve current document Point
	 * selectedRange = viewer.getSelectedRange();// Retrieve current selection
	 * range List propList = new ArrayList();
	 * 
	 * if (selectedRange.y > 0) { try {
	 * 
	 * // Retrieve selected text String text = doc.get(selectedRange.x,
	 * selectedRange.y);
	 * 
	 * // Compute completion proposals // /computeStyleProposals(text,
	 * selectedRange, propList);
	 * 
	 * } catch (BadLocationException e) {
	 * 
	 * } } else { // Retrieve qualifier String qualifier = getQualifier(doc,
	 * documentOffset);
	 * 
	 * // Compute completion proposals computeStructureProposals(qualifier,
	 * documentOffset, propList); } // Create completion proposal array
	 * ICompletionProposal[] proposals = new ICompletionProposal[propList
	 * .size()];
	 * 
	 * // and fill with list elements propList.toArray(proposals);
	 * 
	 * // Return the proposals return proposals;
	 * 
	 * }
	 */
	private String getQualifier(IDocument doc, int documentOffset) {

		String returnVal = "";

		// Use string buffer to collect characters
		StringBuffer buf = new StringBuffer();
		String[] words = new String(buf).split(" ");
		String lastWord = words[words.length - 1];

		// This was not the start of a tag

		if (lastWord.isEmpty())
			returnVal = "";
		else
			returnVal = lastWord;

		return returnVal;

	}

	/*
	 * List<ICompletionProposal> result = new ArrayList<ICompletionProposal>();
	 * for (int i = 0; i < methods.length; i++) { result.add(new
	 * CompletionProposal(methods[i], offset, 0, methods[i].length())); } return
	 * result.toArray(new ICompletionProposal[result.size()]);
	 */

	/**
	 * @param qualifier
	 *            - partially entered HTML tag
	 * @param documentOffset
	 *            - current cursor position
	 * @param propList
	 *            - result list
	 */
	/*
	 * private void computeStructureProposals(String qualifier,int
	 * documentOffset,List propList) { int qlen = qualifier.length(); // Loop
	 * through all proposals for (int i = 0; i < reserveWords.length; i++) {
	 * String startTag = reserveWords[i]; // Check if proposal matches qualifier
	 * if (startTag.startsWith(qualifier)) { // Yes – compute whole proposal
	 * text String text = startTag ; // Derive cursor position int cursor =
	 * startTag.length(); // Construct proposal CompletionProposal proposal =
	 * new CompletionProposal( text, documentOffset - qlen, qlen, cursor); //
	 * and add to result list propList.add(proposal); } } }
	 */
	public ArrayList computeCompletion(Point selectedRange) {
		return null;
	}

	// completion hints triggered when the user types '.' or ' '
	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return autoActivationChars;// TODO Auto-generated method stub
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer,
			int offset) {
		// TODO Auto-generated method stub
		return null;
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
		// return null;
		return new ContextInformationValidator(this);
	}

	/*
	 * private ArrayList<ICompletionProposal> getProposalforCommand (String
	 * prefix) { if (prefix == null) prefix = ""; prefix = prefix.toLowerCase();
	 * ArrayList<String> proposalStrs = new ArrayList<String>(); for (int i = 0;
	 * i < commands.length; i++) { if
	 * (commands[i].toLowerCase().startsWith(prefix))
	 * proposalStrs.add(commands[i]); } return createProposals(proposalStrs,
	 * MadlCompletionProposal.RESERVE_TYPE);
	 * 
	 * 
	 * }
	 */

	/*
	 * private ArrayList<ICompletionProposal> createProposals( ArrayList<String>
	 * proposalStrs, int reserveType) { // TODO Auto-generated method stub if
	 * (proposalStrs == null) return null;
	 * 
	 * ArrayList<ICompletionProposal> result = new
	 * ArrayList<ICompletionProposal>();
	 * 
	 * for (int i = 0; i < proposalStrs.size(); i++) { result.add( new
	 * MadlCompletionProposal(proposalStrs.get(i), reserveType)); }
	 * 
	 * return result; }
	 * 
	 * private static String getFieldNameFromFunctionCall(String fieldName) { //
	 * check if the prefix has ( int index = fieldName.lastIndexOf('('); int
	 * index1 = -1; String newFieldName = fieldName; if (index >= 0) { // check
	 * if there is corresponding ) index1 = fieldName.indexOf(')', index); int
	 * fromIndex = index + 1; if (index + 1 >= fieldName.length()) return "";
	 * //$NON-NLS-1$ if (index1 > 0) { newFieldName =
	 * fieldName.substring(fromIndex, index1); } else newFieldName =
	 * fieldName.substring(fromIndex); }
	 * 
	 * return newFieldName; }
	 */
}
