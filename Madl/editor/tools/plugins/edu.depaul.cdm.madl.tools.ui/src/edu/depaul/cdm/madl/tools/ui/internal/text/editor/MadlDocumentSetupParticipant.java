package edu.depaul.cdm.madl.tools.ui.internal.text.editor;

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.jface.text.IDocument;
import edu.depaul.cdm.madl.tools.ui.MadlToolsPlugin;
import edu.depaul.cdm.madl.tools.ui.text.MadlPartitions;
import edu.depaul.cdm.madl.tools.ui.text.MadlTextTools;

/**
 * @author sstalin
 *
 */
public class MadlDocumentSetupParticipant implements IDocumentSetupParticipant {

public MadlDocumentSetupParticipant(){}

	/* (non-Javadoc)
	 * @see org.eclipse.core.filebuffers.IDocumentSetupParticipant#setup(org.eclipse.jface.text.IDocument)
	 */
	@Override
	public void setup(IDocument document) {
		// TODO Auto-generated method stub
		//MadlToolsPlugin this_plugin= new MadlToolsPlugin();
		MadlTextTools tools= MadlToolsPlugin.getDefault().getMadlTextTools();
		tools.setupMadlDocumentPartitioner(document, MadlPartitions.MADL_PARTITIONING);
	}

}
