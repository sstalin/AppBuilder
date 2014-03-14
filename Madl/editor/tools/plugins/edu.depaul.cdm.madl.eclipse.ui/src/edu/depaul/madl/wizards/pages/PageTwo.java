package edu.depaul.madl.wizards.pages;

import org.eclipse.core.internal.runtime.Activator;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import edu.depaul.madl.wizards.constants.AppPlatform;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class PageTwo extends WizardPage {
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_5;
	
	private Combo combo;
	private Combo combo_1;
	
	private Button btnUseDefaultDestination;
	private Button button_2;
	private Button button_3;
	private Button btnBrowse;
	private Button btnYes;
	private Button button;
	
	private String iOsDestinationDir = "";
	private String androidDestinationDir = "";
	
	private Text txtGenplatformandroid;

	/**
	 * Create the wizard.
	 */
	public PageTwo() {
		super("wizardPage");
		setTitle("AppBuilder Project Configurations");
		setDescription("Setup your AppBuilder project");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(1, false));
		
		Group grpDeveloper = new Group(container, SWT.NONE);
		grpDeveloper.setText("Developer");
		GridData gd_grpDeveloper = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_grpDeveloper.heightHint = 100;
		gd_grpDeveloper.widthHint = 580;
		grpDeveloper.setLayoutData(gd_grpDeveloper);
		
		Label lblName = new Label(grpDeveloper, SWT.NONE);
		lblName.setBounds(10, 20, 70, 22);
		lblName.setText("Name:");
		
		text = new Text(grpDeveloper, SWT.BORDER);
		text.setBounds(135, 20, 440, 22);
		
		Label lblOrganization = new Label(grpDeveloper, SWT.NONE);
		lblOrganization.setBounds(10, 47, 100, 22);
		lblOrganization.setText("Organization:");
		
		Label lblDomain = new Label(grpDeveloper, SWT.NONE);
		lblDomain.setBounds(10, 75, 59, 22);
		lblDomain.setText("Domain:");
		
		text_1 = new Text(grpDeveloper, SWT.BORDER);
		text_1.setBounds(135, 47, 440, 22);
		
		text_2 = new Text(grpDeveloper, SWT.BORDER);
		text_2.setBounds(135, 75, 440, 22);
		
		Group grpIos = new Group(container, SWT.NONE);
		GridData gd_grpIos = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_grpIos.heightHint = 130;
		gd_grpIos.widthHint = 580;
		grpIos.setLayoutData(gd_grpIos);
		grpIos.setText("iOS");
		
		Label lblCreateIosApp = new Label(grpIos, SWT.NONE);
		lblCreateIosApp.setText("Create iOS App:");
		lblCreateIosApp.setBounds(10, 20, 85, 22);
		
		Label lblVersion = new Label(grpIos, SWT.NONE);
		lblVersion.setText("Version:");
		lblVersion.setBounds(10, 55, 100, 22);
		
		Label lblDestination = new Label(grpIos, SWT.NONE);
		lblDestination.setText("Destination:");
		lblDestination.setBounds(10, 115, 100, 22);
		
		//Gan/IOSplatform 
		text_5 = new Text(grpIos, SWT.BORDER);
		text_5.setBounds(135, 115, 300, 22);
		text_5.setText(getDefaultIosAppDestinationDirectory());
		text_5.setEnabled(false);
		
		btnYes = new Button(grpIos, SWT.RADIO);
		btnYes.setSelection(true);
		btnYes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				combo.setEnabled(true);
				btnUseDefaultDestination.setEnabled(true);
				
				if (!btnUseDefaultDestination.getSelection()) {
					text_5.setEnabled(true);
					btnBrowse.setEnabled(true);
				}
			}
		});
		btnYes.setBounds(135, 15, 50, 22);
		btnYes.setText("Yes");
		
		Button btnNo = new Button(grpIos, SWT.RADIO);
		btnNo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				combo.setEnabled(false);
				btnUseDefaultDestination.setEnabled(false);
				text_5.setEnabled(false);
				btnBrowse.setEnabled(false);
			}
		});
		btnNo.setBounds(195, 15, 50, 22);
		btnNo.setText("No");
		
		combo = new Combo(grpIos, SWT.READ_ONLY);
		combo.setBounds(135, 55, 70, 22);
		populateIosVersionsCombo(combo);
		defaultToLatestIosVersion(combo);
		
		final DirectoryDialog iosDirDialog = new DirectoryDialog(parent.getShell());
		iosDirDialog.setText("Select the iOS app destination directory");
		btnBrowse = new Button(grpIos, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String destination = iosDirDialog.open();
				setIosDestinationDir(destination);
			}
		});
		btnBrowse.setEnabled(false);
		btnBrowse.setBounds(442, 115, 100, 24);
		btnBrowse.setText("Browse");
		
		btnUseDefaultDestination = new Button(grpIos, SWT.CHECK);
		btnUseDefaultDestination.setSelection(true);
		btnUseDefaultDestination.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Toggle directory text between enabled/disabled
				text_5.setEnabled(!text_5.getEnabled());
				
				if (text_5.isEnabled()) {	// If text became enabled, reset text to what user entered previously (or blank)
					text_5.setText(getIosDestinationDir());					
				} else {					// If text became disabled, reset text to the default destination directory
					setIosDestinationDir(text_5.getText());
					text_5.setText(getDefaultIosAppDestinationDirectory());					
				}
				
				btnBrowse.setEnabled(!btnBrowse.getEnabled());
			}
		});
		
		btnUseDefaultDestination.setBounds(135, 90, 160, 22);
		btnUseDefaultDestination.setText("Use default destination");
		
		Group grpAndroid = new Group(container, SWT.NONE);
		GridData gd_grpAndroid = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_grpAndroid.heightHint = 130;
		gd_grpAndroid.widthHint = 580;
		grpAndroid.setLayoutData(gd_grpAndroid);
		grpAndroid.setText("Android");
		
		Label lblCreateAndroidApp = new Label(grpAndroid, SWT.NONE);
		lblCreateAndroidApp.setText("Create Android App:");
		lblCreateAndroidApp.setBounds(10, 20, 110, 22);
		
		Label label_1 = new Label(grpAndroid, SWT.NONE);
		label_1.setText("Version:");
		label_1.setBounds(10, 55, 100, 22);
		
		Label label_2 = new Label(grpAndroid, SWT.NONE);
		label_2.setText("Destination:");
		label_2.setBounds(10, 115, 100, 22);
		
		
		txtGenplatformandroid = new Text(grpAndroid, SWT.BORDER);
		txtGenplatformandroid.setText("gen/Platform.Android");
		txtGenplatformandroid.setEnabled(false);
		
		txtGenplatformandroid.setBounds(135, 115, 300, 22);
		
		button = new Button(grpAndroid, SWT.RADIO);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				combo_1.setEnabled(true);
				button_3.setEnabled(true);
				
				if (!button_3.getSelection()) {
					txtGenplatformandroid.setEnabled(true);
					button_2.setEnabled(true);
				}
			}
		});
		
		combo.setEnabled(true);
		btnUseDefaultDestination.setEnabled(true);
		
		if (!btnUseDefaultDestination.getSelection()) {
			text_5.setEnabled(true);
			btnBrowse.setEnabled(true);
		}
		
		button.setSelection(true);
		button.setText("Yes");
		button.setBounds(135, 15, 50, 22);
		
		
		Button button_1 = new Button(grpAndroid, SWT.RADIO);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				combo_1.setEnabled(false);
				button_3.setEnabled(false);
				txtGenplatformandroid.setEnabled(false);
				button_2.setEnabled(false);
			}
		});
		button_1.setText("No");
		button_1.setBounds(195, 15, 50, 22);
		
		combo_1 = new Combo(grpAndroid, SWT.READ_ONLY);
		combo_1.setBounds(135, 55, 70, 22);
		combo_1.select(-1);
		populateAndroidVersionsCombo(combo_1);
		defaultToLatestAndroidVersion(combo_1);
		
		final DirectoryDialog androidDirDialog = new DirectoryDialog(parent.getShell());
		androidDirDialog.setText("Select the Android app destination directory");
		button_2 = new Button(grpAndroid, SWT.NONE);
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String destination = androidDirDialog.open();
				setAndroidDestinationDir(destination);
			}
		});
		
		
		// Browse for and android
		
		button_2.setText("Browse");
		button_2.setEnabled(false);
		button_2.setBounds(442, 115, 100, 24);
		
		button_3 = new Button(grpAndroid, SWT.CHECK);
		button_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Toggle directory text between enabled/disabled
				txtGenplatformandroid.setEnabled(!txtGenplatformandroid.getEnabled());
				
				if (txtGenplatformandroid.isEnabled()) {	// If text became enabled, reset text to what user entered previously (or blank)
					txtGenplatformandroid.setText(getAndroidDestinationDir());					
				} else {					// If text became disabled, reset text to the default destination directory
					setAndroidDestinationDir(txtGenplatformandroid.getText());
					txtGenplatformandroid.setText(getDefaultAndroidAppDestinationDirectory());					
				}
				
				button_2.setEnabled(!button_2.getEnabled());
			}
		});
		button_3.setText("Use default destination");
		button_3.setSelection(true);
		button_3.setBounds(135, 90, 160, 22);
	}
	
	private void populateIosVersionsCombo(Combo combo) {
		for (int i = 0; i < AppPlatform.getIOsVersions().length; i++) {
			combo.add(AppPlatform.getIOsVersions()[i]);
		}
	}
	
	private void defaultToLatestIosVersion(Combo combo) {
		combo.select(AppPlatform.getIOsVersions().length - 1);
	}

	private void populateAndroidVersionsCombo(Combo combo) {
		for (int i = 0; i < AppPlatform.getAndroidVersions().length; i++) {
			combo.add(AppPlatform.getAndroidVersions()[i]);
		}
	}
	
	private void defaultToLatestAndroidVersion(Combo combo) {
		combo.select(AppPlatform.getAndroidVersions().length - 1);
	}
	
	// TODO, hard-coded
	private String getDefaultIosAppDestinationDirectory() {
		return "gen/Platform.ios";
	}
	
	private void setIosDestinationDir(String destination) {
		System.out.println("Setting iOS dir to: " + destination);
		iOsDestinationDir = destination;
		text_5.setText(destination);
		text_5.setFocus();
		System.out.println("iOS dir set to: " + iOsDestinationDir);
	}
	
	public String getIosDestinationDir() {
		System.out.println("Getting iOS destination dir: " + iOsDestinationDir);
		return iOsDestinationDir;
	}
	
	// TODO, hard-coded
	private String getDefaultAndroidAppDestinationDirectory() {
		return "gen/Platform.Android";
	}
	
	private void setAndroidDestinationDir(String destination) {
		System.out.println("Setting Android dir to: " + destination);
		androidDestinationDir = destination;
		txtGenplatformandroid.setText(destination);
		txtGenplatformandroid.setFocus();
		System.out.println("iOS dir set to: " + iOsDestinationDir);
	}

	public String getAndroidDestinationDir() {
		System.out.println("Getting Android destination dir: " + androidDestinationDir);
		return androidDestinationDir;
	}

	public String getDeveloperName() {
		return text.getText();
	}

	public String getDeveloperOrg() {
		return text_1.getText();
	}

	public String getDeveloperDomain() {
		return text_2.getText();
	}

	public boolean isIosEnabled() {
		return btnYes.getSelection();
	}

	public String getIosVersion() {
		return combo.getItems()[combo.getSelectionIndex()];
	}

	public boolean isAndroidEnabled() {
		return button.getSelection();
	}

	public String getAndroidVersion() {
		return combo_1.getItems()[combo_1.getSelectionIndex()];
	}
	
	public boolean isDefaultIosOutputDir() {
		return btnUseDefaultDestination.getSelection();
	}
	
	public boolean isDefaultAndroidOutputDir() {
		return button_3.getSelection();
	}
}