
package xj.mobile.model.properties

// To-do: 
//  * handle alpha 
//  * generate doc 
class Color extends Property { 

  static values = [:]

  static final colorMap = [
    'black':     java.awt.Color.black,
    'blue':      java.awt.Color.blue,
    'cyan':      java.awt.Color.cyan,
    'darkGray':  java.awt.Color.darkGray,
    'gray':      java.awt.Color.gray,
    'green':     java.awt.Color.green,
    'lightGray': java.awt.Color.lightGray, 
    'magenta':   java.awt.Color.magenta,
    'orange':    java.awt.Color.orange,
    'pink':      java.awt.Color.pink,
    'red':       java.awt.Color.red,
    'white':     java.awt.Color.white,
    'yellow':    java.awt.Color.yellow,

    'purple':    new java.awt.Color(128, 0, 128),
    'brown':     new java.awt.Color(165, 42, 42),
  ]

  static final htmlColorNames = [
	AliceBlue      : 'F0F8FF',
	AntiqueWhite   : 'FAEBD7',
	Aqua           : '00FFFF',
	Aquamarine     : '7FFFD4',
	Azure          : 'F0FFFF',
	Beige          : 'F5F5DC',
	Bisque         : 'FFE4C4',
	Black          : '000000',
	BlanchedAlmond : 'FFEBCD',
	Blue           : '0000FF',
	BlueViolet     : '8A2BE2',
	Brown          : 'A52A2A',
	BurlyWood      : 'DEB887',
	CadetBlue      : '5F9EA0',
	Chartreuse     : '7FFF00',
	Chocolate      : 'D2691E',
	Coral          : 'FF7F50',
	CornflowerBlue : '6495ED',
	Cornsilk       : 'FFF8DC',
	Crimson        : 'DC143C',
	Cyan           : '00FFFF',
	DarkBlue       : '00008B',
	DarkCyan       : '008B8B',
	DarkGoldenRod  : 'B8860B',
	DarkGray       : 'A9A9A9',
	DarkGrey       : 'A9A9A9',
	DarkGreen      : '006400',
	DarkKhaki      : 'BDB76B',
	DarkMagenta    : '8B008B',
	DarkOliveGreen : '556B2F',
	Darkorange     : 'FF8C00',
	DarkOrchid     : '9932CC',
	DarkRed        : '8B0000',
	DarkSalmon     : 'E9967A',
	DarkSeaGreen   : '8FBC8F',
	DarkSlateBlue  : '483D8B',
	DarkSlateGray  : '2F4F4F',
	DarkSlateGrey  : '2F4F4F',
	DarkTurquoise  : '00CED1',
	DarkViolet     : '9400D3',
	DeepPink       : 'FF1493',
	DeepSkyBlue    : '00BFFF',
	DimGray        : '696969',
	DimGrey        : '696969',
	DodgerBlue     : '1E90FF',
	FireBrick      : 'B22222',
	FloralWhite    : 'FFFAF0',
	ForestGreen    : '228B22',
	Fuchsia        : 'FF00FF',
	Gainsboro      : 'DCDCDC',
	GhostWhite     : 'F8F8FF',
	Gold           : 'FFD700',
	GoldenRod      : 'DAA520',
	Gray           : '808080',
	Grey           : '808080',
	Green          : '008000',
	GreenYellow    : 'ADFF2F',
	HoneyDew       : 'F0FFF0',
	HotPink        : 'FF69B4',
	IndianRed      : 'CD5C5C',
	Indigo         : '4B0082',
	Ivory          : 'FFFFF0',
	Khaki          : 'F0E68C',
	Lavender       : 'E6E6FA',
	LavenderBlush  : 'FFF0F5',
	LawnGreen      : '7CFC00',
	LemonChiffon   : 'FFFACD',
	LightBlue      : 'ADD8E6',
	LightCoral     : 'F08080',
	LightCyan      : 'E0FFFF',
	LightGoldenRodYellow : 'FAFAD2',
	LightGray      : 'D3D3D3',
	LightGrey      : 'D3D3D3',
	LightGreen     : '90EE90',
	LightPink      : 'FFB6C1',
	LightSalmon    : 'FFA07A',
	LightSeaGreen  : '20B2AA',
	LightSkyBlue   : '87CEFA',
	LightSlateGray : '778899',
	LightSlateGrey : '778899',
	LightSteelBlue : 'B0C4DE',
	LightYellow    : 'FFFFE0',
	Lime           : '00FF00',
	LimeGreen      : '32CD32',
	Linen          : 'FAF0E6',
	Magenta        : 'FF00FF',
	Maroon         : '800000',
	MediumAquaMarine : '66CDAA',
	MediumBlue     : '0000CD',
	MediumOrchid   : 'BA55D3',
	MediumPurple   : '9370D8',
	MediumSeaGreen : '3CB371',
	MediumSlateBlue : '7B68EE',
	MediumSpringGreen : '00FA9A',
	MediumTurquoise : '48D1CC',
	MediumVioletRed : 'C71585',
	MidnightBlue    : '191970',
	MintCream       : 'F5FFFA',
	MistyRose       : 'FFE4E1',
	Moccasin        : 'FFE4B5',
	NavajoWhite     : 'FFDEAD',
	Navy            : '000080',
	OldLace         : 'FDF5E6',
	Olive           : '808000',
	OliveDrab       : '6B8E23',
	Orange          : 'FFA500',
	OrangeRed       : 'FF4500',
	Orchid          : 'DA70D6',
	PaleGoldenRod   : 'EEE8AA',
	PaleGreen       : '98FB98',
	PaleTurquoise   : 'AFEEEE',
	PaleVioletRed   : 'D87093',
	PapayaWhip      : 'FFEFD5',
	PeachPuff       : 'FFDAB9',
	Peru            : 'CD853F',
	Pink            : 'FFC0CB',
	Plum            : 'DDA0DD',
	PowderBlue      : 'B0E0E6',
	Purple          : '800080',
	Red             : 'FF0000',
	RosyBrown       : 'BC8F8F',
	RoyalBlue       : '4169E1',
	SaddleBrown     : '8B4513',
	Salmon          : 'FA8072',
	SandyBrown      : 'F4A460',
	SeaGreen        : '2E8B57',
	SeaShell        : 'FFF5EE',
	Sienna          : 'A0522D',
	Silver          : 'C0C0C0',
	SkyBlue         : '87CEEB',
	SlateBlue       : '6A5ACD',
	SlateGray       : '708090',
	SlateGrey       : '708090',
	Snow            : 'FFFAFA',
	SpringGreen     : '00FF7F',
	SteelBlue       : '4682B4',
	Tan             : 'D2B48C',
	Teal            : '008080',
	Thistle         : 'D8BFD8',
	Tomato          : 'FF6347',
	Turquoise       : '40E0D0',
	Violet          : 'EE82EE',
	Wheat           : 'F5DEB3',
	White           : 'FFFFFF',
	WhiteSmoke      : 'F5F5F5',
	Yellow          : 'FFFF00',
	YellowGreen     : '9ACD32',	
  ]

