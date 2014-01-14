/*
 * Copyright (c) 2012, the Madl project authors.
 * 
 * Licensed under the Eclipse Public License v1.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package edu.depaul.cdm.madl.tools.ui;

import edu.depaul.cdm.madl.engine.context.AnalysisContext;
import edu.depaul.cdm.madl.engine.element.Element;
import edu.depaul.cdm.madl.engine.search.SearchScope;
import edu.depaul.cdm.madl.engine.search.SearchScopeFactory;
import edu.depaul.cdm.madl.engine.source.Source;
import edu.depaul.cdm.madl.tools.core.MadlCore;
import edu.depaul.cdm.madl.tools.core.analysis.model.ProjectManager;
import edu.depaul.cdm.madl.tools.core.analysis.model.ResourceMap;
import edu.depaul.cdm.madl.tools.core.model.MadlElement;
import edu.depaul.cdm.madl.tools.core.model.MadlModelException;
import edu.depaul.cdm.madl.tools.core.model.SourceReference;
//import edu.depaul.cdm.madl.tools.internal.corext.refactoring.util.MadlElementUtil;
//import edu.depaul.cdm.madl.tools.ui.dialogs.TypeSelectionExtension;
import edu.depaul.cdm.madl.tools.ui.internal.SharedImages;
//import edu.depaul.cdm.madl.tools.ui.internal.dialogs.FilteredTypesSelectionDialog;
import edu.depaul.cdm.madl.tools.ui.internal.text.editor.MadlEditor;
import edu.depaul.cdm.madl.tools.ui.internal.text.editor.EditorUtility;
import edu.depaul.cdm.madl.tools.ui.text.IColorManager;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;

import java.net.URL;

/**
 * Central access point for the Madl UI plug-in (id <code>"edu.depaul.cdm.madl.tools.ui"</code>). This
 * class provides static methods for:
 * <ul>
 * <li>creating various kinds of selection dialogs to present a collection of Madl elements to the
 * user and let them make a selection.</li>
 * <li>opening a Madl editor on a compilation unit.</li>
 * </ul>
 * <p>
 * This class provides static methods and fields only; it is not intended to be instantiated or
 * subclassed by clients.
 */
public final class MadlUI {

  private static ISharedImages fgSharedImages = null;

  /**
   * The id of the Madl UI plug-in (value <code>"edu.depaul.cdm.madl.tools.ui"</code>).
   */
  public static final String ID_PLUGIN = "edu.depaul.cdm.madl.tools.ui"; //$NON-NLS-1$

  /**
   * The id of the Madl perspective (value <code>"edu.depaul.cdm.madl.tools.ui.JavaPerspective"</code>).
   */
  public static final String ID_PERSPECTIVE = "edu.depaul.cdm.madl.tools.ui.JavaPerspective"; //$NON-NLS-1$

  /**
   * The id of the JavaScript action set (value
   * <code>"edu.depaul.cdm.madl.tools.ui.JavaActionSet"</code>).
   */
  public static final String ID_ACTION_SET = "edu.depaul.cdm.madl.tools.ui.JavaActionSet"; //$NON-NLS-1$

  /**
   * The id of the JavaScript Element Creation action set (value
   * <code>"edu.depaul.cdm.madl.tools.ui.JavaElementCreationActionSet"</code>).
   */
  public static final String ID_ELEMENT_CREATION_ACTION_SET = "edu.depaul.cdm.madl.tools.ui.JavaElementCreationActionSet"; //$NON-NLS-1$

  /**
   * The id of the JavaScript Coding action set (value
   * <code>"edu.depaul.cdm.madl.tools.ui.CodingActionSet"</code>).
   */
  public static final String ID_CODING_ACTION_SET = "edu.depaul.cdm.madl.tools.ui.CodingActionSet"; //$NON-NLS-1$

  /**
   * The id of the JavaScript action set for open actions (value
   * <code>"edu.depaul.cdm.madl.tools.ui.A_OpenActionSet"</code>).
   */
  public static final String ID_OPEN_ACTION_SET = "edu.depaul.cdm.madl.tools.ui.A_OpenActionSet"; //$NON-NLS-1$

  /**
   * The id of the JavaScript Search action set (value
   * <code>edu.depaul.cdm.madl.tools.ui.SearchActionSet"</code>).
   */
  public static final String ID_SEARCH_ACTION_SET = "edu.depaul.cdm.madl.tools.ui.SearchActionSet"; //$NON-NLS-1$

  /**
   * The editor part id of the editor that presents JavaScript compilation units (value
   * <code>"edu.depaul.cdm.madl.tools.ui.text.editor.CompilationUnitEditor"</code> ).
   */
  public static final String ID_CU_EDITOR = "edu.depaul.cdm.madl.tools.ui.text.editor.CompilationUnitEditor"; //$NON-NLS-1$

