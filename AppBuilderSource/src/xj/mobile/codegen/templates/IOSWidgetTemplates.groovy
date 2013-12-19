package xj.mobile.codegen.templates

import xj.mobile.lang.WidgetMap

import xj.mobile.*
import xj.mobile.model.ui.*
import xj.mobile.ios.*

import static xj.mobile.util.CommonUtil.*
import static xj.mobile.codegen.IOSUtil.*
import static xj.mobile.model.impl.ClassModel.getCustomViewName

class IOSWidgetTemplates extends WidgetTemplates { 

  IOSWidgetTemplates(target) { 
    super(target)
  }

  ////// Widget Templates  

  def CommonWidgetTemplate = [
    create: '${name} = [[${uiclass} alloc] initWithFrame: CGRectMake(${frame})];',

    setAttribute: '${name}.${attribute} = ${value}',

    getAttribute: '${name}.${attribute}',

    getIndexedAttribute: '${name}.${attribute}[${index}]',

    setFrame: '${name}.frame = CGRectMake(${frame});',

	actionEvent: 'UIControlEventValueChanged',

    action: '''
- (IBAction) ${actionName}:(id) sender 
{
\tNSLog(@"${actionName}");
${indent(actionBody)}
}
''',

    target: '[${name} addTarget:self action:@selector(${actionName}:) forControlEvents:${event}];',

	addSubview: '[self.view addSubview: ${name}];\n',

  ]
  
  def widgetMap = WidgetMap.widgets_ios

  /*
   *  defaultAttributes: the default values of the attributes, 
   *                     always set after a widget is created with the user value or the default value
   *
   *  initialAttributes: the attributes that will be set after a widget is created, if a value is provided  
   */


