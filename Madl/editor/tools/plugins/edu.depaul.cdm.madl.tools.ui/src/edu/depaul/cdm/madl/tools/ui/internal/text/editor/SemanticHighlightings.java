/*
 * Copyright (c) 2013, the Madl project authors.
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
package edu.depaul.cdm.madl.tools.ui.internal.text.editor;

import com.google.common.collect.Lists;
import edu.depaul.cdm.madl.engine.ast.ASTNode;

//import edu.depaul.cdm.madl.engine.ast.Annotation;
//import edu.depaul.cdm.madl.engine.ast.AsExpression;
//import edu.depaul.cdm.madl.engine.ast.CatchClause;
//import edu.depaul.cdm.madl.engine.ast.ClassDeclaration;
//import edu.depaul.cdm.madl.engine.ast.Combinator;
//import edu.depaul.cdm.madl.engine.ast.ConstructorDeclaration;
import edu.depaul.cdm.madl.engine.ast.DoubleLiteral;
//import edu.depaul.cdm.madl.engine.ast.ExportDirective;
//import edu.depaul.cdm.madl.engine.ast.FieldDeclaration;
//import edu.depaul.cdm.madl.engine.ast.FunctionDeclaration;
//import edu.depaul.cdm.madl.engine.ast.ImplementsClause;
//import edu.depaul.cdm.madl.engine.ast.ImportDirective;
import edu.depaul.cdm.madl.engine.ast.IntegerLiteral;
//import edu.depaul.cdm.madl.engine.ast.LibraryDirective;
//import edu.depaul.cdm.madl.engine.ast.MethodDeclaration;
//import edu.depaul.cdm.madl.engine.ast.NativeClause;
//import edu.depaul.cdm.madl.engine.ast.NativeFunctionBody;
//import edu.depaul.cdm.madl.engine.ast.PartDirective;
//import edu.depaul.cdm.madl.engine.ast.PartOfDirective;
//import edu.depaul.cdm.madl.engine.ast.PrefixedIdentifier;
import edu.depaul.cdm.madl.engine.ast.SimpleIdentifier;
//import edu.depaul.cdm.madl.engine.ast.StringLiteral;
//import edu.depaul.cdm.madl.engine.ast.TryStatement;
//import edu.depaul.cdm.madl.engine.ast.TypeAlias;
//import edu.depaul.cdm.madl.engine.ast.TypeName;
//import edu.depaul.cdm.madl.engine.ast.VariableDeclaration;
//import edu.depaul.cdm.madl.engine.ast.VariableDeclarationList;
//import edu.depaul.cdm.madl.engine.ast.VariableDeclarationStatement;

import edu.depaul.cdm.madl.engine.element.ClassElement;

//import edu.depaul.cdm.madl.engine.element.ConstructorElement;

import edu.depaul.cdm.madl.engine.element.Element;
import edu.depaul.cdm.madl.engine.element.ElementAnnotation;
import edu.depaul.cdm.madl.engine.element.ElementKind;

//import edu.depaul.cdm.madl.engine.element.FunctionElement;
//import edu.depaul.cdm.madl.engine.element.ImportElement;

import edu.depaul.cdm.madl.engine.element.LibraryElement;

//import edu.depaul.cdm.madl.engine.element.MethodElement;
//import edu.depaul.cdm.madl.engine.element.PrefixElement;
//import edu.depaul.cdm.madl.engine.element.PropertyAccessorElement;
//import edu.depaul.cdm.madl.engine.element.PropertyInducingElement;
//import edu.depaul.cdm.madl.engine.element.TypeParameterElement;
import edu.depaul.cdm.madl.engine.element.VariableElement;

import edu.depaul.cdm.madl.engine.scanner.Token;
import edu.depaul.cdm.madl.engine.type.Type;
import edu.depaul.cdm.madl.engine.utilities.source.SourceRange;
import edu.depaul.cdm.madl.tools.ui.MadlToolsPlugin;
import edu.depaul.cdm.madl.tools.ui.PreferenceConstants;
import edu.depaul.cdm.madl.tools.ui.text.IMadlColorConstants;

import static edu.depaul.cdm.madl.engine.utilities.source.SourceRangeFactory.rangeStartEnd;
import static edu.depaul.cdm.madl.engine.utilities.source.SourceRangeFactory.rangeToken;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.RGB;

import java.util.List;

/**
 * Semantic highlightings.
 * 
 * @coverage madl.editor.ui.text.highlighting
 */
