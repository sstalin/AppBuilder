package xj.mobile.codegen.templates

import xj.mobile.lang.WidgetMap
import xj.mobile.android.*

import static xj.mobile.model.impl.ClassModel.getCustomViewName

class AndroidWidgetTemplates extends WidgetTemplates { 

  ///// Templates

  static String declarationTemplate = '${uiclass} ${name};'

  static String findViewTemplate = '${name} = (${uiclass}) findViewById(R.id.${name});'

  AndroidWidgetTemplates(target) { 
    super(target)
  }

  ////// Widget Templates  

  def CommonWidgetTemplate = [

    setAttribute: '${name}.set${capitalize(attribute)}(${value})',
    getAttribute: '${name}.get${capitalize(attribute)}()',
    getBooleanAttribute: '${name}.is${capitalize(attribute)}()',
    getIndexedAttribute: '${name}.get${capitalize(attribute)}()[${index}]',

    action: '''
public void ${actionName}(View view) {
${actionBody}
}
''',

  ]

  def widgetMap = WidgetMap.widgets_android

  def widgetTemplates = [
    TextView:  [
      uiclass: 'TextView',
    ],

    Button: [
      uiclass: 'Button',
      xevent: 'onClick',
    ],

    EditText: [
      uiclass: 'EditText',

      defaultAttributes: { widget ->
		def attr = [:]
		if (widget.inputType) { 
		  attr['android:inputType'] = widget.inputType
		}
		if (widget.prompt) { 
		  attr['android:hint'] = widget.prompt
		}
		return attr
      },

	  get_text: '${name}.getText().toString()',
    ],

    SeekBar: [
      uiclass: 'SeekBar',

	  template: 'Default:slider'
    ],

    CheckBox: [
      uiclass: 'CheckBox',
      xevent: 'onClick',
      get_checked: '${name}.isChecked()',
    ],

    RadioButton: [
      uiclass: 'RadioButton',
      xevent: 'onClick',
    ],

    RadioGroup: [
      get_selected: 'selectedRadioButton_${name}()'
    ],

    SpinnerGroup: [
      get_value: 'items',
      get_value_indexed: 'items[${index}]'
    ], 

    ToggleButton: [
      uiclass: 'ToggleButton',
      xevent: 'onClick',
    ],

    Spinner: [
      uiclass: 'Spinner',

      defaultAttributes: [
		'android:drawSelectorOnTop': 'true' 
      ],

      processor: new SpinnerProcessor(),

      get_value: 'item'
    ],

	NumberStepper: [
	  uiclass: 'NumberStepper',

	  custom: true,
	  superclass: 'LinearLayout',
	  xevent: 'onAction',

	  styleable: [ value: 'float',
				   minValue: 'float',
				   maxValue: 'float',
				   step: 'float',
				   autoRepeat: 'boolean',
				   intValue: 'boolean',
				   onAction: 'string' ],

	  get_value: '${name}.get${capitalize(attribute)}()'

	],

	'NumberStepper#Int': [
	  uiclass: 'NumberStepper',

	  custom: true,
	  superclass: 'LinearLayout',
	  xevent: 'onAction',

	  styleable: [ value: 'float',
				   minValue: 'float',
				   maxValue: 'float',
				   step: 'float',
				   autoRepeat: 'boolean',
				   intValue: 'boolean',
				   onAction: 'string' ],

	  get_value: '(int) ${name}.get${capitalize(attribute)}()'
	],


    TimePicker: [
      uiclass: 'TimePicker',

      processor: new TimePickerProcessor(),

      get_time: 'time'
    ],

    DatePicker: [
      uiclass: 'DatePicker',

      processor: new DatePickerProcessor(),

      get_date: 'date'
    ],

    ProgressBar: [
      uiclass: 'ProgressBar',

      defaultAttributes: [
		'style': '@android:style/Widget.ProgressBar.Horizontal'
      ]
    ],

    ImageView: [
      uiclass: 'ImageView',

      defaultAttributes: [
		'android:src': '@drawable/${xj.mobile.util.CommonUtil.getFileName(file.toLowerCase())}'
      ],

	  layoutProcessor: new ImageLayoutProcessor()
    ],

    ImageButton: [
      uiclass: 'ImageButton',
      xevent: 'onClick',

      defaultAttributes: [
		'android:src': '@drawable/${xj.mobile.util.CommonUtil.getFileName(image.toLowerCase())}'
      ],

	  layoutProcessor: new ImageLayoutProcessor()
    ],

	ListView: [
	  uiclass: 'ListView',
	],

    WebView: [
      uiclass: 'WebView',

      permission: 'INTERNET',

      set_url: '''${name}.loadUrl(${value});''',

      initialAttributes: [ 'url' ],

	  template: 'Web:web1'
    ],

    MapView: [
      uiclass: 'com.google.android.maps.MapView',

      activity: 'MapActivity',

      library: 'com.google.android.maps',

      permission: [ 'INTERNET', 'ACCESS_FINE_LOCATION', 'ACCESS_COARSE_LOCATION' ],

      defaultAttributes: [
		'android:clickable': 'true',
		'android:apiKey': AndroidAppGenerator.androidConfig.defaults.Map.apiKey, 
      ],

      initialAttributes: [ [ 'latlon', 'span' ] ],

      set_latlon_span: '''MapController mapCtrl = ${name}.getController();
GeoPoint point = new GeoPoint(${Eval.me(\'(int) (\' + latlon[0] + \'*1e6)\')}, ${Eval.me(\'(int) (\' + latlon[1] + \'*1e6)\')});
mapCtrl.setCenter(point);
mapCtrl.zoomToSpan(${Eval.me(\'(int) (\' + span[0] + \'*1e6)\')}, ${Eval.me(\'(int) (\' + span[1] + \'*1e6)\')});''', 


	  template: 'Map:map1'
    ],

	Canvas: [
	  uiclass: { widget -> getCustomViewName(widget.id) },

	  custom: true,

	  processor: new CanvasProcessor()
	],


  ]

}