  /**
   * The editor part id of the editor that presents JavaScript binary class files (value
   * <code>"edu.depaul.cdm.madl.tools.ui.ClassFileEditor"</code>).
   */
  public static final String ID_CF_EDITOR = "edu.depaul.cdm.madl.tools.ui.ClassFileEditor"; //$NON-NLS-1$

  /**
   * The editor part id of the default text editor (used in the RCP but not the plugin).
   */
  public static final String ID_DEFAULT_TEXT_EDITOR = "edu.depaul.cdm.madl.tools.ui.text.editor.TextEditor";

  /**
   * The editor part id of the code snippet editor (value
   * <code>"edu.depaul.cdm.madl.tools.ui.SnippetEditor"</code>).
   */
  public static final String ID_SNIPPET_EDITOR = "edu.depaul.cdm.madl.tools.ui.SnippetEditor"; //$NON-NLS-1$

  /**
   * The view part id of the Problems view (value
   * <code>"edu.depaul.cdm.madl.tools.ui.ProblemsView"</code>).
   */
  public static final String ID_PROBLEMS = "edu.depaul.cdm.madl.tools.ui.ProblemsView"; //$NON-NLS-1$

  /**
   * The view part id of the Call Hierarchy view (value
   * <code>"edu.depaul.cdm.madl.tools.ui.callhierarchy.view"</code>).
   */
  public static final String ID_CALL_HIERARCHY = "edu.depaul.cdm.madl.tools.ui.callhierarchy.view"; //$NON-NLS-1$

  /**
   * The view part id of the type hierarchy part (value
   * <code>"edu.depaul.cdm.madl.tools.ui.TypeHierarchy"</code>).
   * <p>
   * When this id is used to access a view part with <code>IWorkbenchPage.findView</code> or
   * <code>showView</code>, the returned <code>IViewPart</code> can be safely cast to an
   * <code>ITypeHierarchyViewPart</code>.
   * </p>
   * 
   * @see ITypeHierarchyViewPart
   * @see org.eclipse.ui.IWorkbenchPage#findView(java.lang.String)
   * @see org.eclipse.ui.IWorkbenchPage#showView(java.lang.String)
   */
  public static final String ID_TYPE_HIERARCHY = "edu.depaul.cdm.madl.tools.ui.TypeHierarchy"; //$NON-NLS-1$

  /**
   * The view part id of the source (declaration) view (value
   * <code>"edu.depaul.cdm.madl.tools.ui.SourceView"</code>).
   * 
   * @see org.eclipse.ui.IWorkbenchPage#findView(java.lang.String)
   * @see org.eclipse.ui.IWorkbenchPage#showView(java.lang.String)
   */
  public static final String ID_SOURCE_VIEW = "edu.depaul.cdm.madl.tools.ui.SourceView"; //$NON-NLS-1$

  /**
   * The view part id of the Files view (value <code>"edu.depaul.cdm.madl.tools.ui.FileExplorer"</code>
   * ).
   */
  public static final String ID_FILE_EXPLORER = "edu.depaul.cdm.madl.tools.ui.FileExplorer"; //$NON-NLS-1$

  /**
   * The view part id of the Apps view (value <code>"edu.depaul.cdm.madl.tools.ui.AppsView"</code>).
   */
  public static final String ID_APPS_VIEW = "edu.depaul.cdm.madl.tools.ui.AppsView"; //$NON-NLS-1$

  /**
   * The view part id of the Console view (value <code>"edu.depaul.cdm.madl.tools.ui.console"</code>).
   */
  public static final String ID_CONSOLE_VIEW = "edu.depaul.cdm.madl.tools.ui.console"; //$NON-NLS-1$

  public static final String ID_INSPECTOR_VIEW = "edu.depaul.cdm.madl.tools.debug.objectInspectorView"; //$NON-NLS-1$

  /**
   * The view part id of the Files view (value <code>"edu.depaul.cdm.madl.tools.ui.FilesView"</code> ).
   */
  public static final String ID_FILE_VIEW = "edu.depaul.cdm.madl.tools.ui.FilesView"; //$NON-NLS-1$

  /**
   * The view part id of the Tests view (<code>"edu.depaul.cdm.madl.tools.ui.MadlUnitView"</code>).
   */
  public static final String ID_MADLUNIT_VIEW = "edu.depaul.cdm.madl.tools.ui.MadlUnitView"; //$NON-NLS-1$

  /**
   * The view part id of the JavaScript Documentation view (value
   * <code>"edu.depaul.cdm.madl.tools.ui.JavadocView"</code>).
   * 
   * @see org.eclipse.ui.IWorkbenchPage#findView(java.lang.String)
   * @see org.eclipse.ui.IWorkbenchPage#showView(java.lang.String)
   */
  public static final String ID_JAVADOC_VIEW = "edu.depaul.cdm.madl.tools.ui.JavadocView"; //$NON-NLS-1$