public class SemanticHighlightings {

  /**
   * Abstract {@link SemanticHighlighting} with empty methods by default.
   */
  private static abstract class DefaultSemanticHighlighting extends SemanticHighlighting {
    @Override
    public RGB getDefaultDefaultTextColor() {
      return new RGB(0, 0, 0);
    }

    @Override
    public boolean isBoldByDefault() {
      return false;
    }

    @Override
    public boolean isItalicByDefault() {
      return false;
    }

    @Override
    public boolean isStrikethroughByDefault() {
      return false;
    }

    @Override
    public boolean isUnderlineByDefault() {
      return false;
    }

    protected List<SourceRange> addPosition(List<SourceRange> positions, SourceRange range) {
      if (positions == null) {
        positions = Lists.newArrayList();
      }
      positions.add(range);
      return positions;
    }

    protected List<SourceRange> addPosition(List<SourceRange> positions, Token token) {
      return addPosition(positions, rangeToken(token));
    }

    RGB defaultFieldColor() {
      return new RGB(0, 0, 192);
    }
  }

  /**
   * Semantic highlighting for variables with dynamic types.
   */
  private static final class DynamicTypeHighlighting extends DefaultSemanticHighlighting {
    @Override
    public boolean consumesIdentifier(SemanticToken token) {
      SimpleIdentifier node = token.getNodeIdentifier();
      // should be variable
      Element element = node.getStaticElement();
      if (!(element instanceof VariableElement)) {
        return false;
      }
      // may be has propagated type
   /*   Type propagatedType = node.getPropagatedType();
      if (propagatedType != null && !propagatedType.isDynamic()) {
        return false;
      }*/
      // has dynamic static type
   /*   Type staticType = node.getStaticType();
      return staticType != null && staticType.isDynamic();*/
      return true;
    }

    @Override
    public RGB getDefaultDefaultTextColor() {
      return new RGB(0x80, 0x00, 0xCC);
    }

    @Override
    public String getDisplayName() {
      return MadlEditorMessages.SemanticHighlighting_dynamicType;
    }

    @Override
    public String getPreferenceKey() {
      return DYNAMIC_TYPE;
    }

    @Override
    public boolean isEnabledByDefault() {
      return true;
    }
  }

  private static class LocalVariableDeclarationHighlighting extends DefaultSemanticHighlighting {
    @Override
    public boolean consumesIdentifier(SemanticToken token) {
    	//TODO
    	//ss
    /*  SimpleIdentifier node = token.getNodeIdentifier();
      return node.getParent() instanceof VariableDeclaration
          && node.getParent().getParent() instanceof VariableDeclarationList
          && node.getParent().getParent().getParent() instanceof VariableDeclarationStatement;*/
    	return false;
    }

    @Override
    public RGB getDefaultDefaultTextColor() {
      return new RGB(0, 0, 0); // same as parameter
    }

    @Override
    public String getDisplayName() {
      return MadlEditorMessages.SemanticHighlighting_localVariableDeclaration;
    }

    @Override
    public String getPreferenceKey() {
      return LOCAL_VARIABLE_DECLARATION;
    }

    @Override
    public boolean isEnabledByDefault() {
      return true;
    }
  }

