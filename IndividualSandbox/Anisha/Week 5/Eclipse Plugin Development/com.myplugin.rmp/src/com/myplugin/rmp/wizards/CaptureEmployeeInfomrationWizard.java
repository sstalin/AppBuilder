package com.myplugin.rmp.wizards;
import org.eclipse.jface.wizard.Wizard;
public class CaptureEmployeeInfomrationWizard extends Wizard {
     PersonalInformationPage personalInfoPage;
     AddressInformationPage addressInfoPage;

     public void addPages() {
              personalInfoPage = new PersonalInformationPage("Personal Information Page");
              addPage(personalInfoPage);
              addressInfoPage = new AddressInformationPage("Address Information");
              addPage(addressInfoPage);
     }
     public boolean performFinish() {
              return false;
     }
}