  /**
   * The id of the JavaScript Browsing Perspective (value
   * <code>"edu.depaul.cdm.madl.tools.ui.JavaBrowsingPerspective"</code>).
   */
  public static String ID_BROWSING_PERSPECTIVE = "edu.depaul.cdm.madl.tools.ui.JavaBrowsingPerspective"; //$NON-NLS-1$

  /**
   * The view part id of the JavaScript Browsing Projects view (value
   * <code>"edu.depaul.cdm.madl.tools.ui.ProjectsView"</code>).
   */
  public static String ID_PROJECTS_VIEW = "edu.depaul.cdm.madl.tools.ui.ProjectsView"; //$NON-NLS-1$

  /**
   * The view part id of the JavaScript Browsing Packages view (value
   * <code>"edu.depaul.cdm.madl.tools.ui.PackagesView"</code>).
   */
  public static String ID_PACKAGES_VIEW = "edu.depaul.cdm.madl.tools.ui.PackagesView"; //$NON-NLS-1$

  /**
   * The view part id of the JavaScript Browsing Types view (value
   * <code>"edu.depaul.cdm.madl.tools.ui.TypesView"</code>).
   */
  public static String ID_TYPES_VIEW = "edu.depaul.cdm.madl.tools.ui.TypesView"; //$NON-NLS-1$

  /**
   * The view part id of the JavaScript Browsing Members view (value
   * <code>"edu.depaul.cdm.madl.tools.ui.MembersView"</code>).
   */
  public static String ID_MEMBERS_VIEW = "edu.depaul.cdm.madl.tools.ui.MembersView"; //$NON-NLS-1$

  /**
   * Creates a selection dialog that lists all types in the given project. The caller is responsible
   * for opening the dialog with <code>Window.open</code>, and subsequently extracting the selected
   * type(s) (of type <code>Type</code> ) via <code>SelectionDialog.getResult</code>.
   * 
   * @param parent the parent shell of the dialog to be created
   * @param context the runnable context used to show progress when the dialog is being populated
   * @param project the project
   * @param style flags defining the style of the dialog; the only valid values are
   *          <code>IMadlElementSearchConstants.CONSIDER_CLASSES</code>,
   *          <code>IMadlElementSearchConstants.CONSIDER_INTERFACES</code>,
   *          <code>IMadlElementSearchConstants.CONSIDER_ANNOTATION_TYPES</code> ,
   *          <code>IMadlElementSearchConstants.CONSIDER_ENUMS</code>,
   *          <code>IMadlElementSearchConstants.CONSIDER_ALL_TYPES</code>,
   *          <code>IMadlElementSearchConstants.CONSIDER_CLASSES_AND_INTERFACES</code>
   *          <code>IMadlElementSearchConstants.CONSIDER_CLASSES_AND_ENUMS</code> . Please note that
   *          the bitwise OR combination of the elementary constants is not supported.
   * @param multipleSelection <code>true</code> if multiple selection is allowed
   * @return a new selection dialog
   * @exception MadlModelException if the selection dialog could not be opened
   */
  //ss
/*  public static SelectionDialog createTypeDialog(Shell parent, IRunnableContext context,
      IProject project, int style, boolean multipleSelection) throws MadlModelException {
    // TODO (pquitslund): update to use project scope once implemented in search core
    //MadlSearchScope scope = SearchEngine.createJavaSearchScope(
    //                            new MadlProject[] {JavaScriptCore.create(project)});
    SearchScope scope = SearchScopeFactory.createUniverseScope();
    return createTypeDialog(parent, context, scope, style, multipleSelection);
  }*/

  /**
   * Creates a selection dialog that lists all types in the given scope. The caller is responsible
   * for opening the dialog with <code>Window.open</code>, and subsequently extracting the selected
   * type(s) (of type <code>Type</code> ) via <code>SelectionDialog.getResult</code>.
   * 
   * @param parent the parent shell of the dialog to be created
   * @param context the runnable context used to show progress when the dialog is being populated
   * @param scope the scope that limits which types are included
   * @param style flags defining the style of the dialog; the only valid values are
   *          <code>IMadlElementSearchConstants.CONSIDER_CLASSES</code>,
   *          <code>IMadlElementSearchConstants.CONSIDER_INTERFACES</code>,
   *          <code>IMadlElementSearchConstants.CONSIDER_ANNOTATION_TYPES</code> ,
   *          <code>IMadlElementSearchConstants.CONSIDER_ENUMS</code>,
   *          <code>IMadlElementSearchConstants.CONSIDER_ALL_TYPES</code>,
   *          <code>IMadlElementSearchConstants.CONSIDER_CLASSES_AND_INTERFACES</code>
   *          <code>IMadlElementSearchConstants.CONSIDER_CLASSES_AND_ENUMS</code> . Please note that
   *          the bitwise OR combination of the elementary constants is not supported.
   * @param multipleSelection <code>true</code> if multiple selection is allowed
   * @return a new selection dialog
   * @exception MadlModelException if the selection dialog could not be opened
   */
  //ss
/*  public static SelectionDialog createTypeDialog(Shell parent, IRunnableContext context,
      SearchScope scope, int style, boolean multipleSelection) throws MadlModelException {
    return createTypeDialog(parent, context, scope, style, multipleSelection, "");//$NON-NLS-1$
  }*/