  private static class LocalVariableHighlighting extends DefaultSemanticHighlighting {
    @Override
    public boolean consumesIdentifier(SemanticToken token) {
      SimpleIdentifier node = token.getNodeIdentifier();
      Element element = node.getStaticElement();
      return ElementKind.of(element) == ElementKind.LOCAL_VARIABLE;
    }

    @Override
    public RGB getDefaultDefaultTextColor() {
      return new RGB(0, 0, 0); // same as parameter
    }

    @Override
    public String getDisplayName() {
      return MadlEditorMessages.SemanticHighlighting_localVariable;
    }

    @Override
    public String getPreferenceKey() {
      return LOCAL_VARIABLE;
    }

    @Override
    public boolean isEnabledByDefault() {
      return true;
    }
  }

  private static class NumberHighlighting extends DefaultSemanticHighlighting {
    @Override
    public boolean consumes(SemanticToken token) {
      ASTNode node = token.getNode();
      return node instanceof IntegerLiteral || node instanceof DoubleLiteral;
    }

    @Override
    public RGB getDefaultDefaultTextColor() {
      return new RGB(0x00, 0x70, 0x00);
    }

    @Override
    public String getDisplayName() {
      return MadlEditorMessages.SemanticHighlighting_number;
    }

    @Override
    public String getPreferenceKey() {
      return NUMBER;
    }

    @Override
    public boolean isEnabledByDefault() {
      return true;
    }
  }


private static class TypeVariableHighlighting extends DefaultSemanticHighlighting {
    @Override
    public boolean consumesIdentifier(SemanticToken token) {
    	//TODO
      SimpleIdentifier node = token.getNodeIdentifier();
     // return node.getStaticElement() instanceof TypeParameterElement;
      return false;
    }

    @Override
    public String getDisplayName() {
      return MadlEditorMessages.SemanticHighlighting_typeVariable;
    }

    @Override
    public String getPreferenceKey() {
      return TYPE_VARIABLE;
    }

    @Override
    public boolean isEnabledByDefault() {
      return true;
    }
  }

  private static final RGB KEY_WORD_COLOR = PreferenceConverter.getColor(
      MadlToolsPlugin.getDefault().getPreferenceStore(),
      IMadlColorConstants.JAVA_KEYWORD);

  /**
   * A named preference part that controls the highlighting of deprecated elements.
   */
  public static final String DEPRECATED_ELEMENT = "deprecated"; //$NON-NLS-1$

  /**
   * A named preference part that controls the highlighting of static final fields.
   */
  public static final String STATIC_FINAL_FIELD = "staticFinalField"; //$NON-NLS-1$

  /**
   * A named preference part that controls the highlighting of static fields.
   */
  public static final String STATIC_FIELD = "staticField"; //$NON-NLS-1$

  /**
   * A named preference part that controls the highlighting of top level members.
   */
  public static final String TOP_LEVEL_MEMBER = "topLevelMember"; //$NON-NLS-1$

  /**
   * A named preference part that controls the highlighting of built-in identifiers.
   */
  public static final String BUILT_IN = "builtin"; //$NON-NLS-1$

  /**
   * A named preference part that controls the highlighting of fields.
   */
  public static final String FIELD = "field"; //$NON-NLS-1$

  /**
   * A named preference part that controls the highlighting of method declarations.
   */
  public static final String METHOD_DECLARATION = "methodDeclarationName"; //$NON-NLS-1$

  /**
   * A named preference part that controls the highlighting of static method declarations.
   */
  public static final String STATIC_METHOD_DECLARATION = "staticMethodDeclarationName"; //$NON-NLS-1$

  /**
   * A named preference part that controls the highlighting of static method invocations.
   */
  public static final String STATIC_METHOD_INVOCATION = "staticMethodInvocation"; //$NON-NLS-1$

  /**
   * A named preference part that controls the highlighting of inherited method invocations.
   */
  public static final String INHERITED_METHOD_INVOCATION = "inheritedMethodInvocation"; //$NON-NLS-1$

