package xj.mobile.lang

class AttributeMap {


  //
  // attribute definitions 
  //

  public static CommonAttributeDef = [
    Label:  [
	  text: [ type: 'String' ],
	  backgroundColor: [ type: 'Color' ],
    ],

    ImageView: [
	  //contentMode: [ type: 'ContentMode' ],
	  //clipsToBounds: [ type: 'Boolean' ],
    ],

    Button:  [
	  text: [ type: 'String' ],
	  image: [ type: 'String' ],
    ],

    CheckBox:  [
	  text: [ type: 'String' ],
	  checked: [ type: 'Boolen' ],

	  //titleEdgeInsets: [ type: 'EdgeInsets' ],
	  //contentHorizontalAlignment: [ type: 'ContentHorizontalAlignment' ],
    ],

    RadioButton:  [
	  text: [ type: 'String' ],

	  //titleEdgeInsets: [ type: 'EdgeInsets' ],
	  //contentHorizontalAlignment: [ type: 'ContentHorizontalAlignment' ],
	],

    Text: [
	  text: [ type: 'String' ],

	  //borderStyle: [ type: 'BorderStyle' ],
	  //keyboardType: [ type: 'KeyboardType' ],

	  // prompt: [ type: 'String' ], 
    ], 

    Switch: [

    ],

    Slider: [

    ],

    Selection: [
	  value: [ type: 'BigDecimal', indexed: true ], 
	],

    SegmentedControl: [
	  value: [ type: 'BigDecimal', indexed: true ], 

	  //segmentedControlStyle: [ type: 'SegmentedControlStyle' ], 
    ],

	NumberStepper: [
	  value: [ type: 'BigDecimal' ],
	],

    ProgressView: [
    ],

    Picker: [
	  value: [ type: 'Object', indexed: true ], 

	  //showsSelectionIndicator: [ type: 'Boolean' ],
    ],

    Spinner: [ // android
	  value: [ type: 'Object' ], 
	],

    SpinnerGroup: [ // android
	  value: [ type: 'Object', indexed: true ], 
	],

	RadioGroup: [ // android 
	  selected: [ type: 'Object' ], 
	],

    DatePicker: [
	  date: [ type: 'Date' ]

	  //datePickerMode: [ type: 'DatePickerMode' ],
    ],

    TimePicker: [
	  time: [ type: 'Date' ]
	  //datePickerMode: [ type: 'DatePickerMode' ],
    ],

    DateTimePicker: [
	  date_time: [ type: 'Date' ],
    ],

    Web: [
	  url: [ type: 'String' ],
	  html: [ type: 'String' ],

	  //'scalesPageToFit': 'YES',
	  //'autoresizingMask': '(UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight)'
    ],

    Map: [

    ]

  ];

  static findCommonAttributeDef(String widgetType, String attrName) { 
	def wdef = CommonAttributeDef[widgetType]
	if (wdef) { 
	  return wdef[attrName]
	} else { 
	  return null
	}
  }

  //
  //  attribute aliases 
  //

  static commonAliases = [
	color: 'textColor',
	background: 'backgroundColor',
	shadow: 'shadowColor',

	lines: 'numberOfLines',

	max: [ 'maximum', 'maxValue', 'maximumValue'],
	min: [ 'minimum', 'minValue', 'minimumValue'],
  ]

  static platformAliases = [
	'iOS': [
	  UIButton: [
		color: 'titleColor',
		textColor: 'titleColor',
	  ],
	  
	  UILabel: [
	  ],

	  UIStepper: [
		step: 'stepValue'
	  ],

	  UIAlertView: [
		style: 'alertViewStyle'
	  ]
	],

	'Android': [
	  Button: [
		titleFont: 'font',
		titleText: 'text',
	  ],

	  TextView: [
	  ],

	  SeekBar: [
		value: 'progress'
	  ],

	]
  ]

  // attributes should not be accessible to user
  static filteredAttributes = [
	'iOS': [
	  'autoresizesSubviews',
	  'autoresizingMask',
	  'bounds',
	  'center',
	  'clearsContextBeforeDrawing',
	  'clipsToBounds',
	  'contentMode',
	  'delegate',
	  'exclusiveTouch',
	  'frame',
	  'gestureRecognizers',
	  'inputAccessoryView',
	  'inputView',
	  'layer',
	  'multipleTouchEnabled',
	  'restorationIdentifier',
	  'subviews',
	  'superview',
	  'tag',
	  'undoManager',
	  'userInteractionEnabled',
	  'window',
	],

	'Android': [
	  'drawableBottom',
	  'drawableLeft',
	  'drawablePadding',
	  'drawableRight',
	  'drawableTop',
	  'drawingCacheQuality',
	  'editorExtras',
	  'ems',
	  'filterTouchesWhenObscured',
	  'fitsSystemWindows',
	  //'gravity',
	  'height',
	  //'hint',
	  'id',
	  //'imeActionId',
	  //'imeActionLabel',
	  //'imeOptions',
	  'importantForAccessibility',
	  'includeFontPadding',
	  //'isScrollContainer',
	  'layerType',
	  //'longClickable',
	  //'marqueeRepeatLimit',
	  'maxEms',
	  'maxHeight',
	  'maxWidth',
	  'minEms',
	  'minHeight',
	  'minWidth',
	  //'nextFocusDown',
	  //'nextFocusForward',
	  //'nextFocusLeft',
	  //'nextFocusRight',
	  //'nextFocusUp',
	  'padding',
	  'paddingBottom',
	  'paddingLeft',
	  'paddingRight',
	  'paddingTop',
	  'privateImeOptions',
	  //'requiresFadingEdge',
	  //'scrollHorizontally',
	  //'scrollbarDefaultDelayBeforeFade',
	  //'scrollbarFadeDuration',
	  //'scrollbarSize',
	  //'scrollbarStyle',
	  'selectAllOnFocus',
	  //'soundEffectsEnabled',
	  'width',
	],
  ]


}