  static { 
	def cnames = []
	cnames.addAll(htmlColorNames.keySet())
	cnames.each { name ->
	  htmlColorNames[name.toLowerCase()] = htmlColorNames[name]
	}
  }

  static final Color black     = new Color('black'); 
  static final Color blue      = new Color('blue'); 
  static final Color cyan      = new Color('cyan'); 
  static final Color darkGray  = new Color('darkGray'); 
  static final Color gray      = new Color('gray'); 
  static final Color green     = new Color('green'); 
  static final Color lightGray = new Color('lightGray'); 
  static final Color magenta   = new Color('magenta'); 
  static final Color orange    = new Color('orange'); 
  static final Color pink      = new Color('pink'); 
  static final Color red       = new Color('red'); 
  static final Color white     = new Color('white'); 
  static final Color yellow    = new Color('yellow'); 

  static final Color purple    = new Color('purple'); //new java.awt.Color(128, 0, 128);
  static final Color brown     = new Color('brown'); //new java.awt.Color(165, 42, 42);

  String name
  java.awt.Color color

  private Color(String name) { 
    this.name = name
    this.color = colorMap[name]
    values[name] = this
  }

  private Color(java.awt.Color color) { 
	this.color = color
  }

  static boolean isCompatible(value) { 
	(value instanceof String) || (value instanceof List)
  }

