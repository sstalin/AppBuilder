package xj.mobile.codegen.templates

import xj.mobile.model.properties.PropertyType

import static xj.mobile.codegen.CodeGenerator.InjectionPoint.*

import static org.codehaus.groovy.ast.ClassHelper.*


class IOSDefaultViewTemplates { 

  static templates = [

	//
	// handling keyboard 
	// 

	keyboard: [
	  [ 
		methodDeclaration: '- (void)registerForKeyboardNotifications;'
	  ], 
	  [
		loadView: '''[self registerForKeyboardNotifications];
UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]
                               initWithTarget:self
                               action:@selector(dismissKeyboard)];
[self.view addGestureRecognizer:tap];
activeField = nil;'''
	  ],
	  [
		variableDeclaration: 'UITextField* activeField;'
	  ],
	  [
		method: '''-(void)dismissKeyboard {
    [self.view endEditing:YES];
}

- (void)registerForKeyboardNotifications
{
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWasShown:)
                                                 name:UIKeyboardDidShowNotification object:nil];    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWillBeHidden:)
                                                 name:UIKeyboardWillHideNotification object:nil];    
}

- (void)keyboardWasShown:(NSNotification*)aNotification
{
    NSDictionary* info = [aNotification userInfo];
    CGSize kbSize = [[info objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size;
    
    UIEdgeInsets contentInsets = UIEdgeInsetsMake(0.0, 0.0, kbSize.height, 0.0);
    UIScrollView* scrollView = (UIScrollView*) self.view;
    scrollView.contentInset = contentInsets;
    scrollView.scrollIndicatorInsets = contentInsets;
    
    CGRect aRect = self.view.frame;
    aRect.size.height -= kbSize.height;
    if (!CGRectContainsPoint(aRect, activeField.frame.origin) ) {
        CGPoint scrollPoint = CGPointMake(0.0, activeField.frame.origin.y-kbSize.height);
        [scrollView setContentOffset:scrollPoint animated:YES];
    }
}

- (void)keyboardWillBeHidden:(NSNotification*)aNotification
{
    UIEdgeInsets contentInsets = UIEdgeInsetsZero;
    UIScrollView* scrollView = (UIScrollView*) self.view;
    scrollView.contentInset = contentInsets;
    scrollView.scrollIndicatorInsets = contentInsets;
}

- (void)textFieldDidBeginEditing:(UITextField *)textField
{
    activeField = textField;
}

- (void)textFieldDidEndEditing:(UITextField *)textField
{
    activeField = nil;
}'''
	  ]
	],

	// handle closure declaration 

	closureParam: [
	  code: { params.collect{ p -> ":(${p.typeName}) ${p.name}" }.join(' ') }
	],

	closureDecl: [
	  method: '''-(${type}) ${name}${params}
{
${indent(body)}
}
'''
	],

	//
	// handle embedded view
	//

	embeddedView: [
	  loadView: '''${name} = [[${viewClass} alloc] init];
${name}.view.frame = CGRectMake(${frame});
${name}.owner = self;
[self.view addSubview: ${name}.view];
'''
	],

	//
	// handle radio group 
	//

	radioGroup: [
	  [
		declaration: 'NSArray *${name};'
	  ],
	  [
		loadView: '''${name} = @[ ${members.join(', ')} ];
'''
		//code: '''${name} = [NSArray arrayWithObjects: ${members.join(', ')}, nil];

	  ]
	],

	//
	// handle shake gesture 
	//

	onShake: [
	  [
		method: '''- (void)motionEnded:(UIEventSubtype)motion withEvent:(UIEvent *)event
{	
${indent(code, 1)}
}
'''
	  ],
	  [
		do: { 
		  canBecomeFirstResponder = true 
		}
	  ]
	],

	//
	// handle interface orientations
	//

	orientations: [
	  [ 
		projectInfo: { 
		'''\t<key>UISupportedInterfaceOrientations</key>
\t<array>''' + orientations.collect{ "\n\t\t<string>${it.toIOSString()}</string>\n" }.join() + 
'''\t</array>'''
		}
	  ],
	  [ 
		when: { supportedOrientationsMethod },
		method: { '''- (NSUInteger)supportedInterfaceOrientations {
    return ''' + orientations.collect{ "UIInterfaceOrientationMask${it.name}" }.join(' | ') + ''';
}'''
		}
	  ]
	],

	onOrientationChange: [
	  method: '''- (void)willAnimateRotationToInterfaceOrientation:(UIInterfaceOrientation)orientation duration:(NSTimeInterval)duration 
{
${indent(code, 1)}
}''',
	  parameters: [ orientation: new PropertyType('InterfaceOrientations') ]
	],

	onDeviceOrientationChange: [
	  [
		method: '''- (void)deviceOrientationChanged:(NSNotification *)notification 
{
    UIDeviceOrientation orientation;
    orientation = [[UIDevice currentDevice] orientation];

${indent(code, 1)}
}''',
		parameters: [ orientation: new PropertyType('DeviceOrientation') ]
	  ],
	  [
		loadView: '''[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(deviceOrientationChanged:) 
                                             name:@"UIDeviceOrientationDidChangeNotification" 
                                           object:nil];
'''
	  ]
	],

	//
	// Handle gestures 
	// 

 	onTap: [
	  [
		when: { hasActionParameters },
		method: '''- (void)${name}_action:(UITapGestureRecognizer *)gesture
{
    CGPoint loc = [gesture locationInView: self.view];
    int x = (int) loc.x;
    int y = (int) loc.y;
${indent(code, 1)}
}''',
		parameters: [ x: int_TYPE, y: int_TYPE ]
	  ],
	  [
		when: { !hasActionParameters },
		method: '''- (void)${name}_action
{
${indent(code, 1)}
}''',
	  ],
	  [
		loadView: '''UITapGestureRecognizer *${name} =
[[UITapGestureRecognizer alloc] initWithTarget:self
                                        action:@selector(${name}_action${hasActionParameters ? \':\' : \'\'})];
${name}.numberOfTapsRequired = ${taps ?: 1};
${name}.numberOfTouchesRequired = ${touches ?: 1};
[self.view addGestureRecognizer:${name}];
'''
	  ]
	],

	afterAction1: [
	  code: '''[self performSelector:@selector(${name}_after)
           withObject:nil
           afterDelay:${delay.getValueInSecond()}];'''
	],
	afterAction2: [
	  method: '''- (void)${name}_after
{
${indent(code, 1)}
}'''
	],
	
	onSwipe: [
	  [
		method: '''- (void)${name}_action
{
${indent(code, 1)}
}'''
	  ],
	  [
		loadView: '''UISwipeGestureRecognizer *${name} =
[[UISwipeGestureRecognizer alloc] initWithTarget:self
                                          action:@selector(${name}_action)];
${name}.direction = ${direction ?: \'UISwipeGestureRecognizerDirectionRight\'};
[self.view addGestureRecognizer:${name}];
'''
	  ]
	],

	onPinch: [
	  [
		method: '''- (void)${name}_action:(UIPinchGestureRecognizer *)gesture
{
    float scale = gesture.scale;
    CGPoint loc0 = [gesture locationOfTouch: 0 inView: self.view];
    int focusX = (int) loc0.x;
    int focusY = (int) loc0.y;
    if ([gesture numberOfTouches] > 1) {
        CGPoint loc1 = [gesture locationOfTouch: 1 inView: self.view];
        focusX = (int) ((loc0.x + loc1.x) / 2);
        focusY = (int) ((loc0.y + loc1.y) / 2);
    }
${indent(code, 1)}
}''',
		parameters: [ scale: float_TYPE,
					  focusX: int_TYPE, focusY: int_TYPE ]
	  ],
	  [
		loadView: '''UIPinchGestureRecognizer *${name} =
[[UIPinchGestureRecognizer alloc] initWithTarget:self
                                          action:@selector(${name}_action:)];
[self.view addGestureRecognizer:${name}];
'''
	  ],

	  /*
${name}.delegate = self;

	  [
		protocol: 'UIGestureRecognizerDelegate'
	  ]
	  */
	],

	onRotation: [
	  [
		method: '''- (void)${name}_action:(UIRotationGestureRecognizer *)gesture
{
    float rotation = gesture.rotation;
    CGPoint loc0 = [gesture locationOfTouch: 0 inView: self.view];
    int focusX = (int) loc0.x;
    int focusY = (int) loc0.y;
    if ([gesture numberOfTouches] > 1) {
        CGPoint loc1 = [gesture locationOfTouch: 1 inView: self.view];
        focusX = (int) ((loc0.x + loc1.x) / 2);
        focusY = (int) ((loc0.y + loc1.y) / 2);
    }
${indent(code, 1)}
}''',
		parameters: [ rotation: float_TYPE,
					  focusX: int_TYPE, focusY: int_TYPE ]
	  ],
	  [
		loadView: '''UIRotationGestureRecognizer *${name} =
[[UIRotationGestureRecognizer alloc] initWithTarget:self
                                             action:@selector(${name}_action:)];
[self.view addGestureRecognizer:${name}];
'''
	  ],

	  /*
${name}.delegate = self;
	  [
		protocol: 'UIGestureRecognizerDelegate'
	  ]
	  */
	],

	onLongPress: [
	  [
		when: { hasActionParameters },
		method: '''- (void)${name}_action:(UILongPressGestureRecognizer *)gesture
{
    CGPoint loc = [gesture locationInView: self.view];
    int x = (int) loc.x;
    int y = (int) loc.y;
${indent(code, 1)}
}''',
		parameters: [ x: int_TYPE, y: int_TYPE ]
	  ],
	  [
		when: { !hasActionParameters },
		method: '''- (void)${name}_action
{
${indent(code, 1)}
}'''
	  ],
	  [
		loadView: '''UILongPressGestureRecognizer *${name} =
[[UILongPressGestureRecognizer alloc] initWithTarget:self
                                              action:@selector(${name}_action${hasActionParameters ? \':\' : \'\'})];
[self.view addGestureRecognizer:${name}];
'''
	  ]
	],

	onDrag: [
	  [
		when: { hasActionParameters },
		method: '''- (void)${name}_action:(UIPanGestureRecognizer *)gesture
{
    CGPoint loc = [gesture locationInView: self.view];
    int x = (int) loc.x;
    int y = (int) loc.y;
    CGPoint tr = [gesture translationInView: self.view];
    float distanceX = tr.x;
    float distanceY = tr.y;
${indent(code, 1)}
}''',
		parameters: [ x: int_TYPE, y: int_TYPE,
					  distanceX: float_TYPE, distanceY: float_TYPE ]
	  ],
	  [
		when: { !hasActionParameters },
		method: '''- (void)${name}_action
{
${indent(code, 1)}
}'''
	  ],
	  [
		loadView: '''UIPanGestureRecognizer *${name} =
[[UIPanGestureRecognizer alloc] initWithTarget:self
                                        action:@selector(${name}_action${hasActionParameters ? \':\' : \'\'})];'''
	  ],
	  [
		when: { touches != null }, 
		loadView: '${name}.minimumNumberOfTouches = ${touches};'
	  ],
	  [
		loadView: '''[self.view addGestureRecognizer:${name}];
'''
	  ],

	],
	
  ]

}