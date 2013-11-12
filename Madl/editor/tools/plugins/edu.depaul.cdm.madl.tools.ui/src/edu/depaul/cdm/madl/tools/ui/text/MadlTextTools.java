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
package edu.depaul.cdm.madl.tools.ui.text;

import edu.depaul.cdm.madl.tools.ui.internal.text.functions.FastMadlPartitionScanner;
import edu.depaul.cdm.madl.tools.ui.internal.text.functions.MadlColorManager;
import edu.depaul.cdm.madl.tools.ui.internal.text.functions.MadlCommentScanner;
import edu.depaul.cdm.madl.tools.ui.internal.text.functions.MadlMultilineStringScanner;
import edu.depaul.cdm.madl.tools.ui.internal.text.functions.SingleTokenMadlScanner;
import edu.depaul.cdm.madl.tools.ui.internal.text.madl.MadlCodeScanner;
import edu.depaul.cdm.madl.tools.ui.internal.text.madldoc.MadlDocScanner;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

/**
 * Tools required to configure a Madl text viewer. The color manager and all scanners are
 * singletons.
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 * Provisional API: This class/interface is part of an interim API that is still under development
 * and expected to change significantly before reaching stability. It is being made available at
 * this early stage to solicit feedback from pioneering adopters on the understanding that any code
 * that uses this API will almost certainly be broken (repeatedly) as the API evolves.
 */
@SuppressWarnings("deprecation")
public class MadlTextTools {

  /**
   * This tools' preference listener.
   */
  private class PreferenceListener implements IPropertyChangeListener,
      Preferences.IPropertyChangeListener {
    @Override
    public void propertyChange(Preferences.PropertyChangeEvent event) {
      adaptToPreferenceChange(new PropertyChangeEvent(
          event.getSource(),
          event.getProperty(),
          event.getOldValue(),
          event.getNewValue()));
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
      adaptToPreferenceChange(event);
    }
  }

  /**
   * Array with legal content types.
   */
  private final static String[] LEGAL_CONTENT_TYPES = new String[] {
      MadlPartitions.MADL_DOC, MadlPartitions.MADL_MULTI_LINE_COMMENT,
      MadlPartitions.MADL_SINGLE_LINE_COMMENT, MadlPartitions.MADL_SINGLE_LINE_DOC,
      MadlPartitions.MADL_STRING, MadlPartitions.MADL_MULTI_LINE_STRING};

  /** The color manager. */
  private MadlColorManager colorManager;
  /** The Madl source code scanner. */
  private MadlCodeScanner codeScanner;
  /** The Madl multi-line comment scanner. */
  private MadlCommentScanner multilineCommentScanner;
  /** The Madl single-line comment scanner. */
  private MadlCommentScanner singlelineCommentScanner;
  /** The Madl string scanner. */
  private SingleTokenMadlScanner stringScanner;
  /** The Madl multi-line scanner */
  private MadlMultilineStringScanner multilineStringScanner;
  /** The Doc scanner. */
  private MadlDocScanner dartDocScanner;
  /** The preference store. */
  private IPreferenceStore preferenceStore;
  /**
   * The core preference store.
   */
  private Preferences corePreferenceStore;
  /** The preference change listener */
  private PreferenceListener preferenceListener = new PreferenceListener();

  /**
   * Creates a new JavaScript text tools collection.
   * 
   * @param store the preference store to initialize the text tools. The text tool instance installs
   *          a listener on the passed preference store to adapt itself to changes in the preference
   *          store. In general <code>PreferenceConstants.
   * 			getPreferenceStore()</code> should be used to initialize the text tools.
   * @see edu.depaul.cdm.madl.editor.ui.google.dart.tools.ui.PreferenceConstants#getPreferenceStore()
   */
  public MadlTextTools(IPreferenceStore store) {
    this(store, null, true);
  }