  String toIOSString() { 
	if (name) { 
	  return "[UIColor ${name}Color]"
	} else {
	  float r = color.getRed()
	  float g = color.getGreen()
	  float b = color.getBlue()
	  r = r / 255f
	  g = g / 255f
	  b = b / 255f
	  return "[UIColor colorWithRed:${r} green:${g} blue:${b} alpha:1.0]"
	}
  }

  def toIOSRGBA() { 
	float r = color.getRed()
	float g = color.getGreen()
	float b = color.getBlue()
	return [ r / 255f,  g / 255f, b / 255f, 1.0f ]
  }

  def getRGB() { 
	[ color.getRed(), color.getGreen(), color.getBlue() ]
  }

  String toAndroidJavaString() { 
	if (name) { 
	  return "android.graphics.Color.${name.toUpperCase()}"
	} else { 
	  int r = color.getRed()
	  int g = color.getGreen()
	  int b = color.getBlue()
	  return "android.graphics.Color.argb(255, ${r}, ${g}, ${b})"
	}
  }

  String toAndroidXMLString() { 
	'#' + toHexString()
  }

  String toString() { 
	if (name) { 
	  return "Color.${name}"
	} else if (color) { 
	  int r = color.getRed()
	  int g = color.getGreen()
	  int b = color.getBlue()
	  return "Color[${r}, ${g}, ${b}]"
	} 
	//color?.toString()
	return 'Color.undefined'
  }

  String toHexString() { 
    if (color) { 
      return Integer.toHexString(color.getRGB())[2 .. -1].toUpperCase()
    }
    return null
  }

  static getColor(name) {
    if (name) { 
      return values[name.toLowerCase()]
    } else { 
      return null
    } 
  }

  static hasValue(name) { 
	name && (values.containsKey(name.toLowerCase()) || isHexCode(name) || htmlColorNames.containsKey(name))
  }

  static getValue(name) { 
	if (name) {
	  if (name instanceof String) { 
		String name0 = name.toLowerCase()
		if (values.containsKey(name0)) { 
		  return values[name0]
		} else if (isHexCode(name)) { 
		  return getColorByHexCode(name[1..6])
		} else if (htmlColorNames.containsKey(name)) { 
		  return getColorByHexCode(htmlColorNames[name])
		}
	  } else if (name instanceof List) { 
		if(name.size() == 3) { 
		  if (name.every { n -> n instanceof Number && n >= 0 && n <= 1}) { 
			float r = name[0] as float
			float g = name[1] as float
			float b = name[2] as float
			return new Color(new java.awt.Color(r, g, b))
		  } else if (name.every { n -> n instanceof Number && n >= 0 && n <= 255}) { 
			int r = name[0] as int
			int g = name[1] as int
			int b = name[2] as int
			return new Color(new java.awt.Color(r, g, b))
		  }
		}
	  }
	}
	return null
  }

  static getColorByHexCode(code) { 
	int r = Integer.parseInt(code[0,1], 16)
	int g = Integer.parseInt(code[2,3], 16)
	int b = Integer.parseInt(code[4,5], 16)
	return new Color(new java.awt.Color(r, g, b))
  }

  static boolean isHexCode(String code) { 
	(code && code.length() == 7 && code[0] == '#' && 
	 code[1 .. 6].every { c -> (c in '0' .. '9') || 
	   (c in 'a' .. 'f') || (c in 'A' .. 'F')})
  }

}