  /**
   * A named preference part that controls the highlighting of abstract method invocations.
   */
  public static final String ABSTRACT_METHOD_INVOCATION = "abstractMethodInvocation"; //$NON-NLS-1$

  /**
   * A named preference part that controls the highlighting of local variables.
   */
  public static final String LOCAL_VARIABLE_DECLARATION = "localVariableDeclaration"; //$NON-NLS-1$

  /**
   * A named preference part that controls the highlighting of getters.
   */
  public static final String GETTER_DECLARATION = "getterDeclaration"; //$NON-NLS-1$

  /**
   * A named preference part that controls the highlighting of setters.
   */
  public static final String SETTER_DECLARATION = "setterDeclaration"; //$NON-NLS-1$

  /**
   * A named preference part that controls the highlighting of local variables.
   */
  public static final String LOCAL_VARIABLE = "localVariable"; //$NON-NLS-1$

  /**
   * A named preference part that controls the highlighting of parameter variables.
   */
  public static final String PARAMETER_VARIABLE = "parameterVariable"; //$NON-NLS-1$

  /**
   * A named preference part that controls the highlighting of dynamic types.
   */
  public static final String DYNAMIC_TYPE = "dynamicType"; //$NON-NLS-1$

  /**
   * A named preference part that controls the highlighting of type parameters.
   */
  public static final String TYPE_VARIABLE = "typeParameter"; //$NON-NLS-1$

  /**
   * A named preference part that controls the highlighting of type parameters.
   * 
   * @author STP
   */
  public static final String OBJECT_INITIALIZER = "objectInitializer"; //$NON-NLS-1$

  /**
   * A named preference part that controls the highlighting of methods (invocations and references).
   */
  public static final String METHOD = "method"; //$NON-NLS-1$

  /**
   * A named preference part that controls the highlighting of functions (invocations and
   * references).
   */
  public static final String FUNCTION = "function"; //$NON-NLS-1$

  /**
   * A named preference part that controls the highlighting of static methods (invocations and
   * declarations).
   */
  public static final String STATIC_METHOD = "staticMethod"; //$NON-NLS-1$

  /**
   * A named preference part that controls the highlighting of classes.
   */
  public static final String CLASS = "class"; //$NON-NLS-1$

  /**
   * A named preference part that controls the highlighting of import prefix.
   */
  public static final String IMPORT_PREFIX = "importPrefix"; //$NON-NLS-1$

  /**
   * A named preference part that controls the highlighting of interfaces.
   */
  public static final String INTERFACE = "interface"; //$NON-NLS-1$

  /**
   * A named preference part that controls the highlighting of annotations.
   */
  public static final String ANNOTATION = "annotation"; //$NON-NLS-1$

  /**
   * A named preference part that controls the highlighting of type arguments.
   */
  public static final String TYPE_ARGUMENT = "typeArgument"; //$NON-NLS-1$

  /**
   * A named preference part that controls the highlighting of numbers.
   */
  public static final String NUMBER = "number"; //$NON-NLS-1$

  /**
   * Semantic highlightings
   */
  private static SemanticHighlighting[] SEMANTIC_HIGHTLIGHTINGS;

  /**
   * Tests whether <code>event</code> in <code>store</code> affects the enablement of semantic
   * highlighting.
   * 
   * @param store the preference store where <code>event</code> was observed
   * @param event the property change under examination
   * @return <code>true</code> if <code>event</code> changed semantic highlighting enablement,
   *         <code>false</code> if it did not
   */
  public static boolean affectsEnablement(IPreferenceStore store, PropertyChangeEvent event) {
    return false;
//    String relevantKey = null;
//    SemanticHighlighting[] highlightings = getSemanticHighlightings();
//    for (int i = 0; i < highlightings.length; i++) {
//      if (event.getProperty().equals(getEnabledPreferenceKey(highlightings[i]))) {
//        relevantKey = event.getProperty();
//        break;
//      }
//    }
//    if (relevantKey == null) {
//      return false;
//    }
//
//    for (int i = 0; i < highlightings.length; i++) {
//      String key = getEnabledPreferenceKey(highlightings[i]);
//      if (key.equals(relevantKey)) {
//        continue;
//      }
//      if (store.getBoolean(key)) {
//        return false; // another is still enabled or was enabled before
//      }
//    }
//
//    // all others are disabled, so toggling relevantKey affects the enablement
//    return true;
  }

