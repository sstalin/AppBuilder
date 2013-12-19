package xj.mobile.codegen.templates

import static xj.mobile.codegen.CodeGenerator.InjectionPoint.*

class IOSListViewTemplates { 

  static templates = [
	init: [
	  method: '''-(id) init 
{
    if (self = [super ${superCall}]) {
${indent(body, 2)}
    }
    return self;
}
'''
	],

	section1: [
	  method: '''- (NSInteger) numberOfSectionsInTableView: (UITableView *)tableView 
{
    // Return the number of sections.
    return ${count};
}
'''
	],
	section2: [
	  method: '''- (NSString *) tableView: (UITableView *)tableView titleForHeaderInSection: (NSInteger)section 
{
${indent(title, 1)}
}
'''
	],
	section3: [
	  method: '''- (NSInteger) tableView: (UITableView *)tableView numberOfRowsInSection: (NSInteger)section 
{
${indent(rows, 1)}
}
'''
	],

	cell1: [
	  code: '''
if ((NSNull *) detailText != [NSNull null]) {
\tcell.detailTextLabel.text = detailText;
} else {
\tcell.detailTextLabel.text = nil;
}
'''
	],
	cell2: [
	  code: '''
if ((NSNull *) imageFile != [NSNull null]) {
\tcell.imageView.image = [UIImage imageNamed:imageFile];
} else {
\tcell.imageView.image = nil;
}
'''
	],
	cell3: [
	  code: '''${cellID}
UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellId];
if (cell == nil) {
${indent(cellInit, 1)}
}
    
// Configure the cell...
${getData}
${text}
${detailText}${cellImage}${cellAccessory}return cell;'''
	],
	cell4: [
	  method: '''- (UITableViewCell *) tableView:(UITableView *)tableView cellForRowAtIndexPath: (NSIndexPath *)indexPath 
{
${indent(cellScrap, 1)}
}
'''
	],

	selection: [
	  method: '''- (void) tableView:(UITableView *)tableView didSelectRowAtIndexPath: (NSIndexPath *)indexPath 
{
${indent(selectionCode, 1)}
}
'''
	],

	longpress1: [
	  declaration: 'NSIndexPath *${var};'
	],
	longpress2: [
	  code: '${var} = indexPath;\n'
	],
	longpress3: [
	  loadView: '''UILongPressGestureRecognizer *lpgr = [[UILongPressGestureRecognizer alloc] 
                                      initWithTarget:self action:@selector(handleLongPress:)];
lpgr.minimumPressDuration = 1.0; //seconds
lpgr.delegate = self;
[self.tableView addGestureRecognizer:lpgr];
'''
	],
	longpress4: [
	  code: '''CGPoint p = [gestureRecognizer locationInView:self.tableView];   
NSIndexPath *indexPath = [self.tableView indexPathForRowAtPoint:p];
${indexPath}'''
	],
	longpress5: [
	  method: '''-(void)handleLongPress:(UILongPressGestureRecognizer *)gestureRecognizer
{
    if (gestureRecognizer.state == UIGestureRecognizerStateBegan) {
${indent(actionCode, 2)}
    }
}
'''
	],

	
  ];

}