  /**
   * Creates a new JavaScript text tools collection.
   * 
   * @param store the preference store to initialize the text tools. The text tool instance installs
   *          a listener on the passed preference store to adapt itself to changes in the preference
   *          store. In general <code>PreferenceConstants.
   * 			getPreferenceStore()</code> should be used to initialize the text tools.
   * @param autoDisposeOnDisplayDispose if <code>true</code> the color manager automatically
   *          disposes all managed colors when the current display gets disposed and all calls to
   *          {@link org.eclipse.jface.text.source.ISharedTextColors#dispose()} are ignored.
   * @see edu.depaul.cdm.madl.editor.ui.google.dart.tools.ui.PreferenceConstants#getPreferenceStore()
   */
  public MadlTextTools(IPreferenceStore store, boolean autoDisposeOnDisplayDispose) {
    this(store, null, autoDisposeOnDisplayDispose);
  }

  /**
   * Creates a new JavaScript text tools collection.
   * 
   * @param store the preference store to initialize the text tools. The text tool instance installs
   *          a listener on the passed preference store to adapt itself to changes in the preference
   *          store. In general <code>PreferenceConstants.
   * 			getPreferenceStore()</code> should be used to initialize the text tools.
   * @param coreStore optional preference store to initialize the text tools. The text tool instance
   *          installs a listener on the passed preference store to adapt itself to changes in the
   *          preference store.
   * @see edu.depaul.cdm.madl.editor.ui.google.dart.tools.ui.PreferenceConstants#getPreferenceStore()
   */
  public MadlTextTools(IPreferenceStore store, Preferences coreStore) {
    this(store, coreStore, true);
  }

  /**
   * Creates a new JavaScript text tools collection.
   * 
   * @param store the preference store to initialize the text tools. The text tool instance installs
   *          a listener on the passed preference store to adapt itself to changes in the preference
   *          store. In general <code>PreferenceConstants.
   * 			getPreferenceStore()</code> should be used to initialize the text tools.
   * @param coreStore optional preference store to initialize the text tools. The text tool instance
   *          installs a listener on the passed preference store to adapt itself to changes in the
   *          preference store.
   * @param autoDisposeOnDisplayDispose if <code>true</code> the color manager automatically
   *          disposes all managed colors when the current display gets disposed and all calls to
   *          {@link org.eclipse.jface.text.source.ISharedTextColors#dispose()} are ignored.
   * @see edu.depaul.cdm.madl.editor.ui.google.dart.tools.ui.PreferenceConstants#getPreferenceStore()
   */
  public MadlTextTools(IPreferenceStore store, Preferences coreStore,
      boolean autoDisposeOnDisplayDispose) {
    colorManager = new MadlColorManager(autoDisposeOnDisplayDispose);
    codeScanner = new MadlCodeScanner(colorManager, store);

    preferenceStore = store;
    preferenceStore.addPropertyChangeListener(preferenceListener);

    corePreferenceStore = coreStore;
    if (corePreferenceStore != null) {
      corePreferenceStore.addPropertyChangeListener(preferenceListener);
    }

    multilineCommentScanner = new MadlCommentScanner(
        colorManager,
        store,
        coreStore,
        IMadlColorConstants.JAVA_MULTI_LINE_COMMENT);
    singlelineCommentScanner = new MadlCommentScanner(
        colorManager,
        store,
        coreStore,
        IMadlColorConstants.JAVA_SINGLE_LINE_COMMENT);
    stringScanner = new SingleTokenMadlScanner(colorManager, store, IMadlColorConstants.JAVA_STRING);
    multilineStringScanner = new MadlMultilineStringScanner(
        colorManager,
        store,
        IMadlColorConstants.MADL_MULTI_LINE_STRING);
    dartDocScanner = new MadlDocScanner(colorManager, store, coreStore);
  }

  /**
   * Factory method for creating a Java-specific document partitioner using this object's partitions
   * scanner. This method is a convenience method.
   * 
   * @return a newly created JavaScript document partitioner
   */
  public IDocumentPartitioner createDocumentPartitioner() {
    return new FastPartitioner(getPartitionScanner(), LEGAL_CONTENT_TYPES);
  }