  /**
   * A named preference that controls if the given semantic highlighting has the text attribute
   * bold.
   * 
   * @param semanticHighlighting the semantic highlighting
   * @return the bold preference key
   */
  public static String getBoldPreferenceKey(SemanticHighlighting semanticHighlighting) {
    return PreferenceConstants.EDITOR_SEMANTIC_HIGHLIGHTING_PREFIX
        + semanticHighlighting.getPreferenceKey()
        + PreferenceConstants.EDITOR_SEMANTIC_HIGHLIGHTING_BOLD_SUFFIX;
  }

  /**
   * A named preference that controls the given semantic highlighting's color.
   * 
   * @param semanticHighlighting the semantic highlighting
   * @return the color preference key
   */
  public static String getColorPreferenceKey(SemanticHighlighting semanticHighlighting) {
    return PreferenceConstants.EDITOR_SEMANTIC_HIGHLIGHTING_PREFIX
        + semanticHighlighting.getPreferenceKey()
        + PreferenceConstants.EDITOR_SEMANTIC_HIGHLIGHTING_COLOR_SUFFIX;
  }

  /**
   * A named preference that controls if the given semantic highlighting is enabled.
   * 
   * @param semanticHighlighting the semantic highlighting
   * @return the enabled preference key
   */
  public static String getEnabledPreferenceKey(SemanticHighlighting semanticHighlighting) {
    return PreferenceConstants.EDITOR_SEMANTIC_HIGHLIGHTING_PREFIX
        + semanticHighlighting.getPreferenceKey()
        + PreferenceConstants.EDITOR_SEMANTIC_HIGHLIGHTING_ENABLED_SUFFIX;
  }

  /**
   * A named preference that controls if the given semantic highlighting has the text attribute
   * italic.
   * 
   * @param semanticHighlighting the semantic highlighting
   * @return the italic preference key
   */
  public static String getItalicPreferenceKey(SemanticHighlighting semanticHighlighting) {
    return PreferenceConstants.EDITOR_SEMANTIC_HIGHLIGHTING_PREFIX
        + semanticHighlighting.getPreferenceKey()
        + PreferenceConstants.EDITOR_SEMANTIC_HIGHLIGHTING_ITALIC_SUFFIX;
  }

  /**
   * @return The semantic highlightings, the order defines the precedence of matches, the first
   *         match wins.
   */
  public static SemanticHighlighting[] getSemanticHighlightings() {
    if (SEMANTIC_HIGHTLIGHTINGS == null) {
    	//ss
    /*  SEMANTIC_HIGHTLIGHTINGS = new SemanticHighlighting[] {
          new DirectiveHighlighting(), new BuiltInHighlighting(),
          new DeprecatedElementHighlighting(), new GetterDeclarationHighlighting(),
          new SetterDeclarationHighlighting(), new AnnotationHighlighting(),
          new StaticFieldHighlighting(), new FieldHighlighting(), new DynamicTypeHighlighting(),
          new ClassHighlighting(), new TypeVariableHighlighting(), new NumberHighlighting(),
          new LocalVariableDeclarationHighlighting(), new LocalVariableHighlighting(),
          new ParameterHighlighting(), new StaticMethodDeclarationHighlighting(),
          new StaticMethodHighlighting(), new ConstructorHighlighting(),
          new MethodDeclarationHighlighting(), new MethodHighlighting(),
          new FunctionHighlighting(), new ImportPrefixHighlighting()};*/
    	SEMANTIC_HIGHTLIGHTINGS = new SemanticHighlighting[] {new NumberHighlighting()};
    }
    return SEMANTIC_HIGHTLIGHTINGS;
  }

