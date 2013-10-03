package xj.mobile.lang

class WidgetMap {

  static widgets = [
    Label :       [ ios     : 'Label', 
					android : 'TextView' ],
    Image :       [ ios     : 'ImageView', 
					android : 'ImageView' ],
    Button :      [ ios     : 'Button', 
					android : 'Button' ],
    Text :        [ ios     : 'TextField', 
					android : 'EditText' ],
    Switch :      [ ios     : 'Switch',
					android : 'ToggleButton' ],
    Slider :      [ ios     : 'Slider', 
					android : 'SeekBar' ], 
    Selection :   [ ios     : 'SegmentedControl',
					android : 'Spinner' ], 
	NumberStepper:[ ios     : 'Stepper',
					android : 'NumberStepper' ],
    ProgressBar : [ ios     : 'ProgressView', 
					android : 'ProgressBar' ],
    Picker :      [ ios     : 'PickerView', 
					android : 'Spinner' ],
    DatePicker :  [ ios     : 'DatePicker',
					android : 'DatePicker' ],
    TimePicker :  [ ios     : 'TimePicker', 
					android : 'TimePicker' ],
    Web :         [ ios     : 'WebView',
					android : 'WebView' ],
    Map :         [ ios     : 'MapView', 
					android : 'MapView' ],

	ImageButton:  [ ios     : 'Button',
					android : 'ImageButton' ],

    Popup :       [ ios     : 'ActionSheet', 
					android : 'AlertDialog' ],
    Alert :       [ ios     : 'AlertView', 
					android : 'AlertDialog'],

    Menu :        [ ios     : 'ActionSheet', 
					android : [ 'AlertDialog', 'Menu' ] ],  

    View :        [ ios     : 'Control', 
					android : 'View'],
  ]

  static String getPlatformWidgetName(name, platform) { 
	def pname = null
	if (name && widgets[name]) { 
	  pname = widgets[name][platform]
	  if (pname && pname instanceof List) { 
		pname = pname[0]
      }
	}
	if (!pname) pname = name
	return pname
  }

  static getNativeWidgetName(wname, platform) { 
	if (wname && platform) { 
	  switch (platform) { 
	  case 'ios': return getIOSNativeWidgetName(wname);
	  case 'android': return getAndroidNativeWidgetName(wname);
	  }
	}
	return null
  }

  static getIOSNativeWidgetName(wname) { 
	if (wname) { 
	  if (wname == 'MapView') { 
		return 'MK' + wname
	  } else { 
		return 'UI' + wname
	  }
	}
	return null
  }

  static getAndroidPackageName(wname) { 
	def pkgnames = [
	  'Menu'        : 'android.view',
	  'View'        : 'android.view',
	  'AlertDialog' : 'android.app',
	  'WebView'     : 'android.webkit',
	  'MapView'     : 'com.google.android.maps',

	  'NumberStepper' : '__CUSTOM__',
	]
	if (wname) { 
	  def pkg = pkgnames[wname]
	  if (pkg) 
		return pkg
	  else  
		return 'android.widget'
	}
	return null
  }

  static getAndroidNativeWidgetName(wname) { 
	if (wname) { 
	  return getAndroidPackageName(wname) + '.' + wname
	}
	return null
  }

  static widgets_ios = [
    Label :            [],
    ImageView :        [],
    Button :           [],
    TextField :        [],
    Switch :           [],
    Slider :           [],
    SegmentedControl : [],
	Stepper :          [],
    ProgressView :     [],
    PickerView :       [],
    DatePicker :       [],  // UIDatePicker + UIDatePickerModeDate
    TimePicker :       [],  // UIDatePicker + UIDatePickerModeTime
    WebView :          [],
    MapView :          [],

    ActionSheet :      [],
    AlertView :        [],
  ]

  static widgets_android = [
    TextView :     [],
    ImageView :    [],
    ImageButton :  [],
    Button :       [],
    EditText :     [],
    ToggleButton : [],
    Slider :       [],
    CheckBox :     [],
    RadioButton :  [],
    Spinner :      [],
    ProgressBar :  [],
    DatePicker :   [],   
    TimePicker :   [],  
    WebView :      [],
    MapView :      [],

    AlertDialog :  [],
  ]

}  