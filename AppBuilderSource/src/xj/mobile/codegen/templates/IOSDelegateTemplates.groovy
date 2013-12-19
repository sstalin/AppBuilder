package xj.mobile.codegen.templates

class IOSDelegateTemplates { 

  static delegateTemplates = [
     UITextFieldDelegate : [
       action : '''
- (BOOL)textFieldShouldReturn:(UITextField *)textField 
{
\t[textField resignFirstResponder];
${actionBody}
\treturn YES;
}
''',
       action_var : 'textField'
     ],

     UIActionSheetDelegate : [
       action : '''
- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
${actionBody}
}
''',
       action_var : 'actionSheet'
     ],

     UIAlertViewDelegate : [	
       action : '''
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
${actionBody} 
}
''',
       action_var : 'alertView',
     ],
  ]

}