  /**
   * A named preference that controls if the given semantic highlighting has the text attribute
   * strikethrough.
   * 
   * @param semanticHighlighting the semantic highlighting
   * @return the strikethrough preference key
   */
  public static String getStrikethroughPreferenceKey(SemanticHighlighting semanticHighlighting) {
    return PreferenceConstants.EDITOR_SEMANTIC_HIGHLIGHTING_PREFIX
        + semanticHighlighting.getPreferenceKey()
        + PreferenceConstants.EDITOR_SEMANTIC_HIGHLIGHTING_STRIKETHROUGH_SUFFIX;
  }

  /**
   * A named preference that controls if the given semantic highlighting has the text attribute
   * underline.
   * 
   * @param semanticHighlighting the semantic highlighting
   * @return the underline preference key
   */
  public static String getUnderlinePreferenceKey(SemanticHighlighting semanticHighlighting) {
    return PreferenceConstants.EDITOR_SEMANTIC_HIGHLIGHTING_PREFIX
        + semanticHighlighting.getPreferenceKey()
        + PreferenceConstants.EDITOR_SEMANTIC_HIGHLIGHTING_UNDERLINE_SUFFIX;
  }

  /**
   * Initialize default preferences in the given preference store.
   * 
   * @param store The preference store
   */
  public static void initDefaults(IPreferenceStore store) {
    SemanticHighlighting[] semanticHighlightings = getSemanticHighlightings();
    for (SemanticHighlighting highlighting : semanticHighlightings) {
      setDefaultAndFireEvent(
          store,
          getColorPreferenceKey(highlighting),
          highlighting.getDefaultTextColor());
      store.setDefault(getBoldPreferenceKey(highlighting), highlighting.isBoldByDefault());
      store.setDefault(getItalicPreferenceKey(highlighting), highlighting.isItalicByDefault());
      store.setDefault(
          getStrikethroughPreferenceKey(highlighting),
          highlighting.isStrikethroughByDefault());
      store.setDefault(getUnderlinePreferenceKey(highlighting), highlighting.isUnderlineByDefault());
      store.setDefault(getEnabledPreferenceKey(highlighting), highlighting.isEnabledByDefault());
    }
  }

  /**
   * Tests whether semantic highlighting is currently enabled.
   * 
   * @param store the preference store to consult
   * @return <code>true</code> if semantic highlighting is enabled, <code>false</code> if it is not
   */
  public static boolean isEnabled(IPreferenceStore store) {
    SemanticHighlighting[] highlightings = getSemanticHighlightings();
    for (SemanticHighlighting highlighting : highlightings) {
      String enabledKey = getEnabledPreferenceKey(highlighting);
      if (store.getBoolean(enabledKey)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Sets the default value and fires a property change event if necessary.
   * 
   * @param store the preference store
   * @param key the preference key
   * @param newValue the new value
   */
  private static void setDefaultAndFireEvent(IPreferenceStore store, String key, RGB newValue) {
    RGB oldValue = null;
    if (store.isDefault(key)) {
      oldValue = PreferenceConverter.getDefaultColor(store, key);
    }

    if (newValue != null) {
      PreferenceConverter.setDefault(store, key, newValue);

      if (oldValue != null && !oldValue.equals(newValue)) {
        store.firePropertyChangeEvent(key, oldValue, newValue);
      }
    }
  }

  /**
   * Do not instantiate
   */
  private SemanticHighlightings() {
  }

  public RGB getDefaultDefaultTextColor() {
    return new RGB(13, 100, 0);
  }
}
