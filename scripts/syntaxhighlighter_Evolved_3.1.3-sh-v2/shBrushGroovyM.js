/**
 * SyntaxHighlighter
 * http://alexgorbatchev.com/SyntaxHighlighter
 *
 * SyntaxHighlighter is donationware. If you are using it, please donate.
 * http://alexgorbatchev.com/SyntaxHighlighter/donate.html
 *
 * @version
 * 3.0.83 (July 02 2010)
 * 
 * @copyright
 * Copyright (C) 2004-2010 Alex Gorbatchev.
 *
 * @license
 * Dual licensed under the MIT and GPL licenses.
 *
 * Modified brush for MADL 
 */

;(function()
{
	// CommonJS
	typeof(require) != 'undefined' ? SyntaxHighlighter = require('shCore').SyntaxHighlighter : null;

	function Brush()
	{
		var keywords =	'as assert break case catch class continue def default do else extends finally ' +
		    'if in implements import instanceof interface new package property return switch ' +
			'throw throws try while public protected private static';
		var types    =  'void boolean byte char short int long float double' +
                        'String Object Date List Map Set';
		var constants = 'null true false';
		var methods   = 'allProperties count get '+
			'collect each eachProperty eachPropertyName eachWithIndex find findAll ' +
			'findIndexOf grep inject max min reverseEach sort ' +
			'asImmutable asSynchronized flatten intersect join pop reverse subMap toList ' +
			'padRight padLeft contains eachMatch toCharacter toLong toUrl tokenize ' +
			'eachFile eachFileRecurse eachB yte eachLine readBytes readLine getText ' +
			'splitEachLine withReader append encodeBase64 decodeBase64 filterLine ' +
			'transformChar transformLine withOutputStream withPrintWriter withStream ' +
			'withStreams withWriter withWriterAppend write writeLine '+
			'dump inspect invokeMethod print println step times upto use waitForOrKill '+
			'getText';


	    var app = 'app' 
	    var widgets = 'Alert Popup Menu DatePickerDialog TimePickerDialog Label Image Button Text Switch Slider Selection NumberStepper CheckBox RadioButton ProgressBar Picker DatePicker TimePicker Item Web Map Annotation Toast Canvas DateTimePicker PageControl Page Spinner ImageButton'
	    var containers = 'View TabbedView NavigationView ListView Panel FlowPanel GridPanel DockPanel AbsolutePanel StackPanel SplitPanel Box Table Row Section RadioGroup PageView ExpandableListView SpinnerGroup'
	    var transitions = 'when onShake onTouch onDrag onSwipe onPinch onTap onDoubleTap onRotation onLongPress onMotion onOrientationChange onDeviceOrientationChange'
	    var shapes = 'Point Line Curve Rect Ellipse Circle Arc ImageShape TextShape Path'
	    var path = 'moveTo lineTo curveTo arcTo'
	    var actions = 'onEntry onExit onDone do doAfter draw'

	    var attributes = 'id name navigationBar classes icon background scroll title tabImage rightButton shortTitle supportedOrientations initialOrientation text width color textColor font align lines line lineBreak frame shadow shadowOffset action selection orientation options prompt style next menu accessory detailText image type choiceMode embedded @Design:Android tintColor titleFont size animated transition keyboardType pattern file cols max step value min url html latlon span subtitle affirm cancel buttons message alertViewStyle antialias fillColor position join end start cap center radius clockwise control endAngle startAngle closed location taps touches delay direction frequency'

		this.regexList = [
			{ regex: SyntaxHighlighter.regexLib.singleLineCComments,				css: 'comments' },		// one line comments
			{ regex: SyntaxHighlighter.regexLib.multiLineCComments,					css: 'comments' },		// multiline comments
			{ regex: SyntaxHighlighter.regexLib.doubleQuotedString,					css: 'string' },		// strings
			{ regex: SyntaxHighlighter.regexLib.singleQuotedString,					css: 'string' },		// strings
			{ regex: /""".*"""/g,													css: 'string' },		// GStrings
			{ regex: new RegExp('\b([\d]+(\.[\d]+)?|0x[a-f0-9]+)\b', 'gi'),	css: 'value' },			// numbers
			{ regex: new RegExp(this.getKeywords(keywords), 'gm'),					css: 'keyword' },		// goovy keyword
			{ regex: new RegExp(this.getKeywords(types), 'gm'),					    css: 'color1' },		// goovy/java type
			{ regex: new RegExp(this.getKeywords(constants), 'gm'),					css: 'constants' },		// constants
		    { regex: new RegExp(this.getKeywords(methods), 'gm'),					css: 'functions' },		// methods
		    { regex: new RegExp(this.getKeywords(app), 'gm'),		                css: 'color4 italic' },
		    { regex: new RegExp(this.getKeywords(widgets), 'gm'),		            css: 'functions bold' },
		    { regex: new RegExp(this.getKeywords(containers), 'gm'),		        css: 'color4 bold' },

		    { regex: new RegExp(this.getKeywords(shapes), 'gm'),		            css: 'color5' },
		    { regex: new RegExp(this.getKeywords(path), 'gm'),		                css: 'color5 italic' },

		    { regex: new RegExp(this.getKeywords(transitions), 'gm'),		        css: 'color6 italic' },
		    { regex: new RegExp(this.getKeywords(actions), 'gm'),		            css: 'color7 italic' },

		    { regex: new RegExp(this.getKeywords(attributes), 'gm'),		        css: 'value' },
			];


		this.forHtmlScript(SyntaxHighlighter.regexLib.aspScriptTags);
	}

	Brush.prototype	= new SyntaxHighlighter.Highlighter();
	Brush.aliases	= ['groovy-m'];

	SyntaxHighlighter.brushes.GroovyM = Brush;

	// CommonJS
	typeof(exports) != 'undefined' ? exports.Brush = Brush : null;
})();
