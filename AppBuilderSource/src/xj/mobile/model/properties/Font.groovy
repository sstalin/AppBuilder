
package xj.mobile.model.properties

import xj.mobile.model.PropertyModel

import xj.mobile.ios.IOSAppGenerator

class Font extends Property { 

  static IOSFontNames = [
	// Academy Engraved LET'
	'AcademyEngravedLetPlain',  // 5.0 4.3

	// American Typewriter
	'AmericanTypewriter',                 // 3.0 4.3
	'AmericanTypewriter-Bold',            // 3.0 4.3
	'AmericanTypewriter-Condensed',       // 5.0 5.0
	'AmericanTypewriter-CondensedBold',   // 5.0 5.0
	'AmericanTypewriter-CondensedLight',  // 5.0 5.0
	'AmericanTypewriter-Light',           // 5.0 5.0

	// Apple Color Emoji
	'AppleColorEmoji',          // 3.0 4.3

	// Apple SD Gothic Neo
	'AppleSDGothicNeo-Bold',    // 5.0 5.0
	'AppleSDGothicNeo-Medium',  // 4.3 4.3

	// Arial
	'ArialMT',             // 3.0 4.3
	'Arial-BoldItalicMT',  // 3.0 4.3
	'Arial-BoldMT',        // 3.0 4.3
	'Arial-ItalicMT',      // 3.0 4.3

	// Arial Hebrew
	'ArialHebrew',         // 3.0 4.3
	'ArialHebrew-Bold',    // 3.0 4.3

	// Arial Rounded MT Bold 
	'ArialRoundedMTBold',  // 3.0 4.3

	// Avenir 
	'Avenir-Black',          // 6.0 6.0
	'Avenir-BlackOblique',   // 6.0 6.0
	'Avenir-Book',           // 6.0 6.0
	'Avenir-BookOblique',    // 6.0 6.0
	'Avenir-Heavy',          // 6.0 6.0
	'Avenir-HeavyOblique',   // 6.0 6.0
	'Avenir-Light',          // 6.0 6.0
	'Avenir-LightOblique',   // 6.0 6.0
	'Avenir-Medium',         // 6.0 6.0
	'Avenir-MediumOblique',  // 6.0 6.0
	'Avenir-Oblique',        // 6.0 6.0
	'Avenir-Roman',          // 6.0 6.0

	// Avenir Next 
	'AvenirNext-Bold',              // 6.0 6.0
	'AvenirNext-BoldItalic',        // 6.0 6.0
	'AvenirNext-DemiBold',          // 6.0 6.0
	'AvenirNext-DemiBoldItalic',    // 6.0 6.0
	'AvenirNext-Heavy',             // 6.0 6.0
	'AvenirNext-HeavyItalic',       // 6.0 6.0
	'AvenirNext-Italic',            // 6.0 6.0
	'AvenirNext-Medium',            // 6.0 6.0
	'AvenirNext-MediumItalic',      // 6.0 6.0
	'AvenirNext-Regular',           // 6.0 6.0
	'AvenirNext-UltraLight',        // 6.0 6.0
	'AvenirNext-UltraLightItalic',  // 6.0 6.0

	// Avenir Next Condensed 
	'AvenirNextCondensed-Bold',              // 6.0 6.0
	'AvenirNextCondensed-BoldItalic',        // 6.0 6.0
	'AvenirNextCondensed-DemiBold',          // 6.0 6.0
	'AvenirNextCondensed-DemiBoldItalic',    // 6.0 6.0
	'AvenirNextCondensed-Heavy',             // 6.0 6.0
	'AvenirNextCondensed-HeavyItalic',       // 6.0 6.0
	'AvenirNextCondensed-Italic',            // 6.0 6.0
	'AvenirNextCondensed-Medium',            // 6.0 6.0
	'AvenirNextCondensed-MediumItalic',      // 6.0 6.0
	'AvenirNextCondensed-Regular',           // 6.0 6.0
	'AvenirNextCondensed-UltraLight',        // 6.0 6.0
	'AvenirNextCondensed-UltraLightItalic',  // 6.0 6.0

	// Bangla Sangam MN 
	'BanglaSangamMN',       // 3.0 4.3
	'BanglaSangamMN-Bold',  // 3.0 4.3

	// Baskerville 
	'Baskerville',                 // 3.0 4.3
	'Baskerville-Bold',            // 3.0 4.3
	'Baskerville-BoldItalic',      // 3.0 4.3
	'Baskerville-Italic',          // 3.0 4.3
	'Baskerville-SemiBold',        // 5.0 5.0
	'Baskerville-SemiBoldItalic',  // 5.0 5.0

	// Bodoni Ornaments 
	'BodoniOrnamentsITCTT',        // 5.0 4.3

	// Bodoni 72 
	'BodoniSvtyTwoITCTT-Bold',     // 5.0 4.3
	'BodoniSvtyTwoITCTT-Book',     // 5.0 4.3
	'BodoniSvtyTwoITCTT-BookIta',  // 5.0 4.3

	// Bodoni 72 Oldstyle 
	'BodoniSvtyTwoOSITCTT-Bold',    // 5.0 4.3
	'BodoniSvtyTwoOSITCTT-Book',    // 5.0 4.3
	'BodoniSvtyTwoOSITCTT-BookIt',  // 5.0 4.3
	'BodoniSvtyTwoSCITCTT-Book',    // 5.0 4.3

	// Bradley Hand 
	'BradleyHandITCTT-Bold',   // 6.0 4.3

	// Chalkboard SE 
	'ChalkboardSE-Bold',     // 3.0 4.3
	'ChalkboardSE-Light',    // 5.0 5.0
	'ChalkboardSE-Regular',  // 3.0 4.3

	// Chalkduster 
	'Chalkduster',    // 5.0 4.3

	// Cochin 
	'Cochin',             // 3.0 4.3
	'Cochin-Bold',        // 3.0 4.3
	'Cochin-BoldItalic',  // 3.0 4.3
	'Cochin-Italic',      // 3.0 4.3

	// Copperplate 
	'Copperplate',        // 5.0 4.3
	'Copperplate-Bold',   // 5.0 4.3
	'Copperplate-Light',  // 5.0 5.0

	// Courier 
	'Courier',              // 3.0 4.3
	'Courier-Bold',         // 3.0 4.3
	'Courier-BoldOblique',  // 3.0 4.3
	'Courier-Oblique',      // 3.0 4.3

	// Courier New 
	'CourierNewPS-BoldItalicMT',  // 3.0 4.3
	'CourierNewPS-BoldMT',        // 3.0 4.3
	'CourierNewPS-ItalicMT',      // 3.0 4.3
	'CourierNewPSMT',             // 3.0 4.3

	// DB LCD Temp 
	'DBLCDTempBlack',   // 6.0 4.3

	// Devanagari Sangam MN 
	'DevanagariSangamMN',       // 3.0 4.3
	'DevanagariSangamMN-Bold',  // 3.0 4.3

	// Didot 
	'Didot',          // 5.0 4.3
	'Didot-Bold',     // 5.0 4.3
	'Didot-Italic',   // 5.0 4.3

	// Euphemia UCAS 
	'EuphemiaUCAS',          // 5.0 5.0
	'EuphemiaUCAS-Bold',     // 5.0 5.0
	'EuphemiaUCAS-Italic',   // 5.0 5.0

	// Futura 
	'Futura-CondensedExtraBold',   // 3.0 4.3
	'Futura-CondensedMedium',      // 5.0 5.0
	'Futura-Medium',               // 3.0 4.3
	'Futura-MediumItalic',         // 3.0 4.3

	// Geeza Pro 
	'GeezaPro',        // 3.0 4.3
	'GeezaPro-Bold',   // 3.0 4.3

	// Georgia 
	'Georgia',              // 3.0 4.3
	'Georgia-Bold',         // 3.0 4.3
	'Georgia-BoldItalic',   // 3.0 4.3
	'Georgia-Italic',       // 3.0 4.3

	// Gill Sans 
	'GillSans',                // 5.0 4.3
	'GillSans-Bold',           // 5.0 4.3
	'GillSans-BoldItalic',     // 5.0 4.3
	'GillSans-Italic',         // 5.0 4.3
	'GillSans-Light',          // 5.0 5.0
	'GillSans-LightItalic',    // 5.0 5.0

	// Gujarati Sangam MN 
	'GujaratiSangamMN',        // 3.0 4.3
	'GujaratiSangamMN-Bold',   // 3.0 4.3

	// Gurmukhi MN 
	'GurmukhiMN',        // 3.0 4.3
	'GurmukhiMN-Bold',   // 3.0 4.3

	// Heiti SC 
	'STHeitiSC-Light',    // 3.0 4.3
	'STHeitiSC-Medium',   // 3.0 4.3

	// Heiti TC 
	'STHeitiTC-Light',    // 3.0 4.3
	'STHeitiTC-Medium',   // 3.0 4.3

	// Helvetica 
	'Helvetica',                 // 3.0 4.3
	'Helvetica-Bold',            // 3.0 4.3
	'Helvetica-BoldOblique',     // 3.0 4.3
	'Helvetica-Light',           // 5.0 5.0
	'Helvetica-LightOblique',    // 5.0 5.0
	'Helvetica-Oblique',         // 3.0 4.3

	// Helvetica Neue 
	'HelveticaNeue',                    // 3.0 4.3
	'HelveticaNeue-Bold',               // 3.0 4.3
	'HelveticaNeue-BoldItalic',         // 3.0 4.3
	'HelveticaNeue-CondensedBlack',     // 5.0 5.0
	'HelveticaNeue-CondensedBold',      // 5.0 5.0
	'HelveticaNeue-Italic',             // 3.0 4.3
	'HelveticaNeue-Light',              // 5.0 5.0
	'HelveticaNeue-LightItalic',        // 5.0 5.0
	'HelveticaNeue-Medium',             // 5.0 5.0
	'HelveticaNeue-UltraLight',         // 5.0 5.0
	'HelveticaNeue-UltraLightItalic',   // 5.0 5.0

	// Hiragino Kaku Gothic ProN 
	'HiraKakuProN-W3',    // 3.0 4.3
	'HiraKakuProN-W6',    // 5.0 4.3

	// Hiragino Mincho ProN 
	'HiraMinProN-W3',    // 3.0 4.3
	'HiraMinProN-W6',    // 3.0 4.3

	// Hoefler Text 
	'HoeflerText-Black',          // 5.0 4.3
	'HoeflerText-BlackItalic',    // 5.0 4.3
	'HoeflerText-Italic',         // 5.0 4.3
	'HoeflerText-Regular',        // 5.0 4.3

	// Kailasa 
	'Kailasa',         // 3.0 4.3
	'Kailasa-Bold',    // 3.0 4.3

	// Kannada Sangam MN 
	'KannadaSangamMN',        // 3.0 4.3
	'KannadaSangamMN-Bold',   // 3.0 4.3

	// Malayalam Sangam MN 
	'MalayalamSangamMN',        // 3.0 4.3
	'MalayalamSangamMN-Bold',   // 3.0 4.3

	// Marion 
	'Marion-Bold',      // 5.0 5.0
	'Marion-Italic',    // 5.0 5.0
	'Marion-Regular',   // 5.0 5.0

	// Marker Felt 
	'MarkerFelt-Thin',   // 3.0 4.3
	'MarkerFelt-Wide',   // 3.0 4.3

	// Noteworthy 
	'Noteworthy-Bold',    // 5.0 5.0
	'Noteworthy-Light',   // 5.0 5.0

	// Optima 
	'Optima-Bold',         // 5.0 4.3
	'Optima-BoldItalic',   // 5.0 4.3
	'Optima-ExtraBlack',   // 5.0 5.0
	'Optima-Italic',       // 5.0 4.3
	'Optima-Regular',      // 5.0 4.3

	// Oriya Sangam MN 
	'OriyaSangamMN',        // 3.0 4.3
	'OriyaSangamMN-Bold',   // 3.0 4.3

	// Palatino 
	'Palatino-Bold',          // 3.0 4.3
	'Palatino-BoldItalic',    // 3.0 4.3
	'Palatino-Italic',        // 3.0 4.3
	'Palatino-Roman',         // 3.0 4.3

	// Papyrus 
	'Papyrus',              // 5.0 4.3
	'Papyrus-Condensed',    // 3.0 5.0

	// Party LET 
	'PartyLetPlain',        // 5.0 4.3

	// Sinhala Sangam MN 
	'SinhalaSangamMN',         // 3.0 4.3
	'SinhalaSangamMN-Bold',    // 3.0 4.3

	// Snell Roundhand 
	'SnellRoundhand',          // 3.0 4.3
	'SnellRoundhand-Black',    // 5.0 5.0
	'SnellRoundhand-Bold',     // 3.0 4.3

	// Symbol 
	'Symbol',    // 6.0 6.0

	// Tamil Sangam MN 
	'TamilSangamMN',         // 3.0 4.3
	'TamilSangamMN-Bold',    // 3.0 4.3

	// Telugu Sangam MN 
	'TeluguSangamMN',        // 3.0 4.3
	'TeluguSangamMN-Bold',   // 3.0 4.3

	// Thonburi 
	'Thonburi',         // 3.0 4.3
	'Thonburi-Bold',    // 3.0 4.3

	// Times New Roman 
	'TimesNewRomanPS-BoldItalicMT',   // 3.0 4.3
	'TimesNewRomanPS-BoldMT',         // 3.0 4.3
	'TimesNewRomanPS-ItalicMT',       // 3.0 4.3
	'TimesNewRomanPSMT',              // 3.0 4.3

	// Trebuchet MS 
	'Trebuchet-BoldItalic',   // 3.0 4.3
	'TrebuchetMS',            // 3.0 4.3
	'TrebuchetMS-Bold',       // 3.0 4.3
	'TrebuchetMS-Italic',     // 3.0 4.3

	// Verdana 
	'Verdana',               // 3.0 4.3
	'Verdana-Bold',          // 3.0 4.3
	'Verdana-BoldItalic',    // 3.0 4.3
	'Verdana-Italic',        // 3.0 4.3

	// Zapf Dingbats 
	'ZapfDingbatsITC',       // 5.0 4.3

	// Zapfino 
	'Zapfino',               // 3.0 4.3

  ];

