package edu.depaul.cdm.madl.tools.ui.text;



public interface IMadlColorConstants {
/*	RGB MADL_COMMENT = new RGB(128, 0, 0);
	RGB STRING = new RGB(0, 128, 0);
	RGB DEFAULT = new RGB(0, 0, 0);
	RGB MADL_DOC = new RGB(128, 0, 0);
	RGB MADL_TOP_VIEW= new RGB(0, 255, 0);
	RGB MADL_CONTAINER= new RGB(0, 255, 0);
	RGB MADL_WIDGET= new RGB(200, 0, 0);
	RGB MADL_COMPOSITE= new RGB(0, 255, 0);
	RGB MADL_POPUP= new RGB(0, 255, 0);*/

	

	  /**
	   * Note: This constant is for internal use only. Clients should not use this constant. The prefix
	   * all color constants start with (value <code>"madl_"</code>).
	   */
	  String PREFIX = "madl_"; 

	  /**
	   * The color key for multi-line comments in Madl code (value
	   * <code>"madl_multi_line_comment"</code>).
	   */
	  String JAVA_MULTI_LINE_COMMENT = "madl_multi_line_comment"; 

	  /**
	   * The color key for single-line comments in Madl code (value
	   * <code>"madl_single_line_comment"</code>).
	   */
	  String JAVA_SINGLE_LINE_COMMENT = "madl_single_line_comment"; //$NON-NLS-1$

	  /**
	   * The color key for Madl keywords in Madl code (value <code>"madl_keyword"</code>).
	   */
	  String JAVA_KEYWORD = "madl_keyword"; //$NON-NLS-1$

	  /**
	   * The color key for strings in Madl code (value <code>"madl_string"</code>).
	   */
	  String JAVA_STRING = "madl_string"; //$NON-NLS-1$

	  /**
	   * The color key for strings in Madl code (value <code>"madl_string"</code>).
	   */
	  String MADL_MULTI_LINE_STRING = "madl_multiline_string"; //$NON-NLS-1$

	  /**
	   * The color key for raw strings in Madl code (value <code>"madl_raw_string"</code>).
	   */
	  String MADL_RAW_STRING = "madl_raw_string"; //$NON-NLS-1$

	  /**
	   * The color key for method names in JavaScript code (value <code>"java_method_name"</code>).
	   * 
	   * @deprecated replaced as of 3.1 by an equivalent semantic highlighting, see
	   *             {@link org.eclipse.wst.jsdt.internal.ui.javaeditor.SemanticHighlightings#METHOD}
	   */
	  @Deprecated
	  String JAVA_METHOD_NAME = "java_method_name"; //$NON-NLS-1$

	  /**
	   * The color key for keyword 'return' in Madl code (value <code>"madl_keyword_return"</code>).
	   */
	  String JAVA_KEYWORD_RETURN = "madl_keyword_return"; //$NON-NLS-1$

	  /**
	   * The color key for operators in Madl code (value <code>"madl_operator"</code>).
	   */
	  String JAVA_OPERATOR = "madl_operator"; //$NON-NLS-1$

	  /**
	   * The color key for brackets in Madl code (value <code>"madl_bracket"</code> ).
	   */
	  String JAVA_BRACKET = "madl_bracket"; //$NON-NLS-1$

	  /**
	   * The color key for everything in Madl code for which no other color is specified (value
	   * <code>"madl_default"</code>).
	   */
	  String JAVA_DEFAULT = "madl_default"; //$NON-NLS-1$

	  /**
	   * The color key for annotations (value <code>"java_annotation"</code>).
	   * 
	   * @deprecated replaced as of 3.2 by an equivalent semantic highlighting, see
	   *             {@link org.eclipse.wst.jsdt.internal.ui.javaeditor.SemanticHighlightings#ANNOTATION}
	   */
	  @Deprecated
	  String JAVA_ANNOTATION = "java_annotation"; //$NON-NLS-1$

	  /**
	   * The color key for task tags in Madl comments (value <code>"madl_comment_task_tag"</code>).
	   */
	  String TASK_TAG = "madl_comment_task_tag"; //$NON-NLS-1$

	  /**
	   * The color key for JavaDoc keywords (<code>@foo</code>) in JavaDoc comments (value
	   * <code>"madl_doc_keyword"</code>).
	   */
	  String JAVADOC_KEYWORD = "madl_doc_keyword"; //$NON-NLS-1$

	  /**
	   * The color key for HTML tags (<code>&lt;foo&gt;</code>) in JavaDoc comments (value
	   * <code>"java_doc_tag"</code>).
	   */
	  String JAVADOC_TAG = "madl_doc_tag"; //$NON-NLS-1$

	  /**
	   * The color key for JavaDoc links (<code>{foo}</code>) in JavaDoc comments (value
	   * <code>"java_doc_link"</code>).
	   */
	  String JAVADOC_LINK = "madl_doc_link"; //$NON-NLS-1$

	  /**
	   * The color key for everything in MadlDoc comments for which no other color is specified (value
	   * <code>"madl_doc_default"</code>).
	   */
	  String JAVADOC_DEFAULT = "madl_doc_default"; //$NON-NLS-1$

	  // ---------- Properties File Editor ----------

	  /**
	   * The color key for keys in a properties file (value <code>"pf_coloring_key"</code>).
	   */
	  String PROPERTIES_FILE_COLORING_KEY = "pf_coloring_key"; //$NON-NLS-1$

	  /**
	   * The color key for comments in a properties file (value <code>"pf_coloring_comment"</code>).
	   */

	  String PROPERTIES_FILE_COLORING_COMMENT = "pf_coloring_comment"; //$NON-NLS-1$

	  /**
	   * The color key for values in a properties file (value <code>"pf_coloring_value"</code>).
	   */
	  String PROPERTIES_FILE_COLORING_VALUE = "pf_coloring_value"; //$NON-NLS-1$

	  /**
	   * The color key for assignment in a properties file. (value <code>"pf_coloring_assignment"</code>
	   * ).
	   */
	  String PROPERTIES_FILE_COLORING_ASSIGNMENT = "pf_coloring_assignment"; //$NON-NLS-1$

	  /**
	   * The color key for arguments in values in a properties file. (value
	   * <code>"pf_coloring_argument"</code>).
	   */
	  String PROPERTIES_FILE_COLORING_ARGUMENT = "pf_coloring_argument"; //$NON-NLS-1$
	
}
