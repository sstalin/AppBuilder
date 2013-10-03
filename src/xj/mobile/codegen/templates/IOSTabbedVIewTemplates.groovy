package xj.mobile.codegen.templates

import static xj.mobile.codegen.CodeGenerator.InjectionPoint.*

class IOSTabbedViewTemplates { 

  static templates = [
	init: [
	  location: LoadView, 
	  code:  '''\nself.viewControllers = ${toNSArrayWithObjects(views)};'''
	],

	tab1: [
	  location: LoadView,
	  code: '\n${name} = [[${uiclass} alloc] init];'
	],

	sub1: [
	  location: LoadView,
	  code: '${name}.title = @\"${title}\";'
	],
	sub2: [
	  location: LoadView,
	  code: '${name}.tabBarItem.image = [UIImage imageNamed: @\"${image}\"];'
	],

  ];

}