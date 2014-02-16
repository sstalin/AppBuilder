package edu.depaul.madl.wizards.pages;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import edu.depaul.madl.wizards.constants.AppPlatform;

public class PageTwo extends WizardPage {

	private Text appBuilderSourceText;
	private Composite container;
	
	private Text textDeveloperName;
	private Text textDeveloperOrg;
	private Text textDeveloperDomain;
	
	private Button iosEnabledYes;
	private Combo iosVersions;
	private Text iosAppDestinationText;
	
	private Button androidEnabledYes;
	private Combo androidVersions;
	
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
		layout.numColumns = 2;
		
		// Developer - Name Label
		Label labelDeveloperName = new Label(container, SWT.NONE);
		labelDeveloperName.setText("Developer Name:");
		
		// Developer - Name Field
		textDeveloperName = new Text(container, SWT.BORDER);
		GridData developerNameGridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		developerNameGridData.horizontalIndent = 8;
		textDeveloperName.setLayoutData(developerNameGridData);
		GridData developerNameData = new GridData(SWT.FILL, SWT.TOP, true, false);
		textDeveloperName.setData(developerNameData);
		
		// Developer - Org Label
		Label labelDeveloperOrg = new Label(container, SWT.NONE);
		labelDeveloperOrg.setText("Developer Organization:");
		
		// Developer - Org Field
		textDeveloperOrg = new Text(container, SWT.BORDER);
		GridData developerOrgGridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		developerOrgGridData.horizontalIndent = 8;
		textDeveloperOrg.setLayoutData(developerOrgGridData);
		GridData developerOrgData = new GridData(SWT.FILL, SWT.TOP, true, false);
		textDeveloperOrg.setData(developerOrgData );
		
		// Developer - Domain Label
		Label labelDeveloperDomain = new Label(container, SWT.NONE);
		labelDeveloperDomain.setText("Developer Domain:");
		
		// Developer - Domain Field
		textDeveloperDomain = new Text(container, SWT.BORDER);
		GridData developerDomainGridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		developerDomainGridData.horizontalIndent = 8;
		textDeveloperDomain.setLayoutData(developerDomainGridData);
		GridData developerDomainData = new GridData(SWT.FILL, SWT.TOP, true, false);
		textDeveloperDomain.setData(developerDomainData);
		
		// Platform - iOS Enabled Radio Buttons
		Label iosEnabledLabel = new Label(container, SWT.BORDER);
		iosEnabledLabel.setText("Create iOS App:");
		
		// Make a separate Composite to group the iOS radio buttons
		Composite iosRadioButtonContainer = new Composite(container, SWT.NONE);
		GridLayout iosLayout = new GridLayout();
		iosRadioButtonContainer.setLayout(iosLayout);
		iosLayout.numColumns = 3;
		
		// Add the iOS buttons to the Composite and set default to Yes
		iosEnabledYes = new Button(iosRadioButtonContainer, SWT.RADIO);
		iosEnabledYes.setText("Yes");
		Button iosEnabledNo = new Button(iosRadioButtonContainer, SWT.RADIO);
		iosEnabledNo.setText("No");
		iosEnabledYes.setSelection(true);
		
		// Platform - iOS Version
		Label iosVersionLabel = new Label(container, SWT.BORDER);
		iosVersionLabel.setText("iOS Version:");
		
		// iOS Versions dropdown with latest selected by default
		iosVersions = new Combo(container, SWT.READ_ONLY);
		populateIosVersionsCombo();
		defaultToLatestIosVersion();
		
		// Destination where to save the generated iOS app
		Label iosAppDestinationLabel = new Label(container, SWT.BORDER);
		iosAppDestinationLabel.setText("iOS App Destination:");
		