  /**
   * Creates a selection dialog that lists all types in the given scope. The caller is responsible
   * for opening the dialog with <code>Window.open</code>, and subsequently extracting the selected
   * type(s) (of type <code>Type</code> ) via <code>SelectionDialog.getResult</code>.
   * 
   * @param parent the parent shell of the dialog to be created
   * @param context the runnable context used to show progress when the dialog is being populated
   * @param scope the scope that limits which types are included
   * @param style flags defining the style of the dialog; the only valid values are
   *          <code>IMadlElementSearchConstants.CONSIDER_CLASSES</code>,
   *          <code>IMadlElementSearchConstants.CONSIDER_INTERFACES</code>,
   *          <code>IMadlElementSearchConstants.CONSIDER_ANNOTATION_TYPES</code> ,
   *          <code>IMadlElementSearchConstants.CONSIDER_ENUMS</code>,
   *          <code>IMadlElementSearchConstants.CONSIDER_ALL_TYPES</code>,
   *          <code>IMadlElementSearchConstants.CONSIDER_CLASSES_AND_INTERFACES</code>
   *          <code>IMadlElementSearchConstants.CONSIDER_CLASSES_AND_ENUMS</code> . Please note that
   *          the bitwise OR combination of the elementary constants is not supported.
   * @param multipleSelection <code>true</code> if multiple selection is allowed
   * @param filter the initial pattern to filter the set of types. For example "Abstract" shows all
   *          types starting with "abstract". The meta character '?' representing any character and
   *          '*' representing any string are supported. Clients can pass an empty string if no
   *          filtering is required.
   * @return a new selection dialog
   * @exception MadlModelException if the selection dialog could not be opened
   */
  //ss
 /* public static SelectionDialog createTypeDialog(Shell parent, IRunnableContext context,
      SearchScope scope, int style, boolean multipleSelection, String filter)
      throws MadlModelException {
    return createTypeDialog(parent, context, scope, style, multipleSelection, filter, null);
  }*/

  /**
   * Creates a selection dialog that lists all types in the given scope. The caller is responsible
   * for opening the dialog with <code>Window.open</code>, and subsequently extracting the selected
   * type(s) (of type <code>Type</code> ) via <code>SelectionDialog.getResult</code>.
   * 
   * @param parent the parent shell of the dialog to be created
   * @param context the runnable context used to show progress when the dialog is being populated
   * @param scope the scope that limits which types are included
   * @param style flags defining the style of the dialog; the only valid values are
   *          <code>IMadlElementSearchConstants.CONSIDER_CLASSES</code>,
   *          <code>IMadlElementSearchConstants.CONSIDER_INTERFACES</code>,
   *          <code>IMadlElementSearchConstants.CONSIDER_ANNOTATION_TYPES</code> ,
   *          <code>IMadlElementSearchConstants.CONSIDER_ENUMS</code>,
   *          <code>IMadlElementSearchConstants.CONSIDER_ALL_TYPES</code>,
   *          <code>IMadlElementSearchConstants.CONSIDER_CLASSES_AND_INTERFACES</code>
   *          <code>IMadlElementSearchConstants.CONSIDER_CLASSES_AND_ENUMS</code> . Please note that
   *          the bitwise OR combination of the elementary constants is not supported.
   * @param multipleSelection <code>true</code> if multiple selection is allowed
   * @param filter the initial pattern to filter the set of types. For example "Abstract" shows all
   *          types starting with "abstract". The meta character '?' representing any character and
   *          '*' representing any string are supported. Clients can pass an empty string if no
   *          filtering is required.
   * @param extension a user interface extension to the type selection dialog or <code>null</code>
   *          if no extension is desired
   * @return a new selection dialog
   * @exception MadlModelException if the selection dialog could not be opened
   */
  //ss
/*  public static SelectionDialog createTypeDialog(Shell parent, IRunnableContext context,
      SearchScope scope, int style, boolean multipleSelection, String filter,
      TypeSelectionExtension extension) throws MadlModelException {
    int elementKinds = 0;
    // TODO (pquitslund): implement search constant kinds
//    if (style == IMadlElementSearchConstants.CONSIDER_ALL_TYPES) {
//      elementKinds = IJavaScriptSearchConstants.TYPE;
//    } else if (style == IMadlElementSearchConstants.CONSIDER_CLASSES) {
//      elementKinds = IJavaScriptSearchConstants.CLASS;
//    } else {
//      throw new IllegalArgumentException("Invalid style constant."); //$NON-NLS-1$
//    }
    FilteredTypesSelectionDialog dialog = new FilteredTypesSelectionDialog(
        parent,
        multipleSelection,
        context,
        scope,
        elementKinds,
        extension);
    dialog.setMessage(MadlUIMessages.JavaUI_defaultDialogMessage);
    dialog.setInitialPattern(filter);
    return dialog;
  }*/

