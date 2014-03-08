package edu.depaul.madl.wizards.pages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.custom.CLabel;

import edu.depaul.madl.wizards.template.TemplateConfig;

import org.eclipse.wb.swt.SWTResourceManager;

public class TemplateSelectionPage extends WizardPage {
	
	private CLabel templateDescriptionLabel;
	private List list;
	private boolean isTemplateSelected;

	/**
	 * Create the wizard.
	 */
	public TemplateSelectionPage() {
		super("wizardPage");
		setTitle("Templates");
		setDescription("Optionally select a starter template for your project");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		setControl(container);
		GridLayout gl_container = new GridLayout(2, false);
		container.setLayout(gl_container);
		
		Label lblTemplate = new Label(container, SWT.NONE);
		lblTemplate.setFont(SWTResourceManager.getFont("Lucida Grande", 13, SWT.BOLD));
		lblTemplate.setText("Template");
		
		Label lblDescription = new Label(container, SWT.NONE);
		lblDescription.setFont(SWTResourceManager.getFont("Lucida Grande", 13, SWT.BOLD));
		lblDescription.setText("Description");
		
		list = new List(container, SWT.BORDER);
		list.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				list.getSelection();
				getTemplateDescriptionLabel().setText(TemplateConfig.getDescription(list.getSelectionIndex()));
				isTemplateSelected = true;
			}
		});
		
		GridData gd_list = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_list.widthHint = 176;
		list.setLayoutData(gd_list);
		list.setItems(new String[] {"Name Input Screen", "Colored Name & Address Input", "Shopping List"});
		list.setItems(TemplateConfig.getTemplateDisplayNames());
		
		final CLabel lblNewLabel = new CLabel(container, SWT.BORDER | SWT.SHADOW_IN);
		lblNewLabel.setTopMargin(0);
		setTemplateDescriptionLabel(lblNewLabel);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_lblNewLabel.heightHint = 107;
		gd_lblNewLabel.widthHint = 393;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText("Choose a template on the left");
	}
	
	private void setTemplateDescriptionLabel(CLabel label) {
		templateDescriptionLabel = label;
	}
	
	private CLabel getTemplateDescriptionLabel() {
		return templateDescriptionLabel;
	}
	
	public boolean isTemplateSelected() {
		return isTemplateSelected;
	}
	
	public int getSelectedTemplateIndex() {
		return list.getSelectionIndex();
	}

}
