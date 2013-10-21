package xj.mobile.transform

import xj.mobile.model.Application
import xj.mobile.model.ui.View
import xj.mobile.model.ui.Widget
import xj.mobile.builder.ModelBuilder
import xj.mobile.builder.Preprocessor
import xj.mobile.model.properties.InterfaceOrientations
import xj.mobile.model.properties.Duration

import static xj.mobile.util.CommonUtil.linesInString
import static xj.translate.Logger.info 

class CommonAppTransformer extends AppTransformer {  

  static DefaultSupportedOrientations = [
	InterfaceOrientations.Portrait,
	InterfaceOrientations.LandscapeLeft,
	InterfaceOrientations.LandscapeRight
  ]

  static Rule_Supported_Orientations = 
  [ { lhs -> lhs.root }, 
	{ lhs ->
	  List supportedOrientations
	  if (lhs.supportedOrientations) { 
		supportedOrientations = lhs.supportedOrientations
	  } else { 
		supportedOrientations = DefaultSupportedOrientations
	  }
	  lhs.supportedOrientationsMethod = (supportedOrientations as Set) != (DefaultSupportedOrientations as Set)
	  if (lhs.initialOrientation) { 
		supportedOrientations = [ lhs.initialOrientation ] + (supportedOrientations - lhs.initialOrientation)
	  }
	  lhs.supportedOrientations = supportedOrientations
	}
  ];
  
  def widgetTransformationRules
  def graphicsTransformationRules

  void init() { 
	widgetTransformationRules = [
	  Label: [
		// rule 1: handle multi-line text
		[ { lhs -> linesInString(lhs.text as String) > 1 && lhs.lines == null }, 
		  { lhs -> lhs.lines = linesInString(lhs.text as String) }
		],
	  ],

	  Button: [
		// rule 1: menu -> name  
		[ { lhs -> lhs.menu instanceof String },
		  { lhs -> mapProperties(lhs, [ 'menu' : 'next' ]) } ],

		// rule 2: menu -> list of items 
		[ { lhs -> lhs.menu instanceof List },
		  { lhs -> 
			def mlist = lhs.menu
			def id = Preprocessor.generateID('menu')
			lhs.next = id
			[ lhs,
			  Menu(id: id) { 
				mlist.each { Item(it) }
			  } ] } ],
	  ],

	  Image: [
		[ { lhs -> lhs.action != null || lhs.next != null }, 
		  { lhs -> 
			lhs['#type'] = 'Button'
			mapWidgetProperties(lhs, [ 'file' : 'image' ])
		  }
		]
	  ],
	
	  Item: [
		/*
		// rule 1: menu -> name  
		[ { lhs -> lhs.menu instanceof String },
		{ lhs -> mapProperties(lhs, [ 'menu' : 'next' ]) } ],
		*/

		// rule 2: menu -> list of items 
		[ { lhs -> lhs.menu instanceof List },
		  { lhs -> 
			def mlist = lhs.menu
			def id = Preprocessor.generateID('menu')
			lhs.menu = id
			[ lhs,
			  Menu(id: id) { 
				mlist.each { Item(it) }
			  } ] } ],
	  ],

	  Section: [
		[ { lhs -> lhs.menu instanceof List },
		  { lhs -> 
			def mlist = lhs.menu
			def id = Preprocessor.generateID('menu')
			lhs.menu = id
			lhs.add(Menu(id: id) { 
					  mlist.each { Item(it) }
					} )
		  } ],
	  ],

	  ListView: [	

		// rule 
		[ { lhs -> lhs.menu instanceof List },
		  { lhs -> 
			def mlist = lhs.menu
			def id = Preprocessor.generateID('menu')
			lhs.menu = id
			lhs.add(Menu(id: id) { 
					  mlist.each { Item(it) }
					} )
		  } ],
      
		[ { lhs -> !(lhs.parent?.nodeType in [ 'app', 'NavigationView' ]) },  
		  { lhs -> lhs.embedded = true } ],
	  ],

	  NumberStepper: [
		[ true, 
		  { lhs -> 
			boolean isInt = true
			def attrs 
			if (lhs.value) { 			  
			  attrs = [ 'value' ]
			} else { 
			  attrs = [ 'max', 'min', 'step' ]
			} 
			attrs.each { a -> if (lhs[a]) isInt &= (lhs[a] instanceof Integer) }
			if (isInt) { 
			  lhs['#subtype'] = 'Int'
			}
		  }
		],

	  ], 


	  View: [
		Rule_Supported_Orientations,
	  ],

	  TabbedView: [
		Rule_Supported_Orientations,
	  ],

	  PageView: [
		Rule_Supported_Orientations,
	  ],

	  NavigationView: [
		Rule_Supported_Orientations,
	  ],

	  onTap: [
		[ true,
		  { lhs ->
			if (lhs.taps == null || lhs.taps == 0) { 
			  lhs.taps = 1
			}
		  }
		]
	  ],

	  doAfter: [
		[ true,
		  { lhs -> 
			if (lhs.delay == null) { 
			  lhs.delay = new Duration(1)
			} else if (lhs.delay instanceof Number) { 
			  lhs.delay = new Duration((float) lhs.delay)
			}
		  }
		]
	  ],

	]

	graphicsTransformationRules = [
	  Rect: [
		[ { lhs -> !lhs.frame && lhs.position && lhs.size },
		  { lhs -> 
			lhs.frame = lhs.position + [ lhs.position[0] + lhs.size[0], lhs.position[1] + lhs.size[1] ]
		  }
		]
	  ],

	  Circle: [
		[ { lhs -> !lhs.frame && lhs.center && lhs.radius },
		  { lhs -> 
			lhs.frame = [ lhs.center[0] - lhs.radius, lhs.center[1] - lhs.radius, lhs.radius * 2, lhs.radius * 2 ]
		  }
		]
	  ],


	]


	transformationRules	= widgetTransformationRules + graphicsTransformationRules
  }

}