package edu.depaul.cdm.madl.tools.ui.text;

import org.eclipse.swt.graphics.RGB;

public interface IColorManagerExtension {
	  /**
	   * Remembers the given color specification under the given key.
	   * 
	   * @param key the color key
	   * @param rgb the color specification
	   * @throws java.lang.UnsupportedOperationException if there is already a color specification
	   *           remembered under the given key
	   */
	  void bindColor(String key, RGB rgb);

	  /**
	   * Forgets the color specification remembered under the given key.
	   * 
	   * @param key the color key
	   */
	  void unbindColor(String key);
}
