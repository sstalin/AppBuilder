package xj.mobile.codegen.templates

import static xj.mobile.codegen.CodeGenerator.InjectionPoint.*

class IOSPickerTemplates { 

  static templates = [
	picker1: [
	  declaration: 'NSArray *${var};' 
	],
	picker2: [
	  loadView: '${var} = ${data};'
	],
	picker3: [
	  method: '''- (void)pickerView:(UIPickerView *)pickerView didSelectRow: (NSInteger)row inComponent:(NSInteger)component 
{
${indent(actionCode, 1)}
}
'''
	],
	picker4: [
	  method: '''- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component 
{
${indent(code, 1)}
}
'''
	],
	picker5: [
	  method: '''- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView 
{
    return ${numComponents};
}
'''
	],
	picker6: [
	  method: '''- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component 
{
${indent(code, 1)}
}
'''
	],
	picker7: [
	  method: '''// tell the picker the width of each row for a given component
- (CGFloat)pickerView:(UIPickerView *)pickerView widthForComponent:(NSInteger)component 
{
    int sectionWidth = ${300 / numComponents};
    return sectionWidth;
}
'''
	],
  ];

}