  /**
   * Returns the color manager the Madl UI plug-in which is used to manage any Madl-specific colors
   * needed for such things like syntax highlighting.
   * 
   * @return the color manager to be used for Madl text viewers
   */
  public static IColorManager getColorManager() {
    return MadlToolsPlugin.getDefault().getMadlTextTools().getColorManager();
  }

  /**
   * Returns the Madldoc URL for an element. This returned location doesn't have to exist. Returns
   * <code>null</code> if no Madldoc location has been attached to the element's library or project.
   * 
   * @param element the element for which the documentation URL is requested.
   * @param includeAnchor If set, the URL contains an anchor for member references
   * @return the Madldoc URL for the element
   * @throws MadlModelException thrown when the element can not be accessed
   */
  public static URL getMadlDocLocation(MadlElement element, boolean includeAnchor)
      throws MadlModelException {
    // TODO(devoncarew):
    return null;
  }

  /**
   * Returns the transfer instance used to copy/paste Madl elements to and from the clipboard.
   * Objects managed by this transfer instance are of type <code>MadlElement[]</code>. So to access
   * data from the clipboard clients should use the following code snippet:
   * 
   * <pre>
   * MadlElement[] elements = (MadlElement[]) clipboard.getContents(MadlUI.getMadlElementClipboardTransfer());
   * </pre>
   * 
   * To put elements into the clipboard use the following snippet:
   * 
   * <pre>
   *    MadlElement[] madlElements= ...;
   *    clipboard.setContents(
   *     new Object[] { madlElements },
   *     new Transfer[] { MadlUI.getMadlElementClipboardTransfer() } );
   * </pre>
   * 
   * @return returns the transfer object used to copy/paste Madl elements to and from the clipboard
   */
  public static Transfer getMadlElementClipboardTransfer() {
// TODO (pquitslund): implement cliboard transfer
//    return JavaElementTransfer.getInstance();
    return null;
  }

  /**
   * Returns the DocumentProvider used for Madl compilation units.
   * 
   * @return the DocumentProvider for Madl compilation units.
   * @see IDocumentProvider
   */
  public static IDocumentProvider getDocumentProvider() {
    return MadlToolsPlugin.getDefault().getCompilationUnitDocumentProvider();
  }

  public static Color getEditorBackground(IPreferenceStore prefs, Display display) {
    return prefs.getBoolean(AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT) ? null
        : createColor(prefs, AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND, display);
  }

  public static Color getEditorForeground(IPreferenceStore prefs, Display display) {
    return prefs.getBoolean(AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND_SYSTEM_DEFAULT) ? null
        : createColor(prefs, AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND, display);
  }

  /**
   * Returns the Madl element wrapped by the given editor input.
   * 
   * @param editorInput the editor input
   * @return the Madl element wrapped by <code>editorInput</code> or <code>null</code> if none
   */
  public static MadlElement getEditorInputMadlElement(IEditorInput editorInput) {

    // Performance: check working copy manager first: this is faster
    MadlElement de = MadlToolsPlugin.getDefault().getWorkingCopyManager().getWorkingCopy(
        editorInput);
    if (de != null) {
      return de;
    }
    return (MadlElement) editorInput.getAdapter(MadlElement.class);
  }

  /**
   * Returns the Madl element wrapped by the given editor input.
   * 
   * @param editorInput the editor input
   * @return the Madl element wrapped by <code>editorInput</code> or <code>null</code> if none
   */
  public static Element getEditorInputMadlElement2(IEditorInput editorInput) {

    //TODO (pquitslund): add support for external compilation units

//    if (editorInput instanceof ExternalCompilationUnitEditorInput) {
//      return ((ExternalCompilationUnitEditorInput) editorInput).getCompilationUnit();
//    }

    //TODO (pquitslund): consider doing this without going through the adapter service

    return (Element) editorInput.getAdapter(Element.class);
  }

