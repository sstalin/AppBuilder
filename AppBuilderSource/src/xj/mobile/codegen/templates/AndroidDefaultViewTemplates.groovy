package xj.mobile.codegen.templates

import xj.mobile.model.properties.PropertyType

import static xj.mobile.codegen.CodeGenerator.InjectionPoint.*

import static org.codehaus.groovy.ast.ClassHelper.*

class AndroidDefaultViewTemplates { 

  static templates = [

	//
	// handle closure declaration
	//

	closureParam: [
	  code: { params.collect{ p -> "${p.typeName} ${p.name}" }.join(', ') }
	],

	closureDecl: [
	  method: '''${type} ${name}(${params}) {
${indent(body)}
}
'''
	],

	//
	// handle spinner 
	//

	spinner1: [
	  creation: '''AdapterView.OnItemSelectedListener selectionListener = new AdapterView.OnItemSelectedListener() {

    String[] items = { ${items} };

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
${indent(selectCode, 2, '    ')}
${indent(actionCode, 2, '    ')}
    }

    public void onNothingSelected(AdapterView parent) { }

};
'''
	],

	spinner2: [
	  creation: '${name}.setOnItemSelectedListener(selectionListener);'
	], 

	//
	// handle slider 
	//

	slider: [
	  creation: '''${name}.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
${indent(actionCode, 2, '    ')}     
    }

    public void onStartTrackingTouch(SeekBar seekBar) {}
    public void onStopTrackingTouch(SeekBar seekBar) {}
});
'''
	],


	//
	// handle interface orientations
	//

	onOrientationChange: [
	  [ 
		method: '''
public void onConfigurationChanged(Configuration config) {
    super.onConfigurationChanged(config);
${indent(code, 1)}
}''',
		parameters: [ orientation: new PropertyType('InterfaceOrientations') ]
	  ],
	  [
		import: 'android.content.res.Configuration'
	  ],
	  [
		do: { attributes[ 'android:configChanges'] = 'orientation|keyboardHidden' }
	  ]
	],

	//
	// Handle gestures 
	// 

	onTap: [
	  [ 
		onTap: '${code}',
		parameters: [ x: float_TYPE, y: float_TYPE ]
	  ],
	  /*
	  [
		when: { touches != null },
		onTap: '''if (event.getPointerCount() >= ${touches}) {
${indent(code)}
}'''
	  ],
	  */
	],

	onDoubleTap: [
	  [ 
		onDoubleTap: '${code}',
		parameters: [ x: float_TYPE, y: float_TYPE ]
	  ]
	],

	afterAction1: [
	  code: '''findViewById(android.R.id.content).postDelayed(new Runnable() {
    public void run() {
${indent(code, 2)}
	}										
}, ${delay.getValueInMilli()});'''
	],

	onSwipe: [
	  [ 
		onFling: '''if ((direction & (${direction ?: \'SwipeGestureDirectionRight\'})) != 0) {
${indent(code)}
}'''

	  ]
	], 

	onPinch: [
	  onScale: '${code}',
	  parameters: [ scale: float_TYPE,
					focusX: float_TYPE, focusY: float_TYPE ]
	],

	onRotation: [
	  onRotate: '${code}',
	  parameters: [ rotation: float_TYPE,
					focusX: float_TYPE, focusY: float_TYPE  ]
	],

	onLongPress: [
	  onLongPress: '${code}',
	  parameters: [ x: float_TYPE, y: float_TYPE ]
	],

	onDrag: [
	  [
		when: { touches == null },
		onDrag: '${code}',
		parameters: [ x: float_TYPE, y: float_TYPE, 
					  distanceX: float_TYPE, distanceY: float_TYPE ]
	  ],
	  [
		when: { touches != null },
		onDrag: '''if (event2.getPointerCount() >= ${touches}) {
${indent(code)}
}''',
		parameters: [ x: float_TYPE, y: float_TYPE, 
					  distanceX: float_TYPE, distanceY: float_TYPE ]
	  ],
	],

  ]

}