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

public class TemplateSelectionPage extends WizardPage {
	
	private CLabel templateDescriptionLabel;

	/**
	 * Create the wizard.
	 */
	public TemplateSelectionPage() {
		super("wizardPage");
		setTitle("Wizard Page title");
		setDescription("Wizard Page description");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(2, false));
		
		final List list = new List(container, SWT.BORDER);
		list.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				list.getSelection();
				getTemplateDescriptionLabel().setText(list.getItems()[list.getSelectionIndex()]);
				System.out.println("Label selected");
			}
		});
		
		GridData gd_list = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_list.widthHint = 176;
		list.setLayoutData(gd_list);
		list.setItems(new String[] {"Text input app", "Hello world app"});
		
		final CLabel lblNewLabel = new CLabel(container, SWT.BORDER | SWT.SHADOW_IN);
		setTemplateDescriptionLabel(lblNewLabel);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_lblNewLabel.heightHint = 268;
		gd_lblNewLabel.widthHint = 393;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText("Template Description");
	}
	
	private void setTemplateDescriptionLabel(CLabel label) {
		templateDescriptionLabel = label;
	}
	
	private CLabel getTemplateDescriptionLabel() {
		return templateDescriptionLabel;
	}

}
