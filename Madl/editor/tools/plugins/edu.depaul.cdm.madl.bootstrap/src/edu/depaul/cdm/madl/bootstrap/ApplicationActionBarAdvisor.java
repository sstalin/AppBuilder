package edu.depaul.cdm.madl.bootstrap;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

  private IWorkbenchAction introAction;

  public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
    super(configurer);
    // TODO Auto-generated constructor stub
  }

  @Override
  protected void fillMenuBar(IMenuManager menuBar) {
    MenuManager helpMenu = new MenuManager("&MADL-INFO", IWorkbenchActionConstants.M_HELP);
    menuBar.add(helpMenu);
    helpMenu.add(introAction);

  }

  @Override
  protected void makeActions(final IWorkbenchWindow window) {

    introAction = ActionFactory.INTRO.create(window);
    register(introAction);
  }

}
