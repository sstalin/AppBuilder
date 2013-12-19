package xj.mobile.test

import org.junit.*
import static org.junit.Assert.*

import xj.mobile.api.FrameworkTypeInfo
import xj.mobile.api.APIResolver

import org.codehaus.groovy.ast.ClassNode
import static org.codehaus.groovy.ast.ClassHelper.*

class FrameworkTypeInfoTest { 

  FrameworkTypeInfo frameworkTypeInfo = new FrameworkTypeInfo()
    
  def iosAPIResolver = APIResolver.getAPIResolver('iOS')
  def androidAPIResolver = APIResolver.getAPIResolver('Android')

  @Test
  void test1() { 
    assert frameworkTypeInfo.getPropertyType('Label', 'text') == STRING_TYPE
    assert frameworkTypeInfo.getPropertyType('Button', 'text') == STRING_TYPE
    assert frameworkTypeInfo.getPropertyType('RadioButton', 'text') == STRING_TYPE

    assert frameworkTypeInfo.getPropertyType('CheckBox', 'checked') == boolean_TYPE
  }

  @Test
  void test2() { 
	println "Test iosAPIResolver"

	def pd1 = iosAPIResolver.findPropertyDef('UIButton', 'buttonType', false) 
	println "UIButton.buttonType ==> ${pd1?.type}"
	assert pd1.type == 'UIButtonType'

	def pd2 = iosAPIResolver.findPropertyDef('UIButton', 'currentTitle', false) 
	println "UIButton.currentTitle ==> ${pd2?.type}"
	assert pd2.type == 'NSString'

	def pd3 = iosAPIResolver.findPropertyDef('UIButton', 'enabled', false) 
	println "UIButton.enabled ==> ${pd3?.type}"
	assert pd3.type == 'BOOL'

	def pd4 = iosAPIResolver.findPropertyDef('UIStepper', 'value', false) 
	println "UIStepper.value ==> ${pd4?.type}"
	assert pd4.type == 'double'

  }

  @Test
  void test3() { 
	println "Test androidAPIResolver"

	def pd1 = androidAPIResolver.findPropertyDef('Button', 'cursorVisible') 
	println "Button.cursorVisible ==> ${pd1?.type}"
	assert pd1.type == 'boolean'

	def pd2 = androidAPIResolver.findPropertyDef('Button', 'lines') 
	println "Button.lines ==> ${pd2?.type}"
	assert pd2.type == 'int'

	def pd3 = androidAPIResolver.findPropertyDef('Button', 'alpha') 
	println "Button.alpha ==> ${pd3?.type}"
	assert pd3.type == 'float'

  }

  

}