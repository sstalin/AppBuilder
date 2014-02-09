package edu.depaul.cdm.madl.tools.ui.internal.text.madl.codeassist;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;

public class MadlContentAssistant extends ContentAssistant {

	public MadlContentAssistant()
	{
		setContentAssistProcessor (new MadlCompletionProcessor(), 
				IDocument.DEFAULT_CONTENT_TYPE);
		
		enableAutoActivation(true);
		
		setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
		
	}
}