  static boolean isCompatible(value) { 
	(value instanceof String) || 
	(value instanceof Number) || 
	(value instanceof List) ||
	(value instanceof Map)
  }

  String family
  boolean bold = false 
  boolean italic = false 
  int size = 0

  String owner

  private void setFont(String family) { 
	this.family = family
  }

  private void setFont(String family, int size, boolean bold = false, boolean italic = false) { 
	this.family = family
	this.size = size
	this.bold = bold
	this.italic = italic
  }

  private void setFont(int size, boolean bold = false, boolean italic = false) { 
	this.size = size
	this.bold = bold
	this.italic = italic
  }

  private void setFont(boolean bold = false, boolean italic = false) { 
	this.bold = bold
	this.italic = italic
  }

  String toIOSString() { 
	String sizeStr = size as String
	if (size < 1) { 
	  if (owner == 'Label') { 
		sizeStr = '[UIFont labelFontSize]'
	  } else if (owner == 'Button') { 
		sizeStr = '[UIFont buttonFontSize]'
	  } else { 
		sizeStr = '[UIFont systemFontSize]'
		// smallSystemFontSize
	  }
	}

	String fname = null
	if (family in [ 'Sans', 'Serif', 'Mono' ]) { 
	  String styleKey 
	  if (bold) { 
		if (italic) { 
		  styleKey = 'bold_italic'
		} else { 
		  styleKey = 'bold'
		}
	  } else { 
		if (italic) { 
		  styleKey = 'italic'
		} else { 
		  styleKey = 'plain'
		}
	  }
	  
	  fname = IOSAppGenerator.iosConfig.defaults.Font[family][styleKey]
	}

	if (fname) { 
	  "[UIFont fontWithName:@\"${fname}\" size:${sizeStr}]"
	} else { 
	  if (bold) { 
		"[UIFont boldSystemFontOfSize:${sizeStr}]"
	  } else if (italic) { 
		"[UIFont italicSystemFontOfSize:${sizeStr}]"
	  } else { 
		"[UIFont systemFontOfSize:${sizeStr}]"
	  } 
	}
  }

