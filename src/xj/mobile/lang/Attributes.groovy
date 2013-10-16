package xj.mobile.lang

import groovy.text.SimpleTemplateEngine

class Attributes {


  static textAttributes = [ 'name', 'title', 'text', 'subtitle', 'message' ]
  static commonWidgetAttributes = [ 'id', 'background', 'alpha', 'hidden', 'enabled' ]

  static antonyms = [
	hidden: 'visible',
	enabled: 'disabled',
  ]

  static attributes = [
	id: [
	  type: 'String',
	  description: 'A unique id of the widget.', 
	  restriction: 'Must be an identifier and unique.', 
	],

	next: [
	  type: 'String',
	  description: 'The id of the next view or state, which is the result or destination of the transition triggered by an event.', 
	  restriction: 'Reference to the unique id of the next view or state.', 
	],

	/////////

	alpha: [
	  type: 'float',
	  description: 'The alpha property of the view, as a value between 0 (completely transparent) and 1 (completely opaque).',
	  restriction: '0 <= alpha <= 1.',
	],

	enabled: [
	  type: 'boolean',
	  description: 'A boolean value that determines whether the widget is enabled.',
	  antonyms: [ 'disabled' ],
	],

	hidden: [
	  type: 'boolean',
	  description: 'A boolean value that determines whether the widget is hidden.',
	  antonyms: [ 'visible' ],
	],

	/*
	background: [
	  
	],
	*/

	///////////// text attributes 

	_TEXT : [
	  type: 'String',
	  description: 'A descriptive ${t}.'
	],

	_SHORT_TEXT : [
	  type: 'String',
	  description: 'A short description of ${t}.'
	],

	_DETAIL_TEXT : [
	  type: 'String',
	  description: 'A detail description of ${t}.'
	],

	////////////////

	latlon : [
	  type: '[BigDecimal,BigDecimal]',
	  description: 'The latitude and longitude of a location.'
	],

	span : [
	  type: '[BigDecimal,BigDecimal]',
	  description: 'The latitude and longitude span of a region.'
	],

	////////////////

	action : [
	  type: 'Closure',
	  description: 'The action triggered by the action event originating from this widget.'
	]

	///////////////

	// color, background, textColor, tintColor
	// button, buttons
	// cancel, affirm
	// prompt 
	// pattern

	// url
 
	// menu 

	// image

	// selection 

	// size, width 

	// max, min, value

  ]

  ////

  static engine = new SimpleTemplateEngine();

  static getAttributeDescription(name) { 
	def desc = null
	def attrDef = attributes[name]
	if (attrDef) { 
	  desc = attrDef.description
	} else {
	  def temp = null
	  def binding = [:]
	  if (name in textAttributes) { 
		temp = attributes['_TEXT'].description
		binding['t'] = name
	  } else { 
		if (name.startsWith('short') && name[5..-1].toLowerCase() in textAttributes) { 
		  temp = attributes['_SHORT_TEXT'].description
		  binding['t'] = name[5..-1].toLowerCase()
		} else if (name.startsWith('detail') && name[6..-1].toLowerCase() in textAttributes) { 
		  temp = attributes['_DETAIL_TEXT'].description
		  binding['t'] = name[6..-1].toLowerCase()
		}
	  }
	  if (temp && binding) { 
		desc = engine.createTemplate(temp).make(binding).toString()
	  }
	}
	return desc
  }

  static getAttributeDef(name) { 
	def attrDef = attributes[name]
	if (!attrDef) { 
	  if (name in textAttributes) { 
		attrDef = attributes['_TEXT']
	  } else { 
		if (name.startsWith('short') && name[5..-1].toLowerCase() in textAttributes) { 
		  attrDef = attributes['_SHORT_TEXT']
		} else if (name.startsWith('detail') && name[6..-1].toLowerCase() in textAttributes) { 
		  attrDef = attributes['_DETAIL_TEXT']
		}
	  }
	}
	return attrDef
  }

}