		iosAppDestinationText = new Text(container, SWT.BORDER);
		GridData iosAppDestinationGridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		iosAppDestinationGridData.horizontalIndent = 8;
		iosAppDestinationText.setLayoutData(iosAppDestinationGridData);
		GridData iosAppDestinationData = new GridData(SWT.FILL, SWT.TOP, true, false);
		iosAppDestinationText.setData(iosAppDestinationData);
		
//		DirectoryFieldEditor iosDirFieldEditor = // TODO, this is convenient but breaks layout
//				new DirectoryFieldEditor("APP_BUILDER_HOME", "iOS App Destination:", container);
		
		// Platform - Android Enabled Radio Buttons
		Label androidEnabledLabel = new Label(container, SWT.BORDER);
		androidEnabledLabel.setText("Create Android App:");
		
		// Make a separate Composite to group the Android radio buttons
		Composite androidRadioButtonContainer = new Composite(container, SWT.NONE);
		GridLayout androidLayout = new GridLayout();
		androidRadioButtonContainer.setLayout(androidLayout);
		androidLayout.numColumns = 3;
		
		// Add the Android buttons to the Composite and set default to Yes
		androidEnabledYes = new Button(androidRadioButtonContainer, SWT.RADIO);
		androidEnabledYes.setText("Yes");
		Button androidEnabledNo = new Button(androidRadioButtonContainer, SWT.RADIO);
		androidEnabledNo.setText("No");
		androidEnabledYes.setSelection(true);
		
		// Platform - Android Version
		Label androidVersionLabel = new Label(container, SWT.BORDER);
		androidVersionLabel.setText("Android Version:");
		
		// Android Versions dropdown with 4 selected by default
		androidVersions = new Combo(container, SWT.READ_ONLY);
		populateAndroidVersionsCombo();
		defaultToLatestAndroidVersion();
		
		// Required to avoid an error in the system
		setControl(container);
		setPageComplete(true);
	}

	public String getAppBuilderSourceRootDirectory() {
		return appBuilderSourceText.getText();
	}
	
	public String getDeveloperName() {
		System.out.println("getDeveloperName: " + textDeveloperName.toString());
		return textDeveloperName.getText();
	}

	public String getDeveloperOrg() {
		System.out.println("getDeveloperOrg: " + textDeveloperOrg.toString());
		return textDeveloperOrg.getText();
	}
	
	public String getDeveloperDomain() {
		System.out.println("getDeveloperDomain: " + textDeveloperDomain.toString());
		return textDeveloperDomain.getText();
	}
	
	public boolean isIosEnabled() {
		System.out.println("isIosEnabled: " + iosEnabledYes.getSelection());
		return iosEnabledYes.getSelection();
	}
	
	public String getIosVersion() {
		System.out.println("getIosVersion: " + iosVersions.getItems()[iosVersions.getSelectionIndex()]);
		return iosVersions.getItems()[iosVersions.getSelectionIndex()];
	}
	
	public boolean isAndroidEnabled() {
		System.out.println("isAndroidEnabled: " + androidEnabledYes.getSelection());
		return androidEnabledYes.getSelection();
	}
	
	public String getAndroidVersion() {
		System.out.println("getAndroidVersion: " + androidVersions.getItems()[androidVersions.getSelectionIndex()]);
		return androidVersions.getItems()[androidVersions.getSelectionIndex()];
	}
	
	private void defaultToLatestAndroidVersion() {
		androidVersions.select(AppPlatform.getAndroidVersions().length - 1);
	}

	private void populateAndroidVersionsCombo() {
		for (int i = 0; i < AppPlatform.getAndroidVersions().length; i++) {
			androidVersions.add(AppPlatform.getAndroidVersions()[i]);
		}
	}

	private void defaultToLatestIosVersion() {
		iosVersions.select(AppPlatform.getIOsVersions().length - 1);
	}

	private void populateIosVersionsCombo() {
		for (int i = 0; i < AppPlatform.getIOsVersions().length; i++) {
			iosVersions.add(AppPlatform.getIOsVersions()[i]);
		}
	}
}
