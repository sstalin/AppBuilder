package xj.mobile.model.impl

import xj.mobile.common.AppGenerator

import xj.mobile.codegen.templates.AndroidTemplates
import xj.mobile.codegen.CodeGenerator.InjectionPoint

import static xj.mobile.codegen.CodeGenerator.InjectionPoint.*
import static xj.mobile.util.CommonUtil.*

//
// represent an activity class in Android   
//
class ActivityClass extends AndroidClass { 

  String onCreateScrap = ''
  String onCreateTailScrap = ''
  String popupActionScrap = ''

  String title
  String orientation

  Map attributes = [:] // activity attributes in manifest 

  // handling gestures 
  boolean useSupportLibrary = true
  Map gestureListener = [:]

  static final ACTIVITY_IMPORTS = [ 
	'android.app.*', 
	'android.widget.*', 
	'android.view.*', 
	'android.os.Bundle' 
  ]

  ActivityClass(String viewName) { 
	super(viewName, 'Activity')

	imports += ACTIVITY_IMPORTS

	templates = AndroidTemplates.getInstance()
  }

  String getOnCreateBodyScrap() { 
	onCreateScrap + onCreateTailScrap
  }

  //
  //  Building components 
  //

  public void injectCode(InjectionPoint location, String code, Map binding = null) { 
	if (location && code) { 
	  switch (location) {
	  case Creation:
		onCreateScrap += "\n${code}"
		break;
	  case PostCreation: 
		onCreateTailScrap += "\n${code}"
		break;

	  case AuxiliaryClass:
		auxiliaryClasses << code
		break;

	  case OnTap:
		injectGestureHandlingCode('onSingleTapConfirmed', code)
		break; 
	  case OnDoubleTap: 
		injectGestureHandlingCode('onDoubleTap', code)
		break; 
	  case OnFling: 
		injectGestureHandlingCode('onFling', code)
		break; 
	  case OnLongPress: 
		injectGestureHandlingCode('onLongPress', code)
		break; 
	  case OnScale:
		injectGestureHandlingCode('onScale', code)
		break; 
	  case OnRotate: 
		injectGestureHandlingCode('onRotate', code)
		break; 
	  case OnDrag:
		injectGestureHandlingCode('onScroll', code)
		break; 
	  default:
		super.injectCode(location, code, binding)
	  }
	}
  }

  public void process() { 
	super.process()
	if (title) { 
	  resources.stringResources["${name}_title".toString()] = title
	}

	handleGestureListener()

	methodScrap = """/** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
${indent(onCreateBodyScrap)}
}
""" + methodScrap + '\n' + popupActionScrap

  }

  void injectGestureHandlingCode(String gesture, String code) { 
	if (gestureListener[gesture]) { 
	  gestureListener[gesture] << code 
	} else { 
	  gestureListener[gesture] = [ code ]
	}
  } 

  static GestureListenerMethodSignatures = [
    onDown: 'public boolean onDown(MotionEvent event)', 
    onFling: 'public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY)',
    onLongPress: 'public void onLongPress(MotionEvent event)',
    onScroll: 'public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX, float distanceY)',
    onShowPress: 'public void onShowPress(MotionEvent event)',
    onSingleTapUp: 'public boolean onSingleTapUp(MotionEvent event)',
    onDoubleTap: 'public boolean onDoubleTap(MotionEvent event)',
    onDoubleTapEvent: 'public boolean onDoubleTapEvent(MotionEvent event)',
    onSingleTapConfirmed: 'public boolean onSingleTapConfirmed(MotionEvent event)',
  ]

  boolean isNeedSupportLibrary() { 
	useSupportLibrary && gestureListener
  }

