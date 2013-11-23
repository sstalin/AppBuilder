/*
 * Copyright (c) 2012, the Dart project authors.
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

public interface MadlPartitions {

  /**
   * The identifier of the Dart partitioning.
   */
  String MADL_PARTITIONING = "___madl_partitioning"; //$NON-NLS-1$

  /**
   * The identifier of the Dart single-line comment partition content type.
   */
  String MADL_SINGLE_LINE_COMMENT = "__madl_singleline_comment"; //$NON-NLS-1$

  /**
   * The identifier of the Dart multi-line comment partition content type.
   */
  String MADL_MULTI_LINE_COMMENT = "__madl_multiline_comment"; //$NON-NLS-1$

  /**
   * The identifier of the DartDoc comment partition content type.
   */
  String MADL_DOC = "__madl_doc"; //$NON-NLS-1$

  /**
   * The identifier of the single-line DartDoc comment partition content type.
   */
  String MADL_SINGLE_LINE_DOC = "__madl_singleline_doc"; //$NON-NLS-1$

  /**
   * The identifier of the Dart string partition content type.
   */
  String MADL_STRING = "__madl_string"; //$NON-NLS-1$

  /**
   * The identifier of the Dart multi-line string partition content type.
   */
  String MADL_MULTI_LINE_STRING = "__madl_multiline_string"; //$NON-NLS-1$

  /**
   * The identifier of the JavaScript character partition content type.
   * 
   * @deprecated
   */
  @Deprecated
  String JAVA_CHARACTER = "__java_character"; //$NON-NLS-1$
}
