package xj.mobile.transform

import xj.mobile.*
import xj.mobile.common.*
import xj.mobile.model.*
import xj.mobile.model.ui.*
import xj.mobile.builder.ModelBuilder

//import static xj.mobile.builder.AppBuilder.builder 

/*
 *   Handle Android design transformation 
 */
class AndroidAppTransformer extends AppTransformer {  

  ModelNode transformSubtree(ModelNode node) {
    if (node) { 
      def design = node['@Design:Android']
      if (design) { 
		def viewDesign = design['view'] 
		if (viewDesign && checkViewDesign(node, viewDesign)) { 
		  node['#type'] = viewDesign
		}
      }
    }
    return super.transformSubtree(node)
  }

  static boolean checkViewDesign(view, viewDesign) { 
    switch (view.widgetType) { 
    case 'ListView': 
      return viewDesign in [ 'ExpandableListView' ]
    }
    return false 
  }

  /*
  def getTransformationRules(ModelNode node) {  
    return widgetTransformationRules[node?.nodeType]
  }
  */

  static boolean isMultiOptions(list) { 
    list.every { opt -> opt instanceof List }
  }

  static textFieldTypeMap = [ 'decimal': [ 'numberDecimal',  'decimal' ],
							  'number':  [ 'number', 'number' ],
							  'email':   [ 'textEmailAddress', 'email' ],
							  'phone':   [ 'phone', 'phone' ], 
							  'url':     [ 'textUri', 'URL' ], 

							  'Default':               [ 'text', 'text' ],
							  'ASCIICapable':          [ 'text', 'ASCII' ],
							  'NumbersAndPunctuation': [ 'numberSigned', 'number' ],
							  'URL':                   [ 'textUri', 'URL' ],
							  'NumberPad':             [ 'number', 'number' ],
							  'PhonePad':              [ 'phone', 'phone' ],
							  'NamePhonePad':          [ 'phone', 'phone' ],
							  'EmailAddress':          [ 'textEmailAddress', 'email' ],
							  'DecimalPad':            [ 'numberDecimal', 'decimal' ],
							  'Twitter':               [ 'text', 'twitter' ],
							  'Alphabet':              [ 'text', 'text' ],
							];

    // rule: [condition, rhs]
  def widgetTransformationRules
  
  void init() { 
	widgetTransformationRules = [
	  Button: [
		[ { lhs -> lhs.image != null },
		  { lhs -> 
			lhs['#type'] = 'ImageButton'
			return lhs 
		  } ],
		[ true, 
		  { lhs -> 
			mapWidgetProperties(lhs, [ 'titleFont' : 'font' ])
		  } ],
	  ],

	  Text: [
		[ true,
		  { lhs ->
			def prompt = lhs.prompt
			def type = lhs.type
			if (type == null && lhs.keyboardType instanceof xj.mobile.model.properties.KeyboardType)
			  type = lhs.keyboardType.toShortString()
			if (type != null) { 
			  def defval = textFieldTypeMap[type] ?: [ null, null ] 
			  lhs.prompt = prompt ?: defval[1]
			  lhs.inputType = defval[0]
			}
			return lhs
		  } ],
	  ], 


	  // Rules for Selection 
	  Selection: [
		// rule 1 
		[ [ design: 'RadioGroup'], 
		  { lhs -> 
			def prop = widgetProperties(lhs)
			mapProperties(prop, [ 'action' : 'selection' ])
			RadioGroup(prop) { 
			  lhs.options.each { opt -> RadioButton(text: opt) }
			}
		  } ],
		// rule 2: default, no longer needed 
		/*
		  [ true, 
		  { lhs -> 
		  lhs.widgetType = 'Spinner';
		  //mapProperties(lhs, [ 'action' : 'selection' ])
		  } ],
		*/
	  ],

	  // Rules for Picker 
	  Picker : [
		// rule 1: multiple options 
		[ { lhs -> isMultiOptions(lhs.options) }, 
		  { lhs ->  
			def prop = widgetProperties(lhs)
			SpinnerGroup(prop) { 
			  lhs.options.each { opt -> Spinner(options: opt) }
			}
		  } ],
		// rule 2: single option, default, nolonger needed  
		/*
		  [ true,
		  { lhs ->  
		  lhs.widgetType = 'Spinner';
		  } ],
		*/
	  ],

	  onTap: [
		[ { (it.taps ?: 0) >= 2 },
		  { lhs -> 
			lhs['#type'] = 'onDoubleTap'
			lhs.taps = 2
		  }
		]
	  ],

	]

	transformationRules	= widgetTransformationRules 	
  }

}