  void handleGestureListener() { 
	def detectorClasses = []
	def onTouchEventCode = []

	if (GestureListenerMethodSignatures.keySet().any { k -> gestureListener.containsKey(k) }) { 
	  String detectorClass = 'GestureDetector'
	  String detectorVar = 'detector'
	  if (useSupportLibrary) { 
		imports << 'android.support.v4.view.GestureDetectorCompat'
		detectorClass = 'GestureDetectorCompat'
	  }
	
	  declarationScrap += "\nprivate ${detectorClass} ${detectorVar};"
	  onCreateScrap += "\n${detectorVar} = new ${detectorClass}(this, new MyGestureListener());"
	
	  def methods = []
	  for (m in GestureListenerMethodSignatures.keySet()) { 
		if (gestureListener[m]) { 
		  String preamble = ''
		  String code = gestureListener[m].join('\n') 
		  if (m == 'onFling') { 
			declarationScrap += '''

public static final int SwipeGestureDirectionDown  = 1; 
public static final int SwipeGestureDirectionUp    = 1 << 1; 
public static final int SwipeGestureDirectionLeft  = 1 << 2; 
public static final int SwipeGestureDirectionRight = 1 << 3; 
'''
			preamble = '''int direction = 0;
if (Math.abs(velocityX) > Math.abs(velocityY)) {
    if (velocityX > 0) direction = SwipeGestureDirectionRight;
    else direction = SwipeGestureDirectionLeft;
} else {
    if (velocityY > 0) direction = SwipeGestureDirectionDown;
    else direction = SwipeGestureDirectionUp;
}
''' 
		  }
		  String eventVar = 'event'
		  if (m == 'onScroll') eventVar = 'event2'
		  else if (m == 'onFling') eventVar = 'event1'

		  preamble += """float x = ${eventVar}.getX();
float y = ${eventVar}.getY();
""" 
		  code = preamble + code + (m != 'onLongPress' ? '\nreturn true;' : '') 		  
		  methods << """${GestureListenerMethodSignatures[m]} {
${indent(code)}
}"""
		}
	  }
	  onTouchEventCode << "this.${detectorVar}.onTouchEvent(event);"
	  String listenerCode = methods.join('\n\n')
	  detectorClasses << """class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
${indent(listenerCode)}
}
"""
	}

	if (gestureListener.containsKey('onScale')) { 
	  String detectorClass = 'ScaleGestureDetector'
	  String detectorVar = 'scaleDetector'
	  String code = gestureListener['onScale'].join('\n') 
	  declarationScrap += "\nprivate ${detectorClass} ${detectorVar};"
	  onCreateScrap += "\n${detectorVar} = new ${detectorClass}(this, new MyScaleGestureListener());"
	  onTouchEventCode << "this.${detectorVar}.onTouchEvent(event);"
	  detectorClasses << """class MyScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
		float scale = detector.getScaleFactor();
		float focusX = detector.getFocusX();
		float focusY = detector.getFocusY();
${indent(code, 2)}
        return true;
    }
}
"""
	}

	if (gestureListener.containsKey('onRotate')) {
	  String detectorClass = 'RotateGestureDetector'
	  String detectorVar = 'rotateDetector'
	  auxiliaryClasses << detectorClass

	  String code = gestureListener['onRotate'].join('\n') 
	  declarationScrap += "\nprivate ${detectorClass} ${detectorVar};"
	  onCreateScrap += "\n${detectorVar} = new ${detectorClass}(this, new MyRotateGestureListener());"
	  onTouchEventCode << "this.${detectorVar}.onTouchEvent(event);"
	  detectorClasses << """class MyRotateGestureListener extends RotateGestureDetector.SimpleOnRotateGestureListener {
    @Override
    public boolean onRotate(RotateGestureDetector detector) {
		float rotation = detector.getRotation();
		float focusX = detector.getFocusX();
		float focusY = detector.getFocusY();
${indent(code, 2)}
        return true;
    }
}
"""
	}


	if (onTouchEventCode) { 
	  String code = onTouchEventCode.join('\n')
	  String classDef = detectorClasses.join('\n\n')
	  methodScrap +=  """
@Override 
public boolean onTouchEvent(MotionEvent event){ 
${indent(code)}
    return super.onTouchEvent(event);
}

${classDef}"""
	}
  }

}