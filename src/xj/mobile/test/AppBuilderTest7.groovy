package xj.mobile.test

import org.junit.*
import static org.junit.Assert.*

import xj.mobile.Main

class AppBuilderTest7 extends AppBuilderTest {  

  def iOSFileMap = [
    'Hello01'  : [ name: 'Hello World', 
				   views: [ 'View1' ] ], 
    'Hello01a' : [ name: 'Hello World 1a', 
				   views: [ 'View1' ],
				   icon: 'icon2' ], 
    'Hello01b' : [ name: 'Hello World 1b', 
				   views: [ 'View1' ] ],
	'Hello02'  : [ name: 'Welcome', 
				   views: [ 'View1' ] ], 
	'Hello02a' : [ name: 'Welcome 2a', 
				   views: [ 'View1' ] ], 
    'Hello03'  : [ name: 'Hello 3', 
				   views: [ 'Top' ] ], 

  ];

  def androidFileMap = [
    'Hello01'  : [ name: 'Hello World', 
				   views: [ 'HelloWorld' ], 
				   layouts: [ 'main' ] ], 
    'Hello01a' : [ name: 'Hello World 1a', 
				   views: [ 'HelloWorld1a' ], 
				   layouts: [ 'main' ] ], 
    'Hello01b' : [ name: 'Hello World 1b', 
				   views: [ 'HelloWorld1b' ], 
				   layouts: [ 'main' ] ], 
    'Hello02'  : [ name: 'Welcome', 
				   views: [ 'Welcome' ], 
				   layouts: [ 'main' ] ], 
    'Hello02a' : [ name: 'Welcome 2a', 
				   views: [ 'Welcome2a' ], 
				   layouts: [ 'main' ] ], 
    'Hello03'  : [ name: 'Hello 3', 
				   views: [ 'Hello3' ], 
				   layouts: [ 'main' ] ], 
  ];
  
  @Test(timeout=80000L)
  public void test_Hello01_iOS() {
    test_iOS('Hello01')
  }

  @Test(timeout=80000L)
  public void test_Hello01_Android() {
    test_Android('Hello01')
  }

  @Test(timeout=80000L)
  public void test_Hello01a_iOS() {
    test_iOS('Hello01a')
  }

  @Test(timeout=80000L)
  public void test_Hello01a_Android() {
    test_Android('Hello01a')
  }

  @Test(timeout=80000L)
  public void test_Hello01b_iOS() {
    test_iOS('Hello01b')
  }

  @Test(timeout=80000L)
  public void test_Hello01b_Android() {
    test_Android('Hello01b')
  }

  @Test(timeout=80000L)
  public void test_Hello02_iOS() {
    test_iOS('Hello02')
  }

  @Test(timeout=80000L)
  public void test_Hello02_Android() {
    test_Android('Hello02')
  }

  @Test(timeout=80000L)
  public void test_Hello02a_iOS() {
    test_iOS('Hello02a')
  }

  @Test(timeout=80000L)
  public void test_Hello02a_Android() {
    test_Android('Hello02a')
  }

  @Test(timeout=80000L)
  public void test_Hello03_iOS() {
    test_iOS('Hello03')
  }

  @Test(timeout=80000L)
  public void test_Hello03_Android() {
    test_Android('Hello03')
  }


}