  String getStyle() { 
	if (bold && italic) { 
	  return 'Bold-Italic'
	} else if (bold) { 
	  return 'Bold'
	} else if (italic) { 
	  return 'Italic'
	} else { 
	  return 'Plain'
	} 
  }

  String toAndroidJavaString() { 
	''
  }

  String toAndroidXMLString() { 
	''
  }

  static Android_Typeface = [
	Sans: 'SANS_SERIF', 
	Serif: 'SERIF', 
	Mono: 'MONOSPACE'
  ]

  static Android_Typeface_XML = [
	Sans: 'sans', 
	Serif: 'serif', 
	Mono: 'monospace'
  ]

  def toAndroidXMLAttributes() { 
	def attrs = [:]
	String typeface = Android_Typeface_XML[family]
	if (typeface) { 
	  attrs['android:typeface'] = typeface 
	}
	if (bold || italic) { 
	  String style = 'normal'
	  if (bold && italic) {  
		style = 'bold|italic'
	  } else if (bold) { 
		style = 'bold'
	  } else { 
		style = 'italic'
	  } 
	  attrs['android:textStyle'] = style 
	}
	if (size >= 1) { 
	  attrs['android:textSize'] = "${size}sp"
	}
	return attrs
  }

  String toAndroidTypeface() { 
	String typeface = Android_Typeface[family]
	if (typeface || bold || italic) {
	  if (!typeface) typeface = 'DEFAULT'	  
	  String style = null
	  if (bold && italic) { 
		style = 'BOLD_ITALIC'
	  } else if (bold) { 
		style = 'BOLD'
	  } else if (italic) { 
		style = 'ITALIC'
	  } else { 
		style = 'NORMAL'
	  }
	  return "Typeface.create(Typeface.${typeface}, Typeface.${style})"
	}
	return null
  }