  public static Color getEditorSelectionBackground(IPreferenceStore prefs, Display display) {
    return prefs.getBoolean(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SELECTION_BACKGROUND_DEFAULT_COLOR)
        ? null : createColor(
            prefs,
            AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SELECTION_BACKGROUND_COLOR,
            display);
  }

  public static Color getEditorSelectionForeground(IPreferenceStore prefs, Display display) {
    return prefs.getBoolean(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SELECTION_FOREGROUND_DEFAULT_COLOR)
        ? null : createColor(
            prefs,
            AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SELECTION_FOREGROUND_COLOR,
            display);
  }

  /**
   * @return the {@link IFile} with {@link #element} to open, may be {@code null}.
   */
  public static IFile getElementFile(Element element) {
    AnalysisContext elementContext = element.getContext();
    Source elementSource = element.getSource();
    ResourceMap map = MadlCore.getProjectManager().getResourceMap(elementContext);
    if (map == null) {
      return null;
    }
    IFile file = map.getResource(elementSource);
    // TODO(keertip): move this to a check for sources that are referenced by package: only. We cannot
    // quite figure this out right now, so we do this for all files. Package sources have their canonical
    // path, so look for a resource with the same path in the workspace.
    if (file != null) {
      IResource resource = MadlCore.getProjectManager().getResource(elementSource);
      if (resource instanceof IFile) {
        file = (IFile) resource;
      }
    }
    return file;
  }

  /**
   * Returns the shared images for the Madl UI.
   * 
   * @return the shared images manager
   */
  public static ISharedImages getSharedImages() {
    if (fgSharedImages == null) {
      fgSharedImages = new SharedImages();
    }
    return fgSharedImages;
  }

  public static Color getViewerBackground(IPreferenceStore prefs, Display display) {
    // TODO(messick) Use a color identifier distinct from the editor.
    return prefs.getBoolean(AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT) ? null
        : createColor(prefs, AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND, display);
  }

  public static Color getViewerForeground(IPreferenceStore prefs, Display display) {
    // TODO(messick) Use a color identifier distinct from the editor.
    return prefs.getBoolean(AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND_SYSTEM_DEFAULT) ? null
        : createColor(prefs, AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND, display);
  }

//TODO (pquitslund): implement when we have madl doc
//  /**
//   * Returns the Javadoc base URL for an element. The base location contains the
//   * index file. This location doesn't have to exist. Returns <code>null</code>
//   * if no javadoc location has been attached to the element's library or
//   * project. Example of a returned URL is
//   * <i>http://www.junit.org/junit/javadoc</i>.
//   *
//   * @param element the element for which the documentation URL is requested.
//   * @return the base location
//   * @throws MadlModelException thrown when the element can not be accessed
//   */
//  public static URL getJSdocBaseLocation(MadlElement element)
//      throws MadlModelException {
//    return JavaDocLocations.getJavadocBaseLocation(element);
//  }
//
//  /**
//   * Returns the Javadoc URL for an element. Example of a returned URL is
//   * <i>http://www.junit.org/junit/javadoc/junit/extensions/TestSetup.html</i>.
//   * This returned location doesn't have to exist. Returns <code>null</code> if
//   * no javadoc location has been attached to the element's library or project.
//   *
//   * @param element the element for which the documentation URL is requested.
//   * @param includeAnchor If set, the URL contains an anchor for member
//   *          references:
//   *          <i>http://www.junit.org/junit/javadoc/junit/extensions/
//   *          TestSetup.html#run(junit.framework.TestResult)</i>. Note that this
//   *          involves type resolving and is a more expensive call than without
//   *          anchor.
//   * @return the Javadoc URL for the element
//   * @throws MadlModelException thrown when the element can not be accessed
//   */
//  public static URL getJSdocLocation(MadlElement element, boolean includeAnchor)
//      throws MadlModelException {
//    return JavaDocLocations.getJavadocLocation(element, includeAnchor);
//  }
//
//  /**
//   * Returns the Javadoc location for library's classpath entry or
//   * <code>null</code> if no location is available. Note that only classpath
//   * entries of kind {@link IIncludePathEntry#CPE_LIBRARY} and
//   * {@link IIncludePathEntry#CPE_VARIABLE} support Javadoc locations.
//   *
//   * @param entry the classpath entry to get the Javadoc location for
//   * @return the Javadoc location or<code>null</code> if no Javadoc location is
//   *         available
//   * @throws IllegalArgumentException Thrown when the entry is <code>null</code>
//   *           or not of kind {@link IIncludePathEntry#CPE_LIBRARY} or
//   *           {@link IIncludePathEntry#CPE_VARIABLE}.
//   */
//  public static URL getLibraryJSdocLocation(IIncludePathEntry entry) {
//    return JavaDocLocations.getLibraryJavadocLocation(entry);
//  }
//
//  /**
//   * Returns the Javadoc location for a JavaScript project or <code>null</code>
//   * if no location is available. This location is used for all types located in
//   * the project's source folders.
//   *
//   * @param project the project
//   * @return the Javadoc location for a JavaScript project or <code>null</code>
//   */
//  public static URL getProjectJSdocLocation(MadlProject project) {
//    return JavaDocLocations.getProjectJavadocLocation(project);
//  }
//
//  /**
//   * Sets the Javadoc location for a JavaScript project. This location is used
//   * for all types located in the project's source folders.
//   *
//   * @param project the project
//   * @param url the Javadoc location to set. This location should contain
//   *          index.html and a file 'package-list'. <code>null</code> clears the
//   *          current documentation location.
//   */
//  public static void setProjectJSdocLocation(MadlProject project, URL url) {
//    JavaDocLocations.setProjectJavadocLocation(project, url);
//  }

