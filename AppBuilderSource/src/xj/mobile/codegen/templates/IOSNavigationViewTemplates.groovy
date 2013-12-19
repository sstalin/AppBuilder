package xj.mobile.codegen.templates

import static xj.mobile.codegen.CodeGenerator.InjectionPoint.*

class IOSNavigationViewTemplates { 

  static templates = [
	init: [
	  location: LoadView, 
	  code:  '''[self pushViewController:${viewname} animated:NO];
'''
	],
	subview1: [
	  location: LoadView, 
	  code: 'self.title = @\"${title}\";\n'
	],
	subview2: [
	  location: LoadView, 
	  code: '''UIBarButtonItem *backButtonItem = [[UIBarButtonItem alloc] 
                                    initWithTitle:@\"${backButtonText}\" style:UIBarButtonItemStyleBordered 
                                    target:nil action:nil];
self.navigationItem.backBarButtonItem = backButtonItem;
'''	
	],
	subview3: [
	  location: LoadView, 
	  code: '''UIBarButtonItem *rightButtonItem = [[UIBarButtonItem alloc] 
                                    initWithBarButtonSystemItem:${systemButton.toIOSString()} 
                                    target:${target} action:${action}];
self.navigationItem.rightBarButtonItem = rightButtonItem;
'''
	],
  ];

}