  String toString() { 
	"Font[${family?:'System'} ${size}${bold ? ' Bold' : ''}${italic ? ' Italic' : ''}]" 
  }

  static getValue(name) { 
	if (name) { 
	  Font font = new Font()
	  font.setFontValue(name)
	  return font
	}
	return null
  }

  void setFontValue(name) { 
	if (name) {
	  if (name instanceof Number) {
		setFont(name as int)
	  } else if (name instanceof String) {
		if (isStyleKey(name)) { 
		  def (bold, italic) = determinStyle(name)
		  setFont(bold, italic)
		} else { 
		  setFont(name)
		}
	  } else if (name instanceof List) { 
		String family = null
		boolean bold = false 
		boolean italic = false 
		int size = 0
		name.each { v -> 
		  if (v instanceof Integer) { 
			size = v
		  } else if (v instanceof String || v instanceof PropertyModel) { 
			String str = v.toString()
			if (isStyleKey(str)) { 
			  (bold, italic) = determinStyle(str)		 
			} else { 
			  family = str
			} 
		  }
		}
		setFont(family, size, bold, italic)
	  } else if (name instanceof Map) { 
		String family = null
		boolean bold = false 
		boolean italic = false 
		int size = 0
		name.each { k, v ->
		  if (k == 'size') { 
			if (v instanceof Number) { 
			  size = (v as int)
			}	
		  } else if (k == 'family') {
			family = v.toString()
		  } else if (k == 'style') {
			String str = v.toString()
			(bold, italic) = determinStyle(str)
		  }
		}
		setFont(family, size, bold, italic)
	  }
	}
  }

  static boolean isStyleKey(key) { 
	if (key) { 
	  String s = key.toString().toLowerCase()
	  return s in [ 'bold', 'italic', 'bold-italic', 'italic-bold' ]
	}
	return false
  }

  static determinStyle(key) { 
	boolean bold = false, italic = false
	if (key) { 
	  String s = key.toString().toLowerCase()
	  if (s == 'bold') { 
		bold = true 
	  } else if (s == 'italic') { 
		italic = true
	  } else if (s == 'bold-italic' || s == 'italic-bold') { 
		bold = italic = true 
	  }
	}
	return [bold, italic]
  }

}

class ButtonFont extends Font { 
  public ButtonFont() { 
	owner = 'Button'
  }
  static getValue(name) { 
	if (name) { 
	  Font font = new ButtonFont()
	  font.setFontValue(name)
	  return font
	}
	return null
  }
}

class LabelFont extends Font { 
  public LabelFont() { 
	owner = 'Label'
  }
  static getValue(name) { 
	if (name) { 
	  Font font = new LabelFont()
	  font.setFontValue(name)
	  return font
	}
	return null
  }
}