  public static Color getViewerSelectionBackground(IPreferenceStore prefs, Display display) {
    // TODO(messick) Use a color identifier distinct from the editor.
    return prefs.getBoolean(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SELECTION_BACKGROUND_DEFAULT_COLOR)
        ? null : createColor(
            prefs,
            AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SELECTION_BACKGROUND_COLOR,
            display);
  }

  public static Color getViewerSelectionForeground(IPreferenceStore prefs, Display display) {
    // TODO(messick) Use a color identifier distinct from the editor.
    return prefs.getBoolean(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SELECTION_FOREGROUND_DEFAULT_COLOR)
        ? null : createColor(
            prefs,
            AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SELECTION_FOREGROUND_COLOR,
            display);
  }

  /**
   * Returns the working copy manager for the Madl UI plug-in.
   * 
   * @return the working copy manager for the Madl UI plug-in
   */
  public static IWorkingCopyManager getWorkingCopyManager() {
    return MadlToolsPlugin.getDefault().getWorkingCopyManager();
  }

  /**
   * Opens an editor with {@link Element} in context of the given {@link MadlEditor}.
   * 
   * @param contextEditor the {@link MadlEditor} to use as context, may be {@code null}.
   * @param element the {@link Element} to open in reveal.
   * @return the opened editor or {@code null} if by some reason editor was not opened.
   */
  public static IEditorPart openInEditor(MadlEditor contextEditor, Element element, boolean activate)
      throws PartInitException, MadlModelException {
    IFile contextFile = contextEditor != null ? contextEditor.getInputResourceFile() : null;
    return openInEditor(contextFile, element, activate);
  }

  /**
   * Opens an editor on the given Madl element in the active page. Valid elements are all Madl
   * elements that are {@link SourceReference}. For elements inside a compilation unit, the parent
   * is opened in the editor is opened and the element revealed. If there already is an open Madl
   * editor for the given element, it is returned.
   * 
   * @param element the input element; either a compilation unit ( <code>CompilationUnit</code>) or
   *          a class file ( <code>IClassFile</code>) or source references inside.
   * @return returns the editor part of the opened editor or <code>null</code> if the element is not
   *         a {@link SourceReference} or the file was opened in an external editor.
   * @exception PartInitException if the editor could not be initialized or no workbench page is
   *              active
   * @exception MadlModelException if this element does not exist or if an exception occurs while
   *              accessing its underlying resource
   */
  public static IEditorPart openInEditor(MadlElement element) throws MadlModelException,
      PartInitException {
    return openInEditor(element, true, true);
  }

  /**
   * Opens an editor on the given Madl element in the active page. Valid elements are all Madl
   * elements that are {@link SourceReference}. For elements inside a compilation unit, the parent
   * is opened in the editor is opened. If there already is an open Madl editor for the given
   * element, it is returned.
   * 
   * @param element the input element; either a compilation unit ( <code>CompilationUnit</code>) or
   *          a class file ( <code>IClassFile</code>) or source references inside.
   * @param activate if set, the editor will be activated.
   * @param reveal if set, the element will be revealed.
   * @return returns the editor part of the opened editor or <code>null</code> if the element is not
   *         a {@link SourceReference} or the file was opened in an external editor.
   * @exception PartInitException if the editor could not be initialized or no workbench page is
   *              active
   * @exception MadlModelException if this element does not exist or if an exception occurs while
   *              accessing its underlying resource
   */
  public static IEditorPart openInEditor(MadlElement element, boolean activate, boolean reveal)
      throws MadlModelException, PartInitException {
	  //ss
/*    if (!(element instanceof SourceReference)) {
      return null;
    }
    IEditorPart part = EditorUtility.openInEditor(element, activate);
    if (reveal && part != null) {
      EditorUtility.revealInEditor(part, element);
    }
    return part;*/
	  return null;
  }

