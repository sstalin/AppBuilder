package edu.depaul.madl.wizards.pages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import edu.depaul.cdm.madl.eclipse.ui.Activator;
import edu.depaul.cdm.madl.eclipse.ui.PreferenceConstants;

public class PageTwo extends WizardPage {

	private Text appBuilderSourceText;
	private Composite container;
	
	public PageTwo() {
		super("Page Two");
		setTitle("Configure Project");
		setDescription("Select project configuration settings");
	}

	@Override
	public void createControl(final Composite parent) {
		// Setup the layout
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 1;
		
		// AppBuilder source directory dialog label
		Label appBuilderSourceFileDialogLabel = new Label(container, SWT.NONE);
		appBuilderSourceFileDialogLabel.setText("AppBuilder Home:");
		
		// Button to open the directory dialog
		Button button = new Button(container, SWT.PUSH);
	    button.setText("Select Directory");
	    button.addSelectionListener(new SelectionAdapter() {
	      @Override
	      public void widgetSelected(SelectionEvent e) {
	        openDirectoryDialog();
	      }
	    });
	    
	    // Text field to display or allow user to enter the AppBuilder source directory
		appBuilderSourceText = new Text(container, SWT.BORDER | SWT.SINGLE);
		System.out.println("PreferenceConstants.APP_BUILDER_HOME: " + PreferenceConstants.APP_BUILDER_HOME);
		appBuilderSourceText.setText(PreferenceConstants.APP_BUILDER_HOME);
		appBuilderSourceText.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!appBuilderSourceText.getText().isEmpty()) {
					setPageComplete(true);
				}
			}
		});
		
		// Stretches the text field across both columns, otherwise it's too small
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		appBuilderSourceText.setLayoutData(gd);
		
		// Required to avoid an error in the system
		setControl(container);
		setPageComplete(true);
	}

	public String getAppBuilderSourceRootDirectory() {
		return appBuilderSourceText.getText();
	}
	
	private void openDirectoryDialog() {
		DirectoryDialog dirDialog = new DirectoryDialog(container.getShell());
	    dirDialog.setText("Select AppBuilder Home directory");
	    String selectedDir = dirDialog.open();
	    System.out.println("Selected directory: " + selectedDir);
	    appBuilderSourceText.setText(selectedDir);
	    PreferenceConstants.APP_BUILDER_HOME = selectedDir;
//	    Activator.getDefault().getPreferenceStore().setDefault("APP_BUILDER_HOME", selectedDir);
	}

}
