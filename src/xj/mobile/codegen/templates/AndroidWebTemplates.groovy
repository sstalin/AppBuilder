package xj.mobile.codegen.templates

import xj.mobile.model.properties.PropertyType

import static xj.mobile.codegen.CodeGenerator.InjectionPoint.*

import static org.codehaus.groovy.ast.ClassHelper.*

class AndroidWebTemplates { 

  static templates = [
	web1: [
	  [
		import: 'android.webkit.WebView'
	  ],
	  [
		import: 'android.webkit.WebViewClient'
	  ],
	  [
		method: '''private class MyWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
'''
	  ],
	  [
		creation: '${name}.setWebViewClient(new MyWebViewClient());\n'
	  ],
	  [
		method: '''@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
    if ((keyCode == KeyEvent.KEYCODE_BACK) && ${name}.canGoBack()) {
        ${name}.goBack();
        return true;
    }
    return super.onKeyDown(keyCode, event);
}
'''
	  ],
	]
  ]

}