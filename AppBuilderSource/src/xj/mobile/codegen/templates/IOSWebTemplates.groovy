package xj.mobile.codegen.templates

import static xj.mobile.codegen.CodeGenerator.InjectionPoint.*

class IOSWebTemplates { 

  static templates = [
	web1: [
	  method: '''- (void)webViewDidStartLoad:(UIWebView *)webView 
{
\t// starting the load, show the activity indicator in the status bar
\t[UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
}

- (void)webViewDidFinishLoad:(UIWebView *)webView 
{
\t// finished loading, hide the activity indicator in the status bar
\t[UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
}

- (void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error 
{
\t// load error, hide the activity indicator in the status bar
\t[UIApplication sharedApplication].networkActivityIndicatorVisible = NO;

\t// report the error inside the webview
\tNSString* errorString = [NSString stringWithFormat:
\t\t\t@\"<html><center><font size=+5 color='red'>An error occurred:<br>%@</font></center></html>\",
\t\t\terror.localizedDescription];
\t[${name} loadHTMLString:errorString baseURL:nil];
}
'''
	],
  ];

}