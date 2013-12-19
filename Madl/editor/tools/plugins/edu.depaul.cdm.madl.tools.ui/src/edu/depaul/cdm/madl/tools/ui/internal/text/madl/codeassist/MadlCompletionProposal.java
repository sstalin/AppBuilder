package edu.depaul.cdm.madl.tools.ui.internal.text.madl.codeassist;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension2;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension4;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;



public class MadlCompletionProposal implements ICompletionProposal,
ICompletionProposalExtension2, ICompletionProposalExtension4,
Comparable<MadlCompletionProposal> {

public static final int UNKNOWN_TYPE = 0;
public static final int COMMAND_TYPE = 1;
public static final int FUNCTION_TYPE = 2;
public static final int VARIABLE_TYPE = 3;

String proposal = null;
String insertionText = null;
int type = UNKNOWN_TYPE;


int newCaretPosition = -1;

public MadlCompletionProposal (String proposal, int type)
{
this.proposal = proposal;
this.type = type;
}

@Override
public void apply(IDocument document) {
}

@Override
public String getAdditionalProposalInfo() {
return null;
}

@Override
public IContextInformation getContextInformation() {
return null;
}

@Override
public String getDisplayString() {
return proposal;
}

@Override
public Image getImage() {
return null;
}

@Override
public Point getSelection(IDocument document) {
if (newCaretPosition < 0)
	return null;

return new  Point(newCaretPosition,0);
}

@Override
public void apply(ITextViewer viewer, char trigger, int stateMask,
	int offset) {
if (insertionText != null)
	proposal = insertionText;
else if (proposal == null)
	return;

IDocument doc = viewer.getDocument();

String lineText = null;
int offsetInLine = 0;
int lineOfOffset, lineLength, lineStartOffset;
try
{
	lineOfOffset = doc.getLineOfOffset(offset);
	lineLength = doc.getLineLength(lineOfOffset);
	lineStartOffset = doc.getLineOffset(lineOfOffset);
	lineText = doc.get(lineStartOffset, lineLength);
	offsetInLine = offset - lineStartOffset;
}
catch (Exception e)
{
	return;
}

int offsetDiff = calcualteOffsetDifference(offsetInLine, doc, lineText);

if (offsetDiff < 0)
	offsetDiff = 0;

newCaretPosition = offset - offsetDiff + proposal.length();

//String insertionText = proposal.substring(offsetDiff);

String modifiedDocText = lineText.substring(0, offsetInLine-offsetDiff) + proposal + lineText.substring(offsetInLine);

try
{
	doc.replace(lineStartOffset, lineLength, modifiedDocText);
} catch (BadLocationException be)
{
	//TODO: log error
}
}

@Override
public void selected(ITextViewer viewer, boolean smartToggle) {
}

@Override
public void unselected(ITextViewer viewer) {
}

@Override
public boolean validate(IDocument document, int offset, DocumentEvent event) {
System.out.println("Validate method triggered");
String docText = document.get();

int offsetDiff = calcualteOffsetDifference(offset, document, docText);

if (offsetDiff > 0)
{
	int modifiedOffset = offset - offsetDiff;
	if (modifiedOffset >= 0)
		return true;
}

return false;
}

@Override
public boolean isAutoInsertable() {
return false;
}

@Override
public int compareTo(MadlCompletionProposal o) {
return proposal.compareTo(o.proposal);
}

private int calcualteOffsetDifference(int offsetInLine, IDocument doc, String lineText)
{
if (lineText == null)
	lineText = doc.get();

String lDocText = lineText.toLowerCase();
String lProposal = null;

if (insertionText != null)
	lProposal = insertionText.toLowerCase();
else
	lProposal = proposal.toLowerCase(); 

//find text to insert

int index = offsetInLine - lProposal.length();

if (index < 0)
	index = 0;

int offsetDiff = -1;

for  (int i = 0; i <= offsetInLine; i++ )
{
	if (lProposal.startsWith(lDocText.substring(index, offsetInLine)))
	{
		offsetDiff = offsetInLine - index;
		break;
	}
	index++;
}

return  offsetDiff;
}

public void setDisplayString(String displayStr)
{
this.proposal = displayStr;
}

public String getInsertionText() {
return insertionText;
}

public void setInsertionText(String insertionText) {
this.insertionText = insertionText;
}

}