  /**
   * Opens an editor on the given Madl element in the active page. Valid elements are all Madl
   * elements that are {@link SourceReference}. For elements inside a compilation unit, the parent
   * is opened in the editor is opened and the element revealed. If there already is an open Madl
   * editor for the given element, it is returned.
   * 
   * @param element the input element; either a compilation unit ( <code>CompilationUnit</code>) or
   *          a class file ( <code>IClassFile</code>) or source references inside.
   * @return returns the editor part of the opened editor or <code>null</code> if the element is not
   *         a {@link SourceReference} or the file was opened in an external editor.
   * @exception PartInitException if the editor could not be initialized or no workbench page is
   *              active
   * @exception MadlModelException if this element does not exist or if an exception occurs while
   *              accessing its underlying resource
   */
  public static IEditorPart openInEditor(Element element) throws MadlModelException,
      PartInitException {
	  //ss
   /* element = MadlElementUtil.getVariableIfSyntheticAccessor(element);
    return openInEditor(element, true, true);*/
	  return null;
  }

  /**
   * Opens an editor on the given Madl element in the active page. Valid elements are all Madl
   * elements that are {@link SourceReference}. For elements inside a compilation unit, the parent
   * is opened in the editor is opened. If there already is an open Madl editor for the given
   * element, it is returned.
   * 
   * @param element the input element; either a compilation unit ( <code>CompilationUnit</code>) or
   *          a class file ( <code>IClassFile</code>) or source references inside.
   * @param activate if set, the editor will be activated.
   * @param reveal if set, the element will be revealed.
   * @return returns the editor part of the opened editor or <code>null</code> if the element is not
   *         a {@link SourceReference} or the file was opened in an external editor.
   * @exception PartInitException if the editor could not be initialized or no workbench page is
   *              active
   * @exception MadlModelException if this element does not exist or if an exception occurs while
   *              accessing its underlying resource
   */
  public static IEditorPart openInEditor(Element element, boolean activate, boolean reveal)
      throws MadlModelException, PartInitException {
    // prepare resource to open
    IFile elementFile = getElementFile(element);
    // open editor
    IEditorPart editor= null;
    //ss
  /*  if (elementFile != null) {
      editor = EditorUtility.openInEditor(elementFile, activate);
    } else {
      editor = EditorUtility.openInEditor(element.getSource(), activate);
    }
    if (editor == null) {
      return null;
    }
    if (reveal && editor != null) {
      EditorUtility.revealInEditor(editor, element);
    }*/
    return editor;
  }

  /**
   * Opens an editor with {@link Element} in context of the given {@link IFile}.
   * 
   * @param context the {@link IResource} to open {@link Element} in, may be {@code null}.
   * @param element the {@link Element} to open in reveal.
   * @return the opened editor or {@code null} if by some reason editor was not opened.
   */
  public static IEditorPart openInEditor(IResource context, Element element, boolean activate)
      throws PartInitException, MadlModelException {
    if (element == null) {
      return null;
    }
    // open editor
    IEditorPart part= null;
 /*   {
      ProjectManager projectManager = MadlCore.getProjectManager();
      Source source = element.getSource();
      IFile file;
      if (context == null) {
        file = (IFile) projectManager.getResource(source);
      } else {
        ResourceMap map = projectManager.getResourceMap(context);
        file = map != null ? map.getResource(source) : null;
      }
      if (file != null) {
        part = EditorUtility.openInEditor(file, activate);
      } else {
        part = EditorUtility.openInEditor(source, activate);
      }
    }
    // reveal Element
    if (part != null) {
      EditorUtility.revealInEditor(part, element);
    }*/
    // done
    return part;
  }

  /**
   * Reveals the given Madl element in the given editor. If the element is not an instance of
   * <code>SourceReference</code> this method result in a NOP. If it is a source reference no
   * checking is done if the editor displays a compilation unit or class file that contains the
   * source reference element. The editor simply reveals the source range denoted by the given
   * element.
   * 
   * @param part the editor displaying a compilation unit or class file
   * @param element the element to be revealed
   */
  public static void revealInEditor(IEditorPart part, MadlElement element) {
	  //ss
    //EditorUtility.revealInEditor(part, element);
  }

  private static Color createColor(IPreferenceStore store, String key, Display display) {
    // TODO(messick) Remove display or use it instead of color manager
    RGB rgb = null;
    if (store.contains(key)) {
      if (store.isDefault(key)) {
        rgb = PreferenceConverter.getDefaultColor(store, key);
      } else {
        rgb = PreferenceConverter.getColor(store, key);
      }
      if (rgb != null) {
        return getColorManager().getColor(rgb);
      }
    }
    return null;
  }

  private MadlUI() {
    // prevent instantiation of MadlUI.
  }

}