  /**
   * Disposes all the individual tools of this tools collection.
   */
  public void dispose() {

    codeScanner = null;
    multilineCommentScanner = null;
    singlelineCommentScanner = null;
    stringScanner = null;
    dartDocScanner = null;

    if (colorManager != null) {
      colorManager.dispose();
      colorManager = null;
    }

    if (preferenceStore != null) {
      preferenceStore.removePropertyChangeListener(preferenceListener);
      preferenceStore = null;

      if (corePreferenceStore != null) {
        corePreferenceStore.removePropertyChangeListener(preferenceListener);
        corePreferenceStore = null;
      }

      preferenceListener = null;
    }
  }

  /**
   * Returns the color manager which is used to manage any Java-specific colors needed for such
   * things like syntax highlighting.
   * <p>
   * Clients which are only interested in the color manager of the JavaScript UI plug-in should use
   * {@link com.google.dart.tools.ui.MadlUI#getColorManager()}.
   * </p>
   * 
   * @return the color manager to be used for JavaScript text viewers
   * @see com.google.dart.tools.ui.MadlUI#getColorManager()
   */
  public IColorManager getColorManager() {
    return colorManager;
  }

  /**
   * Returns a scanner which is configured to scan Java-specific partitions, which are multi-line
   * comments, Javadoc comments, and regular JavaScript source code.
   * 
   * @return a JavaScript partition scanner
   */
  public IPartitionTokenScanner getPartitionScanner() {
    return new FastMadlPartitionScanner();
  }

  /**
   * Sets up the JavaScript document partitioner for the given document for the default
   * partitioning.
   * 
   * @param document the document to be set up
   */
  public void setupJavaDocumentPartitioner(IDocument document) {
    setupMadlDocumentPartitioner(document, IDocumentExtension3.DEFAULT_PARTITIONING);
  }

  /**
   * Sets up the JavaScript document partitioner for the given document for the given partitioning.
   * 
   * @param document the document to be set up
   * @param partitioning the document partitioning
   */
  public void setupMadlDocumentPartitioner(IDocument document, String partitioning) {
    IDocumentPartitioner partitioner = createDocumentPartitioner();
    if (document instanceof IDocumentExtension3) {
      IDocumentExtension3 extension3 = (IDocumentExtension3) document;
      extension3.setDocumentPartitioner(partitioning, partitioner);
    } else {
      document.setDocumentPartitioner(partitioner);
    }
    partitioner.connect(document);
  }

  /**
   * Adapts the behavior of the contained components to the change encoded in the given event.
   * 
   * @param event the event to which to adapt
   * @deprecated As of 3.0, no replacement
   */
  @Deprecated
  protected void adaptToPreferenceChange(PropertyChangeEvent event) {
    if (codeScanner.affectsBehavior(event)) {
      codeScanner.adaptToPreferenceChange(event);
    }
    if (multilineCommentScanner.affectsBehavior(event)) {
      multilineCommentScanner.adaptToPreferenceChange(event);
    }
    if (singlelineCommentScanner.affectsBehavior(event)) {
      singlelineCommentScanner.adaptToPreferenceChange(event);
    }
    if (stringScanner.affectsBehavior(event)) {
      stringScanner.adaptToPreferenceChange(event);
    }
    if (multilineStringScanner.affectsBehavior(event)) {
      multilineStringScanner.adaptToPreferenceChange(event);
    }
    if (dartDocScanner.affectsBehavior(event)) {
      dartDocScanner.adaptToPreferenceChange(event);
    }
  }

  /**
   * Returns this text tool's core preference store.
   * 
   * @return the core preference store
   */
  protected Preferences getCorePreferenceStore() {
    return corePreferenceStore;
  }

  /**
   * Returns this text tool's preference store.
   * 
   * @return the preference store
   */
  protected IPreferenceStore getPreferenceStore() {
    return preferenceStore;
  }
}