  def widgetTemplates = [
    Label:  [
      uiclass: 'UILabel',

      initialAttributes: [ 'text' ],

	  defaultAttributes: [
		'backgroundColor': '[UIColor clearColor]'
	  ]
    ],

    ImageView: [
      uiclass: 'UIImageView',

      //create: '${name} = [[UIImageView alloc] initWithImage: [UIImage imageNamed:@"${value}"]];',
	  create: '${name} = [[UIImageView alloc] initWithImage: [UIImage imageNamed:@"${arg0}"]];',

      //initValue: { widget -> widget.file },
	  arg0: { widget -> widget.file },

      needsFrame: true, 

      defaultAttributes: { widget -> 
		widget.scroll ? [:]:
		  [ 'contentMode': 'UIViewContentModeScaleAspectFill',
			'clipsToBounds': 'YES' ] 
	  },
    ],

    Button:  [
      uiclass: 'UIButton',

	  create: '${name} = [UIButton buttonWithType:${arg0}];',	

	  arg0: { widget -> widget.image ? 'UIButtonTypeCustom': 'UIButtonTypeRoundedRect' },

      needsFrame: true, 

      set_text: '[${name} setTitle:${value} forState:UIControlStateNormal]',
      get_text: '[${name} titleForState:UIControlStateNormal]',

	  set_image: '[${name} setImage: [UIImage imageNamed: ${value}] forState:UIControlStateNormal]',

      actionEvent: 'UIControlEventTouchUpInside',

      initialAttributes: [ 'text', 'image' ],
    ],

    CheckBox:  [
      uiclass: 'UIButton',

      create: '''${name} = [UIButton buttonWithType:UIButtonTypeCustom];
[${name} setTitleColor: [UIColor blackColor] forState:UIControlStateNormal];
[${name} setImage:[UIImage imageNamed:@"checkbox-off.png"] forState:UIControlStateNormal];
[${name} setImage:[UIImage imageNamed:@"checkbox-on.png"] forState:UIControlStateSelected];''',

      images: [ 'checkbox-off.png', 'checkbox-on.png' ],

      needsFrame: true, 

      set_text: '[${name} setTitle:${value} forState:UIControlStateNormal]',
      get_text: '[${name} titleForState:UIControlStateNormal]',

      get_checked: '${name}.selected',
      set_checked: '${name}.selected = ${value}',
      
      actionEvent: 'UIControlEventTouchUpInside',

      actionCode: '''UIButton* b = (UIButton*) sender;
b.selected = !b.selected;''',

      initialAttributes: [ 'text' ],

      defaultAttributes: [ 
		'titleEdgeInsets': 'UIEdgeInsetsMake(0, 10, 0, 0)',
		'contentHorizontalAlignment': 'UIControlContentHorizontalAlignmentLeft',
      ],

    ],

    RadioButton:  [
      uiclass: 'UIButton',

      create: '''${name} = [UIButton buttonWithType:UIButtonTypeCustom];
[${name} setTitleColor: [UIColor blackColor] forState:UIControlStateNormal];
[${name} setImage:[UIImage imageNamed:@"radiobutton-off.png"] forState:UIControlStateNormal];
[${name} setImage:[UIImage imageNamed:@"radiobutton-on.png"] forState:UIControlStateSelected];''',

      images: [ 'radiobutton-off.png', 'radiobutton-on.png' ],

      needsFrame: true, 

      set_text: '[${name} setTitle:${value} forState:UIControlStateNormal]',
      get_text: '[${name} titleForState:UIControlStateNormal]',

      actionEvent: 'UIControlEventTouchUpInside',

      actionCode: '''UIButton* b = (UIButton*) sender;
b.selected = !b.selected;''',

      actionCode2: '''${name}.selected = !${name}.selected;
${other}.selected = !${name}.selected;''',

      actionCode3: '''if (!${name}.selected) {
\t${name}.selected = !${name}.selected;
\tfor (UIButton* b in ${group}) {
\t\tif (b != ${name}) b.selected = !${name}.selected;
\t}
}   
''',

      initialAttributes: [ 'text' ],

      defaultAttributes: [ 
		'titleEdgeInsets': 'UIEdgeInsetsMake(0, 10, 0, 0)',
		'contentHorizontalAlignment': 'UIControlContentHorizontalAlignmentLeft',
      ],

      processor: new RadioButtonProcessor(),

    ],

    TextField: [
      uiclass: 'UITextField',

      defaultAttributes: [ 'borderStyle': 'UITextBorderStyleRoundedRect',
							'delegate': 'self' ],

      initialAttributes: [ 'text' ],

      delegate: 'UITextFieldDelegate',

    ], 

    Switch: [
      uiclass: 'UISwitch', 

    ],

    Slider: [
      uiclass: 'UISlider', 

    ],

    SegmentedControl: [
      uiclass: 'UISegmentedControl',

      //create: '${name} = [[UISegmentedControl alloc] initWithItems:${value}];',
	  create: '${name} = [[UISegmentedControl alloc] initWithItems:${arg0}];',

      //initValue: { widget -> toNSArrayWithStrings(widget.options) },
	  arg0: { widget -> toNSArrayWithStrings(widget.options) },

      needsFrame: true, 

      defaultAttributes: [ 'segmentedControlStyle': 'UISegmentedControlStylePlain' ],
 
      get_value: '[${name} titleForSegmentAtIndex:[${name} selectedSegmentIndex]]',
	  get_value_indexed: '[${name} selectedSegmentIndex]',
    ],

	Stepper: [
	  uiclass: 'UIStepper',

	  get_value: '${name}.${attribute}'
	],

	'Stepper#Int': [
	  uiclass: 'UIStepper',

	  get_value: '(int) ${name}.${attribute}'
	],

    ProgressView: [
      uiclass: 'UIProgressView',
    ],

    PickerView: [
      uiclass: 'UIPickerView',

      defaultAttributes: [ 
		'showsSelectionIndicator': 'YES',
		'delegate': 'self'
      ],

      delegate: 'UIPickerViewDelegate',

      processor: new PickerProcessor(),

      get_value: '[${name}Data objectAtIndex:[${name} selectedRowInComponent:0]]',
      get_value_indexed: '[[${name}Data objectAtIndex:${index}] objectAtIndex:[${name} selectedRowInComponent:${index}]]',
    ],

    DatePicker: [
      uiclass: 'UIDatePicker',

      defaultAttributes: [ 
		'datePickerMode': 'UIDatePickerModeDate'
      ],

    ],

    TimePicker: [
      uiclass: 'UIDatePicker',

      defaultAttributes: [ 
		'datePickerMode': 'UIDatePickerModeTime'
      ],

      get_time: '[[${name} date] description]'
    ],

    DateTimePicker: [
      uiclass: 'UIDatePicker',

      get_date_time: '[[${name} date] description]'
    ],

    WebView: [
      uiclass: 'UIWebView',

      set_url: '[${name} loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:${value}]]]',
	  set_html: '[${name} loadHTMLString: ${value} baseURL:nil]',

      defaultAttributes: [
		'scalesPageToFit': 'YES',
		'autoresizingMask': '(UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight)'
      ],

      initialAttributes: [ 'url', 'html' ],

      delegate: 'UIWebViewDelegate',

	  template:  'Web:web1',
    ],

    MapView: [
      uiclass: 'MKMapView',

      framework: 'MapKit.framework', 

      header: 'MapKit/MapKit.h',

      initialAttributes: [ [ 'latlon', 'span' ] ],

      set_latlon_span: '''MKCoordinateRegion newRegion;
newRegion.center.latitude = ${latlon[0]};
newRegion.center.longitude = ${latlon[1]};
newRegion.span.latitudeDelta = ${span[0]};
newRegion.span.longitudeDelta = ${span[1]};
[${name} setRegion:newRegion animated:YES];''',
    ],

	Canvas: [
	  uiclass: { widget -> getCustomViewName(widget.id) },

	  processor: new CanvasProcessor()
	],

  ]



}