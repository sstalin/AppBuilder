package edu.depaul.cdm.cpdt.editor.codeassist;



import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;



public class MadlSourceViewConfiguration extends SourceViewerConfiguration{

	//private ColorManager colorManager;
	MadlContentAssistant contentAssistant = new MadlContentAssistant();
	
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		
		contentAssistant.setInformationControlCreator(getInformationControlCreator(sourceViewer));

		return contentAssistant;
	}
	
	/*public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {

		   // Create content assistant
		   ContentAssistant assistant = new ContentAssistant();
		   
		   // Create content assistant processor
		   IContentAssistProcessor processor = new MadlCompletionProcessor();
		   
		   // Set this processor for each supported content type
		   assistant.setContentAssistProcessor(processor, MADLPartitionScanner.MADL_COMMENT);
		   assistant.setContentAssistProcessor(processor, MADLPartitionScanner.MADL_DEFAULT);
		   assistant.setContentAssistProcessor(processor, IDocument.DEFAULT_CONTENT_TYPE);

		// Allow automatic activation after 500 msec
			assistant.enableAutoActivation(true);
			assistant.setAutoActivationDelay(500);
			// Set background color
			Color bgColor = colorManager.getColor(new RGB(230,255,230));
			assistant.setProposalSelectorBackground(bgColor);
	        // Return the content assistant
			return assistant;